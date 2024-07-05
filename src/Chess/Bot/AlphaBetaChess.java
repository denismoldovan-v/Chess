package Chess.Bot;

import Chess.Game.Board;
import Chess.Game.Main;
import Chess.Game.Move;
import Chess.Game.Square;
import Chess.Pieces.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Klasa implementująca bota, z którym można zagrać w grę, jeśli nie masz przyjaciół, z którymi mógłbyś to zrobić.
 * @author Logic Crazy Chess
 */
public class AlphaBetaChess implements Serializable {
    static String[][] chessBoard = new String[8][8];
    static int kingPositionC;
    static int kingPositionL;
    public int globalDepth=4;
    private static boolean isBlack = false;

    //////////////////////////////////////
    /*Początek sekcji nie od autora bota*/
    //////////////////////////////////////

    /**
     * Konstruktor bota.
     * @param globalDepth Głębokość przeszukiwania bota. (3 - płytka ("łatwy bot"); 4 - średnia ("średni bot"); 5 - głęboka ("zaawansowany bot"); im większa głębokość, tym dłuższy czas przeszukiwania (rozważania ruchów przez bota))
     * @param initBoard Plansza, od której zaczyna się rozgrywka.
     */
    public AlphaBetaChess(Integer globalDepth, Board initBoard){
        mapBoard(initBoard);
        while (!"A".equals(chessBoard[kingPositionC/8][kingPositionC%8])) {kingPositionC++;}//get King's location
        while (!"a".equals(chessBoard[kingPositionL/8][kingPositionL%8])) {kingPositionL++;}//get king's location
        this.globalDepth = globalDepth;
    }

    /**
     * Ustawienie bota na grającego białymi.
     */
    public void setAsWhite(){
        isBlack = false;
    }

    /**
     * Ustawienie bota na grającego czarnymi.
     */
    public void setAsBlack(){
        isBlack = true;
    }

    /**
     * Sprawdzenie czy bot gra białymi.
     * @return False - bot gra białymi.
     */
    public boolean isBlack() {
        return isBlack;
    }

    /**
     * Prywatna metoda obracająca planszę, w przypadku gdy bot gra białymi.
     */
    private void boardRotate(){
        String[][] tempmap = new String[8][8];
        for (int row = 0; row < chessBoard.length; row++) {
            for (int col = 0; col < chessBoard.length; col++) {
                if(Character.isUpperCase(chessBoard[row][col].charAt(0))) tempmap[7-row][7-col] = chessBoard[row][col].toLowerCase();
                else tempmap[7-row][7-col] = chessBoard[row][col].toUpperCase();
            }
        }
        chessBoard = tempmap;
    }

    /**
     * Mapowanie obecnej planszy na planszę rozumianą przez bota.
     * @param board Obecna plansza.
     */
    private void mapBoard(Board board){
        enum Pieces{
            King,
            Queen,
            Bishop,
            Knight,
            Pawn,
            Rook
        }
        for (int row = 0; row < board.BOARDSIZE; row++) {
            for (int col = 0; col < board.BOARDSIZE; col++) {
                Piece piece = board.getSquare(row,col).getOccupyingPiece();
                if(piece == null) {
                    chessBoard[row][col] = " ";
                    continue;
                }
                Pieces pieceEnum = Pieces.valueOf(piece.getClass().getSimpleName());

                //Bot się rusza pionami z wielkiej litery, więc trzeba zamienić wielkość liter w przypadku gry czarnymi.
                switch (pieceEnum){
                    case King -> {
                        if (piece.isWhite()) chessBoard[row][col] = "A";
                        else chessBoard[row][col] = "a";
                    }
                    case Queen -> {
                        if (piece.isWhite()) chessBoard[row][col] = "Q";
                        else chessBoard[row][col] = "q";
                    }
                    case Rook -> {
                        if (piece.isWhite()) chessBoard[row][col] = "R";
                        else chessBoard[row][col] = "r";
                    }
                    case Pawn -> {
                        if (piece.isWhite()) chessBoard[row][col] = "P";
                        else chessBoard[row][col] = "p";
                    }
                    case Bishop -> {
                        if (piece.isWhite()) chessBoard[row][col] = "B";
                        else chessBoard[row][col] = "b";
                    }
                    case Knight -> {
                        if (piece.isWhite()) chessBoard[row][col] = "K";
                        else chessBoard[row][col] = "k";
                    }
                }
            }
        }
        if(isBlack) boardRotate();
    }


