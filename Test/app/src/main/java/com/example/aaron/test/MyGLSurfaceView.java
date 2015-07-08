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

        import java.util.ArrayList;
        import java.util.List;
        import java.util.concurrent.ScheduledThreadPoolExecutor;
        import java.util.concurrent.TimeUnit;


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
    public int vFlag=0;
    public int fFlag=0;
    public int antispam=0;
    private int freeDrawCount=0;
    ArrayList<Double> temp= new ArrayList<Double>();
    ArrayList<Double> temp2= new ArrayList<Double>();


    /*private double temp[]=new double[10];
    private double temp2[]=new double[10];*/

    public Voronoi vor;
    private List<GraphEdge> voronoiEdges;
    private int state[]= {0,0,0,0,0,0,0,0,0,0,0};
    public MyGLSurfaceView(Context context, float f[], turtle turtleList[]) {
        super(context);




        vor = new Voronoi(.001);
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
                tList[i].setData(temp, t[i].getIdentification());
            }
        }

        mRenderer.updateRen(tList);
    }

    public float getHeight1(){
        return height1;
    }

    public void tick(){
        antispam=antispam+1;
        int count=0;
        for (int i=0;i<10;i++){
            if (tList[i].getState()==1){
                count++;
                if (count>1){
                    mRenderer.textList.get(1).setText("Multiple Robots Selected");
                }
                else{
                    mRenderer.textList.get(1).setText("Selected: " + tList[i].getIdentification());
                }
            }
        }
        if (count==0){
            mRenderer.textList.get(1).setText("No Robots Selected");
        }
    }

    public float getWidth1(){
        return width1;
    }

    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
    private float mPreviousX;
    private float mPreviousY;
    private float previousx = 0;
    private float previousy = 0;
    public void setR(float f[]){mRenderer.setPosition(f);}

    @Override
    public boolean onTouchEvent(MotionEvent e) {

        float x = e.getX();
        float y = e.getY();

        int cc=0;


        float xGL=(width1/2-x)/(float)(height1/1.85);
        float yGL=( height1/2+85-y)/(float)(height1/1.85);



        mRenderer.tempFun(xGL, yGL);

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

                if (antispam>5) {
                    if (xGL<(width1-90)/height1 && xGL>(width1-90)/height1-.1f && yGL > -(height1-10)/(height1*2)-.1f && yGL < -(height1-10)/(height1*2))
                    if (mRenderer.getvToggle() == 1) {
                        mRenderer.setvToggle(0);
                    } else {
                        mRenderer.setvToggle(1);
                    }
                    vFlag = mRenderer.getvToggle();

                    if (xGL<(width1-90)/height1-.11f && xGL>(width1-90)/height1-.2f && yGL > -(height1-10)/(height1*2)-.1f && yGL < -(height1-10)/(height1*2))
                        if (mRenderer.getfToggle() == 1) {
                            mRenderer.setfToggle(0);
                            mRenderer.eraseFreeLine();
                            freeDrawCount=0;
                            previousy=0;
                            previousx=0;
                        } else {
                            mRenderer.setfToggle(1);
                        }
                    fFlag = mRenderer.getfToggle();
                }








            case MotionEvent.ACTION_MOVE:

                float dx = x - mPreviousX;
                float dy = y - mPreviousY;

                if (fFlag==1 && Math.abs(xGL-previousx)> .01f && Math.abs(yGL -previousy)>.01f && xGL>-(width1-100)/height1
                        && xGL < (width1-100)/height1 && yGL < (height1-5)/height1 && yGL > -(height1-5)/(height1*2)) {
                    if (previousx!=0 && previousy!=0){
                        setFreeDrawCoordinates(xGL,yGL, previousx, previousy);
                    }

                    previousx=xGL;
                    previousy=yGL;
                }

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

        antispam=0;
        return true;
    }

    public void rr(){requestRender();}

    public void setVoronoiCoordinates(){
        float vorCoords[] = {
                -0.5f,  0.5f, 0.0f,   // top left
                -0.5f, -0.5f, 0.0f,   // bottom left
                0.5f, -0.5f, 0.0f,   // bottom right
                0.5f,  0.5f, 0.0f }; // top right

        temp.clear();
        temp2.clear();
        for (int i=0;i<10;i++){
            if (tList[i].getOn()==1) {
                temp.add((double) tList[i].getX());
                temp2.add((double) tList[i].getY());
            }
        }
        if (temp!=null) {

            double[] temp3 = new double[temp.size()];
            double[] temp4 = new double[temp.size()];
            for (int i=0;i<temp.size();i++){
                temp3[i]=temp.get(i);
                temp4[i]=temp2.get(i);
            }

            voronoiEdges = vor.generateVoronoi(temp3, temp4, -width1 / height1, width1 / height1, -height1 / (height1 * 2), height1 / height1);

            for(int i = 0; i < voronoiEdges.size(); i++) {
                double cd = Math.cos(Math.atan((voronoiEdges.get(i).x1 - voronoiEdges.get(i).x2) / (voronoiEdges.get(i).y1 - voronoiEdges.get(i).y2)));
                double cy = Math.sin(Math.atan((voronoiEdges.get(i).x1 - voronoiEdges.get(i).x2) / (voronoiEdges.get(i).y1 - voronoiEdges.get(i).y2)));

                vorCoords[0] = (float) voronoiEdges.get(i).x1 + (float) cd * .005f;
                vorCoords[1] = (float) voronoiEdges.get(i).y1 - (float) cy * .005f;

                vorCoords[9] = (float) voronoiEdges.get(i).x2 + (float) cd * .005f;
                vorCoords[10] = (float) voronoiEdges.get(i).y2 - (float) cy * .005f;


                vorCoords[3] = (float) voronoiEdges.get(i).x1 - (float) cd * .005f;
                vorCoords[4] = (float) voronoiEdges.get(i).y1 + (float) cy * .005f;

                vorCoords[6] = (float) voronoiEdges.get(i).x2 - (float) cd * .005f;
                vorCoords[7] = (float) voronoiEdges.get(i).y2 + (float) cy * .005f;

                mRenderer.setVoronoiCoordinates(vorCoords, i, voronoiEdges.size());
            }
        }
    }


    public void setFreeDrawCoordinates(float x, float y, float xp, float yp){
        /*xp=(width1/2-xp)/(float)(height1/1.85);
        yp=(height1/2+85-yp)/(float)(height1/1.85);*/

        float Coords[] = {
                -0.5f,  0.5f, 0.0f,   // top left
                -0.5f, -0.5f, 0.0f,   // bottom left
                0.5f, -0.5f, 0.0f,   // bottom right
                0.5f,  0.5f, 0.0f }; // top right

                double cd = Math.cos(Math.atan((x - xp) / (y - yp)));
                double cy = Math.sin(Math.atan((x - xp) / (y - yp)));

                Coords[0] = x + (float) cd * .005f;
                Coords[1] = y - (float) cy * .005f;

                Coords[9] = xp + (float) cd * .005f;
                Coords[10] =  yp - (float) cy * .005f;

                Coords[3] = x - (float) cd * .005f;
                Coords[4] = y + (float) cy * .005f;

                Coords[6] = xp - (float) cd * .005f;
                Coords[7] = yp + (float) cy * .005f;
        freeDrawCount++;
                if (freeDrawCount<100){
                    mRenderer.setFreeDrawCoordinates(Coords,freeDrawCount-1,freeDrawCount);
                }

    }
}

