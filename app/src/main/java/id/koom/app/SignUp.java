package id.koom.app;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    private Spinner spinner;
    private String phonenumber;
    private EditText edtNomorHp;
    private SignInButton signIn;
    public static final int RC_SIGN_IN = 1;
    GoogleSignInClient mGoogleSignInClient;
    public static final String TAG = "MainActivity";
    private FirebaseAuth mAuth;
    private String android_id;
    private OffDBHelper db;
    SharedPrefManager SPManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_up);

        signIn = (SignInButton)findViewById(R.id.sign_in_button);
        mAuth = FirebaseAuth.getInstance();

        // get android id
        android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        SPManager = new SharedPrefManager(this);

        db = new OffDBHelper(this);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });



        edtNomorHp = findViewById(R.id.nomorHp);

        findViewById(R.id.buttonContinue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String number = edtNomorHp.getText().toString().trim();

                if (number.isEmpty() || number.length() < 10) {
                    edtNomorHp.setError("Valid number is required");
                    edtNomorHp.requestFocus();
                    return;
                }

                StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiInterface.URL_CEK_DAFTAR, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            // mengambil variable dari json
                            JSONObject jsonObject = new JSONObject(response);

                            if(jsonObject.getString("message").equals("berisi")){
                                Toast.makeText(SignUp.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            } else if(jsonObject.getString("message").equals("kosong")) {
                                Toast.makeText(SignUp.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(SignUp.this, Kode_OTR.class);
                                intent.putExtra("phonenumber", phonenumber);
                                startActivity(intent);
                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SignUp.this, "Failed to Data",Toast.LENGTH_SHORT).show();
                    }
                }){
                    protected Map<String , String> getParams() throws AuthFailureError {
                        Map<String , String> params = new HashMap<>();
                        params.put("phone", phonenumber);
                        return params;
                    }
                };
                RequestHandler.getInstance(SignUp.this).addToRequestQueue(stringRequest);
            }
        });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        String email = acct.getEmail();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiInterface.URL_CEK_DAFTAR, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    // mengambil variable dari json
                    JSONObject jsonObject = new JSONObject(response);

                    if(jsonObject.getString("message").equals("berisi")){
                        Toast.makeText(SignUp.this, "Email sudah terdaftar!", Toast.LENGTH_SHORT).show();
                    } else if(jsonObject.getString("message").equals("kosong")) {
                        Toast.makeText(SignUp.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
                        mAuth.signInWithCredential(credential)
                                .addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
                                            Log.d(TAG, "signInWithCredential:success");
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            updateUI(user);

                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                                            Toast.makeText(SignUp.this,"you are not able to log in to google",Toast.LENGTH_LONG).show();
                                            updateUI(null);

                                        }
                                    }
                                });
                    }
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SignUp.this, "Failed to Data",Toast.LENGTH_SHORT).show();
            }
        }){
            protected Map<String , String> getParams() throws AuthFailureError {
                Map<String , String> params = new HashMap<>();
                params.put("phone", email);
                return params;
            }
        };
        RequestHandler.getInstance(SignUp.this).addToRequestQueue(stringRequest);
    }

    private void updateUI(FirebaseUser user) {


        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if (acct != null) {
            String personEmail = acct.getEmail();
            String personId = acct.getId();

            String token = FirebaseInstanceId.getInstance().getToken();
            FirebaseMessaging.getInstance().setAutoInitEnabled(true);

            SPManager.saveSPString("email", personEmail);

            db.saveUser(personId, personEmail);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiInterface.URL_ADD_DAFTAR, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try{
                        // mengambil variable dari json
                        JSONObject jsonObject = new JSONObject(response);

                        if(jsonObject.getString("message").equals("Data Inserted Successfully")){
                            Toast.makeText(SignUp.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        } else if(jsonObject.getString("message").equals("Data Insertion Failed")) {
                            Toast.makeText(SignUp.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(SignUp.this, "Failed to Data",Toast.LENGTH_SHORT).show();
                }
            }){
                protected Map<String , String> getParams() throws AuthFailureError {
                    Map<String , String> params = new HashMap<>();
                    params.put("uid", personId);
                    params.put("email_phone", personEmail);
                    params.put("android_id", android_id);
                    params.put("token_msg", token);
                    return params;
                }
            };
            RequestHandler.getInstance(SignUp.this).addToRequestQueue(stringRequest);

            Toast.makeText(this, personId , Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(SignUp.this, MainApp.class);
            startActivity(intent);
            Toast.makeText(this, "Selamat Datang!" , Toast.LENGTH_SHORT).show();

        }
    }

    public void daftar(View view) {
        Intent intent = new Intent(SignUp.this, Kode_OTR.class);
        startActivity(intent);
    }

}
