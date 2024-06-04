package com.droiduino.smarthome.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

import com.droiduino.smarthome.R;

import java.io.IOException;


public class ConnectThread extends Thread {
    private BluetoothAdapter btAdapter;
    private BluetoothSocket mSocket;
    private ReceiveThread rThread;

    private Handler handler;
    public static final String UUID = "00001101-0000-1000-8000-00805F9B34FB";


    public ConnectThread(BluetoothAdapter btAdapter, BluetoothDevice device, Handler handler){
        this.handler = handler;
        this.btAdapter = btAdapter;
        try{
            mSocket = device.createRfcommSocketToServiceRecord(java.util.UUID.fromString(UUID));
        } catch (IOException e){

        }
    }

    @Override
    public void run() {
        btAdapter.cancelDiscovery();
        try{
            mSocket.connect();
            rThread = new ReceiveThread(mSocket, handler);
            rThread.start();
            Log.d("MyLog","Connected");
            handler.sendEmptyMessage(1111);
        } catch (IOException e){
            Log.d("MyLog","Not Connected");
            handler.sendEmptyMessage(2222);
            closeConnection();
        }
    }
    public void closeConnection(){
        try{
            mSocket.close();
        } catch (IOException y){

        }
    }



    public ReceiveThread getRThread() {
        return rThread;
    }
}