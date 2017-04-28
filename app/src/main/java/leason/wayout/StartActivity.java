package leason.wayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class StartActivity extends AppCompatActivity {
    Intent intent;

    Boolean TESTMODE=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = new Intent();


        if(!getSharedPreferences("set",0).getBoolean("isFinished",false)&TESTMODE){
            intent.setClass(StartActivity.this, MainActivity.class);
        }
        else if(!getSharedPreferences("set",0).getBoolean("isFinished",false)&(!TESTMODE)){
           // intent.setClass(StartActivity.this, PasswordActivity.class);
            //intent.setAction("setpwd");
             intent.setClass(StartActivity.this, ConnectActivity.class);
        }
        else{
            intent.setClass(StartActivity.this, MainActivity.class);
        }

        Intent ServiceIntent=new Intent();
        ServiceIntent.setClass(this,MainService.class);
        startService(ServiceIntent);


        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(MainService.isInstance()){
                    startActivity(intent);
                    timer.cancel();

                    finish();
                }
            }
        }, 0,1000);

    }
}
