package com.example.todolist

data class ListItem(
    var id: Int = 0, // Used for mapping to database objects
    var title: String?,
    var description: String?,
    var completed: Boolean,
    var important: Boolean
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ListItem) return false
        return id == other.id // Compare IDs instead of reference
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}