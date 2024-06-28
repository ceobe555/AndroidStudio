package com.example.ledgerapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ledgerapp.Database.LedgerDBHelper;
import com.example.ledgerapp.Entity.BillInfo;
import com.example.ledgerapp.Util.DateUtil;
import com.example.ledgerapp.Util.ToastUtil;

import java.util.Calendar;

public class BillAddActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private TextView tv_date;
    private RadioGroup rg_type;
    private EditText et_remark;
    private EditText et_amount;
    private Calendar calendar;
    private Spinner sp_type;
    private LedgerDBHelper mDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bill_add);

        TextView tv_title = findViewById(R.id.tv_title);
        TextView tv_option = findViewById(R.id.tv_option);
        tv_title.setText("请填写账单");
        tv_option.setText("");

        tv_date = findViewById(R.id.tv_date);
        rg_type = findViewById(R.id.rg_type);
        sp_type = findViewById(R.id.sp_type);
        et_remark = findViewById(R.id.et_remark);
        et_amount = findViewById(R.id.et_amount);
        // 显示现在的日期
        calendar = Calendar.getInstance();
        tv_date.setText(DateUtil.getDate(calendar));

        // 设置类型的下拉菜单
        String[] aTypes = getResources().getStringArray(R.array.bill_type);
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, aTypes);
        sp_type.setAdapter(typeAdapter);

        // for Database
        mDBHelper = LedgerDBHelper.getInstance(this);
        mDBHelper.openReadLink();
        mDBHelper.openWriteLink();

        tv_date.setOnClickListener(this);
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
            BillInfo bill = new BillInfo();
            bill.date = tv_date.getText().toString();
            bill.bExpenses = rg_type.getCheckedRadioButtonId() == R.id.rb_income ?
                    BillInfo.BILL_TYPE_INCOME : BillInfo.BILL_TYPE_COST;
            bill.type = sp_type.getSelectedItemPosition();
            bill.remark = et_remark.getText().toString();
            bill.amount = Double.parseDouble(et_amount.getText().toString());

            if (mDBHelper.save(bill) > 0) {
                ToastUtil.show(this,"添加账单成功！");
            }
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