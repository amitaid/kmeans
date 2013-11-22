package solo;

import javafx.geometry.Point2D;

import java.util.*;

public class KMeansSolo {

    int k;
    private List<Point2D> points;

    public KMeansSolo(int k, List<Point2D> points) {
        this.k = k;
        this.points = points;
    }

    public List<Point2D> rollInitialCentroids() {
        List<Point2D> result = null;
        double minDistance = 0.0;
        while (minDistance < 5.0) {
            result = new ArrayList<>();
            for (int i = 0; i < k; i++) {
                double x = Math.random() * 100;
                double y = Math.random() * 100;
                Point2D centroid = new Point2D(x, y);
                result.add(centroid);
            }
            minDistance = calcMinDistance(result);
        }
        return result;
    }

    public double calcMinDistance(List<Point2D> points) {
        double minDistance = Double.MAX_VALUE;
        Point2D[] arr = points.toArray(new Point2D[points.size()]);
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = i + 1; j < arr.length; j++) {
                minDistance = Math.min(minDistance, arr[i].distance(arr[j]));
            }
        }
        return minDistance;
    }

    public List<Point2D> getNextCentroids(List<Point2D> centroids) {
        List<Point2D> result = new ArrayList<>();
        Map<Point2D, List<Point2D>> centroidMap = new HashMap<>();
        for (Point2D centroid : centroids) centroidMap.put(centroid, new ArrayList<Point2D>());
        for (Point2D point : points) {
            Point2D closest = null;
            for (Point2D centroid : centroids) {
                if (closest == null) {
                    closest = centroid;
                } else {
                    closest = point.distance(centroid) < point.distance(closest) ? centroid : closest;
                }
            }
            centroidMap.get(closest).add(point);
        }
        for (List<Point2D> points : centroidMap.values()) {
            result.add(calcCenter(points));
        }
        return result;
    }

    public List<Point2D> calcCentroids() {
        List<Point2D> current, previous;
        current = rollInitialCentroids();
        boolean shouldContinue = true;
        do {
            previous = current;
            current = getNextCentroids(previous);
            for (Point2D point : current) {
                shouldContinue = shouldContinue && findClosestDistance(point, previous) > 0.0001;
            }
        } while (shouldContinue);
        return current;
    }

    public double findClosestDistance(Point2D p, List<Point2D> points) {
        double minDistance = Double.MAX_VALUE;
        for (Point2D point : points) {
            double newDistance = p.distance(point);
            minDistance = newDistance < minDistance ? newDistance : minDistance;
        }
        return minDistance;
    }

    public Point2D calcCenter(List<Point2D> points) {
        Point2D result;
        double x, y;
        x = y = 0.0;
        if (points == null || points.isEmpty()) {
            return new Point2D(50.0, 50.0);
        }
        for (Point2D point : points) {
            x += point.getX();
            y += point.getY();
        }

        result = new Point2D(x / points.size(), y / points.size());
        return result;
    }

    public static void main(String[] args) {

        List<Point2D> points = Arrays.asList(
                new Point2D(70.0, 70.0),
                new Point2D(71.0, 71.0),
                new Point2D(70.0, 72.0),
                new Point2D(3.0, 5.0),
                new Point2D(5.0, 3.0),
                new Point2D(4.0, 4.0),
                new Point2D(20.0, 50.0),
                new Point2D(19.0, 51.0),
                new Point2D(30.0, 30.0));


        KMeansSolo kms = new KMeansSolo(3, points);

        System.out.println("Result = " + kms.calcCentroids());

    }


}
