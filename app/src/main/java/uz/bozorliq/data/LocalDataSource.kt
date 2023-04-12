package uz.bozorliq.data

import kotlinx.coroutines.flow.Flow
import uz.bozorliq.data.database.MyDao
import uz.bozorliq.model.category.CategoryData
import uz.bozorliq.model.product.ProductData
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val dao: MyDao
) {

    suspend fun insertCategory(categoryData: CategoryData) {
        return dao.insertCategory(categoryData)
    }

    suspend fun delete(categoryData: CategoryData) {
        return dao.deleteCategory(categoryData)
    }

    suspend fun updateCategory(title: String, category_id: Int) {
        return dao.updateCategory(title, category_id)
    }

    fun searchCategory(query: String): Flow<List<CategoryData>> {
        return dao.searchCategory(query)
    }

    fun getCategory(): Flow<List<CategoryData>> = dao.getCategory()

    suspend fun insertProduct(productData: ProductData) {
        return dao.insertProduct(productData)
    }

    suspend fun delete(productData: ProductData) {
        return dao.deleteProduct(productData)
    }

    suspend fun updateProduct(title: String, checked: Boolean, id: Int) {
        return dao.updateProductData(title, checked, id)
    }

    fun searchProduct(query: String): Flow<List<ProductData>> {
        return dao.searchProductData(query)
    }

    fun getProduct(category_id:Int): Flow<List<ProductData>> = dao.getProductData(category_id)
}