package ru.maksimbulva.chess.person

class PersonsRepository {
    // TODO: read profiles from some data source

    val alice = Person.Computer(
        evaluationsLimit = 1_000_000,
        degreeOfRandomness = 32
    )

    val bob = Person.Computer(
        evaluationsLimit = 300_000,
        degreeOfRandomness = 64
    )
}
