package org.sheridan.ch4_persistinguistate

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_main.*

private const val TAG = "Main Activity"
private const val KEY_CORRECTANS = "CorrectAns"
private const val KEY_ANSWEREDLIST = "AnsweredList"
private  const val KEY_CURRENT_INDEX = "CurrentIndex"

private const val KEY_INDEX = "index"
private const val REQUEST_CODE_CHEAT = 0

private const val IS_CHEATER = "isCheater"

private const val RESULT : String = "RESULT"


class MainActivity : AppCompatActivity() {

    private lateinit var trueButton : Button
    private  lateinit var falseButton : Button

    private lateinit var nextButton: ImageButton
    private lateinit var previousButton: ImageButton
    private lateinit var questionTextView: TextView
    private lateinit var imageView: ImageView
    private lateinit var constraintLayout : ConstraintLayout
    private  lateinit var cheatButton : Button

   /* private val questionBank = listOf(
        Question(R.string.question_australia, true, R.drawable.b1, R.drawable.a1),
        Question(R.string.question_oceans, true, R.drawable.b2, R.drawable.a2),
        Question(R.string.question_mideast, false, R.drawable.b3, R.drawable.a3),
        Question(R.string.question_africa, false, R.drawable.b4, R.drawable.a4),
        Question(R.string.question_americas, true, R.drawable.b6, R.drawable.a6),
        Question(R.string.question_asia, true, R.drawable.b5, R.drawable.a7))

    private var currentIndex = 0*/
    private var correctAns = 0
    private var incorrectAns = 0
    private var percentage : Float = 0.0F
    private var answeredList = ArrayList<Int>()

    private  var result : String = ""

    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProviders.of(this).get(QuizViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*val provider: ViewModelProvider = ViewModelProviders.of(this)
        val quizViewModel = provider.get(QuizViewModel::class.java)
        Log.d(TAG, "Got a QuizViewModel: $quizViewModel")*/

        val currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0
        quizViewModel.currentIndex = currentIndex

        quizViewModel.isCheater = savedInstanceState?.getBoolean(IS_CHEATER,false)?:false


        if(savedInstanceState != null)
        {
            quizViewModel.currentIndex = savedInstanceState.getInt(KEY_CURRENT_INDEX)
            correctAns = savedInstanceState.getInt(KEY_CORRECTANS)
            answeredList = savedInstanceState.getIntegerArrayList(KEY_ANSWEREDLIST) as ArrayList<Int>
            result = savedInstanceState.getString(RESULT).toString()
        }

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)

        nextButton = findViewById(R.id.next_button)
        previousButton = findViewById(R.id.previous_button)
        questionTextView = findViewById(R.id.question_text_View)

        imageView = findViewById(R.id.image_view)
        constraintLayout = findViewById(R.id.view_main)

        cheatButton = findViewById(R.id.cheat_button)



        trueButton.setOnClickListener { view -> view

            checkAnswer(true)
            trueButton.isEnabled = false
            falseButton.isEnabled = false
            answeredList.add(quizViewModel.currentIndex)

            //txtResult.setText(correctAns.toString()+"/"+(quizViewModel.currentIndex+1))
            result = correctAns.toString()+"/"+(quizViewModel.currentIndex+1)
            txtResult.setText(result)
        }

        falseButton.setOnClickListener { view -> view

            checkAnswer(false)
            trueButton.isEnabled = false
            falseButton.isEnabled = false
            answeredList.add(quizViewModel.currentIndex)

            //txtResult.setText(correctAns.toString()+"/"+(quizViewModel.currentIndex+1))
            result = correctAns.toString()+"/"+(quizViewModel.currentIndex+1)
            txtResult.setText(result)
        }

        questionTextView.setOnClickListener {
            //currentIndex = (currentIndex + 1) % questionBank.size
           // quizViewModel.moveToNext()
           // updateQuestion()


            if(quizViewModel.currentIndex == quizViewModel.questionBank.size-1) {
                result = (correctAns.toString()+"/"+(quizViewModel.currentIndex+1))
                val pref = getPreferences(Context.MODE_PRIVATE)
                val getHigh = pref.getInt("Current_Ans",0)
                val editor = pref.edit()

                if(correctAns > getHigh){
                    editor.putInt("Current_Ans",correctAns)
                    editor.commit()
                }
                val intent = Intent(this@MainActivity, ScoreActivity::class.java)
                intent.putExtra("Result",result )
                startActivity(intent)

            }
            else{
                quizViewModel.isCheater=false
                quizViewModel.moveToNext()
                updateQuestion()
            }


        }

