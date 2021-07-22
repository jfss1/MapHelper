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
    val allNotes: LiveData<List<Nota>>

    init {
        val notesDao = NotaDB.getDatabase(application, viewModelScope).NotesDao()
        repository = NotaRepository(notesDao)
        allNotes= repository.allNotes
    }

    fun insert(notes:Nota) = viewModelScope.launch {
        repository.insert(notes)
    }
    fun deleteAll()= viewModelScope.launch(Dispatchers.IO){repository.deleteAll()}

    fun deleteByTittle(city: String )= viewModelScope.launch(Dispatchers.IO){
        repository.deleteByTittle(city)
    }

    fun updateNote(notes:Nota)= viewModelScope.launch(Dispatchers.IO){
        repository.updateNote(notes)
    }

    fun updateNoteFromId(id:Int, title: String,description: String)=viewModelScope.launch(Dispatchers.IO){
        repository.updateNoteFromId(id,title,description)}

    fun updateDescriptionFromTitle(title: String,description: String)=viewModelScope.launch(Dispatchers.IO){
        repository.updateDescriptionFromTitle(title,description)}



}