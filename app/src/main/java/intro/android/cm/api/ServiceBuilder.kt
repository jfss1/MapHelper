package intro.android.cm.api

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ServiceBuilder {
    private val client = OkHttpClient.Builder().build()

    var gson = GsonBuilder()
            .setLenient()
            .create()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://maphelper.000webhostapp.com")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(client)
        .build()

    fun <T> buildService(service: Class<T>): T{
        return retrofit.create(service)
    }
}