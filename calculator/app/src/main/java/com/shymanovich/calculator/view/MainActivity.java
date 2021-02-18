package com.shymanovich.calculator.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.shymanovich.calculator.IContract;
import com.shymanovich.calculator.R;
import com.shymanovich.calculator.presenter.CalculatorMiddleware;

public class MainActivity extends AppCompatActivity implements IContract.IView {

    private TextView expressionView;
    private TextView resultView;
    private IContract.IPresenter middleware;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.middleware = new CalculatorMiddleware(this);

        this.expressionView = findViewById(R.id.expression);
        this.resultView = findViewById(R.id.result);
        this.setupInputBtns();

        TextView btnEquals = findViewById(R.id.btn_equals);
        btnEquals.setOnClickListener(v -> this.middleware.onCalculateBtnClick());
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setContentView(R.layout.activity_main);
        this.expressionView = findViewById(R.id.expression);
        this.resultView = findViewById(R.id.result);
        this.setupInputBtns();

        TextView btnEquals = findViewById(R.id.btn_equals);
        btnEquals.setOnClickListener(v -> this.middleware.onCalculateBtnClick());
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            TextView btnPow = findViewById(R.id.btn_power);
            TextView btnMinusPower = findViewById(R.id.btn_m_power);
            TextView btnExp = findViewById(R.id.btn_e);
            TextView btnPow2 = findViewById(R.id.btn_sqr);
            TextView btnSqrt = findViewById(R.id.btn_sqrt);
            TextView btnPow3 = findViewById(R.id.btn_cube);
            TextView btnLog = findViewById(R.id.btn_log);
            TextView btnLn = findViewById(R.id.btn_ln);
            TextView btnFact = findViewById(R.id.btn_f);
            TextView btnFraction = findViewById(R.id.btn_fraction);
            TextView btnPercent = findViewById(R.id.btn_percent);
            TextView btnPi = findViewById(R.id.btn_pi);
            TextView btnSin = findViewById(R.id.btn_sin);
            TextView btnCos = findViewById(R.id.btn_cos);
            TextView btnTan = findViewById(R.id.btn_tan);

            btnPow.setOnClickListener(v -> this.appendExpression("^("));
            btnMinusPower.setOnClickListener(v -> this.appendExpression("^(1/"));
            btnSqrt.setOnClickListener(v -> this.appendExpression("√"));
            btnPercent.setOnClickListener(v -> this.appendExpression("*0.01"));
            btnFraction.setOnClickListener(v -> this.appendExpression("^(-1)"));

            btnPow2.setOnClickListener(v -> this.appendExpression("^(2)"));
            btnPow3.setOnClickListener(v -> this.appendExpression("^(3)"));
            btnFact.setOnClickListener(v -> this.appendExpression("!"));

            btnPi.setOnClickListener(v -> this.appendExpression("π"));
            btnSin.setOnClickListener(v -> this.appendExpression("sin("));
            btnCos.setOnClickListener(v -> this.appendExpression("cos("));
            btnTan.setOnClickListener(v -> this.appendExpression("tan("));
            btnLog.setOnClickListener(v -> this.appendExpression("log("));
            btnLn.setOnClickListener(v -> this.appendExpression("ln("));
            btnExp.setOnClickListener(v -> this.appendExpression("e"));
        }
    }

    @Override
    public void showResult(String res) {
        TextView result = findViewById(R.id.result);
        result.setText(res);
    }

    public void setupInputBtns() {
        TextView btn0 = findViewById(R.id.btn_0);
        TextView btn1 = findViewById(R.id.btn_1);
        TextView btn2 = findViewById(R.id.btn_2);
        TextView btn3 = findViewById(R.id.btn_3);
        TextView btn4 = findViewById(R.id.btn_4);
        TextView btn5 = findViewById(R.id.btn_5);
        TextView btn6 = findViewById(R.id.btn_6);
        TextView btn7 = findViewById(R.id.btn_7);
        TextView btn8 = findViewById(R.id.btn_8);
        TextView btn9 = findViewById(R.id.btn_9);
        TextView btnPlus = findViewById(R.id.btn_plus);
        TextView btnMinus = findViewById(R.id.btn_minus);
        TextView btnMultiply = findViewById(R.id.btn_multiply);
        TextView btnDivide = findViewById(R.id.btn_divide);
        TextView btnOpenBracket = findViewById(R.id.btn_open_bracket);
        TextView btnCloseBracket = findViewById(R.id.btn_close_bracket);
        TextView btnDot = findViewById(R.id.btn_dot);
        TextView btnClear = findViewById(R.id.btn_clear);
        ImageView btnBack = findViewById(R.id.btn_back);

        btn0.setOnClickListener(v -> this.appendExpression("0"));
        btn1.setOnClickListener(v -> this.appendExpression("1"));
        btn2.setOnClickListener(v -> this.appendExpression("2"));
        btn3.setOnClickListener(v -> this.appendExpression("3"));
        btn4.setOnClickListener(v -> this.appendExpression("4"));
        btn5.setOnClickListener(v -> this.appendExpression("5"));
        btn6.setOnClickListener(v -> this.appendExpression("6"));
        btn7.setOnClickListener(v -> this.appendExpression("7"));
        btn8.setOnClickListener(v -> this.appendExpression("8"));
        btn9.setOnClickListener(v -> this.appendExpression("9"));

        btnPlus.setOnClickListener(v -> this.appendOperation("+"));
        btnMinus.setOnClickListener(v -> this.appendOperation("-"));
        btnMultiply.setOnClickListener(v -> this.appendOperation("*"));
        btnDivide.setOnClickListener(v -> this.appendOperation("/"));

        btnOpenBracket.setOnClickListener(v -> this.appendExpression("("));
        btnCloseBracket.setOnClickListener(v -> this.appendExpression(")"));
        btnDot.setOnClickListener(v -> this.appendExpression("."));

        btnBack.setOnClickListener(v -> this.onBackBtnClick());
        btnClear.setOnClickListener(v -> this.clearAll());
    }

    public void appendExpression(String value) {
        this.expressionView.setText(String.format("%s%s", this.expressionView.getText(), value));
    }

    public void appendOperation(String operator) {
        String existingExpression = this.expressionView.getText().toString();
        if (!existingExpression.equals("") && !this.resultView.getText().equals("Error")) {
            char lastSym = existingExpression.charAt(existingExpression.length() - 1);
            if (lastSym != '(') {
                if (lastSym != '+' && lastSym != '-' && lastSym !='*' && lastSym != '/' ) {
                    this.appendExpression(operator);
                }
            }
        }
    }

    public void clearAll() {
        this.expressionView.setText("");
        this.resultView.setText("");
    }

    public void onBackBtnClick() {
        CharSequence expression = this.expressionView.getText();
        if (this.resultView.getText().equals("Error")) {
            this.expressionView.setText("");
        } else {
            this.expressionView.setText(expression.subSequence(0, expression.length() - 1));
        }
    }

    public String getExpressionText() {
        return this.expressionView.getText().toString();
    }
}