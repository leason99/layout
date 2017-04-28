package leason.wayout;

import android.app.ActivityOptions;
import android.app.LoaderManager;
import android.app.Presentation;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Message;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


import io.palaima.smoothbluetooth.Device;
import io.palaima.smoothbluetooth.SmoothBluetooth;

import static leason.wayout.MyApplication.ispaired;
import static leason.wayout.MyApplication.mSmoothBluetooth;

public class PasswordActivity extends AppCompatActivity implements View.OnClickListener {

    Intent intent;
    final int SET_PASSWORD = 0;
    final int CHECK_PASSWORD = 1;
    final int INPUT_PASSWORD = 2;
    final int CHANGE_PASSWORD = 3;
    final int CHECK_CHANGE_PASSWORD = 4;
    ProgressDialog progressDialog;
    int Mode;
    private MyTextView textView;
    StringBuilder password;
    String checkedPassword;
    private List<Integer> mBuffer;
    int[] passImage = {R.id.pass1, R.id.pass2, R.id.pass3, R.id.pass4, R.id.pass5, R.id.pass6};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        textView = (MyTextView) findViewById(R.id.pass_mode_word);

        intent = this.getIntent();
        mBuffer = new ArrayList<>();
        String[] modeString = this.getResources().getStringArray(R.array.password_mode);

        // determine passwordActivity mode and init
        switch (intent.getAction()) {
            case "setpwd":
                Mode = SET_PASSWORD;
                break;

            case "check":
                Mode = CHECK_PASSWORD;
                checkedPassword = intent.getStringExtra("checkedPassword");
                break;
            case "input":
                Mode = INPUT_PASSWORD;
                break;
            case "change":
                Mode = CHANGE_PASSWORD;
                break;
            case "check_change":
                Mode = CHECK_CHANGE_PASSWORD;
                checkedPassword = intent.getStringExtra("checkedPassword");
                break;
        }


        textView.setText(modeString[Mode]);
        password = new StringBuilder();


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
                modeAction();
            }

            @Override
            public void onDisconnected() {


            }

            @Override
            public void onConnectionFailed(Device device) {
            if(mSmoothBluetooth.isDiscovery()){
             mSmoothBluetooth.cancelDiscovery();
            }
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
                StringBuilder sb = new StringBuilder();
                for (int integer : mBuffer) {
                    sb.append((char) integer);
                }

                getResult(sb.toString());




            }
        });

    }

    @Override
    public void onClick(View v) {

        if (v.getId() != R.id.pass_del && password.length() < 6) {
            switch (v.getId()) {
                case R.id.pass_btn0:
                    password.append(0);
                    break;
                case R.id.pass_btn1:
                    password.append(1);
                    break;
                case R.id.pass_btn2:
                    password.append(2);
                    break;
                case R.id.pass_btn3:
                    password.append(3);
                    break;
                case R.id.pass_btn4:
                    password.append(4);
                    break;
                case R.id.pass_btn5:
                    password.append(5);
                    break;
                case R.id.pass_btn6:
                    password.append(6);
                    break;
                case R.id.pass_btn7:
                    password.append(7);
                    break;
                case R.id.pass_btn8:
                    password.append(8);
                    break;
                case R.id.pass_btn9:
                    password.append(9);
                    break;
            }
            ImageView image = (ImageView) findViewById(passImage[password.length() - 1]);
            image.setImageResource(R.drawable.pass);

        } else if (v.getId() == R.id.pass_del && password.length() > 0) {
            ImageView image = (ImageView) findViewById(passImage[password.length() - 1]);
            password.deleteCharAt(password.length() - 1);


            image.setImageResource(R.drawable.no_pass);
        }


        if (password.length() == 6) {

            modeAction();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == 2)

        {
            if (!BluetoothAdapter.getDefaultAdapter().isEnabled()) {

                Intent mIntentOpenBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(mIntentOpenBT, 2);
            }
            else {
                modeAction();
            }

        }
    }

    private void getResult(String bytes) {


        if (String.valueOf(bytes).equals("finished")) {

            Intent mainIntent = new Intent();
            mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mainIntent.setClass(this, MainActivity.class);
            startActivity(mainIntent);
            finish();
            getSharedPreferences("set", 0)
                    .edit()
                    .putBoolean("isFinished", true)
                    .commit();

                progressDialog.dismiss();


            mBuffer.clear();
        } else if (String.valueOf(bytes).equals("correct")) {
            Intent mainIntent = new Intent();
            mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mainIntent.setClass(this, PasswordActivity.class);
            mainIntent.setAction("change");
            startActivity(mainIntent);
            finish();
                progressDialog.dismiss();

            mBuffer.clear();
        } else if (String.valueOf(bytes).equals("error")) {
            Intent mainIntent = new Intent();
            mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mainIntent.setClass(this, PasswordActivity.class);
            mainIntent.setAction("input");
            mainIntent.putExtra("error", true);
            startActivity(mainIntent);
            finish();
                progressDialog.dismiss();


            mBuffer.clear();
        }
    }

    void showProgressDialog() {
        progressDialog = new ProgressDialog(PasswordActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("等待藍牙連接");
        progressDialog.setTitle("等待藍牙連接");

        progressDialog.setCancelable(false);
        progressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {

                if (KeyEvent.KEYCODE_BACK == keyCode) {

                    progressDialog.dismiss();
                    finish();

                }

                return false;
            }
        });
     if(!progressDialog.isShowing())  { progressDialog.show();
    }}



