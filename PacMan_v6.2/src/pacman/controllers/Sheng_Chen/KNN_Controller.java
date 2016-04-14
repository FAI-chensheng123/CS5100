package pacman.controllers.Sheng_Chen;


import pacman.controllers.Controller;
import pacman.game.*;
import pacman.game.Constants.MOVE;

import java.util.*;

/**
 *  @Title Hmk4, CS5100/4100 Artificial Intelligence, K Nearest Neighbors
 *  @Author Sheng Chen
 *  @Date 4/12/2016
 */
public class KNN_Controller extends Controller {

    int k = 5; // define k value

    // data sets path
    String path1 = "/Users/shengchen/Files/NEU/CS 5100/This Semester/training data/data1.txt";

    // read data sets
    ReadData readData = new ReadData();

    // store data sets to the list
    public List<DataPoint> dataSet1 = readData.read(path1);

    // all possible moves
    MOVE left = MOVE.LEFT;
    MOVE right = MOVE.RIGHT;
    MOVE up = MOVE.UP;
    MOVE down = MOVE.DOWN;
    MOVE neutral = MOVE.NEUTRAL;


    /**
     * KNN Algorithm
     * @param game A copy of the current game
     * @param timeDue The time the next move is due
     * @return
     */
    @Override
    public MOVE getMove(Game game, long timeDue) {

        MOVE[] allMoves = MOVE.values();

        int size = dataSet1.size();
        double distance = 0;

        // get current index of pacman
        int currPacLocation = game.getPacmanCurrentNodeIndex();
        // get current index of ghost
        int ghostLocation = game.getGhostCurrentNodeIndex(Constants.GHOST.BLINKY);

        // convert data type to DataPoint
        DataPoint dp = new DataPoint();

        dp.setScore(game.getScore());
        dp.setTime(game.getTotalTime());
        dp.setDistance(game.getEuclideanDistance(currPacLocation, ghostLocation));
        dp.setIndex(game.getPacmanCurrentNodeIndex());


        Map<Double, Integer> map = new HashMap<>();

        // calculate euclidean distance for all data points
        for (int i = 0; i < size; i++) {

            DataPoint di = dataSet1.get(i);
            distance = computeDistance(di, dp);
           // System.out.println("distance:" + distance);
            map.put(distance, i);
        }

        // sort distance with ascending order and also keep the index
        SortedSet<Double> set = new TreeSet<>(map.keySet());

        int[] res = new int[k];
        int count = 0;

        // get k nearest index with the distance
        for (Double i : set) {
            res[count] = map.get(i);
            System.out.println("res[count]: " + res[count]);
            count++;
            if (count == k) {
                break;
            }
        }

        // get index and moves
        String[] moves = new String[k];

        for (int i = 0; i < res.length; i++) {
             moves[i] = dataSet1.get(res[i]).getMove();
             System.out.println("moves[i]:" + moves[i]);
        }

        return findMajority(moves);
    }



    /**
     * Find majority count decide next move
     * @param moves
     * @return
     */
    public MOVE findMajority(String[] moves) {

        // initialize count value
        int countLeft = 0;
        int countRight = 0;
        int countUp = 0;
        int countDown = 0;
        int countNeutral = 0;

        for (int i = 0; i < moves.length; i++) {
            if (moves[i].equals("LEFT")) {
                countLeft++;
            } else if (moves[i].equals("RIGHT")) {
                countRight++;
            } else if (moves[i].equals("UP")) {
                countUp++;
            } else if (moves[i].equals("DOWN")) {
                countDown++;
            } else if (moves[i].equals("NEUTRAL")) {
                countNeutral++;
            }
        }

        // find the maximum value of count
        int max1 = Math.max(Math.max(Math.max(countLeft, countRight), countUp), countDown);
        int maxValue = Math.max(countNeutral, max1);

        // return next move
        if (maxValue == countLeft) {
            System.out.println("LEFT");
            return left;
        }
        if (maxValue == countRight) {
            System.out.println("RIGHT");
            return right;
        }
        if (maxValue == countUp) {
            System.out.println("UP");
            return up;
        }
        if (maxValue == countDown) {
            System.out.println("DOWN");
            return down;
        }
        if (maxValue == countNeutral) {
            System.out.println("NEUTRAL");
            return neutral;
        }

        return null;
    }


    /**
     * Calculate euclidean distance
     * @param d1
     * @param d2
     * @return
     */
    public double computeDistance(DataPoint d1, DataPoint d2) {

        double dPow = Math.pow((d1.getDistance() - d2.getDistance()), 2); // distance
        double tPow = Math.pow((d1.getTime() - d2.getTime()), 2); // time
        double sPow = Math.pow((d1.getScore() - d2.getScore()), 2); // score
        double iPow = Math.pow((d1.getIndex() - d2.getIndex()), 2); // current index

        return Math.sqrt(dPow + tPow + sPow + iPow);
    }

}


