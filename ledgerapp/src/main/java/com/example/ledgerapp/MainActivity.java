package com.example.ledgerapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager.widget.PagerTabStrip;
import androidx.viewpager.widget.ViewPager;

import com.example.ledgerapp.Adapter.BillPagerAdapter;
import com.example.ledgerapp.Database.LedgerDBHelper;
import com.example.ledgerapp.Util.DateUtil;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private TextView tv_month;
    private Calendar calendar;
    private LedgerDBHelper mDBHelper;
    private ViewPager vp_bill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView tv_title = findViewById(R.id.tv_title);
        TextView tv_option = findViewById(R.id.tv_option);
        tv_title.setText("记账小本本");
        tv_option.setText("添加账单");

        tv_option.setOnClickListener(this);
        findViewById(R.id.iv_back).setOnClickListener(this);

        tv_month = findViewById(R.id.tv_month);
        calendar = Calendar.getInstance();
        tv_month.setText(DateUtil.getMonth(calendar));
        tv_month.setOnClickListener(this);

        mDBHelper = LedgerDBHelper.getInstance(this);
        mDBHelper.openReadLink();
        mDBHelper.openWriteLink();

        initViewPager();
    }

    // 初始化翻页视图
    private void initViewPager() {
        PagerTabStrip pts_bill = findViewById(R.id.pts_bill);
        pts_bill.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
        vp_bill = findViewById(R.id.vp_bill);
        BillPagerAdapter adapter = new BillPagerAdapter(getSupportFragmentManager(), calendar.get(Calendar.YEAR));
        vp_bill.setAdapter(adapter);
        vp_bill.setCurrentItem(calendar.get(Calendar.MONTH));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_option) {
            Intent intent = new Intent(this, BillAddActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        else if (v.getId() == R.id.iv_back) {
            finish();
        }
        else if (v.getId() == R.id.tv_month) {
            DatePickerDialog dialog = new DatePickerDialog(
                    this,
                    this,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
            dialog.show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initViewPager();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        // 更新日期的文本显示
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        tv_month.setText(DateUtil.getMonth(calendar));
        vp_bill.setCurrentItem(month);
    }
}