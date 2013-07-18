package chess;

import java.util.Scanner;

public class uciCom {
    //the main job of uciCom is reading
    private Scanner in;

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

    public boolean hasCommand() {
	if (in.hasNext()) {
	    return true;
	}
	else
	    return false;
    }

    public void handleCommand() {

	if (!hasCommand()) {
	    return; // no command to handle
	}

	//we have a command. lets store it
        String cmd = in.nextLine();

	if (cmd.equals("isready")) {
	    System.out.println("readyok");
	    return;
	}
	else if (cmd.contains("setoption name")) {
	    //do nothing yet;
	    return;
	}
	else if (cmd.contains("register")) {
	    //do nothing yet;
	    return;
	}
	else if (cmd.contains("ucinewgame")) {
	    //do nothing yet;
	    return;
	}
	else if (cmd.contains("position")) {
	    //do nothing yet, but soon
	    return;
	}
	else if (cmd.contains("go")) {
	    sendBestMove("g8f6");
	    return;
	}
	else if (cmd.equals("stop")) {
	    sendBestMove("f6g8");
	    return;
	}
	else if (cmd.equals("ponderhit")) {
	    // we dont ponder, so do nothing
	    return;
	}
	else if (cmd.equals("quit")) {
	    // exit the program
	    System.exit(0);
	}
	    
	
	
    }

    public void sendBestMove(String move) {

	System.out.println("bestmove " + move);

    }
}

class Command {

    private int cmd;

}

