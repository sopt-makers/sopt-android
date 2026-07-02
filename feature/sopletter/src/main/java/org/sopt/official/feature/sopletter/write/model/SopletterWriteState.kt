package org.sopt.official.feature.sopletter.write.model

import androidx.compose.runtime.Immutable

@Immutable
data class SopletterWriteUiState(
    val writerName: String = "",
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String? = null
)

sealed interface SopletterWriteSideEffect {
    data class ShowSnackbar(
        val message: String,
        val type: SopletterSnackbarType
    ) : SopletterWriteSideEffect

    data object NavigateToMain : SopletterWriteSideEffect
}


// TODO: 테스트용, merge 시점에 삭제 예정
interface SopletterSnackbarType{
    @Immutable
    data object SUCCESS : SopletterSnackbarType
    @Immutable
    data object FAILURE : SopletterSnackbarType
    @Immutable
    data object WARNING : SopletterSnackbarType
}