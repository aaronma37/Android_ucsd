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

        import javax.microedition.khronos.egl.EGLConfig;
        import javax.microedition.khronos.opengles.GL10;

        import android.content.Context;
        import android.opengl.GLES20;
        import android.opengl.GLSurfaceView;
        import android.opengl.Matrix;
        import android.util.Log;

        import java.nio.FloatBuffer;
        import java.util.ArrayList;
        import java.util.Collections;
        import java.util.List;

/**
 * Provides drawing instructions for a GLSurfaceView object. This class
 * must override the OpenGL ES drawing lifecycle methods:
 * <ul>
 *   <li>{@link android.opengl.GLSurfaceView.Renderer#onSurfaceCreated}</li>
 *   <li>{@link android.opengl.GLSurfaceView.Renderer#onDrawFrame}</li>
 *   <li>{@link android.opengl.GLSurfaceView.Renderer#onSurfaceChanged}</li>
 * </ul>
 */
public class MyGLRenderer implements GLSurfaceView.Renderer {

    private static final String TAG = "MyGLRenderer";
    private Triangle mTriangle, robot;
    private Square   mSquare, mArena, mArena2;
    private Square vLine[] = new Square[25];
    private Square fLine[] = new Square[100];
    private turtB turt1;
    private target tar;
    private gauss density;
    //private ArrayList<textclass> textSystem= new ArrayList<textclass>();
    private textclass textSystem;
    private toggles vorToggle, freeDrawToggle;
    private float textPosition[]= {-.95f, .5f};
    public ArrayList<toText> textList = new ArrayList<toText>();
    private FloatBuffer textureBuffer;
    public Context context;

    private int vToggle=0;
    private int fToggle=0;
    private int vSize=0;
    private int fSize=0;
    private float texture[] = {
            0.0f, 1.0f,     // top left     (V2)
            0.0f, 0.0f,     // bottom left  (V1)
            1.0f, 1.0f,     // top right    (V4)
            1.0f, 0.0f      // bottom right (V3)
    };

    // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private final float[] mRotationMatrix = new float[16];
    float poseData[]={0,0,0,0,0};
    public turtle turtleList[]= new turtle[10];
    private float tempX;
    private float tempY;
    private float width,height;

    private float mAngle;

    public MyGLRenderer(Context context1,float f[], turtle t[],float w, float h){
        width=w;
        height=h;
        poseData=f;context=context1;
    for (int i=0;i<10;i++){
        turtleList[i]=new turtle();
        if (t[i]!=null){
        turtleList[i].setData(t[i].getData(), t[i].getIdentification());}
    }


    }

    public void updateRen(turtle t[]){
        for (int i=0;i<10;i++){
            if (t[i]!=null){
                turtleList[i].setData(t[i].getData(), t[i].getIdentification());}
        }
    }

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {

        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        mTriangle = new Triangle();

        float sTemp[] = {
                -0.5f,  0.5f, 0.0f,   // top left
                -0.5f, -0.5f, 0.0f,   // bottom left
                0.5f, -0.5f, 0.0f,   // bottom right
                0.5f,  0.5f, 0.0f };
        float c[] = { 0f,255f,255f, 1.0f };
        mSquare   = new Square();
        sTemp[0]=-width/height;sTemp[1]=height/height;
        sTemp[3]=-width/height;;sTemp[4]=-height/(height*2);
        sTemp[6]=width/height;;sTemp[7]=-height/(height*2);
        sTemp[9]=width/height;sTemp[10]=height/height;
        mArena  = new Square(sTemp);
        sTemp[0]=-(width-100)/height;sTemp[1]=(height-5)/height;
        sTemp[3]=-(width-100)/height;sTemp[4]=-(height-10)/(height*2);
        sTemp[6]=(width-100)/height;;sTemp[7]=-(height-10)/(height*2);
        sTemp[9]=(width-100)/height;sTemp[10]=(height-5)/height;

        c[0]=0;c[1]=0;c[2]=0;c[3]=1.0f;

        mArena2  = new Square(sTemp);
        mArena2.setColor(c);
        robot = new Triangle();

        turt1 = new turtB(context);
        tar =new target(context);
        //density = new gauss(context);

        sTemp[0]=-(width-100)/height;sTemp[1]=0;
        sTemp[3]=-(width-100)/height;sTemp[4]=-.01f;
        sTemp[6]=(width-100)/height;;sTemp[7]=-.01f;
        sTemp[9]=(width-100)/height;sTemp[10]=0;

        for (int i=0;i<25;i++) {
            vLine[i] = new Square(sTemp);
        }

        for (int i=0;i<100;i++) {
            fLine[i] = new Square(sTemp);
        }

        float spriteCoords[] = {
                -0.05f,  0.05f,   // top left
                -0.05f, -0.05f,   // bottom left
                0.05f, -0.05f,   // bottom right
                0.05f,  0.05f}; //top right

        spriteCoords[0]=(width-90)/height;spriteCoords[1]=-(height-10)/(height*2);
        spriteCoords[2]=(width-90)/height;spriteCoords[3]=-(height-10)/(height*2)-.1f;
        spriteCoords[4]=(width-90)/height-.1f;spriteCoords[5]=-(height-10)/(height*2)-.1f;
        spriteCoords[6]=(width-90)/height-.1f;spriteCoords[7]=-(height-10)/(height*2);
        vorToggle = new toggles(context,spriteCoords,0);

        spriteCoords[0]=(width-90)/height-.11f;spriteCoords[1]=-(height-10)/(height*2);
        spriteCoords[2]=(width-90)/height-.11f;spriteCoords[3]=-(height-10)/(height*2)-.1f;
        spriteCoords[4]=(width-90)/height-.21f;spriteCoords[5]=-(height-10)/(height*2)-.1f;
        spriteCoords[6]=(width-90)/height-.21f;spriteCoords[7]=-(height-10)/(height*2);
        freeDrawToggle = new toggles(context, spriteCoords,1);

        textSystem = new textclass(context, "A");
        textList.add(new toText(-.95f,.5f,0,"UCSD Distributed Robotics Lab"));
        textList.add(new toText(-.55f,-.1f,0,"No Robots Selected"));

    }

