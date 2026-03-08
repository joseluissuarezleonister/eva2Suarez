package jose.suarez.com.josesuarezeva2.Models

import android.util.Log
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import jose.suarez.com.josesuarezeva2.Entities.Equipo
import jose.suarez.com.josesuarezeva2.Entities.Laboratorio
import jose.suarez.com.josesuarezeva2.Repository.AuditRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val repository: AuditRepository) : ViewModel() {

    // Lista de laboratorios observada
    val allLaboratorios: LiveData<List<Laboratorio>> = repository.allLaboratorios

    // Estados para la sincronización
    val isLoading = MutableLiveData<Boolean>()
    val statusMessage = MutableLiveData<String>()

    // Función para guardar localmente
    fun insertarLaboratorio(nombre: String, edificio: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val nuevoLab = Laboratorio(nombre = nombre, edificio = edificio)
            repository.insertLab(nuevoLab)
        }
    }

    // Función de Sincronización con MockAPI
    fun sincronizarDatos() {
        viewModelScope.launch {
            Log.d("MainViewModel", "Iniciando sincronización...")
            isLoading.value = true // Muestra ProgressBar

            try {
                val result = repository.sync() // Llama a la lógica de Retrofit
                result.onSuccess {
                    Log.d("MainViewModel", "Sincronización exitosa devolvió Success")
                    statusMessage.value = "Sincronización exitosa con la nube"
                }.onFailure { error ->
                    Log.e("MainViewModel", "La sincronización devolvió Failure: ${error.message}", error)
                    statusMessage.value = "Error: ${error.message}"
                }
            } catch (e: Exception) {
                 Log.e("MainViewModel", "Excepción fatal no capturada en sincronizarDatos: ${e.message}", e)
                 statusMessage.value = "Error crítico: ${e.message}"
            } finally {
                 Log.d("MainViewModel", "Finalizando proceso de sincronización, ocultando ProgressBar")
                 isLoading.value = false // Oculta ProgressBar
            }
        }
    }

    //AUDITORIA DE EQUIPOS
    //leer
    fun obtenerEquiposPorLab(labId: Int): LiveData<List<Equipo>> {
        return repository.getEquipos(labId)
    }

    //crear
    fun insertarEquipo(nombre: String, estado: jose.suarez.com.josesuarezeva2.Entities.EstadoEquipo, labId: Int){
        viewModelScope.launch(Dispatchers.IO){
            val nuevoEquipo = Equipo(nombre = nombre, estado = estado, laboratorioId = labId)
            repository.insertEquipo(nuevoEquipo)
        }
    }

    //editar
    fun actualizarEquipo(equipo: Equipo){
        viewModelScope.launch(Dispatchers.IO){
            repository.updateEquipo(equipo)
        }
    }

    //borrar
    fun borrarEquipo(equipo: Equipo) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteEquipo(equipo)
        }
    }


}