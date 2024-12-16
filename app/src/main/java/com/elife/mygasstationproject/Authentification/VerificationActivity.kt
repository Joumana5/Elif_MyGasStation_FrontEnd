package com.elife.mygasstationproject.Authentification

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.elife.mygasstationproject.DTO.Login.ResponseDto
import com.elife.mygasstationproject.DTO.Login.VerifyEmailDto
import com.elife.mygasstationproject.R
import com.elife.mygasstationproject.Service.ApiAuthService
import com.elife.mygasstationproject.Utils.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class VerificationActivity : AppCompatActivity() {
    private lateinit var codeEditText: EditText
    private lateinit var verifyButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_verification)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        codeEditText = findViewById(R.id.code)
        verifyButton = findViewById(R.id.verify_button)
        verifyButton.setOnClickListener {
            val code = codeEditText.text.toString()
            verifyEmail(code)
        }

    }

    private fun verifyEmail(code: String) {
        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val email = sharedPreferences.getString("email", "none")

        Log.v("signup", "Email to be verified: $email")

        if (email == "none") {
            val intent = Intent(this@VerificationActivity, SignupActivity::class.java)
            startActivity(intent)
        } else {
            val verificationData = VerifyEmailDto(email = email!!, verificationCode = code)

            val apiAuthService = RetrofitClient.getClient().create(ApiAuthService::class.java)

            apiAuthService.verifyEmail(verificationData).enqueue(
                object : Callback<ResponseDto>{
                    override fun onResponse(
                        call: Call<ResponseDto>,
                        response: Response<ResponseDto>
                    ) {
                        try {
                            Log.v("signup", "Code is $code")
                            if (response.isSuccessful && response.body() != null) {
                                Toast.makeText(this@VerificationActivity, "Email verification success", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this@VerificationActivity, LoginActivity::class.java)
                                startActivity(intent)
                            } else {
                                Log.e("Verification", "HTTP Error: ${response.code()} ${response.message()}")
                                val toastMessage = when (response.code()) {
                                    400 -> "Invalid verification code"
                                    else -> "Error verifying your email"
                                }
                                Toast.makeText(this@VerificationActivity, toastMessage, Toast.LENGTH_LONG).show()
                            }
                        } catch (e: Exception) {
                            Log.e("verification", e.message ?: "Unknown error")
                        }

                    }

                    override fun onFailure(call: Call<ResponseDto>, t: Throwable) {
                        if (t is IOException) {
                            Log.e("SignupActivity", "Network error: ${t.message}")
                        } else {
                            Log.e("SignupActivity", "Unexpected error: ${t.message}", t)
                        }

                    }

                }

            )
        }
    }

}