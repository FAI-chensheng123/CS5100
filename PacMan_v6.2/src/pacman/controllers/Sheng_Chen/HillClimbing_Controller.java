package pacman.controllers.Sheng_Chen;

import pacman.controllers.Controller;
import pacman.controllers.examples.StarterGhosts;
import pacman.game.Constants;
import pacman.game.Game;
import pacman.game.Constants.MOVE;

import java.util.*;

/**
 *  @Title Hmk2, CS5100/4100 Artificial Intelligence, Hill-climbing
 *  @Author Sheng Chen
 *  @Date 3/1/2016
 */
public class HillClimbing_Controller extends Controller{

    public static StarterGhosts ghosts = new StarterGhosts();

    @Override
    public Constants.MOVE getMove(Game game, long timeDue) {

        // Random random = new Random();
        Constants.MOVE[] allMoves = Constants.MOVE.values();

        int highSorce = -1;
        Constants.MOVE highMove = null; // Start State

        //int[] a = new int[5];
        HashMap<MOVE, Integer> map = new HashMap<>();
        HashMap<Integer,MOVE> map2 = new HashMap<>();

        for (Constants.MOVE m : allMoves) {
            Game gameCopy = game.copy();
            Game gameATM = gameCopy;
            gameATM.advanceGame(m, ghosts.getMove(gameATM, timeDue)); // Neighbors
            int tempHightSocre = this.hill_climbing_value(gameATM);

            map.put(m, tempHightSocre);
            map2.put(tempHightSocre,m);

            System.out.println("PacMan Trying Move:" + m + ", Socre:" + tempHightSocre);
        }
        
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        // Pick one with the highest score, if higher than cuurent state, move to the child state
        for(Map.Entry<MOVE,Integer> it : map.entrySet()){
            min = Math.min(min,it.getValue());
            max = Math.max(max,it.getValue());
        }
        if(min == max) {
            highMove = MOVE.LEFT;
        }
        else {
            highMove = map2.get(max);
        }
        System.out.println("High Score:" + highSorce + ", High Move:" + highMove);
        return highMove;
    }


    /**
     * Hill-climbing-value
     * @param gameState
     * @return
     */
    public int hill_climbing_value(Game gameState) {
        return gameState.getScore();
    }

}
