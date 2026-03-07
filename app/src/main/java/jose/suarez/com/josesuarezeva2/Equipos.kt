package jose.suarez.com.josesuarezeva2

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import jose.suarez.com.josesuarezeva2.Adapters.EquipoAdapter
import jose.suarez.com.josesuarezeva2.Api.RetrofitClient
import jose.suarez.com.josesuarezeva2.Database.TechDatabase
import jose.suarez.com.josesuarezeva2.Entities.Equipo
import jose.suarez.com.josesuarezeva2.Models.MainViewModel
import jose.suarez.com.josesuarezeva2.Models.MainViewModelFactory
import jose.suarez.com.josesuarezeva2.Repository.AuditRepository
import jose.suarez.com.josesuarezeva2.databinding.ActivityEquiposBinding

class Equipos : AppCompatActivity() {

    private lateinit var binding: ActivityEquiposBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: EquipoAdapter
    private var labIdActual: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        binding = ActivityEquiposBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1. Obtener el ID del laboratorio seleccionado del Intent
        labIdActual = intent.getIntExtra("LAB_ID", -1)

        if (labIdActual == -1) {
            Toast.makeText(this, "Error al cargar el laboratorio", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // 2. Configurar ViewModel
        val dao = TechDatabase.getDatabase(applicationContext).techAuditDao()
        val repository = AuditRepository(dao, RetrofitClient.instance)
        val factory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]

        // 3. Configurar UI
        configurarRecyclerView()
        configurarSwipeToDelete()
        
        binding.fabAddEquipo.setOnClickListener {
            mostrarDialogoFormulario(null) // null significa Crear
        }

        // 4. Observar Datos
        viewModel.obtenerEquiposPorLab(labIdActual).observe(this) { listaEquipos ->
            adapter.actualizarLista(listaEquipos)
        }
    }

    private fun configurarRecyclerView() {
        adapter = EquipoAdapter(emptyList()) { equipoSeleccionado ->
            // Si le da clic al ícono de lapiz, abre el diálogo de editar
            mostrarDialogoFormulario(equipoSeleccionado)
        }
        binding.recyclerViewEquipos.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewEquipos.adapter = adapter
    }

    // Requerimiento Sílabo: Swipe to Delete
    private fun configurarSwipeToDelete() {
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false // No permitmos mover arriba y abajo
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val posicion = viewHolder.adapterPosition
                val equipoAEliminar = adapter.getEquipoEnPosicion(posicion)
                
                viewModel.borrarEquipo(equipoAEliminar)
                Toast.makeText(this@Equipos, "Equipo eliminado", Toast.LENGTH_SHORT).show()
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerViewEquipos)
    }

    private fun mostrarDialogoFormulario(equipoAEditar: Equipo?) {
        val esEditar = equipoAEditar != null
        val builder = AlertDialog.Builder(this)
        builder.setTitle(if (esEditar) "Editar Equipo" else "Nuevo Equipo")

        val layout = android.widget.LinearLayout(this)
        layout.orientation = android.widget.LinearLayout.VERTICAL
        layout.setPadding(50, 40, 50, 10)

        // Campo Nombre
        val inputNombre = TextInputEditText(this)
        inputNombre.hint = "Nombre del Equipo"
        if (esEditar) inputNombre.setText(equipoAEditar?.nombre)
        layout.addView(inputNombre)

        // Dropdown Enum para Estado
        val spinnerEstado = Spinner(this)
        val opcionesEstado = arrayOf("Operativo", "Dañado", "Pendiente")
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, opcionesEstado)
        spinnerEstado.adapter = spinnerAdapter
        
        if (esEditar) {
            val position = opcionesEstado.indexOf(equipoAEditar?.estado?.name)
            if (position >= 0) spinnerEstado.setSelection(position)
        }
        layout.addView(spinnerEstado)

        builder.setView(layout)

        builder.setPositiveButton("Guardar") { _, _ ->
            val nombre = inputNombre.text.toString()
            val estadoSeleccionado = spinnerEstado.selectedItem.toString()

            if (nombre.isNotEmpty()) {
                val estadoEnum = jose.suarez.com.josesuarezeva2.Entities.EstadoEquipo.valueOf(estadoSeleccionado)
                if (esEditar) {
                    val equipoEditado = equipoAEditar!!.copy(nombre = nombre, estado = estadoEnum)
                    viewModel.actualizarEquipo(equipoEditado)
                } else {
                    viewModel.insertarEquipo(nombre, estadoEnum, labIdActual)
                }
            } else {
                Toast.makeText(this, "Debe ingresar el nombre", Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton("Cancelar", null)
        builder.show()
    }
}