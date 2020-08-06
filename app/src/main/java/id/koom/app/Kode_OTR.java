package id.koom.app;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import id.koom.app.API.ApiInterface;

import id.koom.app.R;

import id.koom.app.helper.OffDBHelper;
import id.koom.app.helper.RequestHandler;
import id.koom.app.utils.SharedPrefManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Kode_OTR extends AppCompatActivity {

    private String verificationid;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private EditText editText;
    private OffDBHelper db;
    private String android_id;
    private SharedPrefManager SPManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kode__otr);

        mAuth = FirebaseAuth.getInstance();
        db = new OffDBHelper(this);

        progressBar = findViewById(R.id.progressbar);
        editText = findViewById(R.id.etOtp);

        SPManager = new SharedPrefManager(this);

        // get android id
        android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        String phonenumber = getIntent().getStringExtra("phonenumber");
        sendVerificationCode(phonenumber);

        findViewById(R.id.buttonSignIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String code = editText.getText().toString().trim();

                if ((code.isEmpty() || code.length() < 6)){

                    editText.setError("Enter code...");
                    editText.requestFocus();
                    return;
                }
                verifyCode(code);

            }
        });
    }

    private void verifyCode(String code){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationid, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String uid = mAuth.getCurrentUser().getUid();
                            String phone = mAuth.getCurrentUser().getPhoneNumber();

                            SPManager.saveSPString("phone", phone);

                            db.saveUser(uid, phone);

                            StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiInterface.URL_ADD_DAFTAR, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try{
                                        // mengambil variable dari json
                                        JSONObject jsonObject = new JSONObject(response);
                                    }catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(Kode_OTR.this, "Failed to Data",Toast.LENGTH_SHORT).show();
                                }
                            }){
                                protected Map<String , String> getParams() throws AuthFailureError {
                                    Map<String , String> params = new HashMap<>();
                                    params.put("uid", uid);
                                    params.put("email_phone", phone);
                                    params.put("android_id", android_id);
                                    return params;
                                }
                            };
                            RequestHandler.getInstance(Kode_OTR.this).addToRequestQueue(stringRequest);

                            Intent intent = new Intent(Kode_OTR.this, MainApp.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                            startActivity(intent);

                        } else {
//                            Toast.makeText(Kode_OTR.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            Toast.makeText(Kode_OTR.this, "Kode OTP Salah!", Toast.LENGTH_LONG).show();
                        }
                    }

                });
    }


    private void sendVerificationCode(String number){

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );
    }


    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationid = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null){
                progressBar.setVisibility(View.VISIBLE);
                verifyCode(code);
                Toast.makeText(Kode_OTR.this, "Verification Complete",Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(Kode_OTR.this, "Anda tidak dapat register, cobalah beberapa saat lagi!",Toast.LENGTH_LONG).show();
            }
        }


        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(Kode_OTR.this, "Nomor anda tidak valid!",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Kode_OTR.this, SignUp.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);
        }
    };


}
