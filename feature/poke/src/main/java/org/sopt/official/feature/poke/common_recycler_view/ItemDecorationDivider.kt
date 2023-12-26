package org.sopt.official.feature.poke.common_recycler_view

import android.graphics.Canvas
import android.graphics.Paint
import androidx.recyclerview.widget.RecyclerView


class ItemDecorationDivider(
    color: Int,
    private val height: Float,
) : RecyclerView.ItemDecoration() {

    private val paint = Paint()

    init {
        paint.color = color
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        for (i in 0 until parent.childCount) {
            val top = (parent.getChildAt(i).bottom).toFloat()
            val bottom = top + height
            c.drawRect(0f, top, (parent.width).toFloat(), bottom, paint)
        }
    }
}