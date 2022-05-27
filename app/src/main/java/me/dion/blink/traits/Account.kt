package me.dion.blink.traits

data class Account(
    val id: Int,
    val username: String,
    val email: String,
    val emailVerified: Boolean,
    val subscription: Boolean,
    val discordLinked: Boolean,
    val discordOauth: String,
    val registerDate: String
    )