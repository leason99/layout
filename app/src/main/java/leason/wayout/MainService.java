package leason.wayout;

import android.app.Activity;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import static leason.wayout.ConnectActivity.connectActivity;

/**
 * Created by leason on 2017/3/4.
 */

public class MainService extends Service {
    static MainService mainService=null;

    BluetoothAdapter BA;
    BluetoothSocket BS;
    static Handler handler;
   // byte[] receiveDate;
    String receiveDate="";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);


        handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                switch (msg.what) {
                    case 1:
                        Intent intent = new Intent();
                        intent.setClass(connectActivity, PasswordActivity.class);
                        intent.setAction("setpwd");
                        connectActivity.startActivity(intent);
                        connectActivity.finish();
                        break;
                    case 2:
                        try {
                            Message msg2=new Message();
                            msg2.what=3;
                            receiveData(msg2);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        break;
                    case 3:

                        if(String.valueOf(receiveDate).equals("finished")){

                            Intent mainIntent=new Intent();
                            mainIntent.setClass(MainService.this, MainActivity.class);
                            startActivity(mainIntent);
                            connectActivity.finish();
                            getSharedPreferences("set", 0)
                                    .edit()
                                    .putBoolean("isFinished", true)
                                    .commit();

                        }else{
                            Log.i("msg","receiveDate didn't finished or recevied error echo"+String.valueOf(receiveDate));
                        }


                        break;
                }

            }
        };

        BroadcastReceiver mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                String action = intent.getAction();

                if (action.equals(BluetoothDevice.ACTION_FOUND)) {

                    BluetoothDevice Bdevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                    if (Bdevice.getName().equals("HC-06")) {

                        try {
                        BS = Bdevice.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
                  //          BS = Bdevice.createInsecureRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));

                            new ConnectThread(BS, BA).run();
                        } catch (IOException e) {
                        }


                    }
                } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {

                }
            }
        };

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);

        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver, filter);

        mainService = this;
    }
 static public Boolean isInstance(){

     return mainService!=null;

 }
    public void Bluetoothconnect() {


        BA = BluetoothAdapter.getDefaultAdapter();
        if (BA.isDiscovering())

        {
            BA.cancelDiscovery();
        }

        BA.startDiscovery();

    }


    public  void sendData( final String data,final Message msg) throws IOException {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                  //  ByteArrayOutputStream output = new ByteArrayOutputStream();
                   // output.write(data.getBytes());
                   /// output.flush();

                   OutputStream outputStream = BS.getOutputStream();
               //     outputStream.write(output.toByteArray());
                    outputStream.write(data.getBytes());
                    outputStream.flush();

                    handler.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        thread.run();
    }

     public void receiveData(final Message msg) throws IOException {



        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                byte[] buffer = new byte[7];

                try {

                    InputStream inputStream = BS.getInputStream();
                    while (inputStream.available()<=0);


                  //  while (true){
                        // Read from the InputStream
                       int byte_num = inputStream.read(buffer);
                        String temp_msg = new String(buffer,0,byte_num);
                        receiveDate+=temp_msg;
                        //if(inputStream.available()==0)break;
                  //  }






                  //  ByteArrayInputStream input = new ByteArrayInputStream(buffer);
                   // input.read();


                    handler.sendMessage(msg);







                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });
        thread.run();

    }


}

