package com.registro.registromaestros.excel;

import com.registro.registromaestros.model.Profesor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Exportar {

    public void writeDataToExcel(ArrayList<Profesor> asistentes, String fileName) {
        // Crear un nuevo workbook (archivo Excel)
        Workbook workbook = new XSSFWorkbook();
        // Crear una nueva hoja en el workbook
        Sheet sheet = workbook.createSheet(fileName);

        String mesa = obtenerMesa(fileName);

        //Creamos el titulo
        //Agregamos las imagenes
        try {
            // Leer la imagen desde el classpath (resources/img)
            InputStream inputStream = Exportar.class.getResourceAsStream("/img/Logo.png");

            byte[] bytes = IOUtils.toByteArray(inputStream);
            inputStream.close();

            // Agregar la imagen al libro de trabajo
            int pictureIdx = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);
            CreationHelper helper = workbook.getCreationHelper();

            // Crear el dibujo donde se colocará la imagen
            Drawing<?> drawing = sheet.createDrawingPatriarch();

            // Definir el anclaje de la imagen (celda inicial y final)
            ClientAnchor anchor = helper.createClientAnchor();
            anchor.setCol1(0); // Columna de inicio (0 es A, 1 es B, etc.)
            anchor.setRow1(0); // Fila de inicio (0 es la primera fila)
            anchor.setCol2(2); // Columna final
            anchor.setRow2(5); // Fila final

            // Insertar la imagen en el dibujo
            drawing.createPicture(anchor, pictureIdx);


            //agregamos la segunda imagen
            // Leer la imagen desde el classpath (resources/img)
            inputStream = Exportar.class.getResourceAsStream("/img/Logo2.png");

            bytes = IOUtils.toByteArray(inputStream);
            inputStream.close();

            // Agregar la imagen al libro de trabajo
            pictureIdx = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);
            helper = workbook.getCreationHelper();

            // Crear el dibujo donde se colocará la imagen
            drawing = sheet.createDrawingPatriarch();

            // Definir el anclaje de la imagen (celda inicial y final)
            anchor = helper.createClientAnchor();
            anchor.setCol1(7); // Columna de inicio (0 es A, 1 es B, etc.)
            anchor.setRow1(0); // Fila de inicio (0 es la primera fila)
            anchor.setCol2(9); // Columna final
            anchor.setRow2(5); // Fila final

            // Insertar la imagen en el dibujo
            drawing.createPicture(anchor, pictureIdx);
        } catch (IOException e) {
            System.err.println("Ha ocurrido un error: " + e.getMessage());
        }

        //Estilo de la fuente para el titulo
        XSSFFont fuenteTitulo = (XSSFFont) workbook.createFont();
        fuenteTitulo.setFontName("Calibri"); // Establecer el nombre de la fuente
        fuenteTitulo.setFontHeightInPoints((short) 12);
        fuenteTitulo.setBold(true);

        //Estilo de la fuente para el titulo
        XSSFFont fuenteNegrita = (XSSFFont) workbook.createFont();
        fuenteNegrita.setFontName("Calibri"); // Establecer el nombre de la fuente
        fuenteNegrita.setFontHeightInPoints((short) 8);
        fuenteNegrita.setBold(true);

        //Estilo de la fuente para la fecha
        XSSFFont fuenteFecha = (XSSFFont) workbook.createFont();
        fuenteFecha.setFontName("Calibri"); // Establecer el nombre de la fuente
        fuenteFecha.setFontHeightInPoints((short) 8);
        fuenteFecha.setBold(true);
        fuenteFecha.setUnderline(FontUnderline.SINGLE);

        //Estilo de la fuente para el nombre de la hoja
        XSSFFont fuenteMesa = (XSSFFont) workbook.createFont();
        fuenteMesa.setFontName("Calibri"); // Establecer el nombre de la fuente
        fuenteMesa.setFontHeightInPoints((short) 12);
        fuenteMesa.setBold(true);
        fuenteMesa.setUnderline(FontUnderline.SINGLE);

        //Estilo de la fuente para los datos escaneados
        XSSFFont fuenteDatos = (XSSFFont) workbook.createFont();
        fuenteDatos.setFontName("Calibri"); // Establecer el nombre de la fuente
        fuenteDatos.setFontHeightInPoints((short) 8);

        //Estilo de Texto Centrado
        CellStyle styleCenter = workbook.createCellStyle();
        styleCenter.setAlignment(HorizontalAlignment.CENTER); // Alinear horizontalmente al centro
        styleCenter.setVerticalAlignment(VerticalAlignment.CENTER); // Alinear verticalmente al centro
        styleCenter.setFont(fuenteTitulo);

        //Estilo para la fecha
        CellStyle estiloFecha = workbook.createCellStyle();
        CreationHelper createHelper = workbook.getCreationHelper();
        estiloFecha.setDataFormat(createHelper.createDataFormat().getFormat("dd \"de\" MMMM \"de\" yyyy"));  // Formato de fecha por defecto en muchas configuraciones regionales
        estiloFecha.setFont(fuenteFecha);

        //Estilo para el fondo coloreado
        XSSFCellStyle estiloColor = (XSSFCellStyle) workbook.createCellStyle();

        // Definir el color de relleno gris (énfasis 3 - 60%)
        XSSFColor colorRelleno = new XSSFColor(new java.awt.Color(191, 191, 191), null); // 191, 191, 191 es el color gris
        estiloColor.setFillForegroundColor(colorRelleno);
        estiloColor.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        estiloColor.setAlignment(HorizontalAlignment.CENTER); // Alinear horizontalmente al centro
        estiloColor.setVerticalAlignment(VerticalAlignment.CENTER); // Alinear verticalmente al centro
        estiloColor.setBorderBottom(BorderStyle.THIN); // Opcional: añadir bordes si se desea
        estiloColor.setBorderTop(BorderStyle.THIN);
        estiloColor.setBorderLeft(BorderStyle.THIN);
        estiloColor.setBorderRight(BorderStyle.THIN);
        estiloColor.setFont(fuenteNegrita);

        //Creamos el estilo, centrado y con los bordes de la tabla
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER); // Alinear horizontalmente al centro
        style.setVerticalAlignment(VerticalAlignment.CENTER); // Alinear verticalmente al centro
        style.setBorderBottom(BorderStyle.THIN); // Opcional: añadir bordes si se desea
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setFont(fuenteDatos);

        //Creamos el estilo centrado, negritas, y subrayado
        CellStyle estiloMesa = workbook.createCellStyle();
        estiloMesa.setAlignment(HorizontalAlignment.CENTER); // Alinear horizontalmente al centro
        estiloMesa.setVerticalAlignment(VerticalAlignment.CENTER); // Alinear verticalmente al centro
        estiloMesa.setFont(fuenteMesa);


        //Creamos las celdas unidas
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 2, 6));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 2, 6));
        sheet.addMergedRegion(new CellRangeAddress(2, 2, 2, 6));
        sheet.addMergedRegion(new CellRangeAddress(3, 3, 2, 6));
        sheet.addMergedRegion(new CellRangeAddress(4, 4, 2, 6));
        sheet.addMergedRegion(new CellRangeAddress(5, 5, 2, 6));

        //Escribimos el texto de los maestros
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(2);
        cell.setCellValue("SINDICATO DE MAESTROS AL SERVICIO");
        cell.setCellStyle(styleCenter);
        row = sheet.createRow(1);
        cell = row.createCell(2);
        cell.setCellValue("DEL ESTADO DE MÉXICO");
        cell.setCellStyle(styleCenter);
        row = sheet.createRow(2);
        cell = row.createCell(2);
        cell.setCellValue("\"POR LA EDUCACIÓN AL SERVICIO DEL PUEBLO\"");
        cell.setCellStyle(styleCenter);
        row = sheet.createRow(3);
        cell = row.createCell(2);
        cell.setCellValue("XXV CONGRESO ESTATAL ORDINARIO");
        cell.setCellStyle(styleCenter);
        row = sheet.createRow(4);
        cell = row.createCell(2);
        cell.setCellValue(mesa);
        cell.setCellStyle(estiloMesa);
        row = sheet.createRow(5);
        cell = row.createCell(2);
        cell.setCellValue(fileName);
        cell.setCellStyle(estiloMesa);

        //fila 7 (valor 6) fecha
        row = sheet.createRow(6);
        cell = row.createCell(6);
        cell.setCellValue(new Date());
        cell.setCellStyle(estiloFecha);

        // Crear una fila de encabezados
        Row headerRow = sheet.createRow(8);
        Cell cellNP = headerRow.createCell(0);
        cellNP.setCellValue("NP");
        cellNP.setCellStyle(estiloColor);
        Cell cellCSP = headerRow.createCell(1);
        cellCSP.setCellValue("Clave S. P.");
        cellCSP.setCellStyle(estiloColor);
        Cell cellNombre = headerRow.createCell(2);
        cellNombre.setCellValue("Nombre del Profesor");
        cellNombre.setCellStyle(estiloColor);
        Cell cellRegion = headerRow.createCell(3);
        cellRegion.setCellValue("Region");
        cellRegion.setCellStyle(estiloColor);
        Cell cellDelegacion = headerRow.createCell(4);
        cellDelegacion.setCellValue("Delegación Sindical");
        cellDelegacion.setCellStyle(estiloColor);
        Cell cellCartera = headerRow.createCell(5);
        cellCartera.setCellValue("Cartera");
        cellCartera.setCellStyle(estiloColor);
        Cell cellFolio = headerRow.createCell(6);
        cellFolio.setCellValue("Folio");
        cellFolio.setCellStyle(estiloColor);

        // Escribir cada objeto Persona en una fila
        int rowNum = 9;
        int np = 0;
        for (Profesor profTemp : asistentes) {
            row = sheet.createRow(rowNum++); // Empieza en la segunda fila (índice 1)
            np++;

            // Escribir los atributos del bean en celdas
            cellNP = row.createCell(0);
            cellNP.setCellValue(np);
            cellNP.setCellStyle(style);
            cellCSP = row.createCell(1);
            cellCSP.setCellValue(Integer.parseInt(profTemp.getCsp()));
            cellCSP.setCellStyle(style);
            cellNombre = row.createCell(2);
            cellNombre.setCellValue(profTemp.getNombreProfesor());
            cellNombre.setCellStyle(style);
            cellRegion = row.createCell(3);
            cellRegion.setCellValue(Integer.parseInt(profTemp.getRegion()));
            cellRegion.setCellStyle(style);
            cellDelegacion = row.createCell(4);
            cellDelegacion.setCellValue(profTemp.getDelegacionSindical());
            cellDelegacion.setCellStyle(style);
            cellCartera = row.createCell(5);
            cellCartera.setCellValue(profTemp.getCartera());
            cellCartera.setCellStyle(style);
            cellFolio = row.createCell(6);
            cellFolio.setCellValue(Integer.parseInt(profTemp.getFolio()));
            cellFolio.setCellStyle(style);
        }

        // Ajustar ancho para columnas
        for (int i = 2; i <= 6; i++) {
            sheet.autoSizeColumn(i);
        }

        String userHome = System.getProperty("user.home");
        Path desktopPath = Paths.get(userHome, "Desktop"); // O "Escritorio" O "Desktop"

        // Nombre del archivo que quieres guardar
        Path filePath = desktopPath.resolve(fileName+ ".xlsx");

        // Escribir el archivo en el sistema
        try (FileOutputStream fileOut = new FileOutputStream(filePath.toFile())) {
            workbook.write(fileOut);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Cerrar el workbook
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String obtenerMesa(String fileName){
        String mesa= "";
        switch (fileName){
            case "ASUNTOS ECONÓMICOS":
                mesa = "MESA I";
                break;
            case "ASUNTOS PROFESIONALES Y DE CULTURA GENERAL":
                mesa = "MESA II";
                break;
            case "ASUNTOS MÉDICO ASISTENCIALES":
                mesa = "MESA III";
                break;
            case "ASUNTOS POLÍTICO SINDICALES":
                mesa = "MESA IV";
                break;
            case "ASUNTOS GENERALES":
                mesa = "MESA V";
                break;
        }
        return mesa;
    }

}
