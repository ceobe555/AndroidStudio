package com.example.ledgerapp.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.ledgerapp.Entity.BillInfo;
import com.example.ledgerapp.R;

import java.util.List;

public class BillListAdapter extends BaseAdapter {
    private final List<BillInfo> mBillList;

    private final Context mContext;

    public BillListAdapter(Context context, List<BillInfo> billInfoList) {
        this.mContext = context;
        this.mBillList = billInfoList;
    }
    @Override
    public int getCount() {
        return mBillList.size();
    }

    @Override
    public Object getItem(int position) {
        return mBillList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mBillList.get(position).id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_bill, null);
            holder.tv_date = convertView.findViewById(R.id.tv_date);
            holder.tv_type = convertView.findViewById(R.id.tv_type);
            holder.tv_remark = convertView.findViewById(R.id.tv_remark);
            holder.tv_amount = convertView.findViewById(R.id.tv_amount);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        BillInfo bill = mBillList.get(position);
        holder.tv_date.setText(bill.date);
        holder.tv_remark.setText(bill.remark);
        if (bill.bExpenses == 0) {
            holder.tv_amount.setTextColor(Color.RED);
            holder.tv_amount.setText(String.format("%s%.2f元", "+", bill.amount));
        }
        else if (bill.bExpenses == 1) {
            holder.tv_amount.setTextColor(Color.BLACK);
            holder.tv_amount.setText(String.format("%s%.2f元", "-", bill.amount));
        }

        String[] aTypes = convertView.getResources().getStringArray(R.array.bill_type);
        holder.tv_type.setText(aTypes[bill.type]);

        return convertView;
    }

    public final class ViewHolder {
        public TextView tv_date;
        public TextView tv_type;
        public TextView tv_remark;
        public TextView tv_amount;
    }
}
