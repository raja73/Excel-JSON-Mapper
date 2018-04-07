package view;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.*;

import controller.ExcelJSONMapper;
import customisation.ImageConstants;
import customisation.LNFConstants;

public class MainView implements LNFConstants, ImageConstants {
    private JFrame mainFrame, fileFrame;
    private JTextField pathAreaJsontoExcel, pathAreaExceltoData, pathAreaJson, pathAreaOutputFolder, pathAreaExtraInfo;
    private JTextArea progress;
    private ExcelJSONMapper controller;

    public MainView(ExcelJSONMapper controller) {
        this.controller = controller;
        buildComponents();
    }

    private void buildComponents() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mainFrame = new JFrame(PROJECT_NAME);
        try {
            mainFrame.setContentPane(new JLabel(new ImageIcon(BACKGROUND_IMAGE)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        fileFrame = new JFrame(FILE_CHOOSER);

        createJLabel(JSON_XML_MAPPING, new Rectangle(30, 100, 150, 20));
        createJLabel(EXCEL_INPUT, new Rectangle(30, 170, 150, 20));
        createJLabel(DEFAULT_JSON, new Rectangle(30, 240, 150, 20));
        createJLabel(OUTPUT_FOLDER, new Rectangle(30, 310, 150, 20));

        pathAreaJsontoExcel = createTextField(new Rectangle(180, 100, 400, 20));
        pathAreaExceltoData = createTextField(new Rectangle(180, 170, 400, 20));
        pathAreaJson = createTextField(new Rectangle(180, 240, 400, 20));
        pathAreaOutputFolder = createTextField(new Rectangle(180, 310, 400, 20));
        pathAreaExtraInfo = createTextField(new Rectangle(180, 380, 400, 20));

        createButton(new Rectangle(600, 100, 100, 20), pathAreaJsontoExcel);
        createButton(new Rectangle(600, 170, 100, 20), pathAreaExceltoData);
        createButton(new Rectangle(600, 240, 100, 20), pathAreaJson);
        createButton(new Rectangle(600, 310, 100, 20), pathAreaOutputFolder);
        createButton(new Rectangle(600, 310, 100, 20), pathAreaExtraInfo);

        JButton convert = new JButton("Convert");
        convert.setBackground(Color.GRAY);
        convert.setBounds(250, 450, 100, 20);
        convert.setLayout(null);
        convert.setVisible(true);
        convert.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                controller.convertToJSON();
            }
        });

        JButton loadDefault = new JButton("Load Default");
        loadDefault.setBackground(Color.GRAY);
        loadDefault.setBounds(400, 450, 140, 20);
        loadDefault.setLayout(null);
        loadDefault.setVisible(true);
        loadDefault.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                pathAreaJson.setText("");
                pathAreaExceltoData.setText("");
                pathAreaJsontoExcel.setText("");
                pathAreaOutputFolder.setText("");
            }
        });

        progress = new JTextArea();
        progress.setBackground(Color.darkGray);
        progress.setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(progress);
        scrollPane.setBounds(180, 520, 400, 100);
        scrollPane.setLayout(new ScrollPaneLayout());
        scrollPane.setBorder(null);
        scrollPane.setVisible(true);

        mainFrame.getContentPane().add(scrollPane);

        mainFrame.setSize(800, 700);
        mainFrame.setResizable(false);
        mainFrame.setLayout(null);
        mainFrame.setVisible(true);
        fileFrame.setSize(600, 600);
    }

    private void createJLabel(String labelName, Rectangle bounds) {
        JLabel label = new JLabel(labelName);
        label.setBounds(bounds);
        label.setLayout(null);
        label.setVisible(true);
        label.setForeground(Color.WHITE);
        mainFrame.add(label);
    }

    private JTextField createTextField(Rectangle bounds) {
        JTextField textArea = new JTextField();
        textArea.setBounds(bounds);
        textArea.setLayout(null);
        textArea.setVisible(true);
        textArea.setBackground(Color.darkGray);
        textArea.setForeground(Color.WHITE);
        textArea.setBorder(null);
        mainFrame.add(textArea);
        return textArea;
    }

    private void createButton(Rectangle bounds, JTextField textField) {
        JButton button = new JButton("Browse");
        button.setBackground(Color.GRAY);
        button.setBounds(bounds);
        button.setLayout(null);
        button.setVisible(true);
        button.addMouseListener(new MouseAdapter() {
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
                    textField.setText(path);
                    fileFrame.setVisible(false);
                } else if (result == JFileChooser.CANCEL_OPTION) {
                    fileFrame.setVisible(false);
                }
            }
        });
        mainFrame.add(button);
    }

    public String getPathAreaJsontoExcelText() {
        return pathAreaJsontoExcel.getText();
    }

    public String getPathAreaExceltoDataText() {
        return pathAreaExceltoData.getText();
    }

    public String getPathAreaJsonText() {
        return pathAreaJson.getText();
    }

    public JTextArea getProgress() {
        return progress;
    }

    public String getPathAreaOutputFolderText() {
        return pathAreaOutputFolder.getText();
    }

    public String getPathAreaExtraInfoText() {
        return pathAreaExtraInfo.getText();
    }
}