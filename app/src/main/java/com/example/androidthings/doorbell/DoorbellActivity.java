/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androidthings.doorbell;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.KeyEvent;

import com.google.android.things.contrib.driver.button.Button;
import com.google.android.things.contrib.driver.button.ButtonInputDriver;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.Random;

/**
 * Doorbell activity that capture a picture from an Android Things
 * camera on a button press and post it to Firebase and Google Cloud
 * Vision API.
 */
public class DoorbellActivity extends Activity {
    private static final String TAG = DoorbellActivity.class.getSimpleName();

    private FirebaseDatabase mDatabase;

    private DatabaseReference mDataBaseRef;

    private PIRSen PIR;

    private dataPoint currentDataPoint;

    private Random rand = new Random();
    /**
     * Driver for the com.enghack2018.androidthings.com.example.androidthings.doorbell button;
     */
    private ButtonInputDriver mButtonInputDriver;

    /**
     * A {@link Handler} for running Camera tasks in the background.
     */
    private Handler mCameraHandler;

    /**
     * An additional thread for running Camera tasks that shouldn't block the UI.
     */
    private HandlerThread mCameraThread;

    /**
     * A {@link Handler} for running Cloud tasks in the background.
     */
    private Handler mCloudHandler;

    /**
     * An additional thread for running Cloud tasks that shouldn't block the UI.
     */
    private HandlerThread mCloudThread;

    private int dataID = 0;

    public String[] location = new String[]{"Hanxing and Daliang","Jinan and Huanghe",
            "Zhongsan and King Street"};


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Doorbell Activity created.");

        // We need permission to access the camera
        if (checkSelfPermission(Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // A problem occurred auto-granting the permission
            Log.e(TAG, "No permission");
            return;
        }


        mDatabase = FirebaseDatabase.getInstance();
        mDataBaseRef = mDatabase.getReference();


        // Initialize the com.enghack2018.androidthings.com.example.androidthings.doorbell button driver
        initPIO();

    }

    private void initPIO() {
        try {
            mButtonInputDriver = new ButtonInputDriver(
                    BoardDefaults.getGPIOForButton(),
                    Button.LogicState.PRESSED_WHEN_HIGH,
                    KeyEvent.KEYCODE_ENTER);
            mButtonInputDriver.register();
            Log.w(TAG, "GPIO Pin set");
        } catch (IOException e) {
            mButtonInputDriver = null;
            Log.w(TAG, "Could not open GPIO pins", e);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //mCamera.shutDown();

        //mCameraThread.quitSafely();
        //mCloudThread.quitSafely();
        try {
            mButtonInputDriver.close();
        } catch (IOException e) {
            Log.e(TAG, "button driver error", e);
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            // Sensor triggered!
            Log.d(TAG, "sensor triggered");
            Log.d(TAG, "\n");
            int i = rand.nextInt(location.length);
            PIR = new PIRSen(location[i], "BCM17");
            currentDataPoint = new dataPoint(PIR);
            pushToFirebase(currentDataPoint,dataID);
            return true;
        }
        else{
            return false;
        }
    }



    private void pushToFirebase(dataPoint dataP, int ID){
        Log.d(TAG,"Pushing data to firebase");
        mDataBaseRef.child("datapoints").child(Integer.toString(ID)).setValue(dataP.getDate()+" @ "+dataP.getLocation());
        dataID ++;
    }
}
