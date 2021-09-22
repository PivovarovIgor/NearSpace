package ru.brauer.nearspace.presentation.settings

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.StyleRes

private const val FILE_NAME = "app_settings"
private const val KEY_THEME = "key_theme"

class SettingsStorage(private val sharedPreferences: SharedPreferences) {
    constructor(context: Context) : this(
        context.getSharedPreferences(
            FILE_NAME,
            Context.MODE_PRIVATE
        )
    )

    var theme: Int
        set(@StyleRes value) = sharedPreferences.edit()
            .putString(
                KEY_THEME,
                AppTheme.values().find { it.resource == value }?.key ?: AppTheme.DEFAULT.key
            )
            .apply()
        get() = sharedPreferences
            .getString(KEY_THEME, AppTheme.DEFAULT.key)
            .let {
                AppTheme.values().find { appTheme ->
                    appTheme.key == it
                } ?: AppTheme.DEFAULT
            }.resource
}