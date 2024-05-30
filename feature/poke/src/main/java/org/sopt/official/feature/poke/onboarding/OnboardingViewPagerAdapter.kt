package org.sopt.official.feature.poke.onboarding

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import org.sopt.official.domain.poke.entity.PokeRandomUserList

class OnboardingViewPagerAdapter(
    fragmentActivity: FragmentActivity,
    private val profiles: PokeRandomUserList,
    private val args: OnboardingActivity.StartArgs?,
) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = profiles.randomInfoList.size

    override fun createFragment(position: Int): Fragment {
        return OnboardingPokeUserFragment.newInstance(profiles.randomInfoList[position], args)
    }
}
