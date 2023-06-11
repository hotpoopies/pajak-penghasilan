package com.ukm.pajak.ui.apps

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.ukm.pajak.R
import com.ukm.pajak.databinding.AppItemBinding
import com.ukm.pajak.models.PajakAppModel



/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
class AppsAdapter(private val itemClickListener: ItemClickListener,
                                 private val values: List<PajakAppModel>
) : RecyclerView.Adapter<AppsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            AppItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.textViewTitle.text = (position+1).toString() + ". "+ item.problem
        holder.textViewDescription.text = "Category : " + item.category


        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick( position)
        }


    }

    interface ItemClickListener {
        fun onItemClick(position: Int)

    }

    override fun getItemCount(): Int = values.size



    private val drawables = listOf(
        R.drawable.ic_baseline_money_24_black,
        R.drawable.pajak,
        R.drawable.td,
        R.drawable.moneycalculator,
        R.drawable.bodyfatcalculator
    )


    inner class ViewHolder(binding: AppItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val textViewTitle: TextView = binding.textViewTitle
        val textViewDescription: TextView = binding.textViewDescription


        override fun toString(): String {
            return super.toString() + " '" + textViewTitle.text + "'"
        }
    }

}