package de.hsa.games.fatsquirrel.botimpls.ExCells26.Mini;

public enum MiniType {
    FERAL(100), RECON(100), REAPER(200), NONE(0);

    @SuppressWarnings("FieldCanBeLocal")
    private final int energy;

    MiniType(int energy) {
        this.energy = energy;
    }

    //Recon spawn when: fieldLimit null and no Recon
    //Reaper spawn when: free Cell or grid not fully expanded
    //Bomb spawn when: else
}