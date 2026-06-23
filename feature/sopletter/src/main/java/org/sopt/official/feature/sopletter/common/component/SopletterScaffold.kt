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
package org.sopt.official.feature.sopletter.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.feature.sopletter.common.model.SopletterSnackbarType
import org.sopt.official.feature.sopletter.common.model.SopletterSnackbarVisuals
import timber.log.Timber

@Composable
internal fun SopletterScaffold(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    content: @Composable (innerPadding: PaddingValues) -> Unit,
) {
    Scaffold(
        snackbarHost = {
            Box(modifier = Modifier.fillMaxSize()) {
                SnackbarHost(
                    hostState = snackbarHostState,
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .align(Alignment.TopCenter),
                    snackbar = { data ->
                        when (val visuals = data.visuals) {
                            is SopletterSnackbarVisuals -> {
                                SopletterSnackBar(
                                    sopletterSnackbarType = visuals.type,
                                    message = visuals.message,
                                )
                            }
                            else -> {
                                Timber.w(
                                    "Invalid snackbar usage. Use SopletterSnackbarVisuals instead of default showSnackbar(message)."
                                )

                                Snackbar(snackbarData = data)
                            }
                        }
                    },
                )
            }
        },
        containerColor = SoptTheme.colors.background,
        modifier = modifier.fillMaxSize(),
        content = content,
    )
}

@Composable
private fun SopletterSnackBar(
    sopletterSnackbarType: SopletterSnackbarType,
    message: String,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(
                color = SoptTheme.colors.onSurface10,
                shape = RoundedCornerShape(18.dp),
            ),
    ) {
        Spacer(modifier = Modifier.width(16.dp))

        Icon(
            imageVector = ImageVector.vectorResource(id = sopletterSnackbarType.iconRes),
            tint = Color.Unspecified,
            contentDescription = null,
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = message,
            style = SoptTheme.typography.title14SB,
            color = SoptTheme.colors.onSurface900,
            modifier = Modifier.padding(vertical = 16.dp),
        )
    }
}

class SopletterSnackbarTypeProvider : PreviewParameterProvider<SopletterSnackbarType> {
    override val values = SopletterSnackbarType.entries.asSequence()
}

@Preview
@Composable
private fun SopletterSnackBarPreview(
    @PreviewParameter(SopletterSnackbarTypeProvider::class) type: SopletterSnackbarType
) {
    SoptTheme {
        SopletterSnackBar(
            sopletterSnackbarType = type,
            message = when (type) {
                SopletterSnackbarType.SUCCESS -> "성공!"
                SopletterSnackbarType.WARNING -> "주의!"
                SopletterSnackbarType.FAILURE -> "실패!"
            }
        )
    }
}
