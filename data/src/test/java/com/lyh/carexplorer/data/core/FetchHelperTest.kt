package com.lyh.carexplorer.data.core

import app.cash.turbine.test
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class FetchHelperTest {

    private val list = listOf(1, 2, 3)
    private val listLocal = listOf(1, 2, 3, 4)

    @Test
    fun `WHEN get remote data success THEN return success`() = runTest {
        fetchAndStoreLocally(::getRemoteSuccess, ::sync, ::getLocal, null) {}
            .test {
                val result = awaitItem()
                assertTrue(result.isSuccess)
                val resultSuccess = result.getOrThrow()

                assertEquals(list.size, resultSuccess.size)

                awaitComplete()
            }
    }

    @Test
    fun `WHEN get remote data throws exception, and local data are empty THEN return failure with exception`() = runTest {
        fetchAndStoreLocally(::getRemoteFailed, ::sync, ::getLocalEmpty, null) {}

            .test {
                val result = awaitItem()
                assertTrue(result.isFailure)
                val resultError = result.exceptionOrNull()
                assertNotNull(resultError)
                assertTrue(resultError is IllegalArgumentException)

                awaitComplete()
            }
    }

    @Test
    fun `WHEN get remote data throws exception, and local data are present THEN return local data`() =
        runTest {
            fetchAndStoreLocally(::getRemoteFailed, ::sync, ::getLocal, null) {}
                .test {
                    val result = awaitItem()
                    assertTrue(result.isSuccess)
                    val resultSuccess = result.getOrThrow()

                    assertEquals(listLocal.size, resultSuccess.size)

                    awaitComplete()
                }
        }

    private fun getLocal(): List<Int> = listLocal

    private fun getLocalEmpty(): List<Int> = emptyList()

    private fun getRemoteFailed() = Result.failure<List<Int>>(IllegalArgumentException())

    private fun getRemoteSuccess() = Result.success(list)

    private fun sync(newData: List<Int>): List<Int> = newData
}
