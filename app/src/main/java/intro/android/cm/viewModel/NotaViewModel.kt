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

    fun insert(nota: Nota) = viewModelScope.launch {
        repository.insert(nota)
    }

    fun deleteAll() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAll()
    }

    fun deleteFromTitulo(titulo: String) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteFromTitulo(titulo)
    }

    fun updateNotaFromID(id:Int?, titulo: String, descricao: String) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateNotasFromID(id, titulo, descricao)
    }

    fun updateDescricaoFromTitulo(titulo: String, descricao: String) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateDescricaoFromTitulo(titulo, descricao)
    }

}