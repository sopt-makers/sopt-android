package org.sopt.official.domain.entity.main

import org.sopt.official.domain.entity.UserState
import org.sopt.official.util.wrapper.NullableWrapper

data class MainViewResult(
    val user: MainViewUserInfo,
    val operation: MainViewOperationInfo,
)

data class MainViewUserInfo(
    val status: UserState = UserState.UNAUTHENTICATED,
    val name: NullableWrapper<String> = NullableWrapper.none(),
    val profileImage: NullableWrapper<String> = NullableWrapper.none(),
    val generationList: NullableWrapper<List<Long>> = NullableWrapper.none(),
)

data class MainViewOperationInfo(
    val attendanceScore: NullableWrapper<Double> = NullableWrapper.none(),
    val announcement: NullableWrapper<String> = NullableWrapper.none(),
)
