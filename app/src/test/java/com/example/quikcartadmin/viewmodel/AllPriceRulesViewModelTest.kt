package com.example.quikcartadmin.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.quikcartadmin.MainDispatcherRule
import com.example.quikcartadmin.helpers.UiState
import com.example.quikcartadmin.model.repository.FakeAdminRepository
import com.example.quikcartadmin.models.entities.coupons.PriceRule
import com.example.quikcartadmin.ui.coupons.allrulesprice.viewmodel.AllPriceRulesViewModel
import com.example.quikcartadmin.ui.home.viewmodel.HomeViewModel
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
@HiltAndroidTest
@Config(manifest = Config.NONE)
class AllPriceRulesViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var fakeRepository: FakeAdminRepository

    private lateinit var viewModel: AllPriceRulesViewModel

    @Before
    fun setUp() {
        fakeRepository = FakeAdminRepository()
        viewModel = AllPriceRulesViewModel(fakeRepository)
    }


    @Test
    fun testGetPriceRules_success() = runBlockingTest {
        // Given
        val fakePriceRules = listOf(
            PriceRule(id = 1, title = "Rule 1"),
            PriceRule(id = 2, title = "Rule 2")
        )
        // When
        viewModel.getPriceRules()

        // Then
        val state = viewModel.ruleState.first()
        assertTrue(state is UiState.Success)
        assertEquals(fakePriceRules.get(0), (state as UiState.Success).data.get(0))
    }

    @Test
    fun testDeletePriceRule_success() = runBlockingTest {
        // Given
        val ruleId = 1L
        val fakeResponse = "Rule deleted successfully"

        // When
        viewModel.deletePriceRule(ruleId)

        // Then
        val state = viewModel.deleteRuleState.value
        assertEquals(UiState.Success(fakeResponse), state)
    }


}