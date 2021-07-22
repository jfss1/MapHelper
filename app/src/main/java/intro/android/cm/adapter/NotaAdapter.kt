package intro.android.cm.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import intro.android.cm.R
import intro.android.cm.entities.Nota

class NotaAdapter internal constructor(
    context: Context,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<NotaAdapter.NotesViewHolder>() {


    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var notes = emptyList<Nota>()

    inner class NotesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        val notesItemView: TextView = itemView.findViewById(R.id.textView_Recycler_item)

        // isto serve para dar um onClick aos items da lista
        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val current = notes[position]
                listener.onItemClick(current.id,current.title, current.description)
            }
        }
    }
    // esta interface e a maneira como cominamos com a activity principal
    interface OnItemClickListener {
        // todos estes parametros sao os parametros enviados para a actividade principal
        fun onItemClick(id:Int?,title: String,description:String)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_item, parent, false)
        return NotesViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        val current = notes[position]
        holder.notesItemView.text =
            current.title
    }

    internal fun setNotes(notes: List<Nota>) {
        this.notes = notes
        notifyDataSetChanged()
    }


    override fun getItemCount() = notes.size
}