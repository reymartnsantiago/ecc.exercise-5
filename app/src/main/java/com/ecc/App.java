package com.ecc;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Optional;
import java.util.Scanner;
import java.net.URL;

import java.io.UnsupportedEncodingException;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.ecc.utility.Utility;

import com.ecc.service.Exercise4;

public class App {
    private Utility utility;
    private Scanner sc;
    private Exercise4 obj;
    private static final int OPTION_SEARCH = 1;
    private static final int OPTION_EDIT = 2;
    private static final int OPTION_RESET = 3;
    private static final int OPTION_PRINT = 4;
    private static final int OPTION_ADDROW = 5;
    private static final int OPTION_SORTROW = 6;
    private static final int OPTION_EXIT = 7;

    public App() {

        this.sc = new Scanner(System.in);

    }

    private void displayFileSelection() {
        System.out.println("-----------------------------------------");
        System.out.println("EXIST CODE CAMP - Exercise 2");
        System.out.println("");
        System.out.println("Press 1 - to select a file");
        System.out.println("Press 2 - to select the default file");

    }

    private void displaySelection() {

        System.out.println("Selection: ");
        System.out.println("1 - Search String");
        System.out.println("2 - Edit Cell Value");
        System.out.println("3 - Reset Table");
        System.out.println("4 - Print Table");
        System.out.println("5 - Add Row");
        System.out.println("6 - Sort Row");
        System.out.println("7 - Exit");
    }

    public boolean getSelectedFile() {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text file", "txt");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                obj = new Exercise4(chooser.getSelectedFile());
            } catch (Exception e) {
                e.printStackTrace();
                return false;

            }
        } else {
            System.out.println("File chooser is cancelled. Parsing the default file");
            return false;
        }
        return true;
    }

    private int getFileOption() {

        return Utility.getIntegerInput();
    }

    public void getDefaultFile() {

        Optional<String> getFile = getResourcePath("table.txt");

        if (!getFile.isPresent()) {
            System.out.println("File is not available");
            System.exit(0);
        } else {
            this.obj = new Exercise4(getFile.get());
        }

    }

    public static Optional<String> getResourcePath(String resource) {
        Optional<URL> resourceURL = Optional.ofNullable(App.class.getClassLoader().getResource(resource));

        Optional<String> resourcePath = Optional.empty();

        if (resourceURL.isPresent()) {
            try {
                return Optional.of(URLDecoder.decode(resourceURL.get().getPath(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                return resourcePath;
            }
        }

        return resourcePath;
    }

    public void sort() {
        int row = Utility.getIntegerInput("Input a row value: ");
        boolean isAscending = Utility.isAscending();
        this.obj.sortRow(isAscending, row);
    }

    public void start() throws IOException {

        displayFileSelection();
        if (getFileOption() == 1) {
            if (!getSelectedFile()) {
                getDefaultFile();
            }
        }
        if (getFileOption() == 2) {
            getDefaultFile();
            this.obj.displayTable();
        }

        while (true) {

            displaySelection();
            switch (Utility.getIntegerInput("Select from 1-7: ")) {
            case OPTION_SEARCH:
                stringSearch();

                break;
            case OPTION_EDIT:
                editCell();
                storeValue();
                displayTable();

                break;
            case OPTION_RESET:
                resetTable();
                storeValue();
                displayTable();

                break;
            case OPTION_PRINT:
                displayTable();
                break;
            case OPTION_ADDROW:
                addRow();
                storeValue();
                displayTable();
                break;
            case OPTION_SORTROW:
                sort();
                storeValue();
                displayTable();
                break;
            case OPTION_EXIT:
                System.exit(0);
                break;
            default:
                System.out.println("Invalid selection.\nProgram will now terminate");
                System.exit(0);

            }
        }
    }

    public void addRow() {
        obj.addRow();
    }

    public void stringSearch() {

        obj.stringSearch(Utility.getStringValue("Enter any string: "));
    }

    public void storeValue() throws IOException {
        obj.insertTableToFile();
    }

    public void editCell() {
        int row = Utility.getIntegerInput("Input row value: ");
        int column = Utility.getIntegerInput("Input column value: ");
        int cellLocation = Utility.getIntegerInput("Press [1] Left Cell -- [2] Right Cell: ");
        String newValue = Utility.getStringValue("Input new string value: ");
        try {

            this.obj.editCellValue(cellLocation == 1 ? true : false, row, column, newValue);

        } catch (IndexOutOfBoundsException e) {
            System.out.println("Index out of bounds.\nInvalid row/column value. ");

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }

    }

    public void resetTable() {

        try {

            int row = Utility.getIntegerInput("Input number of row: ");
            int column = Utility.getIntegerInput("Input number of column: ");
            obj.resetTable(row, column);

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }

    }

    public void displayTable() {
        this.obj.displayTable();
    }


}
