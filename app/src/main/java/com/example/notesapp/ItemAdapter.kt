package com.example.notesapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.databinding.ItemLayoutBinding

class ItemAdapter(private val items: ArrayList<NoteEntity>,
                  private val updateListener: (id: Int) -> Unit,
                  private val deleteListener: (id: Int) -> Unit) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

                      class ViewHolder(binding: ItemLayoutBinding):RecyclerView.ViewHolder(binding.root){
                          val tvText = binding.tvText
                          val tvDate = binding.tvDate
                          val ivEdit = binding.ivEdit
                          val ivDelete = binding.ivDelete
                      }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.tvText.text = item.note
        holder.tvDate.text = item.date

        holder.ivEdit.setOnClickListener {
            updateListener.invoke(item.id)
        }
        holder.ivDelete.setOnClickListener {
            deleteListener.invoke(item.id)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}