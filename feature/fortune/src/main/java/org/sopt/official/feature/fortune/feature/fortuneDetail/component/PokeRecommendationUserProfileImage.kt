package org.sopt.official.feature.fortune.feature.fortuneDetail.component

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.feature.fortune.R.drawable.ic_empty_profile
import org.sopt.official.feature.fortune.component.UrlImage

@Composable
internal fun PokeRecommendationUserProfileImage(
    profile: String,
    isEmptyProfile: Boolean,
    modifier: Modifier = Modifier,
) {
    when (isEmptyProfile) {
        true -> {
            Image(
                painter = painterResource(ic_empty_profile),
                contentDescription = "profileImageEmpty",
                modifier = modifier,
            )
        }

        false -> {
            UrlImage(
                url = profile,
                contentDescription = "profileImage",
                modifier = modifier,
            )
        }
    }
}

@Preview
@Composable
private fun PokeRecommendationUserProfileImagePreview() {
    SoptTheme {
        PokeRecommendationUserProfileImage(
            profile = "",
            isEmptyProfile = false,
        )
    }
}
