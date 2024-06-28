package com.example.billapp.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.billapp.R;
import com.example.billapp.adapter.BillListAdapter;
import com.example.billapp.datebase.BillDBHelper;
import com.example.billapp.entity.BillInfo;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BillFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BillFragment extends Fragment {


    public static Fragment newInstance(String yearMonth) {
        BillFragment fragment = new BillFragment();
        Bundle args = new Bundle();
        args.putString("yearMonth", yearMonth);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bill, container, false);
        ListView lv_bill = view.findViewById(R.id.lv_bill);
        BillDBHelper mDBHelper = BillDBHelper.getInstance(getContext());
        Bundle arguments = getArguments();
        String yearMonth = arguments.getString("yearMonth");

        List<BillInfo> billInfoList = mDBHelper.queryByMonth(yearMonth);
        BillListAdapter adapter = new BillListAdapter(getContext(), billInfoList);
        lv_bill.setAdapter(adapter);
        return view;
    }
}