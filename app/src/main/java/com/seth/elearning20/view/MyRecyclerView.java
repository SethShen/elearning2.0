package com.seth.elearning20.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.seth.elearning20.R;

/**
 * Created by Seth on 2017/5/10.
 */

public class MyRecyclerView extends RecyclerView {

    private Context mContext;

    //上一次的触摸点
    private int mLastX, mLastY;
    //当前触摸的item的位置
    private int mPosition;

    //item对应的布局
    private LinearLayout mItemLayout;
    //删除按钮
    private TextView mDelete;

    //最大滑动距离(即删除按钮的宽度)
    private int mMaxLength;
    //是否在垂直滑动列表
    private boolean isDragging;
    //item是在否跟随手指移动
    private boolean isItemMoving;

    //item是否开始自动滑动
    private boolean isStartScroll;
    //删除按钮状态   0：关闭 1：将要关闭 2：将要打开 3：打开
    private int mDeleteBtnState;

    //检测手指在滑动过程中的速度
    private VelocityTracker mVelocityTracker;
    private Scroller mScroller;
    private OnItemClickListener mListener;

    public MyRecyclerView(Context context) {
        this(context, null);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;

        mScroller = new Scroller(context, new LinearInterpolator());
        mVelocityTracker = VelocityTracker.obtain();
    }


    public boolean onTouchEvent(MotionEvent e){
        mVelocityTracker.addMovement(e);

        int x = (int) e.getX();
        int y = (int) e.getY();
        switch(e.getAction()) {
            case MotionEvent.ACTION_DOWN:       //点击
                if (mDeleteBtnState == 0) {
                    //通过findChildViewUnder()方法得到触摸点对应的Item View
                    View view = findChildViewUnder(x, y);
                    if (view == null)
                        return false;
                    //接下来通过getChildViewHolder()得到对应的ViewHolder，
                    MyViewHolder viewHolder = (MyViewHolder) getChildViewHolder(view);
                    //有了ViewHolder，我们就可以解析出Item的布局mItemLayout
                    mItemLayout = viewHolder.layout;
                    //当前Item的下标mPosition
                    mPosition = viewHolder.getAdapterPosition();

                    mDelete = (TextView) mItemLayout.findViewById(R.id.item_delete);
                    //删除按钮的最大滑动距离就是其宽度
                    mMaxLength = mDelete.getWidth();
                    //绑定删除点击事件
                    mDelete.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mListener.onDeleteClick(mPosition);
                            mItemLayout.scrollTo(0, 0);
                            mDeleteBtnState = 0;
                        }
                    });
                } else if (mDeleteBtnState == 3) {
                    //当else if (mDeleteBtnState == 3)时，Item上的删除按钮完全展示，
                    // 如果点击删除按钮外的任意区域则通过startScroll()方法使Item自动右滑直到删除按钮完全隐藏
                    mScroller.startScroll(mItemLayout.getScrollX(), 0, -mMaxLength, 0, 200);
                    //刷新界面
                    invalidate();
                    mDeleteBtnState = 0;
                    return false;
                } else {
                    return false;
                }

                break;
            case MotionEvent.ACTION_MOVE:   //手指滑动
                int dx = mLastX - x;
                int dy = mLastY - y;

                int scrollX = mItemLayout.getScrollX();
                if(Math.abs(dx)>Math.abs(dy)){
                    isItemMoving = true;
                    if(scrollX+dx <= 0){        //左边界检测
                        mItemLayout.scrollTo(0,0);
                        return true;
                    }else if(scrollX + dx >= mMaxLength){   //右边界检测
                        mItemLayout.scrollTo(mMaxLength,0);
                        return true;
                    }
                    mItemLayout.scrollBy(dx,0);
                }
                break;

            case MotionEvent.ACTION_UP:
                if(!isItemMoving && !isDragging && mListener != null){
                    mListener.onItemClick(mItemLayout,mPosition);
                }
                isItemMoving = false;
                mVelocityTracker.computeCurrentVelocity(1000);      //计算手指滑动速度
                float xVelocity = mVelocityTracker.getXVelocity();  //获取水平速度
                float yVelocity = mVelocityTracker.getYVelocity();  //获取垂直方向速度

                int deltaX = 0;
                int upScrollX = mItemLayout.getScrollX();

                //根据速度判断手指抬起后Item的滑动情况
                if(Math.abs(xVelocity)>100 && Math.abs(xVelocity)>Math.abs(yVelocity)) {
                    if(xVelocity <= -100) {                  //左划速度大于100，则显示删除按钮
                        deltaX = mMaxLength - upScrollX;
                        mDeleteBtnState = 2;
                    }else if(xVelocity >100){
                        deltaX = upScrollX;                 //又滑速度超100，隐藏删除按钮
                        mDeleteBtnState = 1;
                    } else {
                        if (upScrollX >= mMaxLength / 2) {//item的左滑动距离大于删除按钮宽度的一半，则则显示删除按钮
                            deltaX = mMaxLength - upScrollX;
                            mDeleteBtnState = 2;
                        } else if (upScrollX < mMaxLength / 2) {//否则隐藏
                            deltaX = -upScrollX;
                            mDeleteBtnState = 1;
                        }
                    }

                }

                //item自动滑动到指定位置
                mScroller.startScroll(upScrollX,0,deltaX,0,200);
                isStartScroll = true;
                invalidate();

                mVelocityTracker.clear();
                break;
        }
        mLastX = x;
        mLastY = y;
        return  super.onTouchEvent(e);
        }

    public void computeScroll(){
        if(mScroller.computeScrollOffset()){
            mItemLayout.scrollTo(mScroller.getCurrX(),mScroller.getCurrY());
            invalidate();
        }else if(isStartScroll){    //isStartScroll代表手指抬起后Item自动滑动的状态
                                    //MotionEvent.ACTION_DOWN我们将其赋值为true
            isStartScroll = false;
            if(mDeleteBtnState==1){
                mDeleteBtnState = 0;
            }
            if(mDeleteBtnState == 2)
                mDeleteBtnState =3;
        }
    }

    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        isDragging = state == SCROLL_STATE_DRAGGING;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
}
