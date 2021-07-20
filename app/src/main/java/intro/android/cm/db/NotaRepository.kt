package intro.android.cm.db

import androidx.lifecycle.LiveData
import intro.android.cm.dao.DaoNota
import intro.android.cm.entities.Nota

class NotaRepository(private val daoNota: DaoNota) {
    val allnotas: LiveData<List<Nota>> = daoNota.getAlphabetizedTitulos()

    suspend fun insert(nota: Nota){
        daoNota.insert(nota);
    }
}