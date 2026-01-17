/*
 * MIT License
 * Copyright 2024 SOPT - Shout Our Passion Together
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.sopt.official.feature.poke.message

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.sopt.official.common.util.viewBinding
import org.sopt.official.di.SoptViewModelFactory
import org.sopt.official.domain.poke.entity.PokeMessageList
import org.sopt.official.domain.poke.type.PokeMessageType
import org.sopt.official.feature.poke.R
import org.sopt.official.feature.poke.UiState
import org.sopt.official.feature.poke.databinding.FragmentMessageListBottomSheetBinding
import org.sopt.official.feature.poke.util.showPokeToast

@Inject
class MessageListBottomSheetFragment(
    private val viewModelFactory: SoptViewModelFactory
) : BottomSheetDialogFragment() {
    private val binding by viewBinding(FragmentMessageListBottomSheetBinding::bind)
    private lateinit var viewModel: MessageListBottomSheetViewModel

    var pokeMessageType: PokeMessageType? = null
    var onClickMessageListItem: ((message: String, isAnonymous: Boolean) -> Unit)? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        viewModel = ViewModelProvider(this, viewModelFactory)[MessageListBottomSheetViewModel::class.java]
        return FragmentMessageListBottomSheetBinding.inflate(inflater, container, false).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Arguments에서 읽어오기
        val type = arguments?.getSerializable("pokeMessageType") as? PokeMessageType
        type?.let { 
            pokeMessageType = it
            viewModel.getPokeMessageList(it) 
        } ?: run {
            // 기존 방식 호환 (프로퍼티 직접 설정된 경우)
            pokeMessageType?.let { viewModel.getPokeMessageList(it) }
        }
        
        launchPokeMessageListUiStateFlow()
        initCheckbox()
    }

    private fun launchPokeMessageListUiStateFlow() {
        viewModel.pokeMessageListUiState
            .onEach {
                when (it) {
                    is UiState.Loading -> {}
                    is UiState.Success<PokeMessageList> -> initMessageListContent(it.data)
                    is UiState.ApiError -> showPokeToast(getString(R.string.toast_poke_error))
                    is UiState.Failure -> showPokeToast(it.throwable.message ?: getString(R.string.toast_poke_error))
                }
            }
            .launchIn(lifecycleScope)
    }

    private fun initMessageListContent(data: PokeMessageList) {
        with(binding) {
            textViewTitle.text = data.header
            recyclerView.adapter = MessageListRecyclerAdapter(data.messages, messageListItemClickListener)
        }
    }

    private fun initCheckbox() {
        binding.checkBoxAnonymous.setOnClickListener {
            viewModel.setPokeAnonymousCheckboxClicked()
        }

        viewModel.pokeAnonymousCheckboxChecked.flowWithLifecycle(lifecycle).onEach { isChecked ->
            binding.checkBoxAnonymous.isChecked = isChecked
        }.launchIn(lifecycleScope)

        viewModel.pokeAnonymousOffToast.flowWithLifecycle(lifecycle).onEach {
            showPokeToast(getString(R.string.toast_poke_anonymous_off))
        }.launchIn(lifecycleScope)
    }

    private val messageListItemClickListener =
        MessageItemClickListener { message ->
            onClickMessageListItem?.let { it(message, viewModel.pokeAnonymousCheckboxChecked.value) }
        }
}