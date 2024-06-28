package com.example.shopping;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.shopping.adapter.GoodsAdapter;

import java.util.List;

import database.ShoppingDBHelper;
import entities.GoodsInfo;
import util.ToastUtil;

public class ShoppingChannelActivity extends AppCompatActivity implements View.OnClickListener, GoodsAdapter.AddCartListener {

    private ShoppingDBHelper mDBHelper;
    private TextView tv_count;
    private GridView gv_channel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_shopping_channel);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_shopping_channel), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText("吃冰星期六");

        tv_count = findViewById(R.id.tv_count);
        gv_channel = findViewById(R.id.gv_channel);
        ImageView iv_back = findViewById(R.id.iv_back);
        ImageView iv_cart = findViewById(R.id.iv_cart);

        iv_back.setOnClickListener(this);
        iv_cart.setOnClickListener(this);

        mDBHelper = ShoppingDBHelper.getInstance(this);
        mDBHelper.openReadLink();
        mDBHelper.openWriteLink();

        // 从数据库查询商品信息并展示
        showGoods();
    }

    protected void onResume() {
        super.onResume();
        // 铲鲟购物车商品总数并展示
        showCartInfoTotal();
    }

    private void showCartInfoTotal() {
        int count = mDBHelper.countCartInfo();
        MyApplication.getInstance().goodsCount = count;
        tv_count.setText(String.valueOf(count));
    }

    private void showGoods() {
        List<GoodsInfo> list = mDBHelper.queryAllGoodsInfo();
        GoodsAdapter adapter = new GoodsAdapter(this, list, this);
        gv_channel.setAdapter(adapter);
    }

    @Override
    public void addToCart(int id, String goodsName) {
        mDBHelper.insertCartInfo(id);
        int count = ++MyApplication.getInstance().goodsCount;
        tv_count.setText(String.valueOf(count));
        ToastUtil.show(this, "已添加" + goodsName + "到购物车");


    }

    protected void onDestroy() {
        super.onDestroy();
        mDBHelper.closeLink();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.iv_back) {
            finish();
        }
        else if (id == R.id.iv_cart) {
            Intent intent = new Intent(this, ShoppingCartActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);

    }
}