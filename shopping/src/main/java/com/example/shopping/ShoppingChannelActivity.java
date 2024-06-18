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

import java.util.List;

import database.ShoppingDBHelper;
import entities.GoodsInfo;
import util.ToastUtil;

public class ShoppingChannelActivity extends AppCompatActivity implements View.OnClickListener {

    private ShoppingDBHelper mDBHelper;
    private TextView tv_count;
    private GridLayout gl_Channel;
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
        gl_Channel = findViewById(R.id.gl_channel);
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
        // 设置布局的宽度为屏幕的一半
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(screenWidth / 2, ViewGroup.LayoutParams.WRAP_CONTENT);

        List<GoodsInfo> list = mDBHelper.queryAllGoodsInfo();

        // 添加子视图之前先清空子视图
        gl_Channel.removeAllViews();

        for (GoodsInfo info : list) {
            // 获取布局文件item_goods.xml的根视图
            View view = LayoutInflater.from(this).inflate(R.layout.item_goods, null);
            ImageView iv_thumb = view.findViewById(R.id.iv_thumb);
            TextView tv_name = view.findViewById(R.id.tv_name);
            TextView tv_price = view.findViewById(R.id.tv_price);
            Button btn_add = view.findViewById(R.id.btn_add);

            // 给控件设置值
            iv_thumb.setImageURI(Uri.parse(info.picPath));
            tv_name.setText(info.name);
            tv_price.setText(String.valueOf((int)info.price));
            // 添加到购物车
            btn_add.setOnClickListener(v -> {
                addToCart(info.id, info.name);
            });

            iv_thumb.setOnClickListener(v -> {
                Intent intent = new Intent(ShoppingChannelActivity.this, ShoppingDetailActivity.class);
                intent.putExtra("goods_id", info.id);
                startActivity(intent);
            });

            gl_Channel.addView(view, params);
        }
    }

    private void addToCart(int id, String goodsName) {
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
}