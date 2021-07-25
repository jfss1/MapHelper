package intro.android.cm

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import intro.android.cm.adapter.NotaAdapter
import intro.android.cm.entities.Nota
import intro.android.cm.viewModel.NotaViewModel

@Suppress("DEPRECATION")
class Anotacao : AppCompatActivity(), NotaAdapter.OnItemClickListener {
    private lateinit var noteViewModel: NotaViewModel
    private val newNoteActivityRequestCode = 1
    private val editNoteActivityRequestCode = 2
    private lateinit var shared_preferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anotacao)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = NotaAdapter(this, this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        shared_preferences = getSharedPreferences("shared_preferences", Context.MODE_PRIVATE)

        noteViewModel = ViewModelProvider(this).get(NotaViewModel::class.java)
        noteViewModel.allNotes.observe(this) { notes -> notes?.let { adapter.setNotes(it) } }

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this, NewNota::class.java)
            // aqui comeca a atividade newNote com o codigo =1
            startActivityForResult(intent, newNoteActivityRequestCode)
        }

        val buttonMap = findViewById<Button>(R.id.button_map)
        buttonMap.setOnClickListener{
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }

        val buttonLogin = findViewById<Button>(R.id.button_login1)
        buttonLogin.setOnClickListener{
            val shared_preferences_edit: SharedPreferences.Editor =
                    shared_preferences.edit()
            shared_preferences_edit.putBoolean("loggedIn", false)
            shared_preferences_edit.apply()
            var intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
// aqui ele deteta se recebeu o intent do criar nota , caso crie ele adiciona as notas
        if (requestCode == newNoteActivityRequestCode && resultCode == Activity.RESULT_OK) {

            data?.getStringExtra(NewNota.EXTRA_REPLY)?.let {
                var d = data.getStringExtra("description")
                if (d != null) {
                    val note = Nota(title = it, description = d)
                    noteViewModel.insert(note)
                }
            }
        } else if (requestCode == newNoteActivityRequestCode && resultCode == Activity.RESULT_CANCELED) {
// caso nao detete qualquer texto devolve um toast a dizer que estava vazia
            Toast.makeText(
                applicationContext,
                R.string.empty_not_saved,
                Toast.LENGTH_LONG
            ).show()
            // aqui deteta se o reply vem da atividade de editar/ apagar
        } else if (requestCode == editNoteActivityRequestCode && resultCode == Activity.RESULT_OK) {
            // primeiro verifica se e para a pagar a nota
            if (data?.getStringExtra("delete") == "1") {
                var title = data.getStringExtra("title")
                if (title != null) {
                    noteViewModel.deleteByTittle(title)
                }
                Toast.makeText(this, R.string.Deleted_Note, Toast.LENGTH_SHORT).show()
                // caso contrario vai verificar o que e para editar
            } else {
                if (data?.getStringExtra("edit") == "1") {
                    var idN = data.getIntExtra("id", 0)

                    var t = data.getStringExtra("title")
                    var d = data.getStringExtra("description")
                    if (t != null && d != null && idN != 0) {
                        noteViewModel.updateNoteFromId(idN, t, d)
                        Toast.makeText(this, R.string.edited_note, Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }

    }

    override fun onItemClick(id: Int?, title: String, description: String) {
        // simples toast para saber se esta a registar o on click corretamente
        val intent = Intent(this, SelectedNota::class.java)
        intent.putExtra("id", id)
        intent.putExtra("title", title)
        intent.putExtra("description", description)

        // aqui comeca a atividade selectedNote com o codigo = 2
        startActivityForResult(intent, editNoteActivityRequestCode)

    }

    //Logout
    fun onClickLogin(view: View){
        val shared_preferences_edit: SharedPreferences.Editor =
                shared_preferences.edit()
        shared_preferences_edit.putBoolean("loggedIn", false)
        shared_preferences_edit.apply()
        var intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
    }


}