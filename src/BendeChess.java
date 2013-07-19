package chess;

import java.util.Scanner;

public class BendeChess {

    public static void main(String[] args) {

	// engine must build communicator to use gui
	uciCom guiCommune = new uciCom();

	Board b = new Board();

	Scanner in = new Scanner(System.in);
	while (true) {
	    guiCommune.handleCommand();
	}
    }
}