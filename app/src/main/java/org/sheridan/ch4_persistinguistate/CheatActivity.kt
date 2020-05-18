package org.sheridan.ch4_persistinguistate

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders


private const val EXTRA_ANSWER_IS_TRUE =
    "org.sheridan.ch4_persistinguistate.answer_is_true"

const val EXTRA_ANSWER_SHOWN = "org.sheridan.ch4_persistinguistate.answer_shown"

private const val TAG = "CheatActivity"

private const val IS_CHEATER = "isCheater"

class CheatActivity : AppCompatActivity() {

    private var answerIsTrue = false

    private lateinit var answerTextView: TextView
    private lateinit var showAnswerButton: Button

    private val cheatViewModel: CheatViewModle by lazy {
        ViewModelProviders.of(this).get(CheatViewModle::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)

        answerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)


        answerTextView = findViewById(R.id.answer_text_view)
        showAnswerButton = findViewById(R.id.show_answer_button)

        showAnswerButton.setOnClickListener {
            val answerText = when {
                answerIsTrue -> R.string.true_button
                else -> R.string.false_button
            }
            answerTextView.setText(answerText)

            setAnswerShownResult()
            cheatViewModel.answerWasClicked = true
        }

        if (cheatViewModel.answerWasClicked) {
            answerTextView.setText(R.string.true_button)
            setAnswerShownResult()
        }

    }

    companion object {
        fun newIntent(packageContext: Context, answerIsTrue: Boolean): Intent {
            return Intent(packageContext, CheatActivity::class.java).apply {
                putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue)
            }
        }
    }

    private fun setAnswerShownResult(isAnswerShown: Boolean) {
        val data = Intent().apply {
            putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown)
        }
        setResult(Activity.RESULT_OK, data)
    }

    private fun setAnswerShownResult() {
        cheatViewModel.isCheater=true
        val data = Intent().apply {
            putExtra(EXTRA_ANSWER_SHOWN, true)
        }
        setResult(Activity.RESULT_OK, data)
    }

    fun onClose(view : View){
        finish()
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        Log.i(TAG, "onSaveInstanceState")
        savedInstanceState.putBoolean(IS_CHEATER, cheatViewModel.isCheater)
    }
}
