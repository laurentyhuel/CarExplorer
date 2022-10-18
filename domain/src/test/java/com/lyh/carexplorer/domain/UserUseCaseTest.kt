package com.lyh.carexplorer.domain

import com.lyh.carexplorer.domain.model.UserModel
import com.lyh.carexplorer.domain.repository.IUserRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class UserUseCaseTest {

    private val userRepository = mockk<IUserRepository>()
    private val userUseCase = UserUseCase(userRepository)

    @Test
    fun `WHEN get user THEN call userRepository#getUser()`() = runTest {

        coEvery { userRepository.getUser() } returns createUserModel(5)
        val user = userUseCase.getUser()

        assertEquals(5, user?.id)
        coVerify(exactly = 1) { userRepository.getUser() }
    }

    @Test
    fun `WHEN update user THEN call userRepository#updateUser()`() = runTest {

        coEvery { userRepository.updateUser(any()) } returns Unit
        userUseCase.updateUser(createUserModel(6))
        coVerify(exactly = 1) { userRepository.updateUser(any()) }
    }


    private fun createUserModel(id: Int) = UserModel(
        id,
        "https://myurl.com/tiny/$id.jpg",
        "Firstname $id",
        "Lastname $id",
        "Address $id",
        System.currentTimeMillis()
    )
}