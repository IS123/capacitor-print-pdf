import Foundation
import Capacitor

/**
 * Please read the Capacitor iOS Plugin Development Guide
 * here: https://capacitorjs.com/docs/plugins/ios
 */
@objc(PrintPdfPlugin)
public class PrintPdfPlugin: CAPPlugin, CAPBridgedPlugin {
    public let identifier = "PrintPdfPlugin"
    public let jsName = "PrintPdf"
    
    public let pluginMethods: [CAPPluginMethod] = [
        CAPPluginMethod(name: "printPdf", returnType: CAPPluginReturnPromise)
    ]
    
    private let implementation = PrintPdf()
    
    @objc func printPdf(_ call: CAPPluginCall) {
        guard let filePath = call.getString("filepath") else {
            call.reject("File path is required")
            return
        }
        
        
        implementation.printPdf(filePath: filePath) { success, error in
            if success {
                call.resolve([
                    "status": "success"
                ])
            } else if let errorMessage = error {
                call.reject(errorMessage)
            } else {
                call.resolve([
                    "status": "cancelled"
                ])
            }
        }
    }
}
