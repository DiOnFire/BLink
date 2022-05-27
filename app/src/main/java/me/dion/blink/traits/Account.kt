package me.dion.blink.traits

class Account(
    val id: Int,
    val username: String,
    val email: String,
    val email_verified: Boolean,
    val subscription: Boolean,
    val discord_linked: Boolean,
    val discord_oauth: String,
    val register_date: String
    ) {}