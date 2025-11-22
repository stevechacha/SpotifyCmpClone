package com.chachadev.spotifycmpclone.utils

import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.webkit.ConsoleMessage
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import com.multiplatform.webview.web.NativeWebView
import java.io.ByteArrayInputStream
import java.io.IOException
import java.nio.charset.StandardCharsets

@RequiresApi(Build.VERSION_CODES.O)
actual fun configureWebViewForAuth(webView: NativeWebView) {
    try {
        // Try multiple ways to access Android WebView
        val androidWebView = when {
            webView is WebView -> webView
            webView.javaClass.name.contains("WebView") -> {
                // Try to get the underlying WebView using reflection
                try {
                    val field = webView.javaClass.getDeclaredField("webView")
                    field.isAccessible = true
                    field.get(webView) as? WebView
                } catch (e: Exception) {
                    null
                }
            }
            else -> null
        }
        
        androidWebView?.let { view ->
            // Enable WebView debugging (allows Chrome DevTools remote debugging)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                WebView.setWebContentsDebuggingEnabled(true)
            }
            
            view.settings.apply {
                // Enable DOM storage (localStorage) - critical for Spotify login
                domStorageEnabled = true
                databaseEnabled = true
                // Enable other settings for better form handling
                javaScriptEnabled = true
                javaScriptCanOpenWindowsAutomatically = true
                allowFileAccess = true
                allowContentAccess = true
                // Additional settings for form submission
                @Suppress("DEPRECATION")
                loadWithOverviewMode = true
                @Suppress("DEPRECATION")
                useWideViewPort = true
                // Enable mixed content for better compatibility
                mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                // Set cache mode to ensure proper storage
                cacheMode = WebSettings.LOAD_DEFAULT
            }
            
            // Use addJavascriptInterface to provide localStorage via native code
            // This bypasses the SecurityError by not accessing the blocked localStorage property
            class LocalStorageBridge {
                private val storage = mutableMapOf<String, String>()
                
                @android.webkit.JavascriptInterface
                fun setItem(key: String, value: String) {
                    storage[key] = value
                    Log.d("LocalStorageBridge", "setItem: $key = $value")
                }
                
                @android.webkit.JavascriptInterface
                fun getItem(key: String): String? {
                    val value = storage[key]
                    Log.d("LocalStorageBridge", "getItem: $key = ${value ?: "null"}")
                    return value
                }
                
                @android.webkit.JavascriptInterface
                fun removeItem(key: String) {
                    storage.remove(key)
                    Log.d("LocalStorageBridge", "removeItem: $key")
                }
                
                @android.webkit.JavascriptInterface
                fun clear() {
                    storage.clear()
                    Log.d("LocalStorageBridge", "clear")
                }
                
                @android.webkit.JavascriptInterface
                fun length(): Int {
                    return storage.size
                }
                
                @android.webkit.JavascriptInterface
                fun key(index: Int): String? {
                    val keys = storage.keys.toList()
                    return if (index >= 0 && index < keys.size) keys[index] else null
                }
            }
            
            val localStorageBridge = LocalStorageBridge()
            view.addJavascriptInterface(localStorageBridge, "AndroidLocalStorage")
            
            // Define localStorage using Object.defineProperty BEFORE any page loads
            // This ensures it's available immediately and can't be overwritten
            val defineLocalStorageScript = """
                (function() {
                    try {
                        console.log('=== Defining localStorage BEFORE page load ===');
                        if (typeof(AndroidLocalStorage) !== 'undefined') {
                            Object.defineProperty(window, 'localStorage', {
                                value: {
                                    setItem: function(key, value) { 
                                        AndroidLocalStorage.setItem(key, String(value)); 
                                    },
                                    getItem: function(key) { 
                                        return AndroidLocalStorage.getItem(key); 
                                    },
                                    removeItem: function(key) { 
                                        AndroidLocalStorage.removeItem(key); 
                                    },
                                    clear: function() { 
                                        AndroidLocalStorage.clear(); 
                                    },
                                    get length() { 
                                        return AndroidLocalStorage.length(); 
                                    },
                                    key: function(index) { 
                                        return AndroidLocalStorage.key(index); 
                                    }
                                },
                                writable: false,
                                configurable: false
                            });
                            console.log('✓ localStorage defined using Android bridge (non-configurable)');
                        } else {
                            // Fallback: create in-memory polyfill
                            Object.defineProperty(window, 'localStorage', {
                                value: {
                                    _data: {},
                                    setItem: function(key, value) { this._data[key] = String(value); },
                                    getItem: function(key) { return this._data[key] || null; },
                                    removeItem: function(key) { delete this._data[key]; },
                                    clear: function() { this._data = {}; },
                                    get length() { return Object.keys(this._data).length; },
                                    key: function(index) { var keys = Object.keys(this._data); return keys[index] || null; }
                                },
                                writable: false,
                                configurable: false
                            });
                            console.log('✓ localStorage defined (in-memory fallback, non-configurable)');
                        }
                        console.log('localStorage type: ' + typeof(window.localStorage));
                        console.log('localStorage value: ' + (window.localStorage ? 'defined' : 'null/undefined'));
                    } catch(e) {
                        console.error('Error defining localStorage: ' + e);
                        console.error('Stack: ' + e.stack);
                    }
                })();
            """.trimIndent()
            
            // Inject immediately - this runs before any page loads
            view.evaluateJavascript(defineLocalStorageScript, null)
            
            // Add JavaScript interface to initialize localStorage
            class LocalStorageInterface {
                @android.webkit.JavascriptInterface
                fun initLocalStorage() {
                    Log.d("WebViewConfig", "LocalStorageInterface.initLocalStorage called")
                }
            }
            view.addJavascriptInterface(LocalStorageInterface(), "AndroidLocalStorage")
            
            // Set WebChromeClient to capture JavaScript console messages
            val existingChromeClient = view.webChromeClient
            view.webChromeClient = object : WebChromeClient() {
                override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
                    consoleMessage?.let {
                        val level = when (it.messageLevel()) {
                            ConsoleMessage.MessageLevel.ERROR -> Log.ERROR
                            ConsoleMessage.MessageLevel.WARNING -> Log.WARN
                            ConsoleMessage.MessageLevel.LOG -> Log.INFO
                            ConsoleMessage.MessageLevel.DEBUG -> Log.DEBUG
                            else -> Log.INFO
                        }
                        // Log to both WebViewConsole and chromium tags for better visibility
                        val message = "${it.message()} -- From line ${it.lineNumber()} of ${it.sourceId()}"
                        Log.println(level, "WebViewConsole", message)
                        Log.println(level, "chromium", message) // Also log to chromium tag
                    }
                    // Call existing client if available
                    return existingChromeClient?.onConsoleMessage(consoleMessage) ?: true
                }
                
                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    existingChromeClient?.onProgressChanged(view, newProgress)
                    super.onProgressChanged(view, newProgress)
                    if (newProgress == 100) {
                        Log.d("WebViewConfig", "Page load progress: 100%")
                    }
                }
            }
            
            // Helper function to inject debug script - defined before use
            fun injectDebugScript(view: WebView?) {
                Log.d("WebViewConfig", "Injecting JavaScript monitoring script")
                view?.evaluateJavascript("""
                    (function() {
                        try {
                            console.log('=== WebView Debug Script Loaded ===');
                            
                            // Polyfill localStorage if it's null or undefined
                            if (typeof(Storage) === "undefined" || window.localStorage === null || window.localStorage === undefined) {
                                console.error('✗ localStorage is null/undefined, creating polyfill');
                                window.localStorage = {
                                    _data: {},
                                    setItem: function(key, value) {
                                        this._data[key] = String(value);
                                        console.log('localStorage.setItem(' + key + ', ' + value + ')');
                                    },
                                    getItem: function(key) {
                                        var value = this._data[key] || null;
                                        console.log('localStorage.getItem(' + key + ') = ' + value);
                                        return value;
                                    },
                                    removeItem: function(key) {
                                        delete this._data[key];
                                        console.log('localStorage.removeItem(' + key + ')');
                                    },
                                    clear: function() {
                                        this._data = {};
                                        console.log('localStorage.clear()');
                                    },
                                    get length() {
                                        return Object.keys(this._data).length;
                                    },
                                    key: function(index) {
                                        var keys = Object.keys(this._data);
                                        return keys[index] || null;
                                    }
                                };
                                console.log('✓ localStorage polyfill created');
                            } else {
                                console.log('✓ Storage available');
                                try {
                                    if (window.localStorage) {
                                        localStorage.setItem('test', 'test');
                                        localStorage.removeItem('test');
                                        console.log('✓ localStorage is working');
                                    } else {
                                        console.error('✗ window.localStorage is null');
                                    }
                                } catch(e) {
                                    console.error('✗ localStorage error: ' + e);
                                }
                            }
                            
                            // Monitor form submissions
                            var forms = document.querySelectorAll('form');
                            console.log('Found ' + forms.length + ' form(s)');
                            forms.forEach(function(form, index) {
                                console.log('Monitoring form ' + index);
                                form.addEventListener('submit', function(e) {
                                    console.log('=== FORM SUBMITTED ===');
                                    console.log('Form action: ' + form.action);
                                    console.log('Form method: ' + form.method);
                                    var formData = new FormData(form);
                                    for (var pair of formData.entries()) {
                                        console.log('Form field: ' + pair[0] + ' = ' + pair[1]);
                                    }
                                }, true);
                            });
                            
                            // Monitor input field to see if it gets cleared
                            var emailInput = document.querySelector('input[type="email"], input[name*="email"], input[id*="email"], input[placeholder*="Email"], input[placeholder*="email"]');
                            if (emailInput) {
                                console.log('✓ Found email input field');
                                var originalValue = emailInput.value;
                                
                                emailInput.addEventListener('focus', function() {
                                    console.log('Email input focused');
                                });
                                
                                emailInput.addEventListener('input', function() {
                                    console.log('Email input changed to: ' + this.value);
                                });
                                
                                emailInput.addEventListener('blur', function() {
                                    console.log('Email input blurred, value: ' + this.value);
                                });
                                
                                emailInput.addEventListener('change', function() {
                                    console.log('Email input changed event, value: ' + this.value);
                                });
                                
                                // Monitor if value gets cleared
                                var checkInterval = setInterval(function() {
                                    if (emailInput.value !== originalValue) {
                                        if (emailInput.value === '') {
                                            console.error('=== EMAIL INPUT WAS CLEARED! ===');
                                            console.error('Previous value was: ' + originalValue);
                                        } else {
                                            originalValue = emailInput.value;
                                        }
                                    }
                                }, 100);
                                
                                // Monitor button clicks and prevent form submission if localStorage isn't ready
                                var continueButton = document.querySelector('button[type="submit"], button:contains("Continue"), [role="button"]:contains("Continue")');
                                if (continueButton) {
                                    console.log('✓ Found Continue button');
                                    continueButton.addEventListener('click', function(e) {
                                        console.log('=== CONTINUE BUTTON CLICKED ===');
                                        console.log('Email value at click: ' + (emailInput ? emailInput.value : 'N/A'));
                                        
                                        // Check if localStorage is available
                                        if (!window.localStorage) {
                                            console.error('✗ localStorage is not available, preventing form submission');
                                            e.preventDefault();
                                            e.stopPropagation();
                                            e.stopImmediatePropagation();
                                            return false;
                                        }
                                    }, true);
                                    
                                    // Also prevent form submission
                                    var form = emailInput.closest('form');
                                    if (form) {
                                        form.addEventListener('submit', function(e) {
                                            console.log('=== FORM SUBMIT EVENT ===');
                                            console.log('Email value at submit: ' + (emailInput ? emailInput.value : 'N/A'));
                                            
                                            if (!window.localStorage) {
                                                console.error('✗ localStorage is not available, preventing form submission');
                                                e.preventDefault();
                                                e.stopPropagation();
                                                return false;
                                            }
                                        }, true);
                                    }
                                }
                            } else {
                                console.warn('✗ Email input field not found');
                            }
                        } catch(e) {
                            console.error('Error in injected script: ' + e);
                            console.error('Stack: ' + e.stack);
                        }
                    })();
                """.trimIndent()) { result ->
                    Log.d("WebViewConfig", "JavaScript injection result: $result")
                }
            }
            
            // Get existing WebViewClient if any (library might have set one)
            val existingClient = view.webViewClient
            
            // localStorage polyfill script to inject
            val localStoragePolyfill = """
                <script>
                (function() {
                    if (typeof(Storage) === "undefined" || window.localStorage === null || window.localStorage === undefined) {
                        window.localStorage = {
                            _data: {},
                            setItem: function(key, value) { this._data[key] = String(value); },
                            getItem: function(key) { return this._data[key] || null; },
                            removeItem: function(key) { delete this._data[key]; },
                            clear: function() { this._data = {}; },
                            get length() { return Object.keys(this._data).length; },
                            key: function(index) { var keys = Object.keys(this._data); return keys[index] || null; }
                        };
                    }
                })();
                </script>
            """.trimIndent()
            
            // Set WebViewClient to handle form submissions properly
            // IMPORTANT: Set this BEFORE loading any URL to ensure it's active
            view.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                    // Call existing client if available, otherwise let WebView handle it
                    return existingClient?.shouldOverrideUrlLoading(view, request?.url.toString()) ?: false
                }
                
                @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
                override fun shouldInterceptRequest(view: WebView?, request: WebResourceRequest?): WebResourceResponse? {
                    val url = request?.url?.toString() ?: return null
                    val response = existingClient?.shouldInterceptRequest(view, request) ?: super.shouldInterceptRequest(view, request)
                    
                    // Intercept HTML responses from accounts.scdn.co to inject localStorage polyfill
                    if (url.contains("accounts.scdn.co") && response != null) {
                        val mimeType = response.mimeType ?: ""
                        if (mimeType.contains("text/html") || url.endsWith("/") || (!url.contains(".") && !url.contains("_next"))) {
                            try {
                                val inputStream = response.data
                                val html = inputStream?.bufferedReader(StandardCharsets.UTF_8).use { it?.readText() }
                                if (html != null) {
                                    // Inject localStorage polyfill script at the beginning of <head> or <body>
                                    val polyfillScript = """
                                        <script>
                                        (function() {
                                            if (typeof(Storage) === "undefined" || window.localStorage === null || window.localStorage === undefined) {
                                                window.localStorage = {
                                                    _data: {},
                                                    setItem: function(key, value) { this._data[key] = String(value); },
                                                    getItem: function(key) { return this._data[key] || null; },
                                                    removeItem: function(key) { delete this._data[key]; },
                                                    clear: function() { this._data = {}; },
                                                    get length() { return Object.keys(this._data).length; },
                                                    key: function(index) { var keys = Object.keys(this._data); return keys[index] || null; }
                                                };
                                                console.log('✓ localStorage polyfill injected in HTML');
                                            }
                                        })();
                                        </script>
                                    """.trimIndent()
                                    
                                    val modifiedHtml = when {
                                        html.contains("<head>") -> html.replace("<head>", "<head>$polyfillScript")
                                        html.contains("<body>") -> html.replace("<body>", "<body>$polyfillScript")
                                        else -> "$polyfillScript$html"
                                    }
                                    
                                    val modifiedInputStream = ByteArrayInputStream(modifiedHtml.toByteArray(StandardCharsets.UTF_8))
                                    return WebResourceResponse(
                                        response.mimeType,
                                        response.encoding ?: "utf-8",
                                        modifiedInputStream
                                    )
                                }
                            } catch (e: Exception) {
                                Log.e("WebViewConfig", "Error modifying HTML response: ${e.message}", e)
                            }
                        }
                    }
                    
                    return response
                }
                
                override fun onPageStarted(view: WebView?, url: String?, favicon: android.graphics.Bitmap?) {
                    existingClient?.onPageStarted(view, url, favicon)
                    super.onPageStarted(view, url, favicon)
                    Log.d("WebViewConfig", "Page started loading: $url")
                    Log.d("chromium", "WebViewConfig: Page started loading: $url")
                    
                    // Re-define localStorage in onPageStarted to ensure it's available
                    // Use the same script that was injected before page load
                    view?.evaluateJavascript(defineLocalStorageScript) { result ->
                        Log.d("WebViewConfig", "onPageStarted polyfill injection result: $result")
                        Log.d("chromium", "WebViewConfig: onPageStarted polyfill injection result: $result")
                    }
                    
                    // Also verify localStorage is available
                    view?.evaluateJavascript("""
                        (function() {
                            console.log('=== Verifying localStorage in onPageStarted ===');
                            console.log('localStorage type: ' + typeof(window.localStorage));
                            console.log('localStorage is null: ' + (window.localStorage === null));
                            console.log('localStorage is undefined: ' + (window.localStorage === undefined));
                            if (window.localStorage) {
                                try {
                                    window.localStorage.setItem('test', 'test');
                                    window.localStorage.removeItem('test');
                                    console.log('✓ localStorage is working');
                                } catch(e) {
                                    console.error('✗ localStorage error: ' + e);
                                }
                            } else {
                                console.error('✗ localStorage is still null/undefined!');
                            }
                        })();
                    """.trimIndent(), null)
                    
                    // Also inject debug script after a short delay
                    Handler(Looper.getMainLooper()).postDelayed({
                        injectDebugScript(view)
                    }, 200)
                }
                
                override fun onPageFinished(view: WebView?, url: String?) {
                    existingClient?.onPageFinished(view, url)
                    super.onPageFinished(view, url)
                    
                    Log.d("WebViewConfig", "Page finished loading: $url")
                    
                    // Inject JavaScript immediately and also after a delay to catch dynamic content
                    injectDebugScript(view)
                    Handler(Looper.getMainLooper()).postDelayed({
                        injectDebugScript(view)
                    }, 1000)
                }
            }
            
            // Inject localStorage polyfill immediately (before page loads)
            view.evaluateJavascript("""
                (function() {
                    if (typeof(Storage) === "undefined" || window.localStorage === null || window.localStorage === undefined) {
                        window.localStorage = {
                            _data: {},
                            setItem: function(key, value) { this._data[key] = String(value); },
                            getItem: function(key) { return this._data[key] || null; },
                            removeItem: function(key) { delete this._data[key]; },
                            clear: function() { this._data = {}; },
                            get length() { return Object.keys(this._data).length; },
                            key: function(index) { var keys = Object.keys(this._data); return keys[index] || null; }
                        };
                    }
                })();
            """.trimIndent(), null)
            
            Log.d("WebViewConfig", "DOM storage and WebViewClient configured successfully")
        } ?: Log.w("WebViewConfig", "Failed to cast NativeWebView to Android WebView. Type: ${webView.javaClass.name}")
    } catch (e: Exception) {
        Log.e("WebViewConfig", "Error configuring WebView: ${e.message}", e)
    }
}

