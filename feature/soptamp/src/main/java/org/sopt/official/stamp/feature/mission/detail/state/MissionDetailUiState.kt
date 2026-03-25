package org.sopt.official.stamp.feature.mission.detail.state

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.sopt.official.domain.soptamp.model.Archive
import org.sopt.official.domain.soptamp.model.ImageModel
import org.sopt.official.stamp.designsystem.component.toolbar.ToolbarIconType
import org.sopt.official.stamp.feature.mission.detail.model.StampClapUserUiModel
import org.sopt.official.stamp.feature.mission.detail.type.MissionDetailModeType

data class MissionDetailUiState(
    val id: Int = -1,
    val imageUri: ImageModel = ImageModel.Empty,
    val content: String = "",
    val date: String = "",
    val stampId: Int = -1,
    val isSuccess: Boolean = false,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val error: Throwable? = null,
    val isCompleted: Boolean = false,
    val mode: MissionDetailModeType = MissionDetailModeType.READ_ONLY,
    val toolbarIconType: ToolbarIconType = ToolbarIconType.NONE,
    val isDeleteSuccess: Boolean = false,
    val isDeleteDialogVisible: Boolean = false,
    val isMe: Boolean = true,
    val isBottomSheetOpened: Boolean = false,
    val appliedCount: Int = 0, // 앰플(이번 요청으로 실제 반영된 증가량)
    val totalClapCount: Int = 0,
    val viewCount: Int = 0,
    val myClapCount: Int? = 0, // UI 표시용
    val unSyncedClapCount: Int = 0, // 서버 전송용
    val clappers: ImmutableList<StampClapUserUiModel> = persistentListOf(),
    val isBadgeVisible: Boolean = false,
    val initSnapshotImageUri: ImageModel = ImageModel.Empty,
    val initSnapshotContent: String = "",
    val initSnapshotDate: String = "",
) {
    companion object {
        fun from(data: Archive) =
            MissionDetailUiState(
                id = data.missionId,
                imageUri = if (data.images.isEmpty()) ImageModel.Empty else ImageModel.Remote(data.images),
                content = data.contents,
                date = data.activityDate,
                totalClapCount = data.clapCount,
                viewCount = data.viewCount,
                myClapCount = data.myClapCount
            )
    }
}
