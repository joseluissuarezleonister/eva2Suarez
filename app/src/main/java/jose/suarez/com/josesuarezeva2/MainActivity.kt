package jose.suarez.com.josesuarezeva2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputEditText
import jose.suarez.com.josesuarezeva2.Adapters.LaboratorioAdapter
import jose.suarez.com.josesuarezeva2.Api.RetrofitClient
import jose.suarez.com.josesuarezeva2.Database.TechDatabase
import jose.suarez.com.josesuarezeva2.Models.MainViewModel
import jose.suarez.com.josesuarezeva2.Models.MainViewModelFactory
import jose.suarez.com.josesuarezeva2.Repository.AuditRepository
import jose.suarez.com.josesuarezeva2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: LaboratorioAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dao = TechDatabase.getDatabase(applicationContext).techAuditDao()
        val repository = AuditRepository(dao, RetrofitClient.instance)
        val factory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]

        configurarRecyclerView()
        configurarBotones()
        observarViewModel()
    }

    private fun configurarRecyclerView() {
        adapter = LaboratorioAdapter(emptyList()) { lab ->
            val intent = Intent(this, Equipos::class.java)
            intent.putExtra("LAB_ID", lab.id)
            startActivity(intent)
        }
        binding.listaLabs.layoutManager = LinearLayoutManager(this)
        binding.listaLabs.adapter = adapter
    }

    private fun configurarBotones() {
        binding.botonAgregarLab.setOnClickListener {
            mostrarDialogoCrearLab()
        }

        binding.botonSincronizar.setOnClickListener {
            Log.d("MainActivity", "Botón Sincronizar presionado por el usuario")
            viewModel.sincronizarDatos()
        }
    }

    private fun observarViewModel() {
        viewModel.isLoading.observe(this) { validando ->
            if (validando) {
                binding.barraProgressLab.visibility = View.VISIBLE
                binding.botonSincronizar.isEnabled = false
            } else {
                binding.barraProgressLab.visibility = View.GONE
                binding.botonSincronizar.isEnabled = true
            }
        }

        viewModel.statusMessage.observe(this) { mensaje ->
            Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
        }

        viewModel.allLaboratorios.observe(this) { listaLabs ->
            adapter.actualizarLista(listaLabs)
        }
    }

    private fun mostrarDialogoCrearLab() {
        val builder = AlertDialog.Builder(this, R.style.CustomAlertDialogTheme)
        builder.setTitle("Nuevo Laboratorio")

        val layout = android.widget.LinearLayout(this)
        layout.orientation = android.widget.LinearLayout.VERTICAL
        layout.setPadding(50, 40, 50, 10)

        val colorText = android.graphics.Color.parseColor("#2e2e2e")

        val inputNombre = TextInputEditText(this)
        inputNombre.hint = "Nombre del Lab (Ej: Redes)"
        inputNombre.setTextColor(colorText)
        inputNombre.setHintTextColor(colorText)
        inputNombre.backgroundTintList = android.content.res.ColorStateList.valueOf(colorText)
        layout.addView(inputNombre)

        val inputEdificio = TextInputEditText(this)
        inputEdificio.hint = "Edificio (Ej: Bloque B)"
        inputEdificio.setTextColor(colorText)
        inputEdificio.setHintTextColor(colorText)
        inputEdificio.backgroundTintList = android.content.res.ColorStateList.valueOf(colorText)
        layout.addView(inputEdificio)

        builder.setView(layout)

        builder.setPositiveButton("Guardar") { _, _ ->
            val nombre = inputNombre.text.toString()
            val edificio = inputEdificio.text.toString()

            if (nombre.isNotEmpty() && edificio.isNotEmpty()) {
                viewModel.insertarLaboratorio(nombre, edificio)
            } else {
                Toast.makeText(this, "Debe llenar ambos campos", Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton("Cancelar", null)
        builder.show()
    }
}