package pdsu.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class POITest {

    /**
     * 生成excel
     */
    @Test
    public void testWrite(){
        try {
            //创建Excel对象
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
            //创建sheet
            XSSFSheet mysheet1 = xssfWorkbook.createSheet("mysheet1");
            //创建行
            XSSFRow row0 = mysheet1.createRow(0);
            //创建列
            row0.createCell(0).setCellValue("姓名");
            row0.createCell(1).setCellValue("年龄");
            row0.createCell(2).setCellValue("地址");


            //创建行
            XSSFRow row1 = mysheet1.createRow(1);
            //创建列
            row1.createCell(0).setCellValue("Hebe");
            row1.createCell(1).setCellValue("28");
            row1.createCell(2).setCellValue("台湾");

            //创建行
            XSSFRow row2 = mysheet1.createRow(2);
            //创建列
            row2.createCell(0).setCellValue("允儿");
            row2.createCell(1).setCellValue("28");
            row2.createCell(2).setCellValue("韩国");

            //写入文件
            FileOutputStream outputStream = new FileOutputStream("C:\\Users\\lenovo\\Desktop\\test.xlsx");
            xssfWorkbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
            xssfWorkbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void readExcel(){
        try {
            //获取Excel对象
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook("C:\\Users\\lenovo\\Desktop\\test.xlsx");
            //获取sheet
            XSSFSheet mysheet1 = xssfWorkbook.getSheet("mysheet1");
            //遍历行
            for (Row row : mysheet1) {
                for (Cell cell : row) {
                    String stringCellValue = cell.getStringCellValue();
                    System.out.print(stringCellValue + ",");
                }
                System.out.println();
            }
            //遍历
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
