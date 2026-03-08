package jose.suarez.com.josesuarezeva2.Repository

import android.util.Log

import jose.suarez.com.josesuarezeva2.Api.ApiService
import jose.suarez.com.josesuarezeva2.Dao.TechAuditDao
import jose.suarez.com.josesuarezeva2.Entities.Equipo
import jose.suarez.com.josesuarezeva2.Entities.Laboratorio

class AuditRepository (
    private val dao: TechAuditDao,
    private val apiService: ApiService
) {

    //CRUD LABS
    val allLaboratorios = dao.getAllLabotarios()
    suspend fun insertLab(lab: Laboratorio)= dao.insertLaboratorio(lab)

    //Filtrar equipos por id
    fun getEquipos(labId: Int) = dao.getEquiposByLaboratorio(labId)

    suspend fun insertEquipo(equipo: Equipo) = dao.insertEquipo(equipo)
    suspend fun updateEquipo(equipo: Equipo) = dao.updateEquipo(equipo)
    suspend fun deleteEquipo(equipo: Equipo) = dao.deleteEquipo(equipo)

    //Sincronizacion
    suspend fun sync(): Result<Unit> {
        return try {
            // Leer los datos locales de Room
            val labsLocales = dao.getLaboratoriosSync()

            if (labsLocales.isEmpty()) {
                return Result.failure(Exception("No hay datos locales para sincronizar"))
            }

            // Enviar los datos a MockAPI mediante Retrofit
            //  Enviar uno por uno
            labsLocales.forEach { lab ->
                val response = apiService.syncLaboratorio(lab)
                if (!response.isSuccessful) {
                    return Result.failure(Exception("Error al sincronizar el laboratorio: ${lab.nombre}"))
                }
            }

            Result.success(Unit)
        } catch (e: Exception) {
            // Manejar errores de conexión
            Log.e("AuditRepository", "Excepción ocurrida durante sync(): ${e.message}", e)
            Result.failure(e)
        }
    }
}