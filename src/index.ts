import { registerPlugin } from '@capacitor/core';

import type { PrintPdfPlugin } from './definitions';

const PrintPdf = registerPlugin<PrintPdfPlugin>('PrintPdf', {
  web: () => import('./web').then(m => new m.PrintPdfWeb()),
});

export * from './definitions';
export { PrintPdf };
