package Chess.GUI;

import Chess.Game.GameLayer;
import Chess.Pieces.Piece;
import Chess.Game.Move;
import Chess.Game.Square;
import Chess.Game.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class GameUI extends JFrame {
    private static final int BOARD_SIZE = 8;
    private JPanel[][] squares = new JPanel[BOARD_SIZE][BOARD_SIZE];
    private Piece heldPiece;
    private JLabel draggedPieceLabel;
    private int startX, startY;
    private JPanel chessboardPanel; // Declaring as a class field
    private JPanel overlayPanel;
    private boolean isMulti;
    private boolean isBot;

    public GameUI(boolean isMultiplayer) {
        isMulti = isMultiplayer;
        initializeUI();
        resetOverlayPanel();  // Ensure the overlay is hidden initially
    }

    public GameUI(boolean isMultiplayer, boolean isBotPlaying) {
        isBot = isBotPlaying;
        Main.getCurrentGame().getBot().setAsBlack();
        isMulti = isMultiplayer;
        initializeUI();
        resetOverlayPanel();  // Ensure the overlay is hidden initially
    }

    public GameUI() {
        isMulti = false;
        initializeUI();
        resetOverlayPanel();  // Ensure the overlay is hidden initially
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);

        JMenuItem returnItem = new JMenuItem("Return to Main Menu");
        returnItem.addActionListener(e -> {
            new MenuUI();
            dispose();
        });
        fileMenu.add(returnItem);


        JMenu gameMenu = new JMenu("Game");
        JMenuItem surrenderItem = new JMenuItem("Surrender");
        surrenderItem.addActionListener(e -> {
            Main.getCurrentGame().setSurrendered();
            checkAndShowGameEndMessage();
        });
        gameMenu.add(surrenderItem);

        JMenuItem drawItem = new JMenuItem("Propose a Draw");
        drawItem.addActionListener(e -> {
            Main.getCurrentGame().proposeDraw();
            checkAndShowGameEndMessage();
        });
        gameMenu.add(drawItem);

        menuBar.add(fileMenu);
        menuBar.add(gameMenu);
        return menuBar;
    }

    private void initializeUI() {
        setTitle("Chess Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setJMenuBar(createMenuBar());

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Layered pane for chessboard and overlay
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(700, 700));

        // Chessboard panel setup
        chessboardPanel = new JPanel(new GridLayout(BOARD_SIZE, BOARD_SIZE));

        Dimension boardSize = new Dimension(700, 700);
        chessboardPanel.setPreferredSize(boardSize);
        chessboardPanel.setMinimumSize(boardSize);
        chessboardPanel.setMaximumSize(boardSize);

        Color brown = new Color(130, 71, 10);
        Color light = new Color(245, 210, 181);
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                squares[i][j] = new JPanel(new BorderLayout());
                if ((i + j) % 2 == 0) {
                    squares[i][j].setBackground(light);
                } else {
                    squares[i][j].setBackground(brown);
                }
                squares[i][j].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        onMousePress(e);
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        try {
                            onMouseRelease(e);
                        } catch (Exception ex) {
                            ex.printStackTrace(); // Handle exception appropriately
                        }
                    }
                });
                squares[i][j].addMouseMotionListener(new MouseAdapter() {
                    @Override
                    public void mouseDragged(MouseEvent e) {
                        onMouseDrag(e);
                    }
                });
                chessboardPanel.add(squares[i][j]);
            }
        }

        drawPieces();

        // Overlay panel for game over message
        overlayPanel = new JPanel();
        overlayPanel.setLayout(new GridBagLayout());
        overlayPanel.setOpaque(false);
        overlayPanel.setPreferredSize(boardSize);
        overlayPanel.setVisible(false);

        JLabel messageLabel = new JLabel();
        messageLabel.setFont(new Font("Arial", Font.BOLD, 48));
        messageLabel.setForeground(Color.RED);
        overlayPanel.add(messageLabel);

        // Adding chessboard and overlay to layered pane
        layeredPane.setLayout(new OverlayLayout(layeredPane));
        layeredPane.add(chessboardPanel, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(overlayPanel, JLayeredPane.PALETTE_LAYER);

        mainPanel.add(layeredPane, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);

        pack();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);

        resetOverlayPanel();  // Ensure the overlay is hidden initially
    }

    public void drawPieces() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                squares[row][col].removeAll();
                Piece piece = Main.getCurrentGame().getBoard().getSquare(row, col).getOccupyingPiece();
                if (piece != null) {
                    JLabel pieceLabel = new JLabel(new ImageIcon(piece.getImage().getImage()));
                    pieceLabel.setHorizontalAlignment(SwingConstants.CENTER);
                    pieceLabel.setVerticalAlignment(SwingConstants.CENTER);
                    squares[row][col].add(pieceLabel, BorderLayout.CENTER);
                }
                squares[row][col].revalidate();
                squares[row][col].repaint();
            }
        }
    }

    private void onMousePress(MouseEvent e) {
        Component component = e.getComponent();
        clearHighlights();  // Clear previous highlights before selecting a new piece
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                if (squares[row][col] == component) {
                    startX = row;
                    startY = col;
                    heldPiece = Main.getCurrentGame().getBoard().getSquare(startX, startY).getOccupyingPiece();
                    if (heldPiece != null) {
                        System.out.println("Holding piece: " + heldPiece);
                        // Highlight legal moves for the selected piece
                        highlightLegalMoves(heldPiece, startX, startY);
                        draggedPieceLabel = new JLabel(new ImageIcon(heldPiece.getImage().getImage()));
                        draggedPieceLabel.setSize(component.getWidth(), component.getHeight());
                        JLayeredPane layeredPane = getLayeredPane();
                        layeredPane.add(draggedPieceLabel, JLayeredPane.DRAG_LAYER);
                        Point p = SwingUtilities.convertPoint(component, e.getPoint(), layeredPane);
                        draggedPieceLabel.setLocation(p.x - draggedPieceLabel.getWidth() / 2, p.y - draggedPieceLabel.getHeight() / 2);
                        squares[startX][startY].removeAll();
                        squares[startX][startY].revalidate();
                        squares[startX][startY].repaint();
                    }
                    break;
                }
            }
        }
    }

    private void onMouseDrag(MouseEvent e) {
        if (heldPiece != null && draggedPieceLabel != null) {
            JLayeredPane layeredPane = getLayeredPane();
            Point p = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), layeredPane);
            draggedPieceLabel.setLocation(p.x - draggedPieceLabel.getWidth() / 2, p.y - draggedPieceLabel.getHeight() / 2);
            System.out.println("Dragging piece");
        }
    }

    private void onMouseRelease(MouseEvent e) throws Exception {
        boolean illegal = false;
        if (heldPiece != null && draggedPieceLabel != null) {
            Component component = e.getComponent();
            JLayeredPane layeredPane = getLayeredPane();
            layeredPane.remove(draggedPieceLabel);
            layeredPane.repaint();
            draggedPieceLabel = null;

            Point releasePoint = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), chessboardPanel);
            int endX = releasePoint.y / (chessboardPanel.getHeight() / BOARD_SIZE);
            int endY = releasePoint.x / (chessboardPanel.getWidth() / BOARD_SIZE);

            if (endX >= 0 && endX < BOARD_SIZE && endY >= 0 && endY < BOARD_SIZE) {
                Square startSquare = Main.getCurrentGame().getBoard().getSquare(startX, startY);
                Square endSquare = Main.getCurrentGame().getBoard().getSquare(endX, endY);
                Move move = new Move(startSquare, endSquare);
                try {
                    Main.getCurrentGame().makeMove(move);
                } catch (Exception ex) {
                    System.err.println(ex.getMessage());
                    illegal = true;
                }
                clearHighlights();  // Clear highlights after the move is made
                drawPieces();
                if (isMulti) {
                    Main.sender.sendCurrentGame();
                }
                checkAndShowGameEndMessage(); // Check if the game is over and show the message
            }
            heldPiece = null;

            if (isBot && !illegal) botMove();
        }
    }

    private void botMove() {
        try {
            Main.getCurrentGame().makeMove(Main.getCurrentGame().getBot().AImove(Main.getCurrentGame().getBoard()));
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        drawPieces();
    }

    public Piece getHeldPiece() {
        return heldPiece;
    }

    private void highlightLegalMoves(Piece piece, int row, int col) {
        if (piece == null) return;
        Square currentSquare = Main.getCurrentGame().getBoard().getSquare(row, col);
        List<Square> legalMoves = Piece.ProperMovesHandler.getProperMoves(piece, piece.getLegalMoves(currentSquare));
        for (Square square : legalMoves) {
            int targetRow = square.getRow();
            int targetCol = square.getCol();
            squares[targetRow][targetCol].setBackground(Color.YELLOW);  // Highlighting the square
        }
    }

    private void clearHighlights() {
        Color lightColor = new Color(245, 210, 181);
        Color darkColor = new Color(130, 71, 10);
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if ((i + j) % 2 == 0) {
                    squares[i][j].setBackground(lightColor);
                } else {
                    squares[i][j].setBackground(darkColor);
                }
            }
        }
    }

    private void checkAndShowGameEndMessage() {
        GameLayer currentGame = Main.getCurrentGame();
        JLabel messageLabel = (JLabel) overlayPanel.getComponent(0);

        if (currentGame.isCheckmate()) {
            String winner = currentGame.isWhiteTheCurrentPlayer() ? "Black" : "White";
            messageLabel.setText("Checkmate! " + winner + " wins!");
            overlayPanel.setVisible(true);
        } else if (currentGame.isStalemate()) {
            messageLabel.setText("Stalemate! It's a draw.");
            overlayPanel.setVisible(true);
        } else if (currentGame.isSurrender()) {
            String winner = currentGame.isWhiteTheCurrentPlayer() ? "Black" : "White";
            messageLabel.setText(winner + " wins by surrender!");
            overlayPanel.setVisible(true);
        } else if (currentGame.isDraw()) {
            messageLabel.setText("Draw! The game is a draw.");
            overlayPanel.setVisible(true);
        }
    }

    private void resetOverlayPanel() {
        overlayPanel.setVisible(false);
    }

    public static void main(String[] args) {
        GameLayer gameLayer = new GameLayer();
        Main.setCurrentGame(gameLayer); // Ustawienie aktualnej gry
        new GameUI(); // Inicjalizacja GameUI z aktualną grą
    }
}
