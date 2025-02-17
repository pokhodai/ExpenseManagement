package com.sergei.pokhodai.expensemanagement.recycler.diff

import androidx.recyclerview.widget.DiffUtil
import com.sergei.pokhodai.expensemanagement.recycler.RecyclerState

internal class RecyclerAdapterDiffCallback : DiffUtil.ItemCallback<RecyclerState>() {
    override fun areItemsTheSame(
        oldItem: RecyclerState,
        newItem: RecyclerState
    ) = oldItem.provideId == newItem.provideId

    override fun areContentsTheSame(
        oldItem: RecyclerState,
        newItem: RecyclerState
    ) = oldItem.areContentsTheSame(newItem)
}