package jose.suarez.com.josesuarezeva2.Dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import jose.suarez.com.josesuarezeva2.Entities.Equipo
import jose.suarez.com.josesuarezeva2.Entities.Laboratorio

@Dao
interface TechAuditDao {
    //Gestion de Labotarios
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLaboratorio(laboratorio: Laboratorio): Long

    @Query("SELECT * FROM laboratorio")
    fun getAllLabotarios(): LiveData<List<Laboratorio>>

    //Sincronizacion de get
    @Query("SELECT  * FROM laboratorio")
    suspend fun getLaboratoriosSync(): List<Laboratorio>

    @Query("SELECT * FROM equipo")
    suspend fun getEquiposSync(): List<Equipo>

    //Auditorio de equipos
    //Crear
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEquipo(equipo: Equipo): Long

    //Filtrar equipos por id de lab
    @Query("SELECT * FROM equipo WHERE laboratorioId = :labId")
    fun getEquiposByLaboratorio(labId: Int): LiveData<List<Equipo>>

    //Editar
    @Update
    suspend fun updateEquipo(equipo: Equipo): Int

    //Eliminar
    @Delete
    suspend fun deleteEquipo(equipo: Equipo): Int
}