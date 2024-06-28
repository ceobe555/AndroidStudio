package com.example.billapp;

import android.app.DatePickerDialog;
import android.content.Intent;
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

import com.example.billapp.Util.DateUtil;
import com.example.billapp.Util.ToastUtil;
import com.example.billapp.datebase.BillDBHelper;
import com.example.billapp.entity.BillInfo;

import java.util.Calendar;

public class BillAddActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private TextView tv_date;
    private Calendar calendar;
    private EditText et_remark;
    private EditText et_amount;
    private RadioGroup rg_type;
    private BillDBHelper mDBHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bill_add);

        TextView tv_title = findViewById(R.id.tv_title);
        TextView tv_option = findViewById(R.id.tv_option);
        tv_title.setText("请填写账单");
        tv_option.setText("账单列表");

        tv_option.setOnClickListener(this);
        findViewById(R.id.iv_back).setOnClickListener(this);

        tv_date = findViewById(R.id.tv_date);
        rg_type = findViewById(R.id.rg_type);
        et_remark = findViewById(R.id.et_remark);
        et_amount = findViewById(R.id.et_amount);
        // 显示当前日期
        calendar = Calendar.getInstance();
        tv_date.setText(DateUtil.getDate(calendar));
        tv_date.setOnClickListener(this);

        findViewById(R.id.btn_save).setOnClickListener(this);

        mDBHelper = BillDBHelper.getInstance(this);
        mDBHelper.openReadLink();
        mDBHelper.openWriteLink();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_date) {
            // 弹出日期对话框
            DatePickerDialog dialog = new DatePickerDialog(this, this,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
            dialog.show();
        }
        else if (v.getId() == R.id.btn_save) {
            BillInfo bill = new BillInfo();
            bill.date = tv_date.getText().toString();
            bill.type = rg_type.getCheckedRadioButtonId() == R.id.rb_income ?
                    BillInfo.BILL_TYPE_INCOME : BillInfo.BILL_TYPE_COST;
            bill.remark = et_remark.getText().toString();
            bill.amount = Double.parseDouble(et_amount.getText().toString());

            if (mDBHelper.save(bill) > 0) {
                ToastUtil.show(this, "添加账单成功！");
            }
        }
        else if (v.getId() == R.id.tv_option) {
            Intent intent = new Intent(this, BillPagerActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        }
        else if (v.getId() == R.id.iv_back) {
            finish();

        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        // 设置给文本显示
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        tv_date.setText(DateUtil.getDate(calendar));
    }


}