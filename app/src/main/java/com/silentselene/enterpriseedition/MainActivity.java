package com.silentselene.enterpriseedition;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.silentselene.enterpriseedition.login.LoginPage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static com.silentselene.enterpriseedition.ModifyUI.setStatusBar;


public class MainActivity extends AppCompatActivity {

    private final String FILE_NAME = "config.ini";

    private String usr_info = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        setStatusBar(MainActivity.this, true, true);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_notifications, R.id.navigation_dashboard)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        try{
            loadPreferencesFile();
        }catch (IOException e){
            e.printStackTrace();
        }

        Log.d("FILE", "\n" + usr_info);

        if(usr_info == null){
            Intent loginPage = new Intent(MainActivity.this, LoginPage.class);
            startActivity(loginPage);
        }
    }

    private void loadPreferencesFile() throws IOException {
        try {
            FileInputStream fis = openFileInput(FILE_NAME);
            if (fis.available() == 0) {
                return;
            }
            byte[] readBytes = new byte[fis.available()];
            while (fis.read(readBytes) != -1) {
            }
            usr_info = new String(readBytes);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}