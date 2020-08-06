package id.koom.app;

import android.content.Intent;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import id.koom.app.R;

import id.koom.app.helper.OffDBHelper;

public class SplashScreen extends AppCompatActivity {
    private static int splashInterval = 2000;
    OffDBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        db = new OffDBHelper(this);
        String uid = db.getUser();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i;
//                i = new Intent(SplashScreen.this, MainApp.class);
                if(uid != null){
                    i = new Intent(SplashScreen.this, MainApp.class);
                } else {
                    i = new Intent(SplashScreen.this, Slider.class);
                }
                startActivity(i);

                this.finish();
            }

            private void finish() {

            }
        }, splashInterval);

    }
}
