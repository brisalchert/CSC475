package com.example.steamtracker.ui.components

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Modifier.horizontalScrollbar(
    listState: LazyListState,
    height: Dp = 8.dp,
    color: Color = MaterialTheme.colorScheme.outlineVariant
): Modifier {
    return drawWithContent {
        drawContent()

        val layoutInfo = listState.layoutInfo
        val visibleItemsInfo = layoutInfo.visibleItemsInfo

        // Do not draw for an empty list
        if (visibleItemsInfo.firstOrNull() == null) return@drawWithContent

        val lazyRowWidth = layoutInfo.viewportSize.width.toFloat()
        val currentScrollPosition = visibleItemsInfo.first().offset
        val itemWidth = visibleItemsInfo.first().size
        val totalWidth = itemWidth * layoutInfo.totalItemsCount

        // Do not draw for list that doesn't exceed screen width
        if (totalWidth <= lazyRowWidth) return@drawWithContent

        val scrollProgress = -currentScrollPosition / (totalWidth - lazyRowWidth)
        val scrollbarWidth = lazyRowWidth * (lazyRowWidth / totalWidth).coerceAtLeast(0.1f)
        val scrollbarOffsetX = scrollProgress * (lazyRowWidth - scrollbarWidth)

        drawRoundRect(
            topLeft = Offset(scrollbarOffsetX, this.size.height - height.toPx()),
            color = color,
            size = Size(scrollbarWidth, height.toPx()),
            alpha = 1f,
            cornerRadius = CornerRadius(height.toPx() / 2)
        )
    }
}
