package chess;

import java.util.*;

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

    public Command getCommand() {

	Command whatToDo = new Command();
	if (!hasCommand()) {
	    return whatToDo; // no command to handle
	}

	//we have a command. lets store it
        String totCmd = in.nextLine();
	Scanner cmdScan = new Scanner(totCmd);
	String cmd = cmdScan.next();

	if (cmd.equals("isready")) {
	    System.out.println("readyok");
	    return whatToDo;
	}
	else if (cmd.equals("setoption")) {
	    //do nothing yet;
	    return whatToDo;
	}
	else if (cmd.equals("register")) {
	    //do nothing yet;
	    return whatToDo;
	}
	else if (cmd.equals("ucinewgame")) {
	    whatToDo.topLevelCmd = "newgame";
	    return whatToDo;
	}
	else if (cmd.equals("position")) {
	    cmd = cmdScan.next();
	    if (cmd.equals("startpos")) {
		cmd = cmdScan.next();
		if (cmd.equals("moves")) {
		    while (cmdScan.hasNext()) {
			String nextMove = cmdScan.next();
			whatToDo.gameHistory.push(nextMove);
		    }
		}
	    }
	    whatToDo.topLevelCmd = "position";
	    return whatToDo;
	}
	else if (cmd.equals("go")) {
	    whatToDo.topLevelCmd = "go";
	    return whatToDo;
	}
	else if (cmd.equals("stop")) {
	    whatToDo.topLevelCmd = "stop";
	    return whatToDo;
	}
	else if (cmd.equals("ponderhit")) {
	    // we dont ponder, so do nothing
	    return whatToDo;
	}
	else if (cmd.equals("quit")) {
	    whatToDo.topLevelCmd = "quit";
	    return whatToDo;
	}
	return whatToDo;//in case nothing else worked
	
    }

    public void sendBestMove(String move) {

	System.out.println("bestmove " + move);

    }
}

class Command {

    public String topLevelCmd;
    public Stack<String> gameHistory;

    public Command() {
	topLevelCmd = "null";
	gameHistory = new Stack<String>();
    }
    

}

