package ui.android.waitingtextapplication;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;



/**
 * Created by Administrator on 2017/11/13.
 * 后面省略号动画,随便多少个点 , 省略号loading样式
 */

public class WaitingView extends AppCompatTextView {

    private int position ;
    private int count = 0;
    private int interpolator = DensityUtil.px2dip(getContext() ,30);
    private int pointCount = 3;

    public WaitingView(Context context) {
        super(context);
        init();
    }

    public WaitingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WaitingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        start();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), heightMeasureSpec);
    }


    /**
     * 计算组件宽度
     */
    private int measureWidth(int widthMeasureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);
        if (specMode == MeasureSpec.EXACTLY) {//精确模式
            result = specSize;
        } else {
            result = getWidth();//最大尺寸模式，getDefaultWidth方法需要我们根据控件实际需要自己实现
            if (specMode == MeasureSpec.AT_MOST) {
                //前面文字距离 加上 间距 + 上最后一个点的距离
                result = (int)(getPaint().measureText(getText().toString())+  interpolator + DensityUtil.px2dip(getContext() , 90 * (pointCount -1)) + DensityUtil.px2dip(getContext() ,20));
            }
        }
        return result;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int x = 0 ; x < count ; x ++) {
            canvas.drawCircle(getPaint().measureText(getText().toString())+  interpolator + DensityUtil.px2dip(getContext() , 90 * x), getPaint().descent()-getPaint().ascent() - interpolator, DensityUtil.px2dip(getContext() ,20), getPaint());
        }
    }


    private void start(){
        final ValueAnimator valueAnimator = ValueAnimator.ofInt(0 , 90);
        valueAnimator.setDuration(500);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                position = DensityUtil.px2dip(getContext() ,(int)animation.getAnimatedValue() * pointCount);
                count = position / 30;
                invalidate();
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                valueAnimator.setStartDelay(300);
                valueAnimator.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        valueAnimator.start();
    }
}
