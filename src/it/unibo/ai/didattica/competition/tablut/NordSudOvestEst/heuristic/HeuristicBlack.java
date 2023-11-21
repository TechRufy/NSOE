package it.unibo.ai.didattica.competition.tablut.NordSudOvestEst.heuristic;

import it.unibo.ai.didattica.competition.tablut.NordSudOvestEst.Domain.NSOEgame;
import it.unibo.ai.didattica.competition.tablut.domain.State;

import java.util.*;

public class HeuristicBlack extends Heuristic {


    public HeuristicBlack(State state) {

        super(state);

    }


    public double HKingDistance(){

        ArrayList<Integer> distance = new ArrayList<>();

        for (Integer[] p: this.getBlackP()) {

            Integer[] sopra = {this.getKing()[0] + 1, this.getKing()[1]};
            Integer[] sotto = {this.getKing()[0] - 1, this.getKing()[1]};
            Integer[] destra = {this.getKing()[0] , this.getKing()[1] + 1};
            Integer[] sinistra = {this.getKing()[0] + 1, this.getKing()[1] - 1};

            ArrayList<Integer> Dcroce = new ArrayList<>();
            Dcroce.add(ManhatthanDistance(p,sopra));
            Dcroce.add(ManhatthanDistance(p,sotto));
            Dcroce.add(ManhatthanDistance(p,destra));
            Dcroce.add(ManhatthanDistance(p,sinistra));

            distance.add(Collections.min(Dcroce));
        }

            OptionalDouble average = distance.stream().mapToDouble(a -> a).average();

            double d = average.isPresent() ? average.getAsDouble() : 0;

            return d/13;
    }


    public double smallDiagonal(){
        ArrayList<Integer[]> StrategicPosition = new ArrayList<>(Arrays.asList(
                new Integer[]{1, 2},
                new Integer[]{2, 1},
                new Integer[]{6, 1},
                new Integer[]{7, 2},
                new Integer[]{7, 6},
                new Integer[]{6, 7},
                new Integer[]{2, 7},
                new Integer[]{1, 6}));



        double c = (double) StrategicPosition.stream().filter(tile -> this.getState().getPawn(tile[0],tile[1]).equals(State.Pawn.WHITE)).count();

        return c/8;


    }

    public double bigDiagonal(){
        ArrayList<Integer[]> StrategicPosition = new ArrayList<>(Arrays.asList(
                new Integer[]{3, 1},
                new Integer[]{2, 2},
                new Integer[]{1, 3},
                new Integer[]{5, 1},
                new Integer[]{6, 2},
                new Integer[]{7, 3},
                new Integer[]{7, 5},
                new Integer[]{6, 6},
                new Integer[]{5, 7},
                new Integer[]{3, 7},
                new Integer[]{2, 6},
                new Integer[]{1, 5}));



        double c = (double) StrategicPosition.stream().filter(tile -> this.getState().getPawn(tile[0],tile[1]).equals(State.Pawn.WHITE)).count();

        return c/12;


    }

    public double KingCross(){

        ArrayList<Integer[]> Cross = new ArrayList<>(Arrays.asList(
                new Integer[]{this.getKing()[0] + 1,this.getKing()[1]},
                new Integer[]{this.getKing()[0] - 1,this.getKing()[1]},
                new Integer[]{this.getKing()[0],this.getKing()[1] + 1},
                new Integer[]{this.getKing()[0],this.getKing()[1] - 1}));

        double value = 0;
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
            value = 1;
        }

        if (value == 0.7 && flag == Boolean.TRUE){
            value = 1;
        }


        return value;
    }



    @Override
    public double evaluate() {

        double K = HKingDistance();
        double SD = smallDiagonal();
        double KC = KingCross();
        double NB = super.getNblack()/16.0;
        double NW = 1 - super.getNWhite()/9.0;
        double FK = freeKing();
        double BD = bigDiagonal();
        double KP =  1;
        double SDP = 20;
        double KCP = 4;
        double NBP = 4;
        double NWP = 5;
        double FKP = 50;
        double BDP = 20;



        return (K*KP + KC*KCP + SD*SDP + NW*NWP + NB*NBP + FK*FKP + BD*BDP)/(KP +  KCP + NWP + NBP + FKP+ SDP + BDP);
    }

}