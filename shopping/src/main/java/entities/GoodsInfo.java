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
      "iPhone11", "Mate30", "小米10", "OPPO Reno3", "vivo X30", "荣耀30S"
    };
    // 声明一个手机商品的描述数组
    private static String[] mDescArray = {
        "Apple iPhone11 256GB",
        "华为 HUAWEI Mate30 8GB+256GB",
        "小米 MI10 8GB+128GB",
        "OPPO Reno3 8GB+128GB",
        "vivo X30 8GB+128GB 5G全网通",
        "荣耀30S 8GB+128GB 5G芯片"
    };
    private static float[] mPriceArray = {
            6299, 4999, 3999, 2999, 2998, 2399
    };
    private static int[] mPicArray = {
            R.drawable.ceobe,
            R.drawable.fengdi,
            R.drawable.ling,
            R.drawable.ling2,
            R.drawable.nian,
            R.drawable.shu
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
