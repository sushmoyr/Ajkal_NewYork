package com.sushmoyr.ajkalnewyork.datasource.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sushmoyr.ajkalnewyork.models.InvalidUser
import com.sushmoyr.ajkalnewyork.utils.RoomTypeConverter

@Database(entities = [InvalidUser::class], version = 1, exportSchema = false)
@TypeConverters(RoomTypeConverter::class)
abstract class UserDatabase: RoomDatabase() {

    abstract fun userDao() : UserDao

    companion object{
        @Volatile
        private var INSTANCE: UserDatabase? = null

        fun getDatabase(context: Context): UserDatabase {
            val tempInstance = INSTANCE
            if(tempInstance!=null)
                return tempInstance

            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserDatabase::class.java,
                    "user_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}