package com.lyh.carexplorer.feature.user

import app.cash.turbine.test
import com.lyh.carexplorer.data.core.AppDispatchers
import com.lyh.carexplorer.domain.UserUseCase
import com.lyh.carexplorer.domain.model.UserModel
import com.lyh.carexplorer.feature.user.util.CoroutinesTestExtension
import com.lyh.carexplorer.feature.user.util.InstantExecutorExtension
import com.lyh.carexplorer.feature.user.detail.UserViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDate

@ExtendWith(InstantExecutorExtension::class, CoroutinesTestExtension::class)
class UserViewModelTest {

    private val userId = 5
    private val dispatchers = AppDispatchers(UnconfinedTestDispatcher(), UnconfinedTestDispatcher())

    @Test
    fun `WHEN get user empty THEN get empty state`() = runTest {

        val userUseCase = mockk<UserUseCase> {
            coEvery { getUser() } returns null
        }
        val userViewModel = UserViewModel(userUseCase, dispatchers)

        userViewModel.user.test {
            val result = awaitItem()
            assertNull(result.id)
            assertNull(result.photoUri)
            assertEquals("", result.firstName)
            assertEquals("", result.lastName)
            assertEquals("", result.address)
            assertEquals(LocalDate.now(), result.birthday)
            assertEquals("", result.birthdayString)
        }
    }

    @Test
    fun `WHEN get user filled THEN get filled state`() = runTest {
        val userModel = createUserModel(userId)

        val userUseCase = mockk<UserUseCase> {
            coEvery { getUser() } returns userModel
        }
        val userViewModel = UserViewModel(userUseCase, dispatchers)

        userViewModel.user.test {
            val result = awaitItem()
            assertEquals(userId, result.id)
            assertEquals(userModel.photoUri, result.photoUri)
            assertEquals(userModel.firstName, result.firstName)
            assertEquals(userModel.lastName, result.lastName)
            assertEquals(userModel.address, result.address)
            assertEquals(LocalDate.of(1998, 2, 20), result.birthday)
            assertEquals("1998-02-20", result.birthdayString)

        }
    }

    @Test
    fun `WHEN set first name THEN state is updated`() = runTest {

        val userUseCase = mockk<UserUseCase> {
            coEvery { getUser() } returns null
        }
        val userViewModel = UserViewModel(userUseCase, dispatchers)

        userViewModel.user.test {
            val initial = awaitItem()
            assertTrue(initial.firstName.isEmpty())

            userViewModel.setFirstName("new value")

            val updated = awaitItem()
            assertEquals("new value", updated.firstName)
        }
    }

    @Test
    fun `WHEN set last name THEN state is updated`() = runTest {

        val userUseCase = mockk<UserUseCase> {
            coEvery { getUser() } returns null
        }
        val userViewModel = UserViewModel(userUseCase, dispatchers)

        userViewModel.user.test {
            val initial = awaitItem()
            assertTrue(initial.lastName.isEmpty())

            userViewModel.setLastName("new value")

            val updated = awaitItem()
            assertEquals("new value", updated.lastName)
        }
    }

    @Test
    fun `WHEN set address THEN state is updated`() = runTest {

        val userUseCase = mockk<UserUseCase> {
            coEvery { getUser() } returns null
        }
        val userViewModel = UserViewModel(userUseCase, dispatchers)

        userViewModel.user.test {
            val initial = awaitItem()
            assertTrue(initial.address.isEmpty())

            userViewModel.setAddress("new value")

            val updated = awaitItem()
            assertEquals("new value", updated.address)
        }
    }

    @Test
    fun `WHEN set birthday THEN state is updated`() = runTest {

        val userUseCase = mockk<UserUseCase> {
            coEvery { getUser() } returns null
        }
        val userViewModel = UserViewModel(userUseCase, dispatchers)
        userViewModel.showCalendar()

        userViewModel.user.test {
            val initial = awaitItem()
            assertEquals(LocalDate.now(), initial.birthday)
            assertTrue(initial.birthdayString.isEmpty())
            assertTrue(initial.showCalendar)

            userViewModel.setBirthDay(1996, 12, 25)

            val updated = awaitItem()
            assertEquals(LocalDate.of(1996, 12, 25), updated.birthday)
            assertEquals("1996-12-25", updated.birthdayString)
            assertFalse(updated.showCalendar)
        }
    }

    @Test
    fun `WHEN show calendar THEN state is updated`() = runTest {

        val userUseCase = mockk<UserUseCase> {
            coEvery { getUser() } returns null
        }
        val userViewModel = UserViewModel(userUseCase, dispatchers)

        userViewModel.user.test {
            val initial = awaitItem()
            assertFalse(initial.showCalendar)

            userViewModel.showCalendar()

            val updated = awaitItem()
            assertTrue(updated.showCalendar)
        }
    }

    @Test
    fun `WHEN click THEN state is updated and saving user`() = runTest {

        val userUseCase = mockk<UserUseCase> {
            coEvery { getUser() } returns null

        }
        val userViewModel = UserViewModel(userUseCase,dispatchers)

        userViewModel.user.test {
            val initial = awaitItem()
            assertFalse(initial.editable)

            userViewModel.click()

            val inUpdateMode = awaitItem()
            assertTrue(inUpdateMode.editable)

            coEvery { userUseCase.updateUser(any()) } returns Unit
            userViewModel.click()

            val updatingMode = awaitItem()
            assertFalse(updatingMode.editable)

            coVerify(exactly = 1) { userUseCase.updateUser(any()) }
        }
    }

    private fun createUserModel(id: Int) = UserModel(
        id,
        "https://myurl.com/tiny/$id.jpg",
        "Firstname $id",
        "Lastname $id",
        "adress $id",
        887932800000L
    )
}
