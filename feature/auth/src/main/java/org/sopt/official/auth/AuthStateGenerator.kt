package org.sopt.official.auth

interface AuthStateGenerator {
    fun generate(): String
}