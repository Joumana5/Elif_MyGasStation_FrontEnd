package com.elife.mygasstationproject.DTO.Login

data class LoginResponseDto(
    var token: String? = null,
    var accountId: Int = 0,
    var responseData: String? = null,
    var role: String? = null
)