public void modeAction(){

            switch (Mode) {
        case SET_PASSWORD:
            intent.setClass(PasswordActivity.this, PasswordActivity.class);
            intent.putExtra("checkedPassword", password.toString());
            intent.setAction("check");
            startActivity(intent);
            finish();
            break;
        case CHECK_PASSWORD:

            if (checkedPassword.equals(password.toString())) {

                Message msg = new Message();
                msg.what = 2;
                // TODO: 2017/4/24  MainService.mainService.sendData("setpwd:" + checkedPassword + ":", msg);
                showProgressDialog();
            //    if(!mSmoothBluetooth.isConnected()){
             //       if(ispaired){mSmoothBluetooth.tryConnection();}
             //       else{mSmoothBluetooth.doDiscovery();}
             //   }else{
                    mSmoothBluetooth.send("setpwd:" + checkedPassword + ":",true);
            //    }
            } else {
                intent.setClass(PasswordActivity.this, PasswordActivity.class);
                intent.setAction("setpwd");
                Toast.makeText(getApplicationContext(), password + "密碼錯誤" + checkedPassword, Toast.LENGTH_SHORT).show();
                startActivity(intent);
                finish();
            }
            break;

        case INPUT_PASSWORD:
            if (intent.getBooleanExtra("error", false)) {


                Toast.makeText(getApplicationContext(), "密碼錯誤，請重新輸入", Toast.LENGTH_SHORT).show();


            }
            Message msg = new Message();
            msg.what = 2;

            // TODO: 2017/4/24    MainService.mainService.sendData("chkpwd:" + password + ":", msg);
            showProgressDialog();if(!mSmoothBluetooth.isConnected()){
            if(ispaired){mSmoothBluetooth.tryConnection();}
            else{mSmoothBluetooth.doDiscovery();}
        }else{               mSmoothBluetooth.send("chkpwd:" + password + ":");     }


            break;


        case CHANGE_PASSWORD:

            intent.setClass(PasswordActivity.this, PasswordActivity.class);
            intent.putExtra("checkedPassword", password.toString());
            intent.setAction("check_change");
            startActivity(intent);
            finish();

            break;
        case CHECK_CHANGE_PASSWORD:
            if (checkedPassword.equals(password.toString())) {
                Message msg_change = new Message();
                msg_change.what = 2;
                // TODO: 2017/4/24          MainService.mainService.sendData("setpwd:" + checkedPassword + ":", msg_change);

                showProgressDialog();
                if(!mSmoothBluetooth.isConnected()){
                    if(ispaired){mSmoothBluetooth.tryConnection();}
                    else{mSmoothBluetooth.doDiscovery();}
                }else{                mSmoothBluetooth.send("setpwd:" + checkedPassword + ":",true);
                }

            } else {
                intent.setClass(PasswordActivity.this, PasswordActivity.class);
                intent.setAction("change");
                Toast.makeText(getApplicationContext(), "設定密碼錯誤，請重新設定密碼", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                finish();

            }
            break;

        default:
            finish();
    }}

    @Override
    protected void onStop() {
        super.onStop();
     }
}



