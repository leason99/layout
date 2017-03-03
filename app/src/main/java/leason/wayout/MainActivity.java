package leason.wayout;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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
        startActivity(intent);


    }
}
