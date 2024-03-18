package ru.antares.cheese_android.presentation.view.main.profile_graph.personal_data

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.optics.copy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.antares.cheese_android.data.remote.services.main.profile.request.UpdateProfileRequest
import ru.antares.cheese_android.data.remote.services.main.profile.response.Attachment
import ru.antares.cheese_android.domain.errors.AppError
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
    private val _mutableStateFlow: MutableStateFlow<PersonalDataState> =
        MutableStateFlow(PersonalDataState())
    val state: StateFlow<PersonalDataState> = _mutableStateFlow.asStateFlow()

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

    fun onError(appError: AppError) {
        when (appError as PersonalDataAppError) {
            is PersonalDataAppError.UnknownError -> {
                /* TODO: some handle of error */
            }

            is PersonalDataAppError.UpdateProfile -> {
                /* TODO: handling profile update error */
                initialize()
            }
        }
    }

    private val _navigationEvents: Channel<PersonalDataNavigationEvent> = Channel()
    val navigationEvents: Flow<PersonalDataNavigationEvent> = _navigationEvents.receiveAsFlow()

    private suspend fun sendNavigationEvent(navigationEvent: PersonalDataNavigationEvent) {
        _navigationEvents.send(navigationEvent)
    }

    private fun initialize() = viewModelScope.launch {
        Log.d("PD_VM", "initializing")

        val user = withContext(Dispatchers.IO) { getUserUseCase.value.first() }

        Log.d("USER", user.toString())
        Log.d("USER", user.email.isEmpty().toString())

        _mutableStateFlow.update { state ->
            state.copy {
                PersonalDataState.surname set user.surname
                PersonalDataState.name set user.name
                PersonalDataState.patronymic set user.patronymic
                PersonalDataState.birthday set user.birthday
                PersonalDataState.email set user.email
                PersonalDataState.phone set user.phone.substring(2, user.phone.length)
                PersonalDataState.verifiedEmail set user.verifiedEmail
                PersonalDataState.verifiedPhone set user.verifiedPhone
                PersonalDataState.uiState set PersonalDataLoadingState.SUCCESS
                PersonalDataState.enabledBirthday set user.birthday.isEmpty()
            }
        }
    }

    private fun onSurnameChange(value: String) {
        _mutableStateFlow.update { state ->
            state.copy {
                PersonalDataState.surname set value
                PersonalDataState.validation set state.validation.copy {
                    TextFieldValidations.surnameValidationResult set
                            credentialsTextFieldValidator.validate(value)
                }
            }
        }
    }

    private fun onNameChange(value: String) {
        _mutableStateFlow.update { state ->
            state.copy {
                PersonalDataState.name set value
                PersonalDataState.validation set state.validation.copy {
                    TextFieldValidations.nameValidationResult set
                            credentialsTextFieldValidator.validate(value)
                }
            }
        }
    }

    private fun onPatronymicChange(value: String) {
        _mutableStateFlow.update { state ->
            state.copy {
                PersonalDataState.patronymic set value
                PersonalDataState.validation set state.validation.copy {
                    TextFieldValidations.patronymicValidationResult set
                            credentialsTextFieldValidator.validate(value)
                }
            }
        }
    }

    private fun onBirthdayChange(value: String) {
        _mutableStateFlow.update { state ->
            state.copy {
                PersonalDataState.birthday set value
            }
        }
    }

    private fun onEmailChange(value: String) {
        _mutableStateFlow.update { state ->
            state.copy {
                PersonalDataState.email set value
                PersonalDataState.validation set state.validation.copy {
                    TextFieldValidations.emailValidationResult set
                            emailTextFieldValidator.validate(value)
                }
            }
        }
    }

    private fun onPhoneChange(value: String) {
        _mutableStateFlow.update { state ->
            state.copy {
                PersonalDataState.phone set value
                PersonalDataState.validation set state.validation.copy {
                    TextFieldValidations.phoneValidationResult set phoneTextFieldValidator.validate(
                        value
                    )
                }
            }
        }
    }

    private fun confirm() = viewModelScope.launch {
        repository.updateV2(
            request = UpdateProfileRequest(
                attachments = listOf(
                    Attachment.Phone(
                        phone = state.value.phone,
                        verified = state.value.verifiedPhone
                    ),
                    Attachment.Email(
                        email = state.value.email,
                        verified = state.value.verifiedEmail
                    )
                ),
                birthday = state.value.birthday,
                firstname = state.value.name,
                surname = state.value.surname,
                patronymic = state.value.patronymic
            )
        ).collectLatest { resourceState ->
            resourceState.onLoading { isLoading ->
                if (isLoading) _mutableStateFlow.update { state ->
                    state.copy {
                        PersonalDataState.uiState set PersonalDataLoadingState.LOADING
                    }
                }
            }.onError { error ->
                _mutableStateFlow.update { state ->
                    state.copy {
                        PersonalDataState.error set error
                        PersonalDataState.uiState set PersonalDataLoadingState.ERROR
                    }
                }
            }.onSuccess {
                withContext(Dispatchers.Main) {
                    sendNavigationEvent(PersonalDataNavigationEvent.PopBackStack)
                }
            }
        }
    }
}