package org.sheridan.geoquizch6

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

// Creating TAG varaiables for saveInstanceState
private const val TAG = "MainActivity"
private const val KEY_INDEX = "index"
private const val KEY_CORRECT = "correct"
private const val KEY_ANSWERED = "answered"
private const val REQUEST_CODE_CHEAT = 0
private const val EXTRA_ANSWER_SHOWN1 = "answer_is_shown"
private const val IS_CHEATER = "isCheater"


class MainActivity : AppCompatActivity() {

    override fun onActivityResult(requestCode: Int,
                                  resultCode: Int,
                                  data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK) {
            return
        }

        if (requestCode == REQUEST_CODE_CHEAT) {
            quizViewModel.isCheater =
                data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
        }
    }

    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProviders.of(this).get(QuizViewModel::class.java)
    }

    private lateinit  var quizImageView : ImageView
    //Creating variables for Button True, False and Next
    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    //    private lateinit var nextButton: Button
    //    private lateinit var prevButton: Button
    private lateinit var nextButton: ImageButton
    private lateinit var prevButton: ImageButton
    private lateinit var questionTextView: TextView
    private lateinit var cheatButton: Button
    //Making List for Questions to include in Questions Class
//    private val questionBank = listOf(
//        Question(R.string.question_australia, true, R.drawable.image1),
//        Question(R.string.question_oceans, true, R.drawable.image2),
//        Question(R.string.question_mideast, false, R.drawable.image3),
//        Question(R.string.question_africa, false, R.drawable.image4),
//        Question(R.string.question_americas, true, R.drawable.image5),
//        Question(R.string.question_asia, true, R.drawable.image6))
//
//
//    private var currentIndex = 0
    private var correctAnswers = 0
    private var incorrectAnswers = 0
    private var answeredQuestions = ArrayList<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0
        quizViewModel.currentIndex = currentIndex

        quizViewModel.isCheater = savedInstanceState?.getBoolean(IS_CHEATER,false)?:false

        if(savedInstanceState != null) {
            //quizViewModel.currentIndex = savedInstanceState.getInt(KEY_INDEX, 0)
            correctAnswers = savedInstanceState.getInt(KEY_CORRECT, 0)
            answeredQuestions = savedInstanceState.getIntegerArrayList(KEY_ANSWERED) as ArrayList<Int>
        }
        Log.d(TAG, "Current question index: ${quizViewModel.currentIndex}")

//        try {
//            val question = questionBank[currentIndex]
//        } catch (ex: ArrayIndexOutOfBoundsException) {
//            // Log a message at ERROR log level, along with an exception stack trace
//            Log.e(TAG, "Index was out of bounds", ex)
//        }

//        val provider: ViewModelProvider = ViewModelProviders.of(this)
//        val quizViewModel = provider.get(QuizViewModel::class.java)
//        Log.d(TAG, "Got a QuizViewModel: $quizViewModel")

        setContentView(R.layout.activity_main)

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        prevButton = findViewById(R.id.prev_button)
        questionTextView = findViewById(R.id.question_text_view)
        quizImageView = findViewById(R.id.imageView)
        cheatButton = findViewById(R.id.cheat_button)


        trueButton.setOnClickListener { view: View ->
            // Do something in response to the click here
//            Toast.makeText(
//////                this,
//////                R.string.correct_toast,
//////                Toast.LENGTH_SHORT)
//////                .show()
            falseButton.isEnabled = false
            trueButton.isEnabled = false
            checkAnswer(true)
            answeredQuestions.add(quizViewModel.currentIndex)
        }

        falseButton.setOnClickListener { view: View ->
            // Do something in response to the click here
//            Toast.makeText(
////                this,
////                R.string.incorrect_toast,
////                Toast.LENGTH_SHORT)
////                .show()
            falseButton.isEnabled = false
            trueButton.isEnabled = false
            checkAnswer(false)
            answeredQuestions.add(quizViewModel.currentIndex)

        }

        cheatButton.setOnClickListener {
            // Start CheatActivity
            //val intent = Intent(this, CheatActivity::class.java)
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)
            //startActivity(intent)
            startActivityForResult(intent, REQUEST_CODE_CHEAT)
        }


        nextButton.setOnClickListener {view: View ->
            //currentIndex = (currentIndex + 1) % questionBank.size
            quizViewModel.isCheater=false
            quizViewModel.moveToNext()
            updateQuestion()

        }

//        question_text_view.setOnClickListener {
//            currentIndex = (currentIndex + 1) % questionBank.size
//            updateQuestion()
//        }


        prevButton.setOnClickListener {view: View ->
            //currentIndex = (currentIndex  + questionBank.size - 1 ) % questionBank.size
            quizViewModel.isCheater=false
            quizViewModel.moveToPrev()
            updateQuestion()

        }
        updateQuestion()
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        Log.i(TAG, "onSaveInstanceState")
        savedInstanceState.putInt(KEY_INDEX, quizViewModel.currentIndex)
        savedInstanceState.putInt(KEY_CORRECT, correctAnswers)
        savedInstanceState.putIntegerArrayList(KEY_ANSWERED, answeredQuestions)
        savedInstanceState.putBoolean(IS_CHEATER, quizViewModel.isCheater)
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }


    //Creating Function to update QuestionList in TextView
    private fun updateQuestion() {
        //val questionTextResID = questionBank[currentIndex].textResId
        val questionTextResID = quizViewModel.currentQuestionText
        // val quizImages = questionBank[currentIndex].images
        val quizImages = quizViewModel.currentQuestionImage
        questionTextView.setText(questionTextResID)
        quizImageView.setImageResource(quizImages)
        if(answeredQuestions.contains(quizViewModel.currentIndex)) {
            falseButton.isEnabled = false
            trueButton.isEnabled = false
        } else {
            trueButton.isEnabled = true
            falseButton.isEnabled = true
        }
    }

    private fun checkAnswer(userAnswer: Boolean) {
        //val correctAnswer = questionBank[currentIndex].answer
        val correctAnswer = quizViewModel.currentQuestionAnswer

       if (userAnswer == correctAnswer) {
            correctAnswers += 1
            R.string.correct_toast
        } else {
            incorrectAnswers += 1
            R.string.incorrect_toast
        }

        val messageResId = when {
            quizViewModel.isCheater -> R.string.judgment_toast
            userAnswer == correctAnswer -> R.string.correct_toast
            else -> R.string.incorrect_toast
        }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT)
            .show()

        if( quizViewModel.currentIndex == quizViewModel.questionBank.size-1) {

            val percentage: Double =(correctAnswers.toDouble() / quizViewModel.questionBank.size.toDouble()) * 100
            Toast.makeText(this, getString(R.string.amount_of_correct_answers) + "" + correctAnswers.toString() + "\n" +
                    getString(R.string.percentage_of_correct_answers, percentage), Toast.LENGTH_LONG)
                .show()
        }
    }




}


