package encryptor.gui;

/*
@author anonymous6291
Github:   https://github.com/anonymous6291
 */
import encryptor.Encryptor_Console;
import encryptor.TaskManager;
import encryptor.videomanager.VideoPlayer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

@SuppressWarnings("ClassWithoutLogger")
public class GUIManager implements Runnable, DefaultColors, DefaultFonts, OtherConstants {

    private Toolkit toolkit;
    private int WIDTH;
    private int HEIGHT;
    private volatile boolean initialized = false;
    private volatile boolean animationRunning = false;
    private volatile boolean animationFinished = false;
    private volatile boolean encryptMode;
    private volatile boolean errorOccured;
    private JFrame window;
    private Point windowLocation;
    private BevelBorder selected;
    private BevelBorder notSelected;
    private VideoPlayer videoPlayer;
    private JPanel display;
    private JPanel animationPanel;
    private JLabel message;
    private JPanel left;
    private JPanel right;
    private JLabel leftmsg;
    private JLabel rightmsg;
    private JButton proceed;
    private JPanel queryPanel;
    private JPanel progressContainer;
    private JTextField paths;
    private JPasswordField password;
    private JTextField threads;
    private JCheckBox modify;
    private JLabel modifyLabel;
    private JButton startMenu;
    private BallAndTextAnimation bata;
    private JLabel progressStatus;
    private JTextArea progressMessage;
    private volatile boolean lockProgressMessage;
    private JFileChooser fileChooser;

    public GUIManager() {
    }

    @Override
    public void run() {
        if (!initialized) {
            init();
        }
        showWindow();
        showAnimation();
        playVideo();
    }

    private JButton makeButton(String text, Color background, Color foreground, ActionListener al) {
        JButton b = new JButton(text);
        b.setBackground(background);
        b.setForeground(foreground);
        b.setFont(BUTTON_FONT);
        b.setFocusPainted(false);
        if (al != null) {
            b.addActionListener(al);
        }
        return b;
    }

    private void init() {
        WIDTH = WINDOW_WIDTH;
        HEIGHT = WINDOW_HEIGHT;
        toolkit = Toolkit.getDefaultToolkit();
        window = new JFrame("Encryptor-GUI");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(WIDTH, HEIGHT);
        setWindowPosition();
        window.setLocation(windowLocation);
        window.setResizable(false);
        videoPlayer = new VideoPlayer(true, new BackgroundFrames());
        videoPlayer.setDoubleBuffered(false);
        window.setContentPane(videoPlayer);
        display = new JPanel(new BorderLayout());
        display.setOpaque(false);
        JComponent glassPane = (JComponent) window.getGlassPane();
        glassPane.setLayout(new BorderLayout());
        glassPane.add(display, BorderLayout.CENTER);
        glassPane.setVisible(true);
        initialized = true;
        encryptMode = true;
        paths = new JTextField(200);
        paths.setBackground(QUERY_FIELD_BACKGROUND_COLOR);
        paths.setForeground(QUERY_FIELD_FOREGROUND_COLOR);
        paths.setFont(QUERY_FIELD_FONT);
        paths.setPreferredSize(new Dimension(WIDTH / 2, 50));
        password = new JPasswordField(200);
        password.setBackground(QUERY_FIELD_BACKGROUND_COLOR);
        password.setForeground(QUERY_FIELD_FOREGROUND_COLOR);
        password.setFont(QUERY_FIELD_FONT);
        password.setEchoChar('*');
        password.setPreferredSize(new Dimension(WIDTH / 2, 50));
        threads = new JTextField(5);
        threads.setBackground(QUERY_FIELD_BACKGROUND_COLOR);
        threads.setForeground(QUERY_FIELD_FOREGROUND_COLOR);
        threads.setFont(QUERY_FIELD_FONT);
        threads.setText(Integer.toString(DEFAULT_THREAD_COUNT));
        startMenu = makeButton("Start Menu", START_MENU_BACKGROUND_COLOR, START_MENU_FOREGROUND_COLOR, (ae) -> TaskManager.submit(() -> showAnimation()));//new JButton();
        initAnimationPanel();
        initQueryPanel();
        initProgressPanel();
        initUpdateManager();
    }

