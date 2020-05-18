package org.sheridan.ch4_persistinguistate

import androidx.annotation.StringRes

data class Question (@StringRes val textResId: Int, val answer: Boolean, val imgId: Int, val backImgId: Int)