package cz.beesli.cleverlancetest.ui.login

import cz.beesli.cleverlancetest.data.model.ImageEntity

/**
 * Authentication result : success (user details) or error message.
 */
data class LoginResult(
    val success: ImageEntity? = null,
    val error: Int? = null
)
