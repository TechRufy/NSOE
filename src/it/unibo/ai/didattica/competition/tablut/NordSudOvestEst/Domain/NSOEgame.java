package it.unibo.ai.didattica.competition.tablut.NordSudOvestEst.Domain;

import it.unibo.ai.didattica.competition.tablut.NordSudOvestEst.heuristic.Heuristic;
import it.unibo.ai.didattica.competition.tablut.NordSudOvestEst.heuristic.HeuristicBlack;
import it.unibo.ai.didattica.competition.tablut.NordSudOvestEst.heuristic.HeuristicWhite;
import it.unibo.ai.didattica.competition.tablut.domain.Action;
import it.unibo.ai.didattica.competition.tablut.domain.GameAshtonTablut;
import it.unibo.ai.didattica.competition.tablut.domain.State;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class NSOEgame extends GameAshtonTablut implements aima.core.search.adversarial.Game<State, Action, State.Turn>{
    public NSOEgame(int repeated_moves_allowed, int cache_size, String logs_folder, String whiteName, String blackName) {
        super(repeated_moves_allowed, cache_size, logs_folder, whiteName, blackName);
    }
    @Override
    public State getInitialState() {
        return null;
    }

    @Override
    public State.Turn[] getPlayers() {
        State.Turn []retval={State.Turn.BLACK,State.Turn.WHITE};
        return retval;
    }

    @Override
    public State.Turn getPlayer(State state) {
        return state.getTurn();
    }

    @Override
    public List<Action> getActions(State state) {
        State.Turn turn = state.getTurn();

        int rowIncr[] = {1, 0, -1, 0}; //rowIncr from aima library
        int colIncr[] = {0, 1, 0, -1};

        List<Action> possibleActions = new ArrayList<>();
        State.Pawn pawns=state.getTurn().equals(State.Turn.BLACK)? State.Pawn.BLACK: State.Pawn.WHITE;
        List<Integer[]> pawn = getPositionsOf(state, pawns);
        if(state.getTurn().equalsTurn(String.valueOf(State.Turn.WHITE))){
         pawn.addAll(getPositionsOf(state,State.Pawn.KING));
        }
        for (Integer []pos : pawn){
            int i=pos[0];
            int j=pos[1];

            for(int k=0; k < 4 ;k++) { //4 length of rowIncr, colIncr
                int rIncr = rowIncr[k];
                int cIncr = colIncr[k];

                int rBound = rIncr > 0 ? state.getBoard().length : -1;
                int cBound = cIncr > 0 ? state.getBoard().length : -1;

                int row = i + rIncr;
                int col = j + cIncr;

                boolean uscito=!getCitadels().contains(state.getBox(i, j)); // true if the pawn is not on a citadel
                // search on top of pawn
                while(row != rBound && col != cBound) {

                    if (!getCitadels().contains(state.getBox(row,col))) uscito=true;
                        // break if pawn is out of citadels and it is moving on a citadel
                    else if (uscito) break;

                    if (!getCitadels().contains(state.getBox(i, j)) && getCitadels().contains(state.getBox(row, col))) {
                        break;
                    }

                    // check if we are moving on a empty cell
                    else if (state.getPawn(row, col).equalsPawn(State.Pawn.EMPTY.toString())) {

                        String from = state.getBox(i, j);
                        String to = state.getBox(row, col);

                        Action action = null;
                        try {
                            action = new Action(from, to, turn);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        // check if action is admissible and if it is, add it to list possibleActions
                        try {
                            this.checkMove(state.clone(), action);
                            possibleActions.add(action);
                        } catch (Exception e) {
                            //Do nothing
                        }
                    } else {
                        // there is a pawn in the same cell and it cannot be crossed
                        break;
                    }

                    row += rIncr;
                    col += cIncr;
                }
            }



        }

        return possibleActions;
    }

    @Override
    public State getResult(State state, Action action) {

        state = this.movePawn(state.clone(), action);

        if (state.getTurn().equalsTurn("W")) {
            state = this.checkCaptureBlack(state, action);
        } else if (state.getTurn().equalsTurn("B")) {
            state = this.checkCaptureWhite(state, action);
        }

        return state;
    }

    @Override
    public boolean isTerminal(State state) {
        State.Turn turn = state.getTurn();
        return turn.equals(State.Turn.BLACKWIN)||turn.equals(State.Turn.WHITEWIN)||turn.equals(State.Turn.DRAW);
    }

    @Override
    public double getUtility(State state, State.Turn turn) {


        if (turn.equalsTurn(String.valueOf(State.Turn.BLACK)) && state.getTurn().equalsTurn(String.valueOf(State.Turn.BLACKWIN))){
            return Double.POSITIVE_INFINITY;
        } else if (turn.equalsTurn(String.valueOf(State.Turn.BLACK)) && state.getTurn().equalsTurn(String.valueOf(State.Turn.WHITEWIN))) {
            return Double.NEGATIVE_INFINITY;
        } else if (turn.equalsTurn(String.valueOf(State.Turn.WHITE)) && state.getTurn().equalsTurn(String.valueOf(State.Turn.BLACKWIN))){
            System.out.println("bianco perde");
            return Double.NEGATIVE_INFINITY;
        } else if (turn.equalsTurn(String.valueOf(State.Turn.WHITE)) && state.getTurn().equalsTurn(String.valueOf(State.Turn.WHITEWIN))) {
            System.out.println("bianco vince");
            return Double.POSITIVE_INFINITY;
        }

        Heuristic h;

        if (turn.equalsTurn(String.valueOf(State.Turn.WHITE))){
            h = new HeuristicWhite(state);
        }else {
            h = new HeuristicBlack(state);
        }

        double v = h.evaluate();

        return v;
    }

    static public List<Integer[]> getPositionsOf(State state, State.Pawn pawn){
        String linearState = state.toLinearString().substring(0, 81);
        int pos = 0;
        int boardlength = state.getBoard().length;
        List<Integer[]> result = new ArrayList<>();
        while((pos = linearState.indexOf(pawn.toString(),pos)) > 0){
            result.add(new Integer[]{pos / boardlength,pos % boardlength});
            pos += 1;
        }
        return result;
    }

}
