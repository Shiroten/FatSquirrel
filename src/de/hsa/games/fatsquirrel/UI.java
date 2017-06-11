package de.hsa.games.fatsquirrel;

import de.hsa.games.fatsquirrel.core.BoardView;
import de.hsa.games.fatsquirrel.util.ui.Command;

/**
 * The functions any UI has to have
 */
public interface UI {

    /**
     *
     * @param view Render the Frame with Information from the BoardView
     */
    void render(BoardView view);

    /**
     *
     * @return the Command Object from the UI
     */
    Command getCommand();

}
