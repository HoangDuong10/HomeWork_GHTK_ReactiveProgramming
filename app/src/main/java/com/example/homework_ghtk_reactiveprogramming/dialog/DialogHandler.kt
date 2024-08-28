package com.example.homework_ghtk_reactiveprogramming.dialog

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.homework_ghtk_reactiveprogramming.databinding.ItemDialogBinding
import com.example.homework_ghtk_reactiveprogramming.databinding.LayoutDialogBinding

class DialogHandler {

    companion object {
        fun createSingleItemDialog(activity: Activity, list: List<Any>, title: String,onClick: ((String) -> Unit)? = null) {
            val binding = LayoutDialogBinding.inflate(LayoutInflater.from(activity))
            binding.titleTextview.text = title
            val dialog = prepareDialog(activity, binding)
            val adapter = SingleItemAdapter(list, dialog,onClick)
            binding.rcvData.layoutManager = LinearLayoutManager(activity)
            binding.rcvData.adapter = adapter
            dialog.show()
        }

        private fun prepareDialog(activity: Activity, binding: LayoutDialogBinding): AlertDialog {
            val builder = AlertDialog.Builder(activity)
            builder.setView(binding.root)
            return builder.create()
        }
    }

    private class SingleItemAdapter(
        private val list: List<Any>,
        private val dialog: AlertDialog,
        var onClick: ((String) -> Unit)? = null
    ) : RecyclerView.Adapter<SingleItemAdapter.ViewHolder>() {

        override fun onCreateViewHolder(viewGroup: ViewGroup, positipn: Int): ViewHolder {
            val binding = ItemDialogBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
            return ViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = list[position]
            holder.binding.tvContent.text = when (item) {
                is Int -> item.toString()
                is String -> item
                else -> ""
            }
            holder.itemView.setOnClickListener {
                onClick?.invoke(holder.binding.tvContent.text.toString())
                dialog.dismiss()
            }
        }

        override fun getItemCount(): Int {
            return list.size
        }

        inner class ViewHolder(val binding: ItemDialogBinding) : RecyclerView.ViewHolder(binding.root) {


        }
    }
}