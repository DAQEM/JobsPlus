package com.daqem.jobsplus.player.job;

import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.config.JobsPlusConfig;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class JobLevelCalculator {

    private static final Map<Integer, Double> CACHE = new ConcurrentHashMap<>();

    public static void resetCache() {
        CACHE.clear();
    }

    public static double getExperienceForLevel(int level) {
        if (level == 0) return 0;
        return CACHE.computeIfAbsent(level, JobLevelCalculator::calculate);
    }

    private static double calculate(int level) {
        String formula = JobsPlusConfig.experienceFormula.get();
        try {
            return new Object() {
                int pos = -1, ch;

                void nextChar() {
                    ch = (++pos < formula.length()) ? formula.charAt(pos) : -1;
                }

                boolean eat(int charToEat) {
                    while (ch == ' ') nextChar();
                    if (ch == charToEat) {
                        nextChar();
                        return true;
                    }
                    return false;
                }

                double parse() {
                    nextChar();
                    double x = parseExpression();
                    if (pos < formula.length()) throw new RuntimeException("Unexpected: " + (char) ch);
                    return x;
                }

                double parseExpression() {
                    double x = parseTerm();
                    for (;;) {
                        if      (eat('+')) x += parseTerm(); // addition
                        else if (eat('-')) x -= parseTerm(); // subtraction
                        else return x;
                    }
                }

                double parseTerm() {
                    double x = parseFactor();
                    for (;;) {
                        if      (eat('*')) x *= parseFactor(); // multiplication
                        else if (eat('/')) x /= parseFactor(); // division
                        else return x;
                    }
                }

                double parseFactor() {
                    if (eat('+')) return parseFactor(); // unary plus
                    if (eat('-')) return -parseFactor(); // unary minus

                    double x;
                    int startPos = this.pos;
                    if (eat('(')) { // parentheses
                        x = parseExpression();
                        eat(')');
                    } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                        while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                        x = Double.parseDouble(formula.substring(startPos, this.pos));
                    } else if (ch >= 'a' && ch <= 'z') { // variables
                        while (ch >= 'a' && ch <= 'z') nextChar();
                        String func = formula.substring(startPos, this.pos);
                        if (func.equals("level")) {
                            x = level;
                        } else {
                            throw new RuntimeException("Unknown variable: " + func);
                        }
                    } else {
                        throw new RuntimeException("Unexpected: " + (char) ch);
                    }

                    if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

                    return x;
                }
            }.parse();

        } catch (Exception e) {
            JobsPlus.API.LOGGER.error("Failed to parse experience formula '{}': {}. Using default backup.", formula, e.getMessage());
            return calculateDefault(level);
        }
    }

    private static int calculateDefault(int level) {
        return (int) (100 + level * level * 0.5791);
    }
}