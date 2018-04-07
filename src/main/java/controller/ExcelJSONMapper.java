package controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import view.MainView;


public class ExcelJSONMapper {
    private int numberOfSheets = 0;
    private List<String> columnsJsonExcel = new ArrayList<>();
    private List<String> columnsExcelData = new ArrayList<>();
    private MainView mainView;
    private Map<String, List<Map<String, String>>> mapCollections = null;
    private Map<String, String> mapXmlTags;
    private Set<String> collectionOccurred;

    private ExcelJSONMapper() {
        mainView = new MainView(this);
    }

    private List<Map<String, String>> excelToMapRows(File file) {
        List<Map<String, String>> mapList = new ArrayList<>();
        columnsExcelData = new ArrayList<>();
        try {
            FileInputStream inputStream = new FileInputStream(file);
            //creating a workbook object y reading the excel
            Workbook workbook = new XSSFWorkbook(inputStream);
            numberOfSheets = workbook.getNumberOfSheets();
            for (int i = 0; i < 1; i++) {
                Sheet sheet = workbook.getSheetAt(i);
                Iterator<Row> rowIterator = sheet.iterator();
                int countRow = 0;
                try {
                    while (rowIterator.hasNext()) {
                        Map<String, String> map = new HashMap<>();
                        placeEntryToMap(map, columnsExcelData, countRow, rowIterator);
                        if (countRow != 0) {
                            mapList.add(map);
                        }
                        countRow++;

                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mapList;
    }

    private String checkForNumber(Cell cell) {
        String strValue = cell.toString();
        if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            boolean convertToString = strValue.contains(".");
            if (convertToString) {
                cell.setCellType(Cell.CELL_TYPE_STRING);
                strValue = cell.toString();
            } else {
                strValue = cell.toString();
                cell.setCellType(Cell.CELL_TYPE_STRING);
            }
        }
        return strValue;
    }

    private Map<String, String> excelToMapColumns(File file) {
        Map<String, String> map = new HashMap<>();
        try {
            FileInputStream inputStream = new FileInputStream(file);
            Workbook workbook = new XSSFWorkbook(inputStream);
            numberOfSheets = workbook.getNumberOfSheets();
            for (int i = 0; i < 1; i++) {
                Sheet sheet = workbook.getSheetAt(i);
                Iterator<Row> rowIterator = sheet.iterator();
                int countRow = 0;
                try {
                    while (rowIterator.hasNext()) {
                        if (countRow != 0) {
                            Row row = rowIterator.next();
                            Iterator<org.apache.poi.ss.usermodel.Cell> cellIterator = row.iterator();
                            Cell cell1 = cellIterator.next();
                            Cell cell2 = cellIterator.next();
                            map.put(cell2.getStringCellValue(), cell1.getStringCellValue());
                        }
                        countRow++;
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    public void convertToJSON() {
        File exceltoDataFile = new File(mainView.getPathAreaExceltoDataText());
        File jsontoXMLFile = new File(mainView.getPathAreaJsontoExcelText());
        List<Map<String, String>> mapMainData;
        mapMainData = excelToMapRows(exceltoDataFile);
        mapXmlTags = excelToMapColumns(jsontoXMLFile);
        mapCollections = new HashMap<>();
        mapCollections = excelToMapCollections(exceltoDataFile);
        for (Map<String, String> p : mapMainData) {
            convertEachData(p);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void convertEachData(Map<String, String> mapED) {
        File jsonToExcelFile = new File(mainView.getPathAreaJsontoExcelText());
        File json = new File(mainView.getPathAreaJsonText());

        JsonObject object = jsonToObject(json);
        JsonArray jArray;

        if (object != null) {
            try {
                FileInputStream inputStream = new FileInputStream(jsonToExcelFile);
                Workbook workbook = new XSSFWorkbook(inputStream);
                numberOfSheets = workbook.getNumberOfSheets();
                collectionOccurred = new HashSet<>();
                for (int i = 0; i < numberOfSheets; i++) {
                    Sheet sheet = workbook.getSheetAt(i);
                    Iterator<Row> rowIterator = sheet.iterator();
                    int countRow = 0;
                    // iterates through rows in the current sheet
                    try {
                        while (rowIterator.hasNext()) {
                            if (countRow == 0) {
                                Row row = rowIterator.next();
                                row.forEach(cell -> columnsJsonExcel.add(cell.getStringCellValue()));
                            } else {
                                Row row = rowIterator.next();
                                Iterator<org.apache.poi.ss.usermodel.Cell> cellIterator = row.iterator();
                                Cell cell = cellIterator.next();
                                String jsonString = cell.getStringCellValue();
                                cell = cellIterator.next();
                                String excelString = cell.getStringCellValue();
                                String[] jsonStrings;
                                jsonStrings = jsonString.split("\\.");
                                String value;

                                if (jsonStrings.length == 2) {
                                    value = mapED.get(excelString);
                                    if (value != null)
                                        object.addProperty(jsonStrings[1], value);
                                } else if (jsonStrings.length == 3) {
                                    if (object.get(jsonStrings[1]) instanceof JsonArray) {
                                        if (!collectionOccurred.contains(jsonStrings[1])) {
                                            jArray = GetCollectionArray(mapCollections.get(mapED.get(columnsExcelData.get(0))));
                                            object.add(jsonStrings[1], jArray);
                                        }
                                    } else {
                                        value = mapED.get(excelString);
                                        if (value != null)
                                            (object.getAsJsonObject(jsonStrings[1])).addProperty(jsonStrings[2], value);
                                    }
                                } else if (jsonStrings.length == 4) {
                                    if (object.getAsJsonObject(jsonStrings[1]).get(jsonStrings[2]) instanceof JsonArray) {
                                        if (!collectionOccurred.contains(jsonStrings[2])) {
                                            jArray = GetCollectionArray(mapCollections.get(mapED.get(columnsExcelData.get(0))));
                                            object.getAsJsonObject(jsonStrings[1]).add(jsonStrings[2], jArray);
                                        }
                                    } else {
                                        value = mapED.get(excelString);
                                        if (value != null)
                                            ((object.getAsJsonObject(jsonStrings[1])).getAsJsonObject(jsonStrings[2]))
                                                    .addProperty(jsonStrings[3], value);
                                    }
                                } else if (jsonStrings.length == 5) {
                                    if (object.getAsJsonObject(jsonStrings[1]).getAsJsonObject(jsonStrings[2]).get(jsonStrings[3]) instanceof JsonArray) {
                                        if (!collectionOccurred.contains(jsonStrings[3])) {
                                            jArray = GetCollectionArray(mapCollections.get(mapED.get(columnsExcelData.get(0))));
                                            object.getAsJsonObject(jsonStrings[1]).getAsJsonObject(jsonStrings[2]).add(jsonStrings[3], jArray);
                                        }
                                    } else {
                                        value = mapED.get(excelString);
                                        if (value != null)
                                            ((object.getAsJsonObject(jsonStrings[1])).getAsJsonObject(jsonStrings[2])).getAsJsonObject(jsonStrings[3]).addProperty(jsonStrings[4], value);
                                    }
                                }
                            }
                            countRow++;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                objectToJson(object, mapED.get(columnsExcelData.get(0)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private JsonArray GetCollectionArray(List<Map<String, String>> maps) {
        JsonArray jArray = new JsonArray();
        if (maps != null) {
            for (Map<String, String> m : maps) {
                JsonObject jObj = new JsonObject();
                Set<String> keys = m.keySet();
                for (Object key1 : keys) {
                    String key = key1.toString();
                    if (mapXmlTags.get(key) != null) {
                        String[] names = mapXmlTags.get(key).split("\\.");
                        jObj.addProperty(names[names.length - 1], m.get(key));
                        collectionOccurred.add(names[names.length - 2]);
                    }
                }
                jArray.add(jObj);
            }
        }
        return jArray;
    }


    private JsonObject jsonToObject(File file) {
        JsonParser parser = new JsonParser();
        try {
            return (JsonObject) parser.parse(new FileReader(file));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void objectToJson(JsonObject object, String fileName) throws Exception {
        String filePath = mainView.getPathAreaOutputFolderText();
        File file = new File(filePath + "/" + fileName + ".json");
        try (FileWriter inputStream = new FileWriter(file);
             BufferedWriter bufferStream = new BufferedWriter(inputStream);
             PrintWriter out = new PrintWriter(bufferStream)) {
            out.print(object.toString());
        }
    }

    private Map<String, List<Map<String, String>>> excelToMapCollections(File file) {
        List<String> columnsCollectionExcel = new ArrayList<>();
        try {
            FileInputStream inputStream = new FileInputStream(file);
            Workbook workbook = new XSSFWorkbook(inputStream);
            numberOfSheets = workbook.getNumberOfSheets();
            String testName;

            if (numberOfSheets > 1) {
                Sheet sheet = workbook.getSheetAt(1);
                Iterator<Row> rowIterator = sheet.iterator();
                int countRow = 0;
                try {
                    while (rowIterator.hasNext()) {
                        Map<String, String> map = new HashMap<>();
                        placeEntryToMap(map, columnsCollectionExcel, countRow, rowIterator);
                        if (countRow != 0) {
                            testName = map.get(columnsCollectionExcel.get(0));
                            if (mapCollections.containsKey(testName)) {
                                map.remove(columnsCollectionExcel.get(0));
                                mapCollections.get(testName).add(map);
                            } else {
                                map.remove(columnsCollectionExcel.get(0));
                                List<Map<String, String>> list = new ArrayList<>();
                                list.add(map);
                                mapCollections.put(testName, list);
                            }
                        }
                        countRow++;
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mapCollections;
    }

    private void placeEntryToMap(Map<String, String> map, List<String> columnList, int countRow, Iterator<Row> rowIterator) {
        if (countRow == 0) {
            Row row = rowIterator.next();
            row.forEach(cell -> columnList.add(cell.getStringCellValue()));
        } else {
            Row row = rowIterator.next();
            for (Cell cell : row) {
                String strValue = checkForNumber(cell);
                int cellType = cell.getCellType();
                try {
                    switch (cellType) {
                        case 0:
                            map.put(columnList.get(cell.getColumnIndex()), String.valueOf(cell.getNumericCellValue()));
                            break;
                        case 1:
                            map.put(columnList.get(cell.getColumnIndex()), strValue);
                            break;
                        case 4:
                            map.put(columnList.get(cell.getColumnIndex()), String.valueOf(cell.getBooleanCellValue()));
                            break;
                        default:
                            map.put(columnList.get(cell.getColumnIndex()), null);
                            break;
                    }
                } catch (Exception e2) {
                    map.put(columnList.get(cell.getColumnIndex()), null);
                }
            }
        }
    }

    @SuppressWarnings("unused")
    public static void main(String[] args) {
        ExcelJSONMapper controller = new ExcelJSONMapper();
    }
}
