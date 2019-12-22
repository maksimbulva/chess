package ru.maksimbulva.chess.core.pgn

object PgnParser {

    private val GAME_RESULTS = hashSetOf("1-0", "0-1", "1/2-1/2", "*")

    fun parse(strIter: Iterator<String>): List<PgnGame> {
        return parse(strIter.asSequence().flatMap { parseToWords(it).asSequence() })
    }

    private fun parse(words: Sequence<String>): List<PgnGame> {
        val games = mutableListOf<PgnGame>()

        var currentParserState: State = State.ParseMoveList()
        var currentTagKey = ""
        val currentTags = mutableMapOf<String, String>()
        val unparsedMoves = mutableListOf<String>()

        words.forEach { word ->
            val newParserState = currentParserState.consume(word)
            val oldParserState = currentParserState

            if (newParserState !== oldParserState) {
                when (oldParserState) {
                    is State.ParseMoveList -> unparsedMoves.addAll(oldParserState.moveList)
                    is State.ParseTagKey -> currentTagKey = oldParserState.tagKey
                    is State.ParseTagValue -> {
                        currentTags[currentTagKey] = oldParserState.tagValue
                        currentTagKey = ""
                    }
                }

                if (newParserState is State.ParseTagKey) {
                    if (!unparsedMoves.isEmpty()) {
                        require(unparsedMoves.isEmpty())
                    }
                }
            }

            currentParserState = newParserState

            if (newParserState is State.ParseMoveList && word in GAME_RESULTS) {
                val moveList = newParserState.moveList
                unparsedMoves.addAll(moveList.take(moveList.size - 1))
                games.add(PgnGameFactory.createGame(currentTags, unparsedMoves))
                currentTags.clear()
                unparsedMoves.clear()
                currentParserState = State.ParseMoveList()
            }
        }

        return games
    }

    private fun parseToWords(str: String): List<String> {
        val words = mutableListOf<String>()
        var currentWord = StringBuilder()
        val completeCurrentWord = {
            if (currentWord.isNotEmpty()) {
                words.add(currentWord.toString())
            }
            currentWord = StringBuilder()
        }

        var index = 0
        while (index < str.length) {
            val c = str[index]

            when {
                c == '[' || c == ']' || c == '\"' || c == '{' || c == '}' -> {
                    completeCurrentWord()
                    words.add(c.toString())
                }
                c == '\\' -> {
                    if (index + 1 < str.length) {
                        when (str[index + 1]) {
                            '\\', '\"' -> {
                                currentWord.append(str[index])
                                ++index
                            }
                            else -> currentWord.append(c)
                        }
                    }
                }
                c.isWhitespace() -> completeCurrentWord()
                else -> currentWord.append(c)
            }

            ++index
        }

        completeCurrentWord()
        return words
    }

    private sealed class State {

        abstract fun consume(word: String): State

        class ParseMoveList : State() {
            private val consumedWords = mutableListOf<String>()

            val moveList: List<String>
                get() = consumedWords

            override fun consume(word: String): State {
                return when (word) {
                    "[" -> ParseTagKey()
                    "]" -> this
                    "{" -> ParseAnnotation
                    else -> this.also { consumedWords.add(word) }
                }
            }
        }

        class ParseTagKey : State() {
            private val consumedWords = mutableListOf<String>()

            val tagKey: String
                get() = consumedWords.joinToString(separator = " ")

            override fun consume(word: String): State {
                return when (word) {
                    "\"" -> ParseTagValue()
                    "]" -> ParseMoveList()
                    else -> this.also { consumedWords.add(word) }
                }
            }
        }

        class ParseTagValue : State() {
            private val consumedWords = mutableListOf<String>()

            val tagValue: String
                get() = consumedWords.joinToString(separator = " ")

            override fun consume(word: String): State {
                return when (word) {
                    "\"" -> ParseMoveList()
                    else -> this.also { consumedWords.add(word) }
                }
            }
        }

        object ParseAnnotation : State() {
            override fun consume(word: String): State {
                return when (word) {
                    "}" -> ParseMoveList()
                    else -> this
                }
            }
        }
    }
}
