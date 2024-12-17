package com.elife.mygasstationproject

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.ScaleAnimation
import android.widget.TextView
import androidx.fragment.app.Fragment

class ContactFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_contact, container, false)

        // Find the TextViews
        val contactText: TextView = view.findViewById(R.id.contact_text)
        val descriptionText: TextView = view.findViewById(R.id.description_text)
        val linkedinText: TextView = view.findViewById(R.id.linkedin_text)
        val emailText: TextView = view.findViewById(R.id.email_text)

        // Apply letter-by-letter animation
        animateText(contactText, "Contact Fragment")
        animateText(descriptionText, "This application helps you find the nearest gas stations and provides real-time updates on fuel availability.")
        animateText(linkedinText, "LinkedIn: linkedin.com/in/yourprofile")
        animateText(emailText, "Email: contact@mygasstation.com")

        // Apply expanding animation to the circle
        val expandingCircle: View = view.findViewById(R.id.expanding_circle)
        val scaleAnimation = ScaleAnimation(
            0f, 20f, // Start and end values for the X axis scaling
            0f, 20f, // Start and end values for the Y axis scaling
            ScaleAnimation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
            ScaleAnimation.RELATIVE_TO_SELF, 0.5f // Pivot point of Y scaling
        )
        scaleAnimation.duration = 2000 // Animation duration in milliseconds
        scaleAnimation.fillAfter = true // If fillAfter is true, the transformation that this animation performed will persist when it is finished.
        expandingCircle.startAnimation(scaleAnimation)

        return view
    }

    private fun animateText(textView: TextView, text: String) {
        val handler = Handler(Looper.getMainLooper())
        textView.text = ""
        for (i in text.indices) {
            handler.postDelayed({
                textView.append(text[i].toString())
            }, i * 50L) // Adjust the delay as needed
        }
    }
}