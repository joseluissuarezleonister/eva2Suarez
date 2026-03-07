package jose.suarez.com.josesuarezeva2.Database

import androidx.room.TypeConverter
import jose.suarez.com.josesuarezeva2.Entities.EstadoEquipo

class Converters {
    @TypeConverter
    fun toEstadoEquipo(value: String): EstadoEquipo {
        return enumValueOf<EstadoEquipo>(value)
    }

    @TypeConverter
    fun fromEstadoEquipo(value: EstadoEquipo): String {
        return value.name
    }
}
