/*
 * MIT License
 * Copyright 2023-2025 SOPT - Shout Our Passion Together
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

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.AsyncImage
import org.sopt.official.designsystem.GrayAlpha300
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.feature.appjamtamp.R
import org.sopt.official.feature.appjamtamp.model.ImageModel

@Composable
internal fun ImageContent(
    imageModel: ImageModel,
    onChangeImage: (images: ImageModel) -> Unit,
    onClickZoomIn: (url: String) -> Unit,
    isEditable: Boolean,
    modifier: Modifier = Modifier,
) {
    val isImageEmpty = remember(imageModel) { imageModel.isEmpty() }
    val pagerState = rememberPagerState { imageModel.size }
    val photoPickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickMultipleVisualMedia()) {
            if (it.isNotEmpty()) {
                onChangeImage(ImageModel.Local(it.map { uri -> uri.toString() }))
            }
        }

    HorizontalPager(
        state = pagerState,
        modifier = modifier,
    ) { page ->
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .background(
                        color = SoptTheme.colors.onSurface900,
                        shape = RoundedCornerShape(10.dp),
                    )
                    .clickable {
                        if (isEditable) {
                            photoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly),
                            )
                        }
                    },
            contentAlignment = Alignment.Center
        ) {
            if (isImageEmpty) {
                Text(
                    text = "달성 사진을 올려주세요",
                    style = SoptTheme.typography.body16R,
                    color = SoptTheme.colors.onSurface300,
                )
            } else {
                when (imageModel) {
                    is ImageModel.Local -> {
                        AsyncImage(
                            model = imageModel.uri[page].toUri(),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(10.dp)),
                            contentScale = ContentScale.Crop,
                        )

                        ZoomInIcon(
                            modifier = Modifier
                                .align(Alignment.BottomEnd),
                            onClickZoomIn = {
                                onClickZoomIn(imageModel.uri[page].toUri().toString())
                            }
                        )
                    }

                    is ImageModel.Remote -> {
                        AsyncImage(
                            model = imageModel.url[page],
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(10.dp)),
                            contentScale = ContentScale.Crop,
                        )

                        ZoomInIcon(
                            modifier = Modifier
                                .align(Alignment.BottomEnd),
                            onClickZoomIn = {
                                onClickZoomIn(imageModel.url[page])
                            }
                        )
                    }

                    else -> throw IllegalStateException("예외처리 했으므로 여긴 안 통과함")
                }
            }
        }
    }
}

@Composable
private fun ZoomInIcon(
    onClickZoomIn: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clickable(onClick = onClickZoomIn)
            .padding(16.dp)
            .clip(CircleShape)
            .background(
                color = GrayAlpha300,
                shape = CircleShape
            )
            .padding(6.dp)
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_zoom_in),
            contentDescription = null,
            tint = Color.Unspecified
        )
    }
}

@Preview
@Composable
private fun ImageContentPreview() {
    SoptTheme {
        ImageContent(
            imageModel = ImageModel.Empty,
            onChangeImage = {},
            onClickZoomIn = {},
            isEditable = true
        )
    }
}
