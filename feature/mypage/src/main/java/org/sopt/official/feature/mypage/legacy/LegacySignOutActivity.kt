package org.sopt.official.feature.mypage.legacy

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jakewharton.processphoenix.ProcessPhoenix
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.sopt.official.designsystem.Gray300
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.designsystem.White
import org.sopt.official.feature.mypage.R
import org.sopt.official.feature.mypage.component.MyPageButton
import org.sopt.official.feature.mypage.component.MyPageTopBar
import org.sopt.official.feature.mypage.di.authRepository

/** Exact behavioral fallback of the withdrawal screen used before the MyPage redesign. */
@AndroidEntryPoint
class LegacySignOutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SoptTheme {
                val scope = rememberCoroutineScope()

                Scaffold(
                    modifier = Modifier
                        .background(SoptTheme.colors.background)
                        .fillMaxSize(),
                    topBar = {
                        MyPageTopBar(
                            title = "마이페이지",
                            onNavigationIconClick = onBackPressedDispatcher::onBackPressed,
                        )
                    },
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .background(SoptTheme.colors.background),
                    ) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = stringResource(R.string.sign_out_title),
                            color = White,
                            style = SoptTheme.typography.heading16B,
                            modifier = Modifier.padding(start = 20.dp),
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = stringResource(R.string.sign_out_subtitle),
                            color = Gray300,
                            style = SoptTheme.typography.body14R,
                            modifier = Modifier.padding(horizontal = 20.dp),
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        MyPageButton(
                            paddingVertical = 16.dp,
                            modifier = Modifier
                                .padding(20.dp)
                                .fillMaxWidth(),
                            onClick = {
                                scope.launch {
                                    authRepository.clearUserToken()
                                    ProcessPhoenix.triggerRebirth(this@LegacySignOutActivity)
                                }
                            },
                        ) {
                            Text(
                                text = stringResource(R.string.sign_out_button),
                                style = SoptTheme.typography.heading18B,
                            )
                        }
                        Spacer(modifier = Modifier.height(14.dp))
                    }
                }
            }
        }
    }

    companion object {
        fun getIntent(context: Context): Intent = Intent(context, LegacySignOutActivity::class.java)
    }
}
