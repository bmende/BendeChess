package chess;

public class Piece {

    private String type;
    private String color;
    private String curSquare;

    public Piece (String _type, String _color, String _curSquare) {

	type = _type;
	color = _color;
	curSquare = _curSquare;

    }

    public String getType() { return type; }
    public String getColor() { return color; }
    public String getSquare() { return curSquare; }

    public boolean movePiece(String start, String end) {

	if (!start.equals(curSquare)) {
	    return false;
	}

	curSquare = end;
	return true;

    }

}