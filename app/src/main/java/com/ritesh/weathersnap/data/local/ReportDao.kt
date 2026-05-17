package com.ritesh.weathersnap.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ReportDao {

    @Insert
    suspend fun insert(report: ReportEntity): Long

    @Update
    suspend fun update(report: ReportEntity)

    @Query("DELETE FROM reports WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM reports WHERE status = :status")
    suspend fun deleteByStatus(status: String)

    @Query("SELECT * FROM reports WHERE id = :id")
    suspend fun getById(id: Long): ReportEntity?

    @Query("SELECT * FROM reports WHERE status = :status LIMIT 1")
    suspend fun getFirstByStatus(status: String): ReportEntity?

    @Query("SELECT * FROM reports WHERE status = :status ORDER BY createdAt DESC")
    fun observeByStatus(status: String): Flow<List<ReportEntity>>
}
