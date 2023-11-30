package it.unibo.ai.didattica.competition.tablut.NordSudOvestEst.heuristic;

import it.unibo.ai.didattica.competition.tablut.NordSudOvestEst.Domain.NSOEgame;
import it.unibo.ai.didattica.competition.tablut.NordSudOvestEst.NSOEPlayer;
import it.unibo.ai.didattica.competition.tablut.domain.State;

import java.util.*;

public class HeuristicWhite extends Heuristic {

    Integer Krow = this.getKing()[0];
    Integer Kcolumn = this.getKing()[1];

    private static final List<Integer[]> winTiles = new ArrayList<>(Arrays.asList(
            new Integer[]{0, 1},
            new Integer[]{0, 2},
            new Integer[]{0, 6},
            new Integer[]{0, 7},
            new Integer[]{1, 0},
            new Integer[]{2, 0},
            new Integer[]{6, 0},
            new Integer[]{7, 0},
            new Integer[]{8, 1},
            new Integer[]{8, 2},
            new Integer[]{8, 6},
            new Integer[]{8, 7},
            new Integer[]{1, 8},
            new Integer[]{2, 8},
            new Integer[]{6, 8},
            new Integer[]{7, 8}
    ));


    private static final ArrayList<Integer[]> StrategicPosition = new ArrayList<>(Arrays.asList(
            new Integer[]{2, 2},
            new Integer[]{3, 3},
            new Integer[]{5, 3},
            new Integer[]{6, 2},
            new Integer[]{5, 5},
            new Integer[]{6, 6},
            new Integer[]{3, 5},
            new Integer[]{2, 6},
            new Integer[]{1, 3},
            new Integer[]{1, 5},
            new Integer[]{3, 1},
            new Integer[]{5, 1},
            new Integer[]{3, 7},
            new Integer[]{5, 7},
            new Integer[]{7, 3},
            new Integer[]{7, 5}));

    private static final ArrayList<Integer[]> kingStrategicPosition1 = new ArrayList<>(Arrays.asList(
            new Integer[]{2, 2},
            new Integer[]{6, 2},
            new Integer[]{2, 6},
            new Integer[]{6, 6}));


    private static final ArrayList<Integer[]> kingStrategicPosition2 = new ArrayList<>(Arrays.asList(
            new Integer[]{4, 2},
            new Integer[]{2, 4},
            new Integer[]{4, 6},
            new Integer[]{6, 4}));

    public HeuristicWhite(State state) {
        super(state);
    }

    public double XPosition() {


        double c = (double) StrategicPosition.stream().filter(tile -> this.getState().getPawn(tile[0], tile[1]).equals(State.Pawn.WHITE)).count();

        return c / 16.0;


    }
/*
    public double Inthrone() {
        if (Arrays.equals(this.getKing(), new Integer[]{4, 4})) {
            return 0;
        } else {
            return 0.5;
        }
    }

 */

    public double kingStrategic() {


        if(kingStrategicPosition1.stream().anyMatch(tile -> Arrays.equals(tile, this.getKing()))){
            return 1;
        };

        if(kingStrategicPosition2.stream().anyMatch(tile -> Arrays.equals(tile, this.getKing()))){
            return 0.5;
        };
        return 0.0;
    }


    @Override
    public double evaluate() {

        double K = kingExit();
        double CD = checkDensity();
        double NB = 1 - super.getNblack() / 16.0;
        double NW = super.getNWhite() / 9.0;
        double FK = freeKing();
        //double T = Inthrone();
        double X = XPosition();
        double KS = kingStrategic();
        double WC = WhitePawnCross();

        double KP = 3.0;
        double CDP = 10.0;
        double NBP = 25.0;
        double NWP = 40.0;
        double FKP = 7.0;
        //double TP = 0.0;
        double XP = 13.0;
        double KSP = 8.0;
        double WCP = 15.0;

        return (K * KP + KS * KSP + NW * NWP + NB * NBP + FK * FKP + X * XP + CD*CDP + WC*WCP);

    }

    private double kingExit() {
        int min = 10;
        for (Integer[] tile : winTiles) {
            int mnd = super.ManhatthanDistance(this.getKing(), tile);
            if (min > mnd) {
                min = mnd;
            }
        }
        return 1 - ((double) min / 6.0);
    }

    private double checkDensity() {

        int blackNumber = 0;
        int[] quarter = new int[4];
        if (Krow >= 0 && Krow <= 3 && Kcolumn >= 0 && Kcolumn <= 3) {
            quarter = new int[]{0, 3, 0, 3};
        }
        if (Krow >= 5 && Krow <= 8 && Kcolumn >= 0 && Kcolumn <= 3) {
            quarter = new int[]{5, 8, 0, 3};
        }
        if (Krow >= 0 && Krow <= 3 && Kcolumn >= 5 && Kcolumn <= 8) {
            quarter = new int[]{0, 3, 5, 8};
        }
        if (Krow >= 5 && Krow <= 8 && Kcolumn >= 5 && Kcolumn <= 8) {
            quarter = new int[]{5, 8, 5, 8};
        }
        for (Integer[] blackP : this.getBlackP()) {
            if (blackP[0] >= quarter[0] && blackP[0] <= quarter[1] && blackP[1] >= quarter[2] && blackP[1] <= quarter[3]) {
                blackNumber++;
            }
        }
        return 1 - ((double) blackNumber / getNblack());
    }

    public double WhitePawnCross(){

        double val = 0.0;

        for(Integer[] pawn : this.getWhiteP()){

            if(pawn[0] < 8 && super.getState().getPawn(pawn[0]+1, pawn[1]) == State.Pawn.EMPTY){
                val += 0.25;
            }
            if(pawn[1] < 8 && super.getState().getPawn(pawn[0], pawn[1]+1) == State.Pawn.EMPTY){
                val += 0.25;
            }
            if(pawn[0] > 0 && super.getState().getPawn(pawn[0]-1, pawn[1]) == State.Pawn.EMPTY){
                val += 0.25;
            }
            if(pawn[1] > 0 && super.getState().getPawn(pawn[0], pawn[1]-1) == State.Pawn.EMPTY){
                val += 0.25;
            }
            if(pawn[0] < 8 && (super.getState().getPawn(pawn[0]+1, pawn[1]) == State.Pawn.BLACK ||
                    super.getState().getPawn(pawn[0]+1, pawn[1]) == State.Pawn.KING)){
                val += 0.125;
            }
            if(pawn[1] < 8 && (super.getState().getPawn(pawn[0], pawn[1]+1) == State.Pawn.BLACK ||
                    super.getState().getPawn(pawn[0], pawn[1]+1) == State.Pawn.KING)){
                val += 0.125;
            }
            if(pawn[0] > 0 && (super.getState().getPawn(pawn[0]-1, pawn[1]) == State.Pawn.BLACK ||
                    super.getState().getPawn(pawn[0]-1, pawn[1]) == State.Pawn.KING)){
                val += 0.125;
            }
            if(pawn[1] > 0 && (super.getState().getPawn(pawn[0], pawn[1]-1) == State.Pawn.BLACK ||
                    super.getState().getPawn(pawn[0], pawn[1]-1) == State.Pawn.KING)){
                val += 0.125;
            }
        }
        return val/this.getNWhite();
    }

}