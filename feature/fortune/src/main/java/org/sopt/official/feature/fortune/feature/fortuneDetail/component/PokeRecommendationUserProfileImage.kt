/*
 * MIT License
 * Copyright 2024-2025 SOPT - Shout Our Passion Together
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
package org.sopt.official.feature.fortune.feature.fortuneDetail.component

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale.Companion.Crop
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.designsystem.component.UrlImage
import org.sopt.official.feature.fortune.R.drawable.ic_empty_profile

@Composable
fun PokeRecommendationUserProfileImage(
    profile: String,
    modifier: Modifier = Modifier,
) {
    when (profile.isEmpty()) {
        true -> {
            Image(
                imageVector = ImageVector.vectorResource(id = ic_empty_profile),
                contentDescription = "profileImageEmpty",
                modifier = modifier,
            )
        }

        false -> {
            UrlImage(
                url = profile,
                contentDescription = "profileImage",
                contentScale = Crop,
                modifier = modifier,
            )
        }
    }
}

@Preview
@Composable
private fun PokeRecommendationUserProfileImagePreview() {
    SoptTheme {
        PokeRecommendationUserProfileImage(profile = "")
    }
}
