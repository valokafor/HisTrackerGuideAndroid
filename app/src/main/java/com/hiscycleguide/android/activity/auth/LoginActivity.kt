package com.hiscycleguide.android.activity.auth

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.hiscycleguide.android.R
import com.hiscycleguide.android.activity.MainActivity
import com.hiscycleguide.android.model.UserModel
import com.hiscycleguide.android.provider.FirebaseProvider
import com.hiscycleguide.android.provider.ProgressProvider
import com.hiscycleguide.android.util.getSha1Hex
import com.hiscycleguide.android.util.isValidEmail
import com.hiscycleguide.android.util.isValidPassword

class LoginActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPass: EditText

    private lateinit var progressDialog: ProgressProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        getContent()
    }

    private fun getContent() {
        etEmail = findViewById(R.id.et_login_email)
        etPass = findViewById(R.id.et_login_pass)

        progressDialog = ProgressProvider.newInstance(this)
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

    fun onClickForgetPassword(view: View) {
        MaterialDialog(this).show {
            title(R.string.forgot_password)
            input(
                hint = getString(R.string.hint_enter_email),
                inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            ) { dialog, email ->
                if (email.toString().isValidEmail()) {
                    sendForgotPasswordEmail(email.toString())
                } else {
                    showErrorMessage(getString(R.string.invalidEmail))
                }
            }
            negativeButton(R.string.cancel)
            positiveButton(R.string.submit)
        }
    }

    private fun freshData(user: FirebaseUser) {
        progressDialog.show()
        FirebaseProvider.getUserFirestore().document(user.uid)
            .get()
            .addOnSuccessListener {
                val currentUser = it!!.toObject(UserModel::class.java)
                UserModel.setCurrentUser(currentUser!!)
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
            }
            .addOnFailureListener {
                Snackbar.make(
                    this.findViewById(R.id.ll_content),
                    it.message!!,
                    Snackbar.LENGTH_LONG
                ).show()
                progressDialog.dismiss()
            }
    }

    private fun showErrorMessage(errorMessage: String?) {
        errorMessage?.let { error ->
            MaterialDialog(this).show {
                title(R.string.error)
                message(text = error)
                icon(R.drawable.ic_icon_input_lock)
                negativeButton(R.string.cancel)
            }
        }
    }

    private fun sendForgotPasswordEmail(email: String) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Forgot password email sent", Toast.LENGTH_SHORT).show()
                } else {
                    task.exception?.localizedMessage?.let { message ->
                        showErrorMessage(message)
                    }
                }
            }
    }
}