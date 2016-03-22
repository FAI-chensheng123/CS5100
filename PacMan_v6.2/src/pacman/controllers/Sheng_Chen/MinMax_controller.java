package pacman.controllers.Sheng_Chen;

import pacman.controllers.Controller;
import pacman.controllers.examples.StarterGhosts;
import pacman.game.Constants;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.internal.Node;

import java.util.EnumMap;
import java.util.Random;


/**
 *  @Title Hmk3, CS5100/4100 Artificial Intelligence, Min-max algorithm
 *  @Author Sheng Chen
 *  @Date 3/22/2016
 *
 */
public class MinMax_controller extends Controller<Constants.MOVE> {

    public static StarterGhosts ghosts = new StarterGhosts();
    private double inf = Double.POSITIVE_INFINITY;

    @Override
    public Constants.MOVE getMove(Game game, long timeDue) {

        MOVE[] allMoves = MOVE.values();

        int highSorce = -1;
        MOVE highMove = allMoves[new Random().nextInt(allMoves.length)];
        int currHightSocre = minMax(game, 7, true, timeDue);

        for (MOVE m : allMoves) {
            Game gameCopy = game.copy();
            Game gameATM = gameCopy;
            gameATM.advanceGame(m, ghosts.getMove(gameATM, timeDue));

            int tempHightSocre = minMax(game, 7 - 1, false, timeDue);

            if (currHightSocre == tempHightSocre) {
                return m;
            }

            System.out.println("PacMan Trying Move:" + m + ", Socre:" + tempHightSocre);
        }

        System.out.println("High Score:" + highSorce + ", High Move:" + highMove);
        return highMove;

    }


    /**
     * minMax Algorithm
     * @param game
     * @param depth
     * @param pacMan
     * @return
     */
    public int minMax(Game game, int depth, Boolean pacMan, long timeDue) {

        double bestValue;
        double score = 0;

        MOVE[] moves = MOVE.values();

        if (depth == 0) {
            //score = evaluationFunction(game);
            return evaluationFunction(game);
        }
        if (pacMan) {
            bestValue = (inf * -1);
            Game gameCopy = game.copy();
            for (int i = 0; i < moves.length; i++) {
                gameCopy.advanceGame(moves[i], ghosts.getMove(gameCopy, timeDue));
                int temp = minMax(gameCopy, depth - 1, Boolean.FALSE, timeDue);
                bestValue = Math.max(temp, bestValue);
            }
            return (int)bestValue;
        } else {
            bestValue = inf;
            Game gameCopy = game.copy();
            for (int i = 0; i < moves.length; i++) {
                gameCopy.advanceGame(moves[i], ghosts.getMove(gameCopy, timeDue));
                int temp = minMax(gameCopy, depth - 1, Boolean.TRUE, timeDue);
                bestValue = Math.min(temp, bestValue);
            }
            return (int)bestValue;
        }

    }

    /**
     * Evaluation Function
     * @param game
     * @return
     */
    public int evaluationFunction(Game game) {

        int currentScore = game.getScore();
        int nodeIndex = game.getPacmanCurrentNodeIndex();

        Constants.GHOST[] ghostIndex = Constants.GHOST.values();
        Random random = new Random();

        int temp = random.nextInt(ghostIndex.length);
        int finalSore = currentScore + game.getManhattanDistance(nodeIndex,
                game.getGhostCurrentNodeIndex(ghostIndex[temp]));

        return finalSore;
    }

















}
