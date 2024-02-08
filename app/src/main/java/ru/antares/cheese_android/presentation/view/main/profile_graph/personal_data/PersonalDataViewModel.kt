package ru.antares.cheese_android.presentation.view.main.profile_graph.personal_data

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.antares.cheese_android.domain.errors.UIError
import ru.antares.cheese_android.domain.repository.IProfileRepository
import ru.antares.cheese_android.domain.validators.CredentialsTextFieldValidator
import ru.antares.cheese_android.domain.validators.EmailTextFieldValidator
import ru.antares.cheese_android.domain.validators.PhoneTextFieldValidator

class PersonalDataViewModel(
    private val repository: IProfileRepository,
    private val credentialsTextFieldValidator: CredentialsTextFieldValidator,
    private val phoneTextFieldValidator: PhoneTextFieldValidator,
    private val emailTextFieldValidator: EmailTextFieldValidator
) : ViewModel() {
    private val _mutableStateFlow: MutableStateFlow<PersonalDataViewState> =
        MutableStateFlow(PersonalDataViewState.Loading)
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
        when (uiError) {
            is PersonalDataUIError.SomeError -> {
                /* TODO: some handle of error */
            }
        }
    }

    private fun emitState(state: PersonalDataViewState) = viewModelScope.launch {
        _mutableStateFlow.emit(state)
    }

    private fun initialize() = viewModelScope.launch {
        delay(1000)
        repository.get().onSuccess { response ->
            val email = if (response.attachments.isNotEmpty()) {
                response.attachments.firstOrNull {
                    it.typeName == "EMAIL"
                }.takeIf { it != null }?.typeName ?: ""
            } else ""

            val phone = if (response.attachments.isNotEmpty()) {
                response.attachments.firstOrNull {
                    it.typeName == "PHONE"
                }.takeIf { it != null }?.value ?: ""
            } else ""

            emitState(
                PersonalDataViewState.Success(
                    surname = response.surname,
                    name = response.firstname,
                    patronymic = response.patronymic,
                    birthday = response.birthday,
                    email = email,
                    phone = phone.substring(2, phone.length)
                )
            )
        }.onFailure { errorMessage ->
            Log.d("INITIALIZE_PERSONAL_DATA", errorMessage)
            emitState(PersonalDataViewState.Error(PersonalDataUIError.SomeError("Произошла ошибка")))
        }
    }

    private fun onSurnameChange(value: String) {
        val currentState = _mutableStateFlow.value as PersonalDataViewState.Success
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
        val currentState = _mutableStateFlow.value as PersonalDataViewState.Success
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
        val currentState = _mutableStateFlow.value as PersonalDataViewState.Success
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
        val currentState = _mutableStateFlow.value as PersonalDataViewState.Success
        emitState(currentState.copy(birthday = value))
    }

    private fun onEmailChange(value: String) {
        val currentState = _mutableStateFlow.value as PersonalDataViewState.Success
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
        val currentState = _mutableStateFlow.value as PersonalDataViewState.Success
        val phoneValidationResult = phoneTextFieldValidator.validate(value)
        emitState(
            currentState.copy(
                phone = value, validation = currentState.validation.copy(
                    phoneValidationResult = phoneValidationResult
                )
            )
        )
    }

    private fun confirm() {
        // TODO: save new data
    }
}