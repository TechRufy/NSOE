package it.unibo.ai.didattica.compettion.tablut.NordSudOvestEst;

import java.io.IOException;
import java.net.UnknownHostException;

import it.unibo.ai.didattica.competition.tablut.client.TablutClient;
import it.unibo.ai.didattica.competition.tablut.domain.*;

public class NSOEClient extends TablutClient{

    public NSOEClient(String player, String name, int timeout, String ipAddress) throws UnknownHostException, IOException {
        super(player, name, timeout, ipAddress);
    }
    
    public NSOEClient(String player, int timeout, String ipAddress) throws UnknownHostException, IOException {
        super(player, "NSOE", timeout, ipAddress);
    }
    
    public NSOEClient(String player) throws UnknownHostException, IOException {
        super(player, "NSOE", 60, "localhost");
    }

    public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException {
        String role = "";
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

        NSOEClient client = new NSOEClient(role, name, timeout, ipAddress);
        client.run();
    }
    
	@Override
    public void run() {
		
		System.out.print(
				  "****************************************************************\n"+
				  "*                              :            :                  *\n"							   	
				+ "*                              :            :                  *\n"
				+ "*                              :            :                  *\n"
				+ "*                              :            :                  *\n"
				+ "*                             .'            :                  *\n"
				+ "*                         _.-\"              :                  *\n"
				+ "*                     _.-\"                  '.                 *\n"
				+ "*      ..__...____...-\"                       :                *\n"
				+ "*    : \\_\\                                     :               *\n"
				+ "*    :    .--\"                                  :              *\n"
				+ "*    `.__/  .-\" _                               :              *\n"
				+ "*       /  /  ,\" ,-                             .'             *\n"
				+ "*      (_)(`,(_,'L_,_____       ____....__   _.'               *\n"
				+ "*         \"' \"             \"\"\"\"\"\"\"          \"\"\"                *\n");	
		
		
		
        try {
            declareName();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("You are player " + this.getPlayer().toString() + "!");

    }

}


