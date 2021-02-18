package com.shymanovich.quiz.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.shymanovich.quiz.R;
import com.shymanovich.quiz.model.Question;
import com.shymanovich.quiz.model.Quiz;


public class QuizFragment extends Fragment {

    private FirebaseFirestore db;
    private Quiz quiz;
    private RadioGroup choices;
    private RadioButton choice1, choice2, choice3, choice4, selected;
    private TextView questionText;
    private int CURRENT_QUESTION = 0;
    private Button submitBtn;
    private int total;
    private int correct;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.quiz, null);
        quiz = getArguments().getParcelable("quiz");
        total = quiz.getQuestions().size();

        choice1 = root.findViewById(R.id.choice1);
        choice2 = root.findViewById(R.id.choice2);
        choice3 = root.findViewById(R.id.choice3);
        choice4 = root.findViewById(R.id.choice4);
        questionText = root.findViewById(R.id.questionDescription);
        submitBtn = root.findViewById(R.id.submitQuestion);
        choices = root.findViewById(R.id.questionRadiogroup);

        submitBtn.setOnClickListener(v -> {
            if (choices.getCheckedRadioButtonId() != -1) {
                updateQuestion();
            }
        });

        ((TextView)root.findViewById(R.id.questionTitle)).setText(quiz.getTitle());
        Question first = quiz.getQuestions().get(0);
        questionText.setText(first.getTitle());
        choice1.setText(first.getChoice1());
        choice2.setText(first.getChoice2());
        choice3.setText(first.getChoice3());
        choice4.setText(first.getChoice4());
        return root;
    }

    public void updateQuestion() {
        if (CURRENT_QUESTION == quiz.getQuestions().size() - 1) {
            this.updateScore();
            this.switchToResult();
            return;
        }
        this.updateScore();
        choices.clearCheck();
        Question next = quiz.getQuestions().get(CURRENT_QUESTION);
        questionText.setText(next.getTitle());
        choice1.setText(next.getChoice1());
        choice2.setText(next.getChoice2());
        choice3.setText(next.getChoice3());
        choice4.setText(next.getChoice4());
    }

    public void switchToResult() {
        ResultFragment fragment = new ResultFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("total", this.quiz.getQuestions().size());
        bundle.putInt("correct", this.correct);
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_content, fragment)
                .commit();
    }

    public void updateScore() {
        Question current = quiz.getQuestions().get(CURRENT_QUESTION);
        int selectedRadioButtonId = choices.getCheckedRadioButtonId();
        selected = getActivity().findViewById(selectedRadioButtonId);
        String answer = selected.getText().toString();
        if (answer.equals(current.getAnswer())) {
            this.correct++;
        }
        CURRENT_QUESTION++;
    }
}
