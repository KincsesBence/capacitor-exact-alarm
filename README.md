# capacitor-alarm

Alarm clock scheduler

## Install

```bash
npm install capacitor-alarm
npx cap sync
```

## API

<docgen-index>

* [`setAlarm(...)`](#setalarm)
* [`cancelAlarm(...)`](#cancelalarm)
* [`cancelAllAlarm()`](#cancelallalarm)
* [`requestExactAlarmPermission()`](#requestexactalarmpermission)
* [`requestNotificationPermission()`](#requestnotificationpermission)
* [`checkNotificationPermission()`](#checknotificationpermission)
* [`checkExactAlarmPermission()`](#checkexactalarmpermission)
* [`getAlarms()`](#getalarms)
* [`pickAlarmSound()`](#pickalarmsound)
* [`stopAlarm()`](#stopalarm)
* [`addListener('alarmTriggered', ...)`](#addlisteneralarmtriggered-)
* [`addListener('alarmNotificationTapped', ...)`](#addlisteneralarmnotificationtapped-)
* [`removeAllListeners()`](#removealllisteners)
* [Interfaces](#interfaces)
* [Type Aliases](#type-aliases)
* [Enums](#enums)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

Main Capacitor Alarm Plugin Interface.

### setAlarm(...)

```typescript
setAlarm(alarm: Alarm) => Promise<Alarm>
```

Schedule an alarm.

Supports:
- Exact timestamp alarms
- Repeating alarms
- Weekly <a href="#calendar">calendar</a> alarms

## Examples

### One-time alarm
```ts
await capacitorAlarm.setAlarm({
  timestamp: Date.now() + 10_000,
  title: "One-time Alarm",
  msg: "This alarm will ring once.",
  soundName: "content://media/internal/audio/media/21"
});
```

### Repeating alarm (every 15 minutes)
```ts
await capacitorAlarm.setAlarm({
  repeatInterval: 1000 * 60 * 15,
  title: "Repeating Alarm",
  msg: "This alarm repeats every 15 minutes.",
  soundName: "content://media/internal/audio/media/33"
});
```

### Weekly alarm (every Monday at 7:30)
```ts
await capacitorAlarm.setAlarm({
  calendar: {
    weekday: Weekday.Monday
    hour: 7,
    minute: 30,
  },
  title: "Weekly Alarm",
  msg: "It's Monday at 7:30!",
  soundName: "content://media/internal/audio/media/12"
});
```

| Param       | Type                                    |
| ----------- | --------------------------------------- |
| **`alarm`** | <code><a href="#alarm">Alarm</a></code> |

**Returns:** <code>Promise&lt;<a href="#alarm">Alarm</a>&gt;</code>

--------------------


### cancelAlarm(...)

```typescript
cancelAlarm(alarm: cancelAlarm) => Promise<void>
```

Cancel a specific alarm using its ID.

```ts
await capacitorAlarm.<a href="#cancelalarm">cancelAlarm</a>({ alarmId: 1 });
```

| Param       | Type                                                |
| ----------- | --------------------------------------------------- |
| **`alarm`** | <code><a href="#cancelalarm">cancelAlarm</a></code> |

--------------------


### cancelAllAlarm()

```typescript
cancelAllAlarm() => Promise<void>
```

Cancel all scheduled alarms.

```ts
await capacitorAlarm.cancelAllAlarm();
```

--------------------


### requestExactAlarmPermission()

```typescript
requestExactAlarmPermission() => Promise<void>
```

Request permission for exact alarms (Android 12+).

```ts
await capacitorAlarm.requestExactAlarmPermission();
```

--------------------


### requestNotificationPermission()

```typescript
requestNotificationPermission() => Promise<void>
```

Request notification permission (Android 13+).

```ts
await capacitorAlarm.requestNotificationPermission();
```

--------------------


### checkNotificationPermission()

```typescript
checkNotificationPermission() => Promise<checkResult>
```

Check notification permission status.

```ts
const res = await capacitorAlarm.checkNotificationPermission();
console.log(res.hasPermission);
```

**Returns:** <code>Promise&lt;<a href="#checkresult">checkResult</a>&gt;</code>

--------------------


### checkExactAlarmPermission()

```typescript
checkExactAlarmPermission() => Promise<checkResult>
```

Check exact alarm permission status.

```ts
const res = await capacitorAlarm.checkExactAlarmPermission();
console.log(res.hasPermission);
```

**Returns:** <code>Promise&lt;<a href="#checkresult">checkResult</a>&gt;</code>

--------------------


### getAlarms()

```typescript
getAlarms() => Promise<alarmResult>
```

Retrieve all currently scheduled alarms.

```ts
const { alarms } = await capacitorAlarm.getAlarms();
console.log(alarms);
```

**Returns:** <code>Promise&lt;<a href="#alarmresult">alarmResult</a>&gt;</code>

--------------------


### pickAlarmSound()

```typescript
pickAlarmSound() => Promise<AlarmSoundResult>
```

Open the Android ringtone picker and return the selected sound URI.

```ts
const sound = await capacitorAlarm.pickAlarmSound();
console.log("Selected sound:", sound.uri);
```

**Returns:** <code>Promise&lt;<a href="#alarmsoundresult">AlarmSoundResult</a>&gt;</code>

--------------------


### stopAlarm()

```typescript
stopAlarm() => Promise<void>
```

Stop the currently ringing alarm sound.

```ts
await capacitorAlarm.stopAlarm();
```

--------------------


### addListener('alarmTriggered', ...)

```typescript
addListener(eventName: "alarmTriggered", listenerFunc: (data: Alarm) => void) => Promise<PluginListenerHandle>
```

Triggered when an alarm fires.

```ts
capacitorAlarm.addListener("alarmTriggered", (alarm) =&gt; {
  console.log("<a href="#alarm">Alarm</a> fired:", alarm);
});
```

| Param              | Type                                                       |
| ------------------ | ---------------------------------------------------------- |
| **`eventName`**    | <code>'alarmTriggered'</code>                              |
| **`listenerFunc`** | <code>(data: <a href="#alarm">Alarm</a>) =&gt; void</code> |

**Returns:** <code>Promise&lt;<a href="#pluginlistenerhandle">PluginListenerHandle</a>&gt;</code>

--------------------


### addListener('alarmNotificationTapped', ...)

```typescript
addListener(eventName: "alarmNotificationTapped", listenerFunc: (data: Alarm) => void) => Promise<PluginListenerHandle>
```

Triggered when the user taps the alarm notification.

```ts
capacitorAlarm.addListener("alarmNotificationTapped", (alarm) =&gt; {
  console.log("Notification tapped:", alarm);
});
```

| Param              | Type                                                       |
| ------------------ | ---------------------------------------------------------- |
| **`eventName`**    | <code>'alarmNotificationTapped'</code>                     |
| **`listenerFunc`** | <code>(data: <a href="#alarm">Alarm</a>) =&gt; void</code> |

**Returns:** <code>Promise&lt;<a href="#pluginlistenerhandle">PluginListenerHandle</a>&gt;</code>

--------------------


### removeAllListeners()

```typescript
removeAllListeners() => Promise<void>
```

Remove all registered listeners.

```ts
await capacitorAlarm.removeAllListeners();
```

--------------------


### Interfaces


#### calendar

## Calendar Schedule Properties

| Property | Type            | Description |
|----------|----------------|-------------|
| weekday  | <a href="#weekday">Weekday</a>        | Day of the week (Sunday–Saturday). |
| hour     | number         | Hour (0–23). |
| minute   | number         | Minute (0–59). |

| Prop          | Type                                        |
| ------------- | ------------------------------------------- |
| **`weekday`** | <code><a href="#weekday">Weekday</a></code> |
| **`hour`**    | <code>number</code>                         |
| **`minute`**  | <code>number</code>                         |


#### cancelAlarm

Payload to cancel an alarm.

| Prop          | Type                |
| ------------- | ------------------- |
| **`alarmId`** | <code>number</code> |


#### checkResult

Permission status object.

| Prop                | Type                 |
| ------------------- | -------------------- |
| **`hasPermission`** | <code>boolean</code> |


#### alarmResult

Returned when retrieving alarms.

| Prop         | Type                 |
| ------------ | -------------------- |
| **`alarms`** | <code>Alarm[]</code> |


#### AlarmSoundResult

Returned when selecting an alarm sound.

| Prop      | Type                |
| --------- | ------------------- |
| **`uri`** | <code>string</code> |


#### PluginListenerHandle

| Prop         | Type                                      |
| ------------ | ----------------------------------------- |
| **`remove`** | <code>() =&gt; Promise&lt;void&gt;</code> |


### Type Aliases


#### Alarm

## <a href="#alarm">Alarm</a> Properties

| Property        | Type       | Description |
|----------------|------------|-------------|
| id             | number?    | Unique ID of the alarm (auto-generated when created). |
| timestamp      | number?    | Unix timestamp (ms) when the alarm should fire. |
| <a href="#calendar">calendar</a>       | <a href="#calendar">calendar</a>?  | Calendar-based schedule (weekly / monthly / daily). |
| repeatInterval | number?    | Repeating interval in milliseconds. |
| title          | string     | Notification title. |
| msg            | string     | Notification message. |
| soundName      | string     | URI of the alarm sound. |
| icon           | string?    | Android notification icon name. |
| dismissText    | string?    | Text for the dismiss action button. |
| missedText     | string?    | Text shown for missed alarms. |
| data           | any?       | Additional custom data returned on events. |

<code>{ id?: number; timestamp?: number; <a href="#calendar">calendar</a>?: <a href="#calendar">calendar</a>; repeatInterval?: number; title: string; msg: string; soundName: string; icon?: string; dismissText?: string; missedText?: string; data?: any; }</code>


### Enums


#### Weekday

| Members         | Value          |
| --------------- | -------------- |
| **`Sunday`**    | <code>1</code> |
| **`Monday`**    | <code>2</code> |
| **`Tuesday`**   | <code>3</code> |
| **`Wednesday`** | <code>4</code> |
| **`Thursday`**  | <code>5</code> |
| **`Friday`**    | <code>6</code> |
| **`Saturday`**  | <code>7</code> |

</docgen-api>
