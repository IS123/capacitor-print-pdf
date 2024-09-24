import { WebPlugin } from '@capacitor/core';

import type { PrintPdfPlugin } from './definitions';

export class PrintPdfWeb extends WebPlugin implements PrintPdfPlugin {
  printPdf(): Promise<void> {
    throw new Error('Method not implemented.');
  }
}
