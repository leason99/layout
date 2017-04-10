package leason.wayout;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static leason.wayout.MainService.BA;
import static leason.wayout.MainService.BS;


public class ConnectActivity extends AppCompatActivity {
    static ConnectActivity connectActivity;
    static Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        if (!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
            // 發出一個intent去開啟藍芽，
            Intent mIntentOpenBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(mIntentOpenBT, 1);

        }
        else {
            MainService.mainService.Bluetoothconnect();
        }
        connectActivity = this;

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == 1) {
            if (!BA.isEnabled()) {
//
//待寫dialog 警告
              Intent mIntentOpenBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(mIntentOpenBT, 1);
            } else {

                MainService.mainService.Bluetoothconnect();
            }
        }

    }

}
