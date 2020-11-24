package processing.java.objects.widget.container.graph;

import processing.core.PGraphics;
import processing.java.thread.data.stock.DataSet;
import processing.java.objects.base.properties.Vector;
import processing.java.util.Color;

/**
 * scatter plot extending graph to represent the information as a scatter plot
 *
 * @author brian dunne
 * @version 0.0
 * @since   2020-04-10
 */
public class ScatterPlot extends  Graph{


    public ScatterPlot(DataSet dataSet, String xGraph, String yGraph, Color color, int w, int h, int i, int index) {
        super(dataSet, xGraph, yGraph, color, w, h, i, index);
    }

    /**
     * implement draw to resample a scatter plot
     * @param graphic graphics
     * @param maxX maxX of graphic
     * @param maxY maxYof graphic
     *
     * @author  brian dunne
     */
    @Override
    public void draw(PGraphics graphic, float maxX, float maxY) {
            graphic.fill(label.getColour().toInt());
            float xMin = points[0].X;
            for (int p = 0; p < points.length - 1; p++) {
                Vector point1 = new Vector((((points[p].X - xMin) / (maxX - xMin)) * (graphic.width - 32)) + 16, graphic.height - ((points[p].Y / maxY) * (graphic.height - 32) + 16));
                Vector point2 = new Vector((((points[p + 1].X - xMin) / (maxX - xMin)) * (graphic.width - 32)) + 16, graphic.height - ((points[p + 1].Y / maxY) * (graphic.height - 32) + 16));

                //if() TODO, check when moused over a point and issue a pop up event to parent

                graphic.noStroke();
                graphic.ellipse(point1.X, point1.Y, 3, 3);
                graphic.ellipse(point2.X, point2.Y, 3, 3);
                graphic.stroke(label.getColour().toInt());
                graphic.line(point1.X, point1.Y, point2.X, point2.Y);
            }

            graphic.stroke(30);
            graphic.fill(30);
            graphic.line(16, 16, 16, graphic.height - 16);
            graphic.strokeWeight(2);
            graphic.fill(0);
            graphic.line(16, graphic.height - 16, graphic.width - 16, graphic.height - 16);
            graphic.strokeWeight(1);
    }
}
