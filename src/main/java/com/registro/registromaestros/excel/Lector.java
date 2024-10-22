package com.registro.registromaestros.excel;

import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Lector {

    public List<String> readExcelFromResources() {
        List<String> lista = new ArrayList<>();

        // Ajustar el límite de compresión (ajustar según sea necesario)
        ZipSecureFile.setMinInflateRatio(0.001); // Reducir el límite de seguridad

        // Usamos getClass().getResourceAsStream para acceder al archivo dentro del classpath
        try (InputStream file = getClass().getResourceAsStream("/document/MESAS DE TRABAJO DELEGADOS.xlsx");
             Workbook workbook = new XSSFWorkbook(file)) {

            for (int sheetIndex = 0; sheetIndex < workbook.getNumberOfSheets(); sheetIndex++) {
                Sheet sheet = workbook.getSheetAt(sheetIndex);

                // Definir la fila y la columna de inicio
                int startRow = 9; // Fila 10
                int startColumn = 5; // Columna F (índice f)

                // Recorrer desde la fila 8 hasta la última fila de la hoja
                for (int rowIndex = startRow; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                    Row row = sheet.getRow(rowIndex);
                    if (row != null) {
                        // Obtener la celda en la columna G (índice 6)
                        Cell cell = row.getCell(startColumn);
                        if (cell != null) {
                            // Obtener el valor de la celda y agregarlo a la lista
                            lista.add(getCellValue(cell));
                        }
                    }
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    // Método para obtener el valor de la celda como String
    private String getCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case NUMERIC:
                return BigDecimal.valueOf(cell.getNumericCellValue()).toPlainString();
            default:
                return "";
        }
    }

}
