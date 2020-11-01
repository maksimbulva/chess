package ru.maksimbulva.chess

import android.app.Application
import androidx.annotation.Keep
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.module
import ru.maksimbulva.chess.chess.ChessEngineService
import ru.maksimbulva.chess.chess.notation.LongAlgebraicNotation
import ru.maksimbulva.chess.person.PersonsRepository
import ru.maksimbulva.chess.settings.UserSettings

@Keep
class ChessApplication : Application() {

    private val personsRepository = PersonsRepository()

    private val appModule = module {
        single { personsRepository }
        single { ChessEngineService(personsRepository.getHuman()) }
        single { UserSettings(LongAlgebraicNotation) }
    }

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            modules(appModule)
        }
    }
}
