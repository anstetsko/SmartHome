package com.droiduino.smarthome.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.widget.Toast;

import com.droiduino.smarthome.adapter.BtConsts;

public class BtConnection {
    private Context context;
    private SharedPreferences pref;
    private BluetoothAdapter btAdapter;
    private BluetoothDevice device;
    private ConnectThread connectThread;

    private Handler handler;

    public BtConnection(Context context, Handler handler) {
        this.context = context;
        this.handler = handler;
        pref = context.getSharedPreferences(BtConsts.MY_PREF, Context.MODE_PRIVATE);
        btAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public void connect(){

        String mac = pref.getString(BtConsts.MAC_KEY, "");
        if(!btAdapter.isEnabled() || mac.isEmpty()) return;
        device = btAdapter.getRemoteDevice(mac);

        if(device == null) {
            Toast.makeText(this.context, "Увімкніть bluetooth!", Toast.LENGTH_SHORT).show();
            return;
        }
        connectThread = new ConnectThread(btAdapter, device, handler);
        connectThread.start();
        Toast.makeText(this.context, String.format("Під'єнання до %s", device), Toast.LENGTH_SHORT).show();
    }

    public void sendMessage(String message){
        connectThread.getRThread().sendMessage(message.getBytes());
    }

}