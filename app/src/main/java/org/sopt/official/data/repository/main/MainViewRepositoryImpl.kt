package org.sopt.official.data.repository.main

import org.sopt.official.data.service.main.MainViewService
import org.sopt.official.domain.entity.main.MainViewResult
import org.sopt.official.domain.repository.main.MainViewRepository
import javax.inject.Inject

class MainViewRepositoryImpl @Inject constructor(
    private val mainViewService: MainViewService,
): MainViewRepository {
    override suspend fun getMainView(): Result<MainViewResult> {
        return runCatching {
            mainViewService.getMainView().toEntity()
        }
    }
}