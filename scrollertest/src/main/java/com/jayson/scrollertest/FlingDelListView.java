package com.jayson.scrollertest;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

/**
 * author: jayson
 * blog: http://blog.csdn.net/jia4525036
 * time: 2018/6/15.
 * desc:
 */
public class FlingDelListView extends ListView implements View.OnTouchListener,GestureDetector.OnGestureListener{

    private GestureDetector gestureDetector;

    private OnDeleteListener onDeleteListener;

    private View deleteButton;

    private ViewGroup itemLayout;

    private int selectItem;

    private boolean isDeleteShown;

    public FlingDelListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        gestureDetector = new GestureDetector(context,this);
        setOnTouchListener(this);
    }

    public void setOnDeleteListener(OnDeleteListener listener){
        onDeleteListener = listener;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (isDeleteShown){
            itemLayout.removeView(deleteButton);
            deleteButton = null;
            isDeleteShown = false;
            return false;
        }else {
            return gestureDetector.onTouchEvent(event);
        }
    }

    @Override
    public boolean onDown(MotionEvent e) {
        if (!isDeleteShown){
            selectItem = pointToPosition((int) e.getX(),(int)e.getY());
        }
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (!isDeleteShown && Math.abs(velocityX) > Math.abs(velocityY)){
            deleteButton = LayoutInflater.from(getContext()).inflate(R.layout.delete_button,null);
            deleteButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemLayout.removeView(deleteButton);
                    deleteButton = null;
                    isDeleteShown = false;
                    onDeleteListener.onDelete(selectItem);
                }
            });
            itemLayout = (ViewGroup)getChildAt(selectItem - getFirstVisiblePosition());
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params.addRule(RelativeLayout.CENTER_VERTICAL);
            itemLayout.addView(deleteButton,params);
            isDeleteShown = true;
        }
        return false;
    }

    public interface OnDeleteListener{
        void onDelete(int index);
    }
}
