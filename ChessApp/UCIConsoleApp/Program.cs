using System;
using System.Collections.Concurrent;
using System.Threading;

namespace UCIConsoleApp
{
    class Program
    {
        private static readonly BlockingCollection<string> consoleInputQueue = new BlockingCollection<string>();
        private static readonly BlockingCollection<string> consoleOutputQueue = new BlockingCollection<string>();

        static void Main()
        {
            var consoleInputThread = CreateConsoleInputThread();
            var consoleOutputThread = CreateConsoleOutputThread();

            consoleInputThread.Start();
            consoleOutputThread.Start();

            using var engineTerminatedEvent = new ManualResetEvent(false);
            var mainEngineThread = CreateMainChessEngineThread(() => engineTerminatedEvent.Set());
            mainEngineThread.Start();

            engineTerminatedEvent.WaitOne();
            Environment.Exit(0);
        }

        private static Thread CreateConsoleInputThread()
        {
            return new Thread(() =>
            {
                while (true)
                {
                    var inputMessage = Console.ReadLine().Trim();
                    if (!string.IsNullOrEmpty(inputMessage))
                    {
                        consoleInputQueue.Add(inputMessage);
                    }
                }
            });
        }

        private static Thread CreateConsoleOutputThread()
        {
            return new Thread(() =>
            {
                while (true)
                {
                    var outputMessage = consoleOutputQueue.Take();
                    Console.WriteLine(outputMessage);
                }
            });
        }

        private static Thread CreateMainChessEngineThread(Action onTerminate)
        {
            return new Thread(() =>
            {
                var uciEngineWrapper = new UciEngineWrapper(
                    consoleInputQueue,
                    consoleOutputQueue);
                uciEngineWrapper.Run();
                onTerminate();
            });
        }
    }
}
