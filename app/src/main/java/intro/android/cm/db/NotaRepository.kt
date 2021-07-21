package intro.android.cm.db

import androidx.lifecycle.LiveData
import intro.android.cm.dao.DaoNota
import intro.android.cm.entities.Nota

class NotaRepository(private val daoNota: DaoNota) {
    val allnotas: LiveData<List<Nota>> = daoNota.getAllNotasOrder()

    suspend fun getNotasFromTitulo(titulo: String): LiveData<Nota>{
        return daoNota.getFromTitulo(titulo)
    }

    suspend fun insert(nota: Nota){
        daoNota.insert(nota);
    }

    suspend fun deleteAll(){
        daoNota.deleteAll()
    }

    suspend fun deleteFromTitulo(titulo: String){
        daoNota.deleteFromTitulo(titulo)
    }

    suspend fun updateNotasFromID(id:Int?, titulo: String, descricao: String){
        daoNota.updateFromID(id,titulo,descricao)
    }

    suspend fun updateDescricaoFromTitulo(titulo: String, descricao: String){
        daoNota.updateFromTitle(titulo, descricao)
    }


}