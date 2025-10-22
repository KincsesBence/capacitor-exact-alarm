export interface capacitorAlarmPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}
