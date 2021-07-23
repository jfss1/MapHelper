package intro.android.cm.api

import retrofit2.Call
import retrofit2.http.*

interface EndPoints {
    @GET("/myslim/api/users")
    fun getUsers(): Call<List<User>>

    @GET("/myslim/api/users/{id}")
    fun getUserById(@Path("id") id: Int): Call<User>



    @FormUrlEncoded
    @POST("/myslim/api/users_login")
    fun login(@Field("username") username: String?, @Field("password") password: String?): Call<OutputLogin>


    @FormUrlEncoded
    @POST("/myslim/api/users_register")
    fun registarUser(@Field("username") username: String?, @Field("password") password: String?, @Field("email") email: String?): Call<OutputLogin>





}