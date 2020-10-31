package ru.maksimbulva.ui.person

import androidx.annotation.DrawableRes

class PersonCardItem(
    val personId: Int?,
    @DrawableRes val portrait: Int,
    val personName: String
)