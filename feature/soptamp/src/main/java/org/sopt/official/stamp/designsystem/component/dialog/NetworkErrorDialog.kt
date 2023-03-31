/*
 * Copyright 2023 SOPT - Shout Our Passion Together
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sopt.official.stamp.designsystem.component.dialog

import androidx.compose.runtime.Composable

@Composable
fun NetworkErrorDialog(
    onRetry: () -> Unit = {}
) {
    ErrorDialog(
        title = "네트워크가 원활하지 않습니다.",
        content = "인터넷 연결을 확인하고 다시 시도해 주세요.",
        onRetry = { onRetry() }
    )
}
