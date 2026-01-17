/*
 * MIT License
 * Copyright 2025 SOPT - Shout Our Passion Together
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
package org.sopt.official.feature.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.Orange300
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.designsystem.component.UrlImage
import org.sopt.official.feature.home.R

@Composable
fun HomePlaygroundPost(
    profileImage: String,
    userName: String,
    userPart: String?,
    label: String,
    category: String,
    title: String,
    isAnonymous: Boolean,
    description: String,
    onClick: () -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(17.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = SoptTheme.colors.onSurface900,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 17.dp)
    ) {
        ProfileItem(
            profileImage = profileImage,
            name = userName,
            part = userPart,
            isAnonymous = isAnonymous,
            modifier = Modifier
                .clickable(onClick = onProfileClick)
                .padding(top = 18.dp, bottom = 22.dp)
        )

        PostContentSection(
            label = label,
            category = category,
            title = title,
            description = description
        )
    }
}

@Composable
private fun ProfileItem(
    profileImage: String,
    name: String,
    part: String?,
    isAnonymous: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .width(IntrinsicSize.Min)
    ) {
        if (isAnonymous || profileImage.isBlank()) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_empty_profile),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(54.dp)
                    .clip(shape = CircleShape)
                    .background(SoptTheme.colors.onSurface800)
            )
        } else {
            UrlImage(
                url = profileImage,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(54.dp)
                    .clip(shape = CircleShape)
                    .background(SoptTheme.colors.onSurface800)
            )
        }

        Text(
            text = name,
            style = SoptTheme.typography.body10M,
            color = SoptTheme.colors.primary,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            maxLines = if (isAnonymous) 2 else 1,
            modifier = Modifier.padding(top = 3.dp, bottom = 1.dp)
        )
        if (!part.isNullOrBlank()) {
            Text(
                text = part,
                style = SoptTheme.typography.body10M,
                color = SoptTheme.colors.onSurface400,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun PostContentSection(
    label: String,
    category: String,
    title: String,
    description: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = SoptTheme.typography.label11SB,
                color = Orange300
            )
            Box(
                modifier = Modifier
                    .background(SoptTheme.colors.onSurface600)
                    .size(width = 1.dp, height = 7.dp)
            )
            Text(
                text = category,
                style = SoptTheme.typography.label11SB,
                color = Orange300
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = title,
            style = SoptTheme.typography.title16SB,
            color = SoptTheme.colors.primary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = description,
            style = SoptTheme.typography.body13M,
            color = SoptTheme.colors.onSurface400,
            maxLines = 2,
            minLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}
