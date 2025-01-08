import Foundation

@objc public class PrintPdf: NSObject {
    @objc public func printPdf(filePath: String, completion: @escaping (_ success: Bool, _ error: String?) -> Void) {
        let fileURL = URL(fileURLWithPath: filePath)
        
        guard FileManager.default.fileExists(atPath: filePath) else {
            completion(false, "File does not exist at the provided path.")
            return
        }
        
        DispatchQueue.main.async {
            let printController = UIPrintInteractionController.shared
            let printInfo = UIPrintInfo(dictionary: nil)
            printInfo.outputType = .general
            printInfo.jobName = fileURL.lastPathComponent
            printController.printInfo = printInfo
            
            do {
                let pdfData = try Data(contentsOf: fileURL)
                printController.printingItem = pdfData
            } catch {
                completion(false, "Failed to load PDF data: \(error.localizedDescription)")
                return
            }
            
            printController.present(animated: true) { (controller, completed, error) in
                if let error = error {
                    completion(false, "Print failed: \(error.localizedDescription)")
                } else if completed {
                    completion(true, nil)
                } else {
                    completion(false, "Print was cancelled by the user.")
                }
            }
        }
    }
}
