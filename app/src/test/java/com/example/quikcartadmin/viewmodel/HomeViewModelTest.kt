package com.example.quikcartadmin.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.quikcartadmin.MainDispatcherRule
import com.example.quikcartadmin.helpers.UiState
import com.example.quikcartadmin.model.repository.FakeAdminRepository
import com.example.quikcartadmin.models.entities.coupons.CouponsCountResponse
import com.example.quikcartadmin.models.entities.inventory.InventoryCountResponse
import com.example.quikcartadmin.models.entities.products.ProductsCountResponse
import com.example.quikcartadmin.ui.home.viewmodel.HomeViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import javax.inject.Inject

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
@HiltAndroidTest
@Config(manifest = Config.NONE)
class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var fakeAdminRepository: FakeAdminRepository

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        fakeAdminRepository = FakeAdminRepository()
        viewModel = HomeViewModel(fakeAdminRepository)
    }

    @Test
    fun testGetCountOfProducts() = runBlockingTest {
        // Given

        // When
        val resultFlow = viewModel.productCount.value
        val result = (resultFlow as UiState.Success).data.count

        // Then
        assertEquals(2, result)
    }

    @Test
    fun testGetCountOfCoupons() = runBlockingTest {
        // Given

        // When
        val resultFlow = viewModel.couponsCount.value
        val result = (resultFlow as UiState.Success).data.count

        // Then
        assertEquals(10, result)
    }

    @Test
    fun testGetCountOfInventory() = runBlockingTest {
        // Given

        // When
        val resultFlow = viewModel.inventoryCount.value
        val result = (resultFlow as UiState.Success).data.count

        // Then
        assertEquals(200, result)
    }
}