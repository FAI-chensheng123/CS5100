package pacman.controllers.Sheng_Chen;

import pacman.controllers.Controller;
import pacman.controllers.examples.StarterGhosts;
import pacman.game.Constants;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

import java.util.EnumMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

/**
 *  @Title Hmk2, CS5100/4100 Artificial Intelligence, A-star Search
 *  @Author Sheng Chen
 *  @Date 2/9/2016
 */
public class Astart_Controller extends Controller<MOVE> {

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

            if (highSorce == -1) {
                highMove = MOVE.RIGHT;
            }

            System.out.println("PacMan Trying Move:" + m + ", Socre:" + tempHightSocre);

        }

        System.out.println("High Score:" + highSorce + ", High Move:" + highMove);
        return highMove;
    }

    /**
     * A-star Search
     * @param gameState
     * @param maxDepth
     * @return
     */
    public int breadthFirstSearch_shengchen(PacManNode gameState, int maxDepth) {


        MOVE[] allMoves = MOVE.values();
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
                    EnumMap<Constants.GHOST,MOVE> gm = ghosts.getMove(gameCopy,0);
                    gameCopy.advanceGame(m, gm);
                    PacManNode node = new PacManNode(gameCopy, pacManNode.depth + 1 + h(gameCopy));  // Evaluation function f(n) = g(n) + h(n)
                    queue.add(node);
                }
            }
        }

        return highScore;
    }


    /**
     * h(n): Estimate cost from n to goal
     * @param
     * @return
     */
    public int h(Game game) {

        int current=game.getPacmanCurrentNodeIndex();


        //Strategy 1: if any non-edible ghost is too close (less than MIN_DISTANCE), run away
        for(Constants.GHOST ghost : Constants.GHOST.values())
            if(game.getGhostEdibleTime(ghost)==0 && game.getGhostLairTime(ghost)==0)
                if(game.getShortestPathDistance(current,game.getGhostCurrentNodeIndex(ghost))<20)
                    //return game.getNextMoveAwayFromTarget(game.getPacmanCurrentNodeIndex(),game.getGhostCurrentNodeIndex(ghost), Constants.DM.PATH);
                    return 2;

        //Strategy 2: find the nearest edible ghost and go after them
        int minDistance=Integer.MAX_VALUE;
        Constants.GHOST minGhost=null;

        for(Constants.GHOST ghost : Constants.GHOST.values())
            if(game.getGhostEdibleTime(ghost)>0)
            {
                int distance=game.getShortestPathDistance(current,game.getGhostCurrentNodeIndex(ghost));

                if(distance<minDistance)
                {
                    minDistance=distance;
                    minGhost=ghost;
                }
            }

        if(minGhost!=null)	//we found an edible ghost
            //return game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(),game.getGhostCurrentNodeIndex(minGhost), Constants.DM.PATH);
            return 0;

       return 0;
    }

}
