package com.example.notesapp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [NoteEntity::class], version = 1)
abstract class NoteDatabase: RoomDatabase() {
    abstract fun noteDao():NoteDao
    companion object{
        @Volatile
        private var INSTANCE:NoteDatabase? = null

        fun getInstance(context: Context):NoteDatabase{
            synchronized(this){
                var instance= INSTANCE

                if(instance == null){
                    instance = Room.databaseBuilder(context.applicationContext,NoteDatabase::class.java,"note_database"
                    ).fallbackToDestructiveMigration().build()

                    INSTANCE =instance
                }
                return instance
            }

        }


    }

}