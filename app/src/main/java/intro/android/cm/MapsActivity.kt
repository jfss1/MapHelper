package intro.android.cm
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import intro.android.cm.api.EndPoints
import intro.android.cm.api.Notas
import intro.android.cm.api.ServiceBuilder

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//atribuir strings aos que faltam(LEMBRAR)
class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var notas: List<Notas>
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback


    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private lateinit var shared_preferences: SharedPreferences
    private val editMapNoteActivityRequestCode = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        shared_preferences = getSharedPreferences("shared_preferences", Context.MODE_PRIVATE)
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult?) {
                super.onLocationResult(p0)
                if (p0 != null) {
                    lastLocation = p0.lastLocation
                    var loc = LatLng(lastLocation.latitude, lastLocation.longitude)
                    //  mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc,15.0f))
                    findViewById<TextView>(R.id.textViewMapsCoordenadas).setText("Lat: " + loc.latitude + " - Long: " + loc.longitude)
                }
            }
        }


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.getNotas()
        var position: LatLng
        val id_user = shared_preferences.getInt("id", 0)
        call.enqueue(object : Callback<List<Notas>> {
            override fun onResponse(call: Call<List<Notas>>, response: Response<List<Notas>>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@MapsActivity, R.string.Loading_Notes, Toast.LENGTH_LONG).show()
                    notas = response.body()!!
                    for (nota in notas) {
                        position = LatLng(nota.latitude.toDouble(), nota.longitude.toDouble())
                        if (nota.id_utilizador == id_user) {
                            mMap.addMarker(
                                MarkerOptions().position(position).title(nota.id.toString())
                                    .snippet(nota.title + "-" + nota.description)
                            )
                                .setIcon(
                                    BitmapDescriptorFactory.defaultMarker(
                                        BitmapDescriptorFactory.HUE_BLUE
                                    )
                                )
                        } else {
                            mMap.addMarker(
                                MarkerOptions().position(position).title(nota.id.toString())
                                    .snippet(nota.title + "-" + nota.description)
                            )
                        }

                    }
                }
            }

            override fun onFailure(call: Call<List<Notas>>, t: Throwable) {
                Toast.makeText(this@MapsActivity, "${t.message}", Toast.LENGTH_LONG).show()
            }
        })


        val fab = findViewById<FloatingActionButton>(R.id.buttonReportar)
        fab.setOnClickListener {
            val intent = Intent(this, ReportNoteActivity::class.java)

            startActivity(intent)
        }

        // para filtar pode se so apagar todos os filtros do mapa e adicionar so os que estao a X distancia
        val buttonFiltro = findViewById<Button>(R.id.buttonFiltrar)
        buttonFiltro.setOnClickListener {

            mMap.clear()
            val request = ServiceBuilder.buildService(EndPoints::class.java)
            val call = request.getNotas()
            var position: LatLng
            val id_user = shared_preferences.getInt("id", 0)
            call.enqueue(object : Callback<List<Notas>> {
                override fun onResponse(call: Call<List<Notas>>, response: Response<List<Notas>>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@MapsActivity, R.string.Filter, Toast.LENGTH_LONG).show()
                        notas = response.body()!!
                        for (nota in notas) {
                            position = LatLng(nota.latitude.toDouble(), nota.longitude.toDouble())
                            if (calculateDistance(
                                    position.latitude,
                                    position.longitude,
                                    lastLocation.latitude,
                                    lastLocation.longitude
                                ) < 1000
                            ) {
                                if (nota.id_utilizador == id_user) {
                                    mMap.addMarker(
                                        MarkerOptions().position(position).title(nota.id.toString())
                                            .snippet(nota.title + "-" + nota.description)
                                    )
                                        .setIcon(
                                            BitmapDescriptorFactory.defaultMarker(
                                                BitmapDescriptorFactory.HUE_BLUE
                                            )
                                        )
                                } else {
                                    mMap.addMarker(
                                        MarkerOptions().position(position).title(nota.id.toString())
                                            .snippet(nota.title + "-" + nota.description)
                                    )
                                }

                            }

                        }


                    }
                }

                override fun onFailure(call: Call<List<Notas>>, t: Throwable) {
                    Toast.makeText(this@MapsActivity, "${t.message}", Toast.LENGTH_LONG).show()
                }
            })

        }

        val buttonFiltroUser = findViewById<Button>(R.id.buttonFiltrarUser)
        buttonFiltroUser.setOnClickListener {

            mMap.clear()
            val request = ServiceBuilder.buildService(EndPoints::class.java)
            val call = request.getNotas()
            var position: LatLng
            val id_user = shared_preferences.getInt("id", 0)
            call.enqueue(object : Callback<List<Notas>> {
                override fun onResponse(call: Call<List<Notas>>, response: Response<List<Notas>>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@MapsActivity, R.string.Filter_Users, Toast.LENGTH_LONG).show()
                        notas = response.body()!!
                        for (nota in notas) {
                            position = LatLng(nota.latitude.toDouble(), nota.longitude.toDouble())

                            if (nota.id_utilizador == id_user) {
                                mMap.addMarker(
                                    MarkerOptions().position(position).title(nota.id.toString())
                                        .snippet(nota.title + "-" + nota.description)
                                ).setIcon( BitmapDescriptorFactory.defaultMarker( BitmapDescriptorFactory.HUE_BLUE ))
                            }

                        }


                    }
                }

                override fun onFailure(call: Call<List<Notas>>, t: Throwable) {
                    Toast.makeText(this@MapsActivity, "${t.message}", Toast.LENGTH_LONG).show()
                }
            })

        }


