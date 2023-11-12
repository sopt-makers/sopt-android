/*
 * MIT License
 * Copyright 2023 SOPT - Shout Our Passion Together
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
package org.sopt.official.feature.attendance

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.children
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.sopt.official.databinding.DialogAttendanceCodeBinding
import org.sopt.official.feature.attendance.model.DialogState

class AttendanceCodeDialog : DialogFragment() {
    private var _binding: DialogAttendanceCodeBinding? = null
    private val binding: DialogAttendanceCodeBinding get() = requireNotNull(_binding)
    private val viewModel: AttendanceViewModel by activityViewModels()
    private lateinit var dialogTitle: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
    }

    override fun onResume() {
        super.onResume()
        val back = ColorDrawable(Color.TRANSPARENT)
        val inset = InsetDrawable(back, 24, 0, 24, 0)
        dialog?.window?.run {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setDimAmount(0.85f)
            setBackgroundDrawable(inset)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogAttendanceCodeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initTitle()
        initListener()
        observeState()
    }

    fun setTitle(title: String): AttendanceCodeDialog {
        dialogTitle = title
        return this
    }

    private fun initTitle() {
        viewModel.initDialogTitle(dialogTitle)
        viewModel.title
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                if (it.isEmpty()) return@onEach
                binding.tvAttendanceCodeDialogTitle.text = "${it.substring(0, 5)}하기"
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun initListener() {
        initEdittextListener()
        initButtonListener()
    }

    private fun initEdittextListener() {
        with(binding) {
            // 마지막 입력란부터 시작하도록 확인
            focusedLastInput(etAttendanceCode2)
            focusedLastInput(etAttendanceCode3)
            focusedLastInput(etAttendanceCode4)
            focusedLastInput(etAttendanceCode5)

            // edittext 사이 공간 클릭 시에도 입력란으로 이동
            linearLayout.setOnClickListener {
                checkedLastInputEditText().requestFocus()
                val imm = getSystemService(requireContext(), InputMethodManager::class.java)
                imm?.showSoftInput(checkedLastInputEditText(), 0)
            }

            // back 버튼 클릭
            etAttendanceCode1.setOnBackKeyListener(etAttendanceCode1)
            etAttendanceCode2.setOnBackKeyListener(etAttendanceCode1)
            etAttendanceCode3.setOnBackKeyListener(etAttendanceCode2)
            etAttendanceCode4.setOnBackKeyListener(etAttendanceCode3)
            etAttendanceCode5.setOnBackKeyListener(etAttendanceCode4)

            // edittext 자동 이동
            etAttendanceCode1.addTextChangedListener(
                beforeTextChanged = { _, _, _, _ ->
                    binding.tvAttendanceCodeDialogError.visibility = View.GONE
                },
                afterTextChanged = {
                    if (it?.length == 1) {
                        etAttendanceCode2.requestFocus()
                        etAttendanceCode1.isEnabled = false
                    }
                }
            )
            etAttendanceCode2.requestFocusAfterTextChanged(etAttendanceCode3)
            etAttendanceCode3.requestFocusAfterTextChanged(etAttendanceCode4)
            etAttendanceCode4.requestFocusAfterTextChanged(etAttendanceCode5)

            // 출석하기 버튼 활성화
            etAttendanceCode5.doAfterTextChanged {
                btnAttendanceCodeDialog.isEnabled = etAttendanceCode5.text.isNotEmpty()
            }
        }
    }

    private fun focusedLastInput(et: EditText) {
        et.setOnFocusChangeListener { _, _ ->
            if (et != checkedLastInputEditText()) {
                checkedLastInputEditText().requestFocus()
                et.clearFocus()
            }
            val imm = getSystemService(requireContext(), InputMethodManager::class.java)
            imm?.showSoftInput(et, 0)
        }
    }

    private fun EditText.setOnBackKeyListener(to: EditText) {
        setOnKeyListener { view, i, keyEvent ->
            if (keyEvent.action != KeyEvent.ACTION_DOWN) {
                return@setOnKeyListener true
            }
            if (text.toString().isEmpty() && i == KeyEvent.KEYCODE_DEL) {
                to.isEnabled = true
                clearFocus()
                if (this == binding.etAttendanceCode1) {
                    hideKeyboard()
                } else {
                    to.text = null
                    to.requestFocus()
                }
            }

            if (i == KeyEvent.KEYCODE_ENTER) {
                val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
            return@setOnKeyListener false
        }
    }

    private fun checkedLastInputEditText(): EditText {
        with(binding) {
            if (etAttendanceCode1.text.isEmpty()) {
                return etAttendanceCode1
            } else if (etAttendanceCode2.text.isEmpty()) {
                return etAttendanceCode2
            } else if (etAttendanceCode3.text.isEmpty()) {
                return etAttendanceCode3
            } else if (etAttendanceCode4.text.isEmpty()) {
                return etAttendanceCode4
            } else {
                return etAttendanceCode5
            }
        }
    }

    private fun initButtonListener() {
        with(binding) {
            ivAttendanceCodeClose.setOnClickListener {
                dismiss()
            }
            btnAttendanceCodeDialog.setOnClickListener {
                viewModel.checkAttendanceCode(
                    "${etAttendanceCode1.text}${etAttendanceCode2.text}${etAttendanceCode3.text}" +
                        "${etAttendanceCode4.text}${etAttendanceCode5.text}"
                )
            }
        }
    }

    override fun dismiss() {
        viewModel.initDialogState()
        super.dismiss()
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.dialogState.collectLatest {
                when (it) {
                    is DialogState.Close -> {
                        dismiss()
                        viewModel.run {
                            fetchSoptEvent()
                            initDialogState()
                            dialogErrorMessage = ""
                        }
                    }

                    is DialogState.Failure -> {
                        // 설정한
                        with(binding) {
                            // 숫자 초기화
                            binding.linearLayout.children
                                .forEach { child ->
                                    if (child is EditText) {
                                        initCodeEditText(child)
                                    }
                                }
                            etAttendanceCode5.clearFocus()
                            linearLayout.requestFocus()

                            // 에러 메시지 나타나도록
                            tvAttendanceCodeDialogError.visibility = View.VISIBLE
                            tvAttendanceCodeDialogError.text = viewModel.dialogErrorMessage

                            // 키보드 내리기
                            hideKeyboard()
                        }
                    }

                    else -> {}
                }
            }
        }
    }

    private fun hideKeyboard() {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    private fun initCodeEditText(et: EditText) {
        et.isEnabled = true
        et.text = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
