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


public class DataDrivenController {
    private int numberOfSheets = 0;
    private List<String> columnsJsonExcel = new ArrayList<>();
    private List<String> columnsCollectionExcel = new ArrayList<>();
    private List<String> columnsExcelData = new ArrayList<>();
    private MainView mainView;
    private String userName, passWord;
    private JsonObject object;
    private Map<String, List<Map<String, String>>> mapCollections = null;
    private Map<String, String> mapXmlTags;
    private Set<String> collectionOccurred;

    public DataDrivenController() {
        mainView = new MainView(this);
    }

    private List<Map<String, String>> excelToMapRows(File file) {
        List<Map<String, String>> mapList = new ArrayList<>();
        columnsExcelData = new ArrayList<String>();
        int cellType = 0;
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
                        int columnCount = 0;
                        Map<String, String> map = new HashMap<>();
                        if (countRow == 0) {
                            Row row = rowIterator.next();
                            Iterator<Cell> cellIterator = row.iterator();
                            while (cellIterator.hasNext()) {
                                Cell cell = cellIterator.next();
                                columnsExcelData.add(cell.getStringCellValue());
                            }
                        } else {
                            Row row = rowIterator.next();
                            Iterator<org.apache.poi.ss.usermodel.Cell> cellIterator = row.iterator();
                            while (cellIterator.hasNext()) {
                                Cell cell = cellIterator.next();
                                String strValue = cell.toString();
                                if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                                    boolean convertToString = CheckForNumber(cell.toString());
                                    if (convertToString) {
                                        cell.setCellType(Cell.CELL_TYPE_STRING);
                                        strValue = cell.toString();
                                    } else {
                                        strValue = cell.toString();
                                        cell.setCellType(Cell.CELL_TYPE_STRING);
                                    }
                                }
                                cellType = cell.getCellType();
                                try {
                                    switch (cellType) {
                                        case 0:
                                            map.put(columnsExcelData.get(cell.getColumnIndex()), String.valueOf(cell.getNumericCellValue()));
                                            columnCount++;
                                            break;
                                        case 1:
                                            map.put(columnsExcelData.get(cell.getColumnIndex()), strValue);
                                            columnCount++;
                                            break;
                                        case 4:
                                            map.put(columnsExcelData.get(cell.getColumnIndex()), String.valueOf(cell.getBooleanCellValue()));
                                            columnCount++;
                                            break;
                                        default:
                                            map.put(columnsExcelData.get(cell.getColumnIndex()), null);
                                            columnCount++;
                                            break;
                                    }
                                } catch (Exception e2) {
                                    map.put(columnsExcelData.get(cell.getColumnIndex()), null);
                                    columnCount++;
                                }
                            }
                        }
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

    private boolean CheckForNumber(String numericCellValue) {
        if (numericCellValue.contains(".")) {
            return true;
        } else {
            return false;
        }
    }

