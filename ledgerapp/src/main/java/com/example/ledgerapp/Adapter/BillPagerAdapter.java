package com.example.ledgerapp.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.ledgerapp.Fragment.BillFragment;

public class BillPagerAdapter extends FragmentStateAdapter {
    private int mYear;

    public BillPagerAdapter(@NonNull FragmentActivity fragmentActivity, int year) {
        super(fragmentActivity);
        this.mYear = year;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        int month = position + 1;
        return BillFragment.newInstance(mYear, month);
    }

    @Override
    public int getItemCount() {
        return 12;
    }

    public CharSequence getPageTitle(int position) {
        return (position + 1) + "月份";
    }

}
