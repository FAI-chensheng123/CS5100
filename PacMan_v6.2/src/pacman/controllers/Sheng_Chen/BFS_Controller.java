package pacman.controllers.Sheng_Chen;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import pacman.controllers.Controller;
import pacman.controllers.examples.StarterGhosts;
import pacman.game.Constants;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.internal.Node;
import pacman.game.internal.PacMan;

/**
 *  @Title Hmk1, CS5100/4100 Artificial Intelligence, Breadth-First Search
 *  @Author Sheng Chen
 *  @Date 2/9/2016
 */
public class BFS_Controller extends Controller<Constants.MOVE> {

    public static StarterGhosts ghosts = new StarterGhosts();

    /**
     *
     * @param game A copy of the current game
     * @param timeDue The time the next move is due
     * @return
     */
    @Override
    public MOVE getMove(Game game, long timeDue) {

        Random random = new Random();
        MOVE[] allMoves = MOVE.values();

        int highSorce = -1;
        MOVE highMove = null;

        for (MOVE m : allMoves) {
            Game gameCopy = game.copy();
            Game gameATM = gameCopy;
            gameATM.advanceGame(m, ghosts.getMove(gameATM, timeDue));
            int tempHightSocre = this.breadthFirstSearch_shengchen(new PacManNode(gameATM, 0), 7);

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
     * Breadth-First Search
     * @param gameState
     * @param maxDepth
     * @return
     */
    public int breadthFirstSearch_shengchen(PacManNode gameState, int maxDepth) {


        MOVE[] allMoves = Constants.MOVE.values();
        int depth = 0;
        int highScore = -1;

        Queue<PacManNode> queue = new LinkedList<PacManNode>();
        queue.add(gameState);

        while (!queue.isEmpty()) {
            PacManNode pacManNode = queue.remove();

            if (pacManNode.depth >= maxDepth) {
                int score = pacManNode.gameState.getScore();
                if (highScore < score) {
                    highScore = score;
                }
            } else {
                // search next layer of the tree
                for (MOVE m : allMoves) {
                    Game gameCopy = pacManNode.gameState.copy();
                    gameCopy.advanceGame(m, ghosts.getMove(gameCopy, 0));
                    PacManNode node = new PacManNode(gameCopy, pacManNode.depth + 1);
                    queue.add(node);
                }
            }
        }

        return highScore;
    }
}
