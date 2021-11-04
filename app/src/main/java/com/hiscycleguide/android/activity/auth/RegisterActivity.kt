package com.hiscycleguide.android.activity.auth

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.hiscycleguide.android.R
import com.hiscycleguide.android.model.UserModel
import com.hiscycleguide.android.provider.FirebaseProvider
import com.hiscycleguide.android.util.getSha1Hex
import com.hiscycleguide.android.util.isValidEmail
import com.hiscycleguide.android.util.isValidPassword


class RegisterActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etRepass: EditText

    private lateinit var progressDialog : ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        getContent()
    }

    private fun getContent() {
        etName = findViewById(R.id.et_register_name)
        etEmail = findViewById(R.id.et_register_email)
        etPassword = findViewById(R.id.et_register_password)
        etRepass = findViewById(R.id.et_register_repass)

        progressDialog = ProgressDialog(this@RegisterActivity)
        progressDialog.setTitle(getString(R.string.progressTitle))
        progressDialog.setMessage(getString(R.string.progressDetail))
    }

    fun onClickLogin(view: View) {
        onBackPressed()
    }

    fun onClickSignup(view: View) {
        val name = etName.text.toString()
        if (name.isEmpty()) {
            Snackbar.make(
                this.findViewById(R.id.ll_content),
                getString(R.string.emptyName),
                Snackbar.LENGTH_LONG
            ).show()
            return
        }
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
        val pass = etPassword.text.toString()
        if (!pass.isValidPassword()) {
            Snackbar.make(
                this.findViewById(R.id.ll_content),
                getString(R.string.invalidPass),
                Snackbar.LENGTH_LONG
            ).show()
            return
        }
        val repass = etRepass.text.toString()
        if (pass != repass) {
            Snackbar.make(
                this.findViewById(R.id.ll_content),
                getString(R.string.notMatchPass),
                Snackbar.LENGTH_LONG
            ).show()
            return
        }

        progressDialog.show()

        val sha1 = pass.getSha1Hex()
        val auth = FirebaseProvider.getAuth()
        auth.createUserWithEmailAndPassword(email, sha1!!)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user!!.sendEmailVerification()
                        .addOnCompleteListener { t ->
                            if (t.isSuccessful) {
                                val appUser = UserModel(user.uid, name, email)
                                FirebaseProvider.getUserReference().child(user.uid).setValue(appUser)
                                    .addOnCompleteListener(this) { ta ->
                                        if (ta.isSuccessful) {
                                            val intent =
                                                Intent(view.context, SpouseActivity::class.java)
                                            intent.putExtra("uid", user.uid)
                                            intent.putExtra("email", appUser.email)
                                            intent.putExtra("name", appUser.name)
                                            startActivity(intent)
                                            overridePendingTransition(
                                                R.anim.slide_in_right,
                                                R.anim.slide_out_left
                                            )
                                            this.finish()
                                        } else {
                                            Snackbar.make(
                                                this.findViewById(R.id.ll_content),
                                                ta.exception!!.message!!,
                                                Snackbar.LENGTH_LONG
                                            ).show()
                                        }
                                        progressDialog.dismiss()
                                    }
                            } else {
                                Snackbar.make(
                                    this.findViewById(R.id.ll_content),
                                    t.exception!!.message!!,
                                    Snackbar.LENGTH_LONG
                                ).show()
                                progressDialog.dismiss()
                            }
                        }
                } else {
                    Snackbar.make(
                        this.findViewById(R.id.ll_content),
                        task.exception!!.message!!,
                        Snackbar.LENGTH_LONG
                    ).show()
                    progressDialog.dismiss()
                }
            }
    }
}