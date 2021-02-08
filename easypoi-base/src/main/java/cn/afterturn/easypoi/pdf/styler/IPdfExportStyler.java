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
import com.itextpdf.layout.element.Cell;

/**
 * PDF导出样式设置
 *
 * @author JueYue
 * 2016年1月7日 下午11:16:51
 */
public interface IPdfExportStyler {

    /**
     * 设置Cell的样式
     *
     * @param entity
     * @param text
     */
    public void setCellStyler(Cell iCell, ExcelExportEntity entity, String text);

    /**
     * 获取字体
     *
     * @param entity
     * @param text
     */
    public PdfFont getFont(ExcelExportEntity entity, String text);

    /**
     * 获取字体
     */
    public PdfFont getFont();
}
