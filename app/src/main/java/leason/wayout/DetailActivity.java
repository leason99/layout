package leason.wayout;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;


import java.util.Calendar;
import java.util.Date;
import java.util.zip.Inflater;

public class DetailActivity extends AppCompatActivity {
    String Num;
    MyTextView textView;

    final int[] dateId = {R.id.food_date, R.id.water_date, R.id.battery_date, R.id.bag_date};

    final int[] Id = {R.id.food, R.id.water, R.id.battery, R.id.bag};
    static  DetailActivity instanceDetailActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        instanceDetailActivity=this;
        Num = getIntent().getAction();
        textView = (MyTextView) findViewById(R.id.bag_num);
        textView.setText(Num);


        for (int i = 0; i < 4; i++) {

            TextView view = (TextView) findViewById(dateId[i]);
            String dateText = this.getSharedPreferences(String.valueOf(Num), Context.MODE_PRIVATE)
                    .getString(BagItem.Type.values()[i].toString(), "");
            view.setText(dateText);
        }

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


        Calendar calendar = Calendar.getInstance();

        MyDialog alertDialog = new MyDialog(this, bagItem);
        alertDialog.show();
             /*  View datePicker= view.findViewById(R.id.datePicker);
        View numberPicker = datePicker.findViewById(Resources.getSystem().getIdentifier("month", "id", "android"));
        datePicker.
        ImageButton imageButton = (ImageButton) numberPicker.findViewById(getResources().getIdentifier("android:id/increment",null,null));
        imageButton.setBackgroundColor(Color.RED);

        ImageButton imageButton = (ImageButton) numberPicker.findViewById(numberPicker.getResources().getIdentifier("increment", "id", "android"));
        imageButton.setBackgroundColor(Color.RED);
        imageButton = (ImageButton) numberPicker.findViewById( Resources.getSystem().getIdentifier("decrement", "id", "android"));
        imageButton.setBackgroundColor(Color.RED);
*/


    }




}
