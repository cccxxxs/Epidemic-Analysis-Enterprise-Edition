package com.silentselene.enterpriseedition.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.silentselene.enterpriseedition.R;

import java.io.IOException;
import java.util.Calendar;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.silentselene.enterpriseedition.ModifyUI.setStatusBar;

public class RegisterPage extends AppCompatActivity {

    private EditText usr_tel;
    private EditText usr_code;
    private EditText usr_id_card;
    private EditText usr_pwd;
    private EditText check_pwd;
    private EditText get_shop_name;
    private EditText get_shop_loc;

    private String check_code;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_register);
        
        setStatusBar(RegisterPage.this, true, true);

        usr_tel = (EditText) findViewById(R.id.usr_tel);
        usr_code = (EditText) findViewById(R.id.usr_code);
        usr_id_card = (EditText) findViewById(R.id.usr_id_card);
        usr_pwd = (EditText) findViewById(R.id.set_pwd);
        check_pwd = (EditText) findViewById(R.id.set_pwd_again);
        get_shop_name = (EditText) findViewById(R.id.shop_name);
        get_shop_loc = (EditText) findViewById(R.id.shop_loc);

        findViewById(R.id.btn_get_code).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tel = usr_tel.getText().toString();

                if(tel.length()<11){
                    Toast.makeText(getApplicationContext(), "手机号错误", Toast.LENGTH_SHORT).show();
                    return;
                }

                getCheck_code(tel);
            }
        });

        findViewById(R.id.btn_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tel = usr_tel.getText().toString();
                String code = usr_code.getText().toString();
                String id_card = usr_id_card.getText().toString();
                String pwd = usr_pwd.getText().toString();
                String check = check_pwd.getText().toString();
                String shop_name = get_shop_name.getText().toString();
                String shop_loc = get_shop_loc.getText().toString();

                if (pwd.length() < 8) {
                    Toast.makeText(getApplicationContext(), "密码至少为8位", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!pwd.equals(check)) {
                    Toast.makeText(getApplicationContext(), "两次密码不同!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (id_card.length() != 18) {
                    Toast.makeText(getApplicationContext(), "身份证错误!", Toast.LENGTH_SHORT).show();

                }

                uploadRegisterInfo(tel, pwd, id_card, shop_name, shop_loc, code);
                //Toast.makeText(getApplicationContext(),"注册成功，等待审核",Toast.LENGTH_SHORT).show();
            }
        });
    }

    void getCheck_code(String usr_tel){
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("phone", usr_tel)
                .build();
        Request request = new Request.Builder().url("http://123.56.117.101:8080/getRandomNum").post(requestBody).build();

        Call call = okHttpClient.newCall(request);//发送请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(RegisterPage.this, "验证码发送失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("POST", "result: " + response.body().string());
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(RegisterPage.this, "验证码发送成功", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    void uploadRegisterInfo(String user_tel, String pwd, String ID_card, String shop_name, String shop_loc, String random) {
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("shop_userPhone", user_tel)
                .add("password", pwd)
                .add("ID_card", ID_card)
                .add("shop_name", shop_name)
                .add("shop_location", shop_loc)
                .add("random", random)
                .build();
        Request request = new Request.Builder().url("http://123.56.117.101:8080/shop_userRegister").post(requestBody).build();

        Call call = okHttpClient.newCall(request);//发送请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(RegisterPage.this, "注册失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.d("POST", "result: " + result);
                if(result.equals("false")){
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegisterPage.this, "信息错误，请重试", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegisterPage.this, "注册成功，等待审核", Toast.LENGTH_SHORT).show();
                        }
                    });
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    finish();
                }
            }
        });
    }
}
