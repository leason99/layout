package leason.wayout;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.util.logging.LogRecord;

import static leason.wayout.MainService.BA;
import static leason.wayout.MainService.BS;
import static leason.wayout.MainService.mainService;

public class MainActivity extends AppCompatActivity {
    final int[][] itemId = {{R.id.food1, R.id.water1, R.id.battery1, R.id.bag1},
            {R.id.food2, R.id.water2, R.id.battery2, R.id.bag2},
            {R.id.food3, R.id.water3, R.id.battery3, R.id.bag3},
            {R.id.food4, R.id.water4, R.id.battery4, R.id.bag4}};
    static MainActivity mainActivity = null;
    Handler handler;
    int chargeId[] = {R.id.charge1, R.id.charge2, R.id.charge3, R.id.charge4};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//check overdue


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
        handler = new Handler() {


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
        };
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
        }

    }

    public void SyncChargeData(View v) {


        if (!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
//
//待寫dialog 警告
            Intent mIntentOpenBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(mIntentOpenBT, 2);
        } else {

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("等待藍牙連接");
            progressDialog.setTitle("等待藍牙連接");
            progressDialog.setCancelable(false);
            progressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {

                    if(KeyEvent.KEYCODE_BACK==keyCode){
                        progressDialog.dismiss();
                    }

                    return false;
                }
            });
            new AsyncTask<Void, Void, Void>() {

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();

                    if (BS == null) {
                        MainService.mainService.Bluetoothconnect();
                    }
                    progressDialog.show();

                }

                @Override
                protected Void doInBackground(Void... params) {
                    while (BS == null);

                    while (!BS.isConnected());
                    Message message = new Message();
                    message.what = 100;//do notthing ,just match the function

                    try {
                        mainService.sendData("update", message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    progressDialog.dismiss();

                }

            }.execute();


        }
    }


}
