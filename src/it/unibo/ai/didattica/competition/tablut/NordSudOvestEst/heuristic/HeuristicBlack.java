package it.unibo.ai.didattica.competition.tablut.NordSudOvestEst.heuristic;

import it.unibo.ai.didattica.competition.tablut.NordSudOvestEst.Domain.NSOEgame;
import it.unibo.ai.didattica.competition.tablut.domain.State;

import java.util.*;

public class HeuristicBlack extends Heuristic {

    private Integer[] king;
    private List<Integer[]> blackP;

    private ArrayList<Integer[]> citadels;





    public HeuristicBlack(State state) {

        super(state);
        this.king = NSOEgame.getPositionsOf(super.getState(),State.Pawn.KING).get(0);
        this.blackP = NSOEgame.getPositionsOf(super.getState(),State.Pawn.BLACK);
        this.citadels = new ArrayList<>();
        this.citadels.add(new Integer[]{0,3});
        this.citadels.add(new Integer[]{0,4});
        this.citadels.add(new Integer[]{0,5});
        this.citadels.add(new Integer[]{1,4});
        this.citadels.add(new Integer[]{3,0});
        this.citadels.add(new Integer[]{4,0});
        this.citadels.add(new Integer[]{5,0});
        this.citadels.add(new Integer[]{4,1});
        this.citadels.add(new Integer[]{8,3});
        this.citadels.add(new Integer[]{8,4});
        this.citadels.add(new Integer[]{8,5});
        this.citadels.add(new Integer[]{7,4});
        this.citadels.add(new Integer[]{3,8});
        this.citadels.add(new Integer[]{4,8});
        this.citadels.add(new Integer[]{5,8});
        this.citadels.add(new Integer[]{4,7});

    }


    public double HKingDistance(){

        ArrayList<Integer> distance = new ArrayList<>();

        for (Integer[] p: this.blackP) {

            Integer[] sopra = {this.king[0] + 1, this.king[1]};
            Integer[] sotto = {this.king[0] - 1, this.king[1]};
            Integer[] destra = {this.king[0] , this.king[1] + 1};
            Integer[] sinistra = {this.king[0] + 1, this.king[1] - 1};

            ArrayList<Integer> Dcroce = new ArrayList<>();
            Dcroce.add(ManhatthanDistance(p,sopra));
            Dcroce.add(ManhatthanDistance(p,sotto));
            Dcroce.add(ManhatthanDistance(p,destra));
            Dcroce.add(ManhatthanDistance(p,sinistra));

            distance.add(Collections.min(Dcroce));
        }

            OptionalDouble average = distance.stream().mapToDouble(a -> a).average();

            double d = average.isPresent() ? average.getAsDouble() : 0;

            return d/5;
    }


    public double DstrategicPosition(){
        ArrayList<Integer[]> StrategicPosition = new ArrayList<>(Arrays.asList(
                new Integer[]{1, 2},
                new Integer[]{2, 1},
                new Integer[]{6, 1},
                new Integer[]{7, 2},
                new Integer[]{7, 6},
                new Integer[]{6, 7},
                new Integer[]{2, 7},
                new Integer[]{1, 6}));

        ArrayList<Integer> distance = new ArrayList<>();

        for (Integer[] p: this.blackP) {
            ArrayList<Integer> Distancepos = new ArrayList<>();
            for (Integer[] Pos: StrategicPosition){
                Distancepos.add(ManhatthanDistance(p,Pos));
            }
            distance.add(Collections.min(Distancepos));
        }

        OptionalDouble average = distance.stream().mapToDouble(a -> a).average();

        double d = average.isPresent() ? average.getAsDouble() : 0;

        return d/5;


    }

    public double KingCross(){

        ArrayList<Integer[]> Cross = new ArrayList<>(Arrays.asList(
                new Integer[]{this.king[0] + 1,this.king[1]},
                new Integer[]{this.king[0] - 1,this.king[1]},
                new Integer[]{this.king[0],this.king[1] + 1},
                new Integer[]{this.king[0],this.king[1] - 1}));

        double value = 0;
        Boolean flag = Boolean.FALSE;
        for (Integer[] p : Cross) {
            if (super.getState().getPawn(p[0],p[1]) == State.Pawn.BLACK || super.getState().getPawn(p[0],p[1]) == State.Pawn.THRONE){
                value += 0.2;
            } else if (super.getState().getPawn(p[0],p[1]) == State.Pawn.EMPTY) {
                value += 0.1;
            } else if (citadels.contains(p)) {
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
        double DP = DstrategicPosition();
        double KC = KingCross();
        double NB = super.getNblack()/16.0;
        double NW = 1 - super.getNWhite()/9.0;
        double KP = 10;
        double DPP = 10;
        double NBP = 1;
        double KCP = 10;
        double NWP = 1;



        return (K*KP + DP*DPP + KC*KCP + NW*NWP + NB*NBP)/(KP + DPP + KCP + NWP + NBP);
    }

}