package cz.beesli.cleverlancetest.data

import com.google.common.hash.Hashing
import cz.beesli.cleverlancetest.R
import cz.beesli.cleverlancetest.data.model.ImageEntity
import cz.beesli.cleverlancetest.network.CleverlanceServiceApi
import cz.beesli.cleverlancetest.ui.login.LoginResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import org.koin.core.KoinComponent

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository(private val dataSource: CleverlanceServiceApi) : KoinComponent {

    // in-memory cache of the Result object
    private var user: LoginResult?

    private val isLoggedIn: Boolean
        get() = user != null

    init {
        user = null
    }

    suspend fun login(username: String, password: String): LoginResult {
        // handle login
        if (isLoggedIn) {
            return user as LoginResult
        }

        val request = dataSource.instance().postUserAsync(hashPasswordToSHA1(password), prepareBody(username))

        return withContext(Dispatchers.IO) {
            try {
                val response = request.await()
                if (response.isSuccessful) {
                    val data = LoginResult(success = response.body() as ImageEntity)
                    setLoggedInUser(data)
                    data
                } else {
                    LoginResult(error = R.string.login_failed)
                }
            } catch (e: Exception) {
                LoginResult(error = R.string.login_failed)
            }
        }
    }

    private fun prepareBody(username: String) : String {
        return "username=$username"
    }

    @Suppress("UnstableApiUsage")
    private fun hashPasswordToSHA1(password: String) : String {
        return Hashing.sha1().hashString(password, java.nio.charset.StandardCharsets.UTF_8).toString()
    }

    private fun setLoggedInUser(data: LoginResult) {
        this.user = data
    }
}
