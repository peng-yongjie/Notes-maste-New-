package com.soulocean.Diary;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class StartActivity extends AppCompatActivity {

    private EditText userSnoEditText;
    private SharedPreferences shp;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        shp = getSharedPreferences("info", Context.MODE_PRIVATE);


        if (shp.getBoolean("firstOpen",true)) {
            editor = shp.edit();
            editor.putString("password","0000");
            editor.putBoolean("firstOpen",false);
            editor.apply();
        }

        userSnoEditText = new EditText(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("请输入密码");
        builder.setMessage("小组成员：彭永杰、黄沃政、方俊濠");
        builder.setMessage("默认密码0000");
        builder.setView(userSnoEditText);
        builder.setPositiveButton("确定", (dialog, which) -> {
            if (userSnoEditText.getText().toString().trim().isEmpty()) {
                Toast.makeText(this, "密码格式不合法", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                if (userSnoEditText.getText().toString().trim().equals(shp.getString("password", null))) {
                    Intent intent = new Intent(StartActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(this, "密码错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
        builder.create();
        builder.show();
    }
}