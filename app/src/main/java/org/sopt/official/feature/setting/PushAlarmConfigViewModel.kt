package org.sopt.official.feature.setting

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.sopt.official.domain.entity.Part
import javax.inject.Inject

@HiltViewModel
class PushAlarmConfigViewModel @Inject constructor() : ViewModel() {
    var selectedItems by mutableStateOf(setOf<Part>())
        private set

    fun onItemCheckedChange(part: Part, isChecked: Boolean) {
        selectedItems = if (isChecked) {
            selectedItems + part
        } else {
            selectedItems - part
        }
    }

    fun isSelected(part: Part) = selectedItems.contains(part)
}
