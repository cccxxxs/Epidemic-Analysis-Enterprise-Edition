package com.silentselene.enterpriseedition.ui.home;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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
                        Iterator<String> timeIterator = result.keys();
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
                    //                    final List<AccessRecord> accessRecordList = new ArrayList<>();
                    //                    for (int i = 0; i < jsonArray.length(); i++) {
                    //                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                    //                        String mac = jsonObject.getString("mac");
                    //                        int user_level = jsonObject.getInt("user_level");
                    //                        String date_str = jsonObject.getString("visitTime");
                    //                        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date_str);
                    //                        assert date != null;
                    //                        accessRecordList.add(new AccessRecord(mac, 0, user_level,
                    //                                new Date(date.getTime() - DateUtils.DAY_IN_MILLIS * 3), new Date(date.getTime() + DateUtils.DAY_IN_MILLIS * 3), date));
                    //                        Log.d("POST_danger_item", "mac: " + mac + " user_level: " + user_level + " date: " + date);
                    //                    }
                    //                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                    //                        @Override
                    //                        public void run() {
                    //                            if (context.stateList.get(0).state == AccessRecord.HIGH_RISK) return;
                    //                            PredictOnePerson predictOnePerson = new PredictOnePerson(context.getMyWifiList(), accessRecordList);
                    //                            int res = predictOnePerson.getJudgeLevel();
                    //                            float num = predictOnePerson.getPredict();
                    //                            if (res == AccessRecord.HIGH_RISK) {
                    //                                context.updateStateTo(new Date().getTime(), AccessRecord.HIGH_RISK);
                    //                                context.updateUi();
                    //                            }
                    //                        }
                    //                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void drawCharts(View view, Set<String> strings) {

    }


}