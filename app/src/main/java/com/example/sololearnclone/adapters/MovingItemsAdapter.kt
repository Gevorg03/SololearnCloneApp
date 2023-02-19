package com.example.sololearnclone.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sololearnclone.R
import com.example.sololearnclone.fragments.lessons.ItemMoveCallback.ItemTouchHelperContract
import java.util.Collections

class MovingItemsAdapter(private val data: List<String>) :
    RecyclerView.Adapter<MovingItemsAdapter.MyViewHolder>(), ItemTouchHelperContract {

    fun getResultList(): List<String> {
        return data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_moving, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.mTitle.text = data[position]
    }

    override fun getItemCount(): Int = data.size

    override fun onRowMoved(fromPosition: Int, toPosition: Int) {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(data, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(data, i, i - 1)
            }
        }
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onRowSelected(myViewHolder: MyViewHolder?) {
        myViewHolder?.rowView?.setBackgroundColor(Color.GRAY)
    }

    override fun onRowClear(myViewHolder: MyViewHolder?) {
        myViewHolder?.rowView?.setBackgroundColor(Color.WHITE)
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mTitle: TextView
        var rowView: View

        init {
            rowView = itemView
            mTitle = itemView.findViewById(R.id.tv_moving_item)
        }
    }
}