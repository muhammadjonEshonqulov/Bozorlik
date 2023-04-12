package uz.bozorliq.model.category

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CategoryData(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var title: String,
//    var longClicked: Boolean = false
)
