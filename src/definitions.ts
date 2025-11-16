import { PluginListenerHandle } from "@capacitor/core";

export type Alarm = {
  id?:number,
  timestamp?: number,
  calendar?: calendar,
  repeatInterval?: number,
  title: string,
  msg: string,
  soundName: string,
  icon?:string,
  dismissText?:string,
  missedText?:string,
  data?:any
}

export enum Weekday {
  Sunday = 1,
  Monday,
  Tuesday,
  Wednesday,
  Thursday,
  Friday,
  Saturday
}

export interface calendar {
    weekday: Weekday;
    hour: number;
    minute: number;
}

export interface cancelAlarm {
  alarmId: number
}

export interface alarmResult {
  alarms: Alarm[]
}

export interface checkResult {
  hasPermission: boolean
}

export interface AlarmSoundResult {
  uri: string;
}

export interface capacitorAlarmPlugin {
  setAlarm(alarm:Alarm): Promise<Alarm>;
  cancelAlarm(alarm:cancelAlarm): Promise<void>;
  cancelAllAlarm(): Promise<void>;
  requestExactAlarmPermission(): Promise<void>;
  requestNotificationPermission(): Promise<void>;
  checkExactAlarmPermission(): Promise<checkResult>;
  checkNotificationPermission(): Promise<checkResult>;
  getAlarms():Promise<alarmResult>;
  pickAlarmSound(): Promise<AlarmSoundResult>;
  stopAlarm(): Promise<void>;
  addListener(
    eventName: 'alarmTriggered',
    listenerFunc: (data: Alarm) => void
  ): Promise<PluginListenerHandle>;
  addListener(
    eventName: 'alarmNotificationTapped',
    listenerFunc: (data: Alarm) => void
  ): Promise<PluginListenerHandle>;

  removeAllListeners(): Promise<void>;

}
