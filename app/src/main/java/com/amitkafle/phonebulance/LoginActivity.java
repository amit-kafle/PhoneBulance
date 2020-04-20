package com.amitkafle.phonebulance;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
EditText phonee,passwordet;
Button login;
CheckBox loginstate;
TextView signupn;
SharedPreferences sharedPreferences;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);

        phonee = findViewById(R.id.phone);
        passwordet = findViewById(R.id.password);
        login = findViewById(R.id.signin);
        loginstate = findViewById(R.id.combo);
        signupn = findViewById(R.id.signupa);
        signupn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
                finish();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtContact=phonee.getText().toString();
                String txtPassword=passwordet.getText().toString();
                if (TextUtils.isEmpty(txtContact) || TextUtils.isEmpty(txtPassword)){
            Toast.makeText(LoginActivity.this,"All Fields required",Toast.LENGTH_SHORT).show();
                }
                else {
                    login(txtContact,txtPassword);
                }
            }
        });
        String loginState=sharedPreferences.getString(getResources().getString(R.string.prefLoginState),"");
        if (loginState.equals("LoggedIN")){
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
        }
    }






    private  void login(final String phone, final String password){
        //final ProgressBar progressBar=new ProgressBar(LoginActivity.this);
            //progressBar.setIndeterminate(false);
        String uRl="http://192.168.0.105/Login.php";
        StringRequest request=new StringRequest(Request.Method.POST, uRl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("Login Success")){

                    Toast.makeText(LoginActivity.this,response,Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    if (loginstate.isChecked()){
                        editor.putString(getResources().getString(R.string.prefLoginState),"LoggedIN");
                    }else{
                        editor.putString(getResources().getString(R.string.prefLoginState),"LoggedOut");
                    }
                    editor.apply();

                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                }
                else{

                    Toast.makeText(LoginActivity.this,response,Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(LoginActivity.this,error.toString(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> param=new HashMap<>();
                param.put("phone",phone);
                param.put("password",password);

                return param;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(3000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getmInstance(LoginActivity.this).addToRequestQueue(request);

    }
}
