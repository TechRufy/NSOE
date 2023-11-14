package it.unibo.ai.didattica.competition.tablut.NordSudOvestEst;

import aima.core.search.adversarial.Game;
import aima.core.search.adversarial.IterativeDeepeningAlphaBetaSearch;
import it.unibo.ai.didattica.competition.tablut.domain.Action;
import it.unibo.ai.didattica.competition.tablut.domain.State;

public class NSOEPlayer extends IterativeDeepeningAlphaBetaSearch<State, Action, State.Turn> {
    public NSOEPlayer(aima.core.search.adversarial.Game<State, Action, State.Turn> game, double utilMin, double utilMax, int time) {
        super(game, utilMin, utilMax, time);
    }

    @Override
    protected double eval(State state, State.Turn player) {
        // needed to make heuristicEvaluationUsed = true, if the state evaluated isn't terminal
        super.eval(state, player);
        // return heuristic value for given state
        return game.getUtility(state, player);
    }

    @Override
    public Action makeDecision(State state) {
        return super.makeDecision(state);
    }

}