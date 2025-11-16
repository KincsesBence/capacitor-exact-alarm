import { WebPlugin } from '@capacitor/core';

import type { capacitorExactAlarmPlugin } from './definitions';

export class capacitorExactAlarmWeb extends WebPlugin implements capacitorExactAlarmPlugin {
  async setAlarm(): Promise<any> {

    console.warn('Alarm is only available on Android.');
    return Promise.reject('Not available on web.');
  }
  async cancelAlarm(): Promise<void> {
    console.warn('Alarm only supported on native Android');
  }

  async stopAlarm(): Promise<void> {
    console.warn('Alarm only supported on native Android');
  }

  async cancelAllAlarm(): Promise<void> {
    console.warn('Alarm only supported on native Android');
  }

  async requestExactAlarmPermission(): Promise<void> {
    console.warn('Alarm only supported on native Android');
  }

  async requestNotificationPermission(): Promise<void> {
    console.warn('Alarm only supported on native Android');
  }

  async checkExactAlarmPermission(): Promise<any>{
    console.warn('Alarm only supported on native Android');
    return Promise.reject('Not available on web.');
  }
  async checkNotificationPermission(): Promise<any>{
    console.warn('Alarm only supported on native Android');
    return Promise.reject('Not available on web.');
  }

  async getAlarms(): Promise<any> {
    console.warn('Alarm only supported on native Android');
    return Promise.reject('Not available on web.');
  }

  pickAlarmSound(): Promise<any> {
    console.warn('Alarm is only available on Android.');
    return Promise.reject('Not available on web.');
  }

  pickAlarmSoundWithCallback(): Promise<any> {
    console.warn('Alarm is only available on Android.');
    return Promise.reject('Not available on web.');
  }

  
}
