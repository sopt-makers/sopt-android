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
package org.sopt.official.feature.attendance.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import org.sopt.official.designsystem.Gray10
import org.sopt.official.designsystem.Gray300
import org.sopt.official.designsystem.Gray400
import org.sopt.official.designsystem.Gray50
import org.sopt.official.designsystem.Gray700
import org.sopt.official.designsystem.Gray800
import org.sopt.official.designsystem.Orange500
import org.sopt.official.feature.attendance.R
import org.sopt.official.feature.attendance.model.AttendanceConstants

/**
 * 출석 코드 입력 다이얼로그
 *
 * @param title 다이얼로그 제목
 * @param onDismiss 다이얼로그 닫기 콜백
 * @param onCodeSubmit 코드 제출 콜백
 * @param errorMessage 에러 메시지 (null이면 표시하지 않음)
 */
@Composable
fun AttendanceCodeDialog(
    title: String,
    onDismiss: () -> Unit,
    onCodeSubmit: (String) -> Unit,
    errorMessage: String?,
    modifier: Modifier = Modifier
) {
    val codeInputState = rememberCodeInputState()

    // 에러 발생 시 입력 필드 초기화
    LaunchedEffect(errorMessage) {
        if (errorMessage != null) {
            codeInputState.clearAndFocusFirst()
        }
    }

    // 다이얼로그 열릴 때 첫 번째 필드에 포커스
    LaunchedEffect(Unit) {
        codeInputState.focusFirst()
    }

    Dialog(onDismissRequest = onDismiss) {
        AttendanceCodeDialogContent(
            title = title,
            codeInputState = codeInputState,
            errorMessage = errorMessage,
            onDismiss = onDismiss,
            onCodeSubmit = { onCodeSubmit(codeInputState.getCompleteCode()) },
            modifier = modifier
        )
    }
}

/**
 * 다이얼로그 컨텐츠
 */
@Composable
private fun AttendanceCodeDialogContent(
    title: String,
    codeInputState: CodeInputState,
    errorMessage: String?,
    onDismiss: () -> Unit,
    onCodeSubmit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = Gray800)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .clickable {
                    // 빈 필드 클릭 시 해당 필드로 포커스 이동
                    codeInputState.focusFirstEmpty()
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DialogHeader(onDismiss = onDismiss)

            DialogTitle(title = title)

            Spacer(modifier = Modifier.height(16.dp))

            DialogSubtitle()

            Spacer(modifier = Modifier.height(22.dp))

            CodeInputFields(codeInputState = codeInputState)

            ErrorMessage(errorMessage = errorMessage)

            Spacer(modifier = Modifier.height(32.dp))

            SubmitButton(
                isEnabled = codeInputState.isComplete,
                onClick = onCodeSubmit
            )
        }
    }
}

/**
 * 다이얼로그 헤더 (닫기 버튼)
 */
@Composable
private fun DialogHeader(
    onDismiss: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        IconButton(onClick = onDismiss) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_close),
                contentDescription = "닫기",
                tint = Gray300
            )
        }
    }
}

/**
 * 다이얼로그 제목
 */
@Composable
private fun DialogTitle(title: String) {
    Text(
        text = title,
        color = Gray10,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold
    )
}

/**
 * 다이얼로그 부제목
 */
@Composable
private fun DialogSubtitle() {
    Text(
        text = "출석 코드 5자리를 입력해 주세요",
        color = Gray300,
        fontSize = 12.sp
    )
}

/**
 * 코드 입력 필드들
 */
@Composable
private fun CodeInputFields(
    codeInputState: CodeInputState
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier.width(AttendanceConstants.DIALOG_WIDTH_DP.dp)
    ) {
        codeInputState.codeValues.forEachIndexed { index, value ->
            CodeInputField(
                value = value,
                isFocused = codeInputState.focusedIndex == index,
                onValueChange = { newValue ->
                    codeInputState.updateValue(index, newValue)
                },
                onFocus = {
                    codeInputState.onFieldFocused(index)
                },
                onBackspace = {
                    codeInputState.handleBackspace(index)
                },
                focusRequester = codeInputState.focusRequesters[index],
                modifier = Modifier.weight(1f)
            )
        }
    }
}

/**
 * 에러 메시지
 */
