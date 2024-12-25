package seamfinding;

import seamfinding.energy.EnergyFunction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Dynamic programming implementation of the {@link SeamFinder} interface.
 *
 * @see SeamFinder
 */
public class DynamicProgrammingSeamFinder implements SeamFinder {

    @Override
    public List<Integer> findHorizontal(Picture picture, EnergyFunction f) {
        int width = picture.width();
        int height = picture.height();

        double[][] energy = new double[width][height];
        double[][] cost = new double[width][height];
        int[][] edgeTo = new int[width][height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                energy[x][y] = f.apply(picture, x, y);
            }
        }

        for (int y = 0; y < height; y++) {
            cost[0][y] = energy[0][y];
        }

        for (int x = 1; x < width; x++) {
            for (int y = 0; y < height; y++) {
                double min = cost[x - 1][y];
                int yprev = y;

                if (y > 0 && cost[x - 1][y - 1] < min) {
                    min = cost[x - 1][y - 1];
                    yprev = y - 1;
                }
                if (y < height - 1 && cost[x - 1][y + 1] < min) {
                    min = cost[x - 1][y + 1];
                    yprev = y + 1;
                }

                cost[x][y] = energy[x][y] + min;
                edgeTo[x][y] = yprev;
            }
        }

        double minTotal = Double.POSITIVE_INFINITY;
        int yEnd = 0;
        for (int y = 0; y < height; y++) {
            if (cost[width - 1][y] < minTotal) {
                minTotal = cost[width - 1][y];
                yEnd = y;
            }
        }

        List<Integer> seam = new ArrayList<>();
        int yCurr = yEnd;
        for (int x = width - 1; x >= 0; x--) {
            seam.add(yCurr);
            yCurr = edgeTo[x][yCurr];
        }

        Collections.reverse(seam);
        return seam;
    }
}
