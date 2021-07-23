package intro.android.cm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.Toast
import intro.android.cm.api.EndPoints
import intro.android.cm.api.OutputLogin
import intro.android.cm.api.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistarUser : AppCompatActivity() {
    private lateinit var editUsernameView: EditText
    private lateinit var editPasswordView: EditText
    private lateinit var editEmailView: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registar_user)


        editUsernameView = findViewById(R.id.registerUsernameEditText)
        editPasswordView = findViewById(R.id.registerPasswordEditText)
        editEmailView = findViewById(R.id.registerEmailEditText)


    }

// funcao que efetua o login

    fun register(view: View) {
        // primeiro verifica se os edit estapo prenchidos
        if (TextUtils.isEmpty(editUsernameView.text) || TextUtils.isEmpty(editPasswordView.text)|| TextUtils.isEmpty(editEmailView.text)) {
            Toast.makeText(this@RegistarUser, R.string.login_Error, Toast.LENGTH_LONG).show()
        } // caso tenha texto ira passsar para o request ao servidor
        else {
            val request = ServiceBuilder.buildService(EndPoints::class.java)
            val username = editUsernameView.text.toString()
            val password = editPasswordView.text.toString()
            val email = editEmailView.text.toString()
            val call = request.registarUser(username = username, password = password,email=email )
            call.enqueue(object : Callback<OutputLogin> {
                override fun onResponse(call: Call<OutputLogin>, response: Response<OutputLogin>) {
                    if (response.isSuccessful) {

                        val c: OutputLogin = response.body()!!
                        if (c.status == "false") {
                            Toast.makeText(this@RegistarUser, c.MSG, Toast.LENGTH_LONG).show()
                        } else {

                            val intent = Intent(this@RegistarUser, MainActivity::class.java)
                            Toast.makeText(this@RegistarUser, R.string.Sucess_Register, Toast.LENGTH_LONG).show()
                            startActivity(intent)
                            finish()
                        }

                    }
                }

                override fun onFailure(call: Call<OutputLogin>, t: Throwable) {
                    Toast.makeText(this@RegistarUser, "${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

}