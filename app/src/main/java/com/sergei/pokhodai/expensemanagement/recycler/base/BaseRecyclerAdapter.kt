package com.sergei.pokhodai.expensemanagement.recycler.base

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sergei.pokhodai.expensemanagement.recycler.RecyclerState
import com.sergei.pokhodai.expensemanagement.recycler.holder.HolderItemState
import com.sergei.pokhodai.expensemanagement.recycler.holder.RecyclerViewHolder
import java.util.concurrent.ConcurrentHashMap

internal class BaseRecyclerAdapter {

    private val holderViewTypeMap: ConcurrentHashMap<Int, HolderItemState> = ConcurrentHashMap()

    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return getItemView(
            viewType = viewType,
            context = parent.context
        )?.let { view ->
            RecyclerViewHolder(
                view = view,
                viewType = viewType
            )
        } ?: throw IllegalArgumentException("Invalid View")
    }

    fun onBindViewHolder(
        item: RecyclerState,
        holder: RecyclerView.ViewHolder,
    ) {
        if (holder is RecyclerViewHolder && holder.viewType == item.viewType) {
            holder.bind(item)
        }
    }

    fun getItemViewType(
        item: HolderItemState,
    ): Int {
        holderViewTypeMap.getOrPut(item.viewType) { item }
        return item.viewType
    }

    private fun getItemView(
        viewType: Int,
        context: Context,
    ): View? = holderViewTypeMap[viewType]?.getView(context)
}