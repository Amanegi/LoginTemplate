package com.example.logintemplate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.example.logintemplate.databinding.ActivityMain2Binding;

public class Main2Activity extends AppCompatActivity {
    private ActivityMain2Binding main2Binding;
    public static final String KEY_USERNAME = "username_key";
    public static final String KEY_PASSWORD = "password_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        main2Binding = DataBindingUtil.setContentView(this, R.layout.activity_main2);

        String username = getIntent().getStringExtra(KEY_USERNAME);
        String password = getIntent().getStringExtra(KEY_PASSWORD);

        main2Binding.textView.setText("Hello " + username + " !\nYour password is " + password);
    }

}
