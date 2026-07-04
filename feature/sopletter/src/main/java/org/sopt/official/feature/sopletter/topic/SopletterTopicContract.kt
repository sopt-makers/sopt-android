package org.sopt.official.feature.sopletter.topic

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.sopt.official.domain.sopletter.model.SopletterTopic

@Immutable
data class SopletterTopicState(
    val topicList: ImmutableList<SopletterTopic> = persistentListOf(),
    val isShowErrorDialog: Boolean = false,
)
