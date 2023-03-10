package com.android.pokhodai.expensemanagement.ui.home

import androidx.core.net.toUri
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import com.android.pokhodai.expensemanagement.R
import com.android.pokhodai.expensemanagement.base.ui.fragments.BaseFragment
import com.android.pokhodai.expensemanagement.databinding.FragmentHomeBinding
import com.android.pokhodai.expensemanagement.ui.home.adapter.WalletAdapter
import com.android.pokhodai.expensemanagement.ui.home.creater.CreaterWalletFragment
import com.android.pokhodai.expensemanagement.ui.date_picker.MonthPickerDialog
import com.android.pokhodai.expensemanagement.utils.*
import com.android.pokhodai.expensemanagement.utils.ClickUtils.setOnThrottleClickListener
import com.android.pokhodai.expensemanagement.utils.enums.Creater
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val viewModel by viewModels<HomeViewModel>()

    private val adapter by lazy { WalletAdapter() }

    override fun onBackPressed() {
        navViewModel.onClickHardDeepLink("".toUri(), R.id.report_nav_graph)
    }

    override fun setAdapter() = with(binding) {
        rvHome.itemAnimator = null
        rvHome.adapter = adapter
    }

    override fun setListeners() = with(binding) {
        btnAddNewHome.setOnClickListener {
            navigationController.navigateSafe(
                HomeFragmentDirections.actionHomeFragmentToCreaterWalletFragment(type = Creater.CREATE)
            )
        }

        srlHome.setOnRefreshListener {
            viewModel.onSwipeRefresh()
            navViewModel.onSwipeRefresh()
        }

        incMonthSelectorHome.run {
            btnLeftMonthSelector.setOnClickListener {
                viewModel.onChangePreviousOrNextMonth(MINUS)
            }

            chipDateMonthSelector.setOnThrottleClickListener {
                navigationController.navigateSafe(
                    HomeFragmentDirections.actionHomeFragmentToMonthPickerDialog(
                        viewModel.dateFlow.value.timeInMillis()
                    )
                )
            }

            btnRightMonthSelector.setOnClickListener {
                viewModel.onChangePreviousOrNextMonth(PLUS)
            }
        }

        adapter.setOnLongClickActionListener { action ->
            when (action) {
                is WalletAdapter.ActionWallet.ActionDeleteWallet -> {
                    viewModel.onClickDeleteWallet(action.id)
                }
                is WalletAdapter.ActionWallet.ActionEditWallet -> {
                    navigationController.navigateSafe(
                        HomeFragmentDirections.actionHomeFragmentToCreaterWalletFragment(
                            type = Creater.EDIT,
                            wallet = action.wallet
                        )
                    )
                }
            }

        }

        setFragmentResultListener(CreaterWalletFragment.ADD_NEW_WALLET) { _, _ ->
            viewModel.onSumIncomeAndExpense()
            navViewModel.onSwipeRefresh()
            adapter.refresh()
        }

        setFragmentResultListener(MonthPickerDialog.CHANGE_DATE) { _, bundle ->
            val timeInMillis = bundle.getLong(MonthPickerDialog.DATE)
            viewModel.onChangeMonthDate(LocalDateFormatter.from(timeInMillis))
            viewModel.onSumIncomeAndExpense()
        }
    }

    override fun setObservable() = with(viewModel) {
        dateFlow.observe(viewLifecycleOwner) {
            binding.incMonthSelectorHome.run {
                chipDateMonthSelector.text = it.MMMM_yyyy()
            }
        }

        walletsFlow.observeLatest(viewLifecycleOwner) {
            adapter.submitData(it)
        }

        adapter.loadStateFlow
            .distinctUntilChangedBy { it.refresh }
            .filter { it.refresh is LoadState.NotLoading }
            .observe(viewLifecycleOwner) {
                if (binding.srlHome.isRefreshing) {
                    binding.rvHome.scrollToPosition(0)
                    binding.srlHome.isRefreshing = false
                }
            }

        statusFlow.observe(viewLifecycleOwner) { triple ->
            binding.incStatusHome.run {
                txtIncomeStatus.text = triple.first.toString()
                txtBalanceStatus.text = triple.second.toString()
                txtExpenseStatus.text = triple.third.toString()
            }
        }
    }

    companion object {
        const val PLUS = "PLUS"
        const val MINUS = "MINUS"
    }
}