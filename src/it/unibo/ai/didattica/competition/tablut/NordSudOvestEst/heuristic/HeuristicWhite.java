package it.unibo.ai.didattica.competition.tablut.NordSudOvestEst.heuristic;

import it.unibo.ai.didattica.competition.tablut.NordSudOvestEst.Domain.NSOEgame;
import it.unibo.ai.didattica.competition.tablut.NordSudOvestEst.NSOEPlayer;
import it.unibo.ai.didattica.competition.tablut.domain.State;

import java.util.*;

public class HeuristicWhite extends Heuristic {
    public HeuristicWhite(State state) {
        super(state);
    }

    public double XPosition() {
        ArrayList<Integer[]> StrategicPosition = new ArrayList<>(Arrays.asList(
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


        double c = (double) StrategicPosition.stream().filter(tile -> this.getState().getPawn(tile[0], tile[1]).equals(State.Pawn.WHITE)).count();

        return c / 16;


    }

    public double Inthrone() {
        if (Arrays.equals(this.getKing(), new Integer[]{4, 4})) {
            return 0;
        } else {
            return 0.4;
        }
    }

    public double kingStrategic() {
        ArrayList<Integer[]> kingStrategicPosition1 = new ArrayList<>(Arrays.asList(
                new Integer[]{2, 2},
                new Integer[]{6, 2},
                new Integer[]{2, 6},
                new Integer[]{6, 6}));

        List<Integer[]> blackP = getBlackP();

        Integer[] p1 = {4, 2};
        Integer[] p2 = {2, 4};
        Integer[] p3 = {4, 6};
        Integer[] p4 = {6, 4};
        Integer[] b1 = {4, 3};
        Integer[] b2 = {3, 4};
        Integer[] b3 = {4, 5};
        Integer[] b4 = {5, 4};

        for (Integer[] tile : kingStrategicPosition1) {
            if (Arrays.equals(this.getKing(), tile)) {
                return 2;
            }
        }
        for (Integer[] tileBlack : blackP) {
            if (Arrays.equals(this.getKing(), p1) && Arrays.equals(tileBlack, b1)) {
                return 0;
            }
            if (Arrays.equals(this.getKing(), p2) && Arrays.equals(tileBlack, b2)) {
                return 0;
            }
            if (Arrays.equals(this.getKing(), p3) && Arrays.equals(tileBlack, b3)) {
                return 0;
            }
            if (Arrays.equals(this.getKing(), p4) && Arrays.equals(tileBlack, b4)) {
                return 0;
            }
            if (Arrays.equals(this.getKing(), p1)) {
                return 2;
            }
            if (Arrays.equals(this.getKing(), p2)) {
                return 2;
            }
            if (Arrays.equals(this.getKing(), p3)) {
                return 2;
            }
            if (Arrays.equals(this.getKing(), p4)) {
                return 2;
            }
        }
        return 0;
    }


    @Override
    public double evaluate() {

        double K = kingExit();
        double CD = checkDensity();
        double NB = 1 - super.getNblack() / 16.0;
        double NW = super.getNWhite() / 9.0;
        double FK = freeKing();
        double T = Inthrone();
        double X = XPosition();
        double KS = kingStrategic();

        double KP = 20;
        double CDP = 0;
        double NBP = 2;
        double NWP = 4;
        double FKP = 40;
        double TP = 20;
        double XP = 50;
        double KSP = 50;

        return (K * KP + KS * KSP + NW * NWP + NB * NBP + FK * FKP + T * TP + X * XP) / (KP + KSP + NWP + NBP + TP + FKP + XP);

    }

    private double kingExit() {
        List<Integer[]> winTiles = (List<Integer[]>) new ArrayList<>(Arrays.asList(
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
        List<Integer[]> kingPos = NSOEgame.getPositionsOf(super.getState(), State.Pawn.KING);
        int min = 10;
        for (Integer[] tile : winTiles) {
            int mnd = super.ManhatthanDistance(kingPos.get(0), tile);
            if (min > mnd) {
                min = mnd;
            }
        }
        return 1 - ((double) min / 6);
    }

    private double checkDensity() {

        int blackNumber = 0;
        /*if(this.getKing()[0] == 4 || this.getKing()[1] == 4){
            return 0.5;
        }*/
        int[] quarter = new int[4];
        if (this.getKing()[0] >= 0 && this.getKing()[0] <= 3 && this.getKing()[1] >= 0 && this.getKing()[1] <= 3) {
            quarter = new int[]{0, 3, 0, 3};
        }
        if (this.getKing()[0] >= 5 && this.getKing()[0] <= 8 && this.getKing()[1] >= 0 && this.getKing()[1] <= 3) {
            quarter = new int[]{5, 8, 0, 3};
        }
        if (this.getKing()[0] >= 0 && this.getKing()[0] <= 3 && this.getKing()[1] >= 5 && this.getKing()[1] <= 8) {
            quarter = new int[]{0, 3, 5, 8};
        }
        if (this.getKing()[0] >= 5 && this.getKing()[0] <= 8 && this.getKing()[1] >= 5 && this.getKing()[1] <= 8) {
            quarter = new int[]{5, 8, 5, 8};
        }
        for (Integer[] blackP : this.getBlackP()) {
            if (blackP[0] >= quarter[0] && blackP[0] <= quarter[1] && blackP[1] >= quarter[2] && blackP[1] <= quarter[3]) {
                blackNumber++;
            }
        }
        return 1 - ((double) blackNumber / getNblack());
    }


}