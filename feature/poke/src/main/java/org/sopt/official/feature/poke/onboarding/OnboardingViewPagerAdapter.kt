package org.sopt.official.feature.poke.onboarding

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class OnboardingViewPagerAdapter(fragmentActivity: FragmentActivity, private val profiles: List<List<PokeUserParcelable>>) :
    FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = profiles.size

    override fun createFragment(position: Int): Fragment {
        return OnboardingPokeUserFragment.newInstance(profiles[position])
    }
}
