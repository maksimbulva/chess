package ru.maksimbulva.chess

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import ru.maksimbulva.chess.chesslib.ChesslibWrapper

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val chesslibWrapper = ChesslibWrapper()
        val myText: TextView = findViewById(R.id.text)
        myText.text = chesslibWrapper.calculateLegalMovesCount("", 0).toString()
    }
}
