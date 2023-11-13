package it.unibo.ai.didattica.competition.tablut.NordSudOvestEst.heuristic;

import it.unibo.ai.didattica.competition.tablut.NordSudOvestEst.Domain.NSOEgame;
import it.unibo.ai.didattica.competition.tablut.domain.State;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

public class HeuristicBlack extends Heuristic {

    public HeuristicBlack(State state) {
        super(state);
    }


    public double HKingDistance(){
        List<Integer[]> king = NSOEgame.getPositionsOf(super.getState(),State.Pawn.KING);
        List<Integer[]> blackP = NSOEgame.getPositionsOf(super.getState(),State.Pawn.BLACK);

        ArrayList<Integer> distance = new ArrayList<>();

        for (Integer[] p: blackP) {

            int Mdistance = ManhatthanDistance(p,king.get(0));
            distance.add(Mdistance);
        }

            OptionalDouble average = distance.stream().mapToDouble(a -> a).average();

            return average.isPresent() ? average.getAsDouble() : 0;
    }

    @Override
    public double evaluate() {

        //media distanze manhattan dal re V
        //distanza posizioni strategiche
        //posizioni intorno al re + possibilità di prendere
        //linea d'area re uscita
        //controllo minaccia
        //controllo minacciato

        return 0;
    }

}