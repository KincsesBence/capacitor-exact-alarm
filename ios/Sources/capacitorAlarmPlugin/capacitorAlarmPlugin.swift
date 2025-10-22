import Foundation
import Capacitor

/**
 * Please read the Capacitor iOS Plugin Development Guide
 * here: https://capacitorjs.com/docs/plugins/ios
 */
@objc(capacitorAlarmPlugin)
public class capacitorAlarmPlugin: CAPPlugin, CAPBridgedPlugin {
    public let identifier = "capacitorAlarmPlugin"
    public let jsName = "capacitorAlarm"
    public let pluginMethods: [CAPPluginMethod] = [
        CAPPluginMethod(name: "echo", returnType: CAPPluginReturnPromise)
    ]
    private let implementation = capacitorAlarm()

    @objc func echo(_ call: CAPPluginCall) {
        let value = call.getString("value") ?? ""
        call.resolve([
            "value": implementation.echo(value)
        ])
    }
}
