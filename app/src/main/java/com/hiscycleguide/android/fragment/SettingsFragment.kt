package com.hiscycleguide.android.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.hiscycleguide.android.R
import com.hiscycleguide.android.activity.NotificationActivity
import com.hiscycleguide.android.activity.PeriodActivity
import com.hiscycleguide.android.activity.auth.LoginActivity
import com.hiscycleguide.android.provider.NotificationProvider

class SettingsFragment : Fragment() {

    private lateinit var cvPeriod: CardView
    private lateinit var cvNotification: CardView
    private lateinit var cvPinLock: CardView
    private lateinit var logoutBotton: LinearLayout

    companion object {
        fun newInstance(): SettingsFragment {
            return SettingsFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        getContent(view)
        return view
    }

    private fun getContent(view: View) {
        cvPeriod = view.findViewById(R.id.cv_profile_period)
        cvNotification = view.findViewById(R.id.cv_profile_notification)
        cvPinLock = view.findViewById(R.id.cv_profile_pinlock)
        logoutBotton = view.findViewById(R.id.linear_layout_logout)

        setEvent()
    }

    @SuppressLint("UseRequireInsteadOfGet")
    private fun setEvent() {
        cvPeriod.setOnClickListener {
            startActivity(Intent(context, PeriodActivity::class.java))
            activity!!.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
        cvNotification.setOnClickListener {
            startActivity(Intent(context, NotificationActivity::class.java))
            activity!!.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
        cvPinLock.setOnClickListener {
//            NotificationProvider(activity!!.application).showNotification("Test Notification")
            NotificationProvider.createNotificationChannel(context!!,
                NotificationManagerCompat.IMPORTANCE_DEFAULT, false,
                getString(R.string.app_name), "App notification channel.")
        }

        logoutBotton.setOnClickListener {
            onLogOutButtonClicked()
        }
    }

    private fun onLogOutButtonClicked() {
        FirebaseAuth.getInstance().currentUser?.let {
            AuthUI.getInstance()
                .signOut(requireContext())
                .addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        startActivity(Intent(requireActivity(), LoginActivity::class.java))
                    } else {
                        Toast.makeText(requireContext(), "Unable to logout", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

}