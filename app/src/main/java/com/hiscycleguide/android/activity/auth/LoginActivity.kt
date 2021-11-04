package com.hiscycleguide.android.activity.auth

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseUser
import com.hiscycleguide.android.R
import com.hiscycleguide.android.activity.MainActivity
import com.hiscycleguide.android.model.UserModel
import com.hiscycleguide.android.provider.FirebaseProvider
import com.hiscycleguide.android.util.getSha1Hex
import com.hiscycleguide.android.util.isValidEmail
import com.hiscycleguide.android.util.isValidPassword

class LoginActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPass: EditText

    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        getContent()
    }

    private fun getContent() {
        etEmail = findViewById(R.id.et_login_email)
        etPass = findViewById(R.id.et_login_pass)

        progressDialog = ProgressDialog(this@LoginActivity)
        progressDialog.setTitle(getString(R.string.progressTitle))
        progressDialog.setMessage(getString(R.string.progressDetail))
    }

    fun onClickRegister(view: View) {
        startActivity(Intent(this, RegisterActivity::class.java))
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    fun onClickSignIn(view: View) {
        val email = etEmail.text.toString()
        if (email.isEmpty()) {
            Snackbar.make(
                this.findViewById(R.id.ll_content),
                getString(R.string.emptyEmail),
                Snackbar.LENGTH_LONG
            ).show()
            return
        }
        if (!email.isValidEmail()) {
            Snackbar.make(
                this.findViewById(R.id.ll_content),
                getString(R.string.invalidEmail),
                Snackbar.LENGTH_LONG
            ).show()
            return
        }
        val pass = etPass.text.toString()
        if (!pass.isValidPassword()) {
            Snackbar.make(
                this.findViewById(R.id.ll_content),
                getString(R.string.invalidPass),
                Snackbar.LENGTH_LONG
            ).show()
            return
        }

        progressDialog.show()
        val auth = FirebaseProvider.getAuth()
        auth.signInWithEmailAndPassword(email, pass.getSha1Hex()!!)
            .addOnCompleteListener(this) { task ->
                progressDialog.dismiss()
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    freshData(user!!)
                } else {
                    Snackbar.make(
                        this.findViewById(R.id.ll_content),
                        task.exception!!.message!!,
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
    }

    private fun freshData(user: FirebaseUser) {
        progressDialog.show()
        if (!user.isEmailVerified) {
            user.sendEmailVerification().addOnCompleteListener(this) {
                progressDialog.dismiss()
                if (it.isSuccessful) {
                    Snackbar.make(
                        this.findViewById(R.id.ll_content),
                        getString(R.string.notVerifyEmail),
                        Snackbar.LENGTH_LONG
                    ).show()
                } else {
                    Snackbar.make(
                        this.findViewById(R.id.ll_content),
                        it.exception!!.message!!,
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        } else {
            FirebaseProvider.getUserReference().child(user.uid).get()
                .addOnSuccessListener {
                    val currentUser = it!!.getValue(UserModel::class.java)
                    UserModel.setCurrentUser(currentUser!!)

                    Log.e("Current User", currentUser.toString())
                    progressDialog.dismiss()

                    if (currentUser.wifename.isNotEmpty()) {
                        startActivity(Intent(this, MainActivity::class.java))
                        overridePendingTransition(
                            R.anim.slide_in_right,
                            R.anim.slide_out_left
                        )
                    } else {
                        val intent = Intent(this, CompleteProfileActivity::class.java)
                        intent.putExtra("uid", user.uid)
                        intent.putExtra("email", currentUser.email)
                        intent.putExtra("name", currentUser.name)
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
                    progressDialog.dismiss()
                }
        }
    }
}