package entities;

import com.example.shopping.R;

import java.util.ArrayList;

public class GoodsInfo {
    public int id;
    public String name;
    public String description;
    public float price;
    public String picPath;
    // 大图的资源编号
    public int pic;

    // 声明一个手机商品的名称数组
    private static String[] mNameArray = {
      "红豆冰", "哈密瓜冰", "凤梨冰", "甜筒", "刨冰", "绵绵冰", "芒果冰", "碎冰冰", "上口爱"
    };
    // 声明一个手机商品的描述数组
    private static String[] mDescArray = {
        "地址：萍水街细冰",
        "地址：萍水街细冰",
        "地址：萍水街细冰",
        "网络图片",
        "网络图片",
        "网络图片",
        "网络图片",
        "网络图片",
        "网络图片"
    };
    private static float[] mPriceArray = {
            18, 30, 40, 20, 28, 48, 33, 7, 5
    };
    private static int[] mPicArray = {
            R.drawable.ice_red_bean,
            R.drawable.ice_hami_melon,
            R.drawable.ice_pineapple,
            R.drawable.ice_1,
            R.drawable.ice_2,
            R.drawable.ice_3,
            R.drawable.ice_4,
            R.drawable.ice_sbb,
            R.drawable.ice_ski
    };
    // 获取默认的手机信息列表
    public static ArrayList<GoodsInfo> getDefaultList() {
        ArrayList<GoodsInfo> goodsList = new ArrayList<GoodsInfo>();
        for (int ii = 0; ii < mNameArray.length; ii++) {
            GoodsInfo info = new GoodsInfo();
            info.id = ii;
            info.name = mNameArray[ii];
            info.description = mDescArray[ii];
            info.price = mPriceArray[ii];
            info.pic = mPicArray[ii];
            goodsList.add(info);
        }
        return goodsList;
    }
}