    /**
     * Metoda wywołująca ruch bota.
     * @param board Obecna plansza.
     * @return Ruch bota.
     */
    public Move AImove(Board board){
        //Mapowanie klasy Board na tablicę wydzianą przez bota
        mapBoard(board);

        //Propozycja ruchu
        String move = alphaBeta(globalDepth, 1000000, -1000000, "", 0);

        //Określenie współrzędnych
        int rowFrom = ((int) move.charAt(0))-48;
        int colFrom = ((int) move.charAt(1))-48;
        int rowTo = ((int) move.charAt(2))-48;
        int colTo = ((int) move.charAt(3))-48;

        //Dostosowanie współrzędnych
        if (isBlack){
            rowFrom = 7-rowFrom;
            rowTo = 7-rowTo;
            colFrom = 7-colFrom;
            colTo = 7-colTo;
        }

        try{
            board.getSquare(rowFrom, colFrom);
            board.getSquare(rowTo, colTo);
        } catch (IllegalArgumentException e){
            throw new RuntimeException("Botowi znudziła się gra/przegrał");
        }

        //Wzięcie pierwszego poprawnego ruchu (w przypadku ruchu, który spowodowałby szach na swoich)
        if(ProperMovesHandler.isCheck(move)) {
            move = ProperMovesHandler.getFirstProperMove();

            //Określenie współrzędnych nowego ruchu
            rowFrom = ((int) move.charAt(0)) - 48;
            colFrom = ((int) move.charAt(1)) - 48;
            rowTo = ((int) move.charAt(2)) - 48;
            colTo = ((int) move.charAt(3)) - 48;

            //Dostosowanie współrzędnych nowego ruchu
            if (isBlack) {
                rowFrom = 7 - rowFrom;
                rowTo = 7 - rowTo;
                colFrom = 7 - colFrom;
                colTo = 7 - colTo;
            }
        }

        //Czyszczenie
        ProperMovesHandler.moves.clear();
        return new Move(board.getSquare(rowFrom,colFrom), board.getSquare(rowTo,colTo));
    }

    /**
     * Klasa ukierunkowująca bota na poprawne działanie. (tak, poprawiłem autora ¯\_(ツ)_/¯ ja jestem Polak wolny, aurea libertas mea)
     */
    private static class ProperMovesHandler{
        static ArrayList<String> moves = new ArrayList<>();

        /**
         * Metoda zwracająca pierwszy (byle jaki, byle poprawny) ruch.
         * @return Ruch w formie ciągu znaków.
         */
        static String getFirstProperMove(){
            for (String move : moves) {
                if (!isCheck(move)) {
                    return move;
                }
            }
            return "-1";
        }

