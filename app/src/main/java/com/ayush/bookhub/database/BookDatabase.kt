package bookhub.kartikey.bookhub.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ayush.bookhub.database.BookDao

@Database(entities = [BookEntity::class], version = 1)
abstract class BookDatabase : RoomDatabase() {

    abstract fun bookDao(): BookDao


}