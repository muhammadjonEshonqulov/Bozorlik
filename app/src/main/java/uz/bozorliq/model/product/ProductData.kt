package uz.bozorliq.model.product

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ProductData(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var title: String,
    val category_id: Int,
    var checked: Boolean
)
