package cqipc.lijia.note;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

public class SlideListView extends ListView {

    Context context;
    GestureDetector gestureDetector;
    OnFlingListener mListener;


    @Override
    public ListAdapter getAdapter() {
        return super.getAdapter();
    }

    /*
     * 设置左右滑动监听
     * */
    public void setOnFlingListener(OnFlingListener listener) {
        this.mListener = listener;
        gestureDetector = new GestureDetector(context, new Gesture(context, mListener));
    }

    public SlideListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

    }

    public SlideListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
    }

    public SlideListView(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {


        if (gestureDetector.onTouchEvent(ev))
            return true;//当左右滑动时自己处理
        return super.onTouchEvent(ev);
    }

    /*
     * 滑动监听
     * */
    public class Gesture implements GestureDetector.OnGestureListener {
        Context context;
        OnFlingListener mListener;

        public Gesture(Context context, OnFlingListener listener) {
            this.context = context;
            this.mListener = listener;
        }

        @Override
        public boolean onDown(MotionEvent e) {
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
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }
        @Override
        /**
         *
         */
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            int i1= pointToPosition((int)e1.getX(),(int) e1.getY());
            int startPosition=(int)e2.getX();
            //无效
            if (i1 == AdapterView.INVALID_POSITION) {
                return true;
            }
            int position=i1-getFirstVisiblePosition();
            View view= SlideListView.this.getChildAt(i1-getFirstVisiblePosition());
            //System.out.println("===="+SlideListView.this.getCheckedItemPosition()+"---------------"+(i1-getFirstVisiblePosition()));
           //view.setBackgroundColor(Color.RED);
            //SlideListView.this.getChildAt(e1.getY)
          //  System.out.println("========"+SlideListView.this.getAdapter().toString()+"===="+SlideListView.this.getChildCount()+"+"+SlideListView.this.getY());
            //System.out.println(" e1.getPointerCount()="+ e1.getPointerCount()+" e1.getSource()="+e1.getSource()+" e1.getActionIndex()="+e1.getActionIndex());
            if (Math.abs(e1.getX() - e2.getX()) > Math.abs(e1.getY() - e2.getY())) {//当左右滑动距离大于上下滑动距离时才认为是左右滑
                // 左滑
                if (e1.getX() - e2.getX() > 100) {
                    mListener.onLeftFling(view,position,startPosition);
                    return true;
                }
                // 右滑
                else if (e1.getX() - e2.getX() < -100) {
                    mListener.onRightFling(view,position,startPosition);
                    return true;
                }
            }
            return true;
        }
    }

    /*
     * 左右滑动时调用的监听接口
     * */
    public interface OnFlingListener {
        public void onLeftFling(View itemView,int position ,int startPosition);
        public void onRightFling(View itemView,int position,int startPosition);
    }
}
