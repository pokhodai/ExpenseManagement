package com.sergei.pokhodai.expensemanagement.ui.settings.asked

import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.sergei.pokhodai.expensemanagement.base.ui.fragments.BaseBottomSheetDialogFragment
import com.sergei.pokhodai.expensemanagement.R
import com.sergei.pokhodai.expensemanagement.databinding.DialogAskedQuestBinding
import com.sergei.pokhodai.expensemanagement.utils.UIState
import com.sergei.pokhodai.expensemanagement.utils.observe
import com.sergei.pokhodai.expensemanagement.utils.showSnackBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AskedQuestDialog :
    BaseBottomSheetDialogFragment<DialogAskedQuestBinding>(DialogAskedQuestBinding::inflate) {

    private val viewModel by viewModels<AskedQuestViewModel>()

    override fun setListeners() = with(binding){
        txtTopicAskedQuest.doAfterTextChanged {
            viewModel.onChangeTopic(it.toString())
        }

        txtDescriptionAskedQuest.doAfterTextChanged {
            viewModel.onChangeDescription(it.toString())
        }

        btnAskedQuest.setOnThrottleClickListener {
            viewModel.onClickAskQuest()
        }

        btnAskedQuest.setOnEndAnimateDoneListener {
            onBackPressed()
        }
    }

    override fun setObservable() = with(viewModel) {
        askQuestState.observe(viewLifecycleOwner) {
            binding.btnAskedQuest.setUIState(it)
            if (it is UIState.UIPreSuccess) {
                setFragmentResult(ASK_SUCCESS, bundleOf())
            }
            if (it is UIState.UIError) {
                showSnackBar(getString(R.string.network_error), binding.root)
            }
        }

        validateFlow.observe(viewLifecycleOwner) {
            binding.btnAskedQuest.isEnabled = it
        }
    }

    companion object {
        const val ASK_SUCCESS = "ASK_SUCCESS"
    }
}