package com.lyh.carexplorer.data

import com.lyh.carexplorer.data.local.dao.UserDao
import com.lyh.carexplorer.data.local.entity.UserEntity
import com.lyh.carexplorer.domain.model.UserModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class UserRepositoryTest {

    private val userDao = mockk<UserDao>()
    private val users = List(10) { index -> createUserEntity(index) }

    private val userRepository = UserRepository(userDao)

    @Test
    fun `WHEN getUser and no user THEN return null`() = runTest {
        coEvery { userDao.getUsers() } returns emptyList()

        val user = userRepository.getUser()
        assertNull(user)
    }

    @Test
    fun `WHEN getUser and only one user THEN return first`() = runTest {
        coEvery { userDao.getUsers() } returns listOf(createUserEntity(5))

        val user = userRepository.getUser()
        assertNotNull(user)
        assertEquals(5, user?.id)
    }

    @Test
    fun `WHEN getUser and multiple users THEN return first`() = runTest {
        coEvery { userDao.getUsers() } returns users

        val user = userRepository.getUser()
        assertNotNull(user)
        assertEquals(users.first().id, user?.id)
    }

    @Test
    fun `WHEN updateUser THEN call userDao#insertOrUpdateUser`() = runTest {
        coEvery { userDao.insertOrUpdateUser(any()) } returns Unit
        userRepository.updateUser(createUserModel(5))
        coVerify(exactly = 1) { userDao.insertOrUpdateUser(any()) }
    }

    private fun createUserEntity(id: Int) = UserEntity(
        id,
        "https://myurl.com/tiny/$id.jpg",
        "First Name $id",
        "Last Name $id",
        "Address $id",
        System.currentTimeMillis()
    )

    private fun createUserModel(id: Int) = UserModel(
        id,
        "https://myurl.com/tiny/$id.jpg",
        "First Name $id",
        "Last Name $id",
        "Address $id",
        System.currentTimeMillis()

    )
}