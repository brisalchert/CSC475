package com.example.todolist

import androidx.fragment.app.DialogFragment
import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText


class DialogNewListItem : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        val inflater = requireActivity().layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_new_listitem, null)

        // Get references to UI elements
        val editTitle: EditText = dialogView.findViewById(R.id.editTitle)
        val editDescription: EditText = dialogView.findViewById(R.id.editDescription)
        val checkBoxImportant: CheckBox = dialogView.findViewById(R.id.checkBoxImportant)
        val buttonCancel: Button = dialogView.findViewById(R.id.buttonCancel)
        val buttonOK: Button = dialogView.findViewById(R.id.buttonOK)

        // Set the dialog message
        builder.setView(dialogView).setMessage("Add a new to-do list item")

        // Set cancel button to close the dialog
        buttonCancel.setOnClickListener {
            dismiss()
        }

        // Set the OK button to create a new list item
        buttonOK.setOnClickListener {
            // Create a list item
            val newListItem = ListItem(
                title = editTitle.text.toString(),
                description = editDescription.text.toString(),
                completed = false,
                important = checkBoxImportant.isChecked
            )

            // Get reference to MainActivity
            val callingActivity = activity as MainActivity?

            // Pass list item back to MainActivity
            callingActivity!!.createNewListItem(newListItem)

            // Close the dialog
            dismiss()
        }

        return builder.create()
    }
}