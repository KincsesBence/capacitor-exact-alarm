import Foundation

@objc public class capacitorAlarm: NSObject {
    @objc public func echo(_ value: String) -> String {
        print(value)
        return value
    }
}
