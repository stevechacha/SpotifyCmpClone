import SwiftUI
import UIKit
import Foundation
import ComposeApp

@main
struct iOSApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self) var appDelegate
    
    var body: some Scene {
        WindowGroup {
            ContentView()
                .onOpenURL { url in
                    // Handle deep link callback
                    // Pass URL string directly to Kotlin function
                    MainViewControllerKt.handleCallbackUrl(urlString: url.absoluteString)
                }
        }
    }
}

class AppDelegate: NSObject, UIApplicationDelegate {
    func application(_ application: UIApplication, open url: URL, options: [UIApplication.OpenURLOptionsKey : Any] = [:]) -> Bool {
        // Handle URL when app is opened via deep link
        // Pass URL string directly to Kotlin function
        MainViewControllerKt.handleCallbackUrl(urlString: url.absoluteString)
        return true
    }
}