package org.sheridan.geoquizch6
import androidx.annotation.StringRes

data class Question(@StringRes val textResId: Int, val answer: Boolean, val images: Int, var answered: Boolean = false)