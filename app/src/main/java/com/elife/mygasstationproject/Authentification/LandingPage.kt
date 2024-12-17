package com.elife.mygasstationproject.Authentification

import android.animation.ObjectAnimator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.elife.mygasstationproject.R

class LandingPage : AppCompatActivity() {
    private lateinit var welcomeText: TextView
    private lateinit var buttonContainer: View
    private lateinit var clientButton: Button
    private lateinit var managerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing_page)

        welcomeText = findViewById(R.id.welcome_text)
        buttonContainer = findViewById(R.id.button_container)
        clientButton = findViewById(R.id.client_button)
        managerButton = findViewById(R.id.manager_button)

        // Animation logic
        welcomeText.visibility = View.VISIBLE
        val fadeInAnimator = ObjectAnimator.ofFloat(welcomeText, "alpha", 0f, 1f)
        fadeInAnimator.duration = 2000
        fadeInAnimator.interpolator = AccelerateDecelerateInterpolator()
        fadeInAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: android.animation.Animator) {
                buttonContainer.visibility = View.VISIBLE
                val buttonFadeInAnimator = ObjectAnimator.ofFloat(buttonContainer, "alpha", 0f, 1f)
                buttonFadeInAnimator.duration = 1000
                buttonFadeInAnimator.interpolator = AccelerateDecelerateInterpolator()
                buttonFadeInAnimator.start()
            }
        })

        fadeInAnimator.start()

        clientButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        managerButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // Hover effect for buttons
        setButtonHoverEffect(clientButton)
        setButtonHoverEffect(managerButton)
    }

    private fun setButtonHoverEffect(button: Button) {
        button.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    v.setBackgroundColor(resources.getColor(android.R.color.white))
                    (v as Button).setTextColor(resources.getColor(android.R.color.black))
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    v.setBackgroundColor(resources.getColor(R.color.bleu))
                    (v as Button).setTextColor(resources.getColor(android.R.color.white))
                }
            }
            false
        }
    }
}