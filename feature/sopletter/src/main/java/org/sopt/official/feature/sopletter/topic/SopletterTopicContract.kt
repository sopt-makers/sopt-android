package org.sopt.official.feature.sopletter.topic

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.sopt.official.feature.sopletter.topic.model.SopletterTopicUiModel

@Immutable
data class SopletterTopicState(
    val topicList: ImmutableList<SopletterTopicUiModel> = persistentListOf(),
    val isLoading: Boolean = false,
)