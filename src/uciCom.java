package chess;

import java.util.Scanner;

public class uciCom {

    public static void main(String[] args) {
	Scanner in = new Scanner(System.in);
	if (in.nextLine().equals("uci")) {
	    System.out.println("id name BendeChess");
	    System.out.println("id author Benjamin M.");
	    System.out.println("uciok");
	}

	while (!in.nextLine().equals("quit"));
    }

}

	    