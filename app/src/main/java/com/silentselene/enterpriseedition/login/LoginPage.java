package com.silentselene.enterpriseedition.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.silentselene.enterpriseedition.MainActivity;
import com.silentselene.enterpriseedition.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginPage extends AppCompatActivity {

    private EditText usr_id;
    private EditText usr_pwd;

    private static final String FILE_NAME = "config.ini";
    private String usr_phone = null;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_login);

        usr_id = (EditText)findViewById(R.id.usr_id);
        usr_pwd = (EditText)findViewById(R.id.usr_pwd);

        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = usr_id.getText().toString();
                String pwd = usr_pwd.getText().toString();

                if(id.length()==0){
                    Toast.makeText(getApplicationContext(), "请填写账号", Toast.LENGTH_SHORT).show();
                    return;
                }
                shopUserLogin(id, pwd);
            }
        });

        findViewById(R.id.btn_sign_up).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int SUB_ACTIVITY_REGISTER = 1;
                Intent register_page = new Intent(LoginPage.this, RegisterPage.class);
                startActivityForResult(register_page, SUB_ACTIVITY_REGISTER);
            }
        });
    }

    private void shopUserLogin(String id, String pwd){
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("phone", id)
                .add("password", pwd)
                .build();
        Request request = new Request.Builder().url("http://123.56.117.101:8080/shop_userLogin").post(requestBody).build();

        Call call = okHttpClient.newCall(request);//发送请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginPage.this, "账号或密码错误", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();

                if(result.equals("")){
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginPage.this, "账号或密码错误", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }

                Log.d("POST", "result: " + result);
                try {
                    JSONObject shopInfo = new JSONObject(result);
                    String phone = (String)shopInfo.get("phone");
                    String shopName = (String)shopInfo.get("shop_name");
                    String shopLoc = (String)shopInfo.get("shop_location");

                    Log.d("POST", "phone: " + phone);
                    Log.d("POST", "name: " + shopName);
                    Log.d("POST", "location: " + shopLoc);

                    savePreferenceFiles(phone, shopName, shopLoc);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginPage.this, "登录成功", Toast.LENGTH_SHORT).show();
                    }
                });
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent loginPage = new Intent(LoginPage.this, MainActivity.class);
                loginPage.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(loginPage);
            }
        });
    }

    private void savePreferenceFiles(String phone, String shop_name, String shop_loc) throws IOException {
        FileOutputStream fos = openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
        String result = phone + "\n" + shop_name + "\n" + shop_loc;
        fos.write(result.getBytes());
        fos.flush();
        fos.close();
    }

    public static boolean isLogin(Context context) {
        try {
            FileInputStream fis = context.openFileInput(FILE_NAME);
            try {
                if (fis.available() == 0) {
                    return false;
                }
            }catch (IOException ie){
                ie.printStackTrace();
            }
        }catch (FileNotFoundException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
