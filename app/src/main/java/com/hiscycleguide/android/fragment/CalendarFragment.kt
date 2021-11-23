package com.hiscycleguide.android.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.hiscycleguide.android.R
import com.hiscycleguide.android.calendar.HTGCalendarView
import com.hiscycleguide.android.calendar.OnCalendarListener
import com.hiscycleguide.android.model.UserModel
import com.hiscycleguide.android.provider.FirebaseProvider
import com.hiscycleguide.android.provider.PreferenceProvider
import com.hiscycleguide.android.provider.ProgressProvider
import com.hiscycleguide.android.util.*
import java.util.*


class CalendarFragment : Fragment() {

    private lateinit var cvBody: HTGCalendarView
    private lateinit var tvDay: TextView
    private lateinit var tvMonthYear: TextView
    private lateinit var tvToday: TextView
    private lateinit var tvStatus: TextView
    private lateinit var ivEmoji: ImageView
    private lateinit var llPrediction: LinearLayout
    private lateinit var llSubmit: LinearLayout
    private lateinit var spStatus: Spinner
    private lateinit var llContent: LinearLayout
    private lateinit var progressDialog: ProgressProvider

    private var listener: OnCalendarFragmentListener? = null

    private var selectedDate = Date()
    private var chooseDate = Date()
    private var selectedMood: String = ""
    private var items: List<String> = arrayListOf()

    companion object {
        fun newInstance(): CalendarFragment {
            return CalendarFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_calendar, container, false)
        getContent(view)
        return view
    }

    @SuppressLint("UseRequireInsteadOfGet")
    private fun getContent(view: View) {
        cvBody = view.findViewById(R.id.cv_calendar)
        tvDay = view.findViewById(R.id.tv_calendar_day)
        tvMonthYear = view.findViewById(R.id.tv_calendar_monthyear)
        tvToday = view.findViewById(R.id.tv_calendar_today)
        tvStatus = view.findViewById(R.id.tv_calendar_status)
        ivEmoji = view.findViewById(R.id.iv_calendar_emoji)
        llPrediction = view.findViewById(R.id.ll_calendar_prediction)
        llSubmit = view.findViewById(R.id.ll_calendar_submit)
        spStatus = view.findViewById(R.id.sp_calendar_status)
        llContent = view.findViewById(R.id.ll_content)

        progressDialog = ProgressProvider.newInstance(context!!)

        items = PreferenceProvider.getWifeStatus()
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            context!!,
            android.R.layout.simple_spinner_dropdown_item, items
        )
        spStatus.adapter = adapter
        selectedMood = items[0]

        val moodToday = Utils.getMoodType(Date())
        if (moodToday == MoodType.Ovulation) {
            ivEmoji.setImageResource(R.drawable.ic_emoji_happy)
            tvStatus.setText(R.string.your_wife_is_in_ovulation_mood)
        } else if (moodToday == MoodType.Normal) {
            ivEmoji.setImageResource(R.drawable.ic_emoji_good)
            tvStatus.setText(R.string.your_wife_is_in_good_mood)
        } else {
            ivEmoji.setImageResource(R.drawable.ic_emoji_sad)
            tvStatus.setText(R.string.your_wife_is_in_menstruation_mood)
        }

        setDay()
        setEvent()
    }

    @SuppressLint("SetTextI18n")
    private fun setDay() {
        tvDay.text = selectedDate.tod() + "th"
        tvMonthYear.text = selectedDate.toMY()
        tvToday.text = chooseDate.toDM_Y()
    }

    private fun setEvent() {
        cvBody.setOnCalendarListener(object : OnCalendarListener {
            override fun onPicker(date: Date) {
                selectedDate = date
                setDay()
            }
        })
        llPrediction.setOnClickListener {
            if (listener != null) listener!!.onClickPredictions()
        }

        spStatus.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedMood = items[position]
            }

        }
        llSubmit.setOnClickListener {
            val currentUser = UserModel.getCurrentUser()
            val data = hashMapOf<String, HashMap<String, String>>()
            val map = hashMapOf<String, String>()
            map["userId"] = currentUser.userId
            map["name"] = selectedMood
            map["date"] = Date().toYMDHMS()
            data[Date().toYMDHMS()] = map

            progressDialog.show()
            FirebaseProvider.getStatusFirestore().document(currentUser.userId).set(data)
                .addOnCompleteListener {
                    Snackbar.make(
                        llContent,
                        "Successful upload your wife status",
                        Snackbar.LENGTH_LONG
                    ).show()
                    progressDialog.hide()
                }.addOnFailureListener {
                    Snackbar.make(
                        llContent,
                        it.message!!,
                        Snackbar.LENGTH_LONG
                    ).show()
                    progressDialog.hide()
                }
        }
    }

    fun setOnCalendarFragmentListener(listener: OnCalendarFragmentListener) {
        this.listener = listener
    }

    interface OnCalendarFragmentListener {
        fun onClickPredictions()
    }

}