    private void setWindowPosition() {
        Dimension size = toolkit.getScreenSize();
        windowLocation = new Point();
        windowLocation.setLocation((size.getWidth() - WIDTH) / 2, (size.getHeight() - HEIGHT) / 4);
    }

    @SuppressWarnings({"BroadCatchBlock", "TooBroadCatch", "UseSpecificCatch"})
    private JPanel getCheckForeUpdatesPanel() {
        JCheckBox checkForUpdates = new JCheckBox();
        try {
            checkForUpdates.setSelected(!new File(System.getProperty("user.dir"), "nc").isFile());
        } catch (Exception e) {
        }
        checkForUpdates.setFocusPainted(false);
        checkForUpdates.addActionListener((ae) -> {
            try {
                File nc = new File(System.getProperty("user.dir"), "nc");
                if (checkForUpdates.isSelected()) {
                    nc.delete();
                } else {
                    nc.createNewFile();
                }
            } catch (Exception e) {
            }
            try {
                checkForUpdates.setSelected(!new File(System.getProperty("user.dir"), "nc").isFile());
            } catch (Exception e) {
            }
        });
        JLabel updateLabel = new JLabel("Check for updates on startup ?");
        updateLabel.setFont(LABEL_FONT);
        updateLabel.setForeground(QUERY_LABEL_FOREGROUND_COLOR);
        JPanel cfu = new JPanel(new GridBagLayout());
        cfu.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        cfu.add(checkForUpdates, gbc);
        ++gbc.gridx;
        cfu.add(updateLabel, gbc);
        return cfu;
    }

