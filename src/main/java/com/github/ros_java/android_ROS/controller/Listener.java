package com.github.ros_java.android_ROS.controller;

import android.app.Activity;
import android.util.Log;

import org.ros.android.MessageCallable;
import org.ros.android.RosActivity;
import org.ros.android.view.RosTextView;

import java.util.List;

//My custom messages
import msgs.Ball;
import msgs.ImageData;
import std_msgs.Duration;
import std_msgs.Float32;
import std_msgs.Int32;
import std_msgs.String;
import std_msgs.Time;

/**
 * Created by erki on 5/1/16.
 */
public class Listener{
    static Activity app;

    public RosTextView<ImageData> rosTextViewImageData;
    public RosTextView<std_msgs.String> rosTextViewString;
    public RosTextView<std_msgs.Bool> rosTextViewBool;
    public RosTextView<std_msgs.Byte> rosTextViewByte;
    public RosTextView<std_msgs.Int32> rosTextViewInt32;
    public RosTextView<std_msgs.Float32> rosTextViewFloat32;
    public RosTextView<std_msgs.Duration> rosTextViewDuration;
    public RosTextView<std_msgs.Time> rosTextViewTime;

    private java.lang.String displayed_message;
    private double lastTimeKicked;
    public java.lang.String msg_type;




    public Listener(Activity c) {
        this.app = c;

    }






    public void setMsgTyp(java.lang.String msgTyp) {
        this.msg_type = msgTyp;
        if(msgTyp.equals("msgs/ImageData")) {
            rosTextViewImageData = (RosTextView<ImageData>) app.findViewById(R.id.text);
            listenForImageData();
        } else if(msgTyp.equals("std_msgs/String")) {
            rosTextViewString = (RosTextView<std_msgs.String>) app.findViewById(R.id.text);
            listenForString();
        } else if(msgTyp.equals("std_msgs/Bool")) {
            rosTextViewBool = (RosTextView<std_msgs.Bool>) app.findViewById(R.id.text);
            listenForBool();
        } else if(msgTyp.equals("std_msgs/Byte")) {
            rosTextViewByte = (RosTextView<std_msgs.Byte>) app.findViewById(R.id.text);
            listenForByte();
        } else if(msgTyp.equals("std_msgs/Int32")) {
            rosTextViewInt32 = (RosTextView<std_msgs.Int32>) app.findViewById(R.id.text);
            listenForInt32();
        } else if(msgTyp.equals("std_msgs/Float32")) {
            rosTextViewFloat32 = (RosTextView<std_msgs.Float32>) app.findViewById(R.id.text);
            listenForFloat32();
        } else if(msgTyp.equals("std_msgs/Duration")) {
            rosTextViewDuration = (RosTextView<std_msgs.Duration>) app.findViewById(R.id.text);
            listenForDuration();
        } else if(msgTyp.equals("std_msgs/Time")) {
            rosTextViewTime = (RosTextView<std_msgs.Time>) app.findViewById(R.id.text);
            listenForTime();
        }
    }

    public void setTopic(java.lang.String newTop) {

        getCurrentRTV().setTopicName(newTop);
    }

    public RosTextView getCurrentRTV() {
        if(this.msg_type.equals("msgs/ImageData")) {
            return rosTextViewImageData;
        } else if(this.msg_type.equals("std_msgs/String")) {
            return rosTextViewString;
        } else if(this.msg_type.equals("std_msgs/Bool")) {
            return rosTextViewBool;
        }else if(this.msg_type.equals("std_msgs/Byte")) {
            return rosTextViewByte;
        }else if(this.msg_type.equals("std_msgs/Int32")) {
            return rosTextViewInt32;
        }else if(this.msg_type.equals("std_msgs/Float32")) {
            return rosTextViewFloat32;
        }
        Log.i("returnin null", "" + this.msg_type);
        return null;
    }


    //Hides the displayed message after a sec
    public void hideMessageDelay() {
        if(System.currentTimeMillis() - lastTimeKicked > 1000) {
            Log.d("Time", "" + (System.currentTimeMillis() - lastTimeKicked));
            displayed_message = "";
        }
    }

    private void listenForImageData() {
        rosTextViewImageData.setMessageType(ImageData._TYPE);
        displayed_message = "";
        lastTimeKicked = 5000;


        rosTextViewImageData.setMessageToStringCallable(new MessageCallable<java.lang.String, ImageData>() {
            @Override
            public java.lang.String call(ImageData message) {
                List<Ball> pallid = message.getBalls();
                float smallest_length = 500;

                for (int i = 0; i < pallid.size(); i++) {
                    if (pallid.get(i).getDistance() < smallest_length) {
                        smallest_length = pallid.get(i).getDistance();

                    }
                }
                Log.i("Got message", java.lang.String.valueOf(smallest_length));

                if (smallest_length < Double.parseDouble(app.getString(R.string.kick_range))) {
                    displayed_message = "KICK";
                    lastTimeKicked = System.currentTimeMillis();
                } else {
                    hideMessageDelay();
                }



                return displayed_message;
            }
        });
    }

    private void listenForString() {
        rosTextViewString.setMessageType(std_msgs.String._TYPE);
        rosTextViewString.setMessageToStringCallable(new MessageCallable<java.lang.String, std_msgs.String>() {
            @Override
            public java.lang.String call(std_msgs.String message) {

                return message.getData();
            }
        });
    }

    private void listenForBool() {
        rosTextViewBool.setMessageType(std_msgs.Bool._TYPE);
        rosTextViewBool.setMessageToStringCallable(new MessageCallable<java.lang.String, std_msgs.Bool>() {
            @Override
            public java.lang.String call(std_msgs.Bool message) {

                return java.lang.String.valueOf(message.getData());
            }
        });
    }

    private void listenForByte() {
        rosTextViewByte.setMessageType(std_msgs.Byte._TYPE);
        rosTextViewByte.setMessageToStringCallable(new MessageCallable<java.lang.String, std_msgs.Byte>() {
            @Override
            public java.lang.String call(std_msgs.Byte message) {

                return java.lang.String.valueOf(message.getData());
            }
        });
    }

    private void listenForInt32() {
        rosTextViewInt32.setMessageType(std_msgs.Int32._TYPE);
        rosTextViewInt32.setMessageToStringCallable(new MessageCallable<java.lang.String, Int32>() {
            @Override
            public java.lang.String call(std_msgs.Int32 message) {

                return java.lang.String.valueOf(message.getData());
            }
        });
    }

    private void listenForFloat32() {
        rosTextViewFloat32.setMessageType(std_msgs.Float32._TYPE);
        rosTextViewFloat32.setMessageToStringCallable(new MessageCallable<java.lang.String, Float32>() {
            @Override
            public java.lang.String call(std_msgs.Float32 message) {

                return java.lang.String.valueOf(message.getData());
            }
        });
    }
    private void listenForDuration() {
        rosTextViewDuration.setMessageType(std_msgs.Duration._TYPE);
        rosTextViewDuration.setMessageToStringCallable(new MessageCallable<java.lang.String, Duration>() {
            @Override
            public java.lang.String call(std_msgs.Duration message) {

                return java.lang.String.valueOf(message.getData());
            }
        });
    }
    private void listenForTime() {
        rosTextViewTime.setMessageType(std_msgs.Time._TYPE);
        rosTextViewTime.setMessageToStringCallable(new MessageCallable<java.lang.String, Time>() {
            @Override
            public java.lang.String call(std_msgs.Time message) {

                return java.lang.String.valueOf(message.getData());
            }
        });
    }

}
