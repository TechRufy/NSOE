package it.unibo.ai.didattica.competition.tablut.NordSudOvestEst.heuristic;

import it.unibo.ai.didattica.competition.tablut.NordSudOvestEst.Domain.NSOEgame;
import it.unibo.ai.didattica.competition.tablut.NordSudOvestEst.NSOEPlayer;
import it.unibo.ai.didattica.competition.tablut.domain.State;

import java.util.*;

public class HeuristicWhite extends Heuristic{
    public HeuristicWhite(State state) {
        super(state);
    }

    public double XPosition(){
        ArrayList<Integer[]> StrategicPosition = new ArrayList<>(Arrays.asList(
                new Integer[]{2, 2},
                new Integer[]{3, 3},
                new Integer[]{5, 3},
                new Integer[]{6, 2},
                new Integer[]{5, 5},
                new Integer[]{6, 6},
                new Integer[]{3, 5},
                new Integer[]{2, 6}));



        double c = (double) StrategicPosition.stream().filter(tile -> this.getState().getPawn(tile[0],tile[1]).equals(State.Pawn.WHITE)).count();

        return c/8;


    }

    public double Inthrone(){
        if(Arrays.equals(this.getKing(), new Integer[]{4, 4})){
            return 0;
        }else{
            return 1;
        }
    }


    @Override
    public double evaluate() {

        double K = kingExit();
        double CD = checkDensity();
        double NB = 1 - super.getNblack()/16.0;
        double NW = super.getNWhite()/9.0;
        double FK = freeKing();
        double T = Inthrone();
        double X = XPosition();
        double KP = 20;
        double CDP = 0;
        double NBP = 2;
        double NWP = 4;
        double FKP = 15;
        double TP = 15;
        double XP = 4;




        return (K*KP + NW*NWP + NB*NBP + FK*FKP + X*XP + T*TP + CD*CDP )/(KP +  CDP + NBP + NWP + FKP + TP + XP);

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

        int blackNumber = 0;
        /*if(this.getKing()[0] == 4 || this.getKing()[1] == 4){
            return 0.5;
        }*/
        int[] quarter = new int[4];
        if(this.getKing()[0] >= 0 && this.getKing()[0] <= 3 && this.getKing()[1] >= 0 && this.getKing()[1] <= 3){
            quarter = new int[]{0, 3, 0, 3};
        }
        if(this.getKing()[0] >= 5 && this.getKing()[0] <= 8 && this.getKing()[1] >= 0 && this.getKing()[1] <= 3){
            quarter = new int[]{5, 8, 0, 3};
        }
        if(this.getKing()[0] >= 0 && this.getKing()[0] <= 3 && this.getKing()[1] >= 5 && this.getKing()[1] <= 8){
            quarter = new int[]{0, 3, 5, 8};
        }
        if(this.getKing()[0] >= 5 && this.getKing()[0] <= 8 && this.getKing()[1] >= 5 && this.getKing()[1] <= 8){
            quarter = new int[]{5, 8, 5, 8};
        }
        for(Integer[] blackP: this.getBlackP()){
            if(blackP[0] >= quarter[0] && blackP[0] <= quarter[1] && blackP[1] >= quarter[2] && blackP[1] <= quarter[3]){
                blackNumber++;
            }
        }
        return 1-((double)blackNumber / getNblack());
    }


    protected double checkHazard(){

        int numberHazard = 0;

        for(Integer[] whiteP: this.getWhiteP()){
            for(int i = 0 ; i < whiteP[1]; i++){
                Integer[] coord = {whiteP[0], i};
                State.Pawn tileS = this.getState().getPawn(whiteP[0] + 1, i);
                State.Pawn tileD = this.getState().getPawn(whiteP[0] - 1 , i);
                if(tileS.equalsPawn(String.valueOf(State.Pawn.BLACK)) ||
                        tileD.equalsPawn(String.valueOf(State.Pawn.BLACK))){




                }
            }

            for(int i = whiteP[1] + 1 ; i < 9; i++){
                Integer[] coord = {whiteP[0], i};
                State.Pawn tile = this.getState().getPawn(whiteP[0], i);
                if(tile.equalsPawn(String.valueOf(State.Pawn.BLACK)) ||
                        tile.equalsPawn(String.valueOf(State.Pawn.WHITE)) ||
                        this.getCitadels().contains(coord)){


                    break;
                }
            }

            for(int i = 0 ; i < whiteP[0]; i++){
                Integer[] coord = {i, whiteP[1]};
                State.Pawn tile = this.getState().getPawn(whiteP[0], i);
                if(tile.equalsPawn(String.valueOf(State.Pawn.BLACK)) ||
                        tile.equalsPawn(String.valueOf(State.Pawn.WHITE)) ||
                        this.getCitadels().contains(coord)){


                    break;
                }
            }

            for(int i = whiteP[1] + 1 ; i < 9; i++){
                Integer[] coord = {i, whiteP[1]};
                State.Pawn tile = this.getState().getPawn(whiteP[0], i);
                if(tile.equalsPawn(String.valueOf(State.Pawn.BLACK)) ||
                        tile.equalsPawn(String.valueOf(State.Pawn.WHITE)) ||
                        this.getCitadels().contains(coord)){

                    break;
                }
            }
        }
        return 0;
    }

    private double checkThreat(){
        return 0;
    }



}