    private void initAnimationPanel() {
        animationPanel = new JPanel(new BorderLayout());
        animationPanel.setOpaque(false);
        message = new JLabel();
        message.setPreferredSize(new Dimension(WIDTH, HEIGHT / 4));
        message.setBackground(BACKGROUND_DISPLAY_COLOR);
        message.setForeground(WELCOME_MESSAGE_COLOR);
        message.setFont(WELCOME_MESSAGE_FONT);
        message.setHorizontalAlignment(JLabel.CENTER);
        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);
        left = new JPanel(new BorderLayout());
        left.setPreferredSize(new Dimension(WIDTH / 3, HEIGHT / 2));
        left.setBackground(LEFT_MESSAGE_BACKGROUND_COLOR);
        leftmsg = new JLabel();
        leftmsg.setForeground(LEFT_MESSAGE_FOREGROUND_COLOR);
        leftmsg.setFont(LEFT_MESSAGE_FONT);
        leftmsg.setVerticalAlignment(JLabel.CENTER);
        leftmsg.setHorizontalAlignment(JLabel.CENTER);
        left.add(leftmsg, BorderLayout.CENTER);
        right = new JPanel(new BorderLayout());
        right.setBackground(RIGHT_MESSAGE_BACKGROUND_COLOR);
        right.setPreferredSize(new Dimension(WIDTH / 3, HEIGHT / 2));
        rightmsg = new JLabel();
        rightmsg.setForeground(RIGHT_MESSAGE_FOREGROUND_COLOR);
        rightmsg.setFont(RIGHT_MESSAGE_FONT);
        rightmsg.setVerticalAlignment(JLabel.CENTER);
        rightmsg.setHorizontalAlignment(JLabel.CENTER);
        right.add(rightmsg, BorderLayout.CENTER);
        selected = new BevelBorder(BevelBorder.RAISED, SELECTED_OPTION_BORDER_HIGHLIGHT_COLOR, SELECTED_OPTION_BORDER_SHADOW_COLOR);
        notSelected = new BevelBorder(BevelBorder.RAISED, NOT_SELECTED_OPTION_BORDER_HIGHLIGHT_COLOR, NOT_SELECTED_OPTION_BORDER_SHADOW_COLOR);
        left.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                if (!animationFinished) {
                    return;
                }
                encryptMode = true;
                right.setBorder(notSelected);
                left.setBorder(selected);
                refreshComponent(display);
            }
        });
        right.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                if (!animationFinished) {
                    return;
                }
                encryptMode = false;
                right.setBorder(selected);
                left.setBorder(notSelected);
                refreshComponent(display);
            }
        });
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 5;
        gbc.weighty = 5;
        center.add(left, gbc);
        ++gbc.gridx;
        center.add(right, gbc);
        JPanel c = new JPanel(new BorderLayout());
        c.setOpaque(false);
        c.add(center, BorderLayout.CENTER);
        c.add(getCheckForeUpdatesPanel(), BorderLayout.SOUTH);
        proceed = makeButton("Proceed", PROCEED_BUTTON_BACKGROUND_COLOR, PROCEED_BUTTON_FOREGROUND_COLOR, (ae) -> TaskManager.submit(() -> showQueryPanel()));
        proceed.setEnabled(false);
        JPanel temp = new JPanel();
        temp.setOpaque(false);
        temp.add(proceed);
        animationPanel.add(message, BorderLayout.NORTH);
        animationPanel.add(c, BorderLayout.CENTER);
        animationPanel.add(temp, BorderLayout.SOUTH);
    }

    @SuppressWarnings({"LocalVariableHidesMemberVariable", "BroadCatchBlock", "TooBroadCatch", "UseSpecificCatch"})
    private void initQueryPanel() {
        queryPanel = new JPanel(new BorderLayout());
        queryPanel.setOpaque(false);
        modify = new JCheckBox();
        modify.setFocusPainted(false);
        modifyLabel = new JLabel();
        modifyLabel.setVerticalTextPosition(JLabel.CENTER);
        modifyLabel.setHorizontalTextPosition(JLabel.CENTER);
        modifyLabel.setFont(LABEL_FONT);
        modifyLabel.setForeground(QUERY_LABEL_FOREGROUND_COLOR);
        JPanel modifyPanel = new JPanel(new GridBagLayout());
        modifyPanel.setOpaque(false);
        GridBagConstraints gb = new GridBagConstraints();
        gb.gridx = 0;
        gb.gridy = 0;
        modifyPanel.add(modify, gb);
        ++gb.gridx;
        modifyPanel.add(modifyLabel, gb);
        JPanel query = new JPanel(new GridBagLayout());
        query.setOpaque(false);
        fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(true);
        fileChooser.setFileHidingEnabled(false);
        fileChooser.setDialogTitle("Choose Files");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        JPanel pathQuery = new JPanel(new GridBagLayout());
        pathQuery.setOpaque(false);
        gb = new GridBagConstraints();
        gb.anchor = GridBagConstraints.CENTER;
        gb.fill = GridBagConstraints.BOTH;
        gb.gridx = 0;
        gb.gridy = 0;
        gb.weightx = 0;
        gb.weighty = 1;
        gb.ipadx = WIDTH / 2 - 100;
        pathQuery.add(paths, gb);
        gb.ipadx = 0;
        ++gb.gridx;
        pathQuery.add(makeButton("Choose", CHOOSE_FILE_BUTTON_BACKGROUND_COLOR, CHOOSE_FILE_BUTTON_FOREGROUND_COLOR, (ae) -> {
            fileChooser.setSelectedFiles(stringToFiles(paths.getText()));
            int result = fileChooser.showDialog(display, "Done");
            if (result == JFileChooser.APPROVE_OPTION) {
                StringBuilder sps = new StringBuilder("");
                File selected[] = fileChooser.getSelectedFiles();
                if (selected != null) {
                    for (File f : selected) {
                        sps.append(f.toString());
                        sps.append(PATH_SEPARATOR);
                    }
                }
                paths.setText(sps.toString());
            }
        }), gb);
        JPanel passwordContainer = new JPanel(new GridBagLayout());
        passwordContainer.setOpaque(false);
        GridBagConstraints g = new GridBagConstraints();
        g.anchor = GridBagConstraints.CENTER;
        g.fill = GridBagConstraints.BOTH;
        g.gridx = 0;
        g.gridy = 0;
        g.weightx = 0;
        g.weighty = 1;
        g.ipadx = WIDTH / 2 - 100;
        passwordContainer.add(password, g);
        ++g.gridx;
        final JButton showPassword = makeButton("Show", SHOW_PASSWORD_BUTTON_BACKGROUND_COLOR, SHOW_PASSWORD_BUTTON_FOREGROUND_COLOR, null);
        showPassword.addActionListener((ae) -> {
            if (password.getEchoChar() == '\0') {
                password.setEchoChar('*');
                showPassword.setText("Show");
            } else {
                password.setEchoChar('\0');
                showPassword.setText("Hide");
            }
        });
        g.ipadx = 0;
        passwordContainer.add(showPassword, g);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.weighty = 1;
        final JLabel label1 = new JLabel(ENTER_FILE_PATHS_MESSAGE);
        label1.setForeground(QUERY_LABEL_FOREGROUND_COLOR);
        label1.setFont(LABEL_FONT);
        label1.setHorizontalAlignment(JLabel.CENTER);
        query.add(label1, gbc);
        ++gbc.gridy;
        query.add(pathQuery, gbc);
        ++gbc.gridy;
        query.add(modifyPanel, gbc);
        ++gbc.gridy;
        final JLabel label2 = new JLabel(ENTER_PASSWORD_MESSAGE);
        label2.setForeground(QUERY_LABEL_FOREGROUND_COLOR);
        label2.setFont(LABEL_FONT);
        label2.setHorizontalAlignment(JLabel.CENTER);
        query.add(label2, gbc);
        ++gbc.gridy;
        query.add(passwordContainer, gbc);
        ++gbc.gridy;
        final JLabel label3 = new JLabel(ENTER_THREAD_COUNT_MESSAGE);
        label3.setForeground(QUERY_LABEL_FOREGROUND_COLOR);
        label3.setFont(LABEL_FONT);
        label3.setHorizontalAlignment(JLabel.CENTER);
        query.add(label3, gbc);
        ++gbc.gridy;
        query.add(threads, gbc);
        ++gbc.gridy;
        gbc.fill = GridBagConstraints.NONE;
        JPanel last = new JPanel(new GridBagLayout());
        last.setOpaque(false);
        g = new GridBagConstraints();
        g.gridx = 0;
        g.gridy = 0;
        g.weightx = 1;
        last.add(makeButton("Start Menu", START_MENU_BACKGROUND_COLOR, START_MENU_FOREGROUND_COLOR, (ae) -> TaskManager.submit(() -> showAnimation())), g);
        ++g.gridx;
        last.add(makeButton("Proceed", PROCEED_BUTTON_BACKGROUND_COLOR, PROCEED_BUTTON_FOREGROUND_COLOR, (ae) -> {
            try {
                enableQueryPage(false);
                boolean flag1 = checkPaths(paths.getText());
                if (flag1) {
                    label1.setText(ENTER_FILE_PATHS_MESSAGE);
                } else {
                    label1.setText(WRONG_FILE_PATHS_MESSAGE);
                }
                boolean flag2 = validPassword(password.getPassword());
                if (flag2) {
                    label2.setText(ENTER_PASSWORD_MESSAGE);
                } else {
                    label2.setText(WRONG_PASSWORD_MESSAGE);
                }
                boolean flag3 = validThreadCount(threads.getText());
                if (flag3) {
                    label3.setText(ENTER_THREAD_COUNT_MESSAGE);
                } else {
                    label3.setText(WRONG_THREAD_COUNT_MESSAGE);
                }
                enableQueryPage(true);
                if (flag1 && flag2 && flag3) {
                    TaskManager.submit(() -> proceedProcess());
                }
            } catch (Exception e) {
            }
        }), g);
        query.add(last, gbc);
        queryPanel = query;
    }

    private void initProgressPanel() {
        progressContainer = new JPanel(new GridBagLayout());
        progressContainer.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.ipadx = WIDTH - 150;
        gbc.ipady = HEIGHT / 2 - 40;
        gbc.weighty = 1;
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        bata = new BallAndTextAnimation(WIDTH, HEIGHT / 3, BACKGROUND_DISPLAY_COLOR, WELCOME_MESSAGE_COLOR, "Encrypting", "Encrypted.", SUCCESS_STATUS_COLOR);
        top.add(bata.getDisplay(), BorderLayout.CENTER);
        progressStatus = new JLabel("");
        progressStatus.setBackground(BACKGROUND_DISPLAY_COLOR);
        progressStatus.setForeground(PROGRESS_STATUS_MESSAGE_FOREGROUND_COLOR);
        progressStatus.setFont(LABEL_FONT);
        progressStatus.setHorizontalAlignment(JLabel.CENTER);
        progressStatus.setVerticalAlignment(JLabel.TOP);
        top.add(progressStatus, BorderLayout.SOUTH);
        JPanel bottom = new JPanel(new BorderLayout(0, 10));
        bottom.setOpaque(false);
        progressMessage = new JTextArea();
        progressMessage.setEditable(false);
        progressMessage.setBackground(PROGRESS_MESSAGE_BACKGROUND_COLOR);
        progressMessage.setForeground(PROGRESS_MESSAGE_FOREGROUND_COLOR);
        progressMessage.setFont(TEXT_PANE_FONT);
        progressMessage.setText("");
        JScrollPane scrollPane = new JScrollPane(progressMessage);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setFocusable(false);
        bottom.add(scrollPane, BorderLayout.CENTER);
        JPanel button = new JPanel();
        button.setOpaque(false);
        button.setBackground(BACKGROUND_DISPLAY_COLOR);
        button.add(startMenu);
        bottom.add(button, BorderLayout.SOUTH);
        progressContainer.add(top, gbc);
        ++gbc.gridy;
        progressContainer.add(bottom, gbc);
        lockProgressMessage = false;
    }

    private void initUpdateManager() {
        UpdateManager.setWindow(window);
        TaskManager.submit(() -> UpdateManager.check());
    }

    private boolean isBlank(String text) {
        return text != null && (text.isEmpty() || text.matches("\\s*"));
    }

    private File[] stringToFiles(String p) {
        if (p == null) {
            return null;
        }
        List<File> files = new ArrayList<>(1);
        for (String path : p.split(PATH_SEPARATOR)) {
            if (!isBlank(path)) {
                File f = new File(path);
                if (f.exists()) {
                    files.add(f);
                }
            }
        }
        return files.toArray(new File[0]);
    }

    private void enableQueryPage(boolean enable) {
        startMenu.setEnabled(enable);
        proceed.setEnabled(enable);
        paths.setEditable(enable);
        password.setEditable(enable);
        threads.setEditable(enable);
    }

    private boolean checkPaths(String path) {
        if (path == null || isBlank(path)) {
            return false;
        }
        String files[] = path.split(PATH_SEPARATOR);
        if (files == null || files.length == 0) {
            return false;
        }
        for (String p : files) {
            if (isBlank(p) || !new File(p).exists()) {
                return false;
            }
        }
        return true;
    }

    private boolean validPassword(char password[]) {
        return password != null && password.length >= MIN_PASSWORD_LENGTH && password.length <= MAX_PASSWORD_LENGTH;
    }

    private boolean validThreadCount(String threads) {
        try {
            int thread = Integer.parseInt(threads);
            return thread >= MIN_THREAD_COUNT && thread <= MAX_THREAD_COUNT;
        } catch (NumberFormatException e) {
        }
        return false;
    }

    private void showAnimation() {
        if (animationRunning) {
            return;
        }
        animationRunning = true;
        encryptMode = true;
        System.gc();
        TaskManager.submit(() -> {
            try {
                int n = WELCOME_MESSAGE.length();
                for (int i = 0; i < n + 1; i++) {
                    if (i > 0 && WELCOME_MESSAGE.charAt(i - 1) == ' ') {
                        hold(300);
                    }
                    message.setText(WELCOME_MESSAGE.substring(0, i).concat("|"));
                    refreshComponent(display);
                    hold(100);
                }
            } catch (Exception e) {
            }
            animationFinished = true;
        });
        TaskManager.submit(() -> {
            while (!animationFinished) {
            }
            leftmsg.setText(LEFT_MESSAGE);
            left.setBorder(selected);
            rightmsg.setText(RIGHT_MESSAGE);
            right.setBorder(notSelected);
            proceed.setEnabled(true);
            refreshComponent(display);
            animationRunning = false;
        });
        addToDisplay(animationPanel);
    }

    private void playVideo() {
        TaskManager.submit(() -> {
            videoPlayer.stopVideo();
            hold(80);
            videoPlayer.playVideo();
            while (!videoPlayer.videoPlayed()) {
                videoPlayer.showNextFrame();
                videoPlayer.repaint();
                window.repaint();
                try {
                    hold(FRAME_CHANGE_DELAY);
                    System.gc();
                } catch (Exception e) {
                }
            }
        });
    }

    private void hold(int miliseconds) {
        long flag = System.currentTimeMillis() + miliseconds;
        while (flag > System.currentTimeMillis()) {
        }
    }

    private void showQueryPanel() {
        password.setText("");
        modify.setSelected(true);
        if (encryptMode) {
            modifyLabel.setText("You want to hide your file names ?");
        } else {
            modifyLabel.setText("You want to retrieve your file names ?");
        }
        proceed.setEnabled(true);
        addToDisplay(queryPanel);
    }

    @SuppressWarnings({"NestedAssignment", "UseOfSystemOutOrSystemErr"})
    private void proceedProcess() {
        try {
            processInitialized();
            TaskManager.submit(() -> {
                try {
                    Encryptor_Console encryptor_console = new Encryptor_Console(encryptMode, paths.getText(), modify.isSelected(), Integer.parseInt(this.threads.getText()), new String(password.getPassword()));
                    processStarted();
                    encryptor_console.run();
                    hold(250);
                    TaskManager.submit(() -> {
                        try {
                            byte buffer[] = new byte[65535];
                            do {
                                int len;
                                while ((len = encryptor_console.availableInputStream()) > 0) {
                                    appendMessageToProgressMessage(new String(buffer, 0, encryptor_console.readInputStream(buffer, 0, Math.min(len, buffer.length))));
                                }
                                while ((len = encryptor_console.availableErrorStream()) > 0) {
                                    errorOccured();
                                    appendMessageToProgressMessage(new String(buffer, 0, encryptor_console.readErrorStream(buffer, 0, Math.min(len, buffer.length))));
                                }
                            } while (!encryptor_console.taskCompleted());
                        } catch (Exception e) {
                            appendMessageToProgressMessage(e.getMessage());
                        }
                    });
                    while (!encryptor_console.taskCompleted()) {
                    }
                    processCompleted();
                } catch (Exception e) {
                    errorOccured();
                    processCompleted();
                    progressStatus.setText("Failed....");
                    appendMessageToProgressMessage("Process failed. Reason : ".concat(e.getMessage()));
                }
            });
            addToDisplay(progressContainer);
        } catch (Exception e) {
        }
    }

    private void errorOccured() {
        if (errorOccured) {
            return;
        }
        errorOccured = true;
        bata.setErrorStatus("Failed....", FAILED_STATUS_COLOR);
    }

    private void processInitialized() {
        enableQueryPage(false);
        clearProgressMessage();
        progressStatus.setText(encryptMode ? "Encrypting" : "Decrypting");
        bata.setRunningMessage(encryptMode ? "Encrypting" : "Decrypting");
        bata.setFinishMessage(encryptMode ? "Encrypted." : "Decrypted.");
        errorOccured = false;
        refreshComponent(display);
    }

    private void processStarted() {
        window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        bata.setForeground(RUNNING_STATUS_COLOR);
        bata.start();
    }

    private void processCompleted() {
        enableQueryPage(true);
        progressStatus.setText("Completed.");
        if (!errorOccured) {
            bata.setForeground(SUCCESS_STATUS_COLOR);
        }
        bata.stop();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        refreshComponent(display);
    }

    private void clearProgressMessage() {
        while (lockProgressMessage) {
        }
        lockProgressMessage = true;
        progressMessage.setText("");
        lockProgressMessage = false;
    }

    private synchronized void appendMessageToProgressMessage(String text) {
        while (lockProgressMessage) {
        }
        lockProgressMessage = true;
        try {
            String msg = progressMessage.getText().concat(text);
            progressMessage.setText(msg);
            progressMessage.setCaretPosition(msg.length());
        } catch (Exception e) {
        }
        lockProgressMessage = false;
    }

    private void refreshComponent(Component c) {
        if (c == null) {
            return;
        }
        c.revalidate();
        c.repaint();
    }

    private void clearDisplay() {
        display.removeAll();
        refreshComponent(display);
    }

    private void addToDisplay(Component c) {
        if (c == null) {
            return;
        }
        clearDisplay();
        display.add(c, BorderLayout.CENTER);
        refreshComponent(display);
    }

    private void showWindow() {
        window.setVisible(true);
    }
}
