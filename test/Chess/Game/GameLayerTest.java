package Chess.Game;

import Chess.Pieces.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameLayerTest {

    @Test
    void isVictory() throws Exception {
        GameLayer gameLayer = new GameLayer();
        Main main = new Main();
        Main.setCurrentGame(gameLayer);
        gameLayer.setSurrendered();
        gameLayer.addBoardToPositionList();
        assertTrue(gameLayer.isVictory());
    }

    @Test
    void isDraw() throws Exception {
        GameLayer gameLayer = new GameLayer();
        Main main = new Main();
        Main.setCurrentGame(gameLayer);
        gameLayer.proposeDraw();
        gameLayer.makeMove(new Move(gameLayer.getBoard().getSquare(6,4),gameLayer.getBoard().getSquare(5,4)));
        gameLayer.proposeDraw();
        assertTrue(gameLayer.isDraw());
    }

    @Test
    void isCheck() {
        GameLayer gameLayer = new GameLayer();
        Main main = new Main();
        Main.setCurrentGame(gameLayer);
        gameLayer.getBoard().removePieces();
        gameLayer.getBoard().getSquare(1,5).putPiece(new King(gameLayer.getBoard().getSquare(1,5),true));
        gameLayer.getBoard().getWhitePieces().add(gameLayer.getBoard().getSquare(1,5).getOccupyingPiece());
        gameLayer.getBoard().getSquare(7,7).putPiece(new Rook(gameLayer.getBoard().getSquare(7,7),true));
        gameLayer.getBoard().getWhitePieces().add(gameLayer.getBoard().getSquare(7,7).getOccupyingPiece());
        gameLayer.getBoard().getSquare(0,7).putPiece(new King(gameLayer.getBoard().getSquare(0,7),false));
        gameLayer.getBoard().getBlackPieces().add(gameLayer.getBoard().getSquare(0,7).getOccupyingPiece());
        gameLayer.addBoardToPositionList();
        assertTrue(gameLayer.isCheck(gameLayer.getBoard()));
    }

    @Test
    void isCheckmate1(){
        GameLayer gameLayer = new GameLayer();
        Main main = new Main();
        Main.setCurrentGame(gameLayer);
        gameLayer.getBoard().removePieces();
        gameLayer.getBoard().getSquare(1,5).putPiece(new King(gameLayer.getBoard().getSquare(1,5),true));
        gameLayer.getBoard().getWhitePieces().add(gameLayer.getBoard().getSquare(1,5).getOccupyingPiece());
        gameLayer.getBoard().getSquare(7,7).putPiece(new Rook(gameLayer.getBoard().getSquare(7,7),true));
        gameLayer.getBoard().getWhitePieces().add(gameLayer.getBoard().getSquare(7,7).getOccupyingPiece());
        gameLayer.getBoard().getSquare(0,7).putPiece(new King(gameLayer.getBoard().getSquare(0,7),false));
        gameLayer.getBoard().getBlackPieces().add(gameLayer.getBoard().getSquare(0,7).getOccupyingPiece());
        gameLayer.addBoardToPositionList();
        assertTrue(gameLayer.isCheckmate());
    }

    @Test
    void isCheckmate2(){
        GameLayer gameLayer = new GameLayer();
        Main main = new Main();
        Main.setCurrentGame(gameLayer);
        gameLayer.getBoard().removePieces();
        gameLayer.getBoard().getSquare(7,7).putPiece(new King(gameLayer.getBoard().getSquare(7,7),true));
        gameLayer.getBoard().getWhitePieces().add(gameLayer.getBoard().getSquare(7,7).getOccupyingPiece());
        gameLayer.getBoard().getSquare(2,1).putPiece(new Queen(gameLayer.getBoard().getSquare(2,1),true));
        gameLayer.getBoard().getWhitePieces().add(gameLayer.getBoard().getSquare(2,1).getOccupyingPiece());
        gameLayer.getBoard().getSquare(0,1).putPiece(new King(gameLayer.getBoard().getSquare(0,1),false));
        gameLayer.getBoard().getBlackPieces().add(gameLayer.getBoard().getSquare(0,1).getOccupyingPiece());
        gameLayer.getBoard().getSquare(0,0).putPiece(new Rook(gameLayer.getBoard().getSquare(0,0),false));
        gameLayer.getBoard().getBlackPieces().add(gameLayer.getBoard().getSquare(0,0).getOccupyingPiece());
        gameLayer.getBoard().getSquare(0,2).putPiece(new Rook(gameLayer.getBoard().getSquare(0,2),false));
        gameLayer.getBoard().getBlackPieces().add(gameLayer.getBoard().getSquare(0,2).getOccupyingPiece());
        gameLayer.addBoardToPositionList();
        assertTrue(gameLayer.isCheckmate());
    }

    @Test
    void isStalemate() throws Exception {
        GameLayer gameLayer = new GameLayer();
        Main main = new Main();
        Main.setCurrentGame(gameLayer);
        gameLayer.getBoard().removePieces();
        gameLayer.getBoard().getSquare(0,5).putPiece(new King(gameLayer.getBoard().getSquare(0,5),true));
        gameLayer.getBoard().getWhitePieces().add(gameLayer.getBoard().getSquare(0,5).getOccupyingPiece());
        gameLayer.getBoard().getSquare(2,5).putPiece(new Queen(gameLayer.getBoard().getSquare(2,5),true));
        gameLayer.getBoard().getWhitePieces().add(gameLayer.getBoard().getSquare(2,5).getOccupyingPiece());
        gameLayer.getBoard().getSquare(1,7).putPiece(new King(gameLayer.getBoard().getSquare(1,7),false));
        gameLayer.getBoard().getBlackPieces().add(gameLayer.getBoard().getSquare(1,7).getOccupyingPiece());
        gameLayer.addBoardToPositionList();
        gameLayer.makeMove(new Move(gameLayer.getBoard().getSquare(0,5),gameLayer.getBoard().getSquare(1,5)));
        assertTrue(gameLayer.isStalemate());
    }

    @Test
    void isStalemate2() throws Exception {
        GameLayer gameLayer = new GameLayer();
        Main main = new Main();
        Main.setCurrentGame(gameLayer);
        gameLayer.getBoard().removePieces();
        gameLayer.getBoard().getSquare(0,4).putPiece(new King(gameLayer.getBoard().getSquare(0,4),false));
        gameLayer.getBoard().getBlackPieces().add(gameLayer.getBoard().getSquare(0,4).getOccupyingPiece());
        gameLayer.getBoard().getSquare(1,4).putPiece(new Pawn(gameLayer.getBoard().getSquare(1,4),true));
        gameLayer.getBoard().getWhitePieces().add(gameLayer.getBoard().getSquare(1,4).getOccupyingPiece());
        gameLayer.getBoard().getSquare(2,5).putPiece(new King(gameLayer.getBoard().getSquare(2,5),true));
        gameLayer.getBoard().getWhitePieces().add(gameLayer.getBoard().getSquare(2,5).getOccupyingPiece());
        gameLayer.addBoardToPositionList();
        gameLayer.makeMove(new Move(gameLayer.getBoard().getSquare(2,5),gameLayer.getBoard().getSquare(2,4)));
        assertTrue(gameLayer.isStalemate());
    }

    @Test
    void isSurrender() {
        GameLayer gameLayer = new GameLayer();
        gameLayer.setSurrendered();
        assertTrue(gameLayer.isSurrender());
    }

    @Test
    void isCheckmateNotPossibleKingKnight() {
        GameLayer gameLayer = new GameLayer();
        gameLayer.getBoard().removePieces();
        gameLayer.getBoard().getSquare(0,0).putPiece(new King(gameLayer.getBoard().getSquare(0,0),true));
        gameLayer.getBoard().getWhitePieces().add(gameLayer.getBoard().getSquare(0,0).getOccupyingPiece());
        gameLayer.getBoard().getSquare(0,1).putPiece(new Knight(gameLayer.getBoard().getSquare(0,1),true));
        gameLayer.getBoard().getWhitePieces().add(gameLayer.getBoard().getSquare(0,1).getOccupyingPiece());
        gameLayer.getBoard().getSquare(0,2).putPiece(new King(gameLayer.getBoard().getSquare(0,2),false));
        gameLayer.getBoard().getBlackPieces().add(gameLayer.getBoard().getSquare(0,2).getOccupyingPiece());
        gameLayer.addBoardToPositionList();
        assertTrue(gameLayer.isCheckmateNotPossible());
    }

    @Test
    void isCheckmateNotPossibleKingKing() {
        GameLayer gameLayer = new GameLayer();
        gameLayer.getBoard().removePieces();
        gameLayer.getBoard().getSquare(0,0).putPiece(new King(gameLayer.getBoard().getSquare(0,0),true));
        gameLayer.getBoard().getWhitePieces().add(gameLayer.getBoard().getSquare(0,0).getOccupyingPiece());
        gameLayer.getBoard().getSquare(0,3).putPiece(new King(gameLayer.getBoard().getSquare(0,3),false));
        gameLayer.getBoard().getBlackPieces().add(gameLayer.getBoard().getSquare(0,3).getOccupyingPiece());
        gameLayer.addBoardToPositionList();
        assertTrue(gameLayer.isCheckmateNotPossible());
    }

    @Test
    void isCheckmateNotPossibleKingBishop() {
        GameLayer gameLayer = new GameLayer();
        gameLayer.getBoard().removePieces();
        gameLayer.getBoard().getSquare(0,0).putPiece(new King(gameLayer.getBoard().getSquare(0,0),true));
        gameLayer.getBoard().getWhitePieces().add(gameLayer.getBoard().getSquare(0,0).getOccupyingPiece());
        gameLayer.getBoard().getSquare(0,1).putPiece(new Bishop(gameLayer.getBoard().getSquare(0,1),true));
        gameLayer.getBoard().getWhitePieces().add(gameLayer.getBoard().getSquare(0,1).getOccupyingPiece());
        gameLayer.getBoard().getSquare(0,2).putPiece(new King(gameLayer.getBoard().getSquare(0,2),false));
        gameLayer.getBoard().getBlackPieces().add(gameLayer.getBoard().getSquare(0,2).getOccupyingPiece());
        gameLayer.addBoardToPositionList();
        assertTrue(gameLayer.isCheckmateNotPossible());
    }

    @Test
    void isCheckmateNotPossibleKingBishopKingBishop() {
        GameLayer gameLayer = new GameLayer();
        gameLayer.getBoard().removePieces();
        gameLayer.getBoard().getSquare(0,0).putPiece(new King(gameLayer.getBoard().getSquare(0,0),true));
        gameLayer.getBoard().getWhitePieces().add(gameLayer.getBoard().getSquare(0,0).getOccupyingPiece());
        gameLayer.getBoard().getSquare(0,1).putPiece(new Bishop(gameLayer.getBoard().getSquare(0,1),true));
        gameLayer.getBoard().getWhitePieces().add(gameLayer.getBoard().getSquare(0,1).getOccupyingPiece());
        gameLayer.getBoard().getSquare(0,2).putPiece(new King(gameLayer.getBoard().getSquare(0,2),false));
        gameLayer.getBoard().getBlackPieces().add(gameLayer.getBoard().getSquare(0,2).getOccupyingPiece());
        gameLayer.getBoard().getSquare(0,3).putPiece(new Bishop(gameLayer.getBoard().getSquare(0,3),false));
        gameLayer.getBoard().getBlackPieces().add(gameLayer.getBoard().getSquare(0,3).getOccupyingPiece());
        gameLayer.addBoardToPositionList();
        assertTrue(gameLayer.isCheckmateNotPossible());
    }

    @Test
    void isPositionRepeatedThrice() throws Exception {
        GameLayer gameLayer = new GameLayer();
        Main main = new Main();
        Main.setCurrentGame(gameLayer);
        gameLayer.getBoard().removePieces();
        gameLayer.getBoard().getSquare(0,0).putPiece(new King(gameLayer.getBoard().getSquare(0,0),true));
        gameLayer.getBoard().getWhitePieces().add(gameLayer.getBoard().getSquare(0,0).getOccupyingPiece());
        gameLayer.getBoard().getSquare(1,1).putPiece(new Rook(gameLayer.getBoard().getSquare(1,1),true));
        gameLayer.getBoard().getWhitePieces().add(gameLayer.getBoard().getSquare(1,1).getOccupyingPiece());
        gameLayer.getBoard().getSquare(7,7).putPiece(new King(gameLayer.getBoard().getSquare(7,7),false));
        gameLayer.getBoard().getBlackPieces().add(gameLayer.getBoard().getSquare(7,7).getOccupyingPiece());
        gameLayer.addBoardToPositionList();
        for (int i = 0; i < 2; i++) {
            gameLayer.makeMove(new Move(gameLayer.getBoard().getSquare(0,0),gameLayer.getBoard().getSquare(1,0)));
            gameLayer.makeMove(new Move(gameLayer.getBoard().getSquare(7,7),gameLayer.getBoard().getSquare(6,7)));
            gameLayer.makeMove(new Move(gameLayer.getBoard().getSquare(1,0),gameLayer.getBoard().getSquare(0,0)));
            gameLayer.makeMove(new Move(gameLayer.getBoard().getSquare(6,7),gameLayer.getBoard().getSquare(7,7)));
        }
        assertTrue(gameLayer.isDraw());
    }

    @Test
    void isFiftyMoveRuleSatisfied() throws Exception {
        GameLayer gameLayer = new GameLayer();
        Main main = new Main();
        Main.setCurrentGame(gameLayer);
        gameLayer.getBoard().removePieces();
        gameLayer.getBoard().getSquare(0,0).putPiece(new King(gameLayer.getBoard().getSquare(0,0),true));
        gameLayer.getBoard().getWhitePieces().add(gameLayer.getBoard().getSquare(0,0).getOccupyingPiece());
        gameLayer.getBoard().getSquare(1,1).putPiece(new Rook(gameLayer.getBoard().getSquare(1,1),true));
        gameLayer.getBoard().getWhitePieces().add(gameLayer.getBoard().getSquare(1,1).getOccupyingPiece());
        gameLayer.getBoard().getSquare(7,7).putPiece(new King(gameLayer.getBoard().getSquare(7,7),false));
        gameLayer.getBoard().getBlackPieces().add(gameLayer.getBoard().getSquare(7,7).getOccupyingPiece());
        gameLayer.addBoardToPositionList();
        for (int i = 0; i < 25; i++) {
            gameLayer.makeMove(new Move(gameLayer.getBoard().getSquare(0,0),gameLayer.getBoard().getSquare(1,0)));
            gameLayer.makeMove(new Move(gameLayer.getBoard().getSquare(7,7),gameLayer.getBoard().getSquare(6,7)));
            gameLayer.makeMove(new Move(gameLayer.getBoard().getSquare(1,0),gameLayer.getBoard().getSquare(0,0)));
            gameLayer.makeMove(new Move(gameLayer.getBoard().getSquare(6,7),gameLayer.getBoard().getSquare(7,7)));
        }
        assertTrue(gameLayer.isFiftyMoveRuleSatisfied());
    }

    @Test
    void isDrawAgreedUpon() throws Exception {
        GameLayer gameLayer = new GameLayer();
        Main main = new Main();
        Main.setCurrentGame(gameLayer);
        gameLayer.proposeDraw();
        gameLayer.makeMove(new Move(gameLayer.getBoard().getSquare(6,4),gameLayer.getBoard().getSquare(5,4)));
        gameLayer.proposeDraw();
        assertTrue(gameLayer.isDrawAgreedUpon());
    }
}