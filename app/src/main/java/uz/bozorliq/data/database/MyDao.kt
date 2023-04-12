package uz.bozorliq.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import uz.bozorliq.model.category.CategoryData
import uz.bozorliq.model.product.ProductData

@Dao
interface MyDao {

    // Category
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: CategoryData)

    @Query("UPDATE CategoryData SET title=:title WHERE id = :category_id ")
    suspend fun updateCategory(title: String, category_id: Int)


    @Query("select * from CategoryData where  CategoryData.title like :query")
    fun searchCategory(query: String): Flow<List<CategoryData>>

    @Query("select * from CategoryData")
    fun getCategory(): Flow<List<CategoryData>>

    @Delete
    suspend fun deleteCategory(obj: CategoryData)


    // product
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(category: ProductData)

    @Query("UPDATE ProductData SET title=:title, checked=:checked WHERE id = :id ")
    suspend fun updateProductData(title: String, checked: Boolean, id: Int)

    @Query("select * from ProductData where  ProductData.title like :query")
    fun searchProductData(query: String): Flow<List<ProductData>>

    @Query("select * from ProductData where ProductData.category_id = :id")
    fun getProductData(id: Int): Flow<List<ProductData>>

    @Delete
    suspend fun deleteProduct(obj: ProductData)

}