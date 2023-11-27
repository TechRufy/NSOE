package it.unibo.ai.didattica.competition.tablut.NordSudOvestEst.GA;

import it.unibo.ai.didattica.competition.tablut.NordSudOvestEst.Client.NSOEClient;
import it.unibo.ai.didattica.competition.tablut.server.Server;

import java.io.IOException;

public class game{

    private static int EndGame;

    public static class ServerR extends Thread{


        @Override
        public void run() {
            Server.main(new String[]{"-g","--time 60"});
        }


    }

    public static class PlayerW extends Thread{

        @Override
        public void run() {
            try {
                NSOEClient.main(new String[]{"WHITE"});
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static class PlayerB extends Thread{

        @Override
        public void run() {
            try {
                NSOEClient.main(new String[]{"BLACK"});
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        ServerR s = new ServerR();
        PlayerW w = new PlayerW();
        PlayerB b = new PlayerB();

        s.start();
        Thread.sleep(1000);
        w.start();
        Thread.sleep(1000);
        b.start();

        s.join();
        w.join();
        b.join();

        System.out.println("ciao");
        System.out.println(Server.getEndgame());

        EndGame = Server.getEndgame();

    }

    public static int getEndGame() {
        return EndGame;
    }
}
