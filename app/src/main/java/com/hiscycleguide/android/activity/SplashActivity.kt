package com.hiscycleguide.android.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.util.*
import android.content.Intent
import com.google.android.material.snackbar.Snackbar
import com.hiscycleguide.android.R
import com.hiscycleguide.android.activity.auth.CompleteProfileActivity
import com.hiscycleguide.android.activity.auth.LoginActivity
import com.hiscycleguide.android.model.UserModel
import com.hiscycleguide.android.provider.FirebaseProvider
import com.hiscycleguide.android.provider.PreferenceProvider


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }

    fun goNextScreen() {
        val isFirst = PreferenceProvider.getFirstRunning()
        if (isFirst) {
            startActivity(Intent(this, LandingActivity::class.java))
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    public override fun onStart() {
        super.onStart()

        val currentUser = FirebaseProvider.getAuth().currentUser
        if (currentUser != null && currentUser.isEmailVerified) {
            FirebaseProvider.getUserReference().child(currentUser.uid).get()
                .addOnSuccessListener {
                    val userModel = it!!.getValue(UserModel::class.java)
                    UserModel.setCurrentUser(userModel!!)
                    if (userModel.wifename.isNotEmpty()) {
                        startActivity(Intent(this, MainActivity::class.java))
                        overridePendingTransition(
                            R.anim.slide_in_right,
                            R.anim.slide_out_left
                        )
                    } else {
                        val intent = Intent(this, CompleteProfileActivity::class.java)
                        intent.putExtra("uid", userModel.userId)
                        intent.putExtra("email", userModel.email)
                        intent.putExtra("name", userModel.name)
                        startActivity(intent)
                        overridePendingTransition(
                            R.anim.slide_in_right,
                            R.anim.slide_out_left
                        )
                    }
                }.addOnFailureListener {
                    Snackbar.make(
                        this.findViewById(R.id.ll_content),
                        it.message!!,
                        Snackbar.LENGTH_LONG
                    ).show()
                    Timer().schedule(object : TimerTask() {
                        override fun run() {
                            goNextScreen()
                        }
                    }, 2000)
                }
        } else {
            Timer().schedule(object : TimerTask() {
                override fun run() {
                    goNextScreen()
                }
            }, 3000)
        }
    }
}