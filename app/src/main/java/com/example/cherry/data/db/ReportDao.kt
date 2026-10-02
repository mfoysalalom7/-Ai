package com.example.cherry.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.cherry.data.model.ModerationReport
import kotlinx.coroutines.flow.Flow

@Dao
interface ReportDao {
    @Query("SELECT * FROM moderation_reports ORDER BY timestamp DESC")
    fun getAllReports(): Flow<List<ModerationReport>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReport(report: ModerationReport): Long

    @Update
    suspend fun updateReport(report: ModerationReport)

    @Delete
    suspend fun deleteReport(report: ModerationReport)

    @Query("SELECT COUNT(*) FROM moderation_reports")
    fun getReportCount(): Flow<Int>

    @Query("DELETE FROM moderation_reports")
    suspend fun clearAllReports()
}
