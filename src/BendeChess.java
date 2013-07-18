package chess;

import java.util.Scanner;

public class BendeChess {

    public static void main(String[] args) {

	// engine must build communicator to use gui
	uciCom guiCommune = new uciCom();

	Scanner in = new Scanner(System.in);
	while (true) {
	    guiCommune.handleCommand();
	}
    }
}