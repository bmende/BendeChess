package chess;

import java.util.*;

public class Board {

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

	pieceList = new Piece[32];
	pieceList[0] =  new Piece("rook",   "white", "a1");
	pieceList[1] =  new Piece("knight", "white", "b1");
	pieceList[2] =  new Piece("bishop", "white", "c1");
	pieceList[3] =  new Piece("queen",  "white", "d1");
	pieceList[4] =  new Piece("king",   "white", "e1");
	pieceList[5] =  new Piece("bishop", "white", "f1");
	pieceList[6] =  new Piece("knight", "white", "g1");
	pieceList[7] =  new Piece("rook",   "white", "h1");
	for (int file = 0; file < 8; file++) {
	    char f = (char)('a' + file);
	    String pos = new String(Character.toString(f));
	    pos += Character.toString('2');
	    pieceList[file+8] = new Piece("pawn", "white", pos);
	}
	for (int file = 0; file < 8; file++) {
	    char f = (char)('a' + file);
	    String pos = new String(Character.toString(f));
	    pos += Character.toString('7');
	    pieceList[file+16] = new Piece("pawn", "black", pos);
	}
	pieceList[24] = new Piece("rook",   "black", "a8");
	pieceList[25] = new Piece("knight", "black", "b8");
	pieceList[26] = new Piece("bishop", "black", "c8");
	pieceList[27] = new Piece("queen",  "black", "d8");
	pieceList[28] = new Piece("king",   "black", "e8");
	pieceList[29] = new Piece("bishop", "black", "f8");
	pieceList[30] = new Piece("knight", "black", "g8");
	pieceList[31] = new Piece("rook",   "black", "h8");

    }

}