// para dar reset e limpar o mMap e correr a primeira call de novo
        val buttonReset = findViewById<Button>(R.id.buttonReset)
        buttonReset.setOnClickListener {
            resetMap()
        }
        createLocationRequest()

    }


    override fun onInfoWindowClick(p0: Marker?) {
        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call: Call<List<Notas>> = request.getNotasById(p0!!.title)
        val id_user = shared_preferences.getInt("id", 0)
        call.enqueue(object : Callback<List<Notas>> {
            override fun onResponse(call: Call<List<Notas>>, response: Response<List<Notas>>) {
                if (response.isSuccessful) {
                    notas = response.body()!!
                    for (nota in notas) {
                        if (nota.id_utilizador == id_user) {
                            val intent = Intent(this@MapsActivity, EditMapNoteActivity::class.java)
                            intent.putExtra("ID", nota.id.toString())
                            intent.putExtra("TITLE", nota.title)
                            intent.putExtra("DESC", nota.description)
                            intent.putExtra("LAN", nota.latitude)
                            intent.putExtra("LON", nota.longitude)
                            intent.putExtra("ID_USER", nota.id_utilizador)
                            @Suppress("DEPRECATION")
                            startActivityForResult(intent, editMapNoteActivityRequestCode)
                        } else {
                            Toast.makeText( this@MapsActivity, R.string.Edit_error, Toast.LENGTH_LONG ).show()

                        }

                    }
                }
            }

            override fun onFailure(call: Call<List<Notas>>, t: Throwable) {
                Toast.makeText(this@MapsActivity, "${t.message}", Toast.LENGTH_LONG).show()
            }
        })


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == editMapNoteActivityRequestCode && resultCode == Activity.RESULT_CANCELED) {
// caso nao detete qualquer texto devolve um toast a dizer que estava vazia
            Toast.makeText(
                applicationContext,
                R.string.No_Changes,
                Toast.LENGTH_LONG
            ).show()
            // aqui deteta se o reply vem da atividade de editar/ apagar
        } else if (requestCode == editMapNoteActivityRequestCode && resultCode == Activity.RESULT_OK) {
            resetMap()

        }

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera


        // exemplo de como criar um karcador e depois adicionar lo ao mapa
        // val sydney = LatLng(-34.0, 151.0)
        // mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        // ------------------
        // mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
// NECESSARIO PARA PORDER FAZER OVERRIDE DE MODO A ESCOLHER AS NOTAS
        mMap.setOnInfoWindowClickListener(this)
        setUpMap()
    }


    private fun setUpMap() {
        // promp para dar a permissao a aplicacao para ter a localizacao do telemovel
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
        } else {
            // permite que a localizacao esteja sempre presente no maps (ponto azul)
            mMap.isMyLocationEnabled = true
// ao pegar na localizacao do utilizador ira mover a camara para esse mesmo ponto
            fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
                //3
                if (location != null) {
                    lastLocation = location

                    val currentLatLng = LatLng(location.latitude, location.longitude)
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
                }
            }
        }
    }

    private fun resetMap() {

        mMap.clear()
        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.getNotas()


        var position: LatLng
        val id_user = shared_preferences.getInt("id", 0)


        call.enqueue(object : Callback<List<Notas>> {
            override fun onResponse(call: Call<List<Notas>>, response: Response<List<Notas>>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@MapsActivity, R.string.Loading_Notes, Toast.LENGTH_LONG).show()
                    notas = response.body()!!
                    for (nota in notas) {
                        position = LatLng(nota.latitude.toDouble(), nota.longitude.toDouble())
                        if (nota.id_utilizador == id_user) {
                            mMap.addMarker(
                                MarkerOptions().position(position).title(nota.id.toString())
                                    .snippet(nota.title + "-" + nota.description)
                            )
                                .setIcon(
                                    BitmapDescriptorFactory.defaultMarker(
                                        BitmapDescriptorFactory.HUE_BLUE
                                    )
                                )
                        } else {
                            mMap.addMarker(
                                MarkerOptions().position(position).title(nota.id.toString())
                                    .snippet(nota.title + "-" + nota.description)
                            )
                        }
                    }

                }
            }

            override fun onFailure(call: Call<List<Notas>>, t: Throwable) {
                Toast.makeText(this@MapsActivity, "${t.message}", Toast.LENGTH_LONG).show()
            }
        })


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
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    // esta funcao e para ele estar periodicamente a atualizar a localizacao do utilizador
    private fun createLocationRequest() {
        locationRequest = LocationRequest()

        locationRequest.interval = 10000
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

    private fun calculateDistance(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Float {

        val results = FloatArray(1);
        Location.distanceBetween(lat1, lng1, lat2, lng2, results)

        return results[0];
    }

    fun onClickLogin(view: View){
        val shared_preferences_edit: SharedPreferences.Editor =
                shared_preferences.edit()
        shared_preferences_edit.putBoolean("loggedIn", false)
        shared_preferences_edit.apply()
        var intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
    }

    fun onClickAnotacao(view: View){
        var intent = Intent(this, Anotacao::class.java)
        startActivity(intent)
    }

}