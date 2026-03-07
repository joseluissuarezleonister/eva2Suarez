package jose.suarez.com.josesuarezeva2.Models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import jose.suarez.com.josesuarezeva2.Repository.AuditRepository

class MainViewModelFactory(private val repository: AuditRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(repository) as T
        }
        throw IllegalArgumentException("Clase ViewModel desconocida")
    }
}