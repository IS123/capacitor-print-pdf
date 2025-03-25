import { WebPlugin } from '@capacitor/core';
import type { PrintPdfPlugin } from './definitions';
export declare class PrintPdfWeb extends WebPlugin implements PrintPdfPlugin {
    printPdf(): Promise<void>;
}
