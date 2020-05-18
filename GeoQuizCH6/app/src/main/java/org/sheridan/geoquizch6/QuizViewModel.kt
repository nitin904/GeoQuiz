package org.sheridan.geoquizch6

import android.util.Log
import androidx.lifecycle.ViewModel

private const val TAG = "QuizViewModel"

class QuizViewModel : ViewModel() {
//    init {
//        Log.d(TAG, "ViewModel instance created")
//    }
//    override fun onCleared() {
//        super.onCleared()
//        Log.d(TAG, "ViewModel instance about to be destroyed")
//    }

    var currentIndex = 0
    var isCheater = false

    val questionBank = listOf(
        Question(R.string.question_australia, true, R.drawable.image1),
        Question(R.string.question_oceans, true, R.drawable.image2),
        Question(R.string.question_mideast, false, R.drawable.image3),
        Question(R.string.question_africa, false, R.drawable.image4),
        Question(R.string.question_americas, true, R.drawable.image5),
        Question(R.string.question_asia, true, R.drawable.image6))

    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer

    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId

    val currentQuestionImage: Int
        get() = questionBank[currentIndex].images

    val currentQuestionAnswered: Boolean
        get() = questionBank[currentIndex].answered

    fun moveToNext() {
        currentIndex = (currentIndex + 1) % questionBank.size
    }

    fun moveToPrev() {
        currentIndex = (currentIndex  + questionBank.size - 1 ) % questionBank.size
    }



}