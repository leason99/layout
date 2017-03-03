package leason.wayout;

import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.Toast;

import java.nio.InvalidMarkException;

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
            password.deleteCharAt(password.length() - 1);

            ImageView image = (ImageView) findViewById(passImage[password.length() - 1]);
            image.setImageResource(R.drawable.no_pass);
        }


        if (password.length() == 6) {

            Intent intent = new Intent();

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
                        intent.setClass(PasswordActivity.this, MainActivity.class);
                        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(PasswordActivity.this).toBundle());
                        getSharedPreferences("set", 0)
                                .edit()
                                .putBoolean("isFinished", true)
                                .commit();
                        finish();
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
                    intent.setClass(PasswordActivity.this, PasswordActivity.class);
                    intent.setAction("change");
                    startActivity(intent);
                    finish();
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
//bluetooth
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
        }
    }


}



