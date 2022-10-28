package org.sopt.official.feature.setting

import androidx.lifecycle.ViewModel
import org.sopt.official.R
import org.sopt.official.feature.setting.model.SettingItem

class SettingViewModel : ViewModel() {
    val settingItems = listOf(
        SettingItem(
            title = "푸시 알림 설정",
            rightIcon = R.drawable.ic_right,
            contentDescription = "알림 설정화면으로 가기"
        ),
        SettingItem(
            title = "서비스 이용약관"
        ),
        SettingItem(
            title = "로그아웃"
        )
    )
}
