package me.daqem.jobsplus.handlers;

public class ExperienceHandler {

    private static int sum(int n, int a0, int d) {
        return n * (2 * a0 + (n - 1) * d) / 2;
    }

    public static int getExperienceForLevel(int level) {
        if (level == 0) return 0;
        if (level <= 15) return sum(level, 7, 2);
        if (level <= 30) return 315 + sum(level - 15, 37, 5);
        return 1395 + sum(level - 30, 112, 9);
    }

    public static int getLevelFromExperience(int points) {
        int i = 0;
        while (points >= 0) {
            if (i < 16) points -= (2 * i) + 7;
            else if (i < 31) points -= (5 * i) - 38;
            else points -= (9 * i) - 158;
            i++;
        }
        return i - 1;
    }
}
