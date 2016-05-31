package com.cc.xsl.practiceexplain;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by xushuailong on 2016/5/31.
 */
public class WidgetActivity extends Activity{
    private static final String[] mFamous = {
            "A Dream of Read Mansions",
            "All Men Are Brothers",
            "Journey to the West",
            "The Romance of the Three Kingdoms"};
    private static final String[] WORDS = new String[]{"Child","Chile","China","Chinese"};
    private Spinner spinner;
    private AutoCompleteTextView auto_text;
    private Button btn_get_date;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private Calendar calendar;
    private ArrayAdapter<String> spinnerAdapter;
    private ArrayAdapter<String> autoAdapter;
    private int resId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wiget);
        initViews();
        viewEvents();
    }

    private void initViews() {
        btn_get_date = (Button) findViewById(R.id.btn_get_date);
        timePicker = (TimePicker) findViewById(R.id.timePicker);
        datePicker = (DatePicker) findViewById(R.id.dataPicker);
        auto_text = (AutoCompleteTextView) findViewById(R.id.auto_text);
        spinner = (Spinner) findViewById(R.id.spinner);
        calendar = Calendar.getInstance();
        spinnerAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,mFamous);
        autoAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,WORDS);
        resId = android.R.layout.simple_spinner_dropdown_item;
        spinnerAdapter.setDropDownViewResource(resId);
    }

    private void viewEvents() {
        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(year,monthOfYear,dayOfMonth);
            }
        });
        timePicker.setIs24HourView(true);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
            }
        });
        btn_get_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MINUTE);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                Toast.makeText(WidgetActivity.this,
                        "Now is : "+year+"-"+month+"-"+day+"   "+hour+" : "+minute,
                        Toast.LENGTH_SHORT).show();
            }
        });
        auto_text.setAdapter(autoAdapter);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(WidgetActivity.this, mFamous[position], Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
