package ru.maksimbulva.chess.screens.game

import android.content.res.Resources
import androidx.annotation.StringRes
import ru.maksimbulva.chess.ActionBarPresenter
import ru.maksimbulva.chess.R
import ru.maksimbulva.chess.chess.GameAdjudicationResult
import ru.maksimbulva.chess.core.engine.Player

class GameScreenActionBarWrapper(
    private val resources: Resources,
    private val actionBarPresenterProvider: () -> ActionBarPresenter?
) {
    fun showState(viewState: GameScreenViewModel.ViewState) {
        @StringRes val titleRes = generateActionBarTitle(viewState)
        actionBarPresenterProvider()?.setTitle(titleRes?.let { resources.getString(titleRes) })
    }

    @StringRes
    private fun generateActionBarTitle(viewState: GameScreenViewModel.ViewState): Int? {
        return when (val adjudicationResult = viewState.adjudicationResult) {
            is GameAdjudicationResult.Win -> {
                when (adjudicationResult.reason) {
                    GameAdjudicationResult.Win.WinReason.Checkmate -> {
                        when (adjudicationResult.winner) {
                            Player.Black -> R.string.action_bar_white_checkmated
                            Player.White -> R.string.action_bar_black_checkmated
                        }
                    }
                }
            }
            is GameAdjudicationResult.Draw -> {
                when (adjudicationResult.drawReason) {
                    GameAdjudicationResult.Draw.DrawReason.Stalemate ->
                        R.string.action_bar_stalemate
                    GameAdjudicationResult.Draw.DrawReason.FiftyMovesRule ->
                        R.string.action_bar_fifty_moves_rule
                }
            }
            else -> null
        }
    }
}
