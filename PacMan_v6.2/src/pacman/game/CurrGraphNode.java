package pacman.game;

import pacman.game.Constants.MOVE;
import pacman.game.Constants.GHOST;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Current Node of the graph
 * Created by shengchen on 4/12/16.
 */
public class CurrGraphNode {

    public Game game;
    private int distPill; // distance between panman to pill
    private int distPowerPill; // distance between panman to power pill
    private int distGhost;  // distance between panman to ghost
    private boolean ghostInLair = false; // is ghost in his lair


    public Set<NextGraphNode> moveToNext = new HashSet<>();

    public CurrGraphNode(Game game) {
        this.game = game;

        int currIndex = game.getPacmanCurrentNodeIndex();

        int[] pills = game.getActivePillsIndices();
        int[] powerpills = game.getActivePillsIndices();

        this.distPill = Integer.MAX_VALUE;

        // find the nearest pill
        for (int i  = 0; i < pills.length; i++) {
            int temp = game.getManhattanDistance(currIndex, pills[i]);
            this.distPill = temp < this.distPill ? temp : this.distPill;
        }


        // find the nearest power pill
        this.distPowerPill = Integer.MAX_VALUE;

        for (int i = 0; i < powerpills.length; i++) {
            int temp = game.getManhattanDistance(currIndex, powerpills[i]);
            this.distPowerPill = temp < this.distPowerPill ? temp : this.distPill;
        }

        // find the nearest ghost
        GHOST[] ghosts = GHOST.values();
        this.distGhost = Integer.MAX_VALUE;


        for (GHOST ghost : ghosts) {
            int temp = game.getShortestPathDistance(currIndex, game.getGhostCurrentNodeIndex(ghost));
            if (temp < distGhost) {
                this.distGhost = temp;
                this.ghostInLair = game.getGhostEdibleTime(ghost) > 0;
            }
        }

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
        CurrGraphNode node = (CurrGraphNode) obj;

        return this.ghostInLair == node.ghostInLair && this.distGhost == node.distGhost
                && this.distPill == node.distPill && this.distPowerPill == node.distPowerPill;
    }

}
