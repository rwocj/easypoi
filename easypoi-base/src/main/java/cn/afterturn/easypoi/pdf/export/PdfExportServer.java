/**
 * Copyright 2013-2015 JueYue (qrb.jueyue@gmail.com)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.afterturn.easypoi.pdf.export;

import cn.afterturn.easypoi.cache.ImageCache;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import cn.afterturn.easypoi.excel.export.base.ExportCommonService;
import cn.afterturn.easypoi.pdf.entity.PdfExportParams;
import cn.afterturn.easypoi.pdf.styler.IPdfExportStyler;
import cn.afterturn.easypoi.pdf.styler.PdfExportStylerDefaultImpl;
import cn.afterturn.easypoi.util.PoiPublicUtil;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * PDF导出服务,基于Excel基础的导出
 *
 * @author JueYue
 * 2015年10月6日 下午8:21:08
 */
public class PdfExportServer extends ExportCommonService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PdfExportServer.class);

    private Document         document;
    private IPdfExportStyler styler = new PdfExportStylerDefaultImpl();

    private boolean isListData = false;

    public PdfExportServer(OutputStream outStream, PdfExportParams entity) {
        try {
            styler = entity.getStyler() == null ? styler : entity.getStyler();
            document = new Document(new PdfDocument(new PdfWriter(outStream)), entity.getPageSize());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public PdfExportServer() {

    }

    /**
     * 创建Pdf的表格数据
     *
     * @param entity
     * @param pojoClass
     * @param dataSet
     * @return
     */
    public Document createPdf(PdfExportParams entity, Class<?> pojoClass, Collection<?> dataSet) {
        try {
            List<ExcelExportEntity> excelParams = new ArrayList<ExcelExportEntity>();
            if (entity.isAddIndex()) {
                //excelParams.add(indexExcelEntity(entity));
            }
            // 得到所有字段
            Field[]     fileds   = PoiPublicUtil.getClassFields(pojoClass);
            ExcelTarget etarget  = pojoClass.getAnnotation(ExcelTarget.class);
            String      targetId = etarget == null ? null : etarget.value();
            getAllExcelField(entity.getExclusions(), targetId, fileds, excelParams, pojoClass,
                    null, null);
            createPdfByExportEntity(entity, excelParams, dataSet);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            try {
                document.close();
            } catch (Exception e) {
                //可能之前已经关闭过了
            }
        }
        return document;
    }

    public Document createPdfByExportEntity(PdfExportParams entity,
                                            List<ExcelExportEntity> excelParams,
                                            Collection<?> dataSet) {
        try {
            sortAllParams(excelParams);
            for (int k = 0, paramSize = excelParams.size(); k < paramSize; k++) {
                if (excelParams.get(k).getList() != null) {
                    isListData = true;
                    break;
                }
            }
            //设置各个列的宽度
            float[] widths = getCellWidths(excelParams);
            Table   table  = new Table(widths);
            //设置表头
            createHeaderAndTitle(entity, table, excelParams);
            int         rowHeight = getRowHeight(excelParams) / 50;
            Iterator<?> its       = dataSet.iterator();
            while (its.hasNext()) {
                Object t = its.next();
                createCells(table, t, excelParams, rowHeight);
            }
            document.add(table);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            document.close();
        }
        return document;
    }

    private void createCells(Table table, Object t, List<ExcelExportEntity> excelParams,
                             int rowHeight) throws Exception {
        ExcelExportEntity entity;
        int               maxHeight = getThisMaxHeight(t, excelParams);
        for (int k = 0, paramSize = excelParams.size(); k < paramSize; k++) {
            entity = excelParams.get(k);
            if (entity.getList() != null) {
                Collection<?> list = getListCellValue(entity, t);
                for (Object obj : list) {
                    createListCells(table, obj, entity.getList(), rowHeight);
                }
            } else {
                Object value = getCellValue(entity, t);
                if (entity.getType() == 1) {
                    createStringCell(table, value == null ? "" : value.toString(), entity,
                            rowHeight, 1, maxHeight);
                } else {
                    createImageCell(table, value == null ? "" : value.toString(), entity, rowHeight,
                            1, maxHeight);
                }
            }
        }
    }

    /**
     * 创建集合对象
     *
     * @param table
     * @param obj
     * @param rowHeight
     * @param excelParams
     * @throws Exception
     */
    private void createListCells(Table table, Object obj, List<ExcelExportEntity> excelParams,
                                 int rowHeight) throws Exception {
        ExcelExportEntity entity;
        for (int k = 0, paramSize = excelParams.size(); k < paramSize; k++) {
            entity = excelParams.get(k);
            Object value = getCellValue(entity, obj);
            if (entity.getType() == 1) {
                createStringCell(table, value == null ? "" : value.toString(), entity, rowHeight);
            } else {
                createImageCell(table, value == null ? "" : value.toString(), entity, rowHeight, 1,
                        1);
            }
        }
    }

    /**
     * 获取这一列的高度
     *
     * @param t           对象
     * @param excelParams 属性列表
     * @return
     * @throws Exception 通过反射过去值得异常
     */
    private int getThisMaxHeight(Object t, List<ExcelExportEntity> excelParams) throws Exception {
        if (isListData) {
            ExcelExportEntity entity;
            int               maxHeight = 1;
            for (int k = 0, paramSize = excelParams.size(); k < paramSize; k++) {
                entity = excelParams.get(k);
                if (entity.getList() != null) {
                    Collection<?> list = getListCellValue(entity, t);
                    maxHeight = (list == null || maxHeight > list.size()) ? maxHeight : list.size();
                }
            }
            return maxHeight;
        }
        return 1;
    }

    /**
     * 获取Cells的宽度数组
     *
     * @param excelParams
     * @return
     */
    private float[] getCellWidths(List<ExcelExportEntity> excelParams) {
        List<Float> widths = new ArrayList<Float>();
        for (int i = 0; i < excelParams.size(); i++) {
            if (excelParams.get(i).getList() != null) {
                List<ExcelExportEntity> list = excelParams.get(i).getList();
                for (int j = 0; j < list.size(); j++) {
                    widths.add((float) (20 * list.get(j).getWidth()));
                }
            } else {
                widths.add((float) (20 * excelParams.get(i).getWidth()));
            }
        }
        float[] widthArr = new float[widths.size()];
        for (int i = 0; i < widthArr.length; i++) {
            widthArr[i] = widths.get(i);
        }
        return widthArr;
    }

    private void createHeaderAndTitle(PdfExportParams entity, Table table,
                                      List<ExcelExportEntity> excelParams) {
        int feildWidth = getFieldLength(excelParams);
        if (entity.getTitle() != null) {
            createHeaderRow(entity, table, feildWidth);
        }
        createTitleRow(entity, table, excelParams);
    }

    /**
     * 创建表头
     *
     * @param title
     * @param table
     */
    private int createTitleRow(PdfExportParams title, Table table,
                               List<ExcelExportEntity> excelParams) {
        int rows = getRowNums(excelParams, false);
        for (int i = 0, exportFieldTitleSize = excelParams.size(); i < exportFieldTitleSize; i++) {
            ExcelExportEntity entity = excelParams.get(i);
            if (entity.getList() != null) {
                if (StringUtils.isNotBlank(entity.getName())) {
                    createStringCell(table, entity.getName(), entity, 10, entity.getList().size(),
                            1);
                }
                List<ExcelExportEntity> sTitel = entity.getList();
                for (int j = 0, size = sTitel.size(); j < size; j++) {
                    createStringCell(table, sTitel.get(j).getName(), sTitel.get(j), 10);
                }
            } else {
                createStringCell(table, entity.getName(), entity, 10, 1, rows == 2 ? 2 : 1);
            }
        }
        return rows;

    }

    private void createHeaderRow(PdfExportParams entity, Table table, int feildLength) {
        Cell iCell = new Cell(entity.getSecondTitle() != null ? 2 : 1, feildLength + 1);
        iCell.setHorizontalAlignment(HorizontalAlignment.CENTER);
        iCell.setVerticalAlignment(VerticalAlignment.MIDDLE);
        iCell.setHeight(entity.getTitleHeight());
        iCell.add(new Paragraph(entity.getTitle()));
        table.addCell(iCell);
        if (entity.getSecondTitle() != null) {
            iCell = new Cell(1, feildLength + 1);
            iCell.add(new Paragraph(entity.getSecondTitle()));
            iCell.setHorizontalAlignment(HorizontalAlignment.CENTER);
            iCell.setVerticalAlignment(VerticalAlignment.MIDDLE);
            iCell.setHeight(entity.getSecondTitleHeight());
            table.addCell(iCell);
        }
    }

    private Cell createStringCell(Table table, String text, ExcelExportEntity entity,
                                  int rowHeight, int colspan, int rowspan) {
        Cell iCell = new Cell(rowspan, colspan);
        iCell.add(new Paragraph(text));
        styler.setCellStyler(iCell, entity, text);
        iCell.setHeight((int) (rowHeight * 2.5));
        table.addCell(iCell);
        return iCell;
    }

    private Cell createStringCell(Table table, String text, ExcelExportEntity entity,
                                  int rowHeight) {
        Cell iCell = new Cell();
        iCell.add(new Paragraph(text));
        styler.setCellStyler(iCell, entity, text);
        iCell.setHeight((int) (rowHeight * 2.5));
        table.addCell(iCell);
        return iCell;
    }

    private Cell createImageCell(Table table, String text, ExcelExportEntity entity,
                                 int rowHeight, int rowSpan, int colSpan) {

        Image image = new Image(ImageDataFactory.create(ImageCache.getImage(text)));
        Cell  iCell = new Cell();
        iCell.add(image);
        styler.setCellStyler(iCell, entity, text);
        iCell.setHeight((int) (rowHeight * 2.5));
        table.addCell(iCell);
        return iCell;

    }

}
