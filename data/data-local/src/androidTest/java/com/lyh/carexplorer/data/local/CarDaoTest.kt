package com.lyh.carexplorer.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import com.lyh.carexplorer.data.local.dao.CarDao
import com.lyh.carexplorer.data.local.entity.CarEntity
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CarDaoTest {
    private lateinit var database: AppDatabase
    private lateinit var carDao: CarDao
    private val car1 = createCar(5)
    private val car2 = createCar(6)

    @BeforeEach
    fun setupDatabase() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        carDao = database.carDao()
    }

    @AfterEach
    fun closeDb() = database.close()

    @Test
    fun testInsertData() = runBlocking {

        carDao.insertCars(listOf(car1, car2))

        val cars = carDao.getCars()
        assertEquals(2, cars.size)
        // Car is a data class, so check is made on properties, and not instance :)
        assertEquals(cars.first(), car1)
    }

    @Test
    fun testSearchData() = runBlocking {

        carDao.insertCars(listOf(car1, car2))

        carDao.searchCars("%xp%").test {
            val cars = awaitItem()
            assertEquals(2, cars.size)
            // Car is a data class, so check is made on properties, and not instance :)
            assertEquals(cars.first(), car1)
        }

    }

    @Test
    fun testDeleteData() = runBlocking {

        carDao.insertCars(listOf(car1, car2))

        val list = carDao.getCars()
        assertEquals(2, list.size)

        // delete
        carDao.deleteAll()

        val listAfterDelete = carDao.getCars()
        assertTrue(listAfterDelete.isEmpty())
    }

    @Test
    fun testGetCarById() = runBlocking {
        val id = 5
        val first = carDao.getCarById(id)
        assertNull(first)

        carDao.insertCars(listOf(car1))

        val second = carDao.getCarById(id)
        assertNotNull(second)
    }

    private fun createCar(id: Int) = CarEntity(
        id,
        "Brand Xp $id",
        "Model xP $id",
        2010 + id,
        "https://myurl.com/$id.jpg",
        "equipment A,equipment B,equipment C"
    )
}

