package com.example.littlelemon

import android.content.Context
import androidx.room.*
import kotlinx.serialization.SerialName

@Database(entities = [MenuItem::class], version = 1)
abstract class MenuDatabase: RoomDatabase(){
    abstract fun menuDao(): MenuItemDao

    companion object {
        @Volatile
        private var instance: MenuDatabase? = null

        fun getInstance(context: Context): MenuDatabase {
            return instance ?: synchronized(this) {
                val newInstance = Room.databaseBuilder(
                    context.applicationContext,
                    MenuDatabase::class.java, "app_database"
                ).build()
                instance = newInstance
                newInstance
            }
        }
    }
}


@Entity
data class MenuItem(
    @PrimaryKey val id:Int,
    var title: String,
    var description: String,
    var price: Double,
    var image: String,
    var category: String,
)

@Dao
interface MenuDao{
    @Query("SELECT * FROM menuitem")
    fun getAll():List<MenuItem>
}

@Dao
interface MenuItemDao{
    @Query("SELECT * FROM menuitem")
    suspend fun getAll(): List<MenuItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(menuItems: List<MenuItem>)
}