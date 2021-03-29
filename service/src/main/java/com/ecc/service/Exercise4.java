package com.ecc.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.function.Supplier;

import java.util.stream.Collectors;
import java.util.stream.Stream;


import com.ecc.model.ModelCell;


public class Exercise4 {

    private final char INNER_DELIMITER = ':';
    private final char OUTER_DELIMITER = ',';

    private List<List<Optional<ModelCell>>> container;
    private String path;

    public Exercise4(String filename) {
        this(new File(filename));
    }

    public Exercise4(File path) {
        this.path = path.getAbsolutePath();
        container = generateTable(path);

    }

    public void insertTableToFile() throws IOException {
        FileWriter fileWriter = new FileWriter(new File(this.path));
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        StringBuilder sb = new StringBuilder();
        this.container.stream().forEach(x -> {
            String perRow = x.stream().map(cell -> cell.get().getLeft() + INNER_DELIMITER + cell.get().getRight())
                    .collect(Collectors.joining(OUTER_DELIMITER + ""));

            sb.append(perRow + "\n");
        });

        bufferedWriter.write(sb.toString());
        bufferedWriter.flush();

    }

    public void sortRow(boolean isAscending, int row) {

        Map<Boolean, List<Optional<ModelCell>>> presentValue = this.container.get(row).stream()
                .collect(Collectors.partitioningBy(obj -> obj.isPresent()));

        List<Optional<ModelCell>> sortedValue = presentValue.get(true).stream().map(obj -> obj.get())
                .sorted(isAscending ? Comparator.naturalOrder() : Comparator.reverseOrder())
                .map(obj -> Optional.of(obj)).collect(Collectors.toList());

        this.container.set(row, sortedValue);
    }

    public List<Optional<ModelCell>> addRow() {
        List<Optional<ModelCell>> container = new ArrayList<>();

        for (int i = 0; i < getInnerColumnSize(); i++) {
            container.add(Optional.of(new ModelCell(getRandomString(), getRandomString())));
        }
        if (!container.isEmpty()) {
            this.container.add(container);
        }

        return container;

    }

    private List<Optional<ModelCell>> getRowValue(String text) {
        List<Optional<ModelCell>> container = new ArrayList<>();

        String[] stringContainer = text.split(",");
        if (stringContainer.length <= 0) {
            container.add(Optional.of(new ModelCell("", "")));

            return container;
        }
        for (String value : stringContainer) {
            String[] innerCell = value.split((":"));

            if (innerCell.length < 2) {
                if (innerCell[0].isEmpty()) {
                    container.add(Optional.of(new ModelCell("null", innerCell[1])));
                } else if (innerCell[1].isEmpty()) {
                    container.add(Optional.of(new ModelCell(innerCell[0], "")));
                }
            } else if (innerCell.length == 2) {
                container.add(Optional.of(new ModelCell(innerCell[0], innerCell[1])));
            } else {
                container.add(Optional.of(new ModelCell("", "")));
            }
        }

        return container;
    }

    public void displayTable() {

        for (int i = 0; i < container.size(); i++) {
            List<Optional<ModelCell>> columnCells = this.container.get(i);

            for (int j = 0; j < columnCells.size(); j++) {
                Optional<ModelCell> obj = columnCells.get(j);
                if (obj.isPresent()) {
                    System.out.printf("%s:%s", obj.get().getLeft(), obj.get().getRight());
                }

                if (j < container.get(i).size() - 1) {
                    System.out.print(OUTER_DELIMITER);
                }
            }

            System.out.println();
        }
    }

    public void resetTable(int row, int column) {
        Supplier<List<Optional<ModelCell>>> fillTable = () -> {
            return Stream.generate(() -> {
                return Optional.of(new ModelCell(getRandomString(), getRandomString()));
            }).limit(column).collect(Collectors.toList());
        };

        this.container = Stream.generate(fillTable).limit(row).collect(Collectors.toList());

    }

    public List<List<Optional<ModelCell>>> generateTable(File filepath) {
        List<List<Optional<ModelCell>>> container = new ArrayList<>();
        try {
            String line = "";
            BufferedReader fileReader = new BufferedReader(new FileReader(filepath));

            while ((line = fileReader.readLine()) != null) {

                container.add(getRowValue(line));
            }
            fileReader.close();

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return container;

    }

    public String getRandomString() {
        StringBuilder stringBuilder = new StringBuilder();

        Random randomInteger = new Random();
        int ascii = 0;
        for (int i = 0; i < 3; i++) {
            do {
                {
                    ascii = randomInteger.nextInt(127 - 32) + 32;
                }
            } while (ascii == INNER_DELIMITER || ascii == OUTER_DELIMITER);

            stringBuilder.append((char) ascii);

        }

        return stringBuilder.toString();
    }

    public int getRowNumber() {
        return this.container.size();
    }

    public int getInnerColumnSize() {
        return getRowNumber() > 0 ? this.container.get(0).size() : 0;
    }

    private int numberOfOccurrences(String text, String toSearch) {
        int occurrences = 0;
        for (int y = 0; y < text.length(); y++) {
            occurrences += toSearch.length() + y <= text.length()
                    && text.substring(y, toSearch.length() + y).equals(toSearch) ? 1 : 0;
        }

        return occurrences > 0 ? occurrences : 0;
    }

    //

    private void printKeyField(String toSearch, int rowIndex, int cellIndex, int numberOfOccurrences) {

        System.out.printf("Found %s on (%d,%d) with %d instances on key field \n", toSearch, rowIndex, cellIndex,
                numberOfOccurrences);

    }

    private void printValueField(String toSearch, int rowIndex, int cellIndex, int numberOfOccurrences) {

        System.out.printf("Found %s on (%d,%d) with %d instances on value field \n", toSearch, rowIndex, cellIndex,
                numberOfOccurrences);

    }

    public void stringSearch(String toSearch) {

        for (int i = 0; i < getRowNumber(); i++) {
            for (int j = 0; j < getInnerColumnSize(); j++) {
                int left = numberOfOccurrences(this.container.get(i).get(j).get().getLeft(), toSearch);
                int right = numberOfOccurrences(this.container.get(i).get(j).get().getRight(), toSearch);

                // int left =
                // StringUtils.countMatches(this.container.get(i).get(j).get().getLeft(),
                // toSearch);
                // int right =
                // StringUtils.countMatches(this.container.get(i).get(j).get().getLeft(),
                // toSearch);

                if (left > 0) {
                    printKeyField(toSearch, i, j, left);
                }

                if (right > 0) {
                    printValueField(toSearch, i, j, right);
                }
            }

            System.out.println("");
        }
    }

    private boolean isRowWithinSize(int row) {
        return row <= getRowNumber();
    }

    private boolean isColumnWithinSize(int column) {
        return column <= getInnerColumnSize();
    }

    public void editCellValue(boolean isLeft, int row, int column, String newValue) {
        Optional data = this.container.get(row).get(column);

        if (isRowWithinSize(row) && isColumnWithinSize(column) && data.isPresent()) {
            if (isLeft) {
                this.container.get(row).get(column).get().setLeft(newValue);
            } else {
                this.container.get(row).get(column).get().setRight(newValue);
            }
        } else {
            System.out.println("Invalid row/column value");

        }

    }

}