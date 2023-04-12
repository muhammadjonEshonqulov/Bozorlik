package uz.bozorliq.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import uz.bozorliq.model.category.CategoryData
import uz.bozorliq.model.product.ProductData


@Database(entities = [CategoryData::class, ProductData::class], version = 1)
abstract class MyDatabase : RoomDatabase() {

    abstract fun dao(): MyDao

    companion object {

//        private var instance: MyDatabase? = null

//        fun initDatabase(context: Context) {
//            synchronized(this) {
//                if (instance == null) {
//                    instance = Room
//                        .databaseBuilder(
//                            context.applicationContext,
//                            MyDatabase::class.java, "bozorliq.db"
//                        )
//                        .fallbackToDestructiveMigration()
////                        .addMigrations(MIGRATION_1_2)
//                        .build()
//                }
//            }
//        }
//
//        fun getDatabase() = instance
    }
}