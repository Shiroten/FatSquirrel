package de.hsa.games.fatsquirrel;

import de.hsa.games.fatsquirrel.core.BoardView;
import de.hsa.games.fatsquirrel.util.ui.Command;

/**
 * Created by tillm on 04.04.2017.
 */
public interface UI {
    void render(BoardView view);
    Command getCommand();

}
