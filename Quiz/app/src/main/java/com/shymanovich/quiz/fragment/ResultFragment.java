package com.shymanovich.quiz.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.InterstitialAd;
import com.shymanovich.quiz.R;

public class ResultFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.result, null);
        Button menu = root.findViewById(R.id.menuBtn);
        TextView totalText, correctText, resultText;
        totalText = root.findViewById(R.id.total);
        correctText = root.findViewById(R.id.correct);
        resultText = root.findViewById(R.id.score);
        int total = getArguments().getInt("total");
        int correct = getArguments().getInt("correct");
        int score = correct * 100 / total;
        totalText.setText(String.format("Total: %d", total));
        correctText.setText(String.format("Correct: %d", correct));
        resultText.setText(String.format("Your score: %d", score));
        menu.setOnClickListener(v -> {
            this.switchToMenu();
        });
        return root;
    }

    public void switchToMenu() {
        getActivity().getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.frame_content, new MenuFragment())
            .commit();
    }
}
