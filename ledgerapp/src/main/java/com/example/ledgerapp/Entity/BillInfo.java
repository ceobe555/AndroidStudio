package com.example.ledgerapp.Entity;

public class BillInfo {
    public int id;
    public String date;
    public int bExpenses;
    public int type;
    public double amount;
    public String remark;

    // 账单类型 0-收入 1-支出
    public static final int BILL_TYPE_INCOME = 0;
    public static final int BILL_TYPE_COST = 1;

    public String toString() {
        return "BillInfo{" +
                "id=" + id +
                ", date=" + date + '\'' +
                ", bExpenses=" + bExpenses +
                ", type=" + type +
                ", amount=" + amount +
                ", remark=" + remark +
                "}";

    }
}
