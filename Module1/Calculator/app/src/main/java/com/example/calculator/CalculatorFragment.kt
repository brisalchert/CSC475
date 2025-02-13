package com.example.calculator

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.calculator.databinding.FragmentCalculatorBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class CalculatorFragment : Fragment() {

    private var _binding: FragmentCalculatorBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val postFixStack = ArrayDeque<Char>()

    private var lastWasOperator = false

    private var percent = false

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

        binding.buttonDecimal.setOnClickListener() {
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
            // Split expression on spaces, add operators and values to stacks, calculate result and display
            extendExpression('=')
        }

        binding.buttonClear.setOnClickListener {
            binding.expression.text = ""
            binding.currentNumber.text = "0"
        }

        binding.buttonSign.setOnClickListener {
            if (lastWasOperator) {
                return@setOnClickListener
            }

            if (binding.currentNumber.text.first() == '-') {
                binding.currentNumber.text = binding.currentNumber.text.substring(1)
            } else if (binding.currentNumber.text.toString() != "0") {
                val negate = "-${binding.currentNumber.text}"
                binding.currentNumber.text = negate
            }
        }

        binding.buttonPercent.setOnClickListener {
            if (lastWasOperator) {
                return@setOnClickListener
            }

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

    private fun extendCurrentNumber(number: String) {
        if (!lastWasOperator && binding.currentNumber.text.length >= 12) {
            return
        }

        val next: String

        // Reset currentNumber after selecting an operator or clearing
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

    private fun extendExpression(operator: Char) {
        // Remove previous operator if no new number was provided
        if (lastWasOperator) {
            val operatorLength = 3
            val explength = binding.expression.length()
            val currentNumLength = binding.currentNumber.length()
            binding.expression.text = binding.expression.text.substring(0, explength - currentNumLength - operatorLength)
            percent = false
        }

        var currentNumber: String = binding.currentNumber.text.toString()
        currentNumber += " $operator "

        binding.expression.append(currentNumber)
        lastWasOperator = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}