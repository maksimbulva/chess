package ru.maksimbulva.chess.chess.command

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import ru.maksimbulva.chess.chesslib.ChesslibWrapper
import ru.maksimbulva.chess.core.engine.Engine
import ru.maksimbulva.chess.core.engine.Variation

internal class FindBestVariationCommand(
    private val engine: Engine,
    private val chesslibWrapper: ChesslibWrapper
) : ChessEngineCommand() {

    private var executionDisposable: Disposable? = null

    var bestVariation: Variation? = null
        private set

    override fun execute(onComplete: () -> Unit) {
        executionDisposable = Single.fromCallable {
            chesslibWrapper.findBestVariation(onlyFirstMove = true)
        }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    bestVariation = it
                    onComplete()
                },
                {}
            )
    }

    override fun abort() {
        // TODO: Ask chesslibWrapper to stop
        executionDisposable?.dispose()
        super.abort()
    }
}
