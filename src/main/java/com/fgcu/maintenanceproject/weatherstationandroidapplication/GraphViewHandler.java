package com.fgcu.maintenanceproject.weatherstationandroidapplication;

import android.graphics.Color;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.LineGraphSeries;

public class GraphViewHandler implements Runnable {

    LineGraphSeries series1;
    LineGraphSeries series2;
    GraphView lineGraph;

    public GraphViewHandler(LineGraphSeries series1, LineGraphSeries series2, GraphView lineGraph){
        this.series1 = series1;
        this.series2 = series2;
        this.lineGraph = lineGraph;
    }

    @Override
    public void run() {
        lineGraph.removeAllSeries();
        lineGraph.addSeries(series1);
        lineGraph.addSeries(series2);


    }
}
