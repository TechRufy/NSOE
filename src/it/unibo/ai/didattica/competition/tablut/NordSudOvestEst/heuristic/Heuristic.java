package it.unibo.ai.didattica.competition.tablut.NordSudOvestEst.heuristic;

import it.unibo.ai.didattica.competition.tablut.domain.State;

public class Heuristic {

    private State state;
    private int Nblack;
    private int NWhite;

    public Heuristic(State state) {
        this.state = state;

        State.Pawn[][] board = state.getBoard();
        int nblack = 0;

        for (State.Pawn[] rows: board) {
            for (State.Pawn p: rows) {

                if (p.equalsPawn(String.valueOf(State.Pawn.BLACK))){
                    this.Nblack = this.Nblack + 1;
                }

                if (p.equalsPawn(String.valueOf(State.Pawn.WHITE))){
                    this.NWhite = this.NWhite + 1;
                }


            }

        }
    }
    public double evaluateState(){

        return 0;
    }




}
