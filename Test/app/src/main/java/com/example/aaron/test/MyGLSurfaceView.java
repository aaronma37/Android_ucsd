package com.example.aaron.test;
/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

        import android.app.Activity;
        import com.example.aaron.simplevoronoi.src.main.java.be.humphreys.simplevoronoi.*;
        import android.content.Context;
        import android.graphics.Point;
        import android.opengl.GLSurfaceView;
        import android.util.DisplayMetrics;
        import android.view.Display;
        import android.view.MotionEvent;
        import android.view.View;
        import android.view.WindowManager;

/**
 * A view container where OpenGL ES graphics can be drawn on screen.
 * This view can also be used to capture touch events, such as a user
 * interacting with drawn objects.
 */
public class MyGLSurfaceView extends GLSurfaceView {

    private final MyGLRenderer mRenderer;
    public float poseData[];
    public turtle tList[]=new turtle[10];
    private float width1;
    private float height1;
    public Voronoi vor;
    private int state[]= {0,0,0,0,0,0,0,0,0,0,0};
    public MyGLSurfaceView(Context context, float f[], turtle turtleList[]) {
        super(context);




        vor = new Voronoi(.01);
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        width1 = metrics.widthPixels;
        height1 = metrics.heightPixels;

        poseData=f;
        // Create an OpenGL ES 2.0 context.
        setEGLContextClientVersion(2);
        for (int i=0;i<10;i++){
            tList[i]=new turtle();
        }

        // Set the Renderer for drawing on the GLSurfaceView
        mRenderer = new MyGLRenderer(context,f, tList,width1,height1);
        //float posTemp[]=f;
        setRenderer(mRenderer);

        // Render the view only when there is a change in the drawing data
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

    }

    public void updateRen(turtle[] t){
        for (int i=0;i<10;i++) {
            if (t[i] != null) {
                float temp[]=t[i].getData();
                temp[5]=state[i];
                tList[i].setData(temp);
            }
        }

        mRenderer.updateRen(tList);
    }


    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
    private float mPreviousX;
    private float mPreviousY;

    public void setR(float f[]){mRenderer.setPosition(f);}

    @Override
    public boolean onTouchEvent(MotionEvent e) {

        float x = e.getX();
        float y = e.getY();
        //mRenderer.setPositon(posTemp);
        int cc=0;


        float xGL=(width1/2-x)/(float)(height1/1.85);
        float yGL=( height1/2+85-y)/(float)(height1/1.85);

        mRenderer.tempFun(xGL, yGL);



        


        vor.generateVoronoi([(double)tList[0].getX() ])


        System.out.println("turtleLocation: "+tList[1].getX());
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                for (int i=0;i<10;i++){
                    if(tList[i].getOn()==1) {
                        cc=cc+1;
                        if (Math.abs(tList[i].getX() - xGL) < .1f && Math.abs(tList[i].getY() - yGL) < .1f) {
                            System.out.println("Turtle Press");
                            if (state[i] == 0) {
                                state[i]=1;
                            } else {
                                state[i]=0;
                            }
                        }
                    }

                }




            case MotionEvent.ACTION_MOVE:

                float dx = x - mPreviousX;
                float dy = y - mPreviousY;

                // reverse direction of rotation above the mid-line
                if (y > getHeight() / 2) {
                    dx = dx * -1 ;
                }

                // reverse direction of rotation to left of the mid-line
                if (x < getWidth() / 2) {
                    dy = dy * -1 ;
                }

                mRenderer.setAngle(
                        mRenderer.getAngle() +
                                ((dx + dy) * TOUCH_SCALE_FACTOR));  // = 180.0f / 320
                requestRender();
        }

        mPreviousX = x;
        mPreviousY = y;
        return true;
    }

    public void rr(){requestRender();}

}