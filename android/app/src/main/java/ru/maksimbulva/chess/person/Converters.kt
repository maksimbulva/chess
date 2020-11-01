package ru.maksimbulva.chess.person

import android.content.res.Resources
import ru.maksimbulva.ui.person.PersonCardItem

fun convertToPersonCardItem(resources: Resources, person: Person): PersonCardItem {
    return PersonCardItem(
        personId = person.id,
        portrait = person.portrait,
        personName = resources.getString(person.nameResId)
    )
}
