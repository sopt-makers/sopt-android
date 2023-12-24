package org.sopt.official.feature.poke

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.sopt.official.R
import org.sopt.official.data.model.poke.response.PokeMessageResponse
import org.sopt.official.databinding.FragmentPokeMessageBottomSheetDialogBinding
import org.sopt.official.feature.poke.enums.MessageType

@AndroidEntryPoint
class PokeMessageBottomSheetDialogFragment(
    private val messageType: MessageType,
) : BottomSheetDialogFragment() {

    private var _binding: FragmentPokeMessageBottomSheetDialogBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<PokeMessageViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPokeMessageBottomSheetDialogBinding.inflate(inflater, container, false)

        // Set the style
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)

        initViewModel(messageType)
        initStateFlowValues()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initViewModel(messageType: MessageType) {
        viewModel.getPokeMessage(messageType)
    }

    private fun initStateFlowValues() {
        viewModel.apply {
            pokeMessage.flowWithLifecycle(lifecycle)
                .onEach {
                    it?.let {
                        initPokeMessageView(it)
                    }
                }
                .launchIn(lifecycleScope)
        }
    }

    private fun initPokeMessageView(pokeMessage: PokeMessageResponse) {
        val messages = pokeMessage.messages
        with(binding) {
            val messageTextViews = listOf(tvMessage1, tvMessage2, tvMessage3, tvMessage4, tvMessage5)
            messages.take(messageTextViews.size).forEachIndexed { index, message ->
                messageTextViews[index].text = message.content
            }
        }
    }
}
