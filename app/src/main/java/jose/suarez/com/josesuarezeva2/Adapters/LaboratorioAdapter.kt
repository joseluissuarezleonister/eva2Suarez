package jose.suarez.com.josesuarezeva2.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import jose.suarez.com.josesuarezeva2.Entities.Laboratorio
import jose.suarez.com.josesuarezeva2.databinding.ItemLaboratorioBinding

class LaboratorioAdapter(
    private var laboratorios: List<Laboratorio>,
    private val onClickFila: (Laboratorio) -> Unit
) : RecyclerView.Adapter<LaboratorioAdapter.LaboratorioViewHolder>() {

    inner class LaboratorioViewHolder(val binding: ItemLaboratorioBinding) : RecyclerView.ViewHolder(binding.root) {
        fun enlazar(lab: Laboratorio) {
            binding.tvNombreLab.text = lab.nombre
            binding.tvEdificioLab.text = "Edificio: ${lab.edificio}"
            
            binding.root.setOnClickListener {
                onClickFila(lab)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LaboratorioViewHolder {
        val binding = ItemLaboratorioBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LaboratorioViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LaboratorioViewHolder, position: Int) {
        holder.enlazar(laboratorios[position])
    }

    override fun getItemCount(): Int = laboratorios.size

    fun actualizarLista(nuevaLista: List<Laboratorio>) {
        this.laboratorios = nuevaLista
        notifyDataSetChanged()
    }
}
