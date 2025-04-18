package org.sopt.official.feature.main

import android.content.Context
import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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

private val playgroundMenu = listOf(Triple(R.drawable.ic_pencil_22, "글쓰기", PlaygroundWebLink.WRITE)).toImmutableList()
private val crewMenu = listOf(
    Triple(R.drawable.ic_bolt_22, "번쩍 개설", PlaygroundWebLink.MAKE_FLASH),
    Triple(R.drawable.ic_moim_22, "모임 개설", PlaygroundWebLink.MAKE_MOIM),
    Triple(R.drawable.ic_feed_22, "피드 작성", PlaygroundWebLink.OFFICIAL_HOMEPAGE),
).toImmutableList()
private val homepageMenu = listOf(
    Triple(R.drawable.ic_sopticle_22, "솝티클 올리기", PlaygroundWebLink.SOPTICLE)
).toImmutableList()

@Composable
internal fun MainFloatingButton(
    paddingValues: PaddingValues,
) {
    var isFloatingButtonClicked by remember { mutableStateOf(false) }

    val rotation by animateFloatAsState(
        targetValue = if (isFloatingButtonClicked) 0f else 45f,
        animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing)
    )

    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        if (isFloatingButtonClicked) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .then(
                        if (isFloatingButtonClicked) Modifier.background(SoptTheme.colors.onSurface.copy(0.65f))
                        else Modifier
                    )
                    .clickable {
                        isFloatingButtonClicked = false
                    }
            )
        }

        AnimatedVisibility(
            visible = isFloatingButtonClicked,
            enter = slideInVertically(
                initialOffsetY = { it * 2 },
                animationSpec = tween(durationMillis = 600)
            ),
            exit = slideOutVertically(
                targetOffsetY = { it * 2 },
                animationSpec = tween(durationMillis = 600)
            ),
            modifier = Modifier
                .padding(bottom = 106.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                FloatingMenuItem(title = "플레이그라운드", menuList = playgroundMenu)
                FloatingMenuItem(title = "모임/스터디", menuList = crewMenu)
                FloatingMenuItem(title = "홈페이지", menuList = homepageMenu)
            }
        }

        FloatingActionButton(
            onClick = { isFloatingButtonClicked = !isFloatingButtonClicked },
            shape = RoundedCornerShape(18.dp),
            containerColor = SoptTheme.colors.primary,
            modifier = Modifier
                .padding(bottom = 44.dp)
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
                    .clickable {
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
                )
            }
        }
    }
}

private fun getIntent(url: String, context: Context) = Intent(context, WebViewActivity::class.java).apply {
    putExtra(INTENT_URL, url)
}