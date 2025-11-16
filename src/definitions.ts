import { PluginListenerHandle } from "@capacitor/core";

/**
 * Represents a scheduled alarm.
 */
export type Alarm = {
  /** Unique ID of the alarm (auto-generated when created). */
  id?: number;

  /** Unix timestamp in milliseconds when the alarm should fire. */
  timestamp?: number;

  /** Calendar-based schedule (weekly / monthly / daily). */
  calendar?: calendar;

  /** Repeating interval in milliseconds. */
  repeatInterval?: number;

  /** Notification title. */
  title: string;

  /** Notification message. */
  msg: string;

  /** URI of the alarm sound. */
  soundName: string;

  /** Android notification icon name. */
  icon?: string;

  /** Text for the dismiss action button. */
  dismissText?: string;

  /** Text shown for missed alarms. */
  missedText?: string;

  /** Additional custom data passed back on events. */
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
 * Weekly calendar schedule.
 */
export interface calendar {
  weekday: Weekday;
  hour: number;
  minute: number;
}

/**
 * Payload for canceling an alarm.
 */
export interface cancelAlarm {
  alarmId: number;
}

/**
 * Returned list of alarms.
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
 * Returned when selecting a ringtone.
 */
export interface AlarmSoundResult {
  uri: string;
}

/**
 * Capacitor Alarm Plugin
 *
 * Provides the ability to schedule exact alarms on Android.
 *
 * Features:
 * - One-time alarms
 * - Repeating alarms
 * - Weekly calendar alarms
 * - Alarm notifications
 * - Ringtone picker
 * - Event listeners for alarm triggers & notification taps
 */
export interface capacitorAlarmPlugin {
  /**
   * Schedule an alarm.
   *
   * Alarm may be based on:
   * - A timestamp
   * - A repeat interval
   * - A weekly calendar schedule
   *
   * @example Schedule a simple one-time alarm:
   * ```ts
   * await capacitorAlarm.setAlarm({
   *   timestamp: Date.now() + 5000,
   *   msg: "Wake up!",
   *   title: "Alarm Triggered",
   *   soundName: "content://media/internal/audio/media/50",
   * });
   * ```
   *
   * @example Schedule a repeating alarm:
   * ```ts
   * await capacitorAlarm.setAlarm({
   *   repeatInterval: 1000 * 60 * 15,
   *   title: "Every 15 minute alarm",
   *   msg: "Repeating alarm",
   *   soundName: "content://media/internal/audio/media/33"
   * });
   * ```
   *
   * @example Weekly alarm:
   * ```ts
   * await capacitorAlarm.setAlarm({
   *   calendar: {
   *     weekday: Weekday.Monday,
   *     hour: 7,
   *     minute: 30,
   *   },
   *   title: "Weekly Alarm",
   *   msg: "It's Monday!",
   *   soundName: "content://media/internal/audio/media/23",
   * });
   * ```
   */
  setAlarm(alarm: Alarm): Promise<Alarm>;

  /**
   * Cancel a specific alarm.
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
   * Request notification permissions (Android 13+).
   *
   * @example
   * ```ts
   * await capacitorAlarm.requestNotificationPermission();
   * ```
   */
  requestNotificationPermission(): Promise<void>;

  /**
   * Check if notification permissions were granted.
   *
   * @example
   * ```ts
   * const res = await capacitorAlarm.checkNotificationPermission();
   * console.log(res.hasPermission);
   * ```
   */
  checkNotificationPermission(): Promise<checkResult>;

  /**
   * Check if exact alarm permissions were granted.
   *
   * @example
   * ```ts
   * const res = await capacitorAlarm.checkExactAlarmPermission();
   * console.log("Exact alarm allowed:", res.hasPermission);
   * ```
   */
  checkExactAlarmPermission(): Promise<checkResult>;

  /**
   * Retrieve all currently scheduled alarms.
   *
   * @example
   * ```ts
   * const res = await capacitorAlarm.getAlarms();
   * console.log(res.alarms);
   * ```
   */
  getAlarms(): Promise<alarmResult>;

  /**
   * Opens the Android ringtone picker and returns a selected sound URI.
   *
   * @example
   * ```ts
   * const sound = await capacitorAlarm.pickAlarmSound();
   * console.log("Selected:", sound.uri);
   * ```
   */
  pickAlarmSound(): Promise<AlarmSoundResult>;

  /**
   * Stop the currently ringing alarm sound.
   *
   * @example
   * ```ts
   * await capacitorAlarm.stopAlarm();
   * ```
   */
  stopAlarm(): Promise<void>;

  /**
   * Fired when the alarm goes off.
   *
   * @example
   * ```ts
   * capacitorAlarm.addListener("alarmTriggered", (alarm) => {
   *   console.log("Alarm triggered:", alarm);
   * });
   * ```
   */
  addListener(
    eventName: "alarmTriggered",
    listenerFunc: (data: Alarm) => void
  ): Promise<PluginListenerHandle>;

  /**
   * Fired when the user taps the alarm notification.
   *
   * @example
   * ```ts
   * capacitorAlarm.addListener("alarmNotificationTapped", (alarm) => {
   *   console.log("Notification tapped:", alarm);
   * });
   * ```
   */
  addListener(
    eventName: "alarmNotificationTapped",
    listenerFunc: (data: Alarm) => void
  ): Promise<PluginListenerHandle>;

  /**
   * Remove all listeners registered through this plugin.
   *
   * @example
   * ```ts
   * await capacitorAlarm.removeAllListeners();
   * ```
   */
  removeAllListeners(): Promise<void>;
}
