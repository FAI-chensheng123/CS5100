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
    public Stack<PacManNode> stack = new Stack<PacManNode>();
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
            int tempHightSocre = this.depthFirstSearch_shengchen(new PacManNode(gameATM, 0), 4);

            if (highSorce < tempHightSocre) {
                highSorce = tempHightSocre;
                highMove = m;
            }

            System.out.println("PacMan Trying Move:" + m + ", Socre:" + tempHightSocre);
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
        stack.push(gameState);

        for (MOVE m : allMoves) {
            pacManNode = stack.peek();
            Game gameCopy = pacManNode.gameState.copy();
            gameCopy.advanceGame(m, ghosts.getMove(gameCopy, 0));
            score = dfsRecursive(gameState);
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
    public int dfsRecursive(PacManNode gameState) {
        int highScore = 0;
        for (MOVE m : allMoves) {
            Game gameCopy = pacManNode.gameState.copy();
            gameCopy.advanceGame(m, ghosts.getMove(gameCopy, 0));

            if (gameCopy.gameOver()) {
                score = gameCopy.getScore();
                if (score == gameState.depth) {
                    return score;
                }
                if (score > 0) {
                    highScore = score;
                }
            } else {
                dfsRecursive(gameState);
            }
        }

        return highScore;
    }

}
