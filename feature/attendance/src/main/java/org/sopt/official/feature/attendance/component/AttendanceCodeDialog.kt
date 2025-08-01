/*
 * MIT License
 * Copyright 2023-2024 SOPT - Shout Our Passion Together
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
import androidx.compose.ui.focus.FocusDirection
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

@Composable
fun AttendanceCodeDialog(
    title: String,
    onDismiss: () -> Unit,
    onCodeSubmit: (String) -> Unit,
    errorMessage: String?,
    modifier: Modifier = Modifier
) {
    var codeValues by remember { mutableStateOf(List(AttendanceConstants.CODE_INPUT_FIELDS) { "" }) }
    val focusRequesters = remember { List(AttendanceConstants.CODE_INPUT_FIELDS) { FocusRequester() } }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val isCodeComplete = codeValues.all { it.isNotEmpty() }

    // Reset fields when error occurs
    LaunchedEffect(errorMessage) {
        if (errorMessage != null) {
            codeValues = List(AttendanceConstants.CODE_INPUT_FIELDS) { "" }
            focusRequesters[0].requestFocus()
        }
    }

    // Auto-focus first field on dialog open
    LaunchedEffect(Unit) {
        focusRequesters[0].requestFocus()
    }

    // Function to find the first empty field or last field if all are filled
    fun getTargetFieldIndex(): Int {
        val firstEmptyIndex = codeValues.indexOfFirst { it.isEmpty() }
        return if (firstEmptyIndex != -1) firstEmptyIndex else AttendanceConstants.CODE_INPUT_FIELDS - 1
    }

    Dialog(onDismissRequest = onDismiss) {
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
                        // Focus on the first empty field when clicking outside input fields
                        val targetIndex = getTargetFieldIndex()
                        focusRequesters[targetIndex].requestFocus()
                    },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Close button
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

                // Title
                Text(
                    text = title,
                    color = Gray10,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Subtitle
                Text(
                    text = "출석 코드 5자리를 입력해 주세요",
                    color = Gray300,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(22.dp))

                // Code input fields
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.width(AttendanceConstants.DIALOG_WIDTH_DP.dp)
                ) {
                    codeValues.forEachIndexed { index, value ->
                        CodeInputField(
                            value = value,
                            onValueChange = { newValue ->
                                if (newValue.length <= 1 && newValue.all { it.isDigit() }) {
                                    val newCodeValues = codeValues.toMutableList()
                                    newCodeValues[index] = newValue
                                    codeValues = newCodeValues

                                    // Auto-advance to next field when value is entered
                                    if (newValue.isNotEmpty() && index < AttendanceConstants.CODE_INPUT_FIELDS - 1) {
                                        focusRequesters[index + 1].requestFocus()
                                    }
                                }
                            },
                            onBackspace = {
                                // Handle backspace navigation like original
                                if (value.isEmpty() && index > 0) {
                                    // Clear previous field and focus on it
                                    val newCodeValues = codeValues.toMutableList()
                                    newCodeValues[index - 1] = ""
                                    codeValues = newCodeValues
                                    focusRequesters[index - 1].requestFocus()
                                } else if (index == 0 && value.isEmpty()) {
                                    // Hide keyboard on first field backspace when empty
                                    keyboardController?.hide()
                                }
                            },
                            onFocusRequest = {
                                // When field is clicked but already has value, move to first empty
                                val targetIndex = getTargetFieldIndex()
                                if (targetIndex != index) {
                                    focusRequesters[targetIndex].requestFocus()
                                }
                            },
                            focusRequester = focusRequesters[index],
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // Error message
                if (errorMessage != null) {
                    Spacer(modifier = Modifier.height(18.dp))
                    Text(
                        text = errorMessage,
                        color = androidx.compose.material3.MaterialTheme.colorScheme.error,
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Submit button
                Button(
                    onClick = {
                        if (isCodeComplete) {
                            onCodeSubmit(codeValues.joinToString(""))
                        }
                    },
                    enabled = isCodeComplete,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(6.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isCodeComplete) Orange500 else Gray700,
                        disabledContainerColor = Gray700
                    )
                ) {
                    Text(
                        text = "출석하기",
                        color = if (isCodeComplete) Color.Black else Gray400,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun CodeInputField(
    value: String,
    onValueChange: (String) -> Unit,
    onBackspace: () -> Unit,
    onFocusRequest: () -> Unit,
    focusRequester: FocusRequester,
    modifier: Modifier = Modifier
) {
    var isFocused by remember { mutableStateOf(false) }
    
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
                isFocused = focusState.isFocused
                if (focusState.isFocused) {
                    onFocusRequest()
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
        keyboardActions = KeyboardActions(
            onNext = { /* Handle next field focus */ }
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
