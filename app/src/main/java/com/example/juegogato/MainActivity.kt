package com.example.juegogato

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private val board = Array(3) { CharArray(3) }
    private var playerTurn = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        resetBoard()
    }

    fun onCellClick(view: View) {
        if (view !is Button) {
            return
        }

        if (view.text.toString() != "") {
            return
        }

        if (playerTurn) {
            view.text = "X"
            val buttonIndex = findViewById<GridLayout>(R.id.gridLayout).indexOfChild(view)
            val row = buttonIndex / 3
            val col = buttonIndex % 3
            board[row][col] = 'X'

            if (checkWin('X')) {
                showToast("Player X wins!")
                resetBoard()
                return
            }
        }

        if (isBoardFull()) {
            showToast("It's a draw!")
            resetBoard()
            return
        }

        playerTurn = !playerTurn
        if (!playerTurn) {
            makeComputerMove()
        }
    }

    private fun makeComputerMove() {
        var emptyCells = mutableListOf<Pair<Int, Int>>()
        for (i in 0..2) {
            for (j in 0..2) {
                if (board[i][j] == ' ') {
                    emptyCells.add(Pair(i, j))
                }
            }
        }

        if (emptyCells.isNotEmpty()) {
            val (row, col) = emptyCells.random()
            board[row][col] = 'O'
            val gridLayout = findViewById<GridLayout>(R.id.gridLayout)
            val button = gridLayout.getChildAt(row * 3 + col) as Button
            button.text = "O"

            if (checkWin('O')) {
                showToast("Player O wins!")
                resetBoard()
                return
            }

            if (isBoardFull()) {
                showToast("It's a draw!")
                resetBoard()
                return
            }
        }
        playerTurn = !playerTurn
    }

    private fun checkWin(player: Char): Boolean {
        // Check rows
        for (i in 0..2) {
            if (board[i][0] == player && board[i][1] == player && board[i][2] == player) {
                return true
            }
        }
        // Check columns
        for (j in 0..2) {
            if (board[0][j] == player && board[1][j] == player && board[2][j] == player) {
                return true
            }
        }
        // Check diagonals
        if (board[0][0] == player && board[1][1] == player && board[2][2] == player) {
            return true
        }
        if (board[0][2] == player && board[1][1] == player && board[2][0] == player) {
            return true
        }
        return false
    }

    private fun isBoardFull(): Boolean {
        for (i in 0..2) {
            for (j in 0..2) {
                if (board[i][j] == ' ') {
                    return false
                }
            }
        }
        return true
    }

    private fun resetBoard() {
        for (i in 0..2) {
            for (j in 0..2) {
                board[i][j] = ' '
            }
        }

        val gridLayout = findViewById<GridLayout>(R.id.gridLayout)
        for (i in 0 until gridLayout.childCount) {
            val button = gridLayout.getChildAt(i) as Button
            button.text = ""
        }
        playerTurn = true
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
