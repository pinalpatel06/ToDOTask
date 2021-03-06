package com.example.android.todolist.event;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by veeral on 22/08/2016.
 */
public class RecyclerViewItemClickListener implements RecyclerView.OnItemTouchListener {

    private GestureDetector gestureDetector;
    private OnItemClickListener listener;
    public interface OnItemClickListener{
        public void onItemClick(View v , int position);

    }

    public RecyclerViewItemClickListener(Context context,OnItemClickListener listener) {
        this.listener = listener;
        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
        View childView = view.findChildViewUnder(e.getX(), e.getY());
        if (childView != null && listener != null && gestureDetector.onTouchEvent(e)) {
            listener.onItemClick(childView, view.getChildPosition(childView));
            return true;
        }
        return false;
    }


    @Override
    public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) {

    }
    @Override public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {}

}
