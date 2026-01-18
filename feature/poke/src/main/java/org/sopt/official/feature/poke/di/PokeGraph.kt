package org.sopt.official.feature.poke.di

import org.sopt.official.feature.poke.friend.summary.FriendListSummaryActivity
import org.sopt.official.feature.poke.main.PokeMainActivity
import org.sopt.official.feature.poke.message.MessageListBottomSheetFragment
import org.sopt.official.feature.poke.notification.PokeNotificationActivity
import org.sopt.official.feature.poke.onboarding.OnboardingActivity

interface PokeGraph {
    fun inject(fragment: MessageListBottomSheetFragment)

    fun pokeMainActivity(): PokeMainActivity
    fun pokeNotificationActivity(): PokeNotificationActivity
    fun friendListSummaryActivity(): FriendListSummaryActivity
    fun onboardingActivity(): OnboardingActivity
}