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
            val equiposLocales = dao.getEquiposSync()

            if (labsLocales.isEmpty() && equiposLocales.isEmpty()) {
                return Result.failure(Exception("No hay datos locales para sincronizar"))
            }

            // 1. Limpiar Laboratorios Remotos (MockAPI)
            val remoteLabsResponse = apiService.getRemoteLaboratorios()
            if (remoteLabsResponse.isSuccessful) {
                val remoteLabs = remoteLabsResponse.body() ?: emptyList()
                remoteLabs.forEach { remoteLab ->
                    apiService.deleteRemoteLaboratorio(remoteLab.id.toString())
                }
            }

            // 2. Limpiar Equipos Remotos (MockAPI)
            val remoteEquiposResponse = apiService.getRemoteEquipos()
            if (remoteEquiposResponse.isSuccessful) {
                val remoteEquipos = remoteEquiposResponse.body() ?: emptyList()
                remoteEquipos.forEach { remoteEquipo ->
                    apiService.deleteRemoteEquipo(remoteEquipo.id.toString())
                }
            }

            // 3. Enviar los laboratorios locales a MockAPI
            labsLocales.forEach { lab ->
                val response = apiService.syncLaboratorio(lab)
                if (!response.isSuccessful) {
                    return Result.failure(Exception("Error al sincronizar el laboratorio: ${lab.nombre}"))
                }
            }

            // 4. Enviar los equipos locales a MockAPI
            equiposLocales.forEach { equipo ->
                val response = apiService.syncEquipo(equipo)
                if (!response.isSuccessful) {
                    return Result.failure(Exception("Error al sincronizar el equipo: ${equipo.nombre}"))
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