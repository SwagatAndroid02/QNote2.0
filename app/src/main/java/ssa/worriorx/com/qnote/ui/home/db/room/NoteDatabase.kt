package ssa.worriorx.com.qnote.ui.home.db.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Notes::class],version = 1, exportSchema = false)
abstract class NoteDatabase: RoomDatabase() {
    abstract val noteDao: NoteDao

    companion object{
        @Volatile
        private var INSTANSE : NoteDatabase? = null
        fun getInstanse(context: Context): NoteDatabase{
            synchronized(this){
                var instanse: NoteDatabase? = INSTANSE
                if (instanse == null){
                    instanse = Room.databaseBuilder(
                        context.applicationContext,
                        NoteDatabase::class.java,
                        "QNoteDatabase"
                    ).build()
                }
                return instanse
            }
        }
    }
}