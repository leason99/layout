package leason.wayout;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;

import android.os.Handler;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


import java.util.List;
import java.util.Set;

import io.palaima.smoothbluetooth.Device;
import io.palaima.smoothbluetooth.SmoothBluetooth;

import static leason.wayout.MyApplication.ispaired;

public class ConnectActivity extends AppCompatActivity {
    static ConnectActivity connectActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

MyApplication.mSmoothBluetooth.setListener( new SmoothBluetooth.Listener() {
            @Override
            public void onBluetoothNotSupported() {

            }

            @Override
            public void onBluetoothNotEnabled() {

            }

            @Override
            public void onConnecting(Device device) {
            }

            @Override
            public void onConnected(Device device) {
                Intent intent = new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setClass(ConnectActivity.this, PasswordActivity.class);
                intent.setAction("setpwd");
                startActivity(intent);
                finish();
            }

            @Override
            public void onDisconnected() {


            }

            @Override
            public void onConnectionFailed(Device device) {
                if(ispaired) {
                   MyApplication.mSmoothBluetooth.tryConnection();
                }else{
                    MyApplication.mSmoothBluetooth.doDiscovery();
                }
            }

            @Override
            public void onDiscoveryStarted() {

            }

            @Override
            public void onDiscoveryFinished() {

            }

            @Override
            public void onNoDevicesFound() {

            }

            @Override
            public void onDevicesFound(List<Device> deviceList, SmoothBluetooth.ConnectionCallback connectionCallback) {
                for (Device device:deviceList) {
                    Log.i("onDevicesFound",device.getName());
                    if(device.getName().equals("HC-06")){
                        connectionCallback.connectTo(device);

                    }


                }}

            @Override
            public void onDataReceived(int data) {


            }
        });



        if(ispaired) {
            MyApplication.mSmoothBluetooth.tryConnection();
        }else{
            MyApplication.mSmoothBluetooth.doDiscovery();
        }

        connectActivity = this;

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == 1) {
            if (!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
                Intent mIntentOpenBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(mIntentOpenBT, 1);
            }
            if(ispaired) {
                MyApplication.mSmoothBluetooth.tryConnection();
            }else{
                MyApplication.mSmoothBluetooth.doDiscovery();
            }

        }

    }

    @Override
    protected void onStop() {
        super.onStop();

    }
}