@Composable
private fun ErrorMessage(
    errorMessage: String?
) {
    if (errorMessage != null) {
        Spacer(modifier = Modifier.height(18.dp))
        Text(
            text = errorMessage,
            color = MaterialTheme.colorScheme.error,
            fontSize = 14.sp
        )
    }
}

/**
 * 제출 버튼
 */
@Composable
private fun SubmitButton(
    isEnabled: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = isEnabled,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        shape = RoundedCornerShape(6.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isEnabled) Orange500 else Gray700,
            disabledContainerColor = Gray700
        )
    ) {
        Text(
            text = "출석하기",
            color = if (isEnabled) Color.Black else Gray400,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

/**
 * 개별 코드 입력 필드
 */
@Composable
private fun CodeInputField(
    value: String,
    isFocused: Boolean,
    onValueChange: (String) -> Unit,
    onFocus: () -> Unit,
    onBackspace: () -> Unit,
    focusRequester: FocusRequester,
    modifier: Modifier = Modifier
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .height(60.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Gray700)
            .border(
                width = if (isFocused) 2.dp else 1.dp,
                color = if (isFocused) Orange500 else Gray400,
                shape = RoundedCornerShape(8.dp)
            )
            .focusRequester(focusRequester)
            .onFocusChanged { focusState ->
                if (focusState.isFocused) {
                    onFocus()
                }
            }
            .onKeyEvent { keyEvent ->
                if (keyEvent.key == Key.Backspace) {
                    onBackspace()
                    true
                } else {
                    false
                }
            },
        textStyle = TextStyle(
            color = Gray50,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next
        ),
        singleLine = true,
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                innerTextField()
            }
        }
    )
}

/**
 * 코드 입력 상태 관리 클래스
 */
@Stable
private class CodeInputState(
    private val keyboardController: androidx.compose.ui.platform.SoftwareKeyboardController?
) {
    var codeValues by mutableStateOf(List(AttendanceConstants.CODE_INPUT_FIELDS) { "" })
        private set

    val focusRequesters = List(AttendanceConstants.CODE_INPUT_FIELDS) { FocusRequester() }

    var focusedIndex by mutableStateOf(-1)
        private set

    val isComplete: Boolean
        get() = codeValues.all { it.isNotEmpty() }

    fun updateValue(index: Int, newValue: String) {
        if (newValue.length <= 1 && newValue.all { it.isDigit() }) {
            codeValues = codeValues.toMutableList().apply {
                this[index] = newValue
            }

            // 값 입력 시 다음 필드로 자동 이동
            if (newValue.isNotEmpty() && index < AttendanceConstants.CODE_INPUT_FIELDS - 1) {
                focusRequesters[index + 1].requestFocus()
            }
        }
    }

    fun handleBackspace(index: Int) {
        val value = codeValues[index]
        when {
            // 현재 필드가 비어있고 첫 번째가 아닌 경우 -> 이전 필드로 이동
            value.isEmpty() && index > 0 -> {
                codeValues = codeValues.toMutableList().apply {
                    this[index - 1] = ""
                }
                focusRequesters[index - 1].requestFocus()
            }
            // 첫 번째 필드가 비어있는 경우 -> 키보드 숨김
            index == 0 && value.isEmpty() -> {
                keyboardController?.hide()
            }
        }
    }

    fun onFieldFocused(index: Int) {
        focusedIndex = index
        // 이미 값이 있는 필드 클릭 시 첫 번째 빈 필드로 이동
        if (codeValues[index].isNotEmpty()) {
            focusFirstEmpty()
        }
    }

    fun focusFirst() {
        focusRequesters[0].requestFocus()
    }

    fun focusFirstEmpty() {
        val targetIndex = codeValues.indexOfFirst { it.isEmpty() }
            .takeIf { it != -1 } ?: (AttendanceConstants.CODE_INPUT_FIELDS - 1)
        focusRequesters[targetIndex].requestFocus()
    }

    fun clearAndFocusFirst() {
        codeValues = List(AttendanceConstants.CODE_INPUT_FIELDS) { "" }
        focusFirst()
    }

    fun getCompleteCode(): String = codeValues.joinToString("")
}

/**
 * 코드 입력 상태를 기억하는 Composable
 */
@Composable
private fun rememberCodeInputState(): CodeInputState {
    val keyboardController = LocalSoftwareKeyboardController.current
    return remember { CodeInputState(keyboardController) }
}
