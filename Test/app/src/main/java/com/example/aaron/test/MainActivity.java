package com.example.aaron.test;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import com.example.aaron.simplevoronoi.src.main.java.be.humphreys.simplevoronoi.*;
import org.ros.address.InetAddressFactory;
import org.ros.android.MessageCallable;
import org.ros.android.RosActivity;
import org.ros.android.view.RosTextView;
import org.ros.node.ConnectedNode;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMainExecutor;
import geometry_msgs.Point;
import geometry_msgs.Pose;

import com.example.aaron.simplevoronoi.src.main.java.be.humphreys.simplevoronoi.Voronoi;
import com.example.aaron.test.Talker;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
//import org.ros.rosjava_tutorial_pubsub.Talker;


/**
 * @author damonkohler@google.com (Damon Kohler)
 */
public class MainActivity extends RosActivity {

    private RosTextView<std_msgs.String> rosTextView;
    private Talker talker;
    private poseView poseview;
    private MyGLSurfaceView mGLView;
    private float width1,height1;
    private int flag=0;
    public Intent intent;
    public Voronoi vor;
    public double p[];
    public float pos[]={0,0,0,0,0};
    public float poseData[]={0,0,0,0,0};
    public turtle[] turtleList=new turtle[10];
    public View decorView;
    private int currentApiVersion;
    //public turtle turt;
    TextView poseX;
    private List<GraphEdge> voronoiEdges;
    ArrayList<Double> temp= new ArrayList<Double>();
    ArrayList<Double> temp2= new ArrayList<Double>();




    public MainActivity() {
        // The RosActivity constructor configures the notification title and ticker
        // messages.
        super("Pubsub Tutorial", "Pubsub Tutorial");
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        currentApiVersion = android.os.Build.VERSION.SDK_INT;
        // This work only for android 4.4+


        if(currentApiVersion >= Build.VERSION_CODES.KITKAT)
        {

            getWindow().getDecorView().setSystemUiVisibility(flags);

            // Code below is to handle presses of Volume up or Volume down.
            // Without this, after pressing volume buttons, the navigation bar will
            // show up and won't hide
            final View decorView = getWindow().getDecorView();
            decorView
                    .setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener()
                    {

                        @Override
                        public void onSystemUiVisibilityChange(int visibility)
                        {
                            if((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0)
                            {
                                decorView.setSystemUiVisibility(flags);
                            }
                        }
                    });
        }



        setContentView(R.layout.activity_main);
        View decorView = getWindow().getDecorView();



       // poseview.setTopicName("all_positions");
        rosTextView = (RosTextView<std_msgs.String>) findViewById(R.id.text);
        rosTextView.setTopicName("chatter");
        rosTextView.setMessageType(std_msgs.String._TYPE);
        //std_msgs.String message = "Aaron Ma";
        rosTextView.setMessageToStringCallable(new MessageCallable<String, std_msgs.String>() {
            @Override
            public String call(std_msgs.String message) {
                return message.getData();
            }
        });
        /*final ConnectedNode connectedNode;
        final Publisher<std_msgs.String> publisher =
                connectedNode.newPublisher("Aaron", std_msgs.String._TYPE);
        std_msgs.String str = publisher.newMessage();
        str.setData("AaronMa");
        publisher.publish(str);*/






            /*Bundle extras = getIntent().getExtras();
            if (extras != null) {
                String value = extras.getString("new_variable_name");
            }
            float pos[]= extras.getFloatArray("position");*/


            // Create a GLSurfaceView instance and set it
            // as the ContentView for this Activity.

        mGLView = new MyGLSurfaceView(this, pos,turtleList);
        setContentView(mGLView);
        for (int i=0;i<10;i++){
            turtleList[i]=new turtle();
        }

    }

    @SuppressLint("NewApi")
    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);
        if(currentApiVersion >= Build.VERSION_CODES.KITKAT && hasFocus)
        {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    @Override
    protected void init(NodeMainExecutor nodeMainExecutor) {
        double num=1;
        talker = new Talker(num);
        poseview = new poseView();
        vor = new Voronoi(.001);



        //poseview.setMessageType(std_msgs.String._TYPE);
        /*Pose x= poseimporter.turt.getPose();
        String xString = String.valueOf(x.getPosition().getX());
*/






        //NodeConfiguration nodeConfiguration = NodeConfiguration.newPrivate();
        NodeConfiguration nodeConfiguration = NodeConfiguration.newPublic(InetAddressFactory.newNonLoopback().getHostAddress().toString(), getMasterUri());
        // At this point, the user has already been prompted to either enter the URI
        // of a master to use or to start a master locally.
        nodeConfiguration.setMasterUri(getMasterUri());
        width1=mGLView.getWidth1();
        height1=mGLView.getHeight1();
        //poseimporter.setTurt(turt);
        // The RosTextView is also a NodeMain that must be executed in order to
        // start displaying incoming messages.
        //nodeMainExecutor.execute(rosTextView, nodeConfiguration);

        //while(num<=10000){

        talker=new Talker(num);
        //nodeMainExecutor.execute(talker, nodeConfiguration);
        //nodeMainExecutor.shutdownNodeMain(talker);
        nodeMainExecutor.execute(poseview, nodeConfiguration);
        num=poseview.getX();
        talker.setNum(num);
        nodeMainExecutor.execute(talker, nodeConfiguration);


        ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(5);
        exec.scheduleAtFixedRate(new Runnable() {
            public void run() {
                turtleList = poseview.getTurtles();
                mGLView.updateRen(turtleList);
            }
        }, 0, 10000, TimeUnit.MICROSECONDS);


        ScheduledThreadPoolExecutor exec2 = new ScheduledThreadPoolExecutor(5);
        exec2.scheduleAtFixedRate(new Runnable() {
            public void run() {
                if (flag==1){
                    mGLView.setVoronoiCoordinates();
                }
                flag=mGLView.vFlag;
                mGLView.tick();
            }
        }, 0, 50000, TimeUnit.MICROSECONDS);


    }
}