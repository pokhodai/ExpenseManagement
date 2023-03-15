package com.android.pokhodai.expensemanagement.utils.enums

import com.android.pokhodai.expensemanagement.R

enum class Currency(val resId: Int, val nameResId: Int) {
    DOLLAR(R.string.currency_dollar, R.string.dollar),
    RUBLE(R.string.currency_ruble, R.string.russian)
}