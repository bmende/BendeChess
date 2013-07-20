package chess;

import java.util.*;

public class Board {

    public static final int NUM_PIECES = 32;
    public static final int NUM_SQUARES = 64;
    public static final int NUM_RANKS = 8;
    public static final int NUM_FILES = 8;

    /* Two different board representations: the first
     * is piece-centric, ie the piecelist. The other is
     * the board. The board and piece list should obviously
     * always match. Board will be rank-major order,
     * ie. board = { a1, b1, c1, d1, e1, f1, g1, h1,
     *               a2, b2, c2, d2, e2, f2, g2, h2,
     *		     a3, b3, c3, d3, e3, f3, g3, h3,
     *		     a4, b4, c4, d4, e4, f4, g4, h4,
     *		     a5, b5, c5, d5, e5, f5, g5, h5,
     *		     a6, b6, c6, d6, e6, f6, g6, h6,
     *		     a7, b7, c7, d7, e7, f7, g7, h7,
     *		     a8, b8, c8, d8, e8, f8, g8, h8 }
     * except that of course that board is stacked upside down.
     * So e2e4 is a move from board[12] to board[28];
     */

    private Piece pieceList[];
    private Piece board[];

    private String colorToMove;

    private Stack<String> history;

    //initialize a new board.
    public Board() {

	//begin by initializing piece list.

	pieceList = new Piece[NUM_PIECES];
	pieceList[0] =  new Piece("rook",   "white", "a1");
	pieceList[1] =  new Piece("knight", "white", "b1");
	pieceList[2] =  new Piece("bishop", "white", "c1");
	pieceList[3] =  new Piece("queen",  "white", "d1");
	pieceList[4] =  new Piece("king",   "white", "e1");
	pieceList[5] =  new Piece("bishop", "white", "f1");
	pieceList[6] =  new Piece("knight", "white", "g1");
	pieceList[7] =  new Piece("rook",   "white", "h1");
	for (int file = 0; file < NUM_FILES; file++) {
	    char f = (char)('a' + file);
	    String pos = new String(Character.toString(f));
	    pos += Character.toString('2');
	    pieceList[file+8] = new Piece("pawn", "white", pos);
	}
	pieceList[16] = new Piece("rook",   "black", "a8");
	pieceList[17] = new Piece("knight", "black", "b8");
	pieceList[18] = new Piece("bishop", "black", "c8");
	pieceList[19] = new Piece("queen",  "black", "d8");
	pieceList[20] = new Piece("king",   "black", "e8");
	pieceList[21] = new Piece("bishop", "black", "f8");
	pieceList[22] = new Piece("knight", "black", "g8");
	pieceList[23] = new Piece("rook",   "black", "h8");
	for (int file = 0; file < NUM_FILES; file++) {
	    char f = (char)('a' + file);
	    String pos = new String(Character.toString(f));
	    pos += Character.toString('7');
	    pieceList[file+24] = new Piece("pawn", "black", pos);
	}

	//now initialize board-centric representation
	board = new Piece[NUM_SQUARES];

	for (int piece = 0; piece < NUM_PIECES; piece++) {
	    int pos = pieceList[piece].getBoardPos();
	    String type = pieceList[piece].getType();
	    String color = pieceList[piece].getColor();
	    String curSquare = pieceList[piece].getSquare();
	    board[pos] = new Piece(type, color, curSquare);
	}
	for (int piece = NUM_PIECES/2;
	     piece < (NUM_SQUARES - NUM_PIECES/2); piece++) {
	    char file = (char)((piece % NUM_FILES) + 'a');
	    String pos = new String(Character.toString(file));
	    int rank = (piece - (piece % NUM_FILES)) / NUM_RANKS;
	    rank++; //since in human notation ranks are 1-indexed
	    pos += Integer.toString(rank);
	    board[piece] = new Piece("null", "null", pos);
	}

	colorToMove = "white";

	history = new Stack<String>();
    }


    public void executeMove(String move) {
	String start = move.substring(0, 2);
	String end = move.substring(2);

	int file = (start.charAt(0) - 'a');
	int rank = Character.getNumericValue(start.charAt(1)) - 1;

	int startPos = (8 * rank) + file;
	if (startPos < 0 || startPos >= NUM_SQUARES) {
	    //not a square
	    System.out.println(start + " is NOT a square!!");
	    System.exit(0);
	}

	file = (end.charAt(0) - 'a');
	rank = Character.getNumericValue(end.charAt(1)) - 1;

	int endPos = (8 * rank) + file;
	if (endPos < 0 || endPos >= NUM_SQUARES) {
	    //not a square
	    System.out.println(end + " is NOT a square!!!");
	    System.exit(0);
	}

	// check to make sure move isn't illegal
	if (board[endPos].getColor().equals(
			     board[startPos].getColor())) {
	    //this signifies an illegal move
	    System.out.println("Cant capture own piece!!!");
	    System.exit(0);
	}
	if (!board[endPos].equals("null")) {
	    for (int i = 0; i < NUM_PIECES; i++) {
		if (pieceList[i].getSquare().equals(end)) {
		    pieceList[i].capturePiece();
		    break;
		}
	    }
	}
	
	for (int i = 0; i < NUM_PIECES; i++) {
	    if (pieceList[i].movePiece(start, end)) {;
		break;
		// this will work since movePiece only returns true
		// if start == curSquare
	    }
	}

	board[endPos].movePiece(board[startPos]);
	board[startPos].nullify();

	// remember to change colorToMove
	if (colorToMove.equals("white")) 
	    colorToMove = "black";
	else if (colorToMove.equals("black"))
	    colorToMove = "white";

	history.push(move);
    }


    //The following was initially designed for debugging
    //but I have also realized it can facilitate command
    //line play.
    public void printBoard() {

	for (int rank = NUM_RANKS - 1; rank >= 0; rank--) {
	    for (int file = 0; file < NUM_FILES; file++) {
		int pos = ((8 * rank) + file);
		Piece cur = board[pos];
		if (cur.getType().equals("null")) {
		    System.out.print("--");
		}
		if (cur.getColor().equals("white")) {
		    if (cur.getType().equals("pawn")) {
			System.out.print("wp");
		    }
		    if (cur.getType().equals("rook")) {
			System.out.print("wr");
		    }
		    if (cur.getType().equals("knight")) {
			System.out.print("wn");
		    }
		    if (cur.getType().equals("bishop")) {
			System.out.print("wb");
		    }
		    if (cur.getType().equals("queen")) {
			System.out.print("wq");
		    }
		    if (cur.getType().equals("king")) {
			System.out.print("wk");
		    }
		}
		else if (cur.getColor().equals("black")) {
		    if (cur.getType().equals("pawn")) {
			System.out.print("bp");
		    }
		    if (cur.getType().equals("rook")) {
			System.out.print("br");
		    }
		    if (cur.getType().equals("knight")) {
			System.out.print("bn");
		    }
		    if (cur.getType().equals("bishop")) {
			System.out.print("bb");
		    }
		    if (cur.getType().equals("queen")) {
			System.out.print("bq");
		    }
		    if (cur.getType().equals("king")) {
			System.out.print("bk");
		    }
		}
		System.out.print(" ");
	    } // file loop
	    System.out.println("");
	} // rank loop

	System.out.println(colorToMove + " to move.\n");
    }

    public void printPieceList() {
	for (int i = 0; i < NUM_PIECES; i++) {
	    pieceList[i].printPiece();
	}
    }

    public void printHistory() {
	System.out.println(history);
    }


}