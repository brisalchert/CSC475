package com.example.calculator

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.calculator.databinding.FragmentCalculatorBinding
import kotlin.math.abs
import kotlin.math.min

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class CalculatorFragment : Fragment() {

    private var _binding: FragmentCalculatorBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    // Use Java Postfix Calculator
    private val postfixCalculator = PostfixCalculator()

    private val maxNumber = 999999999999

    private var lastWasOperator = false
    private var percent = false
    private var evaluated = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalculatorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set button functionality
        binding.button0.setOnClickListener {
            extendCurrentNumber("0")
        }

        binding.button1.setOnClickListener {
            extendCurrentNumber("1")
        }

        binding.button2.setOnClickListener {
            extendCurrentNumber("2")
        }

        binding.button3.setOnClickListener {
            extendCurrentNumber("3")
        }

        binding.button4.setOnClickListener {
            extendCurrentNumber("4")
        }

        binding.button5.setOnClickListener {
            extendCurrentNumber("5")
        }

        binding.button6.setOnClickListener {
            extendCurrentNumber("6")
        }

        binding.button7.setOnClickListener {
            extendCurrentNumber("7")
        }

        binding.button8.setOnClickListener {
            extendCurrentNumber("8")
        }

        binding.button9.setOnClickListener {
            extendCurrentNumber("9")
        }

        binding.buttonDecimal.setOnClickListener {
            // Ensure decimal is a valid input
            if (lastWasOperator || !binding.currentNumber.text.contains('.')) {
                // Clear to 0 if necessary to prevent a leading decimal
                if (lastWasOperator) {
                    extendCurrentNumber("0")
                }

                val decimal = binding.currentNumber.text.toString() + "."
                binding.currentNumber.text = decimal
            }
        }

        binding.buttonPlus.setOnClickListener {
            extendExpression('+')
        }

        binding.buttonMinus.setOnClickListener {
            extendExpression('-')
        }

        binding.buttonMultiply.setOnClickListener {
            extendExpression('*')
        }

        binding.buttonDivide.setOnClickListener {
            extendExpression('/')
        }

        binding.buttonEquals.setOnClickListener {
            // Finish the expression with the equals sign
            extendExpression('=')
            val expression = binding.expression.text.toString()

            // Convert expression to postfix form, removing the equals sign
            val infixExpression = expression.substring(0, expression.length - 2)

            val postfixExpression = infixToPostfix(infixExpression)

            // Evaluate postfix expression, converting to integer if possible
            val result = longOrDouble(postfixCalculator.evaluatePostfix(postfixExpression)).toString()

            // Reset calculator on division by 0
            if (result == "Infinity" || result == "-Infinity") {
                binding.expression.text = ""
                binding.currentNumber.text = "0"

                // Notify the user
                Toast.makeText(
                    requireContext(),
                    "Cannot divide by 0.",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            // Set currentNumber to result, bounding at the maximum display length
            if (abs(result.toDouble()) > maxNumber) {
                val toastText: String
                val displayValue: String

                // Check if max or min was exceeded
                if (result.toDouble() > 0) {
                    toastText = "Maximum"
                    displayValue = maxNumber.toString()
                    binding.currentNumber.text = displayValue
                } else {
                    toastText = "Minimum"
                    displayValue = (-maxNumber).toString()
                    binding.currentNumber.text = displayValue
                }

                // Notify the user
                Toast.makeText(
                    requireContext(),
                    "$toastText value exceeded.\nDisplaying ${toastText.lowercase()} instead.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                binding.currentNumber.text = result.substring(0, min(result.length, 12))
            }

            // Update evaluated state
            evaluated = true
        }

        binding.buttonClear.setOnClickListener {
            binding.expression.text = ""
            binding.currentNumber.text = "0"
        }

        binding.buttonSign.setOnClickListener {
            // Don't allow sign changes on an operator
            if (lastWasOperator && !evaluated) {
                return@setOnClickListener
            }

            // Swap sign from negative to positive or vice versa
            if (binding.currentNumber.text.first() == '-') {
                binding.currentNumber.text = binding.currentNumber.text.substring(1)
            } else if (binding.currentNumber.text.toString() != "0") {
                val negate = "-${binding.currentNumber.text}"
                binding.currentNumber.text = negate
            }
        }

        binding.buttonPercent.setOnClickListener {
            // Don't allow percentage on an operator
            if (lastWasOperator && !evaluated) {
                return@setOnClickListener
            }

            // Toggle between percentage and decimal representation
            if (!percent) {
                val numPercent = (binding.currentNumber.text.toString().toDouble() / 100).toString()
                binding.currentNumber.text = numPercent
                percent = true
            } else {
                val num = (binding.currentNumber.text.toString().toDouble() * 100).toString()
                binding.currentNumber.text = num
                percent = false
            }
        }
    }

    /**
     * Extends the input number displayed by the calculator, resetting it if necessary
     */
    private fun extendCurrentNumber(number: String) {
        // Prevent values larger than maxNumber
        if (!lastWasOperator && binding.currentNumber.text.length >= 12) {
            return
        }

        val next: String

        // Reset currentNumber after selecting an operator, evaluating an expression, or clearing
        if (lastWasOperator) {
            next = number
            lastWasOperator = false
        } else if (binding.currentNumber.text.toString() == "0") {
            next = number
        } else {
            next = binding.currentNumber.text.toString() + number
        }

        binding.currentNumber.text = next
    }

    /**
     * Extends the current expression displayed above the input based on the operands
     * and operators chosen by the user.
     */
    private fun extendExpression(operator: Char) {
        if (evaluated) {
            // Reset expression if last expression was evaluated
            binding.expression.text = ""
            evaluated = false
        } else if (lastWasOperator) {
            // Replace previous operator if no new operand was provided
            val operatorLength = 3
            val expressionLength = binding.expression.length()
            val currentNumLength = binding.currentNumber.length()
            binding.expression.text = binding.expression.text.substring(
                0, expressionLength - currentNumLength - operatorLength
            )
        }

        percent = false

        var currentNumber: String = binding.currentNumber.text.toString()
        currentNumber += " $operator "

        binding.expression.append(currentNumber)
        lastWasOperator = true
    }

    /**
     * Returns a value corresponding to the precedence of an operator (higher = more precedence)
     */
    private fun precedence(operator: String): Int {
        // Return higher precedence for division and multiplication
        return if (operator == "/" || operator == "*") {
            1
        } else {
            0
        }
    }

    /**
     * Converts an infix expression to a valid postfix expression
     */
    private fun infixToPostfix(expression: String): String {
        val operatorStack = ArrayDeque<String>()
        val result = StringBuilder()

        // Split string on spaces
        val expressionList = expression.split(" ")

        for (element in expressionList) {
            if (element.toDoubleOrNull() != null) {
                // Add operands to the result
                result.append(element)
                result.append(" ")
            } else {
                // Add operator to the stack, respecting operator precedence
                while (!operatorStack.isEmpty()
                        && precedence(element) <= precedence(operatorStack.last())) {
                    result.append(operatorStack.removeLast())
                    result.append(" ")
                }

                operatorStack.add(element)
            }
        }

        // Add remaining operators
        while (!operatorStack.isEmpty()) {
            result.append(operatorStack.removeLast())
            result.append(" ")
        }

        return result.toString()
    }

    /**
     * Converts a floating-point number to an integer if it has no fractional component
     */
    private fun longOrDouble(number: Double): Number {
        if (number % 1 == 0.0) {
            return number.toLong()
        }

        return number
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}