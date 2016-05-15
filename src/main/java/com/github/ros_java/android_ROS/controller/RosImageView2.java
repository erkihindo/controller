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

package com.github.ros_java.android_ROS.controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import org.ros.android.MessageCallable;
import org.ros.message.MessageListener;
import org.ros.namespace.GraphName;
import org.ros.node.ConnectedNode;
import org.ros.node.Node;
import org.ros.node.NodeMain;
import org.ros.node.topic.Subscriber;

/**
 * Displays incoming sensor_msgs/CompressedImage messages.
 *
 * @author ethan.rublee@gmail.com (Ethan Rublee)
 * @author damonkohler@google.com (Damon Kohler)
 */
public class RosImageView2<T> extends SurfaceView implements NodeMain {

  private String topicName;
  private String messageType;
  private MessageCallable<Bitmap, T> callable;

  private SurfaceHolder surfaceHolder = null;
  private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

  public RosImageView2(Context context) {
    super(context);
  }

  public RosImageView2(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public RosImageView2(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  public void setTopicName(String topicName) {
    this.topicName = topicName;
  }

  public void setMessageType(String messageType) {
    this.messageType = messageType;
  }

  public void setMessageToBitmapCallable(MessageCallable<Bitmap, T> callable) {
    this.callable = callable;
  }

  @Override
  public GraphName getDefaultNodeName() {
    return GraphName.of("ros_image_view");
  }

  @Override
  public void onStart(ConnectedNode connectedNode) {
    surfaceHolder = getHolder();

    Subscriber<T> subscriber = connectedNode.newSubscriber(topicName,messageType);
    subscriber.addMessageListener(new MessageListener<T>() {
      @Override
      public void onNewMessage(final T message) {
        if (surfaceHolder.getSurface().isValid()) {
          Canvas canvas = surfaceHolder.lockCanvas();
          Bitmap bm = callable.call(message);
          if( bm != null ) {
            canvas.drawBitmap(bm, 0, 0, paint);
          } // else { should we log this error? }
          surfaceHolder.unlockCanvasAndPost(canvas);
        }
      }
    });
  }


  @Override
  public void onShutdown(Node node) {
    node.shutdown();
  }

  @Override
  public void onShutdownComplete(Node node) {
  }

  @Override
  public void onError(Node node, Throwable throwable) {
  }
}
