/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.common.util;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelWriter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.apache.poi.xssf.usermodel.XSSFDataValidationConstraint;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 功能: poi导出excel工具类
 *
 * @author lhd
 */
@Slf4j
public class PoiExcelUtil {

    /**
     * 合并单元格处理,获取合并行
     *
     * @param sheet
     * @return List<CellRangeAddress>
     */
    public static List<CellRangeAddress> getCombineCell(Sheet sheet) {
        List<CellRangeAddress> list = new ArrayList<CellRangeAddress>();
        // 获得一个 sheet 中合并单元格的数量
        int sheetmergerCount = sheet.getNumMergedRegions();
        // 遍历所有的合并单元格
        for (int i = 0; i < sheetmergerCount; i++) {
            // 获得合并单元格保存进list中
            CellRangeAddress ca = sheet.getMergedRegion(i);
            list.add(ca);
        }
        return list;
    }

    public static int getRowNum(List<CellRangeAddress> listCombineCell, Cell cell, Sheet sheet) {
        int xr = 0;
        int firstC = 0;
        int lastC = 0;
        int firstR = 0;
        int lastR = 0;
        for (CellRangeAddress ca : listCombineCell) {
            // 获得合并单元格的起始行, 结束行, 起始列, 结束列
            firstC = ca.getFirstColumn();
            lastC = ca.getLastColumn();
            firstR = ca.getFirstRow();
            lastR = ca.getLastRow();
            if (cell.getRowIndex() >= firstR && cell.getRowIndex() <= lastR) {
                if (cell.getColumnIndex() >= firstC && cell.getColumnIndex() <= lastC) {
                    xr = lastR;
                }
            }
        }
        return xr;
    }

