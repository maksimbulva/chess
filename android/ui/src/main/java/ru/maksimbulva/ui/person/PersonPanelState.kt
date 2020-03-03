package ru.maksimbulva.ui.person

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class PersonPanelState(
    @DrawableRes val portraitResId: Int,
    @StringRes val nameResId: Int,
    val evaluation: Double? = null
)
