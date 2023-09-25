package org.sopt.official.data.repository.home

import org.sopt.official.data.service.home.HomeService
import org.sopt.official.domain.entity.home.HomeSection
import org.sopt.official.domain.entity.home.SoptUser
import org.sopt.official.domain.repository.home.HomeRepository
import javax.inject.Inject

class DefaultHomeRepository @Inject constructor(
    private val homeService: HomeService,
) : HomeRepository {
    override suspend fun getMainView(): Result<SoptUser> {
        return runCatching {
            homeService.getMainView().toEntity()
        }
    }

    override suspend fun getMainDescription(): Result<HomeSection> {
        return runCatching {
            homeService.getMainDescription().toEntity()
        }
    }
}