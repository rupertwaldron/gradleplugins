package com.example.plugin.calculators;

import java.util.function.IntBinaryOperator;

public enum Calc {
  SUM(Integer::sum, "+"),
  SUB((a, b) -> a - b, "-"),
  MULTI((a, b) -> a * b, "*"),
  DIV((a, b) -> a / b, "/");

  private IntBinaryOperator operator;
  private String symbol;

  Calc(IntBinaryOperator operator, String symbol) {
    this.operator = operator;
    this.symbol = symbol;
  }

  public int calculate(int a, int b) {
    return operator.applyAsInt(a, b);
  }

  public String getSymbol() {
    return symbol;
  }
}
