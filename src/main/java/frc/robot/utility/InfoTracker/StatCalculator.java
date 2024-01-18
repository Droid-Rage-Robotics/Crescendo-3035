package frc.robot.utility.InfoTracker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StatCalculator {
  private final List<Double> numbers;

  public StatCalculator() {
    numbers = new ArrayList<>();
  }

  public void clear() {
    numbers.clear();
  }

  public void addNumber(double number) {
    numbers.add(number);
  }

  public int getSizeInt() {
    return numbers.size();
  }
  public Double getSizeDouble() {
    return (double) numbers.size();
  }

  public double getSum() {
    double sum = 0;

    for (double d : numbers) {
      sum += d;
    }

    return sum;
  }

  public double getMean() {
    if (getSizeInt() == 0) {
      return Double.NaN;
    } else {
      return getSum() / getSizeInt();
    }
  }

  public double getStandardDeviation() {
    if (getSizeInt() < 2) {
      return Double.NaN;
    } else {
      double mean = getMean();
      return Math.sqrt(
          numbers.stream().mapToDouble(x -> Math.pow(x - mean, 2)).sum() / (getSizeInt() - 1));
    }
  }

  public double getLowestValue() {
    return Collections.min(numbers);
  }

  public double getHighestValue() {
    return Collections.max(numbers);
  }

  public double getMedian() {
    if (getSizeInt() == 0) {
      return Double.NaN;
    }

    Collections.sort(numbers);

    if (getSizeInt() % 2 == 1) {
      return numbers.get(((getSizeInt() + 1) / 2) - 1);
    } else {
      double lower = numbers.get((getSizeInt() / 2) - 1);
      double upper = numbers.get(getSizeInt() / 2);

      return (lower + upper) / 2.0;
    }
  }
  public double getNum(int i) {
    return numbers.get(i);
  }
}
