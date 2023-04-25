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
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doBeforeTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.sopt.official.databinding.DialogAttendanceCodeBinding
import org.sopt.official.feature.attendance.model.DialogState

class AttendanceCodeDialog(title: String) : DialogFragment() {
    private var _binding: DialogAttendanceCodeBinding? = null
    private val binding: DialogAttendanceCodeBinding get() = requireNotNull(_binding)
    private val attendanceViewModel: AttendanceViewModel by activityViewModels()
    private val _title = title

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
            setDimAmount(0.70f)
            setBackgroundDrawable(inset)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = DialogAttendanceCodeBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initTitle()
        initListener()
        observeState()
    }

    private fun initTitle() {
        binding.titleText = "${_title.substring(0,5)}하기"
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
                val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(checkedLastInputEditText(), 0)
            }

            // back 버튼 클릭
            clickedKeyEvent(etAttendanceCode1, etAttendanceCode1)
            clickedKeyEvent(etAttendanceCode2, etAttendanceCode1)
            clickedKeyEvent(etAttendanceCode3, etAttendanceCode2)
            clickedKeyEvent(etAttendanceCode4, etAttendanceCode3)
            clickedKeyEvent(etAttendanceCode5, etAttendanceCode4)

            // edittext 자동 이동
            etAttendanceCode1.addTextChangedListener(CustomTextWatcher(etAttendanceCode2, etAttendanceCode1))
            etAttendanceCode2.addTextChangedListener(CustomTextWatcher(etAttendanceCode3, etAttendanceCode2))
            etAttendanceCode3.addTextChangedListener(CustomTextWatcher(etAttendanceCode4, etAttendanceCode3))
            etAttendanceCode4.addTextChangedListener(CustomTextWatcher(etAttendanceCode5, etAttendanceCode4))

            // 출석하기 버튼 활성화
            etAttendanceCode5.doAfterTextChanged {
                btnAttendanceCodeDialog.isEnabled = etAttendanceCode5.text.isNotEmpty()
            }

            // 에러 메세지 visible 변경
            etAttendanceCode1.doBeforeTextChanged { _, _, _, _ ->
                tvAttendanceCodeDialogError.visibility = View.GONE
            }
        }
    }

    private fun focusedLastInput(et: EditText) {
        et.setOnFocusChangeListener { _, _ ->
            if (et != checkedLastInputEditText()) {
                checkedLastInputEditText().requestFocus()
                et.clearFocus()
            }
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(et, 0)
        }
    }

    private fun clickedKeyEvent(etNow: EditText, etPre: EditText) {
        etNow.setOnKeyListener { view, i, keyEvent ->
            if (keyEvent.action != KeyEvent.ACTION_DOWN) {
                return@setOnKeyListener true
            }

            if (etNow.text.toString().isEmpty() && i == KeyEvent.KEYCODE_DEL) {
                etPre.isEnabled = true

                etNow.clearFocus()
                if (etNow == binding.etAttendanceCode1) {
                    hideKeyboard()
                } else {
                    etPre.text = null
                    etPre.requestFocus()
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
            if (etAttendanceCode1.text.toString().isEmpty()) {
                return etAttendanceCode1
            } else if (etAttendanceCode2.text.toString().isEmpty()) {
                return etAttendanceCode2
            } else if (etAttendanceCode3.text.toString().isEmpty()) {
                return etAttendanceCode3
            } else if (etAttendanceCode4.text.toString().isEmpty()) {
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
                attendanceViewModel.checkAttendanceCode(
                    "${etAttendanceCode1.text}${etAttendanceCode2.text}${etAttendanceCode3.text}${etAttendanceCode4.text}${etAttendanceCode5.text}"
                )
            }
        }
    }

    private fun observeState() {
        lifecycleScope.launch {
            attendanceViewModel.dialogState.collect {
                when (it) {
                    is DialogState.Close -> {
                        dismiss()
                        attendanceViewModel.fetchSoptEvent()
                    }
                    is DialogState.Failure -> {
                        // 설정한
                        with(binding) {
                            // 숫자 초기화
                            initCodeEditText(etAttendanceCode1)
                            initCodeEditText(etAttendanceCode2)
                            initCodeEditText(etAttendanceCode3)
                            initCodeEditText(etAttendanceCode4)
                            initCodeEditText(etAttendanceCode5)
                            etAttendanceCode5.clearFocus()
                            linearLayout.requestFocus()

                            // 에러 메시지 나타나도록
                            tvAttendanceCodeDialogError.visibility = View.VISIBLE
                            tvAttendanceCodeDialogError.text = attendanceViewModel.dialogErrorMessage

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