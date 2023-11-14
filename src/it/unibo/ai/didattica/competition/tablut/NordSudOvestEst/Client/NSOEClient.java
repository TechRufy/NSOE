package it.unibo.ai.didattica.competition.tablut.NordSudOvestEst.Client;

import java.io.IOException;
import java.net.UnknownHostException;

import it.unibo.ai.didattica.competition.tablut.NordSudOvestEst.Domain.NSOEgame;
import it.unibo.ai.didattica.competition.tablut.NordSudOvestEst.NSOEPlayer;
import it.unibo.ai.didattica.competition.tablut.client.TablutClient;
import it.unibo.ai.didattica.competition.tablut.domain.*;

public class NSOEClient extends TablutClient{

    private int game;
    private NSOEPlayer giocatore;

    public NSOEClient(String player, String name, int timeout, String ipAddress,int gameChosen) throws UnknownHostException, IOException {
        super(player, name, timeout, ipAddress);
        game = gameChosen;
    }
    public NSOEClient(String player, String name, int timeout, String ipAddress) throws UnknownHostException, IOException {
        this(player, name, timeout, ipAddress,4);
    }

    public NSOEClient(String player, int timeout, String ipAddress) throws UnknownHostException, IOException {
        this(player, "random", timeout, ipAddress,4);
    }

    public NSOEClient(String player) throws UnknownHostException, IOException {
        this(player, "random", 60, "localhost",4);
    }


    public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException {
        int gametype = 4;
        String role = "WHITE";
        String name = "NSOE";
        String ipAddress = "localhost";
        int timeout = 60;

        if (args.length < 1) {
            System.out.println("You must specify which player you are (WHITE or BLACK)");
            System.exit(-1);
        } else {
            System.out.println(args[0]);
            role = (args[0]);
            role = role.toUpperCase();
        }
        if (args.length >= 2) {
            System.out.println(args[1]);
            timeout = Integer.parseInt(args[1]);
        }
        if (args.length >= 3) {
            System.out.println(args[2]);
            ipAddress = args[2];
        }
        System.out.println("Selected client: " + args[0]);

        NSOEClient client = new NSOEClient(role, name, timeout, ipAddress,gametype);
        client.run();
    }


    @Override
    public void run() {


        try {
            this.declareName();
        } catch (Exception e) {
            e.printStackTrace();
        }

        State state = null;

        aima.core.search.adversarial.Game<State,Action, State.Turn> rules=null;
        if (this.game == 4) {
            state = new StateTablut();
            state.setTurn(State.Turn.WHITE);
            rules = new NSOEgame(99, 0, "garbage", "fake", "fake");
            System.out.println("Ashton Tablut game");
        } else {
            System.out.println("Error in game selection");
            System.exit(4);
        }

        giocatore =new NSOEPlayer(rules, Double.MIN_VALUE,Double.MAX_VALUE,(this.getTimeout()-2)/2);

        state.setTurn(State.Turn.WHITE);

        while(true) {
            try {
                this.read();
            } catch (ClassNotFoundException | IOException e1) {
                e1.printStackTrace();
                System.exit(1);
            }

            // print current state
            state = this.getCurrentState();
            System.out.println("Current state:");
            System.out.println(state.toString());

            // if WHITE
            if (this.getCurrentState().getTurn().equals(StateTablut.Turn.WHITE)) {
                if (this.getPlayer().equals(State.Turn.WHITE)) {
                    // if is my turn (WHITE)
                    // search the best move in search tree
                    Action a = giocatore.makeDecision(state);
                    System.out.println(a);
                    try {
                        this.write(a);
                    } catch (ClassNotFoundException | IOException e) {
                        e.printStackTrace();
                    }
                }

                // if is turn of oppenent (BLACK)
                else {
                    System.out.println("Waiting for your opponent move...\n");
                }
            }
            // if BLACK
            else if (this.getCurrentState().getTurn().equals(StateTablut.Turn.BLACK)) {
                // if is my turn (BLACK)
                if (this.getPlayer().equals(State.Turn.BLACK)) {
                    // search the best move in search tree
                    Action a = giocatore.makeDecision(state);
                    try {
                        this.write(a);
                    } catch (ClassNotFoundException | IOException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    System.out.println("Waiting for your opponent move...\n");
                }
            }

            // if WHITEWIN
            else if (state.getTurn().equals(StateTablut.Turn.WHITEWIN)) {
                System.out.println(this.getPlayer().equals(State.Turn.WHITE)?"YOU WON!":"YOU LOSE!");
                System.exit(0);
            }

            // if BLACKWIN
            else if (state.getTurn().equals(StateTablut.Turn.BLACKWIN)) {
                System.out.println(this.getPlayer().equals(State.Turn.BLACK)?"YOU WON!":"YOU LOSE!");
                System.exit(0);
            }

            // if DRAW
            else if (state.getTurn().equals(StateTablut.Turn.DRAW)) {
                System.out.println("DRAW!");
                System.exit(0);
            }

        }
    }




    }