    public void setVoronoiCoordinates(float s[],int i,int j){
        vLine[i].setSquareCoords(s);
        vSize=j;
    }

    public void setFreeDrawCoordinates(float s[],int i, int j){
        fLine[i].setSquareCoords(s);
        fSize=j;
    }


    public void setPosition(float f[]) {
        poseData = f;
    }

    @Override
    public void onDrawFrame(GL10 unused) {

        float[] scratch = new float[16];
        // Draw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        // Set the camera position (View matrix)
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
        // DRAW OUTLINE
        mArena.draw(mMVPMatrix);
        mArena2.draw(mMVPMatrix);
        // DRAW VORONOI TOGGLE ICON
        vorToggle.Draw(mMVPMatrix,vToggle);
        freeDrawToggle.Draw(mMVPMatrix,fToggle);
        // DRAW TURTLES
        for (int i=0;i<10;i++){
            if (turtleList[i].getOn()==1) {
                Matrix.setRotateM(mRotationMatrix, 0, 0, 0, 0, 1.0f);
                Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mRotationMatrix, 0);
                Matrix.translateM(scratch, 0, turtleList[i].getX(), turtleList[i].getY(), 0);
                Matrix.rotateM(scratch, 0, turtleList[i].getRot(), 0, 0, 1f);
                turt1.Draw(scratch,turtleList[i].getState());
            }
        }

        // DRAW VORONOI LINES
        if (vToggle==1) {
            for (int i = 0; i < vSize; i++) {
                vLine[i].draw(mMVPMatrix);
            }
        }
        if (fToggle==1) {
            for (int i = 0; i < fSize  ; i++) {
                fLine[i].draw(mMVPMatrix);
            }
        }



        /*scratch = new float[16];
        Matrix.setRotateM(mRotationMatrix, 0, mAngle, 0, 0, 1.0f);
        Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mRotationMatrix, 0);
        mTriangle.draw(scratch);*/

        // DRAW TARGET MARK
        Matrix.setRotateM(mRotationMatrix, 0, 0, 0, 0, 1.0f);
        Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mRotationMatrix, 0);
        Matrix.translateM(scratch, 0, tempX, tempY, 0);
        tar.Draw(scratch);

        //START DRAWING TEXT BLOCK
        //
        Matrix.setRotateM(mRotationMatrix, 0, 0, 0, 0, 1.0f);

        int temp = 0;
        for (int j = 0; j<textList.size();j++){
            Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mRotationMatrix, 0);
            Matrix.translateM(scratch, 0, textList.get(j).getyGl(), textList.get(j).getxGl(), 0);
            for (int i = 0; i<textList.get(j).getText().length();i++){
                String s = String.valueOf(textList.get(j).getText().charAt(i));
                if (Character.isUpperCase(textList.get(j).getText().codePointAt(i))==true || s.equals(" ")){
                    if (temp!=0){
                        Matrix.translateM(scratch, 0, -.01f, 0f, 0);
                    }
                    textSystem.Draw(scratch, s, 0);
                    temp++;
                    Matrix.translateM(scratch, 0, -.005f, 0f, 0);
                }
                else{
                    textSystem.Draw(scratch, s, 1);
                    temp = 0;
                }
                Matrix.translateM(scratch, 0, -.015f, 0f, 0);
            }
        }
        //
        //END DRAWING TEXT BLOCK
    }

    public void tempFun(float xx, float yy){
        tempX  =xx;
        tempY  =yy;
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        // Adjust the viewport based on geometry changes,
        // such as screen rotation
        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width / height;

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);

    }

    /**
     * Utility method for compiling a OpenGL shader.
     *
     * <p><strong>Note:</strong> When developing shaders, use the checkGlError()
     * method to debug shader coding errors.</p>
     *
     * @param type - Vertex or fragment shader type.
     * @param shaderCode - String containing the shader code.
     * @return - Returns an id for the shader.
     */
    public static int loadShader(int type, String shaderCode){

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    /**
     * Utility method for debugging OpenGL calls. Provide the name of the call
     * just after making it:
     *
     * <pre>
     * mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
     * MyGLRenderer.checkGlError("glGetUniformLocation");</pre>
     *
     * If the operation is not successful, the check throws an error.
     *
     * @param glOperation - Name of the OpenGL call to check.
     */
    public static void checkGlError(String glOperation) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(TAG, glOperation + ": glError " + error);
            throw new RuntimeException(glOperation + ": glError " + error);
        }
    }

    /**
     * Returns the rotation angle of the triangle shape (mTriangle).
     *
     * @return - A float representing the rotation angle.
     */
    public float getAngle() {
        return mAngle;
    }

    /**
     * Sets the rotation angle of the triangle shape (mTriangle).
     */
    public void setAngle(float angle) {
        mAngle = angle;
    }

    public void setvToggle(int i){
        vToggle=i;
    }

    public int getvToggle(){
        return vToggle;
    }

    public void setfToggle(int i){
        fToggle=i;
    }

    public int getfToggle(){
        return fToggle;
    }

    public void eraseFreeLine(){
        fSize=0;
    }

}