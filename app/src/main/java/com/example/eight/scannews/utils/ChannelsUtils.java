package com.example.eight.scannews.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import org.litepal.crud.DataSupport;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;



public class ChannelsUtils {
    private static Context context;

    public ChannelsUtils(Context context) {
        ChannelsUtils.context = context;
    }

    /**
     * en : social
     * cn : 社会
     */

    public class ChannelBean {
        @SerializedName("en")
        private String en;
        @SerializedName("cn")
        private String cn;

        public String getEn() {
            return en;
        }

        public void setEn(String en) {
            this.en = en;
        }

        public String getCn() {
            return cn;
        }

        public void setCn(String cn) {
            this.cn = cn;
        }
    }


    public static boolean handleChannels(Context context) {                           //新建类继承自DataSupport，成员变量为频道的名称和标识符，解析频道列表的Json文件，遍历并存储频道
        InputStreamReader inputStreamReader = null;                                                               //字符输入流
        try {
            inputStreamReader = new InputStreamReader(context.getAssets()
                    .open("channel.json"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);                                  //从字符输入流中读取文本
            String line;
            StringBuilder stringBuffer = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }
            bufferedReader.close();
            inputStreamReader.close();
            Gson gson = new Gson();
            List<ChannelBean> channelsList = gson.fromJson(stringBuffer.toString(),
                    new TypeToken<List<ChannelBean>>(){}.getType());

            int i = 0;
            for (ChannelBean channelBean : channelsList) {
                Channels channels = new Channels();
                channels.setEn(channelBean.getEn());
                channels.setCn(channelBean.getCn());
                channels.setType(i);
                channels.save();                                                            //LitePal添加数据
                i++;
                Log.i("------>", "handleChannels: " + channelBean.getCn() + channelBean.getEn());
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    /*数据存储*/                 /**暂未实现自定义频道管理功能*/
    //创建 SharedPreferences变量，赋值为 getSharedPreferences("SETTING",MODE_PRIVATE)
    public static List<String> setupTab(String nameType) {
        List<Channels> channelsList = DataSupport.findAll(Channels.class);
        List<Channels> channels = new ArrayList<>();
        SharedPreferences sp = context.getSharedPreferences("SETTING", Context.MODE_PRIVATE);
        if (!sp.contains("selectedList")){
            channels = channelsList;
        } else {
            int size = sp.getInt("selectedList", 0);
            if (size == 0) {
                channels.add(channelsList.get(0));
            } else {
                String c;
                for (int i = 0; i < size; i++) {
                    c = sp.getString("selectedList" + i, null);
                    Log.e("-------->", "setupTab: " + c);
                    Log.e("-------->", "setupTab: " + DataSupport.where("cn = ?", c).find(Channels.class).get(0).getEn());
                    for (Channels cs : DataSupport.where("cn = ?", c).find(Channels.class)) {
                        channels.add(cs);
                    }
                }
            }

        }

        List<String> newsTab = new ArrayList<>();
        switch (nameType) {
            case "en":
                for (Channels c : channels) {
                    newsTab.add(c.getEn());
                }
                return newsTab;
            case "cn":
                for (Channels c : channels) {
                    newsTab.add(c.getCn());
                }
                return newsTab;
            default:
                return null;
        }
    }

}
