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
package cn.afterturn.easypoi.pdf;

import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import cn.afterturn.easypoi.pdf.entity.PdfExportParams;
import cn.afterturn.easypoi.pdf.export.PdfExportServer;
import cn.afterturn.easypoi.pdf.export.PdfTemplateServer;
import com.itextpdf.layout.Document;

import java.io.OutputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * PDF 导出工具类
 *
 * @author JueYue
 * 2015年10月6日 下午8:14:01
 * @version 1.0
 */
public class PdfExportUtil {

    /**
     * 根据注解导出数据
     *
     * @param entity    表格标题属性
     * @param pojoClass PDF对象Class
     * @param dataSet   PDF对象数据List
     */
    public static Document exportPdf(PdfExportParams entity, Class<?> pojoClass,
                                     Collection<?> dataSet, OutputStream outStream) {
        return new PdfExportServer(outStream, entity).createPdf(entity, pojoClass, dataSet);
    }

    /**
     * 根据Map创建对应的PDF
     *
     * @param entity     表格标题属性
     * @param entityList PDF对象Class
     * @param dataSet    PDF对象数据List
     */
    public static Document exportPdf(PdfExportParams entity, List<ExcelExportEntity> entityList,
                                     Collection<? extends Map<?, ?>> dataSet,
                                     OutputStream outStream) {

        return new PdfExportServer(outStream, entity).createPdfByExportEntity(entity, entityList,
                dataSet);
    }

    /**
     * 解析PDF版本
     *
     * @param url 模板地址
     * @param map 解析数据源
     * @return
     */
    public static Document exportPdf(String url, Map<String, Object> map, OutputStream outStream) throws Exception {
        return new PdfTemplateServer().parsePdf(url, map, outStream);
    }

}
