package leason.wayout;

import android.app.ActivityOptions;
import android.app.Presentation;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Message;
import android.support.annotation.UiThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.nio.InvalidMarkException;

import static leason.wayout.MainService.BA;
import static leason.wayout.MainService.BS;
import static leason.wayout.MainService.mainService;

public class PasswordActivity extends AppCompatActivity implements View.OnClickListener {

    Intent intent;
    final int SET_PASSWORD = 0;
    final int CHECK_PASSWORD = 1;
    final int INPUT_PASSWORD = 2;
    final int CHANGE_PASSWORD = 3;
    final int CHECK_CHANGE_PASSWORD = 4;

    int Mode;
    private MyTextView textView;
    StringBuilder password;
    String checkedPassword;

    int[] passImage = {R.id.pass1, R.id.pass2, R.id.pass3, R.id.pass4, R.id.pass5, R.id.pass6};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        textView = (MyTextView) findViewById(R.id.pass_mode_word);

        intent = this.getIntent();

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


        if (BA == null) {
            mainService.Bluetoothconnect();
        }

        if (!BA.isEnabled()) {
//
//待寫dialog 警告
            Intent mIntentOpenBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(mIntentOpenBT, 2);
        }


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

            final Intent intent = new Intent();
            final ProgressDialog progressDialog = new ProgressDialog(this);
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


            new AsyncTask<Void, Void, Void>() {

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    progressDialog.show();
                    if (BS == null) {
                        MainService.mainService.Bluetoothconnect();

                    } else if (!BS.isConnected()) {
                        MainService.mainService.Bluetoothconnect();

                    }
                }

                @Override
                protected Void doInBackground(Void... params) {
                    while (BS == null) {
                    }

                    while (!BS.isConnected()) {
                    }


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

                                try {
                                    Message msg = new Message();
                                    msg.what = 2;
                                    MainService.mainService.sendData("setpwd:" + checkedPassword + ":", msg);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(PasswordActivity.this, "藍牙設定密碼傳送錯誤", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                }
                            } else {
                                intent.setClass(PasswordActivity.this, PasswordActivity.class);
                                intent.setAction("setpwd");
                                Toast.makeText(PasswordActivity.this, password + "密碼錯誤" + checkedPassword, Toast.LENGTH_SHORT).show();
                                startActivity(intent);
                                finish();
                            }
                            break;

                        case INPUT_PASSWORD:
//blueconnect
                            Message msg = new Message();
                            msg.what = 2;
                            try {
                                MainService.mainService.sendData("chkpwd:" + password + ":", msg);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
/*
                            intent.setClass(PasswordActivity.this, PasswordActivity.class);
                            intent.setAction("change");
                            startActivity(intent);
                            finish();
*/
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
//bluetooth Message msg = new Message();
                                Message msg_change = new Message();
                                msg_change.what = 2;
                                try {
                                    MainService.mainService.sendData("setpwd:" + checkedPassword + ":", msg_change);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                finish();
                            } else {
                                intent.setClass(PasswordActivity.this, PasswordActivity.class);
                                intent.setAction("change");
                                Toast.makeText(PasswordActivity.this, password + "設定密碼錯誤" + checkedPassword, Toast.LENGTH_SHORT).show();
                                startActivity(intent);
                                finish();

                            }
                            break;


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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == 2)

        {
            if (!BA.isEnabled()) {

                Intent mIntentOpenBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(mIntentOpenBT, 2);
            }


        }
    }
}



