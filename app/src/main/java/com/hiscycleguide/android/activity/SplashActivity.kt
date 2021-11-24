package com.hiscycleguide.android.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.hiscycleguide.android.R
import com.hiscycleguide.android.activity.auth.CompleteProfileActivity
import com.hiscycleguide.android.activity.auth.LoginActivity
import com.hiscycleguide.android.model.UserModel
import com.hiscycleguide.android.provider.FirebaseProvider
import com.hiscycleguide.android.provider.PreferenceProvider
import java.util.*


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }

    fun goNextScreen() {
        FirebaseProvider.getConfigFirestore().document("landing").get()
            .addOnSuccessListener {
                val titles: List<String> = it!!.get("title") as List<String>
                val details: List<String> = it.get("detail") as List<String>
                val status: List<String> = it.get("status") as List<String>
                Log.d("status", status.toString())
                PreferenceProvider.setWifeStatus(status.joinToString(":"))

                val isFirst = PreferenceProvider.getFirstRunning()
                if (isFirst) {
                    val intent = Intent(this, LandingActivity::class.java)
                    intent.putExtra("titles", titles.joinToString(":"))
                    intent.putExtra("details", details.joinToString(":"))
                    startActivity(intent)
                } else {
                    startActivity(Intent(this, LoginActivity::class.java))
                }
            }
            .addOnFailureListener {
                Snackbar.make(
                    this.findViewById(R.id.ll_content),
                    it.message!!,
                    Snackbar.LENGTH_LONG
                ).show()
            }
    }

    public override fun onStart() {
        super.onStart()

        val currentUser = FirebaseProvider.getAuth().currentUser
        if (currentUser != null) {
            FirebaseProvider.getUserFirestore().document(currentUser.uid)
                .get()
                .addOnSuccessListener {
                    val userModel = it!!.toObject(UserModel::class.java)
                    UserModel.setCurrentUser(userModel!!)

                    FirebaseProvider.getConfigFirestore().document("landing").get()
                        .addOnSuccessListener {
                            val status: List<String> = it.get("status") as List<String>
                            Log.d("status", status.toString())
                            PreferenceProvider.setWifeStatus(status.joinToString(":"))

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
                                intent.putExtra("token", userModel.token)
                                startActivity(intent)
                                overridePendingTransition(
                                    R.anim.slide_in_right,
                                    R.anim.slide_out_left
                                )
                            }
                        }
                        .addOnFailureListener {
                            Snackbar.make(
                                this.findViewById(R.id.ll_content),
                                it.message!!,
                                Snackbar.LENGTH_LONG
                            ).show()
                        }
                }
                .addOnFailureListener {
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