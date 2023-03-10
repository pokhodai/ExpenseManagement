package com.android.pokhodai.expensemanagement.ui.settings

import android.os.Bundle
import android.view.View
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import com.android.pokhodai.expensemanagement.R
import com.android.pokhodai.expensemanagement.base.ui.fragments.BaseFragment
import com.android.pokhodai.expensemanagement.data.models.User
import com.android.pokhodai.expensemanagement.databinding.FragmentSettingsBinding
import com.android.pokhodai.expensemanagement.ui.home.adapter.WalletAdapter
import com.android.pokhodai.expensemanagement.ui.settings.adapter.SettingAdapter
import com.android.pokhodai.expensemanagement.utils.enums.Settings
import com.android.pokhodai.expensemanagement.utils.navigateSafe
import com.android.pokhodai.expensemanagement.utils.observe
import com.android.pokhodai.expensemanagement.utils.showAlert
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingsBinding>(FragmentSettingsBinding::inflate) {

    private val viewModel by viewModels<SettingsViewModel>()

    @Inject
    lateinit var adapter: SettingAdapter

    override fun onBackPressed() {
        navViewModel.onClickHardDeepLink("".toUri(), R.id.report_nav_graph)
    }

    override fun setAdapter() = with(binding) {
        rvSettings.adapter = adapter
    }

    override fun setObservable() = with(viewModel) {
        settingsFlow.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        logoutFlow.observe(viewLifecycleOwner) {

        }

        userFlow.observe(viewLifecycleOwner) { user ->
            binding.run {
                user?.let {
                    txtNameSettings.text = user.fullName()
                    txtEmailSettings.text = user.email
                    avSetting.user = user
                }
            }
        }
    }

    override fun setListeners() = with(binding) {
        adapter.setOnClickActionListener { type ->
            when (type) {
                Settings.MANAGER -> {
                    navigationController.navigateSafe(
                        SettingsFragmentDirections.actionSettingsFragmentToManagerCategoriesFragment()
                    )
                }
                Settings.LOGOUT -> {
                    showAlert(
                        getString(R.string.question_logout),
                        positiveBtnText = getString(R.string.ok),
                        negativeBtnText = getString(R.string.no)
                    ) {
                        viewModel.onLogout()
                    }
                }
                Settings.CHOOSE_LANGUAGE -> {
                    navigationController.navigateSafe(
                        SettingsFragmentDirections.actionSettingsFragmentToLanguageDialog()
                    )
                }
                Settings.ASKED_QUESTIONS -> {

                }
            }
        }
    }
}