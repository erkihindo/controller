package com.github.ros_java.android_ROS.controller;

import android.app.Activity;
import android.content.res.Resources;
import android.util.Log;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by viki on 5/12/16.
 */
public class GuiReader {
    Activity app;



    public static boolean hasPub = false;
    public static boolean hasSub = false;
    public static boolean hasImg = false;


    public static String displayedName = "ERKI ROS";
    public static String pubTopic;
    public static String subTopic;
    public static String imgTopic;

    public static String pubTyp;
    public static String subTyp;

    public static Publisherr newPub;

    public GuiReader(Activity _activity) {
        String result;
        this.app = _activity;
        ViewManager.app = _activity;
        try {

            Resources res = app.getResources();
            InputStream in_s = res.openRawResource(R.raw.gui);

            byte[] b = new byte[in_s.available()];
            in_s.read(b);
            result = new String(b);
            String[] splittedFile = result.split("\n");
            List<String> list = new ArrayList<String>(Arrays.asList(splittedFile));

            for(int listIterator = 0; listIterator < list.size(); listIterator++) {
                Log.i("File", list.get(listIterator));
                if(list.get(listIterator).equals("inputs:")) {
                    list.remove(listIterator);
                    inputHandler(list);
                    listIterator--;
                } else if(list.get(listIterator).equals("publisher:")) {
                    list.remove(listIterator);
                    publisherHandler(list);
                    listIterator--;
                } else if(list.get(listIterator).equals("listener:")) {
                    list.remove(listIterator);
                    listenerHandler(list);
                    listIterator--;
                } else if(list.get(listIterator).equals("image_listener:")) {
                    list.remove(listIterator);
                    imageHandler(list);
                    listIterator--;
                } else {
                    Log.i("Reading error", "Unknown line: " + list.get(listIterator));
                    list.remove(listIterator);
                    listIterator--;
                }


            }





        } catch (Exception e) {
             e.printStackTrace();
            //result = "Error: can't show file.";
        }
    }

    private void imageHandler(List<String> list) {
        hasImg = true;
        Log.i("Showing", "image");
        for(int listListenerIterator = 0; listListenerIterator < list.size(); listListenerIterator++) {
            if (list.get(listListenerIterator).startsWith("\t-topic:")) {
                Log.i("topic: ", list.get(listListenerIterator));
                imgTopic = list.get(listListenerIterator).substring(9);
                ViewManager.defineImageViews(imgTopic);
                list.remove(listListenerIterator);
                listListenerIterator--;
            }
        }
    }

    private void listenerHandler(List<String> list) {
        Log.i("Showing", "listener");
        hasSub = true;
        for(int listListenerIterator = 0; listListenerIterator < list.size(); listListenerIterator++) {
            if(list.get(listListenerIterator).startsWith("\t-topic:")) {
                Log.i("topic: ", list.get(listListenerIterator));
                subTopic = list.get(listListenerIterator).substring(9);
                list.remove(listListenerIterator);
                listListenerIterator--;
            } else if(list.get(listListenerIterator).startsWith("\tmsg_type:")) {
                Log.i("msg_type: ", list.get(listListenerIterator));
                subTyp = list.get(listListenerIterator).substring(11);
                list.remove(listListenerIterator);
                listListenerIterator--;
            } else {
                break;
            }
        }
    }

    private void publisherHandler(List<String> list) {
        Log.i("Showing", "publisher");
        hasPub = true;

        for(int listPublisherIterator = 0; listPublisherIterator < list.size(); listPublisherIterator++) {
            if(list.get(listPublisherIterator).startsWith("\t-topic:")) {
                Log.i("topic: ", list.get(listPublisherIterator));
                pubTopic = list.get(listPublisherIterator).substring(9);
                newPub = new Publisherr(app, pubTopic);
                list.remove(listPublisherIterator);
                listPublisherIterator--;
            } else if(list.get(listPublisherIterator).startsWith("\tmsg_type:")) {
                pubTyp = list.get(listPublisherIterator).substring(11);
                newPub.setMsgType(pubTyp);
                Log.i("msg_type: ", list.get(listPublisherIterator));
                list.remove(listPublisherIterator);
                listPublisherIterator--;
            } else if(list.get(listPublisherIterator).startsWith("\tdata:")) {
                String datas = list.get(listPublisherIterator).substring(7);
                Log.i("datas: ", datas);
                String[] splittedData = datas.split(" ");
                List<String> dataList = new ArrayList<String>(Arrays.asList(splittedData));
                newPub.setDatas(dataList);
                list.remove(listPublisherIterator);
                listPublisherIterator--;
                Controller.publisherrList.add(newPub);
            } else {
                break;
            }
        }


    }

    private void inputHandler(List<String> list) {
        String currentInputObject = "";
        for(int listInputIterator = 0; listInputIterator < list.size(); listInputIterator++) {
            if(list.get(listInputIterator).startsWith("\t-type:")) {
                String inputName = list.get(listInputIterator).substring(8);
                ViewManager.showObject(inputName);
                currentInputObject = inputName;

                list.remove(listInputIterator);
                listInputIterator--;
                continue;
            } else if(list.get(listInputIterator).startsWith("\tsize:")) {
                String inputSize= list.get(listInputIterator).substring(7);
                ViewManager.sizeObject(currentInputObject, inputSize);

                Log.i("Size: ", inputSize);
                list.remove(listInputIterator);
                listInputIterator--;
                continue;
            } else if(list.get(listInputIterator).startsWith("\txy:")) {
                String inputLoc= list.get(listInputIterator).substring(5);
                ViewManager.moveObject(currentInputObject, inputLoc);

                Log.i("Loc: ", inputLoc);
                list.remove(listInputIterator);
                listInputIterator--;
                continue;
            } else {
                Log.i("Show", "break");
                break;
            }
        }
    }




}
