package leason.wayout;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static leason.wayout.BagItem.*;
import static leason.wayout.BagItem.Type.*;
import static leason.wayout.DetailActivity.instanceDetailActivity;

/**
 * Created by leason on 2017/3/2.
 */

public class MyDialog extends Dialog {

    TextView title, remianDay;
    Type type;
    DatePicker datePicker;
       Context context;
    Date nowDate, selectedDate;
    BagItem bagItem;
    ImageButton certainButton ,cancleButton;
    TextView view;
    public MyDialog(Context mcontext, BagItem mbagItem) throws ParseException {
        super(mcontext);
        this.bagItem = mbagItem;
        this.context = mcontext;
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.mydatepick);
        View v = getWindow().getDecorView();
        certainButton= (ImageButton) findViewById(R.id.certain);
        cancleButton= (ImageButton) findViewById(R.id.cancleButton);

        certainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                String dateString = sdf.format(selectedDate);
                context.getSharedPreferences(String.valueOf(bagItem.getNum()), Context.MODE_PRIVATE).edit()
                        .putString("date"+bagItem.getType().toString(),dateString)
                        .commit();


                 view = null;
                switch (bagItem.getType()) {

                    case food:
                        view = (TextView) instanceDetailActivity.findViewById(R.id.food_date);
                        break;
                    case water:
                        view = (TextView) instanceDetailActivity.findViewById(R.id.water_date);
                        break;
                    case bag:
                        view = (TextView) instanceDetailActivity.findViewById(R.id.bag_date);
                        break;
                    case battery:
                        view = (TextView) instanceDetailActivity.findViewById(R.id.battery_date);
                        break;
                }
                view.setText(dateString);
                DetailActivity.instanceDetailActivity.updata();

                MyDialog.this.dismiss();


            }
        });
        cancleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog.this.dismiss();
            }
        });
        v.setBackgroundResource(android.R.color.transparent);

        final Calendar calendar = Calendar.getInstance();
        remianDay = (TextView) findViewById(R.id.remianDay);
        datePicker = (DatePicker) findViewById(R.id.datePicker);
        nowDate = new Date();
        nowDate.setDate(calendar.get(Calendar.DAY_OF_MONTH));
        nowDate.setMonth(calendar.get(Calendar.MONTH));
        nowDate.setYear(calendar.get(Calendar.YEAR));
        selectedDate = new Date();
        DatePicker.OnDateChangedListener dateChangedListener = new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {


                selectedDate.setDate(dayOfMonth);
                selectedDate.setMonth(monthOfYear);
                selectedDate.setYear(year-1900);
                Calendar calendarselect = Calendar.getInstance();
                calendarselect.set(year, monthOfYear, dayOfMonth);
                long difference = calendarselect.getTimeInMillis() - calendar.getTimeInMillis();
                long day = difference / (3600 * 24 * 1000);
                remianDay.setText(String.format("距離到期日還有 %s 日", String.valueOf(day)));











            }
        };
        if(view.getText().equals("")) {
            datePicker.init(nowDate.getYear(), nowDate.getMonth(), nowDate.getDay(), dateChangedListener);
        }else{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            Date setDate=sdf.parse(String.valueOf(view.getText()));

            datePicker.init(setDate.getYear(), setDate.getMonth(), setDate.getDay(), dateChangedListener);


        }
        String[] typeString = context.getResources().getStringArray(R.array.dialog_item_string);
        title = (TextView) findViewById(R.id.dialog_title);
        this.type = bagItem.getType();

        switch (type) {

            case food:
                title.setText(typeString[0]);
                break;

            case water:
                title.setText(typeString[1]);
                break;
            case bag:
                title.setText(typeString[2]);
                break;
            case battery:
                title.setText(typeString[3]);
                break;


        }

    }




}
