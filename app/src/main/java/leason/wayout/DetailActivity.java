package leason.wayout;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.io.InputStream;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class DetailActivity extends AppCompatActivity {
    String Num;
    MyTextView textView;
    int itemId[] = {R.id.food, R.id.water, R.id.battery, R.id.bag};
    int itemPicId[] = {R.drawable.food_item, R.drawable.water_item, R.drawable.battery_item, R.drawable.bag_item};
    int itemPicIdPress[] = {R.drawable.food_item_press, R.drawable.water_item_press, R.drawable.battery_item_press, R.drawable.bag_item_press};
    int itemPicIdOverdue[] = {R.drawable.food_item_overdue, R.drawable.water_item_overdue, R.drawable.battery_item_overdue, R.drawable.bag_item_overdue};
    int itemPicIdOverduePress[] = {R.drawable.food_item_overdue_press, R.drawable.water_item_overdue_press, R.drawable.battery_item_overdue_press, R.drawable.bag_item_overdue_press};
    final int[] dateId = {R.id.food_date, R.id.water_date, R.id.battery_date, R.id.bag_date};
    final int[] Id = {R.id.food, R.id.water, R.id.battery, R.id.bag};
    static DetailActivity instanceDetailActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        instanceDetailActivity = this;
        Num = String.valueOf(Integer.valueOf(getIntent().getAction()) - 1);
        textView = (MyTextView) findViewById(R.id.bag_num);
        textView.setText(Num);


    }

    public void setDate(View v) {

        BagItem bagItem = null;
        switch (v.getId()) {
            case R.id.food:
                bagItem = new BagItem(Integer.valueOf(Num), BagItem.Type.food, new Date());
                break;
            case R.id.water:
                bagItem = new BagItem(Integer.valueOf(Num), BagItem.Type.water, new Date());
                break;
            case R.id.battery:
                bagItem = new BagItem(Integer.valueOf(Num), BagItem.Type.battery, new Date());

                break;
            case R.id.bag:
                bagItem = new BagItem(Integer.valueOf(Num), BagItem.Type.bag, new Date());

                break;
        }


        MyDialog alertDialog = null;
        try {
            alertDialog = new MyDialog(this, bagItem);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        alertDialog.show();


    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setResult(1);
            finish();
            return true;
        } else {

            return super.onKeyDown(keyCode, event);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();


        for (int i = 0; i < itemId.length; i++) {

            TextView textView = (TextView) findViewById(dateId[i]);
            String dateText = this.getSharedPreferences(String.valueOf(Num), Context.MODE_PRIVATE)
                    .getString("date" + BagItem.Type.values()[i].toString(), "");
            Boolean status = getSharedPreferences("icon" + String.valueOf(Num), Context.MODE_PRIVATE)
                    .getBoolean(BagItem.Type.values()[i].toString(), false);
            textView.setText(dateText);


            View view = (RelativeLayout) findViewById(itemId[i]);

            if (status) {
                final int finalI = i;
                view.setBackgroundResource(itemPicIdOverdue[finalI]);

                view.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            v.setBackgroundResource(itemPicIdOverdue[finalI]);
                        } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            v.setBackgroundResource(itemPicIdOverduePress[finalI]);
                        }
                        return false;
                    }
                });
            } else {

                final int finalI = i;
                view.setBackgroundResource(itemPicId[finalI]);

                view.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            v.setBackgroundResource(itemPicId[finalI]);
                        } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            v.setBackgroundResource(itemPicIdPress[finalI]);
                        }
                        return false;
                    }
                });
            }


        }
    }
    public void updata(){


        MainService.mainService.checkDate();

        for (int i = 0; i < itemId.length; i++) {

            Boolean status = getSharedPreferences("icon" + String.valueOf(Num), Context.MODE_PRIVATE)
                    .getBoolean(BagItem.Type.values()[i].toString(), false);
            View view = (RelativeLayout) findViewById(itemId[i]);
            if (status) {
                final int finalI = i;
                view.setBackgroundResource(itemPicIdOverdue[finalI]);



                view.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            v.setBackgroundResource(itemPicIdOverdue[finalI]);
                        } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            v.setBackgroundResource(itemPicIdOverduePress[finalI]);
                        }
                        return false;
                    }
                });
            } else {

                final int finalI = i;
                view.setBackgroundResource(itemPicId[finalI]);

                view.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            v.setBackgroundResource(itemPicId[finalI]);
                        } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            v.setBackgroundResource(itemPicIdPress[finalI]);
                        }
                        return false;
                    }
                });
            }}

    }
}
