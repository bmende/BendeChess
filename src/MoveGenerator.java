package chess;

import java.util.*;


public class MoveGenerator {

    ArrayList<Board> posMoves;

    public MoveGenerator() {
	posMoves = new ArrayList<Board>();
    }

    private void reset() {
	posMoves.clear();
    }

    //this function should be the only one called for this class
    public ArrayList<Board> nextMoves(Board _curBoard) {
	reset();

	int offset = 0;

	if (_curBoard.whiteToMove()) {
	    offset = 0;
	}
	else if (_curBoard.blackToMove()) {
	    offset = 16;
	}

	Piece[] pieces = _curBoard.getPieceList();

	for (int i = offset; i < offset+16; i++) {

	    Piece next = pieces[i];
	    if (next.isCaptured()) {
		continue;
	    }

	    if (next.getType().equals("pawn")) {
		pawnMoves(next, _curBoard);
	    }
	    else if (next.getType().equals("rook")) {

	    }
	    else if (next.getType().equals("knight")) {

	    }
	    else if (next.getType().equals("bishop")) {
	    
	    }
	    else if (next.getType().equals("queen")) {

	    }
	    else if (next.getType().equals("king")) {

	    }
	}

	return posMoves;
    }

    /* Pawns go forward once, unless the way is blocked
     * or it is their first move and they go forward twice.
     * They can only capture diagonally.
     */
    public void pawnMoves(Piece pawn, Board _curBoard) {
	int nextRank = 1;
	if (_curBoard.blackToMove()) {
	    nextRank = -1;
	}
	//where is the pawn
	String start = pawn.getSquare();
	int file = start.charAt(0) - 'a';
	int rank = Character.getNumericValue(start.charAt(1)) - 1;

	int oneUp = rank+nextRank;
	int fileLeft = file - 1, fileRight = file + 1;

	//check for captures
	if (fileLeft >= 0) {
	    if (_curBoard.getPieceAt(fileLeft, oneUp).getColor().
		equals(pawn.getColor()) ||
		_curBoard.getPieceAt(fileLeft, oneUp).getType().
		equals("null")) {
		fileLeft = -1;
	    }
	}
	if (fileRight < Board.NUM_FILES) {
	    if (_curBoard.getPieceAt(fileRight, oneUp).getColor().
		equals(pawn.getColor()) ||
		_curBoard.getPieceAt(fileRight, oneUp).getType().
		equals("null")) {
		fileRight = -1;
	    }
	}
	if (fileLeft != -1) {
	    Board temp = new Board(_curBoard);
	    String end = temp.getPieceAt(fileLeft, oneUp).
		getSquare();
	    temp.executeMove(start+end);
	    posMoves.add(temp);
	}
	if (fileRight != -1) {
	    Board temp = new Board(_curBoard);
	    String end = temp.getPieceAt(fileRight, oneUp).
		getSquare();
	    temp.executeMove(start+end);
	    posMoves.add(temp);
	}


	//check moves forward;
	if (_curBoard.getPieceAt(file, oneUp).getType().
	    equals("null")) {
	    Board temp = new Board(_curBoard);
	    String end = temp.getPieceAt(file, oneUp).getSquare();
	    temp.executeMove(start + end);
	    posMoves.add(temp);
	}

	//check if two moves forward are allowed
	if (((_curBoard.blackToMove() && 
	      pawn.getColor().equals("black")) && rank == 7) ||
	    ((_curBoard.whiteToMove() &&
	      pawn.getColor().equals("white")) && rank == 2)) {

	    int twoUp = oneUp+nextRank;
	    Board temp = new Board(_curBoard);
	    String end = temp.getPieceAt(file, twoUp).getSquare();
	    temp.executeMove(start+end);
	    posMoves.add(temp);
	}


	
    }
}