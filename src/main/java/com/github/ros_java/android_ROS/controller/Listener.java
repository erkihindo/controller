package com.github.ros_java.android_ROS.controller;

import android.util.Log;

import org.ros.android.MessageCallable;
import org.ros.android.RosActivity;
import org.ros.android.view.RosTextView;

import java.util.List;

//My custom messages
import msgs.Ball;
import msgs.ImageData;

/**
 * Created by erki on 5/1/16.
 */
public class Listener{
    RosActivity app;

    public RosTextView<ImageData> rosTextView;
    private String displayed_message;
    private double lastChanceToKick;
    private double shootingRange;



    public Listener(RosActivity c) {
        this.app = c;
        defineTextViews();
    }

    public void defineTextViews() {
        rosTextView = (RosTextView<ImageData>) app.findViewById(R.id.text);
        rosTextView.setTopicName(app.getString(R.string.sub_data_topic));

        rosTextView.setMessageType(ImageData._TYPE);
        displayed_message = "";
        lastChanceToKick = 5000;


        rosTextView.setMessageToStringCallable(new MessageCallable<String, ImageData>() {
            @Override
            public String call(ImageData message) {
                List<Ball> pallid = message.getBalls();
                float smallest_length = 500;

                for (int i = 0; i < pallid.size(); i++) {
                    if (pallid.get(i).getDistance() < smallest_length) {
                        smallest_length = pallid.get(i).getDistance();

                    }
                }


                if (smallest_length < Double.parseDouble(app.getString(R.string.kick_range))) {
                    displayed_message = "KICK";
                    lastChanceToKick = System.currentTimeMillis();
                } else {
                    hideMessageDelay();
                }



                return displayed_message;
            }
        });
    }
    //Hides the displayed message after a sec
    public void hideMessageDelay() {
        if(System.currentTimeMillis() - lastChanceToKick > 1000) {
            Log.d("Time", "" + (System.currentTimeMillis() - lastChanceToKick));
            displayed_message = "";
        }
    }

}