    /**
     * 简单格式化校验，返回字符串类型，如果是int类型还需进行处理，Double.valueOf(value).intValue()
     *
     * @param row     行数据
     * @param cellNum 单元格
     * @param isNum   是否为数值类型
     * @return
     */
    public static String getRowValue(Row row, Integer cellNum, Boolean isNum) {
        // 单品名称
        String value = null;
        CellType cellType = null;
        try {
            cellType = row.getCell(cellNum).getCellType();
        } catch (NullPointerException e) {
            return isNum ? "0" : null;
        }
        if (Objects.equals(CellType.NUMERIC, cellType)) {

            if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(row.getCell(cellNum))) {
                Date time = row.getCell(cellNum).getDateCellValue();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                value = format.format(time);
            } else {
                NumberFormat numberFormat = NumberFormat.getInstance();
                // 不显示千位分割符，否则显示结果会变成类似1,234,567,890
                numberFormat.setGroupingUsed(false);
                double d = row.getCell(cellNum).getNumericCellValue();
                value = numberFormat.format(d);

            }
        } else {
            value = Objects.isNull(row.getCell(cellNum)) ? "" : row.getCell(cellNum).getStringCellValue();
        }
        if (isNum) {
            return StrUtil.isBlank(value) ? "0" : value;
        }
        return value;
    }

    /**
     * 判断指定的单元格是否是合并单元格
     *
     * @param sheet
     * @param row    行下标
     * @param column 列下标
     * @return
     */
    public static boolean isMergedRegion(Sheet sheet, int row, int column) {
        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress range = sheet.getMergedRegion(i);
            int firstColumn = range.getFirstColumn();
            int lastColumn = range.getLastColumn();
            int firstRow = range.getFirstRow();
            int lastRow = range.getLastRow();
            if (row >= firstRow && row <= lastRow) {
                if (column >= firstColumn && column <= lastColumn) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 如果需要合并的话，就合并
     */
    public static void mergeIfNeed(ExcelWriter writer, int firstRow, int lastRow, int firstColumn, int lastColumn, Object content) {
        if (content instanceof Date) {
            content = DateUtil.format((Date) content, DatePattern.NORM_DATETIME_PATTERN);
        }
        if (lastRow - firstRow > 0 || lastColumn - firstColumn > 0) {
            writer.merge(firstRow, lastRow, firstColumn, lastColumn, content, false);
        } else {
            writer.writeCellValue(firstColumn, firstRow, content);
        }

    }

    public static void writeExcel(HttpServletResponse response, ExcelWriter writer) {
        //response为HttpServletResponse对象
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
        response.setHeader("Content-Disposition", "attachment;filename=1.xlsx");

        ServletOutputStream servletOutputStream = null;
        try {
            servletOutputStream = response.getOutputStream();
            writer.flush(servletOutputStream);
            servletOutputStream.flush();
        } catch (IORuntimeException | IOException e) {
            log.error("写出Excel发生错误：", e);
        } finally {
            IoUtil.close(writer);
        }
    }

    public static void writeExcel(String filename, ExcelWriter writer, HttpServletResponse response) {
        OutputStream ouputStream = null;
        try {
            filename = new String(filename.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-disposition", "attachment;filename=" + filename);
            ouputStream = response.getOutputStream();
            writer.flush(ouputStream);
            ouputStream.flush();
        } catch (Exception e) {
            log.error("写出Excel发生错误：", e);
        } finally {
            IoUtil.close(writer);
        }
    }

    /**
     * 创建下拉列表选项
     *
     * @param workbook 当前写入器
     * @param sheet    所在Sheet页面
     * @param list     下拉框的选项值
     * @param firstRow 起始行（从0开始）
     * @param lastRow  终止行（从0开始）
     * @param firstCol 起始列（从0开始）
     * @param lastCol  终止列（从0开始）
     */
    public static void createDropDownList(Workbook workbook, Sheet sheet, List<String> list, int firstRow, int lastRow, int firstCol, int lastCol) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        int sheetTotal = workbook.getNumberOfSheets();
        // 设置下拉框数据
        // 新建一个sheet，把数据隐藏，从单元格引用就没有255的报错
        String sheetName = "hidden" + sheetTotal;
        Sheet hidden = workbook.getSheet(sheetName);
        if (Objects.isNull(hidden)) {
            hidden = workbook.createSheet(sheetName);
            for (int i = 0; i < list.size(); i++) {
                Row row = hidden.createRow(i);
                Cell cell = row.createCell(0);
                cell.setCellValue(list.get(i));
            }
            workbook.setSheetHidden(sheetTotal, true);
        }
        String strFormula = sheetName + "!$A$1:$A$65535";
        DataValidationConstraint constraint = new XSSFDataValidationConstraint(DataValidationConstraint.ValidationType.LIST, strFormula);
        CellRangeAddressList regions = new CellRangeAddressList(firstRow, lastRow, firstCol, lastCol);
        DataValidationHelper help = hidden.getDataValidationHelper();
        DataValidation dataValidation = help.createValidation(constraint, regions);
        sheet.addValidationData(dataValidation);
        // Excel兼容性问题
        if (dataValidation instanceof XSSFDataValidation) {
            dataValidation.setSuppressDropDownArrow(true);
            dataValidation.setShowErrorBox(true);
        } else {
            dataValidation.setSuppressDropDownArrow(false);
        }
        sheet.addValidationData(dataValidation);
    }

    /**
     * 创建下拉列表选项【适用长度不会超过255的】
     *
     * @param sheet    所在Sheet页面
     * @param values   下拉框的选项值
     * @param firstRow 起始行（从0开始）
     * @param lastRow  终止行（从0开始）
     * @param firstCol 起始列（从0开始）
     * @param lastCol  终止列（从0开始）
     */
    public static void createDropDownList(Sheet sheet, String[] values, int firstRow, int lastRow, int firstCol, int lastCol) {
        DataValidationHelper helper = sheet.getDataValidationHelper();
        CellRangeAddressList addressList = new CellRangeAddressList(firstRow, lastRow, firstCol, lastCol);
        // 设置下拉框数据
        DataValidationConstraint constraint = helper.createExplicitListConstraint(values);
        DataValidation dataValidation = helper.createValidation(constraint, addressList);
        // Excel兼容性问题
        if (dataValidation instanceof XSSFDataValidation) {
            dataValidation.setSuppressDropDownArrow(true);
            dataValidation.setShowErrorBox(true);
        } else {
            dataValidation.setSuppressDropDownArrow(false);
        }
        sheet.addValidationData(dataValidation);
    }
}
