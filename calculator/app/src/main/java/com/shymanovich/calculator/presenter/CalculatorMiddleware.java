package com.shymanovich.calculator.presenter;

import com.shymanovich.calculator.IContract;
import com.shymanovich.calculator.model.Calculator;

public class CalculatorMiddleware implements IContract.IPresenter {

    private final IContract.ICalculator calculator;
    private final IContract.IView calculatorActivity;

    public CalculatorMiddleware(IContract.IView calculatorActivity) {
        this.calculatorActivity = calculatorActivity;
        this.calculator = new Calculator();
    }

    @Override
    public void onCalculateBtnClick() {
        String result = this.calculator.calculate(this.calculatorActivity.getExpressionText());
        this.calculatorActivity.showResult(result);
    }
}
