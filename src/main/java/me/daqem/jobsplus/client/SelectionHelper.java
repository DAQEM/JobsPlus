package me.daqem.jobsplus.client;

public class SelectionHelper {

    public static boolean isBetween(double mouseX, double mouseY, int mouseXTop, int mouseYTop, int mouseXBottom, int mouseYBottom, int startX, int startY) {
        return mouseX >= startX + mouseXTop && mouseY >= startY + mouseYTop && mouseX <= startX + mouseXBottom && mouseY <= startY + mouseYBottom;
    }

    public static class Construction {

        public static boolean isOnCraftItemButton(double x, double y, int startX, int startY) {
            return isBetween(x, y, 147, 111, 210, 128, startX, startY);
        }

        public static boolean isOnLeftArrowButton(double x, double y, int startX, int startY) {
            return isBetween(x, y, 127, 111, 145, 129, startX, startY);
        }

        public static boolean isOnRightArrowButton(double x, double y, int startX, int startY) {
            return isBetween(x, y, 213, 111, 231, 129, startX, startY);
        }
    }
}
