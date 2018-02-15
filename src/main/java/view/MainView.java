package view;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneLayout;

import controller.DataDrivenController;

public class MainView {
    private JFrame mainFrame;
    private JFrame fileFrame;
    private JLabel labelJsontoExcel, labelExceltoData, labelJson, labelOutput, labelUserName, labelPassword;
    private JTextField pathAreaJsontoExcel, pathAreaExceltoData, pathAreaJson, pathAreaOutputFolder, userNameField, passwordField;
    private JTextArea progress;
    private JButton browseJsontoExcel, browseExceltoData, browseJson, browseOutputFolder;
    private JButton convert, loadDefault;
    private JScrollPane scrollPane;
    private DataDrivenController controller;

    public MainView(DataDrivenController controller) {
        this.controller = controller;
        buildComponents();
    }

    private void buildComponents() {
        mainFrame = new JFrame("Data Driven FrameWork");
        fileFrame = new JFrame("Select File From Here");
        mainFrame.setContentPane(new JLabel(new ImageIcon("src/main/resources/Images/background_dark.png")));

        labelJsontoExcel = createJLabel("JSON XML Mapping",new Rectangle(30, 100, 150, 20));

        labelExceltoData = createJLabel("Excel Input",new Rectangle(30, 170, 150, 20));

        labelJson = createJLabel("Default JSON", new Rectangle(30, 240, 150, 20));

        labelOutput = createJLabel("Output Folder", new Rectangle(30, 310, 150, 20));

        labelUserName = createJLabel("User Name", new Rectangle(30, 380, 150, 20));

        labelPassword = createJLabel("User Name", new Rectangle(380, 380, 150, 20));

        pathAreaJsontoExcel = new JTextField();
        pathAreaJsontoExcel.setBounds(180, 100, 400, 20);
        pathAreaJsontoExcel.setLayout(null);
        pathAreaJsontoExcel.setVisible(true);
        pathAreaJsontoExcel.setBackground(Color.darkGray);
        pathAreaJsontoExcel.setForeground(Color.WHITE);
        pathAreaJsontoExcel.setBorder(null);

        pathAreaExceltoData = new JTextField();
        pathAreaExceltoData.setBounds(180, 170, 400, 20);
        pathAreaExceltoData.setLayout(null);
        pathAreaExceltoData.setVisible(true);
        pathAreaExceltoData.setBackground(Color.darkGray);
        pathAreaExceltoData.setForeground(Color.WHITE);
        pathAreaExceltoData.setBorder(null);

        pathAreaJson = new JTextField();
        pathAreaJson.setBounds(180, 240, 400, 20);
        pathAreaJson.setLayout(null);
        pathAreaJson.setVisible(true);
        pathAreaJson.setBackground(Color.darkGray);
        pathAreaJson.setForeground(Color.WHITE);
        pathAreaJson.setBorder(null);

        pathAreaOutputFolder = new JTextField();
        pathAreaOutputFolder.setBounds(180, 310, 400, 20);
        pathAreaOutputFolder.setLayout(null);
        pathAreaOutputFolder.setVisible(true);
        pathAreaOutputFolder.setBackground(Color.darkGray);
        pathAreaOutputFolder.setForeground(Color.WHITE);
        pathAreaOutputFolder.setBorder(null);

        userNameField = new JTextField();
        userNameField.setBounds(180, 380, 150, 20);
        userNameField.setLayout(null);
        userNameField.setVisible(true);
        userNameField.setBackground(Color.darkGray);
        userNameField.setForeground(Color.WHITE);
        userNameField.setBorder(null);

        passwordField = new JTextField();
        passwordField.setBounds(550, 380, 150, 20);
        passwordField.setLayout(null);
        passwordField.setVisible(true);
        passwordField.setBackground(Color.darkGray);
        passwordField.setForeground(Color.WHITE);
        passwordField.setBorder(null);

        browseJsontoExcel = new JButton("Browse");
        browseJsontoExcel.setBackground(Color.GRAY);
        browseJsontoExcel.setBounds(600, 100, 100, 20);
        browseJsontoExcel.setLayout(null);
        browseJsontoExcel.setVisible(true);
        browseJsontoExcel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JFileChooser fileChooser = new JFileChooser("..");
                int result = fileChooser.showSaveDialog(fileFrame);
                fileChooser.setVisible(true);
                fileFrame.add(fileChooser);
                fileFrame.setVisible(true);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    String path = file.getAbsolutePath();
                    pathAreaJsontoExcel.setText(path);
                    fileFrame.setVisible(false);
                } else if (result == JFileChooser.CANCEL_OPTION) {
                    fileFrame.setVisible(false);
                }
            }
        });

        browseExceltoData = new JButton("Browse");
        browseExceltoData.setBackground(Color.GRAY);
        browseExceltoData.setBounds(600, 170, 100, 20);
        browseExceltoData.setLayout(null);
        browseExceltoData.setVisible(true);
        browseExceltoData.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JFileChooser fileChooser = new JFileChooser("..");
                int result = fileChooser.showSaveDialog(fileFrame);
                fileChooser.setVisible(true);
                fileFrame.add(fileChooser);
                fileFrame.setVisible(true);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    String path = file.getAbsolutePath();
                    pathAreaExceltoData.setText(path);
                    fileFrame.setVisible(false);
                } else if (result == JFileChooser.CANCEL_OPTION) {
                    fileFrame.setVisible(false);
                }
            }
        });

        browseJson = new JButton("Browse");
        browseJson.setBackground(Color.GRAY);
        browseJson.setBounds(600, 240, 100, 20);
        browseJson.setLayout(null);
        browseJson.setVisible(true);
        browseJson.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JFileChooser fileChooser = new JFileChooser("..");
                int result = fileChooser.showSaveDialog(fileFrame);
                fileChooser.setVisible(true);
                fileFrame.add(fileChooser);
                fileFrame.setVisible(true);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    String path = file.getAbsolutePath();
                    pathAreaJson.setText(path);
                    fileFrame.setVisible(false);
                } else if (result == JFileChooser.CANCEL_OPTION) {
                    fileFrame.setVisible(false);
                }
            }
        });

        browseOutputFolder = new JButton("Browse");
        browseOutputFolder.setBackground(Color.GRAY);
        browseOutputFolder.setBounds(600, 310, 100, 20);
        browseOutputFolder.setLayout(null);
        browseOutputFolder.setVisible(true);
        browseOutputFolder.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JFileChooser fileChooser = new JFileChooser("..");
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int result = fileChooser.showSaveDialog(fileFrame);
                fileChooser.setVisible(true);
                fileFrame.add(fileChooser);
                fileFrame.setVisible(true);
                if (result == JFileChooser.OPEN_DIALOG) {
                    File file = fileChooser.getSelectedFile();
                    String path = file.getAbsolutePath();
                    pathAreaOutputFolder.setText(path);
                    fileFrame.setVisible(false);
                } else if (result == JFileChooser.CANCEL_OPTION) {
                    fileFrame.setVisible(false);
                }
            }
        });

        convert = new JButton("Convert");
        convert.setBackground(Color.GRAY);
        convert.setBounds(250, 450, 100, 20);
        convert.setLayout(null);
        convert.setVisible(true);
        convert.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    if (userNameField.getText().equals("") || userNameField.getText().equals("") || userNameField.getText() == null ||
                            passwordField.getText().equals("") || passwordField.getText().equals(" ") || passwordField.getText() == null) {
                        progress.setText("Please enter Valid UserName");
                    } else {
                        controller.convertToJSON(userNameField.getText().trim(), passwordField.getText().trim());
                    }
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        });

        loadDefault = new JButton("Load Default");
        loadDefault.setBackground(Color.GRAY);
        loadDefault.setBounds(400, 450, 140, 20);
        loadDefault.setLayout(null);
        loadDefault.setVisible(true);
        loadDefault.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                pathAreaJson.setText("C:\\Users\\rajemani\\Desktop\\ddt\\OTCFI_DEFAULT.JSON");
                pathAreaExceltoData.setText("C:\\Users\\rajemani\\Desktop\\ddt\\OTCFI_Data.xlsx");
                pathAreaJsontoExcel.setText("C:\\Users\\rajemani\\Desktop\\ddt\\OTCFI_Mapping.xlsx");
                pathAreaOutputFolder.setText("C:\\Users\\rajemani\\Desktop\\ddt");
                userNameField.setText("T1SB");
                passwordField.setText("MASTER1");
            }
        });

        progress = new JTextArea();
        progress.setBackground(Color.darkGray);
        progress.setForeground(Color.WHITE);

        scrollPane = new JScrollPane(progress);
        scrollPane.setBounds(180, 520, 400, 100);
        scrollPane.setLayout(new ScrollPaneLayout());
        scrollPane.setBorder(null);
        scrollPane.setVisible(true);

        mainFrame.add(labelJsontoExcel);
        mainFrame.add(labelExceltoData);
        mainFrame.add(labelJson);
        mainFrame.add(labelOutput);
        mainFrame.add(pathAreaJsontoExcel);
        mainFrame.add(pathAreaExceltoData);
        mainFrame.add(pathAreaJson);
        mainFrame.add(pathAreaOutputFolder);
        mainFrame.add(browseJsontoExcel);
        mainFrame.add(browseExceltoData);
        mainFrame.add(browseJson);
        mainFrame.add(browseOutputFolder);
        mainFrame.add(labelUserName);
        mainFrame.add(labelPassword);
        mainFrame.add(userNameField);
        mainFrame.add(passwordField);
        mainFrame.add(convert);
        mainFrame.add(loadDefault);
        mainFrame.getContentPane().add(scrollPane);

        mainFrame.setSize(800, 700);
        mainFrame.setResizable(false);
        mainFrame.setLayout(null);
        mainFrame.setVisible(true);
        fileFrame.setSize(600, 600);
    }

    private JLabel createJLabel(String labelName, Rectangle bounds) {
        JLabel label = new JLabel(labelName);
        label.setBounds(bounds);
        label.setLayout(null);
        label.setVisible(true);
        label.setForeground(Color.WHITE);
        return label;
    }

    public JTextField getPathAreaJsontoExcel() {
        return pathAreaJsontoExcel;
    }

    public JTextField getPathAreaExceltoData() {
        return pathAreaExceltoData;
    }

    public JTextField getPathAreaJson() {
        return pathAreaJson;
    }

    public JTextArea getProgress() {
        return progress;
    }

    public JTextField getPathAreaOutputFolder() {
        return pathAreaOutputFolder;
    }
}
