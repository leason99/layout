package leason.wayout;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.util.Log;
import android.widget.ImageView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONStringer;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import static leason.wayout.ConnectActivity.connectActivity;

/**
 * Created by leason on 2017/3/4.
 */

public class MainService extends Service {
    static MainService mainService = null;
    NotificationManager notificationManager;
    String DeviceName = "HC-06";  // can connect to another device by rename DeviceName
    Notification notification;
    static BluetoothAdapter BA;
    static BluetoothSocket BS;
    static Handler handler;
    // byte[] receiveDate;
    String receiveDate = "";
    String itemName[] = {"乾糧", "水", "電池", "急救箱"};
    int chargeId[] = {R.id.charge1, R.id.charge2, R.id.charge3, R.id.charge4};

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                switch (msg.what) {
                    case 1:
                        if(connectActivity!=null) {
                            Intent intent = new Intent();
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                            intent.setClass(mainService, PasswordActivity.class);
                            intent.setAction("setpwd");
                            startActivity(intent);
                        }// connectActivity.finish();
                        break;
                    case 2:
                        try {

                            Message msg2 = new Message();
                            msg2.what = 3;
                            receiveData(msg2);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        break;
                    case 3:

                        if (String.valueOf(receiveDate).equals("finished")) {

                            Intent mainIntent = new Intent();
                            mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mainIntent.setClass(MainService.this, MainActivity.class);
                            startActivity(mainIntent);
                            //connectActivity.finish();
                            getSharedPreferences("set", 0)
                                    .edit()
                                    .putBoolean("isFinished", true)
                                    .commit();

                        } else if (String.valueOf(receiveDate).equals("correct")) {
                            Intent mainIntent = new Intent();
                            mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mainIntent.setClass(MainService.this, PasswordActivity.class);
                            mainIntent.setAction("change");
                            startActivity(mainIntent);

                        } else if (String.valueOf(receiveDate).equals("error")) {
                            Intent mainIntent = new Intent();
                            mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mainIntent.setClass(MainService.this, PasswordActivity.class);
                            mainIntent.setAction("input");
                            mainIntent.putExtra("error",true);
                            startActivity(mainIntent);

                        } else {


                            Log.i("msg", "receiveDate didn't finished or recevied error echo" + String.valueOf(receiveDate));


                        }


                        break;

                    case 4:


                        try {

                            Message msg4 = new Message();
                            msg4.what = 5;
                            receiveData(msg4);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        break;
                    case 5:


                        JSONArray jsonArray = null;
                        try {
                            jsonArray = new JSONArray(receiveDate);
                            if (jsonArray != null) {
                                Message message = new Message();
                                message.what=1;
                                Bundle bundle = new Bundle();


                                for (int i = 0; i < 4; i++) {
                                    try {
                                        Boolean ischargeed = (Boolean) jsonArray.get(i);
                                        bundle.putBoolean(String.valueOf(i), ischargeed);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                message.setData(bundle);
                                MainActivity.mainActivity.handler.sendMessage(message);


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("update", "更新行動電源失敗");
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
                    try{
                        if (Bdevice.getName().equals("HC-06")) {

                            try {

                                // BS = Bdevice.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
                                BS=Bdevice.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
                                //
                         /*   Class<?> clazz = BS.getRemoteDevice().getClass();
                            Class<?>[] paramTypes = new Class<?>[] {Integer.TYPE};

                            Method m = clazz.getMethod("createRfcommSocket", paramTypes);
                            Object[] params = new Object[] {Integer.valueOf(1)};

                            BS = (BluetoothSocket) m.invoke(BS.getRemoteDevice(), params);
                           */
                                //

                                new ConnectThread(BS, BA).run();
/*
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();*/
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                        }}catch (NullPointerException e){

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

        remain();


    }

    private void remain() {

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                checkDate();

            }
        }, 0, 1000*120);


    }

    public void checkDate() {
        final Calendar now = Calendar.getInstance();

        for (int bagNum = 0; bagNum < 4; bagNum++) {
            for (int itemNum = 0; itemNum < 4; itemNum++) {
                String date = "";
                Date itemDate = new Date();
                date = MainService.this.getSharedPreferences(String.valueOf(bagNum), Context.MODE_PRIVATE).getString("date" + BagItem.Type.values()[itemNum].toString(), "");

                if (date.equals("")) {


                } else {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                    try {
                        itemDate = sdf.parse(date);
                        Log.i("itemDate", itemDate.toString());
                    } catch (ParseException e) {
                        Log.w("StringToDate", " error");
                    }


                    Calendar itemCal = Calendar.getInstance();

                    itemCal.set(itemDate.getYear() + 1900, itemDate.getMonth(), itemDate.getDay());
                    long difference = itemCal.getTimeInMillis() - now.getTimeInMillis();
                    long day = difference / (3600 * 24 * 1000);

                    if (day < 3) {
                        String ContentText;
                        Intent intent = new Intent();
                        intent.setClass(mainService, MainActivity.class);
                        PendingIntent pendingIntent = PendingIntent.getActivity(mainService, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                        if (day < 0) {
                            ContentText = "急救包" + bagNum + " 的" + itemName[itemNum] + "已經過期 " + Math.abs((int)day) + "天";


                        }

                        if (day == 0) {
                            ContentText = "急救包" + bagNum + " 的" + itemName[itemNum] + "今天過期";


                        }
                        else {
                            ContentText= "急救包" + bagNum + " 的" + itemName[itemNum] + "再過 " + (int)day + "天即將到期";


                        }
                        notification = new Notification.Builder(mainService)
                                .setTicker("Ticker")
                                .setAutoCancel(false)
                                .setContentInfo("info")
                                //.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.logo))
                                .setSmallIcon(R.drawable.logo)
                                .setContentIntent(pendingIntent)
                                .setContentTitle("The Way Out")
                                .setContentText(ContentText)
                                .setAutoCancel(false).
                                .build();
                        notificationManager.notify(bagNum * 10 + itemNum, notification);

                        getSharedPreferences("icon" + String.valueOf(bagNum), Context.MODE_PRIVATE).edit()
                                .putBoolean(BagItem.Type.values()[itemNum].toString(), true).commit();

                    } else {
                        getSharedPreferences("icon" + String.valueOf(bagNum), Context.MODE_PRIVATE).edit()
                                .putBoolean(BagItem.Type.values()[itemNum].toString(), false).commit();
                        notificationManager.cancel(bagNum * 10 + itemNum);
                    }
                }

            }
        }
    }

    static public Boolean isInstance() {

        return mainService != null;

    }


    public void Bluetoothconnect() {


        BA = BluetoothAdapter.getDefaultAdapter();

        if (BA.isDiscovering())

        {
            BA.cancelDiscovery();
        }

        BA.startDiscovery();

    }


    public void sendData(final String data, final Message msg) throws IOException {

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
                    Log.i("send", data);
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
                byte[] buffer = new byte[50];
                Boolean isjsonend=true;
                try {
                    receiveDate = "";
                    InputStream inputStream = BS.getInputStream();
                    String temp_msg;
                    while (inputStream.available() <4) {};
                    while (inputStream.available() > 0|| !isjsonend) {

                        //  while (true){
                        // Read from the InputStream
                        int byte_num = inputStream.read(buffer);
                        temp_msg = new String(buffer, 0, byte_num);
                        receiveDate += temp_msg;

                        if(receiveDate.contains("[")){
                            isjsonend=false;

                            if(receiveDate.contains("]")){
                                isjsonend=true;
                            }

                        }
                        //if(inputStream.available()==0)break;
                        //  }
                    }

                    //  ByteArrayInputStream input = new ByteArrayInputStream(buffer);
                    // input.read();
                    Message tmpMsg = new Message();
                    tmpMsg.what=msg.what;
                    handler.sendMessage(tmpMsg);

                    Log.i("recrive", receiveDate);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });
        thread.run();

    }


}

