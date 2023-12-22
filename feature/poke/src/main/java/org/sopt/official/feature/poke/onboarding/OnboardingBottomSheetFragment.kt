package org.sopt.official.feature.poke.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.sopt.official.feature.poke.databinding.FragmentOnboardingBottomSheetBinding

class OnboardingBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentOnboardingBottomSheetBinding? = null
    private val binding: FragmentOnboardingBottomSheetBinding get() = requireNotNull(_binding)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboardingBottomSheetBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClickListener()
    }

    private fun initClickListener() {
        binding.buttonConfirm.setOnClickListener { dismiss() }
    }
}