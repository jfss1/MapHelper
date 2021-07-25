package intro.android.cm

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import intro.android.cm.api.EndPoints
import intro.android.cm.api.Notas
import intro.android.cm.api.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReportNoteActivity : AppCompatActivity() {
    private lateinit var editTitleView: EditText
    private lateinit var editDescriptionView: EditText
    private lateinit var shared_preferences: SharedPreferences
    private var latitude : Double = 0.0
    private var longitude : Double = 0.0

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private lateinit var lastLocation: Location

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_note)

        editTitleView = findViewById(R.id.edit_title)
        editDescriptionView = findViewById(R.id.edit_description)

        shared_preferences = getSharedPreferences("shared_preferences", Context.MODE_PRIVATE)
        // utilizar o recolher coordenadas de x em x tempo para ter a localizacao exata aqui nesta atividade
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        locationCallback= object : LocationCallback(){
            override fun onLocationResult(p0: LocationResult?) {
                super.onLocationResult(p0)
                if (p0 != null) {
                    lastLocation =p0.lastLocation


                    latitude= lastLocation.latitude
                    longitude= lastLocation.longitude
                }
            }
        }


        createLocationRequest()
    }



    fun reportar(view: View) {

        // primeiro verifica se os edit estapo prenchidos
        if (TextUtils.isEmpty(editTitleView.text) || TextUtils.isEmpty(editDescriptionView.text)) {
            Toast.makeText(this@ReportNoteActivity, R.string.login_Error, Toast.LENGTH_LONG).show()
        } // caso tenha texto ira passsar para o request ao servidor
        else {
            val request = ServiceBuilder.buildService(EndPoints::class.java)
            val latitude = latitude
            val longitude = longitude
            val title = editTitleView.text.toString()
            val description = editDescriptionView.text.toString()
            val id_utilizador = shared_preferences.getInt("id", 0)

            val call = request.inserirNota(
                id_utilizador = id_utilizador,
                title = title,
                description = description,
                latitude = latitude.toFloat(),
                longitude = longitude.toFloat()

            )

            call.enqueue(object : Callback<Notas> {
                override fun onResponse(call: Call<Notas>, response: Response<Notas>) {
                    if (response.isSuccessful) {
                        val c: Notas = response.body()!!
                        Toast.makeText(this@ReportNoteActivity, c.MSG, Toast.LENGTH_LONG).show()
                        val intent = Intent(this@ReportNoteActivity, MapsActivity::class.java)
                        startActivity(intent);
                        finish()

                    }
                }

                override fun onFailure(call: Call<Notas>, t: Throwable) {
                    Toast.makeText(this@ReportNoteActivity, "${t.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            })

        }
    }




    // funcao para resumir o check periodico da localizacao
    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }
        fusedLocationClient.requestLocationUpdates(locationRequest,locationCallback,null)
    }

    // esta funcao e para ele estar periodicamente a atualizar a localizacao do utilizador
    private fun createLocationRequest() {
        locationRequest = LocationRequest()

        locationRequest.interval = 10000//milissegundos
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }
    // on p[ause e on resume servem para parar e resumir respetivamente o check da localizacao de modo a poupar recursos do telemovel
    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locationCallback)

    }

    public override fun onResume() {
        super.onResume()
        startLocationUpdates()
    }
}