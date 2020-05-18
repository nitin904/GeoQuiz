package org.sheridan.ch4_persistinguistate

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_score.*


private const val EXTRA_ANSWER_IS_TRUE =
    "org.sheridan.ch4.persistinguistate.answer_is_true"


private const val TAG = "CheatActivity"
private const val IS_CHEATER = "isCheater"

class ScoreActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score)

        val intent = getIntent()
        val result = intent.getStringExtra("Result")
        val pref = getSharedPreferences("MainActivity", Context.MODE_PRIVATE)
        val getCorrectAns = pref.getInt("Current_Ans",0)


        txtCurrentScore.text = result.toString()
        txtHighScore.text = getCorrectAns.toString() +"/6"

        Toast.makeText(this, "Score:"+result, Toast.LENGTH_LONG).show()


    }



    fun onExit(view: View) {
        finish()
    }


    companion object {
        fun newIntent(packageContext: Context, answerIsTrue: Boolean): Intent {
            return Intent(packageContext, ScoreActivity::class.java).apply {
                putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue)
            }
        }
    }


    fun onRefresh(view: View) {
        val mStartActivity = Intent(this@ScoreActivity, MainActivity::class.java)
        val mPendingIntentId = 123456
        val mPendingIntent = PendingIntent.getActivity(
            this@ScoreActivity,
            mPendingIntentId,
            mStartActivity,
            PendingIntent.FLAG_CANCEL_CURRENT
        )

        val mgr = this@ScoreActivity.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() , mPendingIntent)

        System.exit(0)
    }

}
