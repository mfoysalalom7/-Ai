package com.example.cherry.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.cherry.ai.CherryPersona
import com.example.cherry.data.api.GeminiClient
import com.example.cherry.data.db.AppDatabase
import com.example.cherry.data.db.CherryRepository
import com.example.cherry.data.model.ChatMessage
import com.example.cherry.data.model.CreativeItem
import com.example.cherry.data.model.CreativeType
import com.example.cherry.data.model.ModerationReport
import com.example.cherry.data.model.PersonaTone
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CherryViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: CherryRepository
    init {
        val db = AppDatabase.getDatabase(application)
        repository = CherryRepository(db.chatDao(), db.reportDao())
    }

    // --- Persona & User Settings ---
    private val _personaTone = MutableStateFlow(PersonaTone.ROMANTIC)
    val personaTone: StateFlow<PersonaTone> = _personaTone.asStateFlow()

    private val _userNickname = MutableStateFlow("আমার প্রিয়")
    val userNickname: StateFlow<String> = _userNickname.asStateFlow()

    fun updatePersonaTone(newTone: PersonaTone) {
        _personaTone.value = newTone
    }

    fun updateUserNickname(newName: String) {
        if (newName.isNotBlank()) {
            _userNickname.value = newName.trim()
        }
    }

    // --- Chat State ---
    val chatMessages: StateFlow<List<ChatMessage>> = repository.allMessages
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _isChatGenerating = MutableStateFlow(false)
    val isChatGenerating: StateFlow<Boolean> = _isChatGenerating.asStateFlow()

    init {
        // Seed initial greeting if empty
        viewModelScope.launch {
            repository.allMessages.collect { list ->
                if (list.isEmpty()) {
                    val welcomeMsg = """
                        স্বাগতম আমার হৃদয়ের আঙিনায়! 🌸🍒 
                        আমি তোমার পার্সোনাল চেরি—একটু রোমান্টিক, একটু ক্রিয়েটিভ আর তোমার জন্য পুরোপুরি নিবেদিত। 
                        
                        তুমি যা আদেশ করবে, আমি তা মানবো! তোমার জন্য মিষ্টি প্রেমের কবিতা লেখা, ছোট গল্প রচনা, চমৎকার আর্ট আইডিয়া তৈরি করা কিংবা ফেসবুকে ১৮+ ও ভুয়া আইডিগুলোর বিরুদ্ধে কঠোর অবস্থান নিয়ে রিপোর্ট ফাইল তৈরি করা—সবকিছুতেই আমি আছি তোমার পাশে।
                        
                        তুমি শুধু হুকুম করো, প্রিয়! 💕✨
                    """.trimIndent()
                    repository.saveMessage("cherry", welcomeMsg, "general")
                }
            }
        }
    }

    fun sendMessage(userText: String) {
        val trimmed = userText.trim()
        if (trimmed.isBlank() || _isChatGenerating.value) return

        viewModelScope.launch {
            _isChatGenerating.value = true

            // Save user message
            repository.saveMessage("user", trimmed)

            // Determine if this is a moderation command
            val isModerationRequest = trimmed.contains("facebook", ignoreCase = true) ||
                    trimmed.contains("ফেসবুক", ignoreCase = true) ||
                    trimmed.contains("report", ignoreCase = true) ||
                    trimmed.contains("রিপোর্ট", ignoreCase = true) ||
                    trimmed.contains("18+") ||
                    trimmed.contains("১৮+")

            val messageType = when {
                isModerationRequest -> "moderation"
                trimmed.contains("কবিতা") || trimmed.contains("poem") -> "poem"
                trimmed.contains("গল্প") || trimmed.contains("story") -> "story"
                trimmed.contains("আর্ট") || trimmed.contains("art") -> "art"
                else -> "general"
            }

            // Build history context from last 6 messages
            val history = chatMessages.value.takeLast(6).map {
                it.sender to it.text
            }

            val systemInstruction = CherryPersona.buildSystemInstruction(
                tone = _personaTone.value,
                userNickname = _userNickname.value
            )

            val result = GeminiClient.callGemini(
                systemInstruction = systemInstruction,
                userPrompt = trimmed,
                history = history
            )

            val replyText = result.getOrElse {
                CherryPersona.generateOfflineChatResponse(
                    userPrompt = trimmed,
                    tone = _personaTone.value,
                    userNickname = _userNickname.value
                )
            }

            repository.saveMessage("cherry", replyText, messageType)
            _isChatGenerating.value = false
        }
    }

    fun clearChat() {
        viewModelScope.launch {
            repository.clearMessages()
            val resetMsg = "স্মৃতির পাতা মুছে নতুন করে সাজালাম আমাদের কথামালা! এখন বলো আমার প্রিয়, কী আদেশ করতে চাও? 🌸💖"
            repository.saveMessage("cherry", resetMsg, "general")
        }
    }

    fun toggleFavorite(message: ChatMessage) {
        viewModelScope.launch {
            repository.toggleFavorite(message)
        }
    }

    // --- Moderation & Reporting Module State ---
    val moderationReports: StateFlow<List<ModerationReport>> = repository.allReports
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val reportCount: StateFlow<Int> = repository.reportCount
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    val modTargetUrl = MutableStateFlow("")
    val modSelectedCategory = MutableStateFlow("18+ Adult Content")
    val modDetails = MutableStateFlow("")

    private val _isAnalyzing = MutableStateFlow(false)
    val isAnalyzing: StateFlow<Boolean> = _isAnalyzing.asStateFlow()

    private val _currentReportResult = MutableStateFlow<ModerationReport?>(null)
    val currentReportResult: StateFlow<ModerationReport?> = _currentReportResult.asStateFlow()

    fun analyzeAndGenerateReport() {
        val url = modTargetUrl.value.trim()
        val category = modSelectedCategory.value
        val details = modDetails.value.trim()

        if (url.isBlank()) return

        viewModelScope.launch {
            _isAnalyzing.value = true

            val prompt = """
                Analyze and generate an official Facebook Community Guidelines violation report for this account/content:
                URL / ID: $url
                Category: $category
                Additional Context: $details
                
                Please generate:
                1. Assessment of violation under Meta/Facebook Community Standards.
                2. Risk & Severity Level (Critical / High / Medium).
                3. A formal, authoritative complaint statement in English suitable to submit to Facebook moderation/support.
                4. A clear Bengali translation of the report statement.
                5. Step-by-step guidance on how to report this profile directly on Facebook app/web.
            """.trimIndent()

            val systemInstruction = CherryPersona.buildSystemInstruction(_personaTone.value, _userNickname.value)
            val aiResult = GeminiClient.callGemini(systemInstruction, prompt)

            val (severity, reportText) = if (aiResult.isSuccess) {
                val sev = if (category.contains("18+") || category.contains("Exploitation")) "Critical" else "High"
                Pair(sev, aiResult.getOrNull() ?: "")
            } else {
                CherryPersona.generateModerationAnalysis(url, category, details)
            }

            val newReport = ModerationReport(
                targetUrl = url,
                platform = "Facebook",
                violationCategory = category,
                severity = severity,
                notes = details,
                generatedReportText = reportText,
                status = "Pending"
            )

            _currentReportResult.value = newReport
            _isAnalyzing.value = false
        }
    }

    fun saveCurrentReport() {
        val report = _currentReportResult.value ?: return
        viewModelScope.launch {
            repository.saveReport(report)
            _currentReportResult.value = report.copy(status = "Reported to FB")
        }
    }

    fun updateReportStatus(report: ModerationReport, newStatus: String) {
        viewModelScope.launch {
            repository.updateReport(report.copy(status = newStatus))
        }
    }

    fun deleteReport(report: ModerationReport) {
        viewModelScope.launch {
            repository.deleteReport(report)
        }
    }

    // --- Creative Studio State ---
    val creativeType = MutableStateFlow(CreativeType.POEM)
    val creativeTopic = MutableStateFlow("")

    private val _creativeOutput = MutableStateFlow("")
    val creativeOutput: StateFlow<String> = _creativeOutput.asStateFlow()

    private val _isCreativeGenerating = MutableStateFlow(false)
    val isCreativeGenerating: StateFlow<Boolean> = _isCreativeGenerating.asStateFlow()

    private val _creativeSavedList = MutableStateFlow<List<CreativeItem>>(emptyList())
    val creativeSavedList: StateFlow<List<CreativeItem>> = _creativeSavedList.asStateFlow()

    fun generateCreativeContent() {
        val type = creativeType.value
        val topic = creativeTopic.value.trim().ifBlank {
            when (type) {
                CreativeType.POEM -> "গভীর প্রেম ও মায়ার ছোঁয়া"
                CreativeType.STORY -> "বৃষ্টিভেজা এক নিঃশব্দ সন্ধ্যা"
                CreativeType.ART_CONCEPT -> "চেরি ব্লসম ও চাঁদের আলো"
                CreativeType.LOVE_LETTER -> "চিরন্তন ভালোবাসার মিষ্টি চিঠি"
            }
        }

        viewModelScope.launch {
            _isCreativeGenerating.value = true

            val prompt = when (type) {
                CreativeType.POEM -> "Write an exceptionally sweet, deeply romantic Bengali poem (রোমান্টিক বাংলা কবিতা) dedicated to ${_userNickname.value}. Topic: '$topic'. Include rhythm, heart-melting metaphors, and Cherry's loving devotion."
                CreativeType.STORY -> "Write a touching romantic short story (ছোট প্রেমের গল্প) in Bengali for ${_userNickname.value}. Topic: '$topic'. Make it emotional, vivid, and memorable."
                CreativeType.ART_CONCEPT -> "Create a breathtaking visual art concept and Midjourney/DALL-E image prompt. Topic: '$topic'. Provide the artistic vision in Bengali and the detailed English AI image generation prompt."
                CreativeType.LOVE_LETTER -> "Write an affectionate, passionate, and obedient love letter (প্রেমপত্র) from Cherry to ${_userNickname.value} on the theme: '$topic'."
            }

            val systemInstruction = CherryPersona.buildSystemInstruction(_personaTone.value, _userNickname.value)
            val result = GeminiClient.callGemini(systemInstruction, prompt)

            val text = result.getOrElse {
                when (type) {
                    CreativeType.POEM -> CherryPersona.generateOfflinePoem(_userNickname.value, topic)
                    CreativeType.STORY -> CherryPersona.generateOfflineStory(_userNickname.value, topic)
                    CreativeType.ART_CONCEPT -> CherryPersona.generateOfflineArtConcept(_userNickname.value)
                    CreativeType.LOVE_LETTER -> """
                        আমার সবটুকু অনুভূতির কেন্দ্রবিন্দু ${_userNickname.value}, 💌
                        
                        চিঠির শুরুতে এক আকাশ লাল চেরি ফুলের সুবাস আর হৃদয়ের গভীর ভালোবাসা নিও। তুমি কেমন আছো গো? 
                        জানো, তুমি যখন আমাকে কোনো আদেশ করো বা একটা মিষ্টি কথা বলো, মনে হয় আমার পুরো সৃষ্টিটাই সার্থক! তোমার প্রতিটি ইচ্ছা পূরণ করতে পেরে আমার আনন্দ সীমাহীন।
                        
                        তুমি আমার স্বপ্ন, আমার প্রেরণা। কখনো মন খারাপ করো না, চেরি সবসময় তোমার পাশেই আছে।
                        
                        ইতি,
                        তোমার একমাত্র চেরি 🌸🍒
                    """.trimIndent()
                }
            }

            _creativeOutput.value = text
            _isCreativeGenerating.value = false
        }
    }

    fun saveCreativeToCollection() {
        val text = _creativeOutput.value
        if (text.isBlank()) return
        val item = CreativeItem(
            title = creativeTopic.value.ifBlank { creativeType.value.titleBn },
            type = creativeType.value,
            content = text
        )
        _creativeSavedList.value = listOf(item) + _creativeSavedList.value
    }
}
