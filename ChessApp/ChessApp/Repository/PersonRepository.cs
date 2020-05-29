using Game.Person;

namespace ChessApp.Repository
{
    internal class PersonRepository
    {
        // TODO: read profiles from some data source

        public static IPerson alice = new Computer();

        public static IPerson bob = new Computer();

        /*
             

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
* 
         */
    }
}