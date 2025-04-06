package org.sopt.official.domain.entity.attendance

sealed interface FetchAttendanceCurrentRoundResult {
    data class Success(val round: Int?) : FetchAttendanceCurrentRoundResult
    data class Failure(val errorMessage: String?) : FetchAttendanceCurrentRoundResult
}