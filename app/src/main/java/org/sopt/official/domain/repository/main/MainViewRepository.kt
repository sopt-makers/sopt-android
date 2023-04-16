package org.sopt.official.domain.repository.main

import org.sopt.official.domain.entity.main.MainViewResult

interface MainViewRepository {
    suspend fun getMainView(): Result<MainViewResult>
}