        /**
         * Metoda określająca, czy podany ruch spowoduje szach na własnym królu.
         * @param move Ruch.
         * @return Fałsz - poprawny ruch, niepowodujący szacha na własnym królu.
         */
        static boolean isCheck(String move){
            //Pobranie planszy
            Board board = Main.getCurrentGame().getBoard();

            //Zmapowanie współrzędnych
            int rowFrom = ((int) move.charAt(0))-48;
            int colFrom = ((int) move.charAt(1))-48;
            int rowTo = ((int) move.charAt(2))-48;
            int colTo = ((int) move.charAt(3))-48;
            if (isBlack){
                rowFrom = 7-rowFrom;
                rowTo = 7-rowTo;
                colFrom = 7-colFrom;
                colTo = 7-colTo;
            }

            //Warunki niepoprawności ruchu
            if(board.getSquare(rowFrom, colFrom).getOccupyingPiece() != null && board.getSquare(rowFrom, colFrom).getOccupyingPiece().isWhite() == isBlack) return true;
            if(board.getSquare(rowFrom, colFrom).getOccupyingPiece() == null) return true;
            if(board.getSquare(rowTo, colTo).getOccupyingPiece() != null && board.getSquare(rowTo, colTo).getOccupyingPiece().isWhite() == !isBlack) return true;
            if(board.getSquare(rowFrom, colFrom).getOccupyingPiece() != null && !board.getSquare(rowFrom, colFrom).getOccupyingPiece().getLegalMoves(board.getSquare(rowFrom, colFrom)).contains(board.getSquare(rowTo, colTo))) return true;

            boolean returning = false;

            //Wprowadzenie ruchu w życie
            Piece movPiece = board.getSquare(rowFrom, colFrom).getOccupyingPiece();
            Piece secPiece = board.getSquare(rowTo, colTo).getOccupyingPiece();

            board.getSquare(rowFrom, colFrom).removeOccupyingPiece();
            board.getSquare(rowTo, colTo).removeOccupyingPiece();

            board.getSquare(rowTo, colTo).putPiece(movPiece);

            movPiece.setOccupyingSquare(board.getSquare(rowTo, colTo));
            if (secPiece != null){
                board.removePiece(secPiece);
                secPiece.setOccupyingSquare(null);
            }

            //Sprawdzenie szacha
            King whiteKong = board.getKing(board.getWhitePieces());
            King blackKong = board.getKing(board.getBlackPieces());

            // Sprawdzenie, czy gracz biały jest szachowany
            for(Piece piece : board.getBlackPieces()){
                for(Square square : piece.getLegalMoves(piece.getOccupyingSquare())){
                    if (whiteKong.getOccupyingSquare() == square) {
                        if(!isBlack) returning = true;
                        break;
                    }
                }
                if(returning) break;
            }
            // Sprawdzenie, czy gracz czarny jest szachowany
            for(Piece piece : board.getWhitePieces()){
                for(Square square : piece.getLegalMoves(piece.getOccupyingSquare())){
                    if (blackKong.getOccupyingSquare() == square) {
                        if(isBlack) returning = true;
                        break;
                    }
                }
                if(returning) break;
            }

            //Cofnięcie zmian
            board.getSquare(rowFrom, colFrom).removeOccupyingPiece();
            board.getSquare(rowTo, colTo).removeOccupyingPiece();

            board.getSquare(rowTo, colTo).putPiece(secPiece);
            board.getSquare(rowFrom, colFrom).putPiece(movPiece);

            movPiece.setOccupyingSquare(board.getSquare(rowFrom, colFrom));
            if (secPiece != null){
                board.addPiece(secPiece);
                secPiece.setOccupyingSquare(board.getSquare(rowTo, colTo));
            }

            return returning;
        }
    }

    ////////////////////////////////////
    /*Koniec sekcji nie od autora bota*/
    ////////////////////////////////////


    public String alphaBeta(int depth, int beta, int alpha, String move, int player) {
        //Dodawanie wszystkiego, co możliwe, do listy ruchów (Wpuśćmy wszystkich, później ustali się, kim są!)
        if(!Objects.equals(move, "")) ProperMovesHandler.moves.add(move);

        //return in the form of 1234b##########
        String list=posibleMoves();
        if (depth==0 || list.isEmpty()) {return move+(Rating.rating(list.length(), depth)*(player*2-1));}
        list=sortMoves(list);
        player=1-player;//either 1 or 0
        for (int i=0;i<list.length();i+=5) {
            makeMove(list.substring(i,i+5));
            flipBoard();
            String returnString=alphaBeta(depth-1, beta, alpha, list.substring(i,i+5), player);
            int value=Integer.parseInt(returnString.substring(5));
            flipBoard();
            undoMove(list.substring(i,i+5));
            if (player==0) {
                if (value<=beta) {beta=value; if (depth==globalDepth) {move=returnString.substring(0,5);}}
            } else {
                if (value>alpha) {alpha=value; if (depth==globalDepth) {move=returnString.substring(0,5);}}
            }
            if (alpha>=beta) {
                if (player==0) {return move+beta;} else {return move+alpha;}
            }
        }
        if (player==0) {
            return move+beta;
        } else {
            return move+alpha;
        }
    }


