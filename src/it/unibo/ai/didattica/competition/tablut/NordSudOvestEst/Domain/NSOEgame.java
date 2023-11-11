package it.unibo.ai.didattica.competition.tablut.NordSudOvestEst.Domain;

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

        List<Action> possibleActions = new ArrayList<Action>();
        State.Pawn pawns=state.getTurn().equals(State.Turn.BLACK)? State.Pawn.BLACK: State.Pawn.WHITE;
        for (Integer []pos : getPositionsOf(state, pawns)){
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
        State.Turn turn=state.getTurn();
        return turn.equals(State.Turn.BLACKWIN)||turn.equals(State.Turn.WHITEWIN)||turn.equals(State.Turn.DRAW);
    }

    @Override
    public double getUtility(State state, State.Turn turn) {

        return new Random().nextInt();
    }

    public List<Integer[]> getPositionsOf(State state, State.Pawn pawn){
        String strState=state.toLinearString().substring(0, 81);
        int pos=0;
        int boardsize=state.getBoard().length;
        List<Integer[]> result=new ArrayList<>();
        while((pos=strState.indexOf(pawn.toString(),pos))>0){
            result.add(new Integer[]{pos/boardsize,pos%boardsize});
            pos+=1;
        }
        Integer king[]= {strState.indexOf("K")/boardsize,strState.indexOf("K")%boardsize};
        if (pawn.equals(State.Pawn.WHITE)) result.add(king);
        Collections.sort(result,(p1, p2)->{
            int dist1=Math.max(Math.abs(p1[0]-king[0]),Math.abs(p1[1]-king[1]));
            int dist2=Math.max(Math.abs(p2[0]-king[0]),Math.abs(p2[1]-king[1]));
            return dist1-dist2;
        });

        return result;
    }

}
