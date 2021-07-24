package intro.android.cm

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import intro.android.cm.api.EndPoints
import intro.android.cm.api.Notas
import intro.android.cm.api.OutputDelete
import intro.android.cm.api.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditMapNoteActivity : AppCompatActivity() {
    private lateinit var TitleView: TextView
    private lateinit var DescriptionView: TextView
    private lateinit var shared_preferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_map_note)
        TitleView = findViewById(R.id.editTextViewSelectedNoteTittle)
        DescriptionView = findViewById(R.id.editTextViewSelectedNoteDescription)
        shared_preferences = getSharedPreferences("shared_preferences", Context.MODE_PRIVATE)
        var id = intent.getStringExtra("ID")
        var ogTitle = intent.getStringExtra("TITLE")
        var ogDesc = intent.getStringExtra("DESC")
        var lat:Float = intent.getFloatExtra("LAT",0f)
        var lon:Float = intent.getFloatExtra("LON",0f)
        var id_user:Int = intent.getIntExtra("ID_USER",0)


        TitleView.text = ogTitle
        DescriptionView.text = ogDesc

        val buttonEdit = findViewById<Button>(R.id.buttonEditNote)
        buttonEdit.setOnClickListener {
            if (TextUtils.isEmpty(TitleView.text) || TextUtils.isEmpty(DescriptionView.text)) {
                Toast.makeText(this@EditMapNoteActivity, R.string.login_Error, Toast.LENGTH_LONG).show()
            } // caso tenha texto ira passsar para o request ao servidor
            else {
                val request = ServiceBuilder.buildService(EndPoints::class.java)

                val title = TitleView.text.toString()
                val description = DescriptionView.text.toString()
                val id_utilizador = shared_preferences.getInt("id", 0)

                val call = request.updateNota(
                    id = id,
                    id_utilizador = id_utilizador,
                    title = title,
                    description = description

                )

                call.enqueue(object : Callback<Notas> {
                    override fun onResponse(call: Call<Notas>, response: Response<Notas>) {
                        if (response.isSuccessful) {
                            val c: Notas = response.body()!!
                            Toast.makeText(this@EditMapNoteActivity, c.MSG, Toast.LENGTH_LONG).show()
                            val intent = Intent(this@EditMapNoteActivity, MapsActivity::class.java)
                            setResult(Activity.RESULT_OK, intent)
                            finish()

                        }
                    }

                    override fun onFailure(call: Call<Notas>, t: Throwable) {
                        Toast.makeText(this@EditMapNoteActivity, "${t.message}", Toast.LENGTH_SHORT)
                            .show()
                    }
                })

            }

        }



        val buttonDelete = findViewById<Button>(R.id.buttonDeleteNote)
        buttonDelete.setOnClickListener {
            val request = ServiceBuilder.buildService(EndPoints::class.java)
            val call = request.deleteNota(id=id.toString())

            call.enqueue(object : Callback<OutputDelete> {
                override fun onResponse(call: Call<OutputDelete>, response: Response<OutputDelete>) {
                    if (response.isSuccessful) {
                        val c: OutputDelete = response.body()!!
                        Toast.makeText(this@EditMapNoteActivity, c.MSG, Toast.LENGTH_LONG).show()
                        val intent = Intent(this@EditMapNoteActivity, MapsActivity::class.java)
                        setResult(Activity.RESULT_OK, intent)
                        finish()

                    }
                }

                override fun onFailure(call: Call<OutputDelete>, t: Throwable) {
                    Toast.makeText(this@EditMapNoteActivity, "${t.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            })

        }


    }
}

