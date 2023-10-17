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
package org.sopt.official.feature.attendance

import android.widget.EditText
import androidx.core.widget.doAfterTextChanged

fun EditText.requestFocusAfterTextChanged(to: EditText, otherLogic: () -> Unit = {}) {
    doAfterTextChanged {
        if (it?.length == 1) {
            to.requestFocus()
            this.isEnabled = false
        }
        otherLogic()
    }
}
