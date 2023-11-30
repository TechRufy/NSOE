package it.unibo.ai.didattica.competition.tablut.NordSudOvestEst.heuristic;

import it.unibo.ai.didattica.competition.tablut.NordSudOvestEst.Domain.NSOEgame;
import it.unibo.ai.didattica.competition.tablut.domain.State;

import java.util.*;

public class HeuristicBlack extends Heuristic {

    private final Integer Krow = this.getKing()[0];
    private final Integer Kcolumn = this.getKing()[1];


    private static final ArrayList<Integer[]> OuterStrategicPosition = new ArrayList<>(Arrays.asList(
            new Integer[]{1, 2},
            new Integer[]{2, 1},
            new Integer[]{6, 1},
            new Integer[]{7, 2},
            new Integer[]{7, 6},
            new Integer[]{6, 7},
            new Integer[]{2, 7},
            new Integer[]{1, 6}));

    private static final ArrayList<Integer[]> InnerStrategicPosition = new ArrayList<>(Arrays.asList(
            new Integer[]{3, 2},
            new Integer[]{2, 3},
            new Integer[]{2, 5},
            new Integer[]{3, 6},
            new Integer[]{5, 6},
            new Integer[]{6, 5},
            new Integer[]{6, 3},
            new Integer[]{5, 2}));


    public HeuristicBlack(State state) {

        super(state);

    }


    public double HKingDistance(){

        ArrayList<Integer> distance = new ArrayList<>();


        for (Integer[] p: this.getBlackP()) {

            Integer[] sopra = {Krow + 2, Kcolumn};
            Integer[] sotto = {Krow - 2, Kcolumn};
            Integer[] destra = {Krow, Kcolumn + 2};
            Integer[] sinistra = {Krow, Kcolumn - 2};

            ArrayList<Integer> Dcroce = new ArrayList<>();
            Dcroce.add(ManhatthanDistance(p,sopra));
            Dcroce.add(ManhatthanDistance(p,sotto));
            Dcroce.add(ManhatthanDistance(p,destra));
            Dcroce.add(ManhatthanDistance(p,sinistra));

            distance.add(Collections.min(Dcroce));
        }

            OptionalDouble average = distance.stream().mapToDouble(a -> a).average();

            double d = average.isPresent() ? average.getAsDouble() : 0;

            return d/13.0;
    }


    public double OuterDiagonal(){

        double c = (double) OuterStrategicPosition.stream().filter(tile -> this.getState().getPawn(tile[0],tile[1]).equals(State.Pawn.WHITE)).count();

        return c/8.0;


    }

    public double InnerDiagonal(){

        double c = (double) InnerStrategicPosition.stream().filter(tile -> this.getState().getPawn(tile[0],tile[1]).equals(State.Pawn.WHITE)).count();

        return c/8.0;
    }

    public double KingCross(){

        ArrayList<Integer[]> Cross = new ArrayList<>(Arrays.asList(
                new Integer[]{Krow + 1,Kcolumn},
                new Integer[]{Krow - 1,Kcolumn},
                new Integer[]{Krow,Kcolumn + 1},
                new Integer[]{Krow,Kcolumn - 1}));

        double value = 0.0;
        Boolean flag = Boolean.FALSE;
        for (Integer[] p : Cross) {
            if (super.getState().getPawn(p[0],p[1]) == State.Pawn.BLACK || super.getState().getPawn(p[0],p[1]) == State.Pawn.THRONE){
                value += 0.2;
            } else if (super.getState().getPawn(p[0],p[1]) == State.Pawn.EMPTY) {
                value += 0.1;
            } else if (this.getCitadels().contains(p)) {
                flag = Boolean.TRUE;
                value += 0.3;
            }


        }

        if (value == 0.8 && flag == Boolean.FALSE){
            value = 1.0;
        }

        if (value == 0.7 && flag == Boolean.TRUE){
            value = 1.0;
        }


        return value;
    }



    @Override
    public double evaluate() {

        double K = HKingDistance();
        double OD = OuterDiagonal();
        double KC = KingCross();
        double NB = super.getNblack()/16.0;
        double NW = 1.0 - super.getNWhite()/9.0;
        double FK = freeKing();
        double ID = InnerDiagonal();
        double KP =  10.0;
        double ODP = 25.0;
        double KCP = 10.0;
        double NBP = 18.0;
        double NWP = 10.0;
        double FKP = 25.0;
        double IDP = 10.0;



        return (K*KP + KC*KCP + OD*ODP + NW*NWP + NB*NBP + FK*FKP + ID*IDP); ///(KP +  KCP + NWP + NBP + FKP+ ODP + IDP);
    }

}