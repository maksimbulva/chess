using ChessEngine;
using ChessEngine.Move;
using System;
using System.Collections.Concurrent;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using static UCIConsoleApp.UciMoveParser;

namespace UCIConsoleApp
{
    internal sealed class UciEngineWrapper
    {
        private readonly BlockingCollection<string> consoleInputQueue;
        private readonly BlockingCollection<string> consoleOutputQueue;

        private readonly IChessEngine chessEngine = ChessEngineFactory.CreateChessEngine();
        private readonly IDecisionMaker decisionMaker = DecisionMakerFactory.CreateDecisionMaker();

        private bool isQuitRequested;

        public UciEngineWrapper(
            BlockingCollection<string> consoleInputQueue,
            BlockingCollection<string> consoleOutputQueue)
        {
            this.consoleInputQueue = consoleInputQueue;
            this.consoleOutputQueue = consoleOutputQueue;
        }

        public void Run()
        {
            isQuitRequested = false;
            while (!isQuitRequested)
            {
                var uciInputCommand = consoleInputQueue.Take();
                ExecuteInputCommand(ParseUciCommand(uciInputCommand));
            }
        }

        private void ExecuteInputCommand(string[] commandTokens)
        {
            if (commandTokens.Length == 0)
            {
                return;
            }

            switch (commandTokens[0].ToLower())
            {
                case "uci":
                    SendUciInitCommands();
                    break;
                case "ucinewgame":
                    chessEngine.ResetGame();
                    break;
                case "isready":
                    consoleOutputQueue.Add("readyok");
                    break;
                case "position":
                    SetPositionFromUciCommand(commandTokens);
                    break;
                case "go":
                    StartThinkingAndPlayMove();
                    break;
                case "quit":
                    isQuitRequested = true;
                    break;
            }
        }

        private void SendUciInitCommands()
        {
            consoleOutputQueue.Add("id name ChesslibCs");
            consoleOutputQueue.Add("id author Maksim Bulva");
            consoleOutputQueue.Add("uciok");
        }

        private void StartThinkingAndPlayMove()
        {
            // TODO: Run thinking on background thread
            var moveToPlay = decisionMaker
                .FindBestMove(chessEngine.CurrentPosition)
                .Result;

            moveToPlay.MatchSome(x => consoleOutputQueue.Add($"bestmove {MoveToUciNotation(x)}"));
        }

        private void SetPositionFromUciCommand(string[] tokens)
        {
            switch (tokens[1].ToLower())
            {
                case "startpos":
                    chessEngine.ResetGame();
                    break;
                case "fen":
                    throw new NotImplementedException();
                default:
                    return;
            }

            PlayMoves(tokens.Skip(2).Where(x => x.ToLower() != "moves"));
        }

        private void PlayMoves(IEnumerable<string> uciEncodedMoves)
        {
            foreach (var uciMove in uciEncodedMoves)
            {
                var parsedMove = ParseUciMove(uciMove);
                bool isMovePlayed = false;
                parsedMove.MatchSome(move =>
                {
                    move.PromoteToPiece.Match(
                        promoteToPiece =>
                        {
                            isMovePlayed = chessEngine.TryPlayMove(
                                move.OriginSquare,
                                move.DestSquare,
                                promoteToPiece);
                        },
                        () =>
                        {
                            isMovePlayed = chessEngine.TryPlayMove(
                                move.OriginSquare,
                                move.DestSquare);
                        });
                });

                if (!isMovePlayed)
                {
                    break;
                }
            }
        }

        private static string[] ParseUciCommand(string uciCommand)
        {
            return uciCommand.Trim().Split(' ', StringSplitOptions.RemoveEmptyEntries);
        }

        private static string MoveToUciNotation(Move move)
        {
            var sb = new StringBuilder(5);
            sb.Append($"{move.OriginSquare}{move.DestSquare}");
            if (move.IsPromotion)
            {
                sb.Append(PromotionPieceToUciNotation(move.GetPromoteToPiece()));
            }
            return sb.ToString();
        }

        private static char PromotionPieceToUciNotation(Piece promoteToPiece)
        {
            return promoteToPiece switch
            {
                Piece.Knight => 'n',
                Piece.Bishop => 'b',
                Piece.Rook => 'r',
                Piece.Queen => 'q',
                _ => throw new ArgumentException($"Unexpected promotion to piece {promoteToPiece}"),
            };
        }
    }
}
