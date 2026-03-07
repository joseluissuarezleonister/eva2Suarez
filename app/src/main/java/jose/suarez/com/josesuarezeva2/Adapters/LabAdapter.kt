package jose.suarez.com.josesuarezeva2.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import jose.suarez.com.josesuarezeva2.Entities.Laboratorio

class LabAdapter(private val onItemClick: (Laboratorio) -> Unit) :
    ListAdapter<Laboratorio, LabAdapter.LabViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LabViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_2, parent, false)
        return LabViewHolder(view)
    }

    override fun onBindViewHolder(holder: LabViewHolder, position: Int) {
        val lab = getItem(position)
        holder.bind(lab, onItemClick)
    }

    class LabViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textNombre: TextView = itemView.findViewById(android.R.id.text1)
        private val textEdificio: TextView = itemView.findViewById(android.R.id.text2)

        fun bind(lab: Laboratorio, onItemClick: (Laboratorio) -> Unit) {
            textNombre.text = lab.nombre
            textEdificio.text = "Edificio: ${lab.edificio}"

            // navegar a la lista de equipos
            itemView.setOnClickListener { onItemClick(lab) }
        }
    }

    object DiffCallback : DiffUtil.ItemCallback<Laboratorio>() {
        override fun areItemsTheSame(oldItem: Laboratorio, newItem: Laboratorio) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Laboratorio, newItem: Laboratorio) = oldItem == newItem
    }
}