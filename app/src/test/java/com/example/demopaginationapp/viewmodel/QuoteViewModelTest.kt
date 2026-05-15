package com.example.demopaginationapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.demopaginationapp.model.dataclasses.Rating
import com.example.demopaginationapp.model.dataclasses.ResponseList
import com.example.demopaginationapp.model.dataclasses.ResponseListItem
import com.example.demopaginationapp.model.networking.Resource
import com.example.demopaginationapp.model.repositories.AppRepository
import com.example.demopaginationapp.utils.ConnectivityObserver
import com.example.demopaginationapp.utils.NetworkConnectivityObserver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

@OptIn(ExperimentalCoroutinesApi::class)
class QuoteViewModelTest {

    // 1. The Rules
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    // 2. The Mocks
    private lateinit var viewModel: QuoteViewModel
    private val mockRepository = mock(AppRepository::class.java)
    private val mockConnectivityObserver = mock(NetworkConnectivityObserver::class.java)

    @Before
    fun setup() {
        // 3. Setup: Set the Main dispatcher to our test dispatcher
        Dispatchers.setMain(testDispatcher)
        viewModel = QuoteViewModel(mockRepository,mockConnectivityObserver)
    }

    @After
    fun tearDown() {
        // 4. Cleanup
        Dispatchers.resetMain()
    }

    @Test
    fun `getList updates itemsList with success data`() = runTest {
        // 1. Arrange: Create the specific ResponseList object
        val fakeData = ResponseList().apply {
            add(ResponseListItem(
                id = 1, title = "Test Quote",
                category = "Test",
                description = "Test",
                image = "Test",
                price = 5.0,
                rating = Rating(
                    count = 5,
                    rate = 5.0
                )
            )
            ) // Assuming these fields exist
        }
        val fakeResponse = Resource.success(fakeData)

        `when`(mockRepository.getList()).thenReturn(fakeResponse)

        // 2. Act
        viewModel.getList()
        testDispatcher.scheduler.advanceUntilIdle()

        // 3. Assert
        assertEquals(fakeResponse, viewModel.itemsList.value)
    }
}