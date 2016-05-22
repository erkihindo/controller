package com.github.ros_java.android_ROS.controller;

import android.os.Bundle;
import android.util.Log;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import org.ros.android.RosActivity;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMainExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

//Custom messages

public class Controller extends RosActivity implements Thread.UncaughtExceptionHandler
{
    public static GuiReader appsettings;

    //Publisher and talker

    public static List<Publisherr> publisherrList;
    public static List<Listener> listenerList;
    public static ImageListener image;

    double blueLeftX;
    double blueRightX;
    double blueLeftY;
    double blueRightY;


    double bluetoothContrX1;
    double bluetoothContrX2;

    double bluetoothContrY1;
    double bluetoothContrY2;

    private double lastKicked;
    Timer timer;

    public Controller() {
        // The RosActivity constructor configures the notification title.

        super("ROS Controller", "ROS Controller");
    }

    /** Called when the activity is first created. */
    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(this);

        hideTitleBar();

        setContentView(R.layout.main);
        image = new ImageListener((this));
        publisherrList = new ArrayList<Publisherr>();
        listenerList = new ArrayList<Listener>();

        //starts settings manager
        appsettings = new GuiReader(this);


    }


    public void hideTitleBar() {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }




    //Is called when the IP is entered
    @Override
    protected void init(NodeMainExecutor nodeMainExecutor) {
        // At this point, the user has already been prompted to either enter the URI
        // of a master to use or to start a master locally.

        NodeConfiguration nodeConfiguration = NodeConfiguration.newPublic(getRosHostname());
        nodeConfiguration.setMasterUri(getMasterUri());

        //starts subscribers and publishers

        if(listenerList.size() > 0) {

            nodeMainExecutor.execute(listenerList.get(0).getCurrentRTV(), nodeConfiguration);
        }

        for (Publisherr pub: publisherrList
             ) {nodeMainExecutor.execute(pub, nodeConfiguration);

        }
        if(appsettings.hasImg) {
            nodeMainExecutor.execute(image.rosImageView, nodeConfiguration);
        }
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        ex.printStackTrace();
        Log.e("Error", String.valueOf(ex.getCause()));
        toast(String.valueOf(ex.getCause().getCause().getMessage()));
    }

    protected void toast(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(Controller.this, text, Toast.LENGTH_LONG).show();
            }
        });
    }


    //Bluetooth listener
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean handled = false;
        System.out.println("Keydown" + keyCode + " " + event);
        if ((event.getSource() & InputDevice.SOURCE_GAMEPAD)
                == InputDevice.SOURCE_GAMEPAD) {
            if (event.getRepeatCount() == 0) {
                switch (keyCode) {
                    // Handle gamepad and D-pad button presses to
                    // navigate the ship
                    //...

                    default:
                        if (isFireKey(keyCode)) {
                            // Update the ship object to fire lasers
                            //...
                            System.out.println("Fire");
                            InputManager.rightJSup = true;

                            kickTrueDelay();
                            handled = true;
                        }

                        if (isBackKey(keyCode)) {
                            System.out.println("Back");
                            handled = true;
                        }
                        if(isSpeedUpKey(keyCode)) {
                            InputManager.progressBar ++;
                        }
                        if(isSpeedDownKey(keyCode)) {
                            InputManager.progressBar --;
                        }
                        System.out.println(keyCode);
                        break;
                }
            }
            if (handled) {
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    private static boolean isFireKey(int keyCode) {
        return keyCode == 103;
    }

    private static boolean isSpeedUpKey(int keyCode) {
        return keyCode == 23;
    }

    private static boolean isSpeedDownKey(int keyCode) {
        return keyCode == 100;
    }


    private static boolean isBackKey(int keyCode) {
        return keyCode == 97 || keyCode == 100;
    }

    //Bluetooth listener
    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {


        blueLeftX = event.getX();
        blueLeftY = event.getY();

        blueRightX = event.getAxisValue(MotionEvent.AXIS_Z);
        blueRightY = event.getAxisValue(MotionEvent.AXIS_RZ);
        bluetoothContrX1 = 0;
        bluetoothContrX2 = 0;

        bluetoothContrY1 = 0;
        bluetoothContrY2 = 0;

        if(!(Math.abs(blueLeftX) < 0.12)) bluetoothContrX1 = 10 * blueLeftX;
        if(!(Math.abs(blueLeftY) < 0.12)) bluetoothContrY1 = 10 * blueLeftY;

        if(!(Math.abs(blueRightX) < 0.12)) bluetoothContrX2 = 10 * blueRightX;
        if(!(Math.abs(blueRightY) < 0.12)) bluetoothContrY2 = 10 * blueRightY;

        Log.d("Left Stick X", bluetoothContrX1 + "");
        Log.d("Left Stick Y", bluetoothContrY1 + "");

        Log.d("Right Stick Y", bluetoothContrY2 + "");
        Log.d("Right Stick X", bluetoothContrX2 + "");
        InputManager.leftJoystickData((int) bluetoothContrX1, (int) -bluetoothContrY1);
        InputManager.rightJoystickData((int) bluetoothContrX2, (int) -bluetoothContrY2);
        return super.onGenericMotionEvent(event);
    }


    public void kickTrueDelay() {
        this.timer = new Timer();

        TimerTask action = new TimerTask() {
            public void run() {
                System.out.println("Goes to false");
                InputManager.rightJSup = false;
            }

        };

        this.timer.schedule(action, 5000); //this starts the task
    }


}
