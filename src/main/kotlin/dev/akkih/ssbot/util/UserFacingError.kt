package dev.akkih.ssbot.util

class UserFacingError(message: String) : Exception(message)

fun userError(message: String): Nothing = throw UserFacingError(message)