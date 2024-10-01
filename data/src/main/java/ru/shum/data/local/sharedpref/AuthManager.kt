package ru.shum.data.local.sharedpref

import android.content.Context
import android.content.SharedPreferences

private const val PREFS_NAME = "auth_prefs"

class AuthManager(context: Context) {
    private val preferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_USER_NAME = "user_name"
    }

    fun login(userName: String) {
        preferences.edit()
            .putBoolean(KEY_IS_LOGGED_IN, true)
            .putString(KEY_USER_NAME, userName)
            .apply()
    }

    fun logout() {
        preferences.edit()
            .remove(KEY_IS_LOGGED_IN)
            .remove(KEY_USER_NAME)
            .apply()
    }

    fun isLoggedIn(): Boolean {
        return preferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun getLoggedInUserName(): String? {
        return preferences.getString(KEY_USER_NAME, null)
    }
}