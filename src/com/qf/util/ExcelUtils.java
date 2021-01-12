package com.qf.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

/**
 * 作者：威哥
 * 时间：2020/12/28
 */
public class ExcelUtils {
    //测试创建excel表格
    public void test1() throws IOException {
        //创建一个03版的Excel文件  07版XSSFWorkbook对象
        HSSFWorkbook work = new HSSFWorkbook();
        //创建一个Excel的Sheet
        HSSFSheet sheet = work.createSheet();
        //创建一个3行3列的单元格
        for(int i=0;i<3;i++){
            //Excel文件的行
            HSSFRow row = sheet.createRow(i);
            for(int j=0;j<3;j++){
                //Excel文件的单元格
                HSSFCell cell = row.createCell(j);
                //向单元格内添加值
                cell.setCellValue("测试");
            }
        }
        //写入到本地中D盘
        FileOutputStream fos=new FileOutputStream("D:\\"+"测试创建表格"+".xls");
        work.write(fos);
        System.out.println("创建excel完成");
    }

    //测试导出数据
    @Test
    public void test2() throws IOException {
        FileOutputStream out=new FileOutputStream("D://"+"测试数据"+".xls");
        String[] titles = {"姓名","年龄","性别"};
        List<String[]> datas = new ArrayList<String[]>();
        String[] data1 = {"张三1","31","男1"};
        String[] data2 = {"张三2","32","男2"};
        String[] data3 = {"张三3","33","男3"};
        datas.add(data1);
        datas.add(data2);
        datas.add(data3);
        export(titles, datas, out);
    }

    //测试导入数据
    @Test
    public void test3() throws IOException {
        FileInputStream fis = new FileInputStream("D:\\测试数据.xls");
        List<String[]> importData = importData("xls", fis, 1);
        for (String[] datas : importData) {
            for (String data : datas) {
                System.out.print(data+"\t");
            }
            System.out.println();
        }
    }

    /**
     * 导出excel文件的方法
     *
     * @param titles 输出到excel的表头
     * @param datas  数据库的表中的数据 集合对象
     * @param out excel(File) 输出到指定的路径 (OutPutStream)
     */
    public static void export(String titles[], List<String[]> datas, OutputStream out) {
        // 创建工作表
        HSSFWorkbook work = new HSSFWorkbook();
        // 创建sheet表
        HSSFSheet sheet = work.createSheet("sheet1");
        // 创建表头【表头数据来源】
        Row headRow = sheet.createRow(0);
        Cell cell = null;
        // 循环输出标题
        for (int i = 0; i < titles.length; i++) {
            cell = headRow.createCell(i);
            cell.setCellValue(titles[i]); // 设置cell中的内容
        }
        // 表体内容
        // 设置表体内容 嵌套循环
        for (int row = 0; row < datas.size(); row++) {
            // 创建行
            HSSFRow sheetRow = sheet.createRow(row + 1);
            // 循环 创建列
            for (int c = 0; c < datas.get(row).length; c++) {
                HSSFCell sheetCell = sheetRow.createCell(c);
                // 设置内容
                sheetCell.setCellValue(datas.get(row)[c]);
            }
        }

        // 创建完成后，内存创建的Excel输出到文件中
        try {
            work.write(out);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     *  导入excel文件的方法
     *
     *  suffix: 传入excel的后缀名
     *      excel  2003版的后缀为(xls) HSSFWorkbook  2007版的后缀为(xlsx)XSSFWorkbook
     *  fis :   通过输入流来读取excel的内容
     *  startRow: 读取内容的起始行
     *
     *  @return 返回导入的数据    List<String[]>
     *
     */
    public static List<String[]> importData(String suffix, InputStream fis, int startrow) {
        List<String[]> datas = new ArrayList<String[]>();
        // 创建表格对象
        Workbook work = null;
        try {
            // 判断后缀名 2003
            if (suffix.equals("xls")) {
                work = new HSSFWorkbook(fis); // 2003的工作表对象
            } else if (suffix.equals("xlsx")) { //
                work = new XSSFWorkbook(fis); // 2007的工作表对象
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 获取sheet表格
        Sheet sheet = work.getSheetAt(0);
        if (sheet == null) { // 代表一个sheet表格也没有
            return null;
        }
        // 获取一共多少行
        int rownum = sheet.getLastRowNum();
        if (rownum == 0 || rownum+1 == startrow) {
            return null;
        }
        for (int i = startrow; i <= rownum; i++) {
            // 获取行
            Row row = sheet.getRow(i);
            // 获取列
            short first = row.getFirstCellNum(); //起始列的下标
            short num = row.getLastCellNum();    //终止列的下标

            String[] cols = new String[num];    //存放当前行所有的列内容
            for (int j = first; j < num; j++) {
                // 处理列对象
                Cell cell = row.getCell(j);
                //获取指定列的内容， 都转换为String类型
                cols[j] = parseCell(cell);
            }
            datas.add(cols);  //列处理完成以后，把该行所有列添加集合中
        }
        return datas;
    }
    // 转换类型
    private static String parseCell(Cell cell) {
        String cellValue = "";
        //判断如果是String类型 则去除空格
        if (cell.getCellTypeEnum() == CellType.STRING) {
            cellValue = cell.getStringCellValue().trim();
        } else if (cell.getCellTypeEnum() == CellType.BOOLEAN) { //如果是boolean类型则获取boolean类型的值
            cellValue = String.valueOf(cell.getBooleanCellValue());
        } else if (cell.getCellTypeEnum() == CellType.NUMERIC) { //如果是数字类型， 则判断是日期类型还是普通数字
            if (HSSFDateUtil.isCellDateFormatted(cell)) { // 判断日期类型
                double d = cell.getNumericCellValue();
                Date date = HSSFDateUtil.getJavaDate(d);
                cellValue = new SimpleDateFormat("yyyy-MM-dd").format(date);
            } else { // 否
                cellValue = new DecimalFormat("#.######").format(cell.getNumericCellValue());
            }
        } else {
            cellValue = "";
        }
        return cellValue;
    }
}