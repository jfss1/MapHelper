package intro.android.cm.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import intro.android.cm.R
import intro.android.cm.entities.Nota

class NotaAdapter internal constructor(context: Context, private val listener: OnItemClickListener): RecyclerView.Adapter<NotaAdapter.NotaViewHolder>(){
    private val inflater : LayoutInflater = LayoutInflater.from(context)
    private var notas = emptyList<Nota>()

    inner class NotaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
            View.OnClickListener {

        val notesItemView: TextView = itemView.findViewById(R.id.textView_Recycler_item)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val current = notas[position]
                listener.onItemClick(current.id,current.titulo, current.descricao)
            }
        }
    }

    interface OnItemClickListener{
        fun onItemClick(id:Int?, titulo:String, descricao:String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotaViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_item, parent,false)
        return NotaViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NotaViewHolder, position: Int) {
        val current = notas[position]
        holder.notesItemView.text = current.titulo
    }

    internal fun setNotas(notas: List<Nota>){
        this.notas = notas
        notifyDataSetChanged()
    }

    override fun getItemCount() = notas.size
}