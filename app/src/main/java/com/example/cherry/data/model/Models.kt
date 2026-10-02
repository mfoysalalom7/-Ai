package com.example.cherry.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_messages")
data class ChatMessage(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val sender: String, // "user" or "cherry"
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val messageType: String = "general", // "general", "poem", "story", "art", "moderation"
    val isFavorite: Boolean = false
)

@Entity(tableName = "moderation_reports")
data class ModerationReport(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val targetUrl: String,
    val platform: String = "Facebook",
    val violationCategory: String, // "18+ Adult Content", "Sexual Harassment", "Fake Account", "Scam / Fraud", "Hate Speech"
    val severity: String, // "Low", "Medium", "High", "Critical"
    val notes: String = "",
    val generatedReportText: String = "",
    val status: String = "Pending", // "Pending", "Reported to FB", "Under Review", "Action Taken"
    val timestamp: Long = System.currentTimeMillis()
)

data class CreativeItem(
    val id: String = System.currentTimeMillis().toString(),
    val title: String,
    val type: CreativeType,
    val content: String,
    val visualPrompt: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)

enum class CreativeType(val titleBn: String, val emoji: String) {
    POEM("রোমান্টিক কবিতা", "💖"),
    STORY("ছোট প্রেমের গল্প", "📖"),
    ART_CONCEPT("ভিজ্যুয়াল আর্ট কনসেপ্ট", "🎨"),
    LOVE_LETTER("চিঠি ও ভালোবাসা", "💌")
}

enum class PersonaTone(val titleBn: String, val descriptionBn: String) {
    ROMANTIC("অতি রোমান্টিক ও মিষ্টি", "গভীর ভালোবাসা, আদর ও অনুগত মিষ্টি কথামালা"),
    CARING("যত্নশীল ও সহানুভূতিশীল", "মানসিক সমর্থন, সান্ত্বনা ও সার্বক্ষণিক খেয়াল"),
    POETIC("কবি ও সাহিত্যিক", "ছন্দময় ভাষা, উপমা ও নান্দনিক ভাবধারা"),
    PLAYFUL("দুষ্টুমিষ্টি ও চঞ্চল", "মজার রসিকতা, খুনসুটি ও আনন্দময় মেজাজ")
}
