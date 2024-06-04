package com.droiduino.smarthome.bluetooth;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ReceiveThread extends Thread{
    private BluetoothSocket socket;
    private InputStream inputS;
    private OutputStream outputS;

    private Handler handler;
    private byte[] rBuffer;

    public ReceiveThread(BluetoothSocket socket, Handler handler){
        this.socket = socket;
        this.handler = handler;
        try{
            inputS = socket.getInputStream();
        } catch (IOException e){

        }
        try{
            outputS = socket.getOutputStream();
        } catch (IOException e){

        }

    }

    @Override
    public void run() {
        rBuffer = new byte[2];
        while(true){
            try{
                int size = inputS.read(rBuffer);
                String message = new String(rBuffer, 0, size);
                Message msg = Message.obtain(); // Creates an new Message instance
                msg.obj = message; // Put the string into Message, into "obj" field.
                msg.setTarget(handler); // Set the Handler
                msg.sendToTarget(); //Send the message
                Log.d("MyLog","Message: " + message);
            } catch (IOException e){
                break;
            }

        }
    }

    public void sendMessage(byte[] byteArray){
        try{
            outputS.write(byteArray);
        }catch (IOException e){

        }
    }
}