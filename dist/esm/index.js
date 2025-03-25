import { registerPlugin } from '@capacitor/core';
const PrintPdf = registerPlugin('PrintPdf', {
    web: () => import('./web').then(m => new m.PrintPdfWeb()),
});
export * from './definitions';
export { PrintPdf };
//# sourceMappingURL=index.js.map