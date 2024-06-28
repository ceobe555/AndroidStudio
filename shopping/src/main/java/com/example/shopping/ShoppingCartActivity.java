package com.example.shopping;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.shopping.adapter.CartAdapter;

import java.util.HashMap;
import java.util.List;

import database.ShoppingDBHelper;
import entities.CartInfo;
import entities.GoodsInfo;
import util.ToastUtil;

public class ShoppingCartActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private TextView tv_count;
    private ListView lv_cart;
    private LinearLayout ll_empty;
    private LinearLayout ll_content;
    private ShoppingDBHelper mDBHelper;
    private List<CartInfo> mCartList;
    private TextView tv_total_price;
    private HashMap<Integer, GoodsInfo> mGoodsMap = new HashMap<>();
    private CartAdapter mCartAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_shopping_cart);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_shopping_cart), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText("购物车");
        lv_cart = findViewById(R.id.lv_cart);
        ll_empty = findViewById(R.id.ll_empty);
        ll_content = findViewById(R.id.ll_content);

        tv_total_price = findViewById(R.id.tv_total_price);

        tv_count = findViewById(R.id.tv_count);
        tv_count.setText(String.valueOf(MyApplication.getInstance().goodsCount));

        mDBHelper = ShoppingDBHelper.getInstance(this);

        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.btn_shopping_channel).setOnClickListener(this);
        findViewById(R.id.btn_clear).setOnClickListener(this);
        findViewById(R.id.btn_settle).setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        showCart();
    }

    // 展示购物车中的商品列表
    private void showCart() {
        // 查询购物车数据库表中的所有商品记录
        mCartList = mDBHelper.queryAllCartInfo();
        if (mCartList.size() == 0) {
            ll_empty.setVisibility(View.VISIBLE);
            return;
        }

        for (CartInfo info : mCartList) {
            // 根据商品编号查询商品数据库中的商品记录
            GoodsInfo goods = mDBHelper.queryGoodsInfoById(info.goodsId);
            mGoodsMap.put(info.goodsId, goods);
            info.goods = goods;
        }
        mCartAdapter = new CartAdapter(this, mCartList);
        lv_cart.setAdapter(mCartAdapter);
        // 给商品行添加点击事件
        lv_cart.setOnItemClickListener(this);

        // 给商品行添加长按事件
        lv_cart.setOnItemLongClickListener(this);

        refreshTotalPrice();
    }

    private void deleteGoods(CartInfo info) {
        MyApplication.getInstance().goodsCount -= info.count;

        mDBHelper.deleteCartInfoByGoodsId(info.goodsId);

        CartInfo removed = null;
        for (CartInfo cartInfo : mCartList) {
            if (info.goodsId == cartInfo.goodsId) {
                removed = cartInfo;
                break;
            }
        }
        mCartList.remove(removed);

        showCount();
        ToastUtil.show(this, "已从购物车中删除" + mGoodsMap.get(info.goodsId).name);
        mGoodsMap.remove(info.goodsId);

        refreshTotalPrice();
    }

    private void showCount() {
        tv_count.setText(String.valueOf(MyApplication.getInstance().goodsCount));
        if (MyApplication.getInstance().goodsCount == 0) {
            ll_empty.setVisibility(View.VISIBLE);
            ll_content.setVisibility(View.GONE);
            // 通知适配器发生数据变化
            mCartAdapter.notifyDataSetChanged();
        } else {
            ll_content.setVisibility(View.VISIBLE);
            ll_empty.setVisibility(View.GONE);
        }
    }

    private void refreshTotalPrice() {
        int totalPrice = 0;
        for (CartInfo info : mCartList) {
            GoodsInfo goods = mGoodsMap.get(info.goodsId);
            totalPrice += goods.price * info.count;
        }
        tv_total_price.setText(String.valueOf(totalPrice));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_back) {
            finish();
        } else if (v.getId() == R.id.btn_shopping_channel) {
            Intent intent = new Intent(this, ShoppingChannelActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_clear) {
            mDBHelper.deleteAllCartInfo();
            MyApplication.getInstance().goodsCount = 0;
            showCount();
            ToastUtil.show(this, "购物车已清空");
        } else if (v.getId() == R.id.btn_settle) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("结算商品");
            builder.setMessage("客官抱歉，支付功能尚未开通，请下次再来");
            builder.setPositiveButton("我知道了", null);
            builder.create().show();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(ShoppingCartActivity.this, ShoppingDetailActivity.class);
        intent.putExtra("goods_id", mCartList.get(position).goodsId);
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        CartInfo info = mCartList.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(ShoppingCartActivity.this);
        builder.setMessage("是否从购物车删除" + info.goods.name + "? ");
        builder.setPositiveButton("是", (dialog, which) -> {
            mCartList.remove(position);
            // 通知适配器发生数据变化
            mCartAdapter.notifyDataSetChanged();
            deleteGoods(info);
        });
        builder.setNegativeButton("否", null);
        builder.create().show();
        return true;
    }
}