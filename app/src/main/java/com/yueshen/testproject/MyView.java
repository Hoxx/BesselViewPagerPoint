package com.yueshen.testproject;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Android on 17/4/14.
 */

public class MyView extends View {

    private Paint paint;

    private Path path;


    private float pointAX=200;
    private float pointAY=200;
    private float pointCX=600;
    private float pointCY=600;

    private P p1,p2,p3,p4,p5;

    private double circleDistance;

    private double maxDistance=600;

    public MyView(Context context) {
        super(context);
        init(context);
    }

    public MyView(Context context,  AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyView(Context context,  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        paint=new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(2);


        path=new Path();

        pointCX=pointAX;
        pointCY=pointAY;

        p1=new P();
        p2=new P();
        p3=new P();
        p4=new P();
        p5=new P();


    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.e("TAG","宽度："+canvas.getWidth());

        canvas.drawCircle(pointAX,pointAY,100,paint);
        canvas.drawCircle(pointCX,pointCY,100,paint);

//        canvas.drawLine(pointAX,pointAY,pointCX,pointCY,paint);

        //坐标
//        canvas.drawLine(pointAX-200,pointAY,pointAX+200,pointAY,paint);
//        canvas.drawLine(pointAX,pointAY-200,pointAX,pointAY+200,paint);
//        canvas.drawLine(pointCX-200,pointCY,pointCX+200,pointCY,paint);
//        canvas.drawLine(pointCX,pointCY-200,pointCX,pointCY+200,paint);

        //两个圆心连线的垂直线
//        if(p1.X>0&&p1.Y>0)
//            canvas.drawLine(pointAX,pointAY,p1.X,p1.Y,paint);
//        if(p2.X>0&&p2.Y>0)
//            canvas.drawLine(pointAX,pointAY,p2.X,p2.Y,paint);
//        if(p3.X>0&&p3.Y>0)
//            canvas.drawLine(pointCX,pointCY,p3.X,p3.Y,paint);
//        if(p4.X>0&&p4.Y>0)
//            canvas.drawLine(pointCX,pointCY,p4.X,p4.Y,paint);

        //绘制贝塞尔曲线
        canvas.drawPath(path,paint);

    }

    private boolean downEnable=false;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(event.getX()-pointAX<=100&&event.getY()-pointAY<=100){
                    downEnable=true;
                    pointCX=event.getX();
                    pointCY=event.getY();
                    postInvalidate();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if(downEnable){
                    pointCX=event.getX();
                    pointCY=event.getY();
                    Calculation(pointAX,pointAY,pointCX,pointCY);
                    postInvalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                downEnable=false;
                pointCX=pointAX;
                pointCY=pointAY;
                path.reset();
                postInvalidate();
                break;
        }

        return true;
    }



    private void Calculation(float pax,float pay,float pbx,float pby){
        double a=  Math.atan((pbx-pax)/(pby-pay));
        double sin=Math.sin(a);
        double cos=Math.cos(a);
        p1.Y= (float) (pay+ (sin*100));
        p1.X= (float) (pax- (cos*100));

        p2.X= (float) (pax+cos*100);
        p2.Y= (float) (pay-sin*100);

        p3.X= (float) (pbx-cos*100);
        p3.Y= (float) (pby+sin*100);

        p4.X= (float) (pbx+cos*100);
        p4.Y= (float) (pby-sin*100);

        p5.X=(pax+pbx)/2;
        p5.Y=(pay+pby)/2;

        circleDistance=Math.abs(Math.sqrt(Math.pow(pax-pbx,2)+Math.pow(pay-pby,2)));

        path.reset();
        if(circleDistance>maxDistance){

        }else {
            path.moveTo(p1.X,p1.Y);
            path.quadTo(p5.X,p5.Y,p3.X,p3.Y);
            path.lineTo(p4.X,p4.Y);
            path.quadTo(p5.X,p5.Y,p2.X,p2.Y);
            path.lineTo(p1.X,p1.Y);
        }
    }
}
