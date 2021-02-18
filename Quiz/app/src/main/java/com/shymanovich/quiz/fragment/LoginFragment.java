package com.shymanovich.quiz.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.shymanovich.quiz.R;

public class LoginFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.login, null);
        TextView registerLabel = root.findViewById(R.id.registerLabel);
        Button loginBtn = root.findViewById(R.id.loginBtn);
        EditText email = root.findViewById(R.id.emailInput);
        EditText password = root.findViewById(R.id.passwordInput);

        loginBtn.setOnClickListener(v -> {
            this.login(email.getText().toString(), password.getText().toString());
        });

        registerLabel.setOnClickListener(v -> {
            this.switchToRegister();
        });
        return root;
    }

    public void login(String email, String password) {
        FirebaseAuth authenticator = FirebaseAuth.getInstance();
        authenticator.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        switchToMenu();
                    }
                }
            });
    }

    public void switchToRegister() {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_content, new RegisterFragment())
                .commit();
    }

    public void switchToMenu() {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_content, new MenuFragment())
                .commit();
    }
}
