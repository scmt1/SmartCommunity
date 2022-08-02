package me.zhengjie.util;

import me.zhengjie.utils.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Excel导入工具类
 *
 * @author Evan.Zhang
 */
public class ImportExcelUtils {
     /**
      * 创建WorkBook对象
      *
      * @param filePath
      * @return
      * @throws IOException
      */
     public static final Workbook createWorkbook(String filePath) throws IOException {
          //文件路径 是否为空
          if (StringUtils.isBlank(filePath)) {
         //   throw new IllegalArgumentException(MassageUtils.getMessage("10011"));
          }
          //文件是否为存在
          File file = new File(filePath);
          if (!file.exists()) {
        //   throw new FileNotFoundException(MassageUtils.getMessage("10012"));
          }
          //判断什么文件类型 xls xlsx
          if (filePath.trim().toLowerCase().endsWith("xls")) {
                return new XSSFWorkbook(new FileInputStream(filePath));
          } else if (filePath.trim().toLowerCase().endsWith("xlsx")) {
                return new XSSFWorkbook(new FileInputStream(filePath));
          } else {
           System.out.println("不支持的文件类型");
           return null;

          }
     }
     /**
      * 获取Sheet页面(按名称)
      *
      * @param wb
      * @param sheetName
      * @return
      */
     public static final Sheet getSheet(Workbook wb, String sheetName) {
      return wb.getSheet(sheetName);
     }
     /**
      * 获取Sheet页面(按页标)
      *
      * @param wb
      * @param index
      * @return
      */
     public static final Sheet getSheet(Workbook wb, int index) {
      return wb.getSheetAt(index);
     }
     /**
      * 获取Sheet页内容
      *
      * @param sheet
      * @return
      */
     public static final List<Object[]> listFromSheet(Sheet sheet) {
      List<Object[]> list = new ArrayList<Object[]>();
      for (int r = sheet.getFirstRowNum(); r <= sheet.getLastRowNum(); r++) {
       Row row = sheet.getRow(r);
       if (row == null || row.getPhysicalNumberOfCells() == 0) {
        continue;
       }
       Object[] cells = new Object[row.getLastCellNum()];
       for (int c = row.getFirstCellNum(); c <= row.getLastCellNum(); c++) {
        Cell cell = row.getCell(c);
        if (cell == null) {
         continue;
        }
        //判断是否为日期类型
        if (HSSFDateUtil.isCellDateFormatted(cell)) {
         //用于转化为日期格式
         Date d = cell.getDateCellValue();
         DateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
         cells[c] = formater.format(d);
        } else {
         cells[c] = getValueFromCell(cell);
        }

                }
                list.add(cells);
            }
            return list;
        }


