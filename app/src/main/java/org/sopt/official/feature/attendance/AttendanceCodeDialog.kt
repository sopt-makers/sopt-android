package org.sopt.official.feature.attendance

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import org.sopt.official.databinding.DialogAttendanceCodeBinding

class AttendanceCodeDialog : DialogFragment() {
    private var _binding: DialogAttendanceCodeBinding? = null
    private val binding: DialogAttendanceCodeBinding get() = requireNotNull(_binding)

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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}