package chess;

import java.util.*;


public class MoveGenerator {

    ArrayList<String> posMoves;

    public MoveGenerator() {
	posMoves = new ArrayList<String>();
    }


    public ArrayList<String> nextMoves(Board curBoard) {

	int offset = 0;

	if (curBoard.whiteToMove()) {
	    offset = 0;
	}
	else if (curBoard.blackToMove()) {
	    offset = 16;
	}

	Piece[] pieces = curBoard.getPieceList();

	for (int i = offset; i < offset+16; i++) {

	    Piece next = pieces[i];
	    if (next.isCaptured()) {
		continue;
	    }

	    if (next.equals("pawn")) {

	    }
	    else if (next.equals("rook")) {

	    }
	    else if (next.equals("knight")) {

	    }
	    else if (next.equals("bishop")) {
	    
	    }
	    else if (next.equals("queen")) {

	    }
	    else if (next.equals("king")) {

	    }
	}

	return posMoves;
    }
}