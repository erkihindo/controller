package com.github.ros_java.android_ROS.controller;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TextView;

import org.ros.android.BitmapFromCompressedImage;

import Joystick.DualJoystickView;
import Joystick.JoystickMovedListener;
import sensor_msgs.CompressedImage;

/**
 * Created by viki on 5/13/16.
 */
public class ViewManager {

    static Activity app;
    //displays the camera`s image




    //Joystick locations
    public static TextView txtX1, txtY1;
    public static TextView txtX2, txtY2;

    //Inputs
    public static EditText enteredText;
    public static DualJoystickView joystick;
    public static SeekBar bar;
    public static Button button;
    public static TableLayout table;




    public static void defineJoySticks() {
        joystick = (DualJoystickView) app.findViewById(R.id.dualjoystickView);

        joystick.setOnJostickMovedListener(_listenerLeft, _listenerRight);
    }

    private static JoystickMovedListener _listenerLeft = new JoystickMovedListener() {

        @Override
        public void OnMoved(int pan, int tilt) {
            txtX1.setText(Integer.toString(pan));
            txtY1.setText(Integer.toString(tilt));
            InputManager.leftJoystickData(pan, -tilt);
        }

        @Override
        public void OnReleased() {
            txtX1.setText("released");
            txtY1.setText("released");
            InputManager.leftJoystickData(0, 0);
        }

        public void OnReturnedToCenter() {
            txtX1.setText("0");
            txtY1.setText("0");
        };
    };


    //right joystick
    private static JoystickMovedListener _listenerRight = new JoystickMovedListener() {

        @Override
        public void OnMoved(int pan, int tilt) {
            txtX2.setText(Integer.toString(pan));
            txtY2.setText(Integer.toString(tilt));
            InputManager.rightJoystickData(pan, -tilt);
        }

        @Override
        public void OnReleased() {
            txtX2.setText("released");
            txtY2.setText("released");
            InputManager.rightJoystickData(0, 0);
        }

        public void OnReturnedToCenter() {
            txtX2.setText("0");
            txtY2.setText("0");
        };
    };

    public static void showTextFields() {
        table = (TableLayout) app.findViewById(R.id.joystickXY);
        table.setVisibility(View.VISIBLE);
    }

    public static void defineTextFields() {
        txtX1 = (TextView) app.findViewById(R.id.TextViewX1);
        txtY1 = (TextView) app.findViewById(R.id.TextViewY1);

        txtX2 = (TextView) app.findViewById(R.id.TextViewX2);
        txtY2 = (TextView) app.findViewById(R.id.TextViewY2);
    }

    public static void defineEditTextFields() {
        enteredText = (EditText)app.findViewById(R.id.entered_text);
        enteredText.setVisibility(View.VISIBLE);

        enteredText.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                InputManager.enteredText = String.valueOf(s);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
            }
        });
    }




    public static void defineSlider() {
        bar = (SeekBar)app.findViewById(R.id.seekBar);
        bar.setVisibility(View.VISIBLE);

        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.i("Seekbar", String.valueOf(seekBar.getProgress()));
                InputManager.progressBar = seekBar.getProgress();


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {


            }
        });

    }


    private static void defineButton() {
        button = (Button) app.findViewById(R.id.button);
        button.setVisibility(View.VISIBLE);


        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                InputManager.buttonPress = !InputManager.buttonPress;
                Log.i("Button", "Pressed button!!!!" + InputManager.buttonPress);
            }
        });


    }






    public static void moveObject(String current, String inputLoc) {
        String[] XY = inputLoc.split(" ");
        if(current.equals("slider")) {
            bar.setX(Integer.parseInt(XY[0]));
            bar.setY(Integer.parseInt(XY[1]));

        } else if(current.equals("button")) {
            button.setX(Integer.parseInt(XY[0]));
            button.setY(Integer.parseInt(XY[1]));

        } else if(current.equals("joysticksTable")) {
            table.setX(Integer.parseInt(XY[0]));
            table.setY(Integer.parseInt(XY[1]));

        } else if(current.equals("enterText")) {
            enteredText.setX(Integer.parseInt(XY[0]));
            enteredText.setY(Integer.parseInt(XY[1]));

        }
    }

    public static void moveListener(String inputLoc) {
        String[] XY = inputLoc.split(" ");
        Controller.listenerList.get(0).getCurrentRTV().setX(Integer.parseInt(XY[0]));
        Controller.listenerList.get(0).getCurrentRTV().setY(Integer.parseInt(XY[1]));
    }


    public static void sizeObject(String current, String sizeAsString) {
        String[] size = sizeAsString.split(" ");
        if(current.equals("joysticks")) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(Integer.parseInt(size[0]), Integer.parseInt(size[1]));
            joystick.setLayoutParams(layoutParams);

        }
    }


    public static void showObject(String inputName) {
        if(inputName.equals("slider")) {
            Log.i("Showing", "Seekbar");
            defineSlider();


        } else if(inputName.equals("joysticks")) {
            Log.i("Showing", "Joysticks");
            LinearLayout lay = (LinearLayout) app.findViewById(R.id.lay);

            lay.setVisibility(View.VISIBLE);

            defineTextFields();
            defineJoySticks();

        } else if(inputName.equals("button")) {
            Log.i("Showing", "Button");
            defineButton();
        }  else if(inputName.equals("joysticksTable")) {
            Log.i("Showing", "table");
            showTextFields();
        } else if(inputName.equals("enterText")) {
            Log.i("Showing", "enterText");
            defineEditTextFields();
        } else {
            Log.i("Showing", "nothing");
        }
    }
}
