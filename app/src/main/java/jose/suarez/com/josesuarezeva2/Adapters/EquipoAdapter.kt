package jose.suarez.com.josesuarezeva2.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import jose.suarez.com.josesuarezeva2.Entities.Equipo
import jose.suarez.com.josesuarezeva2.databinding.ItemEquipoBinding

class EquipoAdapter(
    private var equipos: List<Equipo>,
    private val onEditarClick: (Equipo) -> Unit
) : RecyclerView.Adapter<EquipoAdapter.EquipoViewHolder>() {

    inner class EquipoViewHolder(val binding: ItemEquipoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun enlazar(equipo: Equipo) {
            binding.tvNombreEquipo.text = equipo.nombre
            binding.tvEstadoEquipo.text = "Estado: ${equipo.estado}"

            binding.btnEditarEquipo.setOnClickListener {
                onEditarClick(equipo)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EquipoViewHolder {
        val binding = ItemEquipoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EquipoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EquipoViewHolder, position: Int) {
        holder.enlazar(equipos[position])
    }

    override fun getItemCount(): Int = equipos.size

    fun actualizarLista(nuevaLista: List<Equipo>) {
        this.equipos = nuevaLista
        notifyDataSetChanged()
    }
    
    // Método necesario para Swipe to Delete
    fun getEquipoEnPosicion(posicion: Int): Equipo {
        return equipos[posicion]
    }
}
