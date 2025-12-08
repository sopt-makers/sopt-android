package org.sopt.official.domain.home.usecase

import javax.inject.Inject
import org.sopt.official.domain.home.model.AppService
import org.sopt.official.domain.home.repository.HomeRepository

class GetAppServiceUseCase @Inject constructor(
    private val homeRepository: HomeRepository
) {
    suspend operator fun invoke(): Result<List<AppService>> {
        return homeRepository.getHomeAppService()
    }
}
