package pacman.game;

import org.omg.CORBA.OBJ_ADAPTER;
import pacman.game.Constants.MOVE;


/**
 * Create next Graph Node
 * Created by shengchen on 4/12/16.
 */
public class NextGraphNode {

    public double score;
    public MOVE move;
    public CurrGraphNode initailState;
    public CurrGraphNode nextState;

    public NextGraphNode(CurrGraphNode initialState, MOVE move, CurrGraphNode nextState ) {
        this.initailState = initialState;
        this.nextState = nextState;
        this.move = move;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        NextGraphNode node = (NextGraphNode) obj;
        if (initailState == null) {
            if (node.initailState != null) {
                return false;
            }
        } else if (!initailState.equals(node.initailState)) {
            return false;
        }

        if (nextState == null) {
            if (node.nextState != null) {
                return false;
            }
        } else if (!nextState.equals(node.nextState)) {
            return false;
        }

        return true;
    }

}
