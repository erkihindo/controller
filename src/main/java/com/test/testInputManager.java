package com.test;

import android.test.ActivityTestCase;

import com.github.ros_java.android_ROS.controller.InputManager;

import junit.framework.Assert;

import std_msgs.String;

/**
 * Created by viki on 5/18/16.
 */


public class testInputManager extends ActivityTestCase {

    private InputManager IM;
    public void testLeftJSDistance1(){
        IM = new InputManager();
        IM.leftJoystickData(0, 0);
        Assert.assertTrue(IM.leftJSdistance == 0);

    }

    public void testLeftJSDistance2(){
        IM = new InputManager();
        IM.leftJoystickData(1, 0);
        Assert.assertFalse(IM.leftJSdistance == 0);

    }
    public void testLeftJSxangle(){
        IM = new InputManager();
        IM.leftJoystickData(2, 0);
        Assert.assertFalse(IM.leftJSxangle == 0);

    }

    public void testRightJSDistance1(){
        IM = new InputManager();
        IM.rightJoystickData(0, 0);
        Assert.assertTrue(IM.rightJSdistance == 0);

    }

    public void testRightJSDistance2(){
        IM = new InputManager();
        IM.rightJoystickData(1, 0);
        Assert.assertFalse(IM.rightJSdistance == 0);

    }
    public void testRightJSxangle(){
        IM = new InputManager();
        IM.rightJoystickData(2, 0);
        Assert.assertFalse(IM.rightJSxangle == 0);

    }

    public void testgetFloat() {
        IM = new InputManager();
        IM.enteredText = "5.0";
        assertEquals(5.0, IM.getFloat("enteredText"));
    }

    public void testgetByte() {
        IM = new InputManager();
        IM.leftJSx = 10;
        assertTrue(IM.getByte("leftJSx") == 10);
    }

}
