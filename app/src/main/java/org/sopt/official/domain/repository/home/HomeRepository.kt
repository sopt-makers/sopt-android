package org.sopt.official.domain.repository.home

import org.sopt.official.domain.entity.home.HomeSection
import org.sopt.official.domain.entity.home.SoptUser

interface HomeRepository {
    suspend fun getMainView(): Result<SoptUser>
    suspend fun getMainDescription(): Result<HomeSection>
}