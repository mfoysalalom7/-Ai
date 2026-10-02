package com.example.cherry.data.db

import com.example.cherry.data.model.ChatMessage
import com.example.cherry.data.model.ModerationReport
import kotlinx.coroutines.flow.Flow

class CherryRepository(
    private val chatDao: ChatDao,
    private val reportDao: ReportDao
) {
    val allMessages: Flow<List<ChatMessage>> = chatDao.getAllMessages()
    val allReports: Flow<List<ModerationReport>> = reportDao.getAllReports()
    val reportCount: Flow<Int> = reportDao.getReportCount()

    suspend fun saveMessage(sender: String, text: String, type: String = "general"): Long {
        return chatDao.insertMessage(
            ChatMessage(
                sender = sender,
                text = text,
                messageType = type
            )
        )
    }

    suspend fun clearMessages() {
        chatDao.clearChat()
    }

    suspend fun toggleFavorite(message: ChatMessage) {
        chatDao.updateMessage(message.copy(isFavorite = !message.isFavorite))
    }

    suspend fun saveReport(report: ModerationReport): Long {
        return reportDao.insertReport(report)
    }

    suspend fun updateReport(report: ModerationReport) {
        reportDao.updateReport(report)
    }

    suspend fun deleteReport(report: ModerationReport) {
        reportDao.deleteReport(report)
    }

    suspend fun clearReports() {
        reportDao.clearAllReports()
    }
}
