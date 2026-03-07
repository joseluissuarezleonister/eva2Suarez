package jose.suarez.com.josesuarezeva2.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "laboratorio")
data class Laboratorio (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val edificio: String
)