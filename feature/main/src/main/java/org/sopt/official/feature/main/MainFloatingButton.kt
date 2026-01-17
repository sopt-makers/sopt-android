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
package org.sopt.official.feature.main

import android.content.Context
import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.feature.main.model.PlaygroundWebLink
import org.sopt.official.webview.view.WebViewActivity
import org.sopt.official.webview.view.WebViewActivity.Companion.INTENT_URL

private const val ANIMATION_DURATION = 600

private val playgroundMenu = listOf(
    Triple(R.drawable.ic_pencil_22, "글쓰기", PlaygroundWebLink.WRITE)
).toImmutableList()
private val crewMenu = listOf(
    Triple(R.drawable.ic_bolt_22, "번쩍 개설", PlaygroundWebLink.MAKE_FLASH),
    Triple(R.drawable.ic_moim_22, "모임 개설", PlaygroundWebLink.MAKE_MOIM),
    Triple(R.drawable.ic_feed_22, "피드 작성", PlaygroundWebLink.WRITE_FEED),
).toImmutableList()
private val homepageMenu = listOf(
    Triple(R.drawable.ic_sopticle_22, "활동후기 업로드", PlaygroundWebLink.UPLOAD_REVIEW)
).toImmutableList()

private val menuList = listOf(
    Pair("플레이그라운드", playgroundMenu),
    Pair("모임/스터디", crewMenu),
    Pair("홈페이지", homepageMenu)
).toImmutableList()

@Composable
fun MainFloatingButton(
    paddingValues: PaddingValues
) {
    var isFloatingButtonClicked by remember { mutableStateOf(false) }

    val rotation by animateFloatAsState(
        targetValue = if (isFloatingButtonClicked) 0f else 45f,
        animationSpec = tween(durationMillis = ANIMATION_DURATION, easing = FastOutSlowInEasing)
    )

    val backgroundAlpha by animateFloatAsState(
        targetValue = if (isFloatingButtonClicked) 0.65f else 0f,
        animationSpec = tween(durationMillis = ANIMATION_DURATION, easing = FastOutSlowInEasing)
    )

    Box(
        contentAlignment = Alignment.BottomEnd,
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (isFloatingButtonClicked) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(SoptTheme.colors.onSurface.copy(alpha = backgroundAlpha))
                    .clickable {
                        isFloatingButtonClicked = false
                    }
            )
        }

        AnimatedVisibility(
            visible = isFloatingButtonClicked,
            enter = slideInVertically(
                initialOffsetY = { it * 2 },
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow
                )
            ),
            exit = slideOutVertically(
                targetOffsetY = { it * 2 },
                animationSpec = tween(durationMillis = ANIMATION_DURATION)
            ),
            modifier = Modifier
                .padding(bottom = 146.dp, end = 20.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.End
            ) {
                menuList.forEach { (title, menu) ->
                    FloatingMenuItem(title = title, menuList = menu)
                }
            }
        }

        FloatingActionButton(
            onClick = { isFloatingButtonClicked = !isFloatingButtonClicked },
            shape = RoundedCornerShape(18.dp),
            containerColor = SoptTheme.colors.primary,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(paddingValues)
                .padding(bottom = 84.dp, end = 20.dp)
                .size(48.dp)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_close_28),
                contentDescription = null,
                modifier = Modifier.rotate(rotation)
            )
        }
    }
}

@Composable
private fun FloatingMenuItem(
    title: String,
    menuList: ImmutableList<Triple<Int, String, String>>
) {
    val context = LocalContext.current

    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .background(
                color = SoptTheme.colors.primary,
                shape = RoundedCornerShape(16.dp)
            )
            .width(162.dp)
            .padding(top = 12.dp, bottom = 14.dp)
            .padding(horizontal = 14.dp)
    ) {
        Text(
            text = title,
            style = SoptTheme.typography.body13M,
            color = SoptTheme.colors.onSurface950
        )
        menuList.forEach { (icon, text, weblink) ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        context.startActivity(getIntent(weblink, context))
                    }
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(icon),
                    contentDescription = null,
                    tint = Color.Unspecified
                )
                Text(
                    text = text,
                    style = SoptTheme.typography.body14M,
                    color = SoptTheme.colors.onSurface950,
                    modifier = Modifier
                        .padding(6.dp)
                        .fillMaxWidth()
                )
            }
        }
    }
}

private fun getIntent(url: String, context: Context) = Intent(context, WebViewActivity::class.java).apply {
    putExtra(INTENT_URL, url)
}
