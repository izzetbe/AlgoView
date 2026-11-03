package org.example.testingalgor;

import javafx.application.Platform;
import javafx.scene.control.Label;

public class StatsTracker {

    private int comparisons = 0;
    private int swaps = 0;

    private Label comparisonsLabel;
    private Label swapsLabel;

    public StatsTracker(Label comparisonsLabel, Label swapsLabel) {
        this.comparisonsLabel = comparisonsLabel;
        this.swapsLabel = swapsLabel;
    }

    public void reset() {
        comparisons = 0;
        swaps = 0;
        update();
    }

    public void incrementComparisons() {
        comparisons++;
        update();
    }

    public void incrementSwaps() {
        swaps++;
        update();
    }

    private void update() {
        Platform.runLater(() -> {
            comparisonsLabel.setText("Comparisons: " + comparisons);
            swapsLabel.setText("Swaps: " + swaps);
        });

    }
}