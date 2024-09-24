// swift-tools-version: 5.9
import PackageDescription

let package = Package(
    name: "CapacitorPrintPdf",
    platforms: [.iOS(.v13)],
    products: [
        .library(
            name: "CapacitorPrintPdf",
            targets: ["PrintPdfPlugin"])
    ],
    dependencies: [
        .package(url: "https://github.com/ionic-team/capacitor-swift-pm.git", branch: "main")
    ],
    targets: [
        .target(
            name: "PrintPdfPlugin",
            dependencies: [
                .product(name: "Capacitor", package: "capacitor-swift-pm"),
                .product(name: "Cordova", package: "capacitor-swift-pm")
            ],
            path: "ios/Sources/PrintPdfPlugin"),
        .testTarget(
            name: "PrintPdfPluginTests",
            dependencies: ["PrintPdfPlugin"],
            path: "ios/Tests/PrintPdfPluginTests")
    ]
)