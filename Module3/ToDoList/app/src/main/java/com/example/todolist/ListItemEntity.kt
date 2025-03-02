package com.example.todolist

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "list_items")
data class ListItemEntity(
    @PrimaryKey(autoGenerate = true) val itemId: Int = 0,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "completed") val completed: Boolean,
    @ColumnInfo(name = "important") val important: Boolean
) {
    companion object {
        // Constructs a database entity from a ListItem object
        fun fromListItem(item: ListItem): ListItemEntity {
            return ListItemEntity(
                itemId = item.id,
                title = item.title,
                description = item.description,
                completed = item.completed,
                important = item.important
            )
        }
    }

    // Converts database entity into a ListItem Kotlin object
    fun toListItem(): ListItem {
        return ListItem(
            id = this.itemId,
            title = this.title,
            description = this.description,
            completed = this.completed,
            important = this.important
        )
    }
}