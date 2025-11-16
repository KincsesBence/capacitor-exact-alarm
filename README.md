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

Capacitor Alarm Plugin

Provides the ability to schedule exact alarms on Android.

Features:
- One-time alarms
- Repeating alarms
- Weekly calendar alarms
- Alarm notifications
- Ringtone picker
- Event listeners for alarm triggers & notification taps

### setAlarm(...)

```typescript
setAlarm(alarm: Alarm) => Promise<Alarm>
```

Schedule an alarm.

Alarm may be based on:
- A timestamp
- A repeat interval
- A weekly <a href="#calendar">calendar</a> schedule

| Param       | Type                                    |
| ----------- | --------------------------------------- |
| **`alarm`** | <code><a href="#alarm">Alarm</a></code> |

**Returns:** <code>Promise&lt;<a href="#alarm">Alarm</a>&gt;</code>

--------------------


### cancelAlarm(...)

```typescript
cancelAlarm(alarm: cancelAlarm) => Promise<void>
```

Cancel a specific alarm.

| Param       | Type                                                |
| ----------- | --------------------------------------------------- |
| **`alarm`** | <code><a href="#cancelalarm">cancelAlarm</a></code> |

--------------------


### cancelAllAlarm()

```typescript
cancelAllAlarm() => Promise<void>
```

Cancel all alarms created by the plugin.

--------------------


### requestExactAlarmPermission()

```typescript
requestExactAlarmPermission() => Promise<void>
```

Request permission to schedule exact alarms (Android 12+).

--------------------


### requestNotificationPermission()

```typescript
requestNotificationPermission() => Promise<void>
```

Request notification permissions (Android 13+).

--------------------


### checkNotificationPermission()

```typescript
checkNotificationPermission() => Promise<checkResult>
```

Check if notification permissions were granted.

**Returns:** <code>Promise&lt;<a href="#checkresult">checkResult</a>&gt;</code>

--------------------


### checkExactAlarmPermission()

```typescript
checkExactAlarmPermission() => Promise<checkResult>
```

Check if exact alarm permissions were granted.

**Returns:** <code>Promise&lt;<a href="#checkresult">checkResult</a>&gt;</code>

--------------------


### getAlarms()

```typescript
getAlarms() => Promise<alarmResult>
```

Retrieve all currently scheduled alarms.

**Returns:** <code>Promise&lt;<a href="#alarmresult">alarmResult</a>&gt;</code>

--------------------


### pickAlarmSound()

```typescript
pickAlarmSound() => Promise<AlarmSoundResult>
```

Opens the Android ringtone picker and returns a selected sound URI.

**Returns:** <code>Promise&lt;<a href="#alarmsoundresult">AlarmSoundResult</a>&gt;</code>

--------------------


### stopAlarm()

```typescript
stopAlarm() => Promise<void>
```

Stop the currently ringing alarm sound.

--------------------


### addListener('alarmTriggered', ...)

```typescript
addListener(eventName: "alarmTriggered", listenerFunc: (data: Alarm) => void) => Promise<PluginListenerHandle>
```

Fired when the alarm goes off.

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

Fired when the user taps the alarm notification.

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

Remove all listeners registered through this plugin.

--------------------


### Interfaces


#### calendar

Weekly <a href="#calendar">calendar</a> schedule.

| Prop          | Type                                        |
| ------------- | ------------------------------------------- |
| **`weekday`** | <code><a href="#weekday">Weekday</a></code> |
| **`hour`**    | <code>number</code>                         |
| **`minute`**  | <code>number</code>                         |


#### cancelAlarm

Payload for canceling an alarm.

| Prop          | Type                |
| ------------- | ------------------- |
| **`alarmId`** | <code>number</code> |


#### checkResult

Permission check result.

| Prop                | Type                 |
| ------------------- | -------------------- |
| **`hasPermission`** | <code>boolean</code> |


#### alarmResult

Returned list of alarms.

| Prop         | Type                 |
| ------------ | -------------------- |
| **`alarms`** | <code>Alarm[]</code> |


#### AlarmSoundResult

Returned when selecting a ringtone.

| Prop      | Type                |
| --------- | ------------------- |
| **`uri`** | <code>string</code> |


#### PluginListenerHandle

| Prop         | Type                                      |
| ------------ | ----------------------------------------- |
| **`remove`** | <code>() =&gt; Promise&lt;void&gt;</code> |


### Type Aliases


#### Alarm

Represents a scheduled alarm.

<code>{ /** Unique ID of the alarm (auto-generated when created). */ id?: number; /** Unix timestamp in milliseconds when the alarm should fire. */ timestamp?: number; /** Calendar-based schedule (weekly / monthly / daily). */ <a href="#calendar">calendar</a>?: <a href="#calendar">calendar</a>; /** Repeating interval in milliseconds. */ repeatInterval?: number; /** Notification title. */ title: string; /** Notification message. */ msg: string; /** URI of the alarm sound. */ soundName: string; /** Android notification icon name. */ icon?: string; /** Text for the dismiss action button. */ dismissText?: string; /** Text shown for missed alarms. */ missedText?: string; /** Additional custom data passed back on events. */ data?: any; }</code>


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
