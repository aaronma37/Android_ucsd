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


import org.ros.concurrent.CancellableLoop;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.NodeMain;
import org.ros.node.topic.Publisher;


import geometry_msgs.Point;
import geometry_msgs.Pose;

/**
 * A simple {@link Publisher} {@link NodeMain}.
 *
 * @author damonkohler@google.com (Damon Kohler)
 */
public class Talker extends AbstractNodeMain {

    public double num=5;
    public geometry_msgs.Point p;
    public double x,y;
    public geometry_msgs.Pose str;

    public Talker(double numl){
        num=numl;


    }

    @Override
    public GraphName getDefaultNodeName() {
        return GraphName.of("graphName");
    }


    public void setNum(double numl){
        num=numl;
    }
    public void setP(double f[]){x=f[0];y=f[1];}

    @Override
    public void onStart(final ConnectedNode connectedNode) {

        final Publisher<geometry_msgs.Pose> publisher =
                connectedNode.newPublisher("Aarons", Pose._TYPE);
        geometry_msgs.Pose str = publisher.newMessage();


        // This CancellableLoop will be canceled automatically when the node shuts
        // down.
        connectedNode.executeCancellableLoop(new CancellableLoop() {


            @Override
            protected void setup() {
               //num= 0;
            }

            @Override
            protected void loop() throws InterruptedException {

                geometry_msgs.Pose str = publisher.newMessage();
                p=str.getPosition();
                p.setX(x);p.setY(y);
                str.setPosition(p);
                publisher.publish(str);

                Thread.sleep(10000);

            }
        });
    }
}
