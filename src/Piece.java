package chess;

public class Piece {

    private String type;
    private String color;
    private String curSquare;
    private int boardPos;

    public Piece (String _type, String _color, String _curSquare) {

	type = _type;
	color = _color;
	curSquare = _curSquare;

	int file = (curSquare.charAt(0) - 'a');
	int rank = Character.getNumericValue(curSquare.charAt(1)) - 1;

	boardPos = ((8 * rank) + file);

	//	System.out.println(color+" "+type+" "+curSquare+" "+boardPos);

    }

    public String getType() { return type; }
    public String getColor() { return color; }
    public String getSquare() { return curSquare; }
    public int getBoardPos() { return boardPos; }

    public boolean movePiece(String start, String end) {

	if (!start.equals(curSquare)) {
	    return false;
	}

	int file = (end.charAt(0) - 'a');
	int rank = Character.getNumericValue(end.charAt(1)) - 1;

	boardPos = ((8 * rank) + file);

	curSquare = end;
	return true;

    }

    public void capturePiece() {

	curSquare = "captured";
	boardPos = -1;
    }

    public boolean isCaptured() {

	if (boardPos == -1) return true;

	return false;
    }

    

}