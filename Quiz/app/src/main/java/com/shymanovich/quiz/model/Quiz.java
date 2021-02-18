package com.shymanovich.quiz.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Quiz implements Parcelable {
    private String title;
    private ArrayList<Question> questions;

    public Quiz(String title, ArrayList<Question> questions) {
        this.title = title;
        this.questions = questions;
    }

    public Quiz() {

    }

    protected Quiz(Parcel in) {
        this.title = in.readString();
        this.questions = in.readArrayList(Question.class.getClassLoader());
    }

    public static final Creator<Quiz> CREATOR = new Creator<Quiz>() {
        @Override
        public Quiz createFromParcel(Parcel in) {
            return new Quiz(in);
        }

        @Override
        public Quiz[] newArray(int size) {
            return new Quiz[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeList(questions);
    }
}
