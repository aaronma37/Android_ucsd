package com.example.aaron.test;

/*
 * Copyright (C) 2011 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

        import android.content.Context;
        import android.util.AttributeSet;
        import android.widget.TextView;
        import org.ros.android.MessageCallable;
        import org.ros.message.MessageListener;
        import org.ros.namespace.GraphName;
        import org.ros.node.ConnectedNode;
        import org.ros.node.Node;
        import org.ros.node.NodeMain;
        import org.ros.node.topic.Subscriber;
        import geometry_msgs.Pose;
        import geometry_msgs.PoseStamped;
        import geometry_msgs.Point;



        import org.apache.commons.logging.Log;


/**
 * @author damonkohler@google.com (Damon Kohler)
 */
public class poseView<T> implements NodeMain {

    public String topicName;
    public String messageType,frame_id;
    public double x,y,z,k,w,id;
    public PoseStamped pose;
    public float poseData[]={0,0,0,0,0,0,0};
    public Point p;
    public turtle turtleList[]=new turtle[10];
    //private MessageCallable<String, T> callable;

    public poseView() {
        this.y=1;
        this.z=1;
        this.x=1;
        this.k=1;
        this.w=1;
        messageType= geometry_msgs.PoseStamped._TYPE;
        for (int i=0;i<10;i++){
            turtleList[i]=new turtle();
        }
    }

   public turtle[] getTurtles(){
       return turtleList;
   }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public double getX(){
        return x;
    }
    public double getY(){
        return y;
    }
    public float[] getPoseData(){return poseData;}
    public double getZ(){
        return z;
    }
    public double getID(){return id;}
    public double getYaw(){return Math.atan2(2*(w*k),(w*w-k*k))*57.2957795;}
    public Point getP(){return p;}
    public void setX(double xx) {
    x=xx;
    }
    public void setY(double xx) {
        x=xx;
    }
    /*public void setMessageToStringCallable(MessageCallable<String, T> callable) {
        this.callable = callable;
    }*/

    @Override
    public GraphName getDefaultNodeName() {
        return GraphName.of("poseFinder");
    }

    @Override
    public void onStart(ConnectedNode connectedNode) {
        x=-3;
        Subscriber<geometry_msgs.PoseStamped> subscriber = connectedNode.newSubscriber("all_positions", messageType);

        subscriber.addMessageListener(new MessageListener<PoseStamped>() {
            @Override
            public void onNewMessage(geometry_msgs.PoseStamped pose) {

                //System.out.println("found"+pose.getPosition().getX());
                //x=pose.getPosition().getX();
                y=pose.getPose().getPosition().getY();
                z=pose.getPose().getPosition().getZ();
                p=pose.getPose().getPosition();
                x=(pose.getPose().getPosition().getX());
                w=pose.getPose().getOrientation().getW();
                k=pose.getPose().getOrientation().getZ();
                id=(double)pose.getHeader().getSeq();
                frame_id=pose.getHeader().getFrameId();
                poseData[0]=(float)x;
                poseData[1]=(float)y;
                poseData[2]=(float)z;
                poseData[3]=(float)(Math.atan2(2*(w*k),(w*w-k*k))*57.2957795);
                poseData[5]=0;
                if (frame_id.equals("Bob")){poseData[4]=0;}else if(frame_id.equals("Frank")){poseData[4]=1;}
                else if(frame_id.equals("Eric")){poseData[4]=4;}
                else if(frame_id.equals("Aaron")){poseData[4]=3;}
                else if(frame_id.equals("Richard")){poseData[4]=2;}
                else{poseData[4]=-1;}
                poseData[6]=1;
                turtleList[(int)poseData[4]].setData(poseData,frame_id);


            }
        });
    }

    @Override
    public void onShutdown(Node node) {
    }

    @Override
    public void onShutdownComplete(Node node) {
    }

    @Override
    public void onError(Node node, Throwable throwable) {
    }
}