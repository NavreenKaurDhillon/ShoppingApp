package com.example.demopaginationapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.demopaginationapp.model.dataclasses.Rating
import com.example.demopaginationapp.model.dataclasses.ResponseList
import com.example.demopaginationapp.model.dataclasses.ResponseListItem
import com.example.demopaginationapp.model.networking.Resource
import com.example.demopaginationapp.model.networking.Status
import com.example.demopaginationapp.model.repositories.AppRepository
import com.example.demopaginationapp.utils.ConnectivityObserver
import com.example.demopaginationapp.utils.NetworkConnectivityObserver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emptyFlow
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
import org.mockito.Mockito.verify
import org.mockito.Mockito.atLeastOnce

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
        // Mock the network observer flow to prevent crashes during init
        `when`(mockConnectivityObserver.observe()).thenReturn(emptyFlow())
        viewModel = QuoteViewModel(mockRepository, mockConnectivityObserver)
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

        // Assert
        assertEquals(Status.SUCCESS, viewModel.itemsList.value?.status)
        assertEquals(fakeData, viewModel.itemsList.value?.data)
    }

    @Test
    fun `searchQuote filters data correctly based on title`() = runTest {
        // Arrange: Load some initial data
        val item1 = ResponseListItem(id = 1, title = "Apple", description = "Fruit", category = "A", image = "", price = 1.0, rating = Rating(1, 1.0))
        val item2 = ResponseListItem(id = 2, title = "Banana", description = "Yellow", category = "B", image = "", price = 2.0, rating = Rating(1, 1.0))
        val fakeData = ResponseList().apply { add(item1); add(item2) }
        
        `when`(mockRepository.getList()).thenReturn(Resource.success(fakeData))
        viewModel.getList()
        testDispatcher.scheduler.advanceUntilIdle()

        // Act: Search for "Apple"
        viewModel.searchQuote("Apple")

        // Assert
        val result = viewModel.itemsList.value?.data
        assertEquals(1, result?.size)
        assertEquals("Apple", result?.get(0)?.title)
    }

    @Test
    fun `searchQuote returns all items when query is empty`() = runTest {
        // Arrange
        val item1 = ResponseListItem(id = 1, title = "Apple", description = "Fruit", category = "A", image = "", price = 1.0, rating = Rating(1, 1.0))
        val fakeData = ResponseList().apply { add(item1) }
        
        `when`(mockRepository.getList()).thenReturn(Resource.success(fakeData))
        viewModel.getList()
        testDispatcher.scheduler.advanceUntilIdle()

        // Act: Search with empty string
        viewModel.searchQuote("")

        // Assert
        assertEquals(1, viewModel.itemsList.value?.data?.size)
    }

    @Test
    fun `retry triggers repository getList call`() = runTest {
        // Act
        viewModel.retry()
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert: Verify getList was called (it's also called once in init)
        verify(mockRepository, atLeastOnce()).getList()
    }

    @Test
    fun `sortQuotes sorts by price low to high`() = runTest {
        // Arrange
        val item1 = ResponseListItem(id = 1, title = "A", description = "", category = "", image = "", price = 10.0, rating = Rating(1, 5.0))
        val item2 = ResponseListItem(id = 2, title = "B", description = "", category = "", image = "", price = 5.0, rating = Rating(1, 4.0))
        val fakeData = ResponseList().apply { add(item1); add(item2) }

        `when`(mockRepository.getList()).thenReturn(Resource.success(fakeData))
        viewModel.getList()
        testDispatcher.scheduler.advanceUntilIdle()

        // Act
        viewModel.sortQuotes(SortCriteria.PRICE_LOW_TO_HIGH)

        // Assert
        val result = viewModel.itemsList.value?.data
        assertEquals(2, result?.get(0)?.id) // 5.0 price
        assertEquals(1, result?.get(1)?.id) // 10.0 price
    }

    @Test
    fun `sortQuotes sorts by rating high to low`() = runTest {
        // Arrange
        val item1 = ResponseListItem(id = 1, title = "A", description = "", category = "", image = "", price = 10.0, rating = Rating(1, 4.0))
        val item2 = ResponseListItem(id = 2, title = "B", description = "", category = "", image = "", price = 5.0, rating = Rating(1, 5.0))
        val fakeData = ResponseList().apply { add(item1); add(item2) }

        `when`(mockRepository.getList()).thenReturn(Resource.success(fakeData))
        viewModel.getList()
        testDispatcher.scheduler.advanceUntilIdle()

        // Act
        viewModel.sortQuotes(SortCriteria.RATING_HIGH_TO_LOW)

        // Assert
        val result = viewModel.itemsList.value?.data
        assertEquals(5.0, result?.get(0)?.rating?.rate)
        assertEquals(4.0, result?.get(1)?.rating?.rate)
    }

    @Test
    fun `deleteQuote removes item from both itemsList and original cache`() = runTest {
        // Arrange
        val item1 = ResponseListItem(id = 1, title = "Apple", description = "", category = "", image = "", price = 1.0, rating = Rating(1, 1.0))
        val fakeData = ResponseList().apply { add(item1) }

        `when`(mockRepository.getList()).thenReturn(Resource.success(fakeData))
        viewModel.getList()
        testDispatcher.scheduler.advanceUntilIdle()

        // Act: Delete the item
        viewModel.deleteQuote(1)

        // Assert: It's gone from itemsList
        assertEquals(0, viewModel.itemsList.value?.data?.size)

        // Act: Clear search (which uses originalList) to verify it's gone from cache too
        viewModel.searchQuote("")
        
        // Assert: Still empty
        assertEquals(0, viewModel.itemsList.value?.data?.size)
    }
}
