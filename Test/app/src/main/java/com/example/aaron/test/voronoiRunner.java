package com.example.aaron.test;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by aaron on 7/5/15.
 */
public class voronoiRunner {
    int flag = 0;

    public void Run(){
        flag =1;
        final ScheduledThreadPoolExecutor exec2 = new ScheduledThreadPoolExecutor(5);
        exec2.scheduleAtFixedRate(new Runnable() {
            public void run() {
                if (flag==0){
                    exec2.shutdown();
                }
                else{

                }
            }
        }, 0, 100, TimeUnit.MICROSECONDS);
    }

    public void turnOff(){
        flag=0;
    }


}
