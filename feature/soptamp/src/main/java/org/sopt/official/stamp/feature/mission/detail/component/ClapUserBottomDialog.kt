package org.sopt.official.stamp.feature.mission.detail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.Composable
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
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.stamp.R
import org.sopt.official.stamp.designsystem.style.BackgroundDim
import org.sopt.official.stamp.feature.mission.detail.model.StampClapUserUiModel

@Composable
fun ClapUserBottomDialog(
    userList: ImmutableList<StampClapUserUiModel>,
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
                .background(color = BackgroundDim)
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
                // 헤더
                ClapUserHeader(
                    onDismiss = onDismiss,
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
                tint = Color.White
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = "박수 목록",
            style = SoptTheme.typography.title20SB,
            color = SoptTheme.colors.onSurface10
        )
    }
}

@Composable
private fun ClapUserItem(
    user: StampClapUserUiModel,
    onClickUser: (String?, String?) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(color = SoptTheme.colors.onSurface800)
            .padding(horizontal = 12.dp, vertical = 15.dp)
            .clickable(onClick = {
                onClickUser(user.nickname, if (user.profileMessage.isNullOrEmpty()) "설정된 한 마디가 없습니다" else user.profileMessage)
            }),
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
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    error = painterResource(id = R.drawable.ic_user_profile)
                )
            } else {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_user_profile),
                    contentDescription = "",
                    tint = Color.White,
                    modifier = Modifier
                        .padding(6.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(
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

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${user.clapCount}회",
                style = SoptTheme.typography.heading16B,
                color = Color.White
            )

            Spacer(modifier = Modifier.width(5.dp))

            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_clap),
                contentDescription = "",
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(width = 20.dp, height = 18.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ClapUserItemPreview() {
    SoptTheme {
        ClapUserItem(
            user = StampClapUserUiModel(
                nickname = "안드손민성",
                profileImage = null,
                profileMessage = null,
                clapCount = 10
            ),
            onClickUser = { nickname, profileMessage ->

            }
        )
    }
}