package intro.android.cm.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import intro.android.cm.db.NotaDB
import intro.android.cm.db.NotaRepository
import intro.android.cm.entities.Nota
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotaViewModel(application: Application) : AndroidViewModel(application){

    private val repository: NotaRepository

    val allNotas: LiveData<List<Nota>>

    init {
        val notaDao = NotaDB.getDatabase(application, viewModelScope).notaDao()
        repository = NotaRepository(notaDao)
        allNotas = repository.allnotas
    }

    fun insert(nota: Nota) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(nota)
    }
}