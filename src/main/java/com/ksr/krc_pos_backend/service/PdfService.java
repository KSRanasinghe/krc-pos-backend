package com.ksr.krc_pos_backend.service;

import com.ksr.krc_pos_backend.model.Invoice;
import com.ksr.krc_pos_backend.model.OrderItem;
import com.ksr.krc_pos_backend.util.PdfResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PdfService {

    private final TemplateEngine templateEngine;

    public PdfResponse generateInvoicePdf(Invoice invoice, List<OrderItem> orderItems) throws Exception {

        // 1. Prepare Thymeleaf context with data
        Context context = new Context();
        context.setVariable("invoice", invoice);
        context.setVariable("orderItems", orderItems);

        // 2. Process HTML template
        String html = templateEngine.process("invoice", context);

        // 3. Convert HTML to PDF
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(html);
        renderer.layout();
        renderer.createPDF(outputStream);

        return new PdfResponse(invoice.getInvNumber(), outputStream.toByteArray());
    }
}
