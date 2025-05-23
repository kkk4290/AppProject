package com.example.smartcage;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.DimenRes;
import androidx.recyclerview.widget.RecyclerView;

public class HorizontalMarginItemDecoration extends RecyclerView.ItemDecoration {

    private final int horizontalMargin;

    public HorizontalMarginItemDecoration(Context context, @DimenRes int horizontalMarginInDp) {
        this.horizontalMargin = context.getResources().getDimensionPixelSize(horizontalMarginInDp);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = horizontalMargin;
        outRect.right = horizontalMargin;
    }
}
