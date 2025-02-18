package com.teach.javafx.thread;

public class TimeThread {
    private class SeparateSubTask extends Thread {
        private int count = 0;
        private boolean runFlag = true;

        SeparateSubTask() {
            start();
        }

        void invertFlag() {
            runFlag = !runFlag;
        }

        public void run() {
            while (true) {
                try {
                    sleep(100);
                } catch (InterruptedException e) {
                    System.err.println("Interrupted");
                }
            }
        }
    }
}
