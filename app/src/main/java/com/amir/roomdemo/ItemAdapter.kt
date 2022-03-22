package com.amir.roomdemo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.amir.roomdemo.databinding.ItemsRowBinding

//unit is like void in java, it returns nothing
//in deleteListener and Update Listener, we pass the id that we want to update
class ItemAdapter(
    private val items: ArrayList<EmployeeEntity>,
    private val updateListener: (id: Int) -> Unit,//we say updateListener is from Unit type private val deleteListener: (id: Int) -> Unit
    private val deleteListener: (id: Int) -> Unit
) :
    RecyclerView.Adapter<ItemAdapter.mViewHolder>() {
    //returning the viewHolder, which we have created (mViewHolder)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): mViewHolder {
        return mViewHolder(
            ItemsRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: mViewHolder, position: Int) {
        val context = holder.itemView.context
        val item = items[position]
        holder.tvName.text = item.name.toString()
        holder.tvEmail.text = item.email.toString()

        if (position % 2 == 0) {
            holder.llMain.setBackgroundColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.colorLightGray
                )
            )
        } else {
            holder.llMain.setBackgroundColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.white
                )
            )
        }
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

    class mViewHolder(binding: ItemsRowBinding) : RecyclerView.ViewHolder(binding.root) {
        val llMain = binding.llMain
        val tvName = binding.tvName
        val tvEmail = binding.tvEmail
        val ivEdit = binding.ivEdit
        val ivDelete = binding.ivDelete

    }


}