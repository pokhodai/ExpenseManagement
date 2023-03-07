package com.android.pokhodai.expensemanagement.ui.home.add_wallet.adapter

import android.os.Parcelable
import com.android.pokhodai.expensemanagement.base.ui.adapter.BaseListAdapter
import com.android.pokhodai.expensemanagement.databinding.ItemCategoryBinding
import com.android.pokhodai.expensemanagement.databinding.ItemEmptyBinding
import com.android.pokhodai.expensemanagement.ui.home.add_wallet.income.IncomeViewModel
import com.android.pokhodai.expensemanagement.utils.enums.Icons
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

class CategoriesAdapter @Inject constructor(): BaseListAdapter<CategoriesAdapter.Categories>() {

    private var onClickCategoryActionListener: ((Categories) -> Unit)? = null

    fun setOnClickCategoryActionListener(action: (Categories) -> Unit) {
        onClickCategoryActionListener = action
    }

    private var onLongClickActionListener: ((Int) -> Unit)? = null

    fun setOnLongClickActionListener(action: (Int) -> Unit) {
        onLongClickActionListener = action
    }

    override fun build() {
        baseViewHolder(Categories::class, ItemCategoryBinding::inflate) { item ->
            binding.run {
                txtIncome.text = item.name
                ivIncome.setImageResource(item.icon.resId)
                root.setOnClickListener {
                    onClickCategoryActionListener?.invoke(item)
                }

                root.setOnLongClickListener {
                    onLongClickActionListener?.invoke(item.id)
                    true
                }
            }
        }
    }

    @Parcelize
    data class Categories(
        val id: Int,
        val name: String,
        val icon: Icons
    ): Parcelable
}