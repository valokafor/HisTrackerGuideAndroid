package com.hiscycleguide.android.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.View.OnTouchListener
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.android.material.snackbar.Snackbar
import com.hiscycleguide.android.R
import com.hiscycleguide.android.calendar.HTGCalendarPicker
import com.hiscycleguide.android.calendar.OnCalendarListener
import com.hiscycleguide.android.model.UserModel
import com.hiscycleguide.android.provider.FirebaseProvider
import com.hiscycleguide.android.provider.ProgressProvider
import com.hiscycleguide.android.util.toD
import com.hiscycleguide.android.util.toDateYMD
import com.hiscycleguide.android.util.toWDMY
import com.hiscycleguide.android.util.toYMD
import java.util.*


@SuppressLint("ClickableViewAccessibility")
class PeriodActivity : AppCompatActivity() {

    private lateinit var etPeriod: EditText
    private lateinit var tvPeriod: TextView
    private lateinit var cvPeriod: CardView
    private lateinit var cpPeriod: HTGCalendarPicker
    private lateinit var progressDialog: ProgressProvider

    private var selectedDate = Date()

    private val otl = OnTouchListener { _, _ ->
        cvPeriod.visibility = View.VISIBLE
        true
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_period)

        getContent()
    }

    private fun getContent() {
        selectedDate = UserModel.getCurrentUser().mood.toDateYMD()!!

        etPeriod = findViewById(R.id.et_period_mood)
        etPeriod.setOnTouchListener(otl)
        etPeriod.setText(selectedDate.toWDMY())

        tvPeriod = findViewById(R.id.tv_period_date)
        tvPeriod.text = selectedDate.toD()

        cvPeriod = findViewById(R.id.cv_period)
        cpPeriod = findViewById(R.id.cp_period)
        cpPeriod.setActionDate(selectedDate)

        progressDialog = ProgressProvider.newInstance(this)

        setEvent()
    }

    private fun setEvent() {
        cpPeriod.setOnCalendarListener(object : OnCalendarListener {
            override fun onPicker(date: Date) {
                selectedDate = date
                etPeriod.setText(selectedDate.toWDMY())
                cvPeriod.visibility = View.GONE
                tvPeriod.text = selectedDate.toD()
            }
        })
    }

    fun onClickBack(view: View) {
        onBackPressed()
    }

    override fun onBackPressed() {
        if (cvPeriod.visibility == View.VISIBLE) {
            cvPeriod.visibility = View.GONE
        } else {
            super.onBackPressed()
        }
    }

    fun onClickSave(view: View) {
        val currentUser = UserModel.getCurrentUser()
        currentUser.mood = selectedDate.toYMD()

        progressDialog.show()
        FirebaseProvider.getUserReference().child(currentUser.userId).setValue(currentUser)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    UserModel.setCurrentUser(currentUser)
                    onBackPressed()
                } else {
                    Snackbar.make(
                        this.findViewById(R.id.ll_content),
                        task.exception.toString(),
                        Snackbar.LENGTH_LONG
                    ).show()
                }
                progressDialog.dismiss()
            }
    }

}