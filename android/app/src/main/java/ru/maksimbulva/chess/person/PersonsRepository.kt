package ru.maksimbulva.chess.person

import ru.maksimbulva.chess.R

class PersonsRepository {
    // TODO: read profiles from some data source

    val alice = Person.Computer(
        portrait = R.drawable.portrait_001,
        nameResId = R.string.person_alice_name,
        evaluationsLimit = 1_000_000,
        degreeOfRandomness = 32
    )

    val bob = Person.Computer(
        portrait = R.drawable.portrait_001,
        nameResId = R.string.person_bob_name,
        evaluationsLimit = 300_000,
        degreeOfRandomness = 64
    )

    fun getDefaultPerson(): Person {
        return Person.Human(R.drawable.portrait_001, R.string.person_human_name)
    }
}
