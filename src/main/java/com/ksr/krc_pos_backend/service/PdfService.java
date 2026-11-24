package com.ksr.krc_pos_backend.service;

import com.ksr.krc_pos_backend.dto.PaymentResDto;
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

    private byte[] renderPdf(String templateName, Context context) throws Exception {
        // Process HTML template
        String html = templateEngine.process(templateName, context);

        // Convert HTML to PDF
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(html);
        renderer.layout();
        renderer.createPDF(outputStream);

        return outputStream.toByteArray();
    }

    public PdfResponse generateInvoicePdf(
            Invoice invoice, List<OrderItem> orderItems,
            Double paid, Double balanceDue) throws Exception {

        // 1. Prepare Thymeleaf context with data
        Context context = new Context();
        context.setVariable("invoice", invoice);
        context.setVariable("orderItems", orderItems);
        context.setVariable("paid", paid);
        context.setVariable("balanceDue", balanceDue);

        byte[] pdfBytes = renderPdf("invoice", context);

        return new PdfResponse(invoice.getInvNumber(), pdfBytes);
    }

    public PdfResponse generatePaymentPdf(PaymentResDto dto, Integer paymentId) throws Exception {
        String payNo = String.format("PAY-%04d", paymentId);

        Context context = new Context();
        context.setVariable("payment", dto);
        context.setVariable("payNo", payNo);

        byte[] pdfBytes = renderPdf("payment-slip", context);

        return new PdfResponse(payNo, pdfBytes);
    }
}
