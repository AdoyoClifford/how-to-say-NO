# Design Document

## Overview

This design document outlines the implementation of an API integration feature for the "How to Say NO" Android app that consumes the https://naas.isalman.dev/no API. The app will be built using Material 3 Expressive design system with Jetpack Compose, providing users with dynamic reasons for saying no through a modern, engaging interface.

The API returns JSON responses in the format: `{"reason": "This feels like something Future Me would yell at Present Me for agreeing to."}`. The app will fetch these reasons and present them in an expressive, user-friendly interface that follows Google's latest Material 3 Expressive guidelines.

## Architecture

### High-Level Architecture
```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   UI Layer      │    │  Domain Layer   │    │   Data Layer    │
│  (Compose UI)   │◄──►│   (Use Cases)   │◄──►│ (Repository)    │
└─────────────────┘    └─────────────────┘    └─────────────────┘
                                                        │
                                               ┌─────────────────┐
                                               │  External API   │
                                               │ & Local Cache   │
                                               └─────────────────┘
```

### Technology Stack
- **UI Framework**: Jetpack Compose with Material 3 Expressive
- **HTTP Client**: Retrofit with OkHttp
- **JSON Parsing**: Kotlinx Serialization
- **Local Storage**: SharedPreferences for caching
- **Dependency Injection**: Hilt (if needed) or manual DI
- **Coroutines**: For asynchronous operations
- **State Management**: Compose State and ViewModel

## Components and Interfaces

### 1. Data Layer

#### API Service Interface
```kotlin
interface NoReasonApiService {
    @GET("no")
    suspend fun getNoReason(): NoReasonResponse
}

@Serializable
data class NoReasonResponse(
    val reason: String
)
```

#### Repository Interface
```kotlin
interface NoReasonRepository {
    suspend fun fetchNoReason(): Result<String>
    suspend fun getCachedReason(): String?
    suspend fun cacheReason(reason: String)
}
```

#### Repository Implementation
- Handles API calls through Retrofit with 30-second timeout configuration
- Manages local caching with SharedPreferences for offline support
- Implements caching strategy: store most recent reason with timestamp
- Provides comprehensive error handling and retry logic
- Supports offline-first approach: check cache first when network unavailable

### 2. Domain Layer

#### Use Case
```kotlin
class GetNoReasonUseCase(
    private val repository: NoReasonRepository
) {
    suspend operator fun invoke(): Result<String>
}
```

### 3. UI Layer

#### Main Screen Composable
- Material 3 Expressive themed interface
- Large, prominent display area for the reason text
- Expressive button for fetching new reasons
- Loading states with Material 3 progress indicators
- Error handling with Material 3 snackbars

#### ViewModel
```kotlin
class NoReasonViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(NoReasonUiState())
    val uiState: StateFlow<NoReasonUiState> = _uiState.asStateFlow()
    
    fun fetchNewReason()
    fun retry()
    fun loadCachedReason()
}

#### Button State Management
- **Enabled State**: Button is enabled when not loading and ready for user interaction
- **Disabled State**: Button is disabled during API requests to prevent multiple simultaneous calls
- **Loading Feedback**: Visual loading indicator replaces button text during requests
- **Re-enable Logic**: Button automatically re-enables after request completion (success or error)

data class NoReasonUiState(
    val reason: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isOffline: Boolean = false,
    val hasCache: Boolean = false
)
```

## Data Models

### Primary Data Models

#### NoReasonResponse
```kotlin
@Serializable
data class NoReasonResponse(
    val reason: String
)
```

#### UI State Model
```kotlin
data class NoReasonUiState(
    val reason: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isOffline: Boolean = false,
    val hasCache: Boolean = false
)
```

### Caching Strategy
- Store the most recent reason in SharedPreferences
- Keep a timestamp for cache validation
- Implement cache expiration (optional)
- Fallback to cached content when offline

## Error Handling

### Error Types and Responses
1. **Network Errors**: Display "No internet connection" message with retry option
2. **API Errors**: Display generic error message with retry option  
3. **Timeout Errors**: Display "Request timed out, please try again" message with retry option (30-second timeout)
4. **Parse Errors**: Display generic error message with retry option

### Timeout Configuration
- **Request Timeout**: 30 seconds maximum for API requests
- **Connection Timeout**: 15 seconds for initial connection establishment
- **Read Timeout**: 30 seconds for reading response data
- **Automatic Timeout Handling**: Requests exceeding 30 seconds will be automatically cancelled and show timeout error

### Error UI Components
- Material 3 Expressive error states
- Snackbar notifications for transient errors
- Inline error messages for persistent issues
- Retry buttons with appropriate styling

## Testing Strategy

### Unit Tests
- Repository implementation testing
- Use case testing with mocked dependencies
- ViewModel state management testing
- Error handling scenarios

### Integration Tests
- API service integration testing
- Repository with real API calls (using test environment)
- End-to-end user flow testing

### UI Tests
- Compose UI testing for screen interactions
- Loading state verification
- Error state display testing
- Button interaction testing

## Material 3 Expressive Implementation

### Design Principles
- **Expressive Typography**: Use Material 3 Expressive typography scales for reason display
- **Dynamic Color**: Implement Material You dynamic color theming
- **Enhanced Motion**: Smooth transitions and animations for state changes
- **Larger Touch Targets**: Implement larger, more accessible buttons
- **Emotional Engagement**: Use expressive components to create engaging interactions

### Key Components
- **MaterialExpressiveTheme**: Wrap the app in the expressive theme
- **Enhanced Buttons**: Use Material 3 Expressive button styles with larger touch targets
- **Typography**: Implement expressive typography for reason display
- **Loading Indicators**: Use Material 3 progress indicators with smooth animations
- **Cards**: Display reasons in Material 3 cards with appropriate elevation and styling

### Color and Theming
- Implement Material You dynamic color extraction
- Use semantic color tokens for consistent theming
- Support both light and dark themes
- Ensure accessibility compliance with contrast ratios

### Animation and Motion
- Smooth transitions between loading, content, and error states
- Expressive button press animations
- Content fade-in animations for new reasons
- Micro-interactions for enhanced user engagement

## Implementation Considerations

### Performance
- Implement proper coroutine scoping to prevent memory leaks
- Use appropriate caching strategies to minimize API calls
- Optimize Compose recomposition with stable data classes
- Implement proper error boundaries

### Accessibility
- Ensure all interactive elements have proper content descriptions
- Implement semantic roles for screen readers
- Maintain appropriate contrast ratios
- Support dynamic text sizing

### Security
- Use HTTPS for all API communications
- Implement certificate pinning if required
- Validate API responses before processing
- Handle sensitive data appropriately (though none expected in this use case)

### Offline Support
- Cache the most recent reason for offline viewing
- Provide clear offline indicators
- Graceful degradation when network is unavailable
- Sync when connection is restored