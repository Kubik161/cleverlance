package cz.beesli.cleverlancetest.ui.login

import android.util.Base64
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cz.beesli.cleverlancetest.R
import cz.beesli.cleverlancetest.data.LoginRepository
import cz.beesli.cleverlancetest.data.model.LoginNotification
import cz.beesli.cleverlancetest.util.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loggedInUserView = MutableLiveData<LoggedInUserView>()
    val loggedInUserView: LiveData<LoggedInUserView> = _loggedInUserView

    private val _notifyUser = MutableLiveData<Event<LoginNotification>>()
    val notifyUser : LiveData<Event<LoginNotification>> = _notifyUser


    fun login(username: String, password: String) {
        // can be launched in a separate asynchronous job
        CoroutineScope(Dispatchers.IO).launch {

            //fetch response
            val result : LoginResult = loginRepository.login(username, password)

            //decode base64 string into ByteArray
            val byteArray: ByteArray? = if (result.success != null) {
               Base64.decode(result.success.base64data, Base64.DEFAULT)
            } else {
                null
            }

            //show ByteArray in view, respecting view size/dimensions
            _loggedInUserView.postValue(LoggedInUserView(bitmapByteArray = byteArray))

            //prepare one-time event to notify user about login result
            val event = if (result.error != null) {
                Event(LoginNotification(error = result.error, success = 0) )
            } else {
                Event(LoginNotification(error = 0, success = R.string.welcome))
            }
            _notifyUser.postValue(event)
        }
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    private fun isUserNameValid(username: String): Boolean {
        // Simple username validation check
        return username.isNotBlank()
    }

    private fun isPasswordValid(password: String): Boolean {
        // Simple password validation check
        return password.isNotBlank()
    }
}
