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
package org.sopt.official.feature.sopletter.main.analytics

import org.sopt.official.feature.sopletter.SopletterAnalyticsEvent
import org.sopt.official.feature.sopletter.main.contract.SopletterMemoDetailDialogContract

internal class TrackedSopletterMemoDetailDialogActions(
    private val delegate: SopletterMemoDetailDialogContract.Actions,
    private val trackEvent: (SopletterAnalyticsEvent) -> Unit,
) : SopletterMemoDetailDialogContract.Actions by delegate {
    override fun onLikeClick() {
        trackEvent(SopletterAnalyticsEvent.CLICK_SOPTLETTER_LIKE_BUTTON)
        delegate.onLikeClick()
    }

    override fun onEditClick() {
        trackEvent(SopletterAnalyticsEvent.CLICK_EDIT_SOPTLETTER)
        delegate.onEditClick()
    }

    override fun onEditCompleteClick(content: String) {
        trackEvent(SopletterAnalyticsEvent.CLICK_DONE_EDIT_SOPTLETTER)
        delegate.onEditCompleteClick(content)
    }

    override fun onDeleteConfirmClick() {
        trackEvent(SopletterAnalyticsEvent.CLICK_DELETE_SOPTLETTER)
        delegate.onDeleteConfirmClick()
    }
}
