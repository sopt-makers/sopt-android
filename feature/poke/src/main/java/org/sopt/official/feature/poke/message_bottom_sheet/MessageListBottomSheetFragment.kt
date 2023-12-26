package org.sopt.official.feature.poke.message_bottom_sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.sopt.official.common.view.toast
import org.sopt.official.domain.poke.entity.PokeMessageList
import org.sopt.official.domain.poke.type.PokeMessageType
import org.sopt.official.feature.poke.UiState
import org.sopt.official.feature.poke.databinding.FragmentMessageListBottomSheetBinding

@AndroidEntryPoint
class MessageListBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentMessageListBottomSheetBinding? = null
    private val binding: FragmentMessageListBottomSheetBinding get() = requireNotNull(_binding)
    private lateinit var viewModel: MessageListBottomSheetViewModel

    var pokeMessageType: PokeMessageType? = null
    var onClickMessageListItem: ((message: String) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMessageListBottomSheetBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(MessageListBottomSheetViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pokeMessageType?.let { viewModel.getPokeMessageList(it) }
        launchPokeMessageListUiStateFlow()
    }

    private fun launchPokeMessageListUiStateFlow() {
        viewModel.pokeMessageListUiState
            .flowWithLifecycle(lifecycle)
            .onEach {
                when (it) {
                    is UiState.Loading -> "Loading"
                    is UiState.Success<PokeMessageList> -> initMessageListRecyclerView(it.data.messages)
                    is UiState.ApiError -> if (it.responseMessage.isNotBlank()) toast(it.responseMessage)
                    is UiState.Failure -> it.throwable.message?.let { toast(it) }
                }
            }
            .launchIn(lifecycleScope)
    }

    private fun initMessageListRecyclerView(data: List<PokeMessageList.PokeMessage>) {
        binding.recyclerView.adapter = MessageListRecyclerAdapter(data, messageListItemClickListener)
    }

    private val messageListItemClickListener = MessageItemClickListener { message ->
        onClickMessageListItem?.let { it(message) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    class Builder {
        private val bottomSheet = MessageListBottomSheetFragment()
        fun create(): MessageListBottomSheetFragment = bottomSheet

        fun setMessageListType(pokeMessageType: PokeMessageType): Builder {
            bottomSheet.pokeMessageType = pokeMessageType
            return this
        }

        fun onClickMessageListItem(event: (message: String) -> Unit): Builder {
            bottomSheet.onClickMessageListItem = event
            return this
        }
    }
}