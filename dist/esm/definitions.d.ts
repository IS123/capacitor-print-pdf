export interface PrintPdfPlugin {
    printPdf(options: {
        filepath: string;
    }): Promise<void>;
}
