package com.example.aaron.test;

import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.widget.TextView;

import org.ros.address.InetAddressFactory;
import org.ros.android.MessageCallable;
import org.ros.android.RosActivity;
import org.ros.android.view.RosTextView;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMainExecutor;
import geometry_msgs.Point;
import geometry_msgs.Pose;
import com.example.aaron.test.Talker;

import java.util.concurrent.TimeUnit;
//import org.ros.rosjava_tutorial_pubsub.Talker;


/**
 * @author damonkohler@google.com (Damon Kohler)
 */
public class MainActivity extends RosActivity {

    private RosTextView<std_msgs.String> rosTextView;
    private Talker talker;
    private poseView poseview;
    private GLSurfaceView mGLView;
    public Intent intent;
    public double p[];
    public float pos[]={0,0,0,0};
    public float poseData[]={0,0,0,0,0};

    //public turtle turt;
    TextView poseX;




    public MainActivity() {
        // The RosActivity constructor configures the notification title and ticker
        // messages.
        super("Pubsub Tutorial", "Pubsub Tutorial");
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        mGLView = new MyGLSurfaceView(this, pos);
        setContentView(mGLView);


    }


    @Override
    protected void init(NodeMainExecutor nodeMainExecutor) {
        double num=1;
        talker = new Talker(num);
        poseview = new poseView();

        //poseview.setMessageType(std_msgs.String._TYPE);
        /*Pose x= poseimporter.turt.getPose();
        String xString = String.valueOf(x.getPosition().getX());
*/






        //NodeConfiguration nodeConfiguration = NodeConfiguration.newPrivate();
        NodeConfiguration nodeConfiguration = NodeConfiguration.newPublic(InetAddressFactory.newNonLoopback().getHostAddress().toString(), getMasterUri());
        // At this point, the user has already been prompted to either enter the URI
        // of a master to use or to start a master locally.
        nodeConfiguration.setMasterUri(getMasterUri());

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

        for (int k=1;k<100000;k++){
            num=poseview.getX();
            double p[]={0,0,0,0};
            p[0]=poseview.getX();
            p[1]=poseview.getY();

            pos[0]=(float)poseview.getX();
            pos[1]=(float)poseview.getY();
            pos[2]=(float)poseview.getZ();
            pos[3]=(float)poseview.getYaw();
            poseData=poseview.getPoseData();
            System.out.println("found"+poseData);
            talker.setP(p);
            try {
                // Wait for 1 second.
                Thread.sleep(1);
            }
            catch (InterruptedException ex) {}
        }

        /*intent = new Intent(MainActivity.this, OpenGLES20Activity.class);
        intent.putExtra("positon", pos);
        startActivity(intent);
        for (int k=1;k<100;k++){
            pos[0]=(float)poseview.getX();
            pos[1]=(float)poseview.getY();
            pos[2]=(float)poseview.getZ();
            intent.putExtra("position", pos);
            //p=poseview.getP();
            try {
                // Wait for 1 second.
                Thread.sleep(100);
            }
            catch (InterruptedException ex) {}
        }*/



        //}



        /*runOnUiThread(new Runnable() {

            double t = poseview.getX();
            String xString = String.valueOf(t);
            TextView poseX = (TextView) findViewById(R.id.poseX);

            public void run() {
                poseX.setText(xString);
            }
        });*/
    }

}