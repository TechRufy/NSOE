package it.unibo.ai.didattica.competition.tablut.NordSudOvestEst.heuristic;

import it.unibo.ai.didattica.competition.tablut.NordSudOvestEst.Domain.NSOEgame;
import it.unibo.ai.didattica.competition.tablut.NordSudOvestEst.NSOEPlayer;
import it.unibo.ai.didattica.competition.tablut.domain.State;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HeuristicWhite extends Heuristic{
    public HeuristicWhite(State state) {
        super(state);
    }


    @Override
    public double evaluate() {
        return (kingExit()*5+checkDensity())/6;
    }
    private double kingExit(){
        List<Integer[]> winTiles = (List<Integer[]>) new ArrayList<>(Arrays.asList(
                new Integer[]{0,1},
                new Integer[]{0,2},
                new Integer[]{0,6},
                new Integer[]{0,7},
                new Integer[]{1,0},
                new Integer[]{2,0},
                new Integer[]{6,0},
                new Integer[]{7,0},
                new Integer[]{8,1},
                new Integer[]{8,2},
                new Integer[]{8,6},
                new Integer[]{8,7},
                new Integer[]{1,8},
                new Integer[]{2,8},
                new Integer[]{6,8},
                new Integer[]{7,8}
        ));
        List<Integer[]> kingPos = NSOEgame.getPositionsOf(super.getState(), State.Pawn.KING);
        int min = 10;
        for(Integer[] tile: winTiles){
            int mnd = super.ManhatthanDistance(kingPos.get(0), tile);
            if(min>mnd){
                min = mnd;
            }
        }
        return 1-((double) min /6);
    }

    private double checkDensity(){
        List<Integer[]> blackPos = NSOEgame.getPositionsOf(super.getState(), State.Pawn.BLACK);
        List<Integer[]> kingPos = NSOEgame.getPositionsOf(super.getState(), State.Pawn.KING);
        int blackNumber = 0;
        if(kingPos.get(0)[0] == 4 || kingPos.get(0)[1] == 4){
            return 0.5;
        }
        int[] quarter = new int[4];
        if(kingPos.get(0)[0] >= 0 && kingPos.get(0)[0] <= 3 && kingPos.get(0)[1] >= 0 && kingPos.get(0)[1] <= 3){
            quarter = new int[]{0, 3, 0, 3};
        }
        if(kingPos.get(0)[0] >= 5 && kingPos.get(0)[0] <= 8 && kingPos.get(0)[1] >= 0 && kingPos.get(0)[1] <= 3){
            quarter = new int[]{5, 8, 0, 3};
        }
        if(kingPos.get(0)[0] >= 0 && kingPos.get(0)[0] <= 3 && kingPos.get(0)[1] >= 5 && kingPos.get(0)[1] <= 8){
            quarter = new int[]{0, 3, 5, 8};
        }
        if(kingPos.get(0)[0] >= 5 && kingPos.get(0)[0] <= 8 && kingPos.get(0)[1] >= 5 && kingPos.get(0)[1] <= 8){
            quarter = new int[]{5, 8, 5, 8};
        }
        for(Integer[] blackP: blackPos){
            if(blackP[0] >= quarter[0] && blackP[0] <= quarter[1] && blackP[1] >= quarter[2] && blackP[1] <= quarter[3]){
                blackNumber++;
            }
        }
        return 1-((double)blackNumber / getNblack());
    }


    private double blackEaten(){
        List<Integer[]> blackPos = NSOEgame.getPositionsOf(super.getState(), State.Pawn.BLACK);
        return 1-(getNblack() / 16);
    }

    protected double checkHazard(){
        List<Integer[]> whitePos = NSOEgame.getPositionsOf(super.getState(), State.Pawn.WHITE);
        int numberHazard = 0;

        for(Integer[] whiteP: whitePos){
            if(super.getState().getPawn(whiteP[0]-1, whiteP[1]) == State.Pawn.BLACK){

            }
            if(super.getState().getPawn(whiteP[0]+1, whiteP[1]) == State.Pawn.BLACK){

            }
            if(super.getState().getPawn(whiteP[0], whiteP[1]-1) == State.Pawn.BLACK){

            }
            if(super.getState().getPawn(whiteP[0], whiteP[1]+1) == State.Pawn.BLACK){

            }
        }
        return 0;
    }

    private double checkThreat(){
        return 0;
    }



}
