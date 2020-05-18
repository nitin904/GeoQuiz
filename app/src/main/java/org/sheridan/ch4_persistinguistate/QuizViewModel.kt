package org.sheridan.ch4_persistinguistate

import android.util.Log
import androidx.lifecycle.ViewModel

private const val TAG = "QuizViewModel"

class QuizViewModel : ViewModel(){

  /*  init {
        Log.d(TAG, "ViewModel instance created")
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "ViewModel instance about to be destroyed")
    }*/
     var currentIndex = 0
    var isCheater = false

     val questionBank = listOf(
        Question(R.string.question_australia, true, R.drawable.b1, R.drawable.a1),
        Question(R.string.question_oceans, true, R.drawable.b2, R.drawable.a2),
        Question(R.string.question_mideast, false, R.drawable.b3, R.drawable.a3),
        Question(R.string.question_africa, false, R.drawable.b4, R.drawable.a4),
        Question(R.string.question_americas, true, R.drawable.b6, R.drawable.a6),
        Question(R.string.question_asia, true, R.drawable.b5, R.drawable.a7))

    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer

    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId

    fun moveToNext() {
        currentIndex = (currentIndex + 1) % questionBank.size
    }

    fun moveToPrevious(){
        currentIndex = (currentIndex + questionBank.size - 1) % questionBank.size
    }

    val currentImage : Int
        get() = questionBank[currentIndex].imgId

    val currentBackImage : Int
        get() = questionBank[currentIndex].backImgId


}