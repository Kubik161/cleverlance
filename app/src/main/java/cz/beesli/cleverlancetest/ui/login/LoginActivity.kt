package cz.beesli.cleverlancetest.ui.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import cz.beesli.cleverlancetest.util.PhotoUtil
import org.koin.androidx.viewmodel.ext.android.viewModel



class LoginActivity : AppCompatActivity() {

    private val loginViewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(cz.beesli.cleverlancetest.R.layout.activity_login)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        val username = findViewById<EditText>(cz.beesli.cleverlancetest.R.id.username)
        val password = findViewById<EditText>(cz.beesli.cleverlancetest.R.id.password)
        val login = findViewById<Button>(cz.beesli.cleverlancetest.R.id.login)
        val loading = findViewById<ProgressBar>(cz.beesli.cleverlancetest.R.id.loading)
        val image = findViewById<ImageView>(cz.beesli.cleverlancetest.R.id.image)

        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            login.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                username.error = getString(loginState.usernameError)
            }

            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })

        loginViewModel.loggedInUserView.observe(this@LoginActivity, Observer {
            val loggedInUserView = it ?: return@Observer

            if (loggedInUserView.bitmapByteArray != null) {

                login.visibility = View.GONE
                image.visibility = View.VISIBLE

                val displayMetrics = this@LoginActivity.resources.displayMetrics
                image.setImageBitmap(PhotoUtil.decodeImage(loggedInUserView.bitmapByteArray, displayMetrics.widthPixels, displayMetrics.heightPixels))
            }
        })

        loginViewModel.notifyUser.observe(this@LoginActivity, Observer { event ->
            event.getContentIfNotHandled()?.let { loginNotification ->

                loading.visibility = View.GONE

                val resource = if (loginNotification.error != 0) {
                    username.visibility = View.VISIBLE
                    password.visibility = View.VISIBLE
                    login.visibility = View.VISIBLE
                    loginNotification.error
                } else {
                    loginNotification.success
                }

                Snackbar.make(
                    image,
                    resource,
                    Snackbar.LENGTH_LONG
                ).show()
            }
        })

        username.afterTextChanged {
            loginViewModel.loginDataChanged(
                username.text.toString(),
                password.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    username.text.toString(),
                    password.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE -> {
                        loading.visibility = View.VISIBLE
                        username.visibility = View.GONE
                        password.visibility = View.GONE
                        login.visibility = View.GONE

                        loginViewModel.login(
                            username.text.toString().trim(),
                            password.text.toString().trim()
                        )
                    }
                }
                false
            }
        }

        login.setOnClickListener {
            loading.visibility = View.VISIBLE
            username.visibility = View.GONE
            password.visibility = View.GONE
            login.visibility = View.GONE

            loginViewModel.login(
                username.text.toString().trim(),
                password.text.toString().trim()
            )
        }
    }
}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}
