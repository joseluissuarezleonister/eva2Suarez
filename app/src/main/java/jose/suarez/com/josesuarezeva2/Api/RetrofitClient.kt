package jose.suarez.com.josesuarezeva2.Api

object RetrofitClient {
    private const val BASE_URL = "https://69ab0c98e051e9456fa3308b.mockapi.io/api/eva2/"

    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}