        nextButton.setOnClickListener {
           /* currentIndex = (currentIndex + 1) % questionBank.size*/
            //quizViewModel.isCheater = false
            //quizViewModel.moveToNext()
            //updateQuestion()

            if(quizViewModel.currentIndex == quizViewModel.questionBank.size-1) {
                val result = (correctAns.toString()+"/"+(quizViewModel.currentIndex+1)).toString()
                val pref = getPreferences(Context.MODE_PRIVATE)
                val getHigh = pref.getInt("Current_Ans",0)
                val editor = pref.edit()

                if(correctAns > getHigh){
                    editor.putInt("Current_Ans",correctAns)
                    editor.commit()
                }
                val intent = Intent(this@MainActivity, ScoreActivity::class.java)
                intent.putExtra("Result",result )
                startActivity(intent)

            }
            else{
                quizViewModel.isCheater=false
                quizViewModel.moveToNext()
                updateQuestion()
            }
        }


        previousButton.setOnClickListener {
            /*currentIndex = (currentIndex + questionBank.size - 1) % questionBank.size*/
            quizViewModel.isCheater = false
            quizViewModel.moveToPrevious()
            updateQuestion()
        }


        cheatButton.setOnClickListener {

            //val intent = Intent(this, CheatActivity::class.java)
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)
            //startActivity(intent)
            startActivityForResult(intent, REQUEST_CODE_CHEAT)
        }


        updateQuestion()

    }


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



    fun updateQuestion() {
       /* val questionTextResId = questionBank[currentIndex].textResId*/
        val questionTextResId = quizViewModel.currentQuestionText
        questionTextView.setText(questionTextResId)

        val imageTextResId =quizViewModel.currentImage      //questionBank[currentIndex].imgId
        imageView.setBackgroundResource(imageTextResId)

        val backImageId = quizViewModel.currentBackImage  //questionBank[currentIndex].backImgId
        constraintLayout.setBackgroundResource(backImageId)

        if(answeredList.contains(quizViewModel.currentIndex))
        {
            trueButton.isEnabled = false
            falseButton.isEnabled = false
        }

        else
        {
            trueButton.isEnabled = true
            falseButton.isEnabled = true
        }

    }


    private fun checkAnswer(userAnswer: Boolean) {
        /*val correctAnswer = questionBank[currentIndex].answer*/
        val correctAnswer = quizViewModel.currentQuestionAnswer

        /*val messageResId = if (userAnswer == correctAnswer) {
            correctAns = correctAns + 1
            "Correct!"
        } else {
            incorrectAns = incorrectAns + 1
            "Incorrect!"
        }*/

        if (userAnswer == correctAnswer) {
            correctAns += 1
            R.string.correct_toast
        } else {
            incorrectAns += 1
            R.string.incorrect_toast
        }


        val messageResId = when {
            quizViewModel.isCheater -> R.string.judgment_toast
            userAnswer == correctAnswer -> R.string.correct_toast
            else -> R.string.incorrect_toast
        }

        var toast2 = Toast.makeText(
            this,
            messageResId,
            Toast.LENGTH_SHORT
        )
        toast2.setGravity(Gravity.BOTTOM,0,0)
        toast2.show()

        if(quizViewModel.currentIndex == quizViewModel.questionBank.size-1) {
            percentage = (correctAns.toFloat() * 100) / quizViewModel.questionBank.size

            var toast = Toast.makeText(
                this,
                "Percentage Score: %.2f".format(percentage),
                Toast.LENGTH_LONG
            )
            toast.setGravity(Gravity.TOP,0,150)
            toast.show()
        }
    }



    override fun onStart() {
        super.onStart()
        Log.i(TAG, "onStart() was called")
    }

    override fun onStop() {
        super.onStop()
        Log.i(TAG, "onStop() was called")
    }

    override fun onPause() {
        super.onPause()
        Log.i(TAG, "onPause() was called")
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "onResume() was called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy() was called")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_CURRENT_INDEX, quizViewModel.currentIndex)
        outState.putInt(KEY_CORRECTANS, correctAns)
        outState.putIntegerArrayList(KEY_ANSWEREDLIST, answeredList)
        outState.putBoolean(IS_CHEATER, quizViewModel.isCheater)
        outState.putString(RESULT, result)
    }



    //-- end --
}
