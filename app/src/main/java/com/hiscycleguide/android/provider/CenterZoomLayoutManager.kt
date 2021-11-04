package com.hiscycleguide.android.provider

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hiscycleguide.android.fragment.ArticleFragment

class CenterZoomLayoutManager : LinearLayoutManager {

    private val mShrinkAmount = 0.15f
    private val mShrinkDistance = 0.9f


    constructor(context: Context) : super(context)

    constructor(context: Context, orientation: Int, reverseLayout: Boolean) : super(
        context,
        orientation,
        reverseLayout
    )

    override fun scrollHorizontallyBy(
        dx: Int,
        recycler: RecyclerView.Recycler?,
        state: RecyclerView.State?
    ): Int {
        val orientation = orientation
        if (orientation == HORIZONTAL) {
            val scrolled = super.scrollHorizontallyBy(dx, recycler, state)

            val midPoint = width / 2.0f
            val d0 = 0.0f
            val d1 = mShrinkDistance * midPoint
            val s0 = 1.0f
            val s1 = 1.0 - mShrinkAmount

            for (i in 0 until childCount) {
                val child: View = getChildAt(i)!!
                val childMidPoint = (getDecoratedRight(child) + getDecoratedLeft(child)) / 2.0f
                val d = Math.min(d1, Math.abs(midPoint - childMidPoint))
                val scale = s0 + (s1 - s0) * (d - d0) / (d1 - d0)
                child.scaleY = scale.toFloat()
            }

            return scrolled
        } else {
            return 0
        }
    }
}