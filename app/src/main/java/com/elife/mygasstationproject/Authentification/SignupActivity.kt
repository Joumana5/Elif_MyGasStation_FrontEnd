package com.elife.mygasstationproject.Authentification

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.elife.mygasstationproject.DTO.Login.SignupDto
import com.elife.mygasstationproject.DTO.Login.ResponseDto
import com.elife.mygasstationproject.R
import com.elife.mygasstationproject.Service.ApiAuthService
import com.elife.mygasstationproject.Utils.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupActivity : AppCompatActivity() {
    private lateinit var loginPage: TextView
    private lateinit var signupButton: Button
    private lateinit var username: EditText
    private lateinit var userLastName: EditText
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var age: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signup)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.Signup)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        username = findViewById(R.id.Fn)
        userLastName = findViewById(R.id.Ln)
        email = findViewById(R.id.mail)
        password = findViewById(R.id.pwd)
        age = findViewById(R.id.age)

        loginPage = findViewById(R.id.log)
        signupButton = findViewById(R.id.signup_button)

        val backButton: ImageView = findViewById(R.id.back)

        backButton.setOnClickListener {
            val intent = Intent(this, LandingPage::class.java)
            startActivity(intent)
        }

        loginPage.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        signupButton.setOnClickListener {
            signupButton.isEnabled = false

            if (!verifyFieldsNotEmpty()) {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
                signupButton.isEnabled = true
            } else {
                val signupData = SignupDto(
                    username = username.text.toString(),
                    userLastName = userLastName.text.toString(),
                    email = email.text.toString(),
                    password = password.text.toString(),
                    age = age.text.toString().toInt(),
                    role = "client"
                )

                val apiAuthService: ApiAuthService = RetrofitClient.getClient().create(ApiAuthService::class.java)

                apiAuthService.signup(signupData).enqueue(object : Callback<ResponseDto> {
                    override fun onResponse(call: Call<ResponseDto>, response: Response<ResponseDto>) {
                        signupButton.isEnabled = true
                        if (response.isSuccessful && response.body() != null) {
                            val prefs: SharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
                            val editor: SharedPreferences.Editor = prefs.edit()
                            editor.putString("email", email.text.toString())
                            editor.apply()

                            val intent = Intent(this@SignupActivity, VerificationActivity::class.java)
                            startActivity(intent)

                            Toast.makeText(this@SignupActivity, "signup successful", Toast.LENGTH_SHORT).show()
                        } else {
                            val errorMessage = if (response.code() == 400) "Email is already in use" else "Signup failed"
                            Toast.makeText(this@SignupActivity, errorMessage, Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<ResponseDto>, t: Throwable) {
                        Log.e("Signup-error", t.message.toString())
                    }
                })
            }
        }

        // Animation logic
        val circleView: View = findViewById(R.id.circle_view)
        val logoContainer: View = findViewById(R.id.logo_container)

        // Animate the circle from the right to the center
        circleView.post {
            val startX = logoContainer.width.toFloat()
            val endX = (logoContainer.width / 2 - circleView.width / 2).toFloat()
            val translateAnimator = ObjectAnimator.ofFloat(circleView, "translationX", startX, endX)
            val scaleXAnimator = ObjectAnimator.ofFloat(circleView, "scaleX", 1f, -2f)
            val scaleYAnimator = ObjectAnimator.ofFloat(circleView, "scaleY", 1f, -2f)
            val alphaAnimator = ObjectAnimator.ofFloat(circleView, "alpha", 0f, 1f)

            val animatorSet = AnimatorSet()
            animatorSet.playTogether(translateAnimator, scaleXAnimator, scaleYAnimator, alphaAnimator)
            animatorSet.duration = 3000
            animatorSet.interpolator = AccelerateDecelerateInterpolator()
            animatorSet.start()
        }
    }

    private fun verifyFieldsNotEmpty(): Boolean {
        return username.text.isNotEmpty() &&
                userLastName.text.isNotEmpty() &&
                email.text.isNotEmpty() &&
                password.text.isNotEmpty() &&
                age.text.isNotEmpty()
    }
}