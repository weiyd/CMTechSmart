package com.cmtech.android.account;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.cmtech.android.common.BasicActivity;
import com.cmtech.android.center.R;
import com.cmtech.android.common.LogUtil;
import com.cmtech.android.common.MyApplication;

import org.litepal.crud.DataSupport;

import java.util.List;

public class LoginActivity extends BasicActivity implements View.OnClickListener{
    private EditText etAccountName;
    private EditText etAccountPassword;
    private CheckBox cbRememeber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etAccountName = (EditText) findViewById(R.id.et_accountname);
        etAccountPassword = (EditText) findViewById(R.id.et_accountpassword);
        cbRememeber = (CheckBox) findViewById(R.id.cb_rememberpassword);

        Button btnRegister = (Button) findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(this);
        Button btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String name = etAccountName.getText().toString();
        String password = etAccountPassword.getText().toString();
        boolean exist;

        switch (v.getId()) {
            case R.id.btn_register:
                exist = MyApplication.accountExist(name);
                if(exist) {
                    Toast.makeText(LoginActivity.this, "账户名已存在", Toast.LENGTH_SHORT).show();
                }
                else {
                    MyApplication.saveNewAccount(name, password);
                    Toast.makeText(LoginActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.btn_login:
                exist = MyApplication.accountInfoCorrect(name, password);
                if(exist) {
                    ((MyApplication)getApplicationContext()).saveAccountInPreference(name, password, cbRememeber.isChecked());
                    AccountInfo account = new AccountInfo();
                    account.setName(name);
                    account.setPassword(password);
                    ((MyApplication)getApplicationContext()).setAccountInfo(account);
                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }



}
