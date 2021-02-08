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
package cn.afterturn.easypoi.pdf.styler;

import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 默认的PDFstyler 实现
 *
 * @author JueYue
 * 2016年1月8日 下午2:06:26
 */
public class PdfExportStylerDefaultImpl implements IPdfExportStyler {

    private static final Logger LOGGER = LoggerFactory.getLogger(PdfExportStylerDefaultImpl.class);

    @Override
    public void setCellStyler(Cell iCell, ExcelExportEntity entity, String text) {
        iCell.setHorizontalAlignment(HorizontalAlignment.CENTER);
        iCell.setVerticalAlignment(VerticalAlignment.MIDDLE);
    }

    @Override
    public PdfFont getFont(ExcelExportEntity entity, String text) {
        try {
            //用以支持中文
            return PdfFontFactory.createFont("STSong-Light", "UniGB-UCS2-H", true);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public PdfFont getFont() {
        return getFont(null, null);
    }

}
