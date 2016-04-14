package pacman.controllers.Sheng_Chen;

import pacman.controllers.Controller;
import pacman.controllers.examples.StarterGhosts;
import pacman.game.Constants;
import pacman.game.CurrGraphNode;
import pacman.game.Game;
import pacman.game.Constants.MOVE;
import pacman.game.NextGraphNode;

/**
 * Q - Learning Algorithm
 * Created by shengchen on 4/12/16.
 */
public class QLearning_Controller extends Controller{

    public static StarterGhosts ghosts = new StarterGhosts();
    public CurrGraphNode currNode = null;

    public static final double discount = 0.5; // discount
    public static final double learningRate = 0.8; // learning rate


    /**
     *  Q-Learning Algorithm
     * @param game A copy of the current game
     * @param timeDue The time the next move is due
     * @return
     */
    @Override
    public Object getMove(Game game, long timeDue) {


        int currIndex = game.getPacmanCurrentNodeIndex();
        this.currNode = new CurrGraphNode(game);

        MOVE[] allMoves = game.getPossibleMoves(currIndex);

        double highSocre = Integer.MIN_VALUE;
        MOVE highMove = MOVE.NEUTRAL;

        for (MOVE m : allMoves) {
            Game gameCopy = game.copy();
            Game gameATM = gameCopy;

            gameATM.advanceGame(m, ghosts.getMove(gameATM, timeDue));
            CurrGraphNode node = new CurrGraphNode(gameATM);

            updateGraphNode(this.currNode, m, node); // update q value
        }

        for (NextGraphNode nextGraphNode : this.currNode.moveToNext) {
            if (highSocre < nextGraphNode.score) {
                highSocre = nextGraphNode.score;
                highMove = nextGraphNode.move;
            }
        }

        return highMove;
    }

    /**
     * Update Q value
     * @param currNode
     * @param m
     * @param nextNode
     */
    public void updateGraphNode (CurrGraphNode currNode, MOVE m, CurrGraphNode nextNode) {

        NextGraphNode link = new NextGraphNode(currNode, m, nextNode);

        boolean isPillate = nextNode.game.wasPillEaten();
        boolean isPowerPillate = nextNode.game.wasPowerPillEaten();
        int isGhostAte = nextNode.game.getNumGhostsEaten() - currNode.game.getNumGhostsEaten();
        boolean isGoBack = currNode.game.getPacmanLastMoveMade() == m.opposite();
        int livesLeft = nextNode.game.getPacmanNumberOfLivesRemaining() - currNode.game.getPacmanNumberOfLivesRemaining();

        // calculate reward
        int reward = (isPillate ? 30 : 0) + (isPowerPillate ? 20 : 0)
                   + 50 * (isGhostAte) - (isGoBack ? 7 : 5) + 300 * (livesLeft);


        // decide reward for the next state
        if (currNode.moveToNext.isEmpty() || !(currNode.moveToNext.contains(link))) {

            link.score = reward;
            currNode.moveToNext.add(link);
        } else {
            for (NextGraphNode nextGraphNode : currNode.moveToNext) {
                if (link.equals(nextGraphNode)) {
                    double temp = 0;
                    for (NextGraphNode n : nextGraphNode.nextState.moveToNext) {
                        temp = n.score > temp ? n.score : temp;
                    }
                    // update q value as follow
                    link.score = nextGraphNode.score + learningRate * (reward + discount * temp - nextGraphNode.score);
                    currNode.moveToNext.remove(nextGraphNode);
                    currNode.moveToNext.add(link);
                    break;
                }
            }
        }
    }

}