        /**
         * 获取单元格内信息
         *
         * @param cell
         * @return
         */
    public static final Object getValueFromCell(Cell cell) {
        if (cell == null) {
            System.out.println("Cell is null !!!");
            return null;
        }
        Object result = null;
        if (cell instanceof HSSFCell) {
            if (cell != null) {
                // 单元格类型：Numeric:0,String:1,Formula:2,Blank:3,Boolean:4,Error:5
                int cellType = ((HSSFCell) cell).getCellType();
                switch (cellType) {
                    case HSSFCell.CELL_TYPE_STRING:
                        result = ((HSSFCell) cell).getStringCellValue();
                        break;
                    case HSSFCell.CELL_TYPE_NUMERIC:
                        DecimalFormat df = new DecimalFormat("###.####");
                        result = df.format(((HSSFCell) cell).getNumericCellValue());
                        break;
                    case HSSFCell.CELL_TYPE_FORMULA:
                        result = ((HSSFCell) cell).getNumericCellValue();
                        break;
                    case HSSFCell.CELL_TYPE_BOOLEAN:
                        result = ((HSSFCell) cell).getBooleanCellValue();
                        break;
                    case HSSFCell.CELL_TYPE_BLANK:
                        result = null;
                        break;
                    case HSSFCell.CELL_TYPE_ERROR:
                        result = null;
                        break;
                    default:
                        System.out.println("枚举了所有类型");
                        break;
                }
            }
        } else if (cell instanceof XSSFCell) {
            if (cell != null) {
                // 单元格类型：Numeric:0,String:1,Formula:2,Blank:3,Boolean:4,Error:5
                int cellType = ((XSSFCell) cell).getCellType();
                switch (cellType) {
                    case XSSFCell.CELL_TYPE_STRING:
                        result = ((XSSFCell) cell).getRichStringCellValue().getString();
                        break;
                    case XSSFCell.CELL_TYPE_NUMERIC:
                        DecimalFormat df = new DecimalFormat("###.####");
                        result = df.format(((XSSFCell) cell).getNumericCellValue());
                        break;
                    case XSSFCell.CELL_TYPE_FORMULA:
                        result = ((XSSFCell) cell).getNumericCellValue();
                        break;
                    case XSSFCell.CELL_TYPE_BOOLEAN:
                        result = ((XSSFCell) cell).getBooleanCellValue();
                        break;
                    case XSSFCell.CELL_TYPE_BLANK:
                        result = null;
                        break;
                    case XSSFCell.CELL_TYPE_ERROR:
                        result = null;
                        break;
                    default:
                        System.out.println("枚举了所有类型");
                        break;
                }
            }
        }
        return result;
    }


    /**
     * 根据Sheet页导入Excel信息
     *
     * @param filePath   文件路径
     * @param sheetIndex Sheet页下标
     * @param startRow   开始列 ：默认第一列
     * @param startLine  开始行 ：默认第一行
     * @throws Exception
     */
    public static final List<Object[]> importExcelBySheetIndex(String filePath, int sheetIndex
            , int startRow, int startLine) throws Exception {

        List<Object[]> resultList = null;

        //创建WorkBook对象
        Workbook wb = createWorkbook(filePath);

        // 获取Sheet
        Sheet sheet = ImportExcelUtils.getSheet(wb, sheetIndex);

        // 判断Sheet是否为空
        if (sheet != null) {

            // 遍历Sheet
            List<Object[]> list = ImportExcelUtils.listFromSheet(sheet);
            if (list != null && list.size() > 0) {
                resultList = new ArrayList<Object[]>();
                if (startLine <= list.size()) {
                    for (int i = startLine; i < list.size(); i++) {
                        int nullCount = 0;
                        Object[] rows = list.get(i);
                        if (rows != null && rows.length > 0) {
                            List<Object> resultObjects = new ArrayList<Object>();
                            for (int n = startRow; n < rows.length; n++) {
                                if (isEmpty(rows[n])) {
                                    nullCount++;
                                }
                                resultObjects.add(rows[n]);
                            }

                            //判断空的单元格个数
                            if (nullCount >= rows.length) {
                                break;
                            } else {
                                resultList.add(resultObjects.toArray());
                            }
                        }
                    }
                }
            }
        }
        return resultList;
    }

    public static boolean isEmpty(Object o) {
            if (o == null) {
             return true;
            }
            if (o instanceof String) {
             if (o.toString().trim().equals("")) {
              return true;
             }
             if (o.equals("null") || o.equals("NULL")) {
              return true;
             }
            } else if (o instanceof List) {
             if (((List) o).size() == 0) {
              return true;
             }
            } else if (o instanceof Map) {
             if (((Map) o).size() == 0) {
              return true;
             }
            } else if (o instanceof Set) {
             if (((Set) o).size() == 0) {
              return true;
             }
            } else if (o instanceof Object[]) {
             if (((Object[]) o).length == 0) {
              return true;
             }
            } else if (o instanceof int[]) {
             if (((int[]) o).length == 0) {
              return true;
             }
            } else if (o instanceof long[]) {
             if (((long[]) o).length == 0) {
              return true;
             }
            }
            return false;
        }
}
