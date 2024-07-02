package com.example.ledgerapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ledgerapp.Database.LedgerDBHelper;
import com.example.ledgerapp.Entity.BillInfo;

import java.util.List;

public class MineActivity extends AppCompatActivity implements View.OnClickListener {

    private LedgerDBHelper mDBHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mine);

        // set header title
        TextView tv_title = findViewById(R.id.tv_title);
        TextView tv_option = findViewById(R.id.tv_option);
        tv_title.setText("个人主页");
        tv_option.setText("");

        findViewById(R.id.tv_home).setOnClickListener(this);
        findViewById(R.id.tv_statistics).setOnClickListener(this);
        findViewById(R.id.iv_back).setOnClickListener(this);

        mDBHelper = LedgerDBHelper.getInstance(this);
        mDBHelper.openReadLink();
        mDBHelper.openWriteLink();

        TextView tv_balance = findViewById(R.id.tv_balance);
        double dBalance = 0.0;
        // compute balance
        List<BillInfo> billInfoList = mDBHelper.queryAllBill();
        for (int ii = 0; ii < billInfoList.size(); ii++) {
            BillInfo bill = billInfoList.get(ii);
            if (bill.bExpenses == 0) {
                dBalance += bill.amount;
            }
            else {
                dBalance -= bill.amount;
            }
        }
        tv_balance.setText(String.valueOf(dBalance));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_statistics) {
            Intent intent = new Intent(this, StatisticActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        else if (v.getId() == R.id.tv_home) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        else if (v.getId() == R.id.iv_back) {
            finish();
        }
    }
}