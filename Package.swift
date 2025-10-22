// swift-tools-version: 5.9
import PackageDescription

let package = Package(
    name: "CapacitorAlarm",
    platforms: [.iOS(.v14)],
    products: [
        .library(
            name: "CapacitorAlarm",
            targets: ["capacitorAlarmPlugin"])
    ],
    dependencies: [
        .package(url: "https://github.com/ionic-team/capacitor-swift-pm.git", from: "7.0.0")
    ],
    targets: [
        .target(
            name: "capacitorAlarmPlugin",
            dependencies: [
                .product(name: "Capacitor", package: "capacitor-swift-pm"),
                .product(name: "Cordova", package: "capacitor-swift-pm")
            ],
            path: "ios/Sources/capacitorAlarmPlugin"),
        .testTarget(
            name: "capacitorAlarmPluginTests",
            dependencies: ["capacitorAlarmPlugin"],
            path: "ios/Tests/capacitorAlarmPluginTests")
    ]
)