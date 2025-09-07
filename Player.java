package com.isolo;

public class Player {
    private String name;
    private int wins;
    private int losses;
    private int draws;

    public Player(String name) {
        this.name = name;
        this.wins = 0;
        this.losses = 0;
        this.draws = 0;
    }

    public void addWin() {
        wins++;
    }

    public void addLoss() {
        losses++;
    }

    public void addDraw() {
        draws++;
    }

    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public int getWins() { return wins; }
    public void setWins(int wins) { this.wins = wins; }
    
    public int getLosses() { return losses; }
    public void setLosses(int losses) { this.losses = losses; }
    
    public int getDraws() { return draws; }
    public void setDraws(int draws) { this.draws = draws; }
    
    @Override
    public String toString() {
        return name + " (W: " + wins + ", L: " + losses + ", D: " + draws + ")";
    }
}
