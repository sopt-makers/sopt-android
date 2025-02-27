package org.sopt.official.feature.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.TopEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.sopt.official.designsystem.Orange400
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.designsystem.SoptTheme.colors
import org.sopt.official.designsystem.SoptTheme.typography
import org.sopt.official.designsystem.component.UrlImage
import org.sopt.official.feature.home.model.HomeAppService

@Composable
internal fun HomeEnjoySoptServicesBlock(
    appServices: ImmutableList<HomeAppService>,
    onAppServiceClick: (url: String, appServiceName: String) -> Unit,
) {
    Text(
        text = "SOPT 더 재밌게 즐기기!",
        style = typography.heading20B,
        color = colors.onBackground,
    )
    Spacer(modifier = Modifier.height(height = 16.dp))
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(space = 16.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        items(appServices) { appService ->
            AppServiceItem(
                appService = appService,
                onItemClick = onAppServiceClick,
            )
        }
    }
}

@Preview
@Composable
private fun HomeEnjoySoptServicesBlockPreview() {
    SoptTheme {
        Column {
            HomeEnjoySoptServicesBlock(
                appServices = PREVIEW_FIXTURE,
                onAppServiceClick = { _, _ -> },
            )
        }
    }
}

@Composable
private fun AppServiceItem(
    appService: HomeAppService,
    onItemClick: (url: String, appServiceName: String) -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onItemClick(appService.deepLink, appService.serviceName) }
    ) {
        Box(contentAlignment = TopEnd) {
            HomeButtonCircleBox {
                if (appService.defaultIcon != null) {
                    Image(
                        painter = painterResource(appService.defaultIcon),
                        contentDescription = "",
                        modifier = Modifier.size(size = 60.dp),
                    )
                } else {
                    UrlImage(
                        url = appService.iconUrl,
                        modifier = Modifier.size(size = 60.dp),
                    )
                }
            }

            if (appService.isShowAlarmBadge) AppServiceAlarmBadge(text = appService.alarmBadgeContent)
        }
        Spacer(modifier = Modifier.height(height = 8.dp))
        Text(
            text = appService.serviceName,
            style = typography.body14M,
            color = colors.onSurface200,
        )
    }
}

@Preview
@Composable
private fun AppServiceItemPreview() {
    SoptTheme {
        AppServiceItem(
            appService = PREVIEW_FIXTURE[0],
            onItemClick = { _, _ -> },
        )
    }
}

@Composable
private fun AppServiceAlarmBadge(
    text: String,
) {
    Box(
        contentAlignment = Center,
        modifier = Modifier.background(
            color = Orange400,
            shape = RoundedCornerShape(size = 10.dp),
        ).height(height = 20.dp)
    ) {
        Text(
            text = text,
            style = typography.label12SB,
            color = colors.background,
            modifier = Modifier.padding(horizontal = 6.dp)
        )
    }
}

@Preview
@Composable
private fun AppServiceAlarmBadgePreview() {
    SoptTheme {
        AppServiceAlarmBadge(
            text = "9+"
        )
    }
}

@Composable
private fun HomeButtonCircleBox(
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        content = content,
        contentAlignment = Center,
        modifier = Modifier
            .size(size = 80.dp)
            .background(
                shape = CircleShape,
                color = colors.onSurface800,
            ),
    )
}

@Preview
@Composable
private fun HomeButtonCircleBoxPreview() {
    SoptTheme {
        HomeButtonCircleBox {}
    }
}

private val PREVIEW_FIXTURE = persistentListOf(
    HomeAppService(
        serviceName = "콕찌르기",
        isShowAlarmBadge = false,
        alarmBadgeContent = "123",
        iconUrl = "",
        deepLink = ""
    ),
    HomeAppService(
        serviceName = "솝탬프",
        isShowAlarmBadge = true,
        alarmBadgeContent = "N",
        iconUrl = "",
        deepLink = ""
    )
)
