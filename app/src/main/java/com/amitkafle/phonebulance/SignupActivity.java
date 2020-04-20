package com.amitkafle.phonebulance;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.w3c.dom.Text;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends Activity {
    EditText fname, lname, password, contact,email;
    Button signupb;
    RadioGroup radioGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        fname=(EditText) findViewById(R.id.fname);
        lname=(EditText) findViewById(R.id.lname);
        password= (EditText)findViewById(R.id.passwordEt);
        email=(EditText) findViewById(R.id.email);
        contact=(EditText) findViewById(R.id.contact);
        signupb=(Button)findViewById(R.id.signupBtn);
        radioGroup=findViewById(R.id.radio_group);
        signupb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtFname=fname.getText().toString();
                String txtLname=lname.getText().toString();
                String txtContact= contact.getText().toString();
                String txtPassword=password.getText().toString();
                String txtEmail=email.getText().toString();

                if (TextUtils.isEmpty(txtFname) || TextUtils.isEmpty(txtLname)
                        || TextUtils.isEmpty(txtContact) || TextUtils.isEmpty(txtEmail) || TextUtils.isEmpty(txtPassword)){
                    Toast.makeText(SignupActivity.this,"All Fields required",Toast.LENGTH_SHORT).show();
                }
                else{
                    int genderId=radioGroup.getCheckedRadioButtonId();
                    RadioButton selected_Gender=radioGroup.findViewById(genderId);
                    if (selected_Gender==null){
                        Toast.makeText(SignupActivity.this,"Select gender please",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        String selectGender=selected_Gender.getText().toString();
                        registerNewAccount(txtFname,txtLname,txtEmail,txtContact,txtPassword,selectGender);
                    }
                }

            }
        });
    }
    private void registerNewAccount(final String fname, final String lname, final String email, final String contact, final String password, final String Gender){
//      final ProgressDialog progressDialog=new ProgressDialog(SignupActivity.this);
//      progressDialog.setCancelable(false);
//      progressDialog.setIndeterminate(false);
//      progressDialog.setTitle("Registering new account");
      String uRl="http://192.168.0.105/Register.php";
        StringRequest request= new StringRequest(Request.Method.POST, uRl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("Successfully Registered")){
//                    progressDialog.dismiss();
                    Toast.makeText(SignupActivity.this,response,Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignupActivity.this,LoginActivity.class));
                    finish();
                }
                else{
//                    progressDialog.dismiss();
                    Toast.makeText(SignupActivity.this,response,Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                progressDialog.dismiss();
                Toast.makeText(SignupActivity.this,error.toString(),Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String>param=new HashMap<>();
                param.put("fname",fname);
                param.put("lname",lname);
                param.put("email",email);
                param.put("phone",contact);
                param.put("password",password);
                param.put("gender",Gender);
                return param;
            }
        };

request.setRetryPolicy(new DefaultRetryPolicy(3000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getmInstance(SignupActivity.this).addToRequestQueue(request);
    }

}
