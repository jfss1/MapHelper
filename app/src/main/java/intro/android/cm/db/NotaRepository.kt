package intro.android.cm.db

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import intro.android.cm.dao.DaoNota
import intro.android.cm.entities.Nota

class NotaRepository(private val daoNota: DaoNota) {

    val allNotes: LiveData<List<Nota>> = daoNota.getAllNotes()

    fun getTitleFromNotes(title:String): LiveData<Nota> {
        return daoNota.getTitleFromNotes(title)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(city: Nota) {
        daoNota.insert(city)
    }

    suspend fun deleteAll() {
        daoNota.deleteAll()
    }

    suspend fun deleteByTittle(title: String) {
        daoNota.deleteByTittle(title)
    }

    suspend fun updateNote(title: Nota) {
        daoNota.updateNote(title)
    }
    suspend fun updateNoteFromId(id:Int,title: String, description: String) {
        daoNota.updateNoteFromId(id,title,description)
    }

    suspend fun updateDescriptionFromTitle(title: String, description: String) {
        daoNota.updateDescriptionFromTitle(title,description)
    }


}