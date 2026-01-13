/*
 * MIT License
 * Copyright 2025-2026 SOPT - Shout Our Passion Together
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
package org.sopt.official.feature.appjamtamp.missiondetail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import kotlinx.collections.immutable.ImmutableList
import org.sopt.official.designsystem.GrayAlpha800
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.feature.appjamtamp.R
import org.sopt.official.feature.appjamtamp.missiondetail.model.StampClapUserModel

@Composable
internal fun ClapUserBottomDialog(
    userList: ImmutableList<StampClapUserModel>,
    onDismiss: () -> Unit,
    onClickUser: (String?, String?) -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = GrayAlpha800)
                .padding(bottom = 16.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.8f)
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(SoptTheme.colors.onSurface800)
                    .padding(vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                ClapUserHeader(
                    onDismiss = onDismiss
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = if (userList.isEmpty()) Alignment.Center else Alignment.TopCenter
                ) {
                    if (userList.isEmpty()) {
                        Text(
                            text = "아직 박수친 솝트인이 없어요",
                            color = SoptTheme.colors.onSurface200,
                            style = SoptTheme.typography.body14M,
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            items(userList.size) { index ->
                                ClapUserItem(
                                    user = userList[index],
                                    onClickUser = onClickUser
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ClapUserHeader(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onDismiss) {
            Icon(
                imageVector = Icons.Default.ArrowBackIosNew,
                contentDescription = null,
                tint = SoptTheme.colors.surface
            )
        }

        Text(
            text = "박수 목록",
            style = SoptTheme.typography.title20SB,
            color = SoptTheme.colors.onSurface10
        )
    }
}

@Composable
private fun ClapUserItem(
    user: StampClapUserModel,
    onClickUser: (String?, String?) -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(
                    bounded = true,
                    color = SoptTheme.colors.surface
                ),
                onClick = {
                    onClickUser(
                        user.nickname,
                        if (user.profileMessage.isNullOrEmpty()) "설정된 한 마디가 없습니다" else user.profileMessage
                    )
                })
            .clip(RoundedCornerShape(8.dp))
            .background(color = SoptTheme.colors.onSurface800)
            .padding(horizontal = 8.dp, vertical = 15.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(SoptTheme.colors.onSurface700),
            contentAlignment = Alignment.Center
        ) {
            if (user.profileImage != null) {
                AsyncImage(
                    model = user.profileImage,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    error = painterResource(id = R.drawable.ic_user_profile)
                )
            } else {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_user_profile),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .padding(6.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = user.nickname,
                style = SoptTheme.typography.body16M,
                color = SoptTheme.colors.onSurface10
            )

            Text(
                text = if (user.profileMessage.isNullOrEmpty()) "설정된 한 마디가 없습니다." else user.profileMessage,
                style = SoptTheme.typography.body14R,
                color = SoptTheme.colors.onSurface200,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }

        Spacer(modifier = Modifier.width(18.dp))

        Text(
            text = "${user.clapCount}회",
            style = SoptTheme.typography.heading16B,
            color = Color.White
        )

        Spacer(modifier = Modifier.width(5.dp))

        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_clap),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier
                .size(width = 20.dp, height = 18.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ClapUserItemPreview() {
    SoptTheme {
        ClapUserItem(
            user = StampClapUserModel(
                nickname = "안드손민성",
                profileImage = null,
                profileMessage = null,
                clapCount = 10
            ),
            onClickUser = { _, _ -> }
        )
    }
}
