package com.android.pokhodai.expensemanagement.ui.settings.manager

import androidx.fragment.app.clearFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import com.android.pokhodai.expensemanagement.base.ui.fragments.BaseFragment
import com.android.pokhodai.expensemanagement.databinding.FragmentManagerCategoriesBinding
import com.android.pokhodai.expensemanagement.ui.home.creater.expense.creater_category.CreaterCategoryFragment
import com.android.pokhodai.expensemanagement.ui.settings.manager.adapter.ManagerCategoriesAdapter
import com.android.pokhodai.expensemanagement.utils.ClickUtils.setOnThrottleClickListener
import com.android.pokhodai.expensemanagement.utils.enums.Creater
import com.android.pokhodai.expensemanagement.utils.navigateSafe
import com.android.pokhodai.expensemanagement.utils.observe
import com.android.pokhodai.expensemanagement.utils.touch_helpers.ItemTouchHelperCallback
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ManagerCategoriesFragment :
    BaseFragment<FragmentManagerCategoriesBinding>(FragmentManagerCategoriesBinding::inflate) {

    override val isBnvVisible: Boolean = false

    @Inject
    lateinit var adapter: ManagerCategoriesAdapter

    @Inject
    lateinit var helperCallback: ItemTouchHelperCallback

    private val viewModel by viewModels<ManagerCategoriesViewModel>()

    override fun setListeners() = with(binding) {
        srlManagerCategories.setOnRefreshListener {
            viewModel.onSwipeRefresh()
        }

        btnAddNewManagerCategories.setOnThrottleClickListener {
            navigationController.navigateSafe(
                ManagerCategoriesFragmentDirections.actionManagerCategoriesFragmentToCreaterCategoryNavGraph(
                    type = Creater.CREATE,
                    expense = null,
                )
            )
        }

        adapter.setOnClickEditActionListener {
            navigationController.navigateSafe(
                ManagerCategoriesFragmentDirections.actionManagerCategoriesFragmentToCreaterCategoryNavGraph(
                    type = Creater.EDIT,
                    expense = it
                )
            )
            viewModel.onSwipeRefresh()
        }

        tbManagerCategories.setNavigationOnClickListener {
            onBackPressed()
        }

        helperCallback.setOnItemDismissActionListener { position ->
            viewModel.onDeleteExpense(position)
            adapter.notifyItemRemoved(position)
        }

        helperCallback.setOnMoveActionListener { oldPosition, newPosition ->
            viewModel.onSwapAndUpdateExpenses(oldPosition, newPosition)
            adapter.notifyItemMoved(oldPosition, newPosition)
        }

        setFragmentResultListener(CreaterCategoryFragment.UPDATE_CATEGORY_SUCCESS) { _, _ ->
            viewModel.onSwipeRefresh()
            clearFragmentResult(CreaterCategoryFragment.UPDATE_CATEGORY_SUCCESS)
        }
    }

    override fun setAdapter() = with(binding) {
        rvManagerCategories.adapter = adapter
        ItemTouchHelper(helperCallback).run {
            attachToRecyclerView(rvManagerCategories)
        }
    }

    override fun setObservable() = with(viewModel) {
        refreshFlow.observe(viewLifecycleOwner) {
            binding.srlManagerCategories.isRefreshing = it
        }

        expensesFlow.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }
}