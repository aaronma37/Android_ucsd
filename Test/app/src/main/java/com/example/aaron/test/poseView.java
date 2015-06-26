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
        import geometry_msgs.Point;


        import org.apache.commons.logging.Log;


/**
 * @author damonkohler@google.com (Damon Kohler)
 */
public class poseView<T> implements NodeMain {

    public String topicName;
    public String messageType;
    public double x,y,z,k,w;
    public Pose pose;
    public Point p;
    //private MessageCallable<String, T> callable;

    public poseView() {
        this.y=1;
        this.z=1;
        this.x=1;
        this.k=1;
        this.w=1;
        messageType= geometry_msgs.Pose._TYPE;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
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
    public double getZ(){
        return z;
    }
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
        System.out.println("one");
        Subscriber<geometry_msgs.Pose> subscriber = connectedNode.newSubscriber("all_positions", messageType);

        subscriber.addMessageListener(new MessageListener<geometry_msgs.Pose>() {
            @Override
            public void onNewMessage(geometry_msgs.Pose pose) {

                //System.out.println("found"+pose.getPosition().getX());
                //x=pose.getPosition().getX();
                y=pose.getPosition().getY();
                z=pose.getPosition().getZ();
                p=pose.getPosition();
                x=(pose.getPosition().getX());
                w=pose.getOrientation().getW();
                k=pose.getOrientation().getZ();
                /*y=pose.getPosition().getY();
                z=pose.getPosition().getZ();*/

               /* setOrientation(pose.getOrientation());
                setPosition(pose.getPosition());*/

                /*if (callable != null) {
                    post(new Runnable() {
                        @Override
                        public void run() {
                            setOrientation(callable.call(message));
                            setPosition(callable.call(message));
                        }
                    });
                } else {
                    post(new Runnable() {
                        @Override
                        public void run() {
                            set(message.toString());
                        }
                    });
                }
                postInvalidate();*/
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