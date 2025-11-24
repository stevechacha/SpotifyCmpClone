package model

data class DomainModel(
    var list: List<ProductsDomainModel>
)

data class ProductsDomainModel (
    var id: Int=0,
    var title: String="",
    val description: String="",
    val price: Double=0.0,
    val discountPercentage: Double=0.0,
    val category: String="",
    val thumbnail: String="",
)