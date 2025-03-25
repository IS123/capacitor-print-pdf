'use strict';

Object.defineProperty(exports, '__esModule', { value: true });

var core = require('@capacitor/core');

const PrintPdf = core.registerPlugin('PrintPdf', {
    web: () => Promise.resolve().then(function () { return web; }).then(m => new m.PrintPdfWeb()),
});

class PrintPdfWeb extends core.WebPlugin {
    printPdf() {
        throw new Error('Method not implemented.');
    }
}

var web = /*#__PURE__*/Object.freeze({
    __proto__: null,
    PrintPdfWeb: PrintPdfWeb
});

exports.PrintPdf = PrintPdf;
//# sourceMappingURL=plugin.cjs.js.map
