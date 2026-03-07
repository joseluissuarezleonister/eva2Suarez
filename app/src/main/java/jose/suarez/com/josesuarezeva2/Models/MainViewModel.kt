package jose.suarez.com.josesuarezeva2.Models

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
            isLoading.value = true // Muestra ProgressBar

            val result = repository.sync() // Llama a la lógica de Retrofit

            result.onSuccess {
                statusMessage.value = "Sincronización exitosa con la nube"
            }.onFailure { error ->
                statusMessage.value = "Error: ${error.message}"
            }

            isLoading.value = false // Oculta ProgressBar
        }
    }

    //AUDITORIA DE EQUIPOS
    //leer
    fun obtenerEquiposPorLab(labId: Int): LiveData<List<Equipo>> {
        return repository.getEquipos(labId)
    }

    //crear
    fun insertarEquipo(nombre: String, estado:String, labId: Int){
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