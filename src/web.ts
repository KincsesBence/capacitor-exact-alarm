import { WebPlugin } from '@capacitor/core';

import type { capacitorAlarmPlugin } from './definitions';

export class capacitorAlarmWeb extends WebPlugin implements capacitorAlarmPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}
