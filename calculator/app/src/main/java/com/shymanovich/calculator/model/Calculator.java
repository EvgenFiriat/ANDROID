package com.shymanovich.calculator.model;

import com.shymanovich.calculator.IContract;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.operator.Operator;

public class Calculator implements IContract.ICalculator {
    private final Operator factorial;

    public Calculator() {
        this.factorial = new Operator("!", 1, true, Operator.PRECEDENCE_POWER + 1) {
            @Override
            public double apply(double... args) {
                final int arg = (int) args[0];
                if ((double) arg != args[0]) {
                    throw new IllegalArgumentException("Operand for factorial has to be an integer");
                }
                if (arg < 0) {
                    throw new IllegalArgumentException("The operand of the factorial can not be less than zero");
                }
                double result = 1;
                for (int i = 1; i <= arg; i++) {
                    result *= i;
                }
                return result;
            }
        };
    }

    @Override
    public String calculate(String expression) {
        try {
            Expression exp = new ExpressionBuilder(expression).operator(factorial).build();
            double result = exp.evaluate();
            return Double.toString(result);
        } catch (Exception exception) {
            return "Error";
        }
    }
}
