var capacitorPrintPdf = (function (exports, core) {
    'use strict';

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

    Object.defineProperty(exports, '__esModule', { value: true });

    return exports;

})({}, capacitorExports);
//# sourceMappingURL=plugin.js.map
