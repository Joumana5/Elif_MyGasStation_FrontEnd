package com.elife.mygasstationproject.DTO.Login

data class ApiResponseDto<T>(
    var responseData: T? = null,
    var responseMessage: String? = null
)