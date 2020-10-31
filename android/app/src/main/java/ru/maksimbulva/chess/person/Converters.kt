package ru.maksimbulva.chess.person

import android.content.res.Resources
import ru.maksimbulva.ui.person.PersonCardItem

fun convertToPersonCardItem(resources: Resources, person: Person): PersonCardItem {
    return PersonCardItem(
        personId = if (person is Person.Computer) person.id else null,
        portrait = person.portrait,
        personName = resources.getString(person.nameResId)
    )
}
