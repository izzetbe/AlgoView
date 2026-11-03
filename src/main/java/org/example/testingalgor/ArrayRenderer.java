package org.example.testingalgor;

import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.concurrent.CountDownLatch;

public class ArrayRenderer {

    private Pane canvas;

    public ArrayRenderer(Pane canvas){
        this.canvas = canvas;
    }

    public void drawArray(int[] array,int highlightA,int highlightB){

        if(Platform.isFxApplicationThread()){
            //already on JavaFX thread, just draw directly — no latch needed
            canvas.getChildren().clear();
            int size = array.length;
            double width = canvas.getWidth() / size;
            double height = canvas.getHeight();
            if(width == 0 || height == 0) return;
            for(int i = 0; i < size; i++){
                canvas.getChildren().add(makeBar(i, array, size,
                        (i == highlightA || i == highlightB) ? Color.RED : Color.web("#ca72e3")));
            }
            return;
        }

        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {

            canvas.getChildren().clear();

            int size = array.length;
            double width = canvas.getWidth()/size;
            double height = canvas.getHeight();

            if(width == 0 || height == 0) return;

            for(int i=0;i<size;i++){

                Rectangle bar = new Rectangle(
                        i*width,
                        height-array[i],
                        width-2,
                        array[i]
                );

                if(i==highlightA || i==highlightB)
                    bar.setFill(Color.RED);
                else
                    bar.setFill(Color.web("#ca72e3"));

                canvas.getChildren().add(bar);
            }
            latch.countDown();
        });
        try {
            latch.await();
        } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }

    public void sortedAnimation(int[] array, int size, Runnable sleep){
        drawArray(array, -1, -1); // clear first
        for(int i = 0; i < size; i++){
            final int index = i;
            CountDownLatch latch = new CountDownLatch(1);
            Platform.runLater(() -> {
                canvas.getChildren().set(index, makeBar(index, array, size, Color.LIMEGREEN));
                latch.countDown();
            });
            try { latch.await(); }
            catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            sleep.run();
        }
    }

    private Rectangle makeBar(int index, int[] array, int size, Color color){
        double width = canvas.getWidth() / size;
        double height = canvas.getHeight();
        Rectangle bar = new Rectangle(index * width, height - array[index], width - 2, array[index]);
        bar.setFill(color);
        return bar;
    }
}