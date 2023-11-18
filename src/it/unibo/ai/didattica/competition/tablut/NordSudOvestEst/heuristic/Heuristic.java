package it.unibo.ai.didattica.competition.tablut.NordSudOvestEst.heuristic;

import aima.gui.fx.framework.IntegrableApplication;
import it.unibo.ai.didattica.competition.tablut.NordSudOvestEst.Domain.NSOEgame;
import it.unibo.ai.didattica.competition.tablut.domain.State;

import java.util.ArrayList;
import java.util.List;

public class Heuristic {

    private State state;
    private int Nblack;
    private int NWhite;

    private Integer[] king;
    private List<Integer[]> blackP;
    private List<Integer[]> whiteP;

    private ArrayList<Integer[]> citadels;

    public Heuristic(State state) {

        this.state = state;
        State.Pawn[][] board = this.state.getBoard();

        this.king = NSOEgame.getPositionsOf(this.state,State.Pawn.KING).get(0);
        this.blackP = NSOEgame.getPositionsOf(this.state,State.Pawn.BLACK);
        this.whiteP = NSOEgame.getPositionsOf(this.state,State.Pawn.WHITE);
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

        for (State.Pawn[] rows: board) {
            for (State.Pawn p: rows) {

                if (p.equalsPawn(String.valueOf(State.Pawn.BLACK))){
                    this.Nblack = this.Nblack + 1;
                }

                if (p.equalsPawn(String.valueOf(State.Pawn.WHITE)) || p.equalsPawn(String.valueOf(State.Pawn.KING))){
                    this.NWhite = this.NWhite + 1;
                }
            }
        }
    }

    public double freeKing(){
        Boolean[] block = {Boolean.FALSE,Boolean.FALSE,Boolean.FALSE,Boolean.FALSE};
        if(this.king[0] > 5 ||  this.king[0] < 3 || this.king[1] > 5 ||  this.king[1] < 3){
            for(int i = 0 ; i < this.king[1]; i++){
                Integer[] coord = {this.king[0], i};
                State.Pawn tile = this.state.getPawn(this.king[0], i);
                if(tile.equalsPawn(String.valueOf(State.Pawn.BLACK)) ||
                        tile.equalsPawn(String.valueOf(State.Pawn.WHITE)) ||
                        this.getCitadels().contains(coord)){

                    block[0] = Boolean.TRUE;
                    break;
                }
            }

            for(int i = this.king[1] + 1 ; i < 9; i++){
                Integer[] coord = {this.king[0], i};
                State.Pawn tile = this.state.getPawn(this.king[0], i);
                if(tile.equalsPawn(String.valueOf(State.Pawn.BLACK)) ||
                        tile.equalsPawn(String.valueOf(State.Pawn.WHITE)) ||
                        this.getCitadels().contains(coord)){

                    block[1] = Boolean.TRUE;
                    break;
                }
            }

            for(int i = 0 ; i < this.king[0]; i++){
                Integer[] coord = {i, this.king[1]};
                State.Pawn tile = this.state.getPawn(this.king[0], i);
                if(tile.equalsPawn(String.valueOf(State.Pawn.BLACK)) ||
                        tile.equalsPawn(String.valueOf(State.Pawn.WHITE)) ||
                        this.getCitadels().contains(coord)){

                    block[2] = Boolean.TRUE;
                    break;
                }
            }

            for(int i = this.king[1] + 1 ; i < 9; i++){
                Integer[] coord = {i, this.king[1]};
                State.Pawn tile = this.state.getPawn(this.king[0], i);
                if(tile.equalsPawn(String.valueOf(State.Pawn.BLACK)) ||
                        tile.equalsPawn(String.valueOf(State.Pawn.WHITE)) ||
                        this.getCitadels().contains(coord)){

                    block[3] = Boolean.TRUE;
                    break;
                }
            }




        }else{
            block[0] = Boolean.TRUE;
            block[1] = Boolean.TRUE;
            block[2] = Boolean.TRUE;
            block[3] = Boolean.TRUE;
        }

        int count = 0;
        for(Boolean f : block){
            if (f.equals(Boolean.FALSE)){
                count += 1;
            }
        }

        return count * 0.25;

    }


    public int ManhatthanDistance(Integer[] p1, Integer[] p2){

        return Math.abs(p1[0] - p2[0]) + Math.abs(p1[1] - p2[1]);

    }

    public List<Integer[]> getWhiteP() {
        return whiteP;
    }

    public double evaluate(){

        return 0;
    }


    public Integer[] getKing() {
        return king;
    }

    public List<Integer[]> getBlackP() {
        return blackP;
    }

    public ArrayList<Integer[]> getCitadels() {
        return citadels;
    }

    public State getState() {
        return state;
    }

    public int getNblack() {
        return Nblack;
    }

    public int getNWhite() {
        return NWhite;
    }
}
