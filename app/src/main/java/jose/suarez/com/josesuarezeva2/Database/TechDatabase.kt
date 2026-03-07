package jose.suarez.com.josesuarezeva2.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import jose.suarez.com.josesuarezeva2.Dao.TechAuditDao
import jose.suarez.com.josesuarezeva2.Entities.Equipo
import jose.suarez.com.josesuarezeva2.Entities.Laboratorio

@Database(entities = [Laboratorio::class, Equipo::class], version = 1)
abstract class TechDatabase : RoomDatabase() {
    abstract fun techAuditDao(): TechAuditDao

    // 3. Configura el Singleton
    companion object {
        @Volatile
        private var INSTANCE: TechDatabase? = null

        fun getDatabase(context: Context): TechDatabase {
            // Si la instancia ya existe, la devuelve. Si no, la crea.
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TechDatabase::class.java,
                    "techaudit_database_suarez"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}