package chess;

import java.util.Scanner;

public class uciCom {
    //the main job of uciCom is reading
    Scanner in;

    //during construction of communications,
    //we will perform the initial uci chatter
    public uciCom() {

	in = new Scanner(System.in);

	if (in.nextLine().equals("uci")) {
	    System.out.println("id name BendeChess");
	    System.out.println("id author Benjamin M.");
	    System.out.println("uciok");
	}
	else {
	    System.out.println("we want only uci");
	    System.exit(-1); //we want uci only
	}
	
    }
}

	    