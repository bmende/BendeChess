package chess;

import java.util.Scanner;

public class BendeChess {

    public static void main(String[] args) {

	uciCom thing = new uciCom();

	Scanner in = new Scanner(System.in);
	while (true) {
	    if (in.nextLine().equals("isready")) {
		System.out.println("readyok");
	    }
	    if (in.nextLine().contains("go")) {
		thing.sendBestMove("g8f6");
		break;
	    }
	    else
		continue;
	}
    }
}