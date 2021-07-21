package intro.android.cm

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText

class NewNota : AppCompatActivity() {
    private lateinit var editTitleView: EditText
    private lateinit var editDescriptionView: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_nota)

        editTitleView = findViewById(R.id.tituloInput)
        editDescriptionView = findViewById(R.id.DescricaoInput)

        val button = findViewById<Button>(R.id.save_button)
        button.setOnClickListener {
            val replyIntent = Intent()
            if (TextUtils.isEmpty(editTitleView.text )&& TextUtils.isEmpty(editDescriptionView.text )) {
                // isto vai fazer com que o resultado seja =0 logo nao guardou nada
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                val titulo = editTitleView.text.toString()
                val descricao = editDescriptionView.text.toString()
                replyIntent.putExtra(EXTRA_REPLY, titulo)
                replyIntent.putExtra("descricao", descricao)
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }
    }

    companion object {
        const val EXTRA_REPLY = "into.android.cm.wordlistsql.REPLY"
    }
}