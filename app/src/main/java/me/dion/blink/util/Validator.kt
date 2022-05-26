package me.dion.blink.util

import java.util.regex.Pattern

object Validator {
    private const val EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\." +
            "[a-zA-Z0-9_+&*-]+)*@" +
            "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
            "A-Z]{2,7}$"

    fun isEmail(target: String?): Boolean {
        val pat = Pattern.compile(EMAIL_REGEX)
        return target != null && pat.matcher(target).matches()
    }
}