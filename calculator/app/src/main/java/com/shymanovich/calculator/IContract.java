package com.shymanovich.calculator;

public interface IContract {
    interface IView {
        void showResult(String res);
        String getExpressionText();
    }

    interface IPresenter {
        void onCalculateBtnClick();
    }

    interface ICalculator {
        String calculate(String expression);
    }
}
