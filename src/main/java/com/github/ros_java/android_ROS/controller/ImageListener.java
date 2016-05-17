package com.github.ros_java.android_ROS.controller;

import android.app.Activity;

import org.ros.android.BitmapFromCompressedImage;
import org.ros.android.RosActivity;

import sensor_msgs.CompressedImage;

/**
 * Created by viki on 5/16/16.
 */
public class ImageListener {
    public static RosImageView2<CompressedImage> rosImageView;
    static Activity app;
    public ImageListener(RosActivity app) {
        this.app = app;
    }


    public static void defineImageViews(String topic) {
        rosImageView = (RosImageView2<sensor_msgs.CompressedImage>)app.findViewById(R.id.image);
        rosImageView.setMessageType(sensor_msgs.CompressedImage._TYPE);
        //rosImageView.setTopicName(app.getResources().getString(R.string.sub_image_topic));
        rosImageView.setTopicName(topic);
        rosImageView.setMessageToBitmapCallable(new BitmapFromCompressedImage());
    }
}
