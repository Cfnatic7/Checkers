package Checker;

import javafx.application.Platform;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.concurrent.atomic.AtomicBoolean;


public class PlayerPane extends StackPane {
    private final String playerName;
    private boolean isUpdating;
    public static final int WIDTH = CheckersApp.TILE_SIZE * 3;
    public static final int HEIGHT = CheckersApp.TILE_SIZE * 4;
    private Integer seconds;
    private Integer minutes;
    private Integer hours;
    Text timeText;

    public int getPlayerPaneHight() {
        return HEIGHT;
    }

    public void resetTime() {
        seconds = 0;
        minutes = 0;
        hours = 0;
        updateText("Player " + this.playerName + "\n" + "time elapsed: " + String.format("%02d:%02d:%02d", 0, 0, 0));
    }

    public int getPlayerPaneWidth() {
        return WIDTH;
    }

//    private class TimeUpdater extends Thread {
//        private boolean running;
//
//
//        public void setRunning(boolean running) {
//            this.running = running;
//        }
//
//        public void kill() {
//            this.running = false;
//        }
//
//        public void startExecution() {
//            this.running = true;
//            start();
//        }

//        public void run() {
//            Runnable updater = () -> updateTimeText(System.currentTimeMillis());
//
//            while (this.isInterrupted()) {
//                try {
//                    for (int i = 0; i < 1000; i++) {
//                        if (!this.running) break;
//                        Thread.sleep(1);
//                    }
//                    Thread.sleep(1000);
//                } catch (InterruptedException ex) {
//                }
//
//                // UI update is run on the Application thread
//                Platform.runLater(updater);
//            }
//            try {
//                join();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            if (this.isInterrupted()) {
//                try {
//                    this.join();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
    private Thread timeUpdater;

    public Thread getTimeUpdater() {
        return this.timeUpdater;
    }
//    private final TimeUpdater timeUpdater = new TimeUpdater();

    public PlayerPane(String playerName, int x, int y) {
        this.playerName = playerName;
        this.seconds = 0;
        this.minutes = 0;
        this.hours = 0;
        timeText = new Text();
        timeText.setText("Player " + this.playerName + "\n" + "time elapsed: " + String.format("%02d:%02d:%02d", 0, 0, 0));
        timeText.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
        this.getChildren().add(new Rectangle(WIDTH, HEIGHT, Color.WHITE));
        this.getChildren().add(timeText);
//        super.setAlignment(Pos.CENTER);
        relocate(x * CheckersApp.TILE_SIZE, y * CheckersApp.TILE_SIZE);
    }

    private void incrementTime() {
        this.seconds++;
        if(seconds == 60) {
            this.seconds = 0;
            this.minutes++;
        }
        if (this.minutes == 60) {
            this.minutes = 0;
            this.hours++;
        }
    }

    public void updateTimeText() {
        incrementTime();

        String temp = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        timeText.setText("Player " + this.playerName + "\n" + "time elapsed: " + temp);

    }

    public void updateText(String text) {
        this.timeText.setText(text);
    }

    public void startTimeUpdating() {
//        if (timeUpdater.isAlive()) {
//            synchronized (this) {
//                timeUpdater.notify();
//            }
//            this.timeUpdater.notify();
//        }
//        else {
//            timeUpdater.start();
//        }
        this.isUpdating = true;
        this.timeUpdater = new Thread(() -> {
            Runnable updater = this::updateTimeText;

            while (this.isUpdating) {
                try {
//                    for (int i = 0; i < 10; i++) {
//                        if (!this.isUpdating) break;
//                        Thread.sleep(100);
//                    }
                Thread.sleep(1000);
                } catch (InterruptedException ex) {
                }

                // UI update is run on the Application thread
                Platform.runLater(updater);
            }
        });
        this.timeUpdater.start();
    }

    public void killTimeUpdating() {
        if (timeUpdater != null){
            this.timeUpdater.stop();
        }
    }

    public void stopTimeUpdating() throws InterruptedException {
//        if (timeUpdater.isAlive()){
//            timeUpdater.interrupt();
//        }
//        synchronized (this) {
//            timeUpdater.wait();
//        }
        this.isUpdating = false;
        if (this.timeUpdater != null) {
            this.timeUpdater.join();
        }
    }

}
