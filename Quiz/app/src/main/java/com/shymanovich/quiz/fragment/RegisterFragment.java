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

public class RegisterFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.register, null);
        TextView loginLabel = root.findViewById(R.id.loginLabel);
        Button registerBtn = root.findViewById(R.id.registerBtn);
        EditText email = root.findViewById(R.id.emailInput);
        EditText password = root.findViewById(R.id.passwordInput);

        loginLabel.setOnClickListener(v -> {
            this.switchToLogin();
        });
        registerBtn.setOnClickListener(v -> {
            this.createAccount(email.getText().toString(), password.getText().toString());
        });
        return root;
    }

    public void createAccount(String email, String password) {
        FirebaseAuth authenticator = FirebaseAuth.getInstance();
        authenticator.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        switchToLogin();
                    }
                }
            });
    }

    public void switchToLogin() {
        getActivity().getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.frame_content, new LoginFragment())
            .commit();
    }
}
