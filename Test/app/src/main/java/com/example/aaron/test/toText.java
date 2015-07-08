package com.example.aaron.test;

/**
 * Created by aaron on 7/7/15.
 */
public class toText {
    private float xGl=0;
    private float yGl=0;
    String text;
    int font=0;

    public toText(){
        xGl=0;
        yGl=0;
        font=0;
        text= "empty text>>>";
    }

    public toText(float x, float y, int j, String s){
        xGl=x;
        yGl=y;
        font=j;
        text=s;
    }

    public float getxGl(){
        return xGl;
    }
    public float getyGl(){
        return yGl;
    }
    public int getFont(){
        return font;
    }
    public String getText(){
        return text;
    }

    // SET VALUES

    public void setxGl(float x){
        xGl=x;
    }
    public void setyGl(float y){
        yGl=y;
    }
    public void setFont(int j){
        font=j;
    }
    public void setText(String s){
        text=s;
    }
}
