package com.example.aaron.test;

public class turtle {
    public float x=0;
    public float y=0;
    public float z=0;
    public float rot=0;
    public float data[]={0,0,0};
    public float nameID=0;
    public int state=0;
    public int on=0;
    private String identification;

    public turtle() {
        x=0;y=0;rot=0;z=0;state=0;on=0;
    }

    public int getState(){return state;}

    public String getIdentification(){
        return identification;
    }

    public void setState(int i){state=i;}

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getRot() {
        return rot;
    }

    public int getOn(){
        return on;
    }

    public void setOn(int s){
        on=s;
    }

    public float[] getData(){
        float data[]={x,y,z,rot,nameID, state,on};
        return data;
    }


    public void setData(float[] f, String s) {
        x=f[0];
        y=f[1];
        z=f[2];
        rot=f[3];
        nameID=f[4];
        state=(int)f[5];
        on=(int)f[6];
        identification=s;
    }
}