package com.example.ledgerapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ledgerapp.Util.DateUtil;

import java.util.Calendar;

public class BillAddActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private TextView tv_date;
    private RadioGroup rg_type;
    private EditText et_remark;
    private EditText et_amount;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bill_add);

        TextView tv_title = findViewById(R.id.tv_title);
        TextView tv_option = findViewById(R.id.tv_option);
        tv_title.setText("请填写账单");
        tv_option.setText("回到首页");

        tv_date = findViewById(R.id.tv_date);
        rg_type = findViewById(R.id.rg_type);
        et_remark = findViewById(R.id.et_remark);
        et_amount = findViewById(R.id.et_amount);
        // 显示现在的日期
        calendar = Calendar.getInstance();
        tv_date.setText(DateUtil.getDate(calendar));

        // for Database


        tv_date.setOnClickListener(this);
        tv_option.setOnClickListener(this);
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.btn_save).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_date) {
            DatePickerDialog dialog = new DatePickerDialog(
                    this,
                    this,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
            dialog.show();
        }
        else if (v.getId() == R.id.btn_save) {

        }
        else if (v.getId() == R.id.tv_option) {

        }
        else if (v.getId() == R.id.iv_back) {
            finish();
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        // 日期文本显示
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        tv_date.setText(DateUtil.getDate(calendar));
    }
}