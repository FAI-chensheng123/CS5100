package pacman.controllers.Sheng_Chen;

import java.util.*;

import pacman.controllers.Controller;
import pacman.controllers.examples.StarterGhosts;
import pacman.game.Constants;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.internal.Node;
import pacman.game.internal.PacMan;

/**
 *  @Title Hmk1, CS5100/4100 Artificial Intelligence, Depth-First Search
 *  @Author Sheng Chen
 *  @Date 2/9/2016
 */
public class DFS_Controller extends Controller<Constants.MOVE>{

    public static StarterGhosts ghosts = new StarterGhosts();
    public MOVE[] allMoves = Constants.MOVE.values();
    public PacManNode pacManNode;
    int score;

    /**
     *
     * @param game A copy of the current game
     * @param timeDue The time the next move is due
     * @return
     */
    @Override
    public Constants.MOVE getMove(Game game, long timeDue) {
        Random random = new Random();
        Constants.MOVE[] allMoves = Constants.MOVE.values();

        int highSorce = -1;
        Constants.MOVE highMove = null;

        for (Constants.MOVE m : allMoves) {
            Game gameCopy = game.copy();
            Game gameATM = gameCopy;
            gameATM.advanceGame(m, ghosts.getMove(gameATM, timeDue));

            // Decide if pacman hit the wall
            if (gameATM.getPacmanCurrentNodeIndex() == game.getPacmanCurrentNodeIndex()) {
                System.out.println("Hit the wall");
                continue;
            } else {

                int tempHightSocre = this.depthFirstSearch_shengchen(new PacManNode(gameATM, 0), 4);
                if (highSorce < tempHightSocre) {
                    highSorce = tempHightSocre;
                    highMove = m;
                }

                System.out.println("PacMan Trying Move:" + m + ", Socre:" + tempHightSocre);
            }
        }

        System.out.println("High Score:" + highSorce + ", High Move:" + highMove);
        return highMove;
    }

    /**
     * Depth-First Search
     * @param gameState
     * @return
     */
    public int depthFirstSearch_shengchen(PacManNode gameState, int maxDepth) {

        int highScore = 0;

        for (MOVE m : allMoves) {
            pacManNode = gameState;
            Game gameCopy = pacManNode.gameState.copy();
            gameCopy.advanceGame(m, ghosts.getMove(gameCopy, 0));
            score = dfsRecursive(gameState, 7);
            if (highScore < score) {
                highScore = score;
            }
        }

        return highScore;
    }

    /**
     *
     * @param gameState
     * @return
     */
    public int dfsRecursive(PacManNode gameState, int maxDepth) {
        int highScore = 0;
        for (MOVE m : allMoves) {
            Game gameCopy = pacManNode.gameState.copy();
            gameCopy.advanceGame(m, ghosts.getMove(gameCopy, 0));

            // Decide pacman hit the wall
            if (gameCopy.getPacmanCurrentNodeIndex() == gameState.gameState.getPacmanCurrentNodeIndex()) {
                continue;
            } else {
                if (gameState.depth >= maxDepth) {
                    score = gameCopy.getScore();
                    if (score > highScore) {
                        highScore = score;
                    }
                } else {
                    highScore = dfsRecursive(new PacManNode(gameCopy, gameState.depth + 1), 7);
                }
            }
        }

        return highScore;
    }

}
