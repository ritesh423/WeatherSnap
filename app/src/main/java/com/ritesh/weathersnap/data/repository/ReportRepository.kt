package com.ritesh.weathersnap.data.repository

import com.ritesh.weathersnap.data.local.ReportDao
import com.ritesh.weathersnap.data.local.ReportEntity
import com.ritesh.weathersnap.data.local.mapper.toDomain
import com.ritesh.weathersnap.domain.model.Report
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReportRepository @Inject constructor(
    private val dao: ReportDao
) {

    fun observeSavedReports(): Flow<List<Report>> =
        dao.observeByStatus(ReportEntity.STATUS_SAVED)
            .map { entities -> entities.map { it.toDomain() } }

    suspend fun getById(id: Long): ReportEntity? = dao.getById(id)

    suspend fun getDraft(): ReportEntity? = dao.getFirstByStatus(ReportEntity.STATUS_DRAFT)

    suspend fun insert(report: ReportEntity): Long = dao.insert(report)

    suspend fun update(report: ReportEntity) = dao.update(report)

    suspend fun deleteById(id: Long) = dao.deleteById(id)

    suspend fun deleteAllDrafts() = dao.deleteByStatus(ReportEntity.STATUS_DRAFT)
}
