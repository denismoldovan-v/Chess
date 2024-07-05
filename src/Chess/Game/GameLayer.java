package Chess.Game;

import Chess.Bot.AlphaBetaChess;
import Chess.GUI.GameUI;
import Chess.Pieces.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Klasa trzymająca informację na temat obecnej rozgrywki i obsługująca ją.
 */
public class GameLayer implements Serializable {
    private Board board;
    private final ArrayList<Move> moveList;
    private final ArrayList<Board> positionList;
    private final AlphaBetaChess bot;
    private Boolean isWhiteTheCurrentPlayer;
    private Boolean surrender = false;
    private Boolean hasWhiteProposedDraw = false;
    private Boolean hasBlackProposedDraw = false;
    private Boolean isWhiteChecked = false;

    /**
     * Konstruktor inicjalizujący planszę o ustawieniu domyślnym.
     */
    public GameLayer() {
        moveList = new ArrayList<>();
        positionList = new ArrayList<>();
        board = new Board();
        bot = new AlphaBetaChess(/*szybki, latwy - 3; dlugi,trudny - 5*/4, board);
        isWhiteTheCurrentPlayer = true;
        addBoardToPositionList();
    }

    /**
     * Główna metoda wdrażająca przesłany ruch.
     *
     * @param move Ruch do wykonania.
     */
    public void makeMove(Move move) throws Exception {
        System.out.println("makeMove called with move: " + move);
        //Sprawdzanie gracza
        if (!isWhiteTheCurrentPlayer.toString().equals(move.getPiece().isWhite().toString())){
            throw new Exception("Tura innego gracza" + (isWhiteTheCurrentPlayer? " [biały]" : " [czarny]"));
        }

        //Sprawdzenie legalności ruchu dla króla
        if(move.getPiece() instanceof King && !getUpdatedKingLegalMoves(board).contains(move.getTo())) {
            throw new Exception("Nielegalny ruch!");
        }

        //Sprawdzanie legalności ruchu dla pozostałych figur
        if (!(move.getPiece() instanceof King) && !Piece.ProperMovesHandler.getProperMoves(move.getPiece(), move.getPiece().getLegalMoves(move.getPiece().getOccupyingSquare())).contains(move.getTo())){
            throw new Exception("Nielegalny ruch!");
        }

        //Sprawdzenie czy ruch jest legalny, jeśli król jest szachowany.
        if(isCheck(board) && ((isWhiteChecked && move.getPiece().isWhite()) || (!isWhiteChecked && !move.getPiece().isWhite()))){
            int rowFrom = move.getFrom().getRow();
            int rowTo = move.getTo().getRow();
            int colFrom = move.getFrom().getCol();
            int colTo = move.getTo().getCol();
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

            boolean isAllowed = true;
            if(isCheck(board)){
                isAllowed = false;
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

            if(!isAllowed) throw new Exception("Król jest szachowany! Nie możesz wykonać tego ruchu.");
        }

        //Sprawdzanie zmiany warunków roszad
        if (move.getPiece() instanceof King) {
            if(((King) move.getPiece()).isCastlingAllowed() && (move.getTo() == board.getSquare(move.getFrom().getRow(),0) || move.getTo() == board.getSquare(move.getFrom().getRow(),7))){
                board.makeCastling(move);
                move.setCastling();
                moveList.add(move);
                if (isGameEnded()) return;
                nextTurn();
                return;
            }
            ((King) move.getPiece()).disallowCastling();
        }
        if (move.getPiece() instanceof Rook) {
            ((Rook) move.getPiece()).disallowCastling();
        }

        //Warunki promocji
        if (move.getPiece() instanceof Pawn){
            if (move.getPiece().isWhite() && move.getPiece().getOccupyingSquare().getRow() == 7){
                board.promote(board.getSquare(move.getPiece().getOccupyingSquare().getRow(),move.getPiece().getOccupyingSquare().getCol()),Main.gui.getHeldPiece(),move.getPiece().isWhite());
            }
            else if (move.getPiece().getOccupyingSquare().getRow() == 0){
                board.promote(board.getSquare(move.getPiece().getOccupyingSquare().getRow(),move.getPiece().getOccupyingSquare().getCol()),Main.gui.getHeldPiece(),move.getPiece().isWhite());
            }
        }

        //Ustawienie flagi En Passant
        if (move.getPiece() instanceof Pawn && ((Pawn) move.getPiece()).getIsFirstMove() && (move.getTo().getRow() == 3 || move.getTo().getRow() == 4)){
            ((Pawn) move.getPiece()).setCanGetPassed(true);
        } else if (move.getPiece() instanceof Pawn && !((Pawn) move.getPiece()).getIsFirstMove() && ((Pawn) move.getPiece()).getCanGetPassed()) {
            ((Pawn) move.getPiece()).setCanGetPassed(false);
        }

        //Wykonany pierwszy ruch piona
        if(move.getPiece() instanceof Pawn){
            if(((Pawn) move.getPiece()).getIsFirstMove()){
                ((Pawn) move.getPiece()).makedMove();
            }
        }

        //Wykonanie ruchów
        board.makeMove(move);
        moveList.add(move);
        addBoardToPositionList();

        // Check if the game has ended after making the move
        if (isGameEnded()) return;

        nextTurn();
    }

    /**
     * Metoda zmieniająca turę (zmieniająca aktualnego gracza).
     */
    private void nextTurn(){
        isWhiteTheCurrentPlayer = !isWhiteTheCurrentPlayer;
    }

    /**
     * Metoda podająca obecnego gracza.
     *
     * @return Obecny gracz (true - biały; false - czarny)
     */
    public Boolean isWhiteTheCurrentPlayer(){
        return isWhiteTheCurrentPlayer;
    }

    /**
     * Metoda zwraca listę ruchów, jakie dotychczas zostały wykonane.
     *
     * @return Lista ruchów.
     */
    public String getMoveList() {
        StringBuilder str = new StringBuilder("Movements made:\n");
        for (int i = 0; i < moveList.size(); i++) {
            str.append(i + 1).append(": ").append(moveList.get(i)).append(";\n");
        }
        return str.toString();
    }

    /**
     * Metoda zwracająca true, gdy jedna ze stron wygrała lub doszło do remisu.
     *
     * @return Czy gra jest zakończona?
     */
    public boolean isGameEnded(){
        if (isDraw()) {
            System.out.println("Remis");
            return true;
        }
        return isVictory();
    }

    /**
     * Metoda zwracająca true, gdy jedna ze stron zwyciężyła przez mata lub poddanie oponenta.
     *
     * @return Czy ktoś jest zwycięzcą partii?
     */
    public boolean isVictory(){
        if (isCheckmate()) {
            System.out.println("Zwyciężył gracz " + (isWhiteTheCurrentPlayer ? "Biały" : "Czarny"));
            return true;
        }
        if (isSurrender()) {
            System.out.println("Zwyciężył gracz " + (isWhiteTheCurrentPlayer ? "Czarny" : "Biały"));
            return true;
        }
        return false;
    }

    /**
     * Metoda zwracająca true, gdy:
     * - doszło do pata
     * - mat jest niemożliwy (niewystarczający materiał)
     * - doszło do trzykrotnego portórzenia pozycji
     * - w ostatnich 50 ruchach nie doszło do bicia i ruszenia pionem
     * - gracze zgodzili się na remis
     *
     * @return Czy partia zakończyła się remisem?
     */
    public boolean isDraw() {
        return isStalemate() ||
                isCheckmateNotPossible() ||
                isPositionRepeatedThrice() ||
                isFiftyMoveRuleSatisfied() ||
                isDrawAgreedUpon();
    }

    /**
     * Metoda sprawdzająca, czy któraś ze stron jest szachowana.
     * @return Czy jeden z graczy jest szachowany?
     */
    public boolean isCheck(Board board) {
        King whiteKing = board.getKing(board.getWhitePieces());
        King blackKing = board.getKing(board.getBlackPieces());

        // Sprawdzenie, czy gracz biały jest szachowany
        for(Piece piece : board.getBlackPieces()){
            for(Square square : piece.getLegalMoves(piece.getOccupyingSquare())){
                if(whiteKing.getOccupyingSquare() == square){
                    isWhiteChecked = true;
                    return true;
                }
                else isWhiteChecked = false;
            }
        }
        // Sprawdzenie, czy gracz czarny jest szachowany
        for(Piece piece : board.getWhitePieces()){
            for(Square square : piece.getLegalMoves(piece.getOccupyingSquare())){
                if(blackKing.getOccupyingSquare() == square){return true;}
            }
        }
        return false;
    }

    /**
     * Metoda sprawdzająca, czy wystąpił mat.
     *
     * @return Czy wystąpił mat?
     */
    public boolean isCheckmate() {
        Board currentBoard = positionList.get(positionList.size() - 1);
        Board checkmateTestingBoard = new Board(currentBoard);
        if(isCheck(currentBoard)) {
            if (isWhiteChecked) {
                for (Piece piece : currentBoard.getWhitePieces()) {
                    if(piece instanceof King){
                        for(Square square : getUpdatedKingLegalMoves(checkmateTestingBoard)){
                            Square temporarySquare = piece.getOccupyingSquare();
                            checkmateTestingBoard.makeMove(new Move(piece.getOccupyingSquare(),square));
                            checkmateTestingBoard.removePiece(temporarySquare);
                            if (!isCheck(checkmateTestingBoard)) {
                                return false;
                            }
                            checkmateTestingBoard = positionList.get(positionList.size() - 1);
                        }
                    }
                    else {
                        for (Square square : piece.getLegalMoves(piece.getOccupyingSquare())) {
                            Square temporarySquare = piece.getOccupyingSquare();
                            checkmateTestingBoard.makeMove(new Move(piece.getOccupyingSquare(),square));
                            checkmateTestingBoard.removePiece(temporarySquare);
                            if (!isCheck(checkmateTestingBoard)) {
                                return false;
                            }
                            checkmateTestingBoard = positionList.get(positionList.size() - 1);
                        }
                    }
                }
            } else {
                for (Piece piece : currentBoard.getBlackPieces()){
                    if(piece instanceof King){
                        for(Square square : getUpdatedKingLegalMoves(checkmateTestingBoard)){
                            Square temporarySquare = piece.getOccupyingSquare();
                            checkmateTestingBoard.makeMove(new Move(piece.getOccupyingSquare(),square));
                            checkmateTestingBoard.removePiece(temporarySquare);
                            if (!isCheck(checkmateTestingBoard)) {
                                return false;
                            }
                            checkmateTestingBoard = positionList.get(positionList.size() - 1);
                        }
                    }
                    else {
                        for (Square square : piece.getLegalMoves(piece.getOccupyingSquare())) {
                            Square temporarySquare = piece.getOccupyingSquare();
                            checkmateTestingBoard.makeMove(new Move(piece.getOccupyingSquare(),square));
                            checkmateTestingBoard.removePiece(temporarySquare);
                            if (!isCheck(checkmateTestingBoard)) {
                                return false;
                            }
                            checkmateTestingBoard = positionList.get(positionList.size() - 1);
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Metoda sprawdzająca czy nie wystąpił pat.
     *
     * @return Czy wystąpił pat?
     */
    public boolean isStalemate() {
        Board currentPosition = positionList.get(positionList.size() - 1);
        System.out.println("Białe:");
        for (Piece piece : currentPosition.getWhitePieces()){
            System.out.println(piece.toString());
            System.out.println(piece.getLegalMoves(piece.getOccupyingSquare()));
        }

        System.out.println("Czarne:");
        for (Piece piece : currentPosition.getBlackPieces()){
            System.out.println(piece.toString());
            System.out.println(piece.getLegalMoves(piece.getOccupyingSquare()));
        }

        System.out.println("Król:");
        System.out.println(getUpdatedKingLegalMoves(currentPosition));
        if(
                currentPosition.getWhitePieces().stream()
                        .filter(Piece -> !(Piece instanceof King))
                        .allMatch(Piece -> Piece.getLegalMoves(Piece.getOccupyingSquare()).isEmpty()) &&
                        getUpdatedKingLegalMoves(currentPosition).isEmpty()
        ){
            System.out.println("Stalemate! It's a draw.");
            return true;
        }
        else {
            if(currentPosition.getBlackPieces().stream()
                    .filter(Piece -> !(Piece instanceof King))
                    .allMatch(Piece -> Piece.getLegalMoves(Piece.getOccupyingSquare()).isEmpty()) &&
                    getUpdatedKingLegalMoves(currentPosition).isEmpty()) {
                System.out.println("Stalemate! It's a draw.");
                return true;
            }
        }
        return false;
    }

    /**
     * Metoda zwracająca true, gdy jedna ze stron się podda.
     *
     * @return Czy któraś ze stron się poddała?
     */
    public boolean isSurrender() {
        return surrender;
    }

    /**
     * Metoda zwracająca true, jeżeli gracz biały jest szachowany.
     */
    public boolean isWhiteChecked() {
        return isWhiteChecked;
    }

    /**
     * Metoda sprawdzająca, czy strony posiadają wystarczający materiał do wykonania mata.
     *
     * @return Czy mat jest możliwy?
     */
    public boolean isCheckmateNotPossible() {
        Board currentPosition = positionList.get(positionList.size() - 1);
        int whitePiecesAmount = currentPosition.getWhitePieces().size();
        int blackPiecesAmount = currentPosition.getBlackPieces().size();

        // Król i goniec/skoczek (biały) vs. król
        if(whitePiecesAmount == 2 &&
                blackPiecesAmount == 1 &&
                currentPosition.getWhitePieces()
                        .stream()
                        .anyMatch(Piece -> Piece instanceof Knight || Piece instanceof Bishop)){
            System.out.println("Draw! Checkmate not possible.");
            return true;
        }
        // Król i goniec/skoczek (czarny) vs. król
        if(blackPiecesAmount == 2 &&
                whitePiecesAmount == 1 &&
                currentPosition.getBlackPieces()
                        .stream()
                        .anyMatch(Piece -> Piece instanceof Knight || Piece instanceof Bishop)){
            System.out.println("Draw! Checkmate not possible.");
            return true;
        }

        // Król vs. król
        if(whitePiecesAmount == 1 && blackPiecesAmount == 1){
            System.out.println("Draw! Checkmate not possible.");
            return true;
        }

        // Król i goniec vs. król i goniec
        if(whitePiecesAmount == 2 && blackPiecesAmount == 2){
            if(currentPosition.getWhitePieces().stream().anyMatch(Piece -> Piece instanceof Bishop) &&
                    currentPosition.getBlackPieces().stream().anyMatch(Piece -> Piece instanceof Bishop)){
                Bishop whiteBishop = null, blackBishop = null;
                for(Piece piece : currentPosition.getWhitePieces()){
                    if(piece instanceof Bishop){
                        whiteBishop = (Bishop) piece;
                    }
                }
                for(Piece piece : currentPosition.getBlackPieces()){
                    if(piece instanceof Bishop){
                        blackBishop = (Bishop) piece;
                    }
                }
                // Oba stoją na białym polu
                if(whiteBishop.getOccupyingSquare().isWhite() && blackBishop.getOccupyingSquare().isWhite()){
                    System.out.println("Draw! Checkmate not possible.");
                    return true;
                }
                // Oba stoją na czarnym polu
                else if(!whiteBishop.getOccupyingSquare().isWhite() && !blackBishop.getOccupyingSquare().isWhite()) {
                    System.out.println("Draw! Checkmate not possible.");
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Metoda sprawdzająca, czy w ciągu całej gry nie doszło do potrójnego powtórzenia pozycji.
     *
     * @return Czy doszło do trzykrotnego powtórzenia pozycji?
     */
    public boolean isPositionRepeatedThrice() {
        int positionRepetitionAmount = 0;
        Board currentPosition = positionList.get(positionList.size() - 1);
        for (Board position : positionList) {
            if(currentPosition.equals(position)){positionRepetitionAmount++;}
        }
        if (positionRepetitionAmount == 3) {
            System.out.println("Draw! Position repeated three times.");
            return true;
        }
        return false;
    }

    /**
     * Metoda sprawdzająca czy:
     * - w ciągu ostatnich 50 ruchów nie wystąpiło bicie żadnej figury
     * - w ciągu ostatnich 50 ruchów nie ruszono żadnym pionkiem
     *
     * @return Czy spełniona jest zasada 50 ruchów?
     */
    public boolean isFiftyMoveRuleSatisfied() {
        if (moveList.size() < 50) {
            return false;
        }
        int currentMoveIndex = moveList.size() - 1;
        for (int i = 0; i < 50; i++) {
            if (moveList.get(currentMoveIndex - i).isPawnMoved() ||
                    moveList.get(currentMoveIndex - i).isCapture()) {
                return false;
            }
        }
        System.out.println("Draw! Fifty-move rule satisfied.");
        return true;
    }

    /**
     * Metoda ustawiająca pole hasWhiteProposedDraw lub hasBlackProposedDraw
     * (w zależności od tego, kto aktualnie wykonuje ruch) na true.
     */
    public void proposeDraw(){
        if(isWhiteTheCurrentPlayer){
            hasWhiteProposedDraw = true;
        }
        else hasBlackProposedDraw = true;
    }

    /**
     * Metoda ustawiająca pole hasWhiteProposedDraw lub hasBlackProposedDraw
     * (w zależności od tego, kto aktualnie wykonuje ruch) na false.
     */
    public void withdrawDrawProposal(){
        if(isWhiteTheCurrentPlayer){
            hasWhiteProposedDraw = false;
        }
        else hasBlackProposedDraw = false;
    }

    /**
     * Metoda zwracająca true, gdy gracze zgodzą się na remis.
     *
     * @return Czy gracze zgodzili się na remis?
     */
    public boolean isDrawAgreedUpon() {
        if (hasWhiteProposedDraw && hasBlackProposedDraw) {
            System.out.println("Draw! Both players agreed.");
            return true;
        }
        return false;
    }

    /**
     * Zwrócenie bota dla aktualnej gry.
     *
     * @return Bot.
     */
    public AlphaBetaChess getBot() {
        return bot;
    }

    /**
     * Zwrócenie aktualnej planszy.
     *
     * @return Aktualna plansza.
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Ustawienie planszy.
     *
     * @param board Nowa plansza.
     */
    public void setBoard(Board board) {
        this.board = board;
    }

    /**
     * Ustawienie przełącznika poddania się.
     */
    public void setSurrendered() {
        surrender = true;
    }

    /**
     * Metoda kopiująca stan aktualnej planszy do listy positionList.
     */
    public void addBoardToPositionList(){
        Board addedBoard = new Board(board);
        positionList.add(addedBoard);
    }

    /**
     * Metoda aktualizująca dostępne ruchy króla, w zależności od obecnej sytuacji na planszy.
     */
    public List<Square> getUpdatedKingLegalMoves(Board board){
        King whiteKing = board.getKing(board.getWhitePieces());
        King blackKing = board.getKing(board.getBlackPieces());

        List<Square> updatedWhiteKingLegalMoves = whiteKing.getLegalMoves(whiteKing.getOccupyingSquare());
        List<Square> updatedBlackKingLegalMoves = blackKing.getLegalMoves(blackKing.getOccupyingSquare());

        if(isWhiteTheCurrentPlayer){
            for (Piece piece : board.getBlackPieces()){
                for (Square square : piece.getLegalMoves(piece.getOccupyingSquare())){
                    if(whiteKing.getLegalMoves(whiteKing.getOccupyingSquare()).contains(square)){
                        updatedWhiteKingLegalMoves.remove(square);
                    }
                }
            }
            return updatedWhiteKingLegalMoves;
        }
        else {
            for (Piece piece : board.getWhitePieces()){
                for (Square square : piece.getLegalMoves(piece.getOccupyingSquare())){
                    if(blackKing.getLegalMoves(blackKing.getOccupyingSquare()).contains(square)){
                        updatedBlackKingLegalMoves.remove(square);
                    }
                }
            }
            return updatedBlackKingLegalMoves;
        }
    }
}
