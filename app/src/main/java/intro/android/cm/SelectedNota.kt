package intro.android.cm

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class SelectedNota : AppCompatActivity() {
    private lateinit var tituloView: TextView
    private lateinit var descricaoView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selected_nota)

        tituloView = findViewById(R.id.tituloInput)
        descricaoView = findViewById(R.id.DescricaoInput)

        var id = intent.getIntExtra("id", 0)
        var tit = intent.getStringExtra("titulo")
        var desc = intent.getStringExtra("descricao")
        //TENS DE CRIAR O ACESSO E A MUDANSA DO SELECT NOTE PARA DEPOIS EDITARES E APAGARES
        // utilizar a actividade de criar notas para alterar a nota toda???

        tituloView.text = tit
        descricaoView.text = desc

        val buttonEdit = findViewById<Button>(R.id.uptdateButton)
        buttonEdit.setOnClickListener {
            val titulo = tituloView.text.toString()
            val descricao = descricaoView.text.toString()
            val replyIntent = Intent()
            replyIntent.putExtra("delete", "0")

            replyIntent.putExtra("edit", "1")
            replyIntent.putExtra("id", id)
            replyIntent.putExtra("titulo", titulo)
            replyIntent.putExtra("descricao", descricao)
            setResult(Activity.RESULT_OK, replyIntent)


            finish()
            // Toast.makeText( this,"$ogTitle has been click. ", Toast.LENGTH_SHORT).show()
        }

        val buttonDelete = findViewById<Button>(R.id.DeleteButton)
        buttonDelete.setOnClickListener {
            val replyIntent = Intent()
            replyIntent.putExtra("titulo", tit)
            replyIntent.putExtra("delete", "1")
            setResult(Activity.RESULT_OK, replyIntent)

            finish()
        }

        val buttonCancel = findViewById<Button>(R.id.cancelButton)
        buttonCancel.setOnClickListener{
            val intent = Intent()
            setResult(Activity.RESULT_OK, intent)

            finish()
        }
    }
}