package com.example.reshiprimer

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.SystemClock
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import kotlin.math.roundToInt
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val constraintLayout: ConstraintLayout = findViewById(R.id.constraintLayout)
        val startButton: Button = findViewById(R.id.startButton)
        val trueButton: Button = findViewById(R.id.trueButton)
        val falseButton: Button = findViewById(R.id.falseButton)
        var startTime = SystemClock.elapsedRealtime()
        var minTimeLabel: TextView = findViewById(R.id.textViewMinNum)
        var maxTimeLabel: TextView = findViewById(R.id.textViewMaxNum)
        var avgTimeLabel: TextView = findViewById(R.id.textViewAvgNum)

        var minTime: Float = 0f
        var maxTime: Float = 0f
        var sumTime: Float = 0f

        val firstNum: TextView = findViewById(R.id.firstNum)
        val operationLabel: TextView = findViewById(R.id.operationLabel)
        val secondNum: TextView = findViewById(R.id.secondNum)
        val result: TextView = findViewById(R.id.resultText)
        val correctLabel: TextView = findViewById(R.id.correctLabel)
        val wrongLabel: TextView = findViewById(R.id.wrongLabel)
        val totalLabel: TextView = findViewById(R.id.totalLabel)
        val percentLabel: TextView = findViewById(R.id.percentLabel)
        val rnd = Random
        var correctInt = 0
        var wrongInt = 0
        val operations: List<String> = listOf("+", "-", "/", "*")
        trueButton.isEnabled = false
        falseButton.isEnabled = false

        result.isEnabled = false


        fun getTrueResult(): Int {
            return when (operationLabel.text) {
                "+" -> {
                    (firstNum.text.toString().toInt() + secondNum.text.toString()
                        .toInt())
                }
                "-" -> {
                    (firstNum.text.toString().toInt() - secondNum.text.toString()
                        .toInt())
                }
                "/" -> {
                    (firstNum.text.toString().toInt() / secondNum.text.toString()
                        .toInt())
                }
                "*" -> {
                    (firstNum.text.toString().toInt() * secondNum.text.toString()
                        .toInt())
                }
                else -> 0
            }
        }

        fun genRandomPrimer() {
            result.text = ""
            operationLabel.text = operations.elementAt(rnd.nextInt(0, 4))
            var first = rnd.nextInt(10, 100)
            var second = rnd.nextInt(10, 100)
            if (operationLabel.text == "/") {
                while (first.toDouble() % second.toDouble() != 0.0) {
                    first = rnd.nextInt(10, 100)
                    second = rnd.nextInt(10, 100)
                }
            }
            firstNum.text = first.toString()
            secondNum.text = second.toString()

            // с вероятностью 50 процентов либо генерим правильный ответ, либо случайный неправильный
            val trueResult = Random.nextBoolean()
            if (trueResult) {
                result.text = getTrueResult().toString()
            } else {
                var resultNum = rnd.nextInt(10, 1000)
                while (resultNum == getTrueResult()) {
                    resultNum = rnd.nextInt(10, 1000)
                }
                result.text = resultNum.toString()
            }
        }

        fun checkPrimer(): Boolean {
            return getTrueResult() == result.text.toString().toInt()
        }

        // Событие по нажатии на "Старт"
        startButton.setOnClickListener()
        {
            startButton.isEnabled = false
            constraintLayout.setBackgroundResource(R.color.white);
            trueButton.isEnabled = true
            falseButton.isEnabled = true
            startTime = SystemClock.elapsedRealtime()
            result.text = ""
            genRandomPrimer()
        }

        fun updateTimes(decisionTime: Float, totalCount: Int) {
            if (decisionTime < minTime || minTime == 0f) {
                minTime = decisionTime
                minTimeLabel.text = "%.2f".format(minTime)
            }

            if (decisionTime > maxTime) {
                maxTime = decisionTime
                maxTimeLabel.text = "%.2f".format(maxTime)
            }

            sumTime += decisionTime
            avgTimeLabel.text = "%.2f".format(sumTime / totalCount)
        }

        // проверяет, правильную ли кнопку (Верно/Неверно) тыкнул пользователь
        fun checkCorrectChoice(choice: Boolean) {
            val decisionTime = (SystemClock.elapsedRealtime() - startTime) / 1000f

            if ((checkPrimer() && choice) || (!checkPrimer() && !choice)) {
                // правильное решение
                correctInt++
                correctLabel.text = correctInt.toString()
                constraintLayout.setBackgroundResource(R.color.green);
            } else {
                // неправильное решение
                wrongInt++
                wrongLabel.text = wrongInt.toString()
                constraintLayout.setBackgroundResource(R.color.red);
            }
            totalLabel.text = (correctInt + wrongInt).toString()

            percentLabel.text = (
                    (((correctInt.toDouble() / (totalLabel.text.toString().toDouble() / 100.0)) * 100).roundToInt().toDouble() / 100
                            ).toString()
                            + "%")
            result.isEnabled = false
            trueButton.isEnabled = false
            falseButton.isEnabled = false
            startButton.isEnabled = true

            if (totalLabel.text.toString().toInt() != 0) {
                updateTimes(decisionTime, totalLabel.text.toString().toInt())
            }
        }

        // Событие по нажатии на кнопку "Верно"
        trueButton.setOnClickListener()
        {
            checkCorrectChoice(true);
        }

        // Событие по нажатии на кнопку "Неверно"
        falseButton.setOnClickListener()
        {
            checkCorrectChoice(false);
        }
    }
}