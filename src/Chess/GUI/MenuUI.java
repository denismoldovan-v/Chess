package Chess.GUI;

import Chess.Game.GameLayer;
import Chess.Game.Main;
import Network.NetHandler;
import Chess.FileHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;

public class MenuUI extends JFrame {
    private JButton newGameButton;
    private JButton loadGameButton;
    private JButton exitButton;
    private JButton settingsButton;
    private JButton aboutButton;
    private JButton hostGameButton;
    private JButton joinGameButton;

    public MenuUI() {
        setTitle("Chess Game Menu");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initializeComponents();
        setVisible(true);
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName()
            );
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                 UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }
    }

    private void initializeComponents() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        newGameButton = new JButton("New Local Game");
        loadGameButton = new JButton("Play with Bot");
        settingsButton = new JButton("Settings");
        aboutButton = new JButton("About");
        hostGameButton = new JButton("Host Online Game");
        joinGameButton = new JButton("Join Online Game");
        exitButton = new JButton("Exit");

        Dimension buttonSize = new Dimension(150, 30);
        setButtonSize(newGameButton, buttonSize);
        setButtonSize(loadGameButton, buttonSize);
        setButtonSize(settingsButton, buttonSize);
        setButtonSize(aboutButton, buttonSize);
        setButtonSize(hostGameButton, buttonSize);
        setButtonSize(joinGameButton, buttonSize);
        setButtonSize(exitButton, buttonSize);

        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startLocalGame();
            }
        });

        loadGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playWithBot();
            }
        });

        settingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showSettings();
            }
        });

        aboutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAboutInfo();
            }
        });

        hostGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hostOnlineGame();
            }
        });

        joinGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                joinOnlineGame();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exitGame();
            }
        });

        mainPanel.add(createCenteredPanel(newGameButton));
        mainPanel.add(createVerticalStrut(10));
        mainPanel.add(createCenteredPanel(loadGameButton));
        mainPanel.add(createVerticalStrut(10));
        mainPanel.add(createCenteredPanel(settingsButton));
        mainPanel.add(createVerticalStrut(10));
        mainPanel.add(createCenteredPanel(aboutButton));
        mainPanel.add(createVerticalStrut(10));
        mainPanel.add(createCenteredPanel(hostGameButton));
        mainPanel.add(createVerticalStrut(10));
        mainPanel.add(createCenteredPanel(joinGameButton));
        mainPanel.add(createVerticalStrut(10));
        mainPanel.add(createCenteredPanel(exitButton));

        add(mainPanel);
    }

    private JPanel createCenteredPanel(JButton button) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.add(button);
        return panel;
    }

    private Component createVerticalStrut(int height) {
        return Box.createVerticalStrut(height);
    }

    private void setButtonSize(JButton button, Dimension size) {
        button.setPreferredSize(size);
        button.setMinimumSize(size);
        button.setMaximumSize(size);
    }

    // Method to start a new local game
    private void startLocalGame() {
        System.out.println("New Local Game started");
        Main.setCurrentGame(new GameLayer());
        Main.gui = new GameUI();
        dispose();
    }

    private void playWithBot() {
        Main.setCurrentGame(new GameLayer());
        Random rand = new Random();
        boolean playerIsWhite = rand.nextBoolean();
        Main.gui = new GameUI(false, playerIsWhite);
        dispose();
    }




    // Method to show settings
    private void showSettings() {
        JOptionPane.showMessageDialog(this, "Settings functionality not implemented yet.");
    }

    // Method to show about information
    private void showAboutInfo() {
        JOptionPane.showMessageDialog(this, "Chess Game\nVersion 1.0\nDocumentation:\n   -Aleksander Chojnowski\n   -Bartłomiej Potaman" +
                "\n   -Patrycja Pytlarczyk\n   -Bartłomiej Masiak \nBackend:\n   -Daniel Milczarek\n   -Radosław Makowski\n   -Piotr Stępniak\n   -Mikołaj Brewczak" +
                "\nUser Interface: \n   -Wojciech Golis\n   -Katarzyna Dziura\n   -Denys Moldovan\n   -Remigiusz Tatar");
    }

    // Method to exit the game
    private void exitGame() {
        int response = JOptionPane.showConfirmDialog(this, "Are you sure you want to exit?", "Exit Confirmation",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (response == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    // Method to host an online game
    private void hostOnlineGame() {
        try {
            try (final DatagramSocket socket = new DatagramSocket()) {
                socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
                String hostname = socket.getLocalAddress().getHostAddress();
                System.out.println(hostname);

                Main.netHandler = new NetHandler(19000);
                Thread recThread = new Thread(Main.netHandler);
                recThread.start();
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println(e + " Brak połączenia sieciowego");
            }

//            JOptionPane.showMessageDialog(this, "Hosting game... Waiting for opponent to join.");
            Main.setCurrentGame(new GameLayer());
            Main.gui = new GameUI(true);
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to host game: " + e.getMessage());
        }
    }

    // Method to join an online game
    private void joinOnlineGame() {
        String hostAddress = JOptionPane.showInputDialog(this, "Enter host address:");
        if (hostAddress != null && !hostAddress.trim().isEmpty()) {
            try {
                Main.netHandler = new NetHandler(hostAddress, 19000, 19001);
                Thread senThread = new Thread(Main.netHandler);
                senThread.start();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Failed to join game: " + e.getMessage());
            }

            Main.setCurrentGame(new GameLayer());
            Main.gui = new GameUI(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid host address.");
        }
    }


    private void onMouseClick() {
    }

    public static void main(String[] args) {
        new MenuUI();
    }
}
