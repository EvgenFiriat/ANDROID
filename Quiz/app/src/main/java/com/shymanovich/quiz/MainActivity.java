package com.shymanovich.quiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.shymanovich.quiz.fragment.LoginFragment;
import com.shymanovich.quiz.fragment.MenuFragment;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth authenticator = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("Oncreate");
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                getSupportFragmentManager().beginTransaction().add(R.id.frame_content, getView()).commit();
            }
        });
    }

    public Fragment getView() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        return mAuth.getCurrentUser() != null ? new MenuFragment() : new LoginFragment();
    }
}