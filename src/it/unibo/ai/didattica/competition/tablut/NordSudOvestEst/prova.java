package it.unibo.ai.didattica.competition.tablut.NordSudOvestEst;

import it.unibo.ai.didattica.competition.tablut.NordSudOvestEst.GA.game;

import java.io.IOException;

public class prova {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        game.main(args);
        System.out.println(game.getEndGame());
    }
}
