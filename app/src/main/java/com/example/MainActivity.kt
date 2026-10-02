package com.example

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.FolderSpecial
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.FolderSpecial
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import com.example.cherry.ui.screens.ChatScreen
import com.example.cherry.ui.screens.CreativeStudioScreen
import com.example.cherry.ui.screens.ModerationScreen
import com.example.cherry.ui.screens.ReportVaultScreen
import com.example.cherry.ui.viewmodel.CherryViewModel
import com.example.ui.theme.CherryAITheme
import java.util.Locale

enum class AppNavTab(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    CHAT("চেরি চ্যাট", Icons.Filled.ChatBubble, Icons.Outlined.ChatBubbleOutline),
    MODERATION("মডারেশন", Icons.Filled.Security, Icons.Outlined.Security),
    CREATIVE("ক্রিয়েটিভ", Icons.Filled.Palette, Icons.Outlined.Palette),
    VAULT("ভল্ট", Icons.Filled.FolderSpecial, Icons.Outlined.FolderSpecial)
}

class MainActivity : ComponentActivity(), TextToSpeech.OnInitListener {

    private val viewModel: CherryViewModel by viewModels()
    private var tts: TextToSpeech? = null
    private var isTtsReady = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        try {
            tts = TextToSpeech(this, this)
        } catch (_: Exception) {}

        setContent {
            CherryAITheme {
                CherryAppContent(
                    viewModel = viewModel,
                    onSpeakText = { text -> speakText(text) }
                )
            }
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts?.setLanguage(Locale.forLanguageTag("bn-BD"))
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                tts?.setLanguage(Locale.US)
            }
            isTtsReady = true
        }
    }

    private fun speakText(text: String) {
        if (isTtsReady && tts != null) {
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "CherryTTS")
        } else {
            Toast.makeText(this, "স্পিচ ইঞ্জিন প্রস্তুত হচ্ছে...", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        tts?.stop()
        tts?.shutdown()
        super.onDestroy()
    }
}

@Composable
fun CherryAppContent(
    viewModel: CherryViewModel,
    onSpeakText: (String) -> Unit
) {
    var selectedTab by remember { mutableStateOf(AppNavTab.CHAT) }

    // Android BackHandler: If on other tabs, go back to CHAT tab
    BackHandler(enabled = selectedTab != AppNavTab.CHAT) {
        selectedTab = AppNavTab.CHAT
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                AppNavTab.values().forEach { tab ->
                    val isSelected = selectedTab == tab
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { selectedTab = tab },
                        icon = {
                            Icon(
                                imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                                contentDescription = tab.title
                            )
                        },
                        label = { Text(tab.title) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        modifier = Modifier.testTag("nav_tab_${tab.name.lowercase()}")
                    )
                }
            }
        }
    ) { innerPadding ->
        val screenModifier = Modifier.padding(innerPadding)
        when (selectedTab) {
            AppNavTab.CHAT -> ChatScreen(
                viewModel = viewModel,
                onSpeakText = onSpeakText,
                modifier = screenModifier
            )
            AppNavTab.MODERATION -> ModerationScreen(
                viewModel = viewModel,
                modifier = screenModifier
            )
            AppNavTab.CREATIVE -> CreativeStudioScreen(
                viewModel = viewModel,
                modifier = screenModifier
            )
            AppNavTab.VAULT -> ReportVaultScreen(
                viewModel = viewModel,
                modifier = screenModifier
            )
        }
    }
}
