package org.example.testingalgor;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.util.Objects;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class Visualizer {

    private final Pane canvas = new Pane();

    private final VBox codePanel = new VBox(10);
    private final VBox rightPanel = new VBox(20);

    private final HBox controls = new HBox(10);

    private final Slider speedSlider = new Slider(1,200,80);
    private final Slider sizeSlider = new Slider(10,240,40);

    private final ComboBox<String> algorithmBox = new ComboBox<>();

    private int[] array;
    private int size = 40;

    private Text[] codeLines;

    private boolean sorting = false;
    private boolean paused = false;
    private boolean stepping = false;

    private final Object pauseLock = new Object();

    private final Button generateBtn;
    private final Button startBtn;
    private final Button pauseBtn;
    private final Button stepBtn;

    private final SortingAlgorithm[] algorithms;

    private final StatsTracker stats;
    private final ArrayRenderer renderer;

    public Visualizer(){
        canvas.setMaxSize(900,675);
        canvas.setBackground(new  Background(new BackgroundFill(Color.BLACK,null,null)));

        renderer = new ArrayRenderer(canvas);
        Label comparisonsLabel = new Label("Comparisons: 0");
        Label swapsLabel = new Label("Swaps: 0");
        stats = new StatsTracker(comparisonsLabel, swapsLabel);
        controls.setAlignment(Pos.TOP_LEFT);
        controls.setPadding(new Insets(10,10,10,10));
        controls.setBackground(new Background(new BackgroundFill(Color.rgb(57, 24, 53),null,null)));
        generateBtn = new Button("Generate");
        generateBtn.setPrefSize(110, 10);
        startBtn = new Button("Start");
        startBtn.setPrefSize(110, 10);
        pauseBtn = new Button("Pause");
        pauseBtn.setPrefSize(110, 10);
        pauseBtn.setDisable(true);
        stepBtn = new Button("Step");
        stepBtn.setPrefSize(110, 10);
        stepBtn.setDisable(true);

        Tooltip tooltip = new Tooltip("Step through the algorithm, one move at a time.");
        stepBtn.setTooltip(tooltip);

        GridPane grid = new GridPane();
        grid.add(startBtn,0,0);
        grid.add(pauseBtn,0,1);
        grid.add(stepBtn,1,1);
        grid.add(generateBtn,1,0);
        grid.setHgap(4);
        grid.setVgap(4);
        grid.setAlignment(Pos.CENTER);

        algorithms = new SortingAlgorithm[]{
                new BubbleSort(),
                new MergeSort(),
                new QuickSort()
        };

        for(SortingAlgorithm a : algorithms)
            algorithmBox.getItems().add(a.name());
        algorithmBox.getSelectionModel().selectFirst();
        generateBtn.setOnAction(_ -> generateArray());
        startBtn.setOnAction(_ -> startSelectedAlgorithm());
        pauseBtn.setOnAction(_ -> pauseUnpause());
        stepBtn.setOnAction(_ -> stepMode());
        speedSlider.setPrefWidth(150);
        sizeSlider.setPrefWidth(150);

        sizeSlider.valueProperty().addListener((_, _, n)->{
            size = n.intValue();
            generateArray();
        });

        VBox statsBox = new VBox(5, comparisonsLabel, swapsLabel);
        statsBox.setPadding(new  Insets(10,10,10,90));

        VBox sliderBox = new VBox(5, new Label("Speed"), speedSlider);
        sliderBox.setPadding(new Insets(15,0,0,30));
        VBox sliderBox2 = new VBox(5, new Label("Size"), sizeSlider);
        sliderBox2.setPadding(new Insets(15,0,0,30));

        HBox comboBox = new HBox(5, new Label("Algorithm"), algorithmBox);
        comboBox.setAlignment(Pos.CENTER);
        comboBox.setPadding(new Insets(0,0,0,30));

        controls.getChildren().addAll(
                grid,
                comboBox,
                sliderBox,
                sliderBox2,
                statsBox
        );

        setupCodePanel();
    }

    private void setupCodePanel(){
        Label title = new Label("Algorithm Code");
        title.setMinSize(300, 30);
        title.setBackground(new Background(new BackgroundFill(Color.rgb(57, 24, 53),null,null)));
        title.setPadding(new Insets(0,0,0,10));
        codePanel.setAlignment(Pos.TOP_LEFT);
        codePanel.setBackground(new Background(new BackgroundFill(Color.BLACK,null,null)));
        codePanel.setMinSize(300, 625);
        codePanel.setPadding(new  Insets(10,10,10,10));
        rightPanel.setPadding(new Insets(20));
        rightPanel.setPrefWidth(300);
        rightPanel.setBackground(new Background(new BackgroundFill(Color.rgb(92,32,98),null,null)));
        rightPanel.getChildren().addAll(title,codePanel);
    }

    public Pane getCanvas(){
        return canvas;
    }
    public VBox getRightPanel(){
        return rightPanel;
    }
    public HBox getControls(){
        return controls;
    }

    public void generateArray(){
        if(sorting) return;
        array = new int[size];
        Random rand = new Random();
        for(int i=0;i<size;i++)
            array[i] = rand.nextInt(400)+20;

        stats.reset();
        renderer.drawArray(array,-1,-1);
    }

    public void swap(int i,int j){
        stats.incrementSwaps();

        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;

        renderer.drawArray(array,i,j);
        sleep();
    }

    public void compare(int i,int j){
        stats.incrementComparisons();
        renderer.drawArray(array,i,j);
        sleep();
    }

    public void overwrite(int index,int value){
        array[index] = value;
        renderer.drawArray(array,index,-1);
        sleep();
    }

    public void setCode(String[] code){
        if(Platform.isFxApplicationThread()){
            applySetCode(code);
            return;
        }

        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            applySetCode(code);
            latch.countDown();
        });
        try { latch.await(); }
        catch(InterruptedException e){ Thread.currentThread().interrupt(); }
    }

    private void applySetCode(String[] code){
        codePanel.getChildren().clear();
        codeLines = new Text[code.length];
        for(int i = 0; i < code.length; i++){
            Text t = new Text(code[i]);
            codeLines[i] = t;
            codePanel.getChildren().add(t);
        }
    }

    public void highlightLine(int line){
        if(Platform.isFxApplicationThread()){
            applyHighlight(line);
            return;
        }
        Platform.runLater(() -> applyHighlight(line));
    }

    private void applyHighlight(int line){
        if(codeLines == null) return;
        for(Text t : codeLines)
            t.setFill(Color.web("#ffffff")); // default text colour from your CSS
        if(line >= 0 && line < codeLines.length)
            codeLines[line].setFill(Color.RED);
    }

    private void sleep(){
        synchronized (pauseLock) {
            while(paused){
                try {
                    pauseLock.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }

        try{
            long delay = (long)(201 - speedSlider.getValue());
            Thread.sleep(delay);
        }
        catch(InterruptedException e){
            Thread.currentThread().interrupt();
        }

        if (stepping) {
            stepping = false;
            setPaused(true);

            Platform.runLater(() -> stepBtn.setDisable(false));
        }

    }

    public void delay(){
        sleep();
    }

    private void startSelectedAlgorithm(){
        int index = algorithmBox.getSelectionModel().getSelectedIndex();
        runAlgorithm(algorithms[index]);
    }

    private void runAlgorithm(SortingAlgorithm algorithm){
        if(sorting) return;
        sorting = true;

        setButtonsDisabled(true);

        new Thread(() -> {
            algorithm.sort(array,this);
            renderer.sortedAnimation(array,size,this::sleep);
            sorting = false;
            Platform.runLater(() -> setButtonsDisabled(false));
        }).start();

    }

    private void pauseUnpause(){
        paused = !paused;
        setPaused(paused);
        pauseBtn.setText(paused ? "Resume" : "Pause");
        stepBtn.setDisable(!paused);
    }

    private void stepMode() {
        if (!sorting || !paused) return;

        stepping = true;
        setPaused(false);
    }

    private void setButtonsDisabled(boolean disabled){
        generateBtn.setDisable(disabled);
        startBtn.setDisable(disabled);
        algorithmBox.setDisable(disabled);
        pauseBtn.setDisable(!disabled);
        stepBtn.setDisable(disabled);
        sizeSlider.setDisable(disabled);
        if(!disabled){
            paused = false;
            stepping = false;
            pauseBtn.setText("Pause");
        }
    }

    private void setPaused(boolean p){
        paused = p;
        if(!p){
            synchronized(pauseLock){
                pauseLock.notifyAll();
            }
        }
    }
}