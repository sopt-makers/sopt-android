package org.sopt.official.feature.sopletter.topic

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.persistentListOf
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.feature.sopletter.common.component.SopletterTopbar
import org.sopt.official.feature.sopletter.topic.component.SopletterTopicItem
import org.sopt.official.feature.sopletter.topic.model.SopletterTopicUiModel

@Composable
internal fun SopletterTopicRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    navigateToMain: (Int) -> Unit,
    viewModel: SopletterTopicViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SopletterTopicScreen(
        modifier = Modifier
            .padding(paddingValues),
        uiState = uiState,
        onBackClick = navigateUp,
        onTopicClick = navigateToMain
    )
}

@Composable
private fun SopletterTopicScreen(
    uiState: SopletterTopicState,
    onBackClick: () -> Unit,
    onTopicClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                color = SoptTheme.colors.background
            ),
    ) {
        SopletterTopbar(
            onBackClick = onBackClick,
            topbarTitle = "솝레터 주제"
        )

        Spacer(modifier = Modifier.height(10.dp))

        uiState.topicList.forEachIndexed { index, item ->
            SopletterTopicItem(
                modifier = Modifier
                    .padding(horizontal = 20.dp),
                topicTitle = item.title,
                onTopicClick = {
                    onTopicClick(item.topicId)
                }
            )
            if (index < uiState.topicList.lastIndex) {
                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Preview
@Composable
private fun SopletterTopicScreenPreview() {
    SoptTheme {
        SopletterTopicScreen(
            uiState = SopletterTopicState(
                topicList = persistentListOf(
                    SopletterTopicUiModel(
                        topicId = 1,
                        title = "38기 회고"
                    ),
                    SopletterTopicUiModel(
                        topicId = 2,
                        title = "38기 회고"
                    ),

                )
            ),
            onBackClick = {},
            onTopicClick = {}
        )
    }
}