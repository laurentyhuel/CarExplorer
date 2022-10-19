package com.lyh.carexplorer.data.core


import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.*

/**
 * Helper method, to fetch data from API and sync with local data,
 * remote fetch is done if last fetch is more than x minutes,
 * if remote fetch returns error then return local data if exists
 * @param getRemote method to fetch remote data
 * @param syncData method to synchronize remote data with local data
 * @param getLocal method to get local data
 * @param lastFetch timestamp of last successful fetch
 * @param fetchRemoteSuccessCallback callback when fetch successful (to store last fetch timestamp in repository)
 * @return return a [Flow] of [Result]
 */
internal fun <DTO, MODEL> fetchAndStoreLocally(
    getRemote: suspend () -> Result<List<DTO>>,
    syncData: suspend (List<DTO>) -> List<MODEL>,
    getLocal: suspend () -> List<MODEL>,
    lastFetch: Date?,
    fetchRemoteSuccessCallback: () -> Unit
): Flow<Result<List<MODEL>>> = flow {

    if (!isNeedingFreshRemoteData(lastFetch)) {
        emit(Result.success(getLocal()))
        return@flow
    }

    getRemote()
        .onSuccess {
            val models = syncData(it)
            fetchRemoteSuccessCallback()
            emit(Result.success(models))
        }
        .onFailure {
            Result.failure<List<MODEL>>(it)
        }
}

private fun isNeedingFreshRemoteData(lastRemoteSuccess: Date?): Boolean {
    if (lastRemoteSuccess == null) {
        return true
    }
    val nowLessOneHour = Calendar.getInstance().apply {
        // For testing set low value: 10s
        add(Calendar.SECOND, -10)
    }
    return lastRemoteSuccess.before(nowLessOneHour.time)
}
