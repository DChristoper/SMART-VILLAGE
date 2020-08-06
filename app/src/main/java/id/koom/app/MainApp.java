package id.koom.app;

//import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import id.koom.app.API.ApiInterface;

import id.koom.app.R;

import id.koom.app.helper.BroadCallback;
import id.koom.app.helper.OffDBHelper;
import id.koom.app.helper.RequestHandler;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import id.koom.app.utils.BottomNavigationViewHelper;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainApp extends AppCompatActivity {
//    @BindView(R.id.bottomNavigation
    BottomNavigationView bottomNavigation;
    private Fragment fragment;
    private FragmentManager fragmentManager;
    String TAG = "Firebase Instance";
    private static MainApp instance;
    OffDBHelper db;
    String notif = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_app);

        db = new OffDBHelper(this);

        String token = FirebaseInstanceId.getInstance().getToken();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiInterface.URL_UPDATE_TOKEN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Toast.makeText(MainApp.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                    if (jsonObject.getString("message").equals("Data Updated")) {
                        Log.d("MainApp", "TOKEN UPDATED");
                    } else if (jsonObject.getString("message").equals("Data Insertion Failed")) {
                        Log.d("MainApp", "TOKEN UPDATE FAILED");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("MainApp", "RESPONSE FAILED");
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token", token);
                params.put("uid", db.getUser());
                return params;
            }
        };
        RequestHandler.getInstance(MainApp.this).addToRequestQueue(stringRequest);

        Log.d(TAG, token);

        instance = this;

        fragmentManager = getSupportFragmentManager();

        bottomNavigation = (BottomNavigationView)findViewById(R.id.bottomNavigation);

        Intent intent = getIntent();

        if(intent.getStringExtra("Notif") != null){
            notif = intent.getStringExtra("Notif");
            fragment = new inbox_fragment(MainApp.this, notif);
            bottomNavigation.setSelectedItemId(R.id.pesan_menu);
        } else {
            fragment = new DashboardFragment(MainApp.this);
        }

        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.mainContainer, fragment).commit();

        BottomNavigationViewHelper.removeShiftMode(bottomNavigation);
       bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
           @Override
           public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
               int id= menuItem.getItemId();
               Log.d("MenuItem", String.valueOf(id));
               switch (id){
                   case R.id.pesan_menu:
                       fragment=new inbox_fragment(MainApp.this, notif);
                       break;
                   case R.id.awal_menu:
                       fragment=new DashboardFragment(MainApp.this);
                       break;
                   case R.id.order_menu:
                       fragment=new order_fragment();
                       break;
                   case R.id.profile_menu:
                       fragment=new fragment_profil();
                       break;
               }
               final FragmentTransaction transaction = fragmentManager.beginTransaction();
               transaction.replace(R.id.mainContainer, fragment).commit();
               return true;
           }
       });
//        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                int id = item.getItemId();
//                switch (id){
//                    case R.id.profile_menu:
//                        fragment = new DashboardFragment();
//
//                        break;
//                    case R.id.pesan_menu:
////                        fragment = new DashboardFragment();
//                        break;
//                    case R.id.awal_menu:
////                        fragment = new DashboardFragment();
//                        break;
//                    case R.id.order_menu:
////                        fragment = new DashboardFragment();
//                        break;
//                }

//            }
//        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((mMessageReceiver),
                new IntentFilter("MyData")
        );
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }

    public static MainApp getInstance(){
        return instance;
    }

    public void getBroadcast(BroadCallback callback){
        mMessageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(TAG, intent.getExtras().getString("title"));
                Log.d(TAG, intent.getExtras().getString("body"));
                callback.onSuccess(intent.getExtras().getString("title"));
            }
        };
    }

    BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Bundle extras = intent.getExtras();
            Intent i = new Intent("myData");
            // Data you need to pass to activity
            i.putExtra("message", intent.getExtras().getString("title"));

            context.sendBroadcast(i);

            Log.d(TAG, intent.getExtras().getString("title"));
        }
    };;
}
