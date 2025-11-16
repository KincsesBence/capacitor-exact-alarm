import { PluginListenerHandle } from "@capacitor/core";

/**
 * Capacitor Alarm Plugin
 * ======================
 *
 * A Capacitor plugin that allows scheduling **exact alarms** on Android,
 * similar to the native AlarmManager. You can:
 *
 * - Schedule one-time alarms (using a timestamp)
 * - Schedule repeating alarms (repeatInterval)
 * - Schedule weekly, monthly, or daily calendar-based alarms
 * - Receive "alarmTriggered" events when the alarm fires
 * - Receive "alarmNotificationTapped" events when the user taps the notification
 * - Pick alarm sounds using the Android ringtone picker
 * - Cancel individual alarms or all alarms
 * - Request & check notification and exact alarm permissions
 *
 * ⚠ Android Only: iOS does not allow third-party apps to schedule exact alarms.
 *
 * ----------------------------------------------------------------------------------
 * TYPES
 * ----------------------------------------------------------------------------------
 */

/**
 * Alarm object used for scheduling an alarm.
 */
export type Alarm = {
  id?: number;              // auto-generated when setAlarm() is called
  timestamp?: number;       // Unix timestamp in ms (for one-time alarms)
  calendar?: calendar;      // Weekly / monthly / daily calendar schedule
  repeatInterval?: number;  // ms interval for repeating alarms
  title: string;            // Notification title
  msg: string;              // Notification message
  soundName: string;        // URI of the alarm sound
  icon?: string;            // Notification icon name (Android res)
  dismissText?: string;     // Button text for dismiss action
  missedText?: string;      // Text shown for missed alarms
  data?: any;               // Additional custom data included in events
};

/**
 * Weekday enum for weekly calendar alarms.
 */
export enum Weekday {
  Sunday = 1,
  Monday = 2,
  Tuesday = 3,
  Wednesday = 4,
  Thursday = 5,
  Friday = 6,
  Saturday = 7,
}

/**
 * Calendar-based alarm schedule.
 * Used for weekly, monthly, or daily time schedules.
 */
export interface calendar {
  weekday: Weekday; // day of the week
  hour: number;
  minute: number;
}

/**
 * Used to cancel a specific alarm.
 */
export interface cancelAlarm {
  alarmId: number;
}

/**
 * Returned list of stored alarms.
 */
export interface alarmResult {
  alarms: Alarm[];
}

/**
 * Permission check result.
 */
export interface checkResult {
  hasPermission: boolean;
}

/**
 * Result returned from alarm sound picker.
 */
export interface AlarmSoundResult {
  uri: string;
}

/**
 * Main Capacitor plugin interface.
 */
export interface capacitorAlarmPlugin {

  /**
   * Schedule an alarm.
   * ------------------
   * You can schedule alarms in 3 ways:
   *
   * 1. **Timestamp** alarm  
   *     `{ timestamp: 1724455000000 }`
   *
   * 2. **Repeat interval** alarm  
   *     `{ repeatInterval: 1000 * 60 * 30 }`
   *
   * 3. **Calendar-based** alarm  
   *     `{ calendar: { weekday: Weekday.Monday, hour: 9, minute: 0 } }`
   *
   * @param alarm Alarm configuration.
   * @returns Promise resolving to the created alarm.
   *
   * @example
   * ```ts
   * const result = await capacitorAlarm.setAlarm({
   *    timestamp: Date.now() + 5000, // 5 seconds from now
   *    soundName: "content://media/internal/audio/media/50",
   *    msg: "Wake up!",
   *    title: "Alarm Triggered",
   *    data: { type: "test" },
   * });
   *
   * console.log("Alarm created:", result);
   * ```
   */
  setAlarm(alarm: Alarm): Promise<Alarm>;

  /**
   * Cancel a specific alarm.
   * @param alarm Object containing the alarmId.
   *
   * @example
   * ```ts
   * await capacitorAlarm.cancelAlarm({ alarmId: 123 });
   * ```
   */
  cancelAlarm(alarm: cancelAlarm): Promise<void>;

  /**
   * Cancel all alarms created by the plugin.
   *
   * @example
   * ```ts
   * await capacitorAlarm.cancelAllAlarm();
   * ```
   */
  cancelAllAlarm(): Promise<void>;

  /**
   * Request permission to schedule exact alarms (Android 12+).
   *
   * @example
   * ```ts
   * await capacitorAlarm.requestExactAlarmPermission();
   * ```
   */
  requestExactAlarmPermission(): Promise<void>;

  /**
   * Request notification permission (Android 13+).
   *
   * @example
   * ```ts
   * await capacitorAlarm.requestNotificationPermission();
   * ```
   */
  requestNotificationPermission(): Promise<void>;

  /**
   * Check if notification permission is granted.
   *
   * @returns `{ hasPermission: boolean }`
   */
  checkNotificationPermission(): Promise<checkResult>;

  /**
   * Check whether exact alarms are allowed.
   *
   * @returns `{ hasPermission: boolean }`
   */
  checkExactAlarmPermission(): Promise<checkResult>;

  /**
   * Get a list of all active alarms stored by the plugin.
   *
   * @example
   * ```ts
   * const res = await capacitorAlarm.getAlarms();
   * console.log(res.alarms);
   * ```
   */
  getAlarms(): Promise<alarmResult>;

  /**
   * Opens the Android ringtone picker to select an alarm sound.
   *
   * @returns `{ uri: string }`
   *
   * @example
   * ```ts
   * const sound = await capacitorAlarm.pickAlarmSound();
   * console.log(sound.uri);
   * ```
   */
  pickAlarmSound(): Promise<AlarmSoundResult>;

  /**
   * Stops the currently ringing alarm sound.
   *
   * @example
   * ```ts
   * await capacitorAlarm.stopAlarm();
   * ```
   */
  stopAlarm(): Promise<void>;

  /**
   * Listener fired when the alarm triggers.
   * (The device will send a broadcast event.)
   *
   * @example
   * ```ts
   * capacitorAlarm.addListener("alarmTriggered", (data) => {
   *   console.log("Alarm fired!", data);
   * });
   * ```
   */
  addListener(
    eventName: "alarmTriggered",
    listenerFunc: (data: Alarm) => void
  ): Promise<PluginListenerHandle>;

  /**
   * Listener fired when the user taps the alarm notification.
   *
   * @example
   * ```ts
   * capacitorAlarm.addListener("alarmNotificationTapped", (data) => {
   *   console.log("Notification tapped", data);
   * });
   * ```
   */
  addListener(
    eventName: "alarmNotificationTapped",
    listenerFunc: (data: Alarm) => void
  ): Promise<PluginListenerHandle>;

  /**
   * Removes all event listeners.
   */
  removeAllListeners(): Promise<void>;
}
