package com.hiscycleguide.android.activity.auth

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.hiscycleguide.android.R
import com.hiscycleguide.android.activity.MainActivity
import com.hiscycleguide.android.calendar.HTGCalendarPicker
import com.hiscycleguide.android.calendar.OnCalendarListener
import com.hiscycleguide.android.model.UserModel
import com.hiscycleguide.android.util.toWDMY
import com.hiscycleguide.android.util.toYMD
import java.util.*

@SuppressLint("ClickableViewAccessibility")
class CompleteProfileActivity : AppCompatActivity() {

    private lateinit var etWifeName: EditText
    private lateinit var etSpouse: EditText
    private lateinit var etFrequence: EditText
    private lateinit var cvSpouse: CardView
    private lateinit var cpSpouse: HTGCalendarPicker

    private var userId = ""
    private var email = ""
    private var name = ""
    private var selectedDate = Date()
    private val otl = View.OnTouchListener { _, _ ->
        cvSpouse.visibility = View.VISIBLE
        true
    }

    private lateinit var database: DatabaseReference
    private lateinit var progressDialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complete_profile)

        getContent()
    }

    private fun getContent() {
        userId = intent.getStringExtra("uid")!!
        email = intent.getStringExtra("email")!!
        name = intent.getStringExtra("name")!!

        etWifeName = findViewById(R.id.et_spouse_name)
        etSpouse = findViewById(R.id.et_spouse_mood)
        etSpouse.setOnTouchListener(otl)
        etFrequence = findViewById(R.id.et_spouse_frequence)
        etFrequence.setText(getString(R.string._28))
        cvSpouse = findViewById(R.id.cv_spouse)
        cpSpouse = findViewById(R.id.cp_spouse)

        database = Firebase.database.reference

        progressDialog = ProgressDialog(this@CompleteProfileActivity)
        progressDialog.setTitle(getString(R.string.progressTitle))
        progressDialog.setMessage(getString(R.string.progressDetail))

        setEvent()
    }

    private fun setEvent() {
        cpSpouse.setOnCalendarListener(object : OnCalendarListener {
            override fun onPicker(date: Date) {
                selectedDate = date
                etSpouse.setText(selectedDate.toWDMY())
                cvSpouse.visibility = View.GONE
            }
        })
    }

    fun onClickContinue(view: View) {
        val wifename = etWifeName.text.toString()
        if (wifename.isEmpty()) {
            Snackbar.make(
                this.findViewById(R.id.ll_content),
                getString(R.string.emptySpouseName),
                Snackbar.LENGTH_LONG
            ).show()
            return
        }
        val moodDate = etSpouse.text.toString()
        if (moodDate.isEmpty()) {
            Snackbar.make(
                this.findViewById(R.id.ll_content),
                getString(R.string.emptyMoodDate),
                Snackbar.LENGTH_LONG
            ).show()
            return
        }
        val frequence = etFrequence.text.toString()
        if (frequence.isEmpty()) {
            Snackbar.make(
                this.findViewById(R.id.ll_content),
                getString(R.string.emptyFrequence),
                Snackbar.LENGTH_LONG
            ).show()
            return
        }
        try {
            val iFrequence = Integer.parseInt(frequence)
            if (iFrequence < 1) {
                Snackbar.make(
                    this.findViewById(R.id.ll_content),
                    getString(R.string.invalidFrequence),
                    Snackbar.LENGTH_LONG
                ).show()
                return
            }

            progressDialog.show()
            val appUser = UserModel(userId, name, email, wifename, selectedDate.toYMD(), iFrequence)
            database.child("users").child(userId).setValue(appUser)
                .addOnCompleteListener(this) { ta ->
                    if (ta.isSuccessful) {
                        UserModel.setCurrentUser(appUser)
                        startActivity(Intent(this, MainActivity::class.java))
                        overridePendingTransition(
                            R.anim.slide_in_right,
                            R.anim.slide_out_left
                        )
                        this.finish()
                    } else {
                        Snackbar.make(
                            this.findViewById(R.id.ll_content),
                            ta.exception.toString(),
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                    progressDialog.dismiss()
                }
        } catch (e: Exception) {
            Snackbar.make(
                this.findViewById(R.id.ll_content),
                getString(R.string.invalidFrequence),
                Snackbar.LENGTH_LONG
            ).show()
            return
        }
    }
}