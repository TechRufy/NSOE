package it.unibo.ai.didattica.competition.tablut.NordSudOvestEst.heuristic;

import it.unibo.ai.didattica.competition.tablut.domain.State;

public class Heuristic {

    private State state;
    private int Nblack;
    private int NWhite;

    public Heuristic(State state) {

        this.state = state;
        State.Pawn[][] board = this.state.getBoard();
        for (State.Pawn[] rows: board) {
            for (State.Pawn p: rows) {

                if (p.equalsPawn(String.valueOf(State.Pawn.BLACK))){
                    this.Nblack = this.Nblack + 1;
                }

                if (p.equalsPawn(String.valueOf(State.Pawn.WHITE)) || p.equalsPawn(String.valueOf(State.Pawn.KING))){
                    this.NWhite = this.NWhite + 1;
                }
            }
        }
    }


    public int ManhatthanDistance(Integer[] p1, Integer[] p2){

        return Math.abs(p1[0] - p2[0]) + Math.abs(p1[1] - p2[1]);

    }


    public double evaluate(){

        return 0;
    }

    public State getState() {
        return state;
    }

    public int getNblack() {
        return Nblack;
    }

    public int getNWhite() {
        return NWhite;
    }
}
