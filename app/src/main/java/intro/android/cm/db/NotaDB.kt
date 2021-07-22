package intro.android.cm.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import intro.android.cm.dao.DaoNota
import intro.android.cm.entities.Nota
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = arrayOf(Nota::class), version = 1, exportSchema = false)
public abstract class NotaDB: RoomDatabase() {

    abstract fun NotesDao(): DaoNota
    private class WordDatabaseCallback(
        private val scope: CoroutineScope
    ): RoomDatabase.Callback() {
        override  fun onOpen(db: SupportSQLiteDatabase){
            super.onOpen(db)
            INSTANCE?.let{database->scope.launch {
                var notesDao=database.NotesDao()

                // Codigo da professora comentado no video
                //delete all content here
                // AppDao.deleteAll()
                // Add sample words
                var note= Nota(1,"Buraco ","Existe um buraco na estrada da abelheira")
                notesDao.insert(note)


            }}

        }
    }

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: NotaDB? = null

        fun getDatabase(context: Context, scope: CoroutineScope): NotaDB {
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NotaDB::class.java,
                    "Notes_database"
                )//IMPORTANTE ISTO E NECESSARIO QUANDO QUERES DAR RESET A BASE DE DADOS
                    // .fallbackToDestructiveMigration()
                    .addCallback(WordDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                return instance
            }

        }
    }

}