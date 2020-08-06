package id.koom.app;

import android.content.DialogInterface;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static id.koom.app.SignUp.RC_SIGN_IN;

public class Login extends AppCompatActivity {
    private Spinner spinner;
    private EditText edtNomorHp;
    private SignInButton signIn;
    GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private OffDBHelper db;
    private SharedPrefManager SPManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signIn = (SignInButton)findViewById(R.id.sign_in_button);
        mAuth = FirebaseAuth.getInstance();

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
    }

    public void masuk(View view) {

        String number = edtNomorHp.getText().toString().trim();

        if (number.isEmpty() || number.length() < 10) {
            edtNomorHp.setError("Valid number is required");
            edtNomorHp.requestFocus();
            return;
        }

        String phonenumber = "+62" + number;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiInterface.URL_CEK_DAFTAR, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    // mengambil variable dari json
                    JSONObject jsonObject = new JSONObject(response);

                    if(jsonObject.getString("message").equals("berisi")){
                        Toast.makeText(Login.this, "Berhasil!", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(Login.this, Kode_OTR.class);
                        intent.putExtra("phonenumber", phonenumber);
                        startActivity(intent);
                    } else if(jsonObject.getString("message").equals("kosong")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                        builder.setTitle("Daftar");
                        builder.setMessage("Akun anda belum terdaftar.");
                        builder.setPositiveButton("DAFTAR", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(Login.this, SignUp.class);
                                startActivity(intent);
                            }
                        });
                        builder.setNegativeButton("BATAL", null);
                        builder.show();
                    }
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Login.this, "Failed to Data",Toast.LENGTH_SHORT).show();
            }
        }){
            protected Map<String , String> getParams() throws AuthFailureError {
                Map<String , String> params = new HashMap<>();
                params.put("phone", phonenumber);
                return params;
            }
        };
        RequestHandler.getInstance(Login.this).addToRequestQueue(stringRequest);

//        Intent intent = new Intent(Login.this, Dashboard.class);
//        startActivity(intent);
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
                Log.w("Auth Firebase", "Google sign in failed", e);
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        String email = acct.getEmail();
        String uid = acct.getId();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiInterface.URL_CEK_DAFTAR, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    // mengambil variable dari json
                    JSONObject jsonObject = new JSONObject(response);

                    if(jsonObject.getString("message").equals("berisi")){
                        Intent intent = new Intent(Login.this, MainApp.class);
                        startActivity(intent);

                        SPManager.saveSPString("email", email);
                        db.saveUser(uid, email);
                        Toast.makeText(Login.this, "Selamat Datang!" , Toast.LENGTH_SHORT).show();
                    } else if(jsonObject.getString("message").equals("kosong")) {
                        Toast.makeText(Login.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
                        mAuth.signInWithCredential(credential)
                                .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            updateUI(user);

                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Toast.makeText(Login.this,"you are not able to log in to google",Toast.LENGTH_LONG).show();
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
                Toast.makeText(Login.this, "Failed to Data",Toast.LENGTH_SHORT).show();
            }
        }){
            protected Map<String , String> getParams() throws AuthFailureError {
                Map<String , String> params = new HashMap<>();
                params.put("phone", email);
                return params;
            }
        };
        RequestHandler.getInstance(Login.this).addToRequestQueue(stringRequest);
    }

    private void updateUI(FirebaseUser user) {


        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if (acct != null) {
            String personEmail = acct.getEmail();
            String personId = acct.getId();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiInterface.URL_ADD_DAFTAR, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try{
                        // mengambil variable dari json
                        JSONObject jsonObject = new JSONObject(response);

                        if(jsonObject.getString("message").equals("Data Inserted Successfully")){
                            Toast.makeText(Login.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        } else if(jsonObject.getString("message").equals("Data Insertion Failed")) {
                            Toast.makeText(Login.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Login.this, error.toString() ,Toast.LENGTH_SHORT).show();
                    Toast.makeText(Login.this, "AAA Failed to Data",Toast.LENGTH_SHORT).show();
                }
            }){
                protected Map<String , String> getParams() throws AuthFailureError {
                    Map<String , String> params = new HashMap<>();
                    params.put("uid", personId);
                    params.put("email_phone", personEmail);
                    return params;
                }
            };
            RequestHandler.getInstance(Login.this).addToRequestQueue(stringRequest);

            db.saveUser(personId, personEmail);

            Intent intent = new Intent(Login.this, MainApp.class);
            startActivity(intent);
            Toast.makeText(this, "Selamat Datang!" , Toast.LENGTH_SHORT).show();

        }
    }
}
