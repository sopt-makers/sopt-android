package org.sopt.official.feature.main

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.sopt.official.base.BaseItemType
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(

) : ViewModel() {

    sealed class SmallBlockItemHolder: BaseItemType {
        data class SmallBlock(
            val icon : Int,
            val name : String,
        ): SmallBlockItemHolder()
    }
    sealed class ContentItemHolder: BaseItemType {
        data class Content(
            val icon: Int?,
            val name : String,
        ): ContentItemHolder()
    }
}