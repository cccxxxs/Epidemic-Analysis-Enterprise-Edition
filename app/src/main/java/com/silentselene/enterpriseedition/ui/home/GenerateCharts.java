package com.silentselene.enterpriseedition.ui.home;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import tech.linjiang.suitlines.SuitLines;
import tech.linjiang.suitlines.Unit;

public class GenerateCharts {

    static HashMap<String, HashMap<Integer, HashSet<String>>> info;

    static void getDangerList(final Context context, String mac) {
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("macList", mac)
                .build();
        Request request = new Request.Builder().url("http://123.56.117.101:8081/getDangerList").post(requestBody).build();
        Call call = okHttpClient.newCall(request);//发送请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "Request failed", Toast.LENGTH_SHORT).show();
                    }
                });
                Log.d("POST_getDangerList_faild", "result: " + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responsr_str = response.body().string();
                Log.d("POST_getDangerList_success", "result: " + responsr_str);
                try {
                    JSONObject jsonObject = new JSONObject(responsr_str);
                    String status = jsonObject.getString("status");
                    if (!status.equals("success")) {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, "Request failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                        return;
                    }
                    JSONObject result = jsonObject.getJSONObject("result");
                    Iterator<String> iterator = result.keys();
                    HashMap<String, HashMap<Integer, HashSet<String>>> ret = new HashMap<>();
                    while (iterator.hasNext()) {
                        String mac = iterator.next();
                        ret.put(mac, new HashMap<Integer, HashSet<String>>());
                        JSONObject times = result.getJSONObject(mac);
                        Iterator<String> timeIterator = times.keys();
                        while (timeIterator.hasNext()) {
                            String time = timeIterator.next();
                            ret.get(mac).put(Integer.parseInt(time), new HashSet<String>());
                            JSONObject users = times.getJSONObject(time);
                            Iterator<String> userIterator = users.keys();
                            while (userIterator.hasNext()) {
                                ret.get(mac).get(Integer.parseInt(time)).add(userIterator.next());
                            }
                        }
                    }
                    info = ret;
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "Request success", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void drawCharts(SuitLines suitLines, String mac) {
        Calendar c = Calendar.getInstance();
        int now_hour = c.get(Calendar.HOUR_OF_DAY);
        long time = c.getTimeInMillis();
        time = time / (1000 * 60 * 60);
        time = time - 7 * 24;
        int hour = c.get(Calendar.HOUR_OF_DAY);

        int[] color = {Color.RED, Color.GRAY, Color.BLACK, Color.BLUE, 0xFFF76055, 0xFF9B3655, 0xFFF7A055};
        SuitLines.LineBuilder builder = new SuitLines.LineBuilder();
        for (int j = 0; j < 7; j++) {
            List<Unit> lines = new ArrayList<>();
            info.get(mac);
            for (int i = 0; i < 24; i++) {
                time = time + 1;
                c.setTimeInMillis(time * (1000 * 60 * 60));
                hour = c.get(Calendar.HOUR_OF_DAY);

                int val = 0;
                HashMap<Integer, HashSet<String>> hashSetHashMap = info.get(mac);
                if (hashSetHashMap != null) {
                    HashSet<String> stringHashSet = hashSetHashMap.get((int) time);
                    if (stringHashSet != null) {
                        val = stringHashSet.size();
                    }
                }
//                lines.add(new Unit(new SecureRandom().nextInt(128), (hour > now_hour ? "Y+" :
//                        "T+") + hour));
                lines.add(new Unit(val, (hour > now_hour ? "Y+" :
                        "T+") + hour));
            }
            int[] blue = {0xffd0e7ef, 0xffa5d4e2, 0xff6fc1c9, 0xff419eb7, 0xff3c919f, 0xff327c87, 0xff26616c};
            builder.add(lines, blue[j]);
        }
        builder.build(suitLines, false);
    }
}