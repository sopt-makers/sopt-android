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
package org.sopt.stamp.feature.mission.model

import android.net.Uri

sealed interface ImageModel {
    data class Remote(val url: List<String>) : ImageModel {
        override fun isEmpty() = url.isEmpty()
        override val size = url.size
    }

    data class Local(val uri: List<Uri>) : ImageModel {
        override fun isEmpty() = uri.isEmpty()
        override val size = uri.size
    }

    object Empty : ImageModel {
        override fun isEmpty() = true
        override val size = 1
    }

    fun isEmpty(): Boolean
    val size: Int
}
