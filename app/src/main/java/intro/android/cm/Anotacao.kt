package intro.android.cm

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anotacao)

        val recyclerView = findViewById<RecyclerView>(R.id.recycleview)
        val adapter = NotaAdapter(this, this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)


        noteViewModel = ViewModelProvider(this).get(NotaViewModel::class.java)
        noteViewModel.allNotas.observe(this) { nota -> nota?.let { adapter.setNotas(it) } }

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this, NewNota::class.java)
            // aqui comeca a atividade newNote com o codigo =1
            startActivityForResult(intent, newNoteActivityRequestCode)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // aqui ele deteta se recebeu o intent do criar nota , caso crie ele adiciona as notas
        if (requestCode == newNoteActivityRequestCode && resultCode == Activity.RESULT_OK) {

            data?.getStringExtra(NewNota.EXTRA_REPLY)?.let {
                var d = data.getStringExtra("description")
                // NOTA POR ALGUM MOTIVO AO CRIAR AS NOTAS ELE ESTA A TROCAR A DESCRICAO PELO TITUTLO
                if (d != null) {
                    val nota = Nota(titulo = it, descricao = d)
                    noteViewModel.insert(nota)
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
                var titulo = data.getStringExtra("titulo")
                if (titulo != null) {
                    noteViewModel.deleteFromTitulo(titulo)
                }
                Toast.makeText(this, R.string.Deleted_Note, Toast.LENGTH_SHORT).show()
                // caso contrario vai verificar o que e para editar
            } else {
                if (data?.getStringExtra("edit") == "1") {
                    var idN = data.getIntExtra("id", 0)

                    var t = data.getStringExtra("titulo")
                    var d = data.getStringExtra("descricao")
                    if (t != null && d != null && idN != 0) {
                        noteViewModel.updateNotaFromID(idN, t, d)
                        Toast.makeText(this, R.string.edited_note, Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }

    }

    override fun onItemClick(id: Int?, titulo: String, descricao: String) {
        // simples toast para saber se esta a registar o on click corretamente
        val intent = Intent(this, SelectedNota::class.java)
        intent.putExtra("id", id)
        intent.putExtra("titulo", titulo)
        intent.putExtra("descricao", descricao)

        // aqui comeca a atividade selectedNote com o codigo = 2
        startActivityForResult(intent, editNoteActivityRequestCode)

    }
}