    private Map<String, String> excelToMapColumns(File file) {
        Map<String, String> map = new HashMap<>();
        List<String> columnNames = new ArrayList<String>();
        int cellType = 0;
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
                        if (countRow == 0) {
                            Row row = rowIterator.next();
                            Iterator<Cell> cellIterator = row.iterator();
                            while (cellIterator.hasNext()) {
                                Cell cell = cellIterator.next();
                                columnNames.add(cell.getStringCellValue());
                            }
                        } else {
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

    public void convertToJSON(String userName, String passWord) throws InterruptedException {
        this.userName = userName;
        this.passWord = passWord;
        File exceltoDataFile = new File(mainView.getPathAreaExceltoData().getText());
        File jsontoXMLFile = new File(mainView.getPathAreaJsontoExcel().getText());
        List<Map<String, String>> mapMainData;
        mapMainData = excelToMapRows(exceltoDataFile);
        mapXmlTags = excelToMapColumns(jsontoXMLFile);
        mapCollections = new HashMap<>();
        mapCollections = excelToMapCollections(exceltoDataFile);
        for (Map p : mapMainData) {
            convertEachData(p);
            Thread.sleep(5000);
        }
    }

    public void convertEachData(Map<String, String> mapED) {
        File jsonToExcelFile = new File(mainView.getPathAreaJsontoExcel().getText());
        File json = new File(mainView.getPathAreaJson().getText());

        object = jsonToObject(json);
        JsonArray jArray;

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
                            Iterator<Cell> cellIterator = row.iterator();
                            while (cellIterator.hasNext()) {
                                org.apache.poi.ss.usermodel.Cell cell = cellIterator.next();
                                columnsJsonExcel.add(cell.getStringCellValue());
                            }
                        } else {
                            Row row = rowIterator.next();
                            Iterator<org.apache.poi.ss.usermodel.Cell> cellIterator = row.iterator();
                            Cell cell = cellIterator.next();
                            String jsonString = cell.getStringCellValue();
                            cell = cellIterator.next();
                            String excelString = cell.getStringCellValue();
                            String[] jsonStrings;
                            jsonStrings = jsonString.split("\\.");
                            String value = null;

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
                                        ((object.getAsJsonObject(jsonStrings[1])).getAsJsonObject(jsonStrings[2])).addProperty(jsonStrings[3], value);
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

    private JsonArray GetCollectionArray(List<Map<String, String>> maps) {
        JsonArray jArray = new JsonArray();
        if (maps != null) {
            for (Map<String, String> m : maps) {
                JsonObject jObj = new JsonObject();
                Set<String> keys = m.keySet();
                Iterator keysIterator = keys.iterator();
                while (keysIterator.hasNext()) {
                    String key = keysIterator.next().toString();
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


    public JsonObject jsonToObject(File file) {
        JsonParser parser = new JsonParser();
        try {
            JsonObject obj = (JsonObject) parser.parse(new FileReader(file));
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private File objectToJson(JsonObject object, String fileName) throws Exception {
        mainView.getProgress().append("Creating JSON from JSON Object\n");
        mainView.getProgress().setText(mainView.getProgress().getText());
        String filePath = mainView.getPathAreaOutputFolder().getText();
        File file = new File(filePath + "/" + fileName + ".json");
        try (FileWriter inputStream = new FileWriter(file);
             BufferedWriter bufferStream = new BufferedWriter(inputStream);
             PrintWriter out = new PrintWriter(bufferStream)) {
            StringBuilder sb = new StringBuilder();
            out.print("{\"headerInformation\": {\"userID\": \"" + userName + "\",\"password\": \"" + passWord + "\"},\"inputParameters\": {\"inputParameter\": [{\"name\": \"dealobject\",\"value\": ");
            out.print(object.toString());
            out.print(" ,\"isBodyParameter\": true,\"isRouteParameter\": false}]},\"outputObject\": ");
            out.print(object.toString());
            out.print(" }");
        }
        mainView.getProgress().append("Output File is " + fileName + ".json\n");
        mainView.getProgress().setText(mainView.getProgress().getText());
        return file;
    }

    private Map<String, List<Map<String, String>>> excelToMapCollections(File file) {
        columnsCollectionExcel = new ArrayList<String>();
        int cellType = 0;
        try {
            FileInputStream inputStream = new FileInputStream(file);
            Workbook workbook = new XSSFWorkbook(inputStream);
            numberOfSheets = workbook.getNumberOfSheets();
            String testName = "";

            if (numberOfSheets > 1) {
                Sheet sheet = workbook.getSheetAt(1);
                Iterator<Row> rowIterator = sheet.iterator();
                int countRow = 0;
                try {
                    while (rowIterator.hasNext()) {
                        int columnCount = 0;
                        Map<String, String> map = new HashMap<>();
                        if (countRow == 0) {
                            Row row = rowIterator.next();
                            Iterator<Cell> cellIterator = row.iterator();
                            while (cellIterator.hasNext()) {
                                Cell cell = cellIterator.next();
                                columnsCollectionExcel.add(cell.getStringCellValue());
                            }
                        } else {
                            Row row = rowIterator.next();
                            Iterator<org.apache.poi.ss.usermodel.Cell> cellIterator = row.iterator();
                            while (cellIterator.hasNext()) {
                                Cell cell = cellIterator.next();
                                String strValue = cell.toString();
                                if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                                    boolean convertToString = CheckForNumber(cell.toString());
                                    if (convertToString) {
                                        cell.setCellType(Cell.CELL_TYPE_STRING);
                                        strValue = cell.toString();
                                    } else {
                                        strValue = cell.toString();
                                        cell.setCellType(Cell.CELL_TYPE_STRING);
                                    }
                                }
                                cellType = cell.getCellType();
                                try {
                                    switch (cellType) {
                                        case 0:
                                            map.put(columnsCollectionExcel.get(cell.getColumnIndex()), String.valueOf(cell.getNumericCellValue()));
                                            columnCount++;
                                            break;
                                        case 1:
                                            map.put(columnsCollectionExcel.get(cell.getColumnIndex()), strValue);
                                            columnCount++;
                                            break;
                                        case 4:
                                            map.put(columnsCollectionExcel.get(cell.getColumnIndex()), String.valueOf(cell.getBooleanCellValue()));
                                            columnCount++;
                                            break;
                                        default:
                                            map.put(columnsCollectionExcel.get(cell.getColumnIndex()), null);
                                            columnCount++;
                                            break;
                                    }
                                } catch (Exception e2) {
                                    map.put(columnsCollectionExcel.get(cell.getColumnIndex()), null);
                                    columnCount++;
                                }
                            }
                        }
                        if (countRow != 0) {
                            testName = map.get(columnsCollectionExcel.get(0));
                            if (mapCollections.containsKey(testName)) {
                                map.remove(columnsCollectionExcel.get(0));
                                mapCollections.get(testName).add(map);
                            } else {
                                map.remove(columnsCollectionExcel.get(0));
                                List list = new ArrayList();
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

    public static void main(String[] args) {
        DataDrivenController controller = new DataDrivenController();
    }
}
