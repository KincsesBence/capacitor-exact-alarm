import { registerPlugin } from '@capacitor/core';

import type { capacitorExactAlarmPlugin } from './definitions';

const capacitorExactAlarm = registerPlugin<capacitorExactAlarmPlugin>('capacitorAlarm', {
  web: () => import('./web').then((m) => new m.capacitorExactAlarmWeb()),
});

export * from './definitions';
export { capacitorExactAlarm };
