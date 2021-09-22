package ru.brauer.nearspace.presentation.util

import java.text.SimpleDateFormat
import java.util.*

fun Long.toFormate(pattern: String): String =
    SimpleDateFormat(pattern, Locale.US).format(Date(this))
