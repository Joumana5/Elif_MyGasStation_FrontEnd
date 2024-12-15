package com.elife.mygasstationproject.Service

import com.elife.mygasstationproject.DTO.Login.ApiResponseDto
import com.elife.mygasstationproject.DTO.Login.LoginDto
import com.elife.mygasstationproject.DTO.Login.LoginResponseDto
import com.elife.mygasstationproject.DTO.Login.SignupDto
import com.elife.mygasstationproject.DTO.Login.ResponseDto
import com.elife.mygasstationproject.DTO.Login.VerifyEmailDto
import com.elife.mygasstationproject.DTO.Login.VerifyEmployeeDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("/auth/login")
    fun login(@Body loginDto: LoginDto): Call<LoginResponseDto>

    @POST("/auth/register")
    fun signup(@Body signupDto: SignupDto): Call<ResponseDto>

    @POST("/auth/verify-email")
    fun verifyEmail(@Body verifyEmailDto: VerifyEmailDto): Call<ResponseDto>
}