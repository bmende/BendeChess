package chess;

import java.util.Scanner;

public class BendeChess {

    public static void main(String[] args) {

	// engine must build communicator to use gui
	uciCom guiCommune = new uciCom();

	Board b = new Board();
	b.printBoard();
	b.executeMove("e2e4");
	b.printBoard();
	b.executeMove("d7d5");
	b.printBoard();
	b.executeMove("e4d5");
	b.printBoard();

	b.printPieceList();
	b.printHistory();

	Scanner in = new Scanner(System.in);
	while (true) {
	    guiCommune.handleCommand();
	}
    }
}