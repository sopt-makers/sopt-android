/*
 * MIT License
 * Copyright 2026 SOPT - Shout Our Passion Together
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
package org.sopt.official.data.sopletter.onboarding.repositoryimpl

import org.sopt.official.common.coroutines.suspendRunCatching
import org.sopt.official.data.sopletter.onboarding.service.SopletterOnboardingService
import org.sopt.official.domain.sopletter.onboarding.model.SopletterOnboardingModel
import org.sopt.official.domain.sopletter.onboarding.repository.SopletterOnboardingRepository
import org.sopt.official.localstorage.source.UserStorage
import kotlinx.coroutines.flow.first
import javax.inject.Inject

internal class SopletterOnboardingRepositoryImpl @Inject constructor(
    private val sopletterOnboardingService: SopletterOnboardingService,
    private val userStorage: UserStorage,
) : SopletterOnboardingRepository {

    override suspend fun getOnboarding(): Result<SopletterOnboardingModel> = suspendRunCatching {
        sopletterOnboardingService.getOnboarding().toDomain().also { saveProfile(it) }
    }

    override suspend fun completeOnboarding(): Result<SopletterOnboardingModel> = suspendRunCatching {
        sopletterOnboardingService.completeOnboarding().toDomain().also { saveProfile(it) }
    }

    override suspend fun getCachedOnboardingStatus(): Boolean? =
        userStorage.isSopletterOnboardingCompleted.first()

    override suspend fun getCachedNickname(): String? = userStorage.sopletterNickname.first()

    private suspend fun saveProfile(profile: SopletterOnboardingModel) {
        userStorage.saveSopletterProfile(profile.isOnboarded, profile.nickname)
    }
}
