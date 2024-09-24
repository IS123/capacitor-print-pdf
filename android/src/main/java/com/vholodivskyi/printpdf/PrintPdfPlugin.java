package com.vholodivskyi.printpdf;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfDocument;
import android.graphics.pdf.PdfRenderer;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.PrintManager;
import android.print.pdf.PrintedPdfDocument;
import androidx.annotation.NonNull;

import com.getcapacitor.Plugin;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.JSObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@CapacitorPlugin(name = "PrintPdf")
public class PrintPdfPlugin extends Plugin {

    @PluginMethod
    public void printPdf(PluginCall call) {
        String filePath = call.getString("filepath");

        if (filePath == null || filePath.isEmpty()) {
            call.reject("File path is required");
            return;
        }

        // Check if file exists
        File pdfFile = new File(filePath);
        if (!pdfFile.exists()) {
            call.reject("File not found: " + filePath);
            return;
        }

        // Try to print the PDF using PrintManager
        try {
            printPdfFile(getContext(), pdfFile);
            JSObject result = new JSObject();
            result.put("message", "Print started");
            call.resolve(result);
        } catch (IOException e) {
            call.reject("Error printing PDF: " + e.getMessage());
        }
    }

    private void printPdfFile(Context context, File pdfFile) throws IOException {
        // Create a file descriptor for the PDF file
        ParcelFileDescriptor fileDescriptor = ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_ONLY);
        PdfRenderer pdfRenderer = new PdfRenderer(fileDescriptor);

        PrintManager printManager = (PrintManager) context.getSystemService(Context.PRINT_SERVICE);
        if (printManager != null) {
            // Create a PrintDocumentAdapter for printing the PDF
            PrintDocumentAdapter printAdapter = new PrintDocumentAdapter() {

                private PdfRenderer pdfRenderer;
                private ParcelFileDescriptor fileDescriptor;

                private PrintAttributes printAttributes;
                @Override
                public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes, CancellationSignal cancellationSignal, LayoutResultCallback callback, Bundle bundle) {
                    this.printAttributes = newAttributes;

                    try {
                        if (fileDescriptor == null) {
                            fileDescriptor = ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_ONLY);
                        }

                        if (pdfRenderer == null) {
                            pdfRenderer = new PdfRenderer(fileDescriptor);
                        }
                    } catch (IOException e) {
                        callback.onLayoutFailed(e.toString());
                        return;
                    }

                    // Prepare PrintDocumentInfo and notify the print framework about the layout result
                    PrintDocumentInfo info = new PrintDocumentInfo.Builder(pdfFile.getName())
                            .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                            .setPageCount(pdfRenderer.getPageCount())
                            .build();

                    callback.onLayoutFinished(info, true);
                }

                @Override
                public void onWrite(android.print.PageRange[] pages, ParcelFileDescriptor destination, android.os.CancellationSignal cancellationSignal, WriteResultCallback callback) {
                    try {
                        PrintedPdfDocument document = new PrintedPdfDocument(context, printAttributes);

                        PrintAttributes.MediaSize mediaSize = printAttributes.getMediaSize();
                        int pageWidth;
                        int pageHeight;

                        // If MediaSize is null, set default size (e.g., A4 dimensions in points: 595x842)
                        if (mediaSize != null) {
                            pageWidth = mediaSize.getWidthMils() / 1000 * 72;  // Convert mils to points
                            pageHeight = mediaSize.getHeightMils() / 1000 * 72;
                        } else {
                            // Default to A4 size (595pt width, 842pt height)
                            pageWidth = 595;
                            pageHeight = 842;
                        }

                        for (int i = 0; i < pdfRenderer.getPageCount(); i++) {
                            PdfRenderer.Page page = pdfRenderer.openPage(i);

                            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, i).create();
                            PdfDocument.Page printedPage = document.startPage(pageInfo);

                            Bitmap bitmap = Bitmap.createBitmap(page.getWidth(), page.getHeight(), Bitmap.Config.ARGB_8888);
                            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_PRINT);
                            printedPage.getCanvas().drawBitmap(bitmap, 0, 0, null);

                            document.finishPage(printedPage);
                            page.close();
                        }

                        document.writeTo(new FileOutputStream(destination.getFileDescriptor()));
                        document.close();

                        callback.onWriteFinished(new android.print.PageRange[]{android.print.PageRange.ALL_PAGES});
                    } catch (IOException e) {
                        callback.onWriteFailed(e.toString());
                    } finally {
                        // Close resources only after the whole process is complete
                        if (pdfRenderer != null) {
                            pdfRenderer.close();
                            pdfRenderer = null;
                        }

                        if (fileDescriptor != null) {
                            try {
                                fileDescriptor.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            fileDescriptor = null;
                        }
                    }
                }
                @Override
                public void onFinish() {
                    // Cleanup PdfRenderer and file descriptor when the printing process is done
                    if (pdfRenderer != null) {
                        pdfRenderer.close();
                        pdfRenderer = null;
                    }

                    if (fileDescriptor != null) {
                        try {
                            fileDescriptor.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        fileDescriptor = null;
                    }
                }
            };

            // Start the print job
            printManager.print("PrintJob_" + pdfFile.getName(), printAdapter, new PrintAttributes.Builder().build());
        }

        pdfRenderer.close();
        fileDescriptor.close();
    }

}
