package com.droiduino.smarthome;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.droiduino.smarthome.adapter.BtConsts;
import com.droiduino.smarthome.bluetooth.BtConnection;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnItemClick {

    private MenuItem menuItem;
    private BluetoothAdapter btAdapter;
    private final int ENABLE_REQUEST = 15;
    private final int REQUEST_CODE_DEVICE_ADD = 1;
    private SharedPreferences pref;
    private BtConnection btConnection;
    private Button bA, bB;
    private TextView textView;

    private RecyclerView recyclerView;
    private Button buttonAdd;
    private ArrayList<Room> listItems;
    private ListItemAdapter adapter;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView);
        recyclerView = findViewById(R.id.lights_list);
        buttonAdd = findViewById(R.id.add_button);
        progressBar = findViewById(R.id.progressBar);
        listItems = new ArrayList<>();
        adapter = new ListItemAdapter(this, listItems, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DeviceAddActivity.class);
                startActivityForResult(intent, REQUEST_CODE_DEVICE_ADD);
            }
        });
        init();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        menuItem = menu.findItem(R.id.id_bt_button);
        setViewStates();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.id_bt_button) {
            if (!btAdapter.isEnabled()) {
                enableBt();
            } else {
                btAdapter.disable();
                textView.setText("Увімкніть Bluetooth для роботи з пристроями");
//                menuItem.setIcon(R.drawable.ic_bt_enable);
            }

        } else if (item.getItemId() == R.id.id_menu) {
            if (btAdapter.isEnabled()) {
                Intent i = new Intent(MainActivity.this, BtListActivity.class);
                startActivity(i);
            } else {
                Toast.makeText(this, "Увімкніть bluetooth!", Toast.LENGTH_SHORT).show();
            }
        } else if (item.getItemId() == R.id.id_connect) {
            btConnection.connect();
            progressBar.setVisibility(View.VISIBLE);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == ENABLE_REQUEST) {
                setViewStates();
            }
            if (requestCode == REQUEST_CODE_DEVICE_ADD) {
                if (data.hasExtra("new_device_pin") && data.hasExtra("new_device_pin")) {
                    Room newItem = new Room(data.getStringExtra("new_device_pin"), data.getStringExtra("new_device_name"), true);
                    listItems.add(newItem);
                    Log.d("listItems", "add new device");
                    Log.d("listItems", data.getStringExtra("new_device_pin"));
                    Log.d("listItems", data.getStringExtra("new_device_name"));
                    adapter.notifyItemInserted(listItems.size() - 1);
                }
            }

        }

    }

    private void setViewStates() {

        if (btAdapter.isEnabled()) {
//            buttonAdd.setVisibility(View.VISIBLE);
//            recyclerView.setVisibility(View.VISIBLE);
            textView.setText("Під'єднайтесь до пристрою");
//            menuItem.setIcon(R.drawable.ic_bt_disable);

        } else {
//            buttonAdd.setVisibility(View.INVISIBLE);
//            recyclerView.setVisibility(View.INVISIBLE);
            textView.setText("Увімкніть Bluetooth для роботи з пристроями");
//            menuItem.setIcon(R.drawable.ic_bt_enable);

        }

    }


    private void init() {
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        pref = getSharedPreferences(BtConsts.MY_PREF, Context.MODE_PRIVATE);
        Handler h = new Handler() {
            public void handleMessage(android.os.Message msg) {
                switch (msg.what){
                    case 1111:
                        textView.setText("");
                        progressBar.setVisibility(View.INVISIBLE);
                        buttonAdd.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.VISIBLE);
                        break;
                }

            }

            ;
        };
        btConnection = new BtConnection(this, h);
//        if(btConnection.isSocketConnected()){
//            buttonAdd.setVisibility(View.VISIBLE);
//        }else {
//            buttonAdd.setVisibility(View.INVISIBLE);
//        }
    }

    private void enableBt() {

        Intent i = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(i, ENABLE_REQUEST);

    }

    @Override
    public void onItemClickHandler(String value) {
        btConnection.sendMessage(value);
    }

    @Override
    public void onDeviceAddHandler(String name, String pin) {

    }
}