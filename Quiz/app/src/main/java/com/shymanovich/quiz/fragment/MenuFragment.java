package com.shymanovich.quiz.fragment;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.shymanovich.quiz.R;
import com.shymanovich.quiz.model.Quiz;

import java.util.List;

public class MenuFragment extends Fragment {

    private FirebaseFirestore db;
    private Button logoutBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.menu, null);
        LinearLayout quizContainer = root.findViewById(R.id.quizContainer);
        logoutBtn = root.findViewById(R.id.logoutBtn);
        db = FirebaseFirestore.getInstance();
        logoutBtn.setOnClickListener(v -> {
            this.logout();
        });
        db.collection("quizes").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<Quiz> quizes = queryDocumentSnapshots.toObjects(Quiz.class);
                quizes.forEach(quiz -> {
                    View quizView = LayoutInflater.from(container.getContext()).inflate(R.layout.quizchoice, null);
                    TextView quizTitle = quizView.findViewById(R.id.quizTitle);
                    quizTitle.setText(quiz.getTitle());
                    quizTitle.setOnClickListener(v -> startQuiz(quiz));
                    if (quizView.getParent() != null) {
                        ((ViewGroup)quizView.getParent()).removeView(quizView);
                    }
                    quizContainer.addView(quizView);
                });
            }
        });
        AdView adView = root.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        return root;
    }

    public void startQuiz(Quiz quiz) {
        QuizFragment fragment = new QuizFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("quiz", quiz);
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.frame_content, fragment)
            .commit();
    }

    public void logout() {
        FirebaseAuth.getInstance().signOut();
        if (getParentFragmentManager() != null)
            getParentFragmentManager().beginTransaction().replace(R.id.frame_content, new LoginFragment()).commit();
    }
}
