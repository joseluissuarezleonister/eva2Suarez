package jose.suarez.com.josesuarezeva2.Entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "equipo",
    foreignKeys = [
        ForeignKey(
            entity = Laboratorio::class,
            parentColumns = ["id"],
            childColumns = ["laboratorioId"],
            onDelete = ForeignKey.CASCADE
        )
    ])
data class Equipo (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val estado: EstadoEquipo,
    val laboratorioId: Int
)