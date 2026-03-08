package jose.suarez.com.josesuarezeva2.Api

import jose.suarez.com.josesuarezeva2.Entities.Equipo
import jose.suarez.com.josesuarezeva2.Entities.Laboratorio
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    // POST laboratorio
    @POST("laboratorios")
    suspend fun syncLaboratorio(@Body laboratorio: Laboratorio): Response<Laboratorio>

    // POST equipo
    @POST("equipos")
    suspend fun syncEquipo(@Body equipo: Equipo): Response<Equipo>

    // POST varios laboratorios
    @POST("sync_all")
    suspend fun syncAllData(@Body data: List<Laboratorio>): Response<Unit>

    // Get de laboratorio
    @GET("laboratorios")
    suspend fun getRemoteLaboratorios(): Response<List<Laboratorio>>

    // DELETE laboratorio
    @DELETE("laboratorios/{id}")
    suspend fun deleteRemoteLaboratorio(@Path("id") id: String): Response<Unit>

    // Get de equipos
    @GET("equipos")
    suspend fun getRemoteEquipos(): Response<List<Equipo>>

    // DELETE equipo
    @DELETE("equipos/{id}")
    suspend fun deleteRemoteEquipo(@Path("id") id: String): Response<Unit>
}