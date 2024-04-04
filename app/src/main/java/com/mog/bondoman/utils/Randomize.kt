package com.mog.bondoman.utils

object Randomize {
    private const val STRING_LENGTH = 10
    private val CHAR_POOL: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    fun string(length: Int = STRING_LENGTH): String {
        return List(length) { CHAR_POOL.random() }.joinToString("")
    }
}