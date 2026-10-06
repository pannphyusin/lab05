package com.example.calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                CalculatorApp()
            }
        }
    }
}

@Composable
fun CalculatorApp() {
    var displayText by remember { mutableStateOf("0") }
    var firstNumber by remember { mutableStateOf(0.0) }
    var currentOperation by remember { mutableStateOf("") }
    var isNewInput by remember { mutableStateOf(true) }

    fun onNumberClick(number: String) {
        if (isNewInput || displayText == "0") {
            displayText = number
            isNewInput = false
        } else {
            displayText += number
        }
    }

    fun onOperationClick(op: String) {
        firstNumber = displayText.toDoubleOrNull() ?: 0.0
        currentOperation = op
        isNewInput = true
    }

    fun onEqualsClick() {
        val secondNumber = displayText.toDoubleOrNull() ?: 0.0
        val result = when (currentOperation) {
            "+" -> firstNumber + secondNumber
            "-" -> firstNumber - secondNumber
            "*" -> firstNumber * secondNumber
            "/" -> if (secondNumber != 0.0) firstNumber / secondNumber else "Error"
            else -> displayText
        }
        displayText = if (result is Double && result % 1.0 == 0.0) {
            result.toInt().toString()
        } else {
            result.toString()
        }
        isNewInput = true
    }

    fun onClearClick() {
        displayText = "0"
        firstNumber = 0.0
        currentOperation = ""
        isNewInput = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.End
    ) {
        Text(
            text = displayText,
            fontSize = 48.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 32.dp),
            maxLines = 1
        )

        val buttons = listOf(
            listOf("7", "8", "9", "/"),
            listOf("4", "5", "6", "*"),
            listOf("1", "2", "3", "-"),
            listOf("C", "0", "=", "+")
        )

        for (row in buttons) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                for (btnText in row) {
                    Button(
                        onClick = {
                            when (btnText) {
                                in "0".."9" -> onNumberClick(btnText)
                                "+", "-", "*", "/" -> onOperationClick(btnText)
                                "=" -> onEqualsClick()
                                "C" -> onClearClick()
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(64.dp)
                    ) {
                        Text(text = btnText, fontSize = 24.sp)
                    }
                }
            }
        }
    }
}