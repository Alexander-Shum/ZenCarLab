package ru.shum.zencarlabtest.util

import java.util.Calendar

fun isNameValid(name: String): Boolean {
    return name.isNotBlank()
}

fun isBirthDateValid(birthDate: String): Boolean {
    if (birthDate.length != 8) return false

    val dayStr = birthDate.substring(0, 2)
    val monthStr = birthDate.substring(2, 4)
    val yearStr = birthDate.substring(4, 8)

    val day = dayStr.toIntOrNull() ?: return false
    val month = monthStr.toIntOrNull()?.minus(1) ?: return false // Month is 0-based in Calendar
    val year = yearStr.toIntOrNull() ?: return false

    if (day < 1 || day > 31 || month < 0 || month > 11 || year < 1900 || year > Calendar.getInstance().get(
            Calendar.YEAR)) {
        return false
    }

    val calendar = Calendar.getInstance().apply {
        set(year, month, day)
    }

    return calendar.get(Calendar.DAY_OF_MONTH) == day && calendar.get(Calendar.MONTH) == month && calendar.get(
        Calendar.YEAR) == year
}



fun isPasswordValid(password: String): Boolean {
    return password.length >= 6
}