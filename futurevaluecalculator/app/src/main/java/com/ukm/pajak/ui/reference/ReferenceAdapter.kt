package com.ukm.pajak.ui.reference

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ukm.pajak.R
import com.ukm.pajak.models.ReferenceModel

class ReferenceAdapter (private val onClickListener: OnClickListener)
    : ListAdapter<ReferenceModel, RecyclerView.ViewHolder>(ListItemCallback()) {



    class ListItemCallback : DiffUtil.ItemCallback<ReferenceModel>() {
        override fun areItemsTheSame(oldItem: ReferenceModel, newItem: ReferenceModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ReferenceModel, newItem: ReferenceModel): Boolean {
            return oldItem.title == newItem.title && oldItem.description == newItem.description
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        (holder as ListenItemViewHolder).bind(item, position)

        holder.itemView.setOnClickListener {
            onClickListener.onClick( getItem(position))
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.reference_list_item, parent, false)
        return ListenItemViewHolder(view)
    }

    class OnClickListener(val clickListener: (parameterModel: ReferenceModel) -> Unit) {
        fun onClick(parameterModel: ReferenceModel) = clickListener(parameterModel)
    }




    inner class ListenItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textViewTitle: TextView = itemView.findViewById(R.id.textViewTitle)

        fun bind(item: ReferenceModel, position : Int) {
            textViewTitle.text = item.title!!





        }
    }




}