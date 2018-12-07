package edu.cwru.students.cwrumapper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Class to override BottomSheet behavior to slide instead of snap.
 * @param <V> View containing BottomSheet.
 */
public class SlidingBottomSheetBehavior<V extends View> extends BottomSheetBehavior<V> {

    private boolean isSnapped;

    public SlidingBottomSheetBehavior() {
        super();
    }

    public SlidingBottomSheetBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    void listenForSlideEvents() {
        setBottomSheetCallback(new BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
            }

            @Override
            public void onSlide(@NonNull View view, float slideOffset) {
                // Snap only if slide factor is between 0.1 or 0.9
                isSnapped = slideOffset < 0.1f || slideOffset > 0.9f;
            }
        });
    }

    @Override
    public boolean onTouchEvent(CoordinatorLayout parent, V child, MotionEvent event) {
        if (!isSnapped) {
            int action = event.getActionMasked();
            switch (action) {
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    return true;
            }
        }

        return super.onTouchEvent(parent, child, event);
    }
}
