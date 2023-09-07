package org.sopt.official.auth

import org.sopt.official.auth.AuthStateGenerator
import java.util.UUID

internal class DefaultAuthStateGenerator : AuthStateGenerator {
    override fun generate() = UUID.randomUUID().toString()
}