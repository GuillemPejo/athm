package me.guillem.athm2app.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import me.guillem.athm2app.R;

public class SplashScreen extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_ATHM2APP_Launcher);
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user =  mAuth.getCurrentUser();

        Intent go_home = new Intent(this, HomeActivity.class);


        //mAuth.signOut();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (mAuth.getCurrentUser()!=null){
                    go_home.putExtra("user",user);
                    startActivity(go_home);
                    finish();
                }else {
                    Intent go_auth = new Intent(getApplicationContext(), AuthActivity.class);
                    startActivity(go_auth);
                }
          }
        }, 2000);
    }

}

