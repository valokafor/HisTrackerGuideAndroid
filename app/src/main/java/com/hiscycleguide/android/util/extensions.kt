package com.hiscycleguide.android.util

import android.R.attr
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import java.text.SimpleDateFormat
import java.util.*
import android.R.attr.password
import java.lang.Exception
import java.lang.StringBuilder
import java.security.MessageDigest
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.experimental.and


fun Date.diffDate(field: Int, amount: Int): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.add(field, amount)
    return calendar.time
}

@SuppressLint("SimpleDateFormat")
fun Date.toMDY(): String {
    return SimpleDateFormat("MM-dd-yyyy").format(this)
}

fun String.isValidEmail(): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String.isValidPassword(): Boolean {
    val pattern: Pattern

    val PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$"

    pattern = Pattern.compile(PASSWORD_PATTERN)
    val matcher: Matcher = pattern.matcher(this)
    return matcher.matches()
}

fun String.getSha1Hex(): String? {
    return try {
        val messageDigest: MessageDigest = MessageDigest.getInstance("SHA-1")
        messageDigest.update(this.toByteArray(charset("UTF-8")))
        val bytes: ByteArray = messageDigest.digest()
        val buffer = StringBuilder()
        for (b in bytes) {
            buffer.append(Integer.toString((b and 0xff.toByte()) + 0x100, 16).substring(1))
        }
        buffer.toString()
    } catch (ignored: Exception) {
        ignored.printStackTrace()
        null
    }
}

@SuppressLint("SimpleDateFormat")
fun Date.toWDMY(): String {
    return SimpleDateFormat("EEEE, dd MMMM, yyyy").format(this)
}

@SuppressLint("SimpleDateFormat")
fun Date.toYMD(): String {
    return SimpleDateFormat("yyyy-MM-dd").format(this)
}

@SuppressLint("SimpleDateFormat")
fun Date.toD(): String {
    return SimpleDateFormat("dd").format(this)
}

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}