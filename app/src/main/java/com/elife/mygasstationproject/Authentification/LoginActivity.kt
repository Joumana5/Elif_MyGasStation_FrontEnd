package com.elife.mygasstationproject.Authentification

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.elife.mygasstationproject.DTO.Login.LoginDto
import com.elife.mygasstationproject.DTO.Login.LoginResponseDto
import com.elife.mygasstationproject.MainActivity
import com.elife.mygasstationproject.R
import com.elife.mygasstationproject.Service.ApiAuthService
import com.elife.mygasstationproject.Utils.RetrofitClient
import com.elife.mygasstationproject.profile.UserProfile
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var editmail: TextInputEditText
    private lateinit var editpwd: TextInputEditText
    private lateinit var loginButton: Button
    private lateinit var signPage: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.Login)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        editmail = findViewById(R.id.mail)
        editpwd = findViewById(R.id.pwd)
        loginButton = findViewById(R.id.login_button)
        signPage = findViewById(R.id.sign)

        val backButton: ImageView = findViewById(R.id.back_button)
        backButton.setOnClickListener {
            val intent = Intent(this, LandingPage::class.java)
            startActivity(intent)
        }
        signPage.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

        loginButton.setOnClickListener {
            val mail = editmail.text.toString()
            val pwd = editpwd.text.toString()
            when {
                mail.isEmpty() -> {
                    Toast.makeText(this@LoginActivity, "Email is required", Toast.LENGTH_SHORT).show()
                }
                pwd.isEmpty() -> {
                    Toast.makeText(this@LoginActivity, "Password is required", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    val loginData = LoginDto(mail, pwd)
                    val apiAuthService: ApiAuthService = RetrofitClient.getClient().create(ApiAuthService::class.java)

                    apiAuthService.login(loginData).enqueue(object : Callback<LoginResponseDto> {
                        override fun onResponse(call: Call<LoginResponseDto>, response: Response<LoginResponseDto>) {
                            if (response.isSuccessful && response.body() != null) {
                                val loginResponse = response.body()!!

                                val token = loginResponse.token
                                val role = loginResponse.role

                                val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
                                val editor = sharedPreferences.edit()

                                editor.putString("token", token)
                                editor.putString("role", role)
                                editor.apply()

                                Toast.makeText(this@LoginActivity, "Login successful", Toast.LENGTH_SHORT).show()

                                startActivity(Intent(this@LoginActivity, MainActivity::class.java))

                            } else {
                                if (response.code() == 400) {
                                    Toast.makeText(this@LoginActivity, "Invalid email or password", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }

                        override fun onFailure(call: Call<LoginResponseDto>, t: Throwable) {
                            Toast.makeText(this@LoginActivity, "Login failed", Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            }
        }

        // Animation logic
        val circleView: View = findViewById(R.id.circle_view)
        val logoContainer: View = findViewById(R.id.logo_container)

        // Animate the circle from the top to the center and expand
        circleView.post {
            val startY = -circleView.height.toFloat()
            val endY = (logoContainer.height / 2 - circleView.height / 2).toFloat()
            val translateAnimator = ObjectAnimator.ofFloat(circleView, "translationY", startY, endY)
            val scaleXAnimator = ObjectAnimator.ofFloat(circleView, "scaleX", -1f, logoContainer.width / circleView.width.toFloat())
            val scaleYAnimator = ObjectAnimator.ofFloat(circleView, "scaleY", -1f, logoContainer.width / circleView.width.toFloat()) // Use width to maintain aspect ratio

            val animatorSet = AnimatorSet()
            animatorSet.playTogether(translateAnimator, scaleXAnimator, scaleYAnimator)
            animatorSet.duration = 3000 // Increase duration for slow motion effect
            animatorSet.interpolator = AccelerateDecelerateInterpolator()
            animatorSet.start()
        }
    }
}