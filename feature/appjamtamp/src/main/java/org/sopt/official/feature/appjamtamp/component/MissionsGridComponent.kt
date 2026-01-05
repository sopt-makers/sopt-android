package org.sopt.official.feature.appjamtamp.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import org.sopt.official.feature.appjamtamp.missionlist.component.MissionComponent
import org.sopt.official.feature.appjamtamp.missionlist.model.AppjamtampMissionUiModel

@Composable
internal fun MissionsGridComponent(
    missionList : ImmutableList<AppjamtampMissionUiModel>,
    modifier: Modifier = Modifier,
    onMissionItemClick: (item: AppjamtampMissionUiModel) -> Unit = {},
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(40.dp, Alignment.Top),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(bottom = 80.dp),
    ) {
        items(items = missionList) { mission ->
            MissionComponent(
                mission = mission,
                onClick = {
                    onMissionItemClick(mission)
                },
            )
        }
        item {
            Spacer(modifier = Modifier.padding(vertical = 20.dp))
        }
    }
}