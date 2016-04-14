package pacman.controllers.Sheng_Chen;

import pacman.controllers.Controller;
import pacman.controllers.examples.StarterGhosts;
import pacman.game.Constants;
import pacman.game.Game;
import pacman.game.internal.Node;
import pacman.game.Constants.MOVE;

import java.util.EnumMap;
import java.util.Random;

/**
 *  @Title Hmk3, CS5100/4100 Artificial Intelligence, alpha-beta algorithm
 *  @Author Sheng Chen
 *  @Date 3/22/2016
 *
 */
public class AlphaBeta_Controller extends Controller<Constants.MOVE> {

    public static StarterGhosts ghosts = new StarterGhosts();
    private double inf = Double.POSITIVE_INFINITY;

    @Override
    public Constants.MOVE getMove(Game game, long timeDue) {

        Constants.MOVE[] allMoves = Constants.MOVE.values();

        double alpha = (inf * -1);
        double beta = inf;

        int highSorce = -1;
        Constants.MOVE highMove = allMoves[new Random().nextInt(allMoves.length)];
        // pacMan will move first
        int currHightSocre = alphaBeta(game, 7, (int)alpha, (int)beta, true, timeDue);

        for (Constants.MOVE m : allMoves) {
            Game gameCopy = game.copy();
            Game gameATM = gameCopy;
            gameATM.advanceGame(m, ghosts.getMove(gameATM, timeDue));

            int tempHightSocre = alphaBeta(game, 7 - 1, (int)alpha, (int)beta, false, timeDue);

            if (currHightSocre == tempHightSocre) {
                return m;
            }

            System.out.println("PacMan Trying Move:" + m + ", Socre:" + tempHightSocre);
        }

        System.out.println("High Score:" + highSorce + ", High Move:" + highMove);
        return highMove;

    }


    /**
     * Alpha-beta algorithm
     * @param game
     * @param depth
     * @param alpha
     * @param beta
     * @param pacMan
     * @return
     */
    public int alphaBeta(Game game, int depth, int alpha, int beta, Boolean pacMan, long timeDue) {

        double score = 0;
        double v; // bestValue

        MOVE[] moves = MOVE.values();

        if (depth == 0) {
           return evaluationFunction(game);
        }
        if (pacMan) {
            v = (inf * -1);
            Game gameCopy = game.copy();
            for (int i = 0; i < moves.length; i++) {
                gameCopy.advanceGame(moves[i], ghosts.getMove(gameCopy, timeDue));
                v = alphaBeta(gameCopy, depth - 1, alpha, beta, Boolean.FALSE, timeDue);
                alpha = Math.max((int)v, alpha);
                if (beta <= alpha) {  // beta cut-off
                    break;
                }
            }
            return (int)v;
        } else {
            v = inf;
            Game gameCopy = game.copy();
            for (int i = 0; i < moves.length; i++) {
                gameCopy.advanceGame(moves[i], ghosts.getMove(gameCopy, timeDue));
                v = alphaBeta(gameCopy, depth - 1, alpha, beta, Boolean.TRUE, timeDue);
                beta = Math.min((int)v, beta);
                if (beta <= alpha) { // alpha cut-off
                    break;
                }
            }
            return (int)v;
        }
    }


    /**
     * Evaluation Function
     * @param game
     * @return
     */
    public int evaluationFunction(Game game) {
        int currScore = game.getScore();
        int nodeIndex = game.getPacmanCurrentNodeIndex();

        Constants.GHOST[] ghostIndex = Constants.GHOST.values();
        Random random = new Random();

        int temp = random.nextInt(ghostIndex.length);

        int finallySore = currScore + game.getManhattanDistance(nodeIndex,
                game.getGhostCurrentNodeIndex(ghostIndex[temp]));

        return finallySore;
    }


}
