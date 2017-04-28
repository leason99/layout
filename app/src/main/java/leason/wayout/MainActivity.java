package leason.wayout;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.BoolRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;


import org.json.JSONArray;
import org.json.JSONException;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import io.palaima.smoothbluetooth.Device;
import io.palaima.smoothbluetooth.SmoothBluetooth;

import static leason.wayout.MyApplication.ispaired;
import static leason.wayout.MyApplication.mSmoothBluetooth;


public class MainActivity extends AppCompatActivity {
    final int[][] itemId = {{R.id.food1, R.id.water1, R.id.battery1, R.id.bag1},
            {R.id.food2, R.id.water2, R.id.battery2, R.id.bag2},
            {R.id.food3, R.id.water3, R.id.battery3, R.id.bag3},
            {R.id.food4, R.id.water4, R.id.battery4, R.id.bag4}};
    static MainActivity mainActivity = null;
    Handler handler;
    int chargeId[] = {R.id.charge1, R.id.charge2, R.id.charge3, R.id.charge4};
    private List<Integer> mBuffer;
    ProgressDialog progressDialog;
    StringBuilder sb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBuffer=new ArrayList<>();

        mSmoothBluetooth.setListener(new SmoothBluetooth.Listener() {
            @Override
            public void onBluetoothNotSupported() {

            }

            @Override
            public void onBluetoothNotEnabled() {
                Intent mIntentOpenBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(mIntentOpenBT, 2);
            }

            @Override
            public void onConnecting(Device device) {

            }

            @Override
            public void onConnected(Device device) {
                SyncChargeData(null);
            }

            @Override
            public void onDisconnected() {

/*

                Set<BluetoothDevice> pairedDevices = BluetoothAdapter.getDefaultAdapter().getBondedDevices();
                Boolean ispaired=false;
                if (pairedDevices.size() > 0) {
                    for (BluetoothDevice device : pairedDevices) {
                        if(device.getName().equals("HC-06")){
                            ispaired=true;
                        }
                    }
                }
                if(ispaired) {
                    mSmoothBluetooth.tryConnection();
                }else{
                    mSmoothBluetooth.doDiscovery();
                }*/
            }

            @Override
            public void onConnectionFailed(Device device) {
                if(mSmoothBluetooth.isDiscovery()){
                    mSmoothBluetooth.cancelDiscovery();}
                if(ispaired) {
                    mSmoothBluetooth.tryConnection();
                }else{
                    mSmoothBluetooth.doDiscovery();
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
                mBuffer.add(data);

                if ((char)data==']') { StringBuilder  sb = new StringBuilder();
                    for (int integer : mBuffer) {


                        sb.append((char) integer);
                    }
                    mBuffer.clear();


                    JSONArray jsonArray = null;
                    try {
                        jsonArray = new JSONArray(sb.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (jsonArray != null) {

                        for (int i = 0; i < 4; i++) {

                            Boolean ischargeed = null;
                            try {
                                ischargeed = (Boolean) jsonArray.get(i);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            ImageView imageView = (ImageView) MainActivity.mainActivity.findViewById(chargeId[i]);
                            if (ischargeed) {


                                imageView.setImageResource(R.drawable.charge);

                            } else {

                                imageView.setImageResource(R.drawable.no_charge);

                            }
                            getSharedPreferences("icon" + String.valueOf(i), Context.MODE_PRIVATE).edit().putBoolean("charge",ischargeed).commit();


                        }
                    }
                    progressDialog.dismiss();

                }



            }


        });


    }

    public void changePassword(View v) {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, PasswordActivity.class);
        intent.setAction("input");
        startActivity(intent);
    }

    public void bagDeatil(View v) {

        Intent intent = new Intent();
        intent.setClass(MainActivity.this, DetailActivity.class);
        intent.setAction("1");
        switch (v.getId()) {
            case R.id.bag1_btn:
                intent.setAction("1");
                break;
            case R.id.bag2_btn:
                intent.setAction("2");
                break;
            case R.id.bag3_btn:
                intent.setAction("3");
                break;
            case R.id.bag4_btn:
                intent.setAction("4");
                break;
        }
        startActivityForResult(intent, 1);


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 1) {
            MainService.mainService.checkDate();
            for (int bagItem = 0; bagItem < 4; bagItem++) {
                for (int item = 0; item < 4; item++) {
                    Boolean status = getSharedPreferences("icon" + String.valueOf(bagItem), Context.MODE_PRIVATE)
                            .getBoolean(BagItem.Type.values()[item].toString(), false);
                    ImageView view = (ImageView) findViewById(itemId[bagItem][item]);
                    if (status) {
                        switch (item) {
                            case 0:
                                view.setImageResource(R.drawable.overdue_food);
                                break;
                            case 1:
                                view.setImageResource(R.drawable.overdue_water);
                                break;
                            case 2:
                                view.setImageResource(R.drawable.overdue_battery);
                                break;
                            case 3:
                                view.setImageResource(R.drawable.overdue_bag);
                                break;
                        }
                    } else {
                        switch (item) {

                            case 0:
                                view.setImageResource(R.drawable.food);
                                break;
                            case 1:
                                view.setImageResource(R.drawable.water);
                                break;
                            case 2:
                                view.setImageResource(R.drawable.battery);
                                break;
                            case 3:
                                view.setImageResource(R.drawable.bag);
                                break;

                        }
                    }
                }
            }

        } else if (resultCode == 2)

        {
            if (!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
//
//待寫dialog 警告
                Intent mIntentOpenBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(mIntentOpenBT, 2);
            } else {

                SyncChargeData(null);
            }


        }
    }

    @Override
    protected void onStart() {
        super.onStart();
       /* handler = new Handler() {


            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);


                if (msg.what == 1) {
                    for (int i = 0; i < 4; i++) {
                        ImageView imageView = (ImageView) MainActivity.mainActivity.findViewById(chargeId[i]);
                        msg.getData().getBoolean(String.valueOf(i));
                        if (msg.getData().getBoolean(String.valueOf(i))) {

                            imageView.setImageResource(R.drawable.charge);

                        } else {

                            imageView.setImageResource(R.drawable.no_charge);

                        }

                    }

                }

            }
        };*/
        mainActivity = this;
        for (int bagItem = 0; bagItem < 4; bagItem++) {
            for (int item = 0; item < 4; item++) {
                Boolean status = getSharedPreferences("icon" + String.valueOf(bagItem), Context.MODE_PRIVATE)
                        .getBoolean(BagItem.Type.values()[item].toString(), false);
                ImageView view = (ImageView) findViewById(itemId[bagItem][item]);
                if (status) {
                    switch (item) {
                        case 0:
                            view.setImageResource(R.drawable.overdue_food);
                            break;
                        case 1:
                            view.setImageResource(R.drawable.overdue_water);
                            break;
                        case 2:
                            view.setImageResource(R.drawable.overdue_battery);
                            break;
                        case 3:
                            view.setImageResource(R.drawable.overdue_bag);
                            break;
                    }
                } else {
                    switch (item) {

                        case 0:
                            view.setImageResource(R.drawable.food);
                            break;
                        case 1:
                            view.setImageResource(R.drawable.water);
                            break;
                        case 2:
                            view.setImageResource(R.drawable.battery);
                            break;
                        case 3:
                            view.setImageResource(R.drawable.bag);
                            break;

                    }
                }
            }

            Boolean status = getSharedPreferences("icon" + String.valueOf(bagItem), Context.MODE_PRIVATE)
                    .getBoolean("charge", false);
            ImageView imageView = (ImageView) MainActivity.mainActivity.findViewById(chargeId[bagItem]);
            if (status) {

                imageView.setImageResource(R.drawable.charge);

            } else {

                imageView.setImageResource(R.drawable.no_charge);

            }

        }
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("等待藍牙連接");
        progressDialog.setTitle("等待藍牙連接");
        progressDialog.setCancelable(false);
        progressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {

                if (KeyEvent.KEYCODE_BACK == keyCode) {
                    progressDialog.dismiss();
                }

                return false;
            }
        });

    }

    public void SyncChargeData(View v) {


        if (!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
//
//待寫dialog 警告
            Intent mIntentOpenBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(mIntentOpenBT, 2);
        } else {



          if(!progressDialog.isShowing())  {progressDialog.show();}
          if(!mSmoothBluetooth.isConnected())  {if(ispaired) {
                mSmoothBluetooth.tryConnection();
            }else{
                mSmoothBluetooth.doDiscovery();
            }}else {
              MyApplication.mSmoothBluetooth.send("update");
          }
        }
    }


}
