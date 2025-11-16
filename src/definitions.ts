import { PluginListenerHandle } from "@capacitor/core";

/**
 * ## Alarm Properties
 *
 * | Property        | Type       | Description |
 * |----------------|------------|-------------|
 * | id             | number?    | Unique ID of the alarm (auto-generated when created). |
 * | timestamp      | number?    | Unix timestamp (ms) when the alarm should fire. |
 * | calendar       | calendar?  | Calendar-based schedule (weekly / monthly / daily). |
 * | repeatInterval | number?    | Repeating interval in milliseconds. |
 * | title          | string     | Notification title. |
 * | msg            | string     | Notification message. |
 * | soundName      | string     | URI of the alarm sound. |
 * | icon           | string?    | Android notification icon name. |
 * | dismissText    | string?    | Text for the dismiss action button. |
 * | missedText     | string?    | Text shown for missed alarms. |
 * | data           | any?       | Additional custom data returned on events. |
 */
export type Alarm = {
  id?: number;
  timestamp?: number;
  calendar?: calendar;
  repeatInterval?: number;
  title: string;
  msg: string;
  soundName?: string;
  icon?: string;
  dismissText?: string;
  missedText?: string;
  data?: any;
};

/**
 * Weekdays for calendar scheduling.
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
 * ## Calendar Schedule Properties
 *
 * | Property | Type            | Description |
 * |----------|----------------|-------------|
 * | weekday  | Weekday        | Day of the week (Sunday–Saturday). |
 * | hour     | number         | Hour (0–23). |
 * | minute   | number         | Minute (0–59). |
 */
export interface calendar {
  weekday: Weekday;
  hour: number;
  minute: number;
}

/**
 * Payload to cancel an alarm.
 */
export interface cancelAlarm {
  alarmId: number;
}

/**
 * Returned when retrieving alarms.
 */
export interface alarmResult {
  alarms: Alarm[];
}

/**
 * Permission status object.
 */
export interface checkResult {
  hasPermission: boolean;
}

/**
 * Returned when selecting an alarm sound.
 */
export interface AlarmSoundResult {
  uri: string;
}

/**
 * Main Capacitor Alarm Plugin Interface.
 */
export interface capacitorExactAlarmPlugin {
  /**
   * Schedule an alarm.
   *
   * Supports:
   * - Exact timestamp alarms
   * - Repeating alarms
   * - Weekly calendar alarms
   *
   * ## Examples
   *
   * ### One-time alarm
   * ```ts
   * await capacitorExactAlarmPlugin.setAlarm({
   *   timestamp: Date.now() + 10_000,
   *   title: "One-time Alarm",
   *   msg: "This alarm will ring once.",
   *   soundName: "content://media/internal/audio/media/21"
   * });
   * ```
   *
   * ### Repeating alarm (every 15 minutes)
   * ```ts
   * await capacitorExactAlarmPlugin.setAlarm({
   *   repeatInterval: 1000 * 60 * 15,
   *   title: "Repeating Alarm",
   *   msg: "This alarm repeats every 15 minutes.",
   *   soundName: "content://media/internal/audio/media/33"
   * });
   * ```
   *
   * ### Weekly alarm (every Monday at 7:30)
   * ```ts
   * await capacitorExactAlarmPlugin.setAlarm({
   *   calendar: {
   *     weekday: Weekday.Monday,
   *     hour: 7,
   *     minute: 30,
   *   },
   *   title: "Weekly Alarm",
   *   msg: "It's Monday at 7:30!",
   *   soundName: "content://media/internal/audio/media/12"
   * });
   * ```
   */
  setAlarm(alarm: Alarm): Promise<Alarm>;

  /**
   * Cancel a specific alarm using its ID.
   *
   * ```ts
   * await capacitorExactAlarmPlugin.cancelAlarm({ alarmId: 1 });
   * ```
   */
  cancelAlarm(alarm: cancelAlarm): Promise<void>;

  /**
   * Cancel all scheduled alarms.
   *
   * ```ts
   * await capacitorExactAlarmPlugin.cancelAllAlarm();
   * ```
   */
  cancelAllAlarm(): Promise<void>;

  /**
   * Request permission for exact alarms (Android 12+).
   *
   * ```ts
   * await capacitorExactAlarmPlugin.requestExactAlarmPermission();
   * ```
   */
  requestExactAlarmPermission(): Promise<void>;

  /**
   * Request notification permission (Android 13+).
   *
   * ```ts
   * await capacitorExactAlarmPlugin.requestNotificationPermission();
   * ```
   */
  requestNotificationPermission(): Promise<void>;

  /**
   * Check notification permission status.
   *
   * ```ts
   * const res = await capacitorExactAlarmPlugin.checkNotificationPermission();
   * console.log(res.hasPermission);
   * ```
   */
  checkNotificationPermission(): Promise<checkResult>;

  /**
   * Check exact alarm permission status.
   *
   * ```ts
   * const res = await capacitorExactAlarmPlugin.checkExactAlarmPermission();
   * console.log(res.hasPermission);
   * ```
   */
  checkExactAlarmPermission(): Promise<checkResult>;

  /**
   * Retrieve all currently scheduled alarms.
   *
   * ```ts
   * const { alarms } = await capacitorExactAlarmPlugin.getAlarms();
   * console.log(alarms);
   * ```
   */
  getAlarms(): Promise<alarmResult>;

  /**
   * Open the Android ringtone picker and return the selected sound URI.
   *
   * ```ts
   * const sound = await capacitorExactAlarmPlugin.pickAlarmSound();
   * console.log("Selected sound:", sound.uri);
   * ```
   */
  pickAlarmSound(): Promise<AlarmSoundResult>;

  /**
   * Stop the currently ringing alarm sound.
   *
   * ```ts
   * await capacitorExactAlarmPlugin.stopAlarm();
   * ```
   */
  stopAlarm(): Promise<void>;

  /**
   * Triggered when an alarm fires.
   *
   * ```ts
   * capacitorExactAlarmPlugin.addListener("alarmTriggered", (alarm) => {
   *   console.log("Alarm fired:", alarm);
   * });
   * ```
   */
  addListener(
    eventName: "alarmTriggered",
    listenerFunc: (data: Alarm) => void
  ): Promise<PluginListenerHandle>;

  /**
   * Triggered when the user taps the alarm notification.
   *
   * ```ts
   * capacitorExactAlarmPlugin.addListener("alarmNotificationTapped", (alarm) => {
   *   console.log("Notification tapped:", alarm);
   * });
   * ```
   */
  addListener(
    eventName: "alarmNotificationTapped",
    listenerFunc: (data: Alarm) => void
  ): Promise<PluginListenerHandle>;

  /**
   * Remove all registered listeners.
   *
   * ```ts
   * await capacitorExactAlarmPlugin.removeAllListeners();
   * ```
   */
  removeAllListeners(): Promise<void>;
}
