
package com.github.ros_java.android_ROS.controller;

import android.content.Context;
import android.util.Log;

import org.ros.concurrent.CancellableLoop;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Publisher;

import java.util.ArrayList;
import java.util.List;

import msgs.Cmd;
import std_msgs.Bool;
import std_msgs.Float32;
import std_msgs.Int32;


//Custom messages


public class Publisherr extends AbstractNodeMain {
    private String topic_name;
    Context app;
    private String msg_type;
    List<String> datas;
    int waitTime= 100;



    public Publisherr(Context c, String topic) {
        this.app = c;
        this.topic_name = topic;
    }
    public void setWaitTime(String time) {
        this.waitTime = Integer.parseInt(time);
    }

    public void setMsgType(String msg_type) {
        this.msg_type = msg_type;
    }
    public void setDatas(List<String> dataList) {
        this.datas = new ArrayList<String>();
        this.datas.addAll(dataList);



    }

    public GraphName getDefaultNodeName() {
        return GraphName.of("talker");
    }

    public void onStart(ConnectedNode connectedNode) {
        Log.i("Publisher started", topic_name + " " + msg_type);
        final Publisher publisher = connectedNode.newPublisher(this.topic_name, this.msg_type);

        connectedNode.executeCancellableLoop(new CancellableLoop() {
            protected void loop() throws InterruptedException {
                //compose and send off message
                chooseAndSendMessage(publisher);

                Thread.sleep(waitTime);
            }
        });
    }
    private void chooseAndSendMessage(Publisher publisher) {
        if(msg_type.equals("msgs/Cmd"))
            publisher.publish(createCmdMessage(publisher));
        else if(msg_type.equals("std_msgs/Bool"))
            publisher.publish(createBoolMessage(publisher));
        else if(msg_type.equals("std_msgs/Int32"))
            publisher.publish(createIntMessage(publisher));
        else if(msg_type.equals("std_msgs/String"))
            publisher.publish(createStringMessage(publisher));
        else if(msg_type.equals("std_msgs/Float32"))
            publisher.publish(createFloatMessage(publisher));
        else if(msg_type.equals("std_msgs/Byte"))
            publisher.publish(createByteMessage(publisher));
        else if(msg_type.equals("std_msgs/Char"))
            publisher.publish(createCharMessage(publisher));
    }

    public Cmd createCmdMessage(Publisher publisher) {
        if(datas.size() < 5) {
            datas.add("");
            return createCmdMessage(publisher);
        }
        Cmd msg = (Cmd)publisher.newMessage();
        msg.setAbsSpeed(InputManager.getFloat(datas.get(0)));
        msg.setRadAngle(InputManager.getFloat(datas.get(1)));
        msg.setTurnRate(InputManager.getFloat(datas.get(2)));
        msg.setKick(InputManager.getBool(datas.get(3)));
        msg.setText(InputManager.getText(datas.get(4)));
        return msg;
    }

    public Bool createBoolMessage(Publisher publisher) {
        if(datas.size() < 1) {
            datas.add("");
            return createBoolMessage(publisher);
        }
        Bool msg = (Bool)publisher.newMessage();
        msg.setData(InputManager.getBool(datas.get(0)));
        return msg;
    }

    public Int32 createIntMessage(Publisher publisher) {
        if(datas.size() < 1) {
            datas.add("");
            return createIntMessage(publisher);
        }
        Int32 msg = (Int32)publisher.newMessage();
        msg.setData(InputManager.getInt(datas.get(0)));
        return msg;
    }

    public std_msgs.String createStringMessage(Publisher publisher) {
        if(datas.size() < 1) {
            datas.add("");
            return createStringMessage(publisher);
        }
        std_msgs.String msg = (std_msgs.String)publisher.newMessage();
        msg.setData(InputManager.getText(datas.get(0)));
        return msg;
    }

    public Float32 createFloatMessage(Publisher publisher) {
        if(datas.size() < 1) {
            datas.add("");
            return createFloatMessage(publisher);
        }
        Float32 msg = (Float32)publisher.newMessage();
        msg.setData(InputManager.getFloat(datas.get(0)));
        return msg;
    }

    public std_msgs.Byte createByteMessage(Publisher publisher) {
        if(datas.size() < 1) {
            datas.add("");
            return createByteMessage(publisher);
        }
        std_msgs.Byte msg = (std_msgs.Byte)publisher.newMessage();
        msg.setData(InputManager.getByte(datas.get(0)));
        return msg;
    }

    public std_msgs.Char createCharMessage(Publisher publisher) {
        if(datas.size() < 1) {
            datas.add("");
            return createCharMessage(publisher);
        }
        std_msgs.Char msg = (std_msgs.Char)publisher.newMessage();
        msg.setData(InputManager.getByte(datas.get(0)));
        return msg;
    }

}
