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
    String itemName[] = {"乾糧", "水", "電池", "急救箱"};

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
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

                    itemCal.set(itemDate.getYear() + 1900, itemDate.getMonth(), itemDate.getDate());
                    long difference = itemCal.getTimeInMillis() - now.getTimeInMillis();
                    int day =(int)Math.floor(difference / (3600 * 24 * 1000.00));

                    if (day < 7) {
                        String bagNumText= String.valueOf(bagNum+1);
                        String ContentText;
                        Intent intent = new Intent();
                        intent.setClass(mainService, MainActivity.class);
                        PendingIntent pendingIntent = PendingIntent.getActivity(mainService, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                        if (day < 0) {
                            ContentText = "急救包" + bagNumText+ " 的" + itemName[itemNum] + "已經過期 " + Math.abs((int)day) + "天";




                        }

                        if (day == 0) {
                            ContentText = "急救包" + bagNumText + " 的" + itemName[itemNum] + "今天過期";


                        }
                        else {
                            ContentText= "急救包" + bagNumText + " 的" + itemName[itemNum] + "再過 " + (int)day + "天即將到期";


                        }



                        if(day<0){

                            getSharedPreferences("overdue" + String.valueOf(bagNum), Context.MODE_PRIVATE).edit()
                                    .putBoolean(BagItem.Type.values()[itemNum].toString(), true).commit();

                        }
                        else{

                            getSharedPreferences("overdue" + String.valueOf(bagNum), Context.MODE_PRIVATE).edit()
                                    .putBoolean(BagItem.Type.values()[itemNum].toString(), false).commit();

                        }
                        notification = new Notification.Builder(mainService)
                                .setTicker("Ticker")
                                .setAutoCancel(false)
                                .setContentInfo("info")
                                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.logo))
                                .setSmallIcon(R.drawable.logo)
                                .setContentIntent(pendingIntent)
                                .setContentTitle("The Way Out")
                                .setContentText(ContentText)
                                .setOngoing(true)
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




}