    public static void flipBoard() {
        String temp;
        for (int i=0;i<32;i++) {
            int r=i/8, c=i%8;
            if (Character.isUpperCase(chessBoard[r][c].charAt(0))) {
                temp=chessBoard[r][c].toLowerCase();
            } else {
                temp=chessBoard[r][c].toUpperCase();
            }
            if (Character.isUpperCase(chessBoard[7-r][7-c].charAt(0))) {
                chessBoard[r][c]=chessBoard[7-r][7-c].toLowerCase();
            } else {
                chessBoard[r][c]=chessBoard[7-r][7-c].toUpperCase();
            }
            chessBoard[7-r][7-c]=temp;
        }
        int kingTemp=kingPositionC;
        kingPositionC=63-kingPositionL;
        kingPositionL=63-kingTemp;
    }


    public void makeMove(String move) {
        if (move.charAt(4)!='P') {
            chessBoard[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))]=chessBoard[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))];
            chessBoard[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))]=" ";
            if ("A".equals(chessBoard[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))])) {
                kingPositionC=8*Character.getNumericValue(move.charAt(2))+Character.getNumericValue(move.charAt(3));
            }
        } else {
            //if pawn promotion
            chessBoard[1][Character.getNumericValue(move.charAt(0))]=" ";
            chessBoard[0][Character.getNumericValue(move.charAt(1))]=String.valueOf(move.charAt(3));
        }
    }


    private void undoMove(String move) {
        if (move.charAt(4)!='P') {
            chessBoard[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))]=chessBoard[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))];
            chessBoard[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))]=String.valueOf(move.charAt(4));
            if ("A".equals(chessBoard[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))])) {
                kingPositionC=8*Character.getNumericValue(move.charAt(0))+Character.getNumericValue(move.charAt(1));
            }
        } else {
            //if pawn promotion
            chessBoard[1][Character.getNumericValue(move.charAt(0))]="P";
            chessBoard[0][Character.getNumericValue(move.charAt(1))]=String.valueOf(move.charAt(2));
        }
    }


    private String posibleMoves() {
        StringBuilder list= new StringBuilder();
        for (int i=0; i<64; i++) {
            switch (chessBoard[i/8][i%8]) {
                case "P": list.append(posibleP(i));
                    break;
                case "R": list.append(posibleR(i));
                    break;
                case "K": list.append(posibleK(i));
                    break;
                case "B": list.append(posibleB(i));
                    break;
                case "Q": list.append(posibleQ(i));
                    break;
                case "A": list.append(posibleA(i));
                    break;
            }
        }
        return list.toString();//x1,y1,x2,y2,captured piece
    }


    private String posibleP(int i) {
        StringBuilder list= new StringBuilder();
        String oldPiece;
        int r=i/8, c=i%8;
        for (int j=-1; j<=1; j+=2) {
            try {//capture
                if (Character.isLowerCase(chessBoard[r-1][c+j].charAt(0)) && i>=16) {
                    oldPiece=chessBoard[r-1][c+j];
                    chessBoard[r][c]=" ";
                    chessBoard[r-1][c+j]="P";
                    if (kingSafe()) {
                        list.append(r).append(c).append(r - 1).append(c + j).append(oldPiece);
                    }
                    chessBoard[r][c]="P";
                    chessBoard[r-1][c+j]=oldPiece;
                }
            } catch (Exception _) {}
            try {//promotion && capture
                if (Character.isLowerCase(chessBoard[r-1][c+j].charAt(0)) && i<16) {
                    String[] temp={"Q","R","B","K"};
                    for (int k=0; k<4; k++) {
                        oldPiece=chessBoard[r-1][c+j];
                        chessBoard[r][c]=" ";
                        chessBoard[r-1][c+j]=temp[k];
                        if (kingSafe()) {
                            //column1,column2,captured-piece,new-piece,P
                            list.append(c).append(c + j).append(oldPiece).append(temp[k]).append("P");
                        }
                        chessBoard[r][c]="P";
                        chessBoard[r-1][c+j]=oldPiece;
                    }
                }
            } catch (Exception _) {}
        }
        try {//move one up
            if (" ".equals(chessBoard[r-1][c]) && i>=16) {
                oldPiece=chessBoard[r-1][c];
                chessBoard[r][c]=" ";
                chessBoard[r-1][c]="P";
                if (kingSafe()) {
                    list.append(r).append(c).append(r - 1).append(c).append(oldPiece);
                }
                chessBoard[r][c]="P";
                chessBoard[r-1][c]=oldPiece;
            }
        } catch (Exception _) {}
        try {//promotion && no capture
            if (" ".equals(chessBoard[r-1][c]) && i<16) {
                String[] temp={"Q","R","B","K"};
                for (int k=0; k<4; k++) {
                    oldPiece=chessBoard[r-1][c];
                    chessBoard[r][c]=" ";
                    chessBoard[r-1][c]=temp[k];
                    if (kingSafe()) {
                        //column1,column2,captured-piece,new-piece,P
                        list.append(c).append(c).append(oldPiece).append(temp[k]).append("P");
                    }
                    chessBoard[r][c]="P";
                    chessBoard[r-1][c]=oldPiece;
                }
            }
        } catch (Exception _) {}
        try {//move two up
            if (" ".equals(chessBoard[r-1][c]) && " ".equals(chessBoard[r-2][c]) && i>=48) {
                oldPiece=chessBoard[r-2][c];
                chessBoard[r][c]=" ";
                chessBoard[r-2][c]="P";
                if (kingSafe()) {
                    list.append(r).append(c).append(r - 2).append(c).append(oldPiece);
                }
                chessBoard[r][c]="P";
                chessBoard[r-2][c]=oldPiece;
            }
        } catch (Exception _) {}
        return list.toString();
    }


    private String posibleR(int i) {
        StringBuilder list= new StringBuilder();
        String oldPiece;
        int r=i/8, c=i%8;
        int temp=1;
        for (int j=-1; j<=1; j+=2) {
            try {
                while(" ".equals(chessBoard[r][c+temp*j]))
                {
                    oldPiece=chessBoard[r][c+temp*j];
                    chessBoard[r][c]=" ";
                    chessBoard[r][c+temp*j]="R";
                    if (kingSafe()) {
                        list.append(r).append(c).append(r).append(c + temp * j).append(oldPiece);
                    }
                    chessBoard[r][c]="R";
                    chessBoard[r][c+temp*j]=oldPiece;
                    temp++;
                }
                if (Character.isLowerCase(chessBoard[r][c+temp*j].charAt(0))) {
                    oldPiece=chessBoard[r][c+temp*j];
                    chessBoard[r][c]=" ";
                    chessBoard[r][c+temp*j]="R";
                    if (kingSafe()) {
                        list.append(r).append(c).append(r).append(c + temp * j).append(oldPiece);
                    }
                    chessBoard[r][c]="R";
                    chessBoard[r][c+temp*j]=oldPiece;
                }
            } catch (Exception _) {}
            temp=1;
            try {
                while(" ".equals(chessBoard[r+temp*j][c]))
                {
                    oldPiece=chessBoard[r+temp*j][c];
                    chessBoard[r][c]=" ";
                    chessBoard[r+temp*j][c]="R";
                    if (kingSafe()) {
                        list.append(r).append(c).append(r + temp * j).append(c).append(oldPiece);
                    }
                    chessBoard[r][c]="R";
                    chessBoard[r+temp*j][c]=oldPiece;
                    temp++;
                }
                if (Character.isLowerCase(chessBoard[r+temp*j][c].charAt(0))) {
                    oldPiece=chessBoard[r+temp*j][c];
                    chessBoard[r][c]=" ";
                    chessBoard[r+temp*j][c]="R";
                    if (kingSafe()) {
                        list.append(r).append(c).append(r + temp * j).append(c).append(oldPiece);
                    }
                    chessBoard[r][c]="R";
                    chessBoard[r+temp*j][c]=oldPiece;
                }
            } catch (Exception _) {}
            temp=1;
        }
        return list.toString();
    }


    private String posibleK(int i) {
        StringBuilder list= new StringBuilder();
        String oldPiece;
        int r=i/8, c=i%8;
        for (int j=-1; j<=1; j+=2) {
            for (int k=-1; k<=1; k+=2) {
                try {
                    if (Character.isLowerCase(chessBoard[r+j][c+k*2].charAt(0)) || " ".equals(chessBoard[r+j][c+k*2])) {
                        oldPiece=chessBoard[r+j][c+k*2];
                        chessBoard[r][c]=" ";
                        if (kingSafe()) {
                            list.append(r).append(c).append(r + j).append(c + k * 2).append(oldPiece);
                        }
                        chessBoard[r][c]="K";
                        chessBoard[r+j][c+k*2]=oldPiece;
                    }
                } catch (Exception _) {}
                try {
                    if (Character.isLowerCase(chessBoard[r+j*2][c+k].charAt(0)) || " ".equals(chessBoard[r+j*2][c+k])) {
                        oldPiece=chessBoard[r+j*2][c+k];
                        chessBoard[r][c]=" ";
                        if (kingSafe()) {
                            list.append(r).append(c).append(r + j * 2).append(c + k).append(oldPiece);
                        }
                        chessBoard[r][c]="K";
                        chessBoard[r+j*2][c+k]=oldPiece;
                    }
                } catch (Exception _) {}
            }
        }
        return list.toString();
    }


    private String posibleB(int i) {
        StringBuilder list= new StringBuilder();
        String oldPiece;
        int r=i/8, c=i%8;
        int temp=1;
        for (int j=-1; j<=1; j+=2) {
            for (int k=-1; k<=1; k+=2) {
                try {
                    while(" ".equals(chessBoard[r+temp*j][c+temp*k]))
                    {
                        oldPiece=chessBoard[r+temp*j][c+temp*k];
                        chessBoard[r][c]=" ";
                        chessBoard[r+temp*j][c+temp*k]="B";
                        if (kingSafe()) {
                            list.append(r).append(c).append(r + temp * j).append(c + temp * k).append(oldPiece);
                        }
                        chessBoard[r][c]="B";
                        chessBoard[r+temp*j][c+temp*k]=oldPiece;
                        temp++;
                    }
                    if (Character.isLowerCase(chessBoard[r+temp*j][c+temp*k].charAt(0))) {
                        oldPiece=chessBoard[r+temp*j][c+temp*k];
                        chessBoard[r][c]=" ";
                        chessBoard[r+temp*j][c+temp*k]="B";
                        if (kingSafe()) {
                            list.append(r).append(c).append(r + temp * j).append(c + temp * k).append(oldPiece);
                        }
                        chessBoard[r][c]="B";
                        chessBoard[r+temp*j][c+temp*k]=oldPiece;
                    }
                } catch (Exception _) {}
                temp=1;
            }
        }
        return list.toString();
    }


    private String posibleQ(int i) {
        StringBuilder list= new StringBuilder();
        String oldPiece;
        int r=i/8, c=i%8;
        int temp=1;
        for (int j=-1; j<=1; j++) {
            for (int k=-1; k<=1; k++) {
                if (j!=0 || k!=0) {
                    try {
                        while(" ".equals(chessBoard[r+temp*j][c+temp*k]))
                        {
                            oldPiece=chessBoard[r+temp*j][c+temp*k];
                            chessBoard[r][c]=" ";
                            chessBoard[r+temp*j][c+temp*k]="Q";
                            if (kingSafe()) {
                                list.append(r).append(c).append(r + temp * j).append(c + temp * k).append(oldPiece);
                            }
                            chessBoard[r][c]="Q";
                            chessBoard[r+temp*j][c+temp*k]=oldPiece;
                            temp++;
                        }
                        if (Character.isLowerCase(chessBoard[r+temp*j][c+temp*k].charAt(0))) {
                            oldPiece=chessBoard[r+temp*j][c+temp*k];
                            chessBoard[r][c]=" ";
                            chessBoard[r+temp*j][c+temp*k]="Q";
                            if (kingSafe()) {
                                list.append(r).append(c).append(r + temp * j).append(c + temp * k).append(oldPiece);
                            }
                            chessBoard[r][c]="Q";
                            chessBoard[r+temp*j][c+temp*k]=oldPiece;
                        }
                    } catch (Exception _) {}
                    temp=1;
                }
            }
        }
        return list.toString();
    }


    static String posibleA(int i) {
        StringBuilder list= new StringBuilder();
        String oldPiece;
        int r=i/8, c=i%8;
        for (int j=0;j<9;j++) {
            if (j!=4) {
                try {
                    if (Character.isLowerCase(chessBoard[r-1+j/3][c-1+j%3].charAt(0)) || " ".equals(chessBoard[r-1+j/3][c-1+j%3])) {
                        oldPiece=chessBoard[r-1+j/3][c-1+j%3];
                        chessBoard[r][c]=" ";
                        chessBoard[r-1+j/3][c-1+j%3]="A";
                        int kingTemp=kingPositionC;
                        kingPositionC=i+(j/3)*8+j%3-9;
                        if (kingSafe()) {
                            list.append(r).append(c).append(r - 1 + j / 3).append(c - 1 + j % 3).append(oldPiece);
                        }
                        chessBoard[r][c]="A";
                        chessBoard[r-1+j/3][c-1+j%3]=oldPiece;
                        kingPositionC=kingTemp;
                    }
                } catch (Exception _) {}
            }
        }
        //need to add casting later
        return list.toString();
    }

    private String sortMoves(String list) {
        int[] score=new int [list.length()/5];
        for (int i=0;i<list.length();i+=5) {
            makeMove(list.substring(i, i+5));
            score[i/5]=-Rating.rating(-1, 0);
            undoMove(list.substring(i, i+5));
        }
        StringBuilder newListA= new StringBuilder();
        String newListB=list;
        for (int i=0;i<Math.min(6, list.length()/5);i++) {//first few moves only
            int max=-1000000, maxLocation=0;
            for (int j=0;j<list.length()/5;j++) {
                if (score[j]>max) {max=score[j]; maxLocation=j;}
            }
            score[maxLocation]=-1000000;
            newListA.append(list, maxLocation * 5, maxLocation * 5 + 5);
            newListB=newListB.replace(list.substring(maxLocation*5,maxLocation*5+5), "");
        }
        return newListA+newListB;
    }

    static boolean kingSafe() {
        //bishop/queen
        int temp=1;
        for (int i=-1; i<=1; i+=2) {
            for (int j=-1; j<=1; j+=2) {
                try {
                    while(" ".equals(chessBoard[kingPositionC/8+temp*i][kingPositionC%8+temp*j])) {temp++;}
                    if ("b".equals(chessBoard[kingPositionC/8+temp*i][kingPositionC%8+temp*j]) ||
                            "q".equals(chessBoard[kingPositionC/8+temp*i][kingPositionC%8+temp*j])) {
                        return false;
                    }
                } catch (Exception _) {}
                temp=1;
            }
        }
        //rook/queen
        for (int i=-1; i<=1; i+=2) {
            try {
                while(" ".equals(chessBoard[kingPositionC/8][kingPositionC%8+temp*i])) {temp++;}
                if ("r".equals(chessBoard[kingPositionC/8][kingPositionC%8+temp*i]) ||
                        "q".equals(chessBoard[kingPositionC/8][kingPositionC%8+temp*i])) {
                    return false;
                }
            } catch (Exception _) {}
            temp=1;
            try {
                while(" ".equals(chessBoard[kingPositionC/8+temp*i][kingPositionC%8])) {temp++;}
                if ("r".equals(chessBoard[kingPositionC/8+temp*i][kingPositionC%8]) ||
                        "q".equals(chessBoard[kingPositionC/8+temp*i][kingPositionC%8])) {
                    return false;
                }
            } catch (Exception _) {}
            temp=1;
        }
        //knight
        for (int i=-1; i<=1; i+=2) {
            for (int j=-1; j<=1; j+=2) {
                try {
                    if ("k".equals(chessBoard[kingPositionC/8+i][kingPositionC%8+j*2])) {
                        return false;
                    }
                } catch (Exception _) {}
                try {
                    if ("k".equals(chessBoard[kingPositionC/8+i*2][kingPositionC%8+j])) {
                        return false;
                    }
                } catch (Exception _) {}
            }
        }
        //pawn
        if (kingPositionC>=16) {
            try {
                if ("p".equals(chessBoard[kingPositionC/8-1][kingPositionC%8-1])) {
                    return false;
                }
            } catch (Exception _) {}
            try {
                if ("p".equals(chessBoard[kingPositionC/8-1][kingPositionC%8+1])) {
                    return false;
                }
            } catch (Exception _) {}
            //king
            for (int i=-1; i<=1; i++) {
                for (int j=-1; j<=1; j++) {
                    if (i!=0 || j!=0) {
                        try {
                            if ("a".equals(chessBoard[kingPositionC/8+i][kingPositionC%8+j])) {
                                return false;
                            }
                        } catch (Exception _) {}
                    }
                }
            }
        }
        return true;
    }
}