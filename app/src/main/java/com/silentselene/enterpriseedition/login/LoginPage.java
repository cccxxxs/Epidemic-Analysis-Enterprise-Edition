package com.silentselene.enterpriseedition.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.silentselene.enterpriseedition.R;

public class LoginPage extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_login);

        findViewById(R.id.btn_sign_up).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int SUB_ACTIVITY_REGISTER = 1;
                Intent register_page = new Intent(LoginPage.this, RegisterPage.class);
                startActivityForResult(register_page, SUB_ACTIVITY_REGISTER);
            }
        });
    }
}
