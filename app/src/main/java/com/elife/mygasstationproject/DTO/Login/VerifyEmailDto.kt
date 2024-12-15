package com.elife.mygasstationproject.DTO.Login

data class VerifyEmailDto(
    val email: String,
    val verificationCode: String
)
