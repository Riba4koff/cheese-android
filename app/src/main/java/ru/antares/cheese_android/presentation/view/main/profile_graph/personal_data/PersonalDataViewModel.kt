package ru.antares.cheese_android.presentation.view.main.profile_graph.personal_data

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.antares.cheese_android.data.remote.services.main.profile.request.UpdateProfileRequest
import ru.antares.cheese_android.data.remote.services.main.profile.response.Attachment
import ru.antares.cheese_android.domain.errors.UIError
import ru.antares.cheese_android.domain.repository.IProfileRepository
import ru.antares.cheese_android.domain.usecases.personal_data.GetUserFromDSInfoUseCase
import ru.antares.cheese_android.domain.validators.CredentialsTextFieldValidator
import ru.antares.cheese_android.domain.validators.EmailTextFieldValidator
import ru.antares.cheese_android.domain.validators.PhoneTextFieldValidator

class PersonalDataViewModel(
    private val credentialsTextFieldValidator: CredentialsTextFieldValidator,
    private val phoneTextFieldValidator: PhoneTextFieldValidator,
    private val emailTextFieldValidator: EmailTextFieldValidator,
    private val repository: IProfileRepository,
    private val getUserUseCase: GetUserFromDSInfoUseCase
) : ViewModel() {
    private val _mutableStateFlow: MutableStateFlow<PersonalDataViewState> =
        MutableStateFlow(PersonalDataViewState.Loading())
    val state: StateFlow<PersonalDataViewState> =
        _mutableStateFlow.asStateFlow()

    init {
        initialize()
    }

    fun onEvent(event: PersonalDataEvent) = viewModelScope.launch {
        when (event) {
            is PersonalDataEvent.OnSurnameChange -> onSurnameChange(event.surname)
            is PersonalDataEvent.OnNameChange -> onNameChange(event.name)
            is PersonalDataEvent.OnPatronymicChange -> onPatronymicChange(event.patronymic)
            is PersonalDataEvent.OnBirthdayChange -> onBirthdayChange(event.birthday)
            is PersonalDataEvent.OnPhoneChange -> onPhoneChange(event.phone)
            is PersonalDataEvent.OnEmailChange -> onEmailChange(event.email)
            PersonalDataEvent.Confirm -> confirm()
        }
    }

    fun onError(uiError: UIError) {
        when (uiError as PersonalDataUIError) {
            is PersonalDataUIError.SomeError -> {
                /* TODO: some handle of error */
            }

            is PersonalDataUIError.UpdateProfile -> {
                /* TODO: handling profile update error */
                initialize()
            }
        }
    }

    private val _navigationEvents: Channel<PersonalDataNavigationEvent> = Channel()
    val navigationEvents: Flow<PersonalDataNavigationEvent> = _navigationEvents.receiveAsFlow()

    private fun emitState(state: PersonalDataViewState) = viewModelScope.launch {
        _mutableStateFlow.tryEmit(state)
    }

    private suspend fun sendNavigationEvent(navigationEvent: PersonalDataNavigationEvent) {
        _navigationEvents.send(navigationEvent)
    }

    private fun initialize() = viewModelScope.launch {
        val user = getUserUseCase.value.first()

        emitState(
            PersonalDataViewState.Success(
                surname = user.surname,
                name = user.name,
                patronymic = user.patronymic,
                birthday = user.birthday,
                email = user.email,
                phone = user.phone.substring(2, user.phone.length),
                verifiedEmail = user.verifiedEmail,
                verifiedPhone = user.verifiedPhone
            )
        )
    }

    private fun onSurnameChange(value: String) {
        val currentState = state.value as PersonalDataViewState.Success
        val surnameCredentialValidationResult = credentialsTextFieldValidator.validate(value)
        emitState(
            currentState.copy(
                surname = value,
                validation = currentState.validation.copy(
                    surnameValidationResult = surnameCredentialValidationResult
                )
            )
        )
    }

    private fun onNameChange(value: String) {
        val currentState = state.value as PersonalDataViewState.Success
        val nameCredentialValidationResult = credentialsTextFieldValidator.validate(value)
        emitState(
            currentState.copy(
                name = value,
                validation = currentState.validation.copy(
                    nameValidationResult = nameCredentialValidationResult
                )
            )
        )
    }

    private fun onPatronymicChange(value: String) {
        val currentState = state.value as PersonalDataViewState.Success
        val patronymicCredentialValidationResult = credentialsTextFieldValidator.validate(value)
        emitState(
            currentState.copy(
                patronymic = value,
                validation = currentState.validation.copy(
                    patronymicValidationResult = patronymicCredentialValidationResult
                )
            )
        )
    }

    private fun onBirthdayChange(value: String) {
        val currentState = state.value as PersonalDataViewState.Success
        emitState(currentState.copy(birthday = value))
    }

    private fun onEmailChange(value: String) {
        val currentState = state.value as PersonalDataViewState.Success
        val emailValidationResult = emailTextFieldValidator.validate(value)
        emitState(
            currentState.copy(
                email = value, validation = currentState.validation.copy(
                    emailValidationResult = emailValidationResult
                )
            )
        )
    }

    private fun onPhoneChange(value: String) {
        val currentState = state.value as PersonalDataViewState.Success
        val phoneValidationResult = phoneTextFieldValidator.validate(value)
        emitState(
            currentState.copy(
                phone = value, validation = currentState.validation.copy(
                    phoneValidationResult = phoneValidationResult
                )
            )
        )
    }

    private fun confirm() = viewModelScope.launch(Dispatchers.IO) {
        val successState = state.value as PersonalDataViewState.Success

        withContext(Dispatchers.Main) { emitState(PersonalDataViewState.Loading()) }

        repository.update(
            request = UpdateProfileRequest(
                attachments = listOf(
                    Attachment.Phone(
                        phone = successState.phone,
                        verified = successState.verifiedPhone
                    ),
                    Attachment.Email(
                        email = successState.email,
                        verified = successState.verifiedEmail
                    )
                ),
                birthday = successState.birthday,
                firstname = successState.name,
                surname = successState.surname,
                patronymic = successState.patronymic
            )
        ).onSuccess { _ ->
            withContext(Dispatchers.Main) {
                sendNavigationEvent(PersonalDataNavigationEvent.PopBackStack)
                emitState(successState)
            }
        }.onFailure { message ->
            withContext(Dispatchers.Main) {
                Log.d("UPDATE_PROFILE_UI_ERROR", message)
                emitState(
                    PersonalDataViewState.Error(
                        error = PersonalDataUIError.UpdateProfile(
                            "Не удалось обновить профиль"
                        )
                    )
                )
            }
        }
    }
}