package com.droiduino.smarthome;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;


public class DeviceAddActivity extends AppCompatActivity {

    private Button addButton;

    private EditText nameField;
    private EditText pinField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_add);
        nameField = findViewById(R.id.device_name_input);
        pinField = findViewById(R.id.device_pin_input);
        addButton= findViewById(R.id.device_add_button);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeviceAddActivity.this, MainActivity.class);
                intent.putExtra("new_device_name", nameField.getText().toString());
                intent.putExtra("new_device_pin", pinField.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}