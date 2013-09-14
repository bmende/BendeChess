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
    private String colorInCheck;

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
	colorInCheck = "null";

	history = new Stack<String>();
    }

    //copy constructor
    public Board(Board copy) {
	this.pieceList = new Piece[NUM_PIECES];
	for (int i = 0; i < NUM_PIECES; i++) {
	    this.pieceList[i] = new Piece(copy.pieceList[i]);
	}
	this.board = new Piece[NUM_SQUARES];
	for (int i = 0; i < NUM_SQUARES; i++) {
	    this.board[i] = new Piece(copy.board[i]);
	}
	this.colorToMove = new String(copy.colorToMove);
	this.history = new Stack<String>();
	this.history.addAll(copy.history);
    }

    //if you don't want to make a new board object
    public void copy(Board copy) {
	this.pieceList = new Piece[NUM_PIECES];
	for (int i = 0; i < NUM_PIECES; i++) {
	    this.pieceList[i] = new Piece(copy.pieceList[i]);
	}
	this.board = new Piece[NUM_SQUARES];
	for (int i = 0; i < NUM_SQUARES; i++) {
	    this.board[i] = new Piece(copy.board[i]);
	}
	this.colorToMove = new String(copy.colorToMove);
	this.history = new Stack<String>();
	this.history.addAll(copy.history);
    }

    /*
     * This function will check for move partial legality. 
     * Some illegal moves will return false, to simplify
     * move generation. False guarentees an illegal move,
     * while a return value of true does not guarentee that
     * the move is legal. Checks involve leaving the board,
     * capturing a teammate. Returns false if colorToMove
     * is in check after move is completed.
     */
    public boolean executeMove(String move) {

	history.push(move);

	String start = move.substring(0, 2);
	String end = move.substring(2);

	int s_file = (start.charAt(0) - 'a');
	int s_rank = Character.getNumericValue(start.charAt(1)) - 1;
	int startPos = (NUM_RANKS * s_rank) + s_file;

	int e_file = (end.charAt(0) - 'a');
	int e_rank = Character.getNumericValue(end.charAt(1)) - 1;
	int endPos = (NUM_RANKS * e_rank) + e_file;

	//before we change the board state, check if the 
	//proposed move is legal
	if (!moveIsLegal(s_file, s_rank, e_file, e_rank)) {
	    System.out.println("move is illegal");
	    return false;
	}

	// deal with captures in the pieceList
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

	return true; //can only get here if move is legal;
    }

    /*
     * First check to make sure squares in question actually exit.
     * Next we check to make sure that if there is a capture, it
     * is a different colored piece.
     */
    public boolean moveIsLegal(int s_file, int s_rank,
			       int e_file, int e_rank) {

	if (s_file < 0 || s_file >= NUM_FILES ||
	    s_rank < 0 || s_rank >= NUM_RANKS ||
	    e_file < 0 || e_file >= NUM_FILES ||
	    e_rank < 0 || e_rank >= NUM_RANKS) 
	    {
		return false;
	    }
	int startPos = (NUM_RANKS*s_rank) + s_file;
	int endPos = (NUM_RANKS*e_rank) + e_file;

	if (board[endPos].getColor().equals(colorToMove)) {
	    // System.out.println("Cant capture own piece!!!");
	    return false;
	}

	//now to see how this moves affect inCheck status
	if (colorToMoveInCheck(startPos, endPos)) {
	    //  System.out.println("leaves you in check");
	    return false;
	}
	
	
	return true;
    }

    // if the side to move is in check at the end of move,
    // then the move is illegal. Pass in the move that we
    // want to check

    //////////////// NOT YET CHECKING FOR CHECK FROM KNIGHTS ///////////////////
    public boolean colorToMoveInCheck(int startPos, int endPos) {
	Piece king;
	Piece[] tempList = new Piece[NUM_PIECES];
	Piece[] tempBoard = new Piece[NUM_SQUARES];
	int k_rank, k_file, offset;

	//copy the board and make the move on the copy.
	for (int i = 0; i < NUM_PIECES; i++) {
	    tempList[i] = new Piece(pieceList[i]);
	    String start = board[startPos].getSquare();
	    String end = board[endPos].getSquare();
	    if (tempList[i].getSquare().equals(end)) {
		tempList[i].capturePiece();
	    }
	    // piece only moves if this start == curSquare
	    tempList[i].movePiece(start, end);
	}

	for (int i = 0; i < NUM_SQUARES; i++) {
	    tempBoard[i] = new Piece(board[i]);
	}

	tempBoard[endPos].movePiece(tempBoard[startPos]);
	tempBoard[startPos].nullify();
	

	if (whiteToMove()) {
	    king = tempList[4];
	    // we want to examine the position of the black pieces
	    offset = 16;
	} else {// blackToMove()
	    king = tempList[20];
	    // examine the position of the white pieces
	    offset = 0;
	}
	String k_pos = king.getSquare();
	k_file = k_pos.charAt(0) - 'a';
	k_rank = Character.getNumericValue(k_pos.charAt(1)) - 1;

	// check for every piece and see if it puts king in check
	for (int i = offset; i < offset+16; i++) {

	    String pos = tempList[i].getSquare();
	    int file = pos.charAt(0) - 'a';
	    int rank = Character.getNumericValue(pos.charAt(1)) - 1;

	    /************** Pawns ************************/
	    if (tempList[i].getType().equals("pawn")) {
		if (blackToMove()) {
		    if (k_rank - rank == 1) {
			if (Math.abs(k_file - file) == 1) {
			    System.out.print("pawn ");
			    return true;
			}
		    }
		}
		else if (whiteToMove()) {
		    if (rank - k_rank == 1) {
			if (Math.abs(k_file - file) == 1) {
			    System.out.print("pawn ");
			    return true;
			}
		    }
		}
	    } // if pawn //////////////////////////////////

	    /***************** Rooks *******************/
	    /************* Partial Queens ***************/
	    else if (tempList[i].getType().equals("rook") ||
		     tempList[i].getType().equals("queen")) {
		if ((Math.abs(rank - k_rank) == 1 && file == k_file)
		    || (Math.abs(file - k_file) == 1 && rank == k_rank)) {
		    //rook/queen is right next to the king, so check
		    return true;
		}
		int incr; boolean blocked = false;
		// look to see if another piece blocks the rook
		if (rank == k_rank) {
		    incr = (file > k_file) ? -1 : 1;
		    for (int j = file; j != k_file; j += incr) {
			int square = (NUM_RANKS * rank) + j;
			if (!tempBoard[square].isNull()) {
			    blocked = true; //cant get king, notCheck
			}
		    }
		    if (!blocked) { // can get the king with the rook/queen
			System.out.print("rook not blocked ");
			return true;
		    }
		} // rook/queen and king on same rank
		else if (file == k_file) {
		    incr = (rank > k_rank) ? -1 : 1;
		    for (int j = rank; j != k_rank; j += incr) {
			int square = (NUM_RANKS * j) + file;
			if (!tempBoard[square].isNull()) {
			    blocked = true;
			}
		    }
		    if (!blocked) { // can get king with rook/queen
			return true;
		    }
		} // rook/queen and king on same file
	    } // if rook || queen ////////////////////////////////

	    /********** Bishops **********************/
	    /******* Partial Queen ******************/
	    else if (tempList[i].getType().equals("bishop") ||
		     tempList[i].getType().equals("queen")) {
	    	boolean blocked = false;
	    	if (Math.abs(rank - k_rank) != Math.abs(file - k_file)) {
	    	    blocked = true;
	    	}
		if (Math.abs(rank - k_rank) == 1 &&
		    Math.abs(file - k_file) == 1) {
		    return true; // bishop right next to king
		}
		int f_inc, r_inc, r, f;
		f_inc = (file < k_file) ? 1 : -1;
		if (file == k_rank) f_inc = 0;
		r_inc = (rank < k_rank) ? 1 : -1;
		if (rank == k_rank) r_inc = 0;
		for (f = file, r = rank; (f != k_file) && (r != k_rank);
		     f+=f_inc, r+=r_inc)
		    {
			int square = (NUM_RANKS * r) + f;
			if (!tempBoard[square].isNull()) {
			    blocked = true;
		    
			}
		    }
	    	if (!blocked) { // can get the king with the bishop/queen!!
	    	    return true;
	    	}
	    } // if bishop || queen ////////////////////////////
	    
	    /**************** King *****************************/
	    else if (tempList[i].getType().equals("king")) {
		if (Math.abs(rank - k_rank) <= 1 &&
		    Math.abs(file - k_file) <= 1) {
		    return true;
		}
	    }
	}
	

	return false; // to get here, cant have been in check
    }

    public boolean whiteToMove() {
	return colorToMove.equals("white");
    }

    public boolean blackToMove() {
	return colorToMove.equals("black");
    }

    //The following was initially designed for debugging
    //but I have also realized it can facilitate command
    //line play.
    public void printBoard() {
	printBoard(board);
    }
    public void printBoard(Piece[] _board) {

	if (history.size() > 0) {
	    System.out.println(history.peek());
	}
	for (int rank = NUM_RANKS - 1; rank >= 0; rank--) {
	    for (int file = 0; file < NUM_FILES; file++) {
		int pos = ((8 * rank) + file);
		Piece cur = _board[pos];
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

    public Piece[] getPieceList() {
	return pieceList;
    }

    public Piece getPieceAt(String square) {
	int file = square.charAt(0) - 'a';
	int rank = Character.getNumericValue(square.charAt(1)) - 1;

	return getPieceAt(file, rank);
    }

    public Piece getPieceAt(int file, int rank) {
	return board[(8*rank)+file];
    }
    public Piece getPieceAt(int square) {
	return board[square];
    }

    public String getLastMove() {
	return history.peek();
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