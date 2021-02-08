package com.itextpdf.layout;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.element.IElement;

import java.util.List;

/**
 * @author jueyue on 20-8-22.
 */
public class MyDocument extends Document {
    public MyDocument(PdfDocument pdfDoc) {
        super(pdfDoc);
    }


    public MyDocument(PdfDocument pdfDoc, PageSize pageSize) {
        super(pdfDoc, pageSize);

    }

    public MyDocument(PdfDocument pdfDoc, PageSize pageSize, boolean immediateFlush) {
        super(pdfDoc, pageSize, immediateFlush);
    }

    public List<IElement> getChildElements() {
        return childElements;
    }
}
