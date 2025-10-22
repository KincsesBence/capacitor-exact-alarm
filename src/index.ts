import { registerPlugin } from '@capacitor/core';

import type { capacitorAlarmPlugin } from './definitions';

const capacitorAlarm = registerPlugin<capacitorAlarmPlugin>('capacitorAlarm', {
  web: () => import('./web').then((m) => new m.capacitorAlarmWeb()),
});

export * from './definitions';
export { capacitorAlarm };
