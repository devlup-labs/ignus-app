package org.ignus.app.ui.itemdecoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import org.ignus.app.utils.px

class EventCategoryItemDecoration(private val space: Int = 16.px) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        outRect.bottom = 2 * space
        outRect.left = space
        outRect.right = space

        if (parent.getChildAdapterPosition(view) == 0)
            outRect.top = 2 * space
    }
}