package com.adoyo.howtosayno.domain.usecase

import com.adoyo.howtosayno.data.repository.NoReasonRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.io.IOException

class GetNoReasonUseCaseTest {
    
    @Mock
    private lateinit var repository: NoReasonRepository
    private lateinit var useCase: GetNoReasonUseCase
    
    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        useCase = GetNoReasonUseCase(repository)
    }
    
    @Test
    fun `invoke should emit cached reason first then API result when cache exists`() = runTest {
        // Given
        val cachedReason = "Cached reason"
        val apiReason = "Fresh API reason"
        whenever(repository.getCachedReason()).thenReturn(cachedReason)
        whenever(repository.fetchNoReason()).thenReturn(flowOf(Result.success(apiReason)))
        
        // When
        val results = useCase().toList()
        
        // Then
        assertEquals(2, results.size)
        assertTrue(results[0].isSuccess)
        assertEquals(cachedReason, results[0].getOrNull())
        assertTrue(results[1].isSuccess)
        assertEquals(apiReason, results[1].getOrNull())
    }
    
    @Test
    fun `invoke should emit only API result when no cache exists and API succeeds`() = runTest {
        // Given
        val apiReason = "Fresh API reason"
        whenever(repository.getCachedReason()).thenReturn(null)
        whenever(repository.fetchNoReason()).thenReturn(flowOf(Result.success(apiReason)))
        
        // When
        val results = useCase().toList()
        
        // Then
        assertEquals(1, results.size)
        assertTrue(results[0].isSuccess)
        assertEquals(apiReason, results[0].getOrNull())
    }
    
    @Test
    fun `invoke should emit error when no cache exists and API fails`() = runTest {
        // Given
        val apiError = IOException("Network error")
        whenever(repository.getCachedReason()).thenReturn(null)
        whenever(repository.fetchNoReason()).thenReturn(flowOf(Result.failure(apiError)))
        
        // When
        val results = useCase().toList()
        
        // Then
        assertEquals(1, results.size)
        assertTrue(results[0].isFailure)
        assertEquals(apiError, results[0].exceptionOrNull())
    }
    
    @Test
    fun `invoke should emit cached reason and keep it when API fails but cache exists`() = runTest {
        // Given
        val cachedReason = "Cached reason"
        val apiError = IOException("Network error")
        whenever(repository.getCachedReason()).thenReturn(cachedReason)
        whenever(repository.fetchNoReason()).thenReturn(flowOf(Result.failure(apiError)))
        
        // When
        val results = useCase().toList()
        
        // Then
        assertEquals(2, results.size)
        assertTrue(results[0].isSuccess)
        assertEquals(cachedReason, results[0].getOrNull())
        assertTrue(results[1].isFailure)
        assertEquals(apiError, results[1].exceptionOrNull())
    }
    
    @Test
    fun `getCachedReason should return cached reason when cache exists`() = runTest {
        // Given
        val cachedReason = "Cached reason"
        whenever(repository.getCachedReason()).thenReturn(cachedReason)
        
        // When
        val result = useCase.getCachedReason()
        
        // Then
        assertEquals(cachedReason, result)
    }
    
    @Test
    fun `getCachedReason should return null when no cache exists`() = runTest {
        // Given
        whenever(repository.getCachedReason()).thenReturn(null)
        
        // When
        val result = useCase.getCachedReason()
        
        // Then
        assertEquals(null, result)
    }
    
    @Test
    fun `observeCachedReason should emit success when cached reason exists`() = runTest {
        // Given
        val cachedReason = "Cached reason"
        whenever(repository.observeCachedReason()).thenReturn(flowOf(cachedReason))
        
        // When
        val result = useCase.observeCachedReason().first()
        
        // Then
        assertTrue(result.isSuccess)
        assertEquals(cachedReason, result.getOrNull())
    }
    
    @Test
    fun `observeCachedReason should emit failure when no cached reason exists`() = runTest {
        // Given
        whenever(repository.observeCachedReason()).thenReturn(flowOf(null))
        
        // When
        val result = useCase.observeCachedReason().first()
        
        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is NoReasonCachedException)
    }
    
    @Test
    fun `hasCachedReason should return repository flow`() = runTest {
        // Given
        whenever(repository.observeHasCachedReason()).thenReturn(flowOf(true))
        
        // When
        val result = useCase.hasCachedReason().first()
        
        // Then
        assertTrue(result)
    }
    
    @Test
    fun `clearCache should call repository clearCache`() = runTest {
        // When
        useCase.clearCache()
        
        // Then
        verify(repository).clearCache()
    }
}