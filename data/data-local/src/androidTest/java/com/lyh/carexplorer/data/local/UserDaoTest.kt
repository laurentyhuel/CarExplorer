package com.lyh.carexplorer.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.lyh.carexplorer.data.local.dao.UserDao
import com.lyh.carexplorer.data.local.entity.UserEntity
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class UserDaoTest {
    private lateinit var database: AppDatabase
    private lateinit var userDao: UserDao
    private val user1 = createUser(5)

    @BeforeEach
    fun setupDatabase() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        userDao = database.userDao()
    }

    @AfterEach
    fun closeDb() = database.close()

    @Test
    fun testInsertOrUpdateUserAndGet() = runBlocking {

        userDao.insertOrUpdateUser(user1)

        val users = userDao.getUsers()
        assertEquals(1, users.size)
        assertEquals(user1, users.first())
    }

    private fun createUser(id: Int) = UserEntity(
        id,
        "https://myurl.com/$id.jpg",
        "First name $id",
        "Last name $id",
        "Address $id",
        Date().time
    )
}

