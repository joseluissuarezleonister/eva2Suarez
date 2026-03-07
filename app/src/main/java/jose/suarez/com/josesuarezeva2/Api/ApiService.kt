package jose.suarez.com.josesuarezeva2.Api

import jose.suarez.com.josesuarezeva2.Entities.Laboratorio
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    // POST laboratorio
    suspend fun syncLaboratorio(@Body laboratorio: Laboratorio): Response<Laboratorio>

    // POST varios laboratorios
    @POST("sync_all")
    suspend fun syncAllData(@Body data: List<Laboratorio>): Response<Unit>

    // Get de laboratorio
    @GET("laboratorios")
    suspend fun getRemoteLaboratorios(): Response<List<Laboratorio>>
}