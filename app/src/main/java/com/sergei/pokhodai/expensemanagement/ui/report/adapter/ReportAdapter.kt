package com.sergei.pokhodai.expensemanagement.ui.report.adapter

import android.annotation.SuppressLint
import com.sergei.pokhodai.expensemanagement.base.ui.adapter.BaseListAdapter
import com.sergei.pokhodai.expensemanagement.data.models.ReportWallet
import com.sergei.pokhodai.expensemanagement.R
import com.sergei.pokhodai.expensemanagement.databinding.ItemReportBinding
import com.sergei.pokhodai.expensemanagement.databinding.ItemWalletEmptyBinding
import java.math.RoundingMode
import java.text.DecimalFormat
import javax.inject.Inject

class ReportAdapter @Inject constructor(): BaseListAdapter<ReportAdapter.ItemReport>() {

    @SuppressLint("SetTextI18n")
    override fun build() {
        baseViewHolder(ItemReport.ReportTypesWallet::class, ItemReportBinding::inflate) { item ->

            val df = DecimalFormat("#.##")
            df.roundingMode = RoundingMode.DOWN

            binding.run {
                ivReport.setImageResource(item.reportWallet.icon.resId)
                txtNameReport.text = item.reportWallet.name
                txtPercentReport.text = "${df.format(item.percent)}%"
                txtSumReport.text = "${item.reportWallet.sum}${root.context.getString(item.reportWallet.currency.resId)}"
                txtCountReport.text = root.context.getString(R.string.report_transactions, item.reportWallet.total.toString())
            }
        }

        baseViewHolder(ItemReport.EmptyReport::class, ItemWalletEmptyBinding::inflate) { item ->
            binding.txtWalletEmpty.text = binding.root.context.getString(R.string.home_item_wallet_empty)
        }
    }

    sealed class ItemReport {
        data class ReportTypesWallet(
            val reportWallet: ReportWallet,
            val percent: Float
        ): ItemReport()

        object EmptyReport: ItemReport()
    }
}