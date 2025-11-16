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

Main Capacitor plugin interface.

### setAlarm(...)

```typescript
setAlarm(alarm: Alarm) => Promise<Alarm>
```

Schedule an alarm.
------------------
You can schedule alarms in 3 ways:

1. **Timestamp** alarm  
    `{ timestamp: 1724455000000 }`

2. **Repeat interval** alarm  
    `{ repeatInterval: 1000 * 60 * 30 }`

3. **Calendar-based** alarm  
    `{ <a href="#calendar">calendar</a>: { weekday: <a href="#weekday">Weekday.Monday</a>, hour: 9, minute: 0 } }`

| Param       | Type                                    | Description                               |
| ----------- | --------------------------------------- | ----------------------------------------- |
| **`alarm`** | <code><a href="#alarm">Alarm</a></code> | <a href="#alarm">Alarm</a> configuration. |

**Returns:** <code>Promise&lt;<a href="#alarm">Alarm</a>&gt;</code>

--------------------


### cancelAlarm(...)

```typescript
cancelAlarm(alarm: cancelAlarm) => Promise<void>
```

Cancel a specific alarm.

| Param       | Type                                                | Description                    |
| ----------- | --------------------------------------------------- | ------------------------------ |
| **`alarm`** | <code><a href="#cancelalarm">cancelAlarm</a></code> | Object containing the alarmId. |

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

Request notification permission (Android 13+).

--------------------


### checkNotificationPermission()

```typescript
checkNotificationPermission() => Promise<checkResult>
```

Check if notification permission is granted.

**Returns:** <code>Promise&lt;<a href="#checkresult">checkResult</a>&gt;</code>

--------------------


### checkExactAlarmPermission()

```typescript
checkExactAlarmPermission() => Promise<checkResult>
```

Check whether exact alarms are allowed.

**Returns:** <code>Promise&lt;<a href="#checkresult">checkResult</a>&gt;</code>

--------------------


### getAlarms()

```typescript
getAlarms() => Promise<alarmResult>
```

Get a list of all active alarms stored by the plugin.

**Returns:** <code>Promise&lt;<a href="#alarmresult">alarmResult</a>&gt;</code>

--------------------


### pickAlarmSound()

```typescript
pickAlarmSound() => Promise<AlarmSoundResult>
```

Opens the Android ringtone picker to select an alarm sound.

**Returns:** <code>Promise&lt;<a href="#alarmsoundresult">AlarmSoundResult</a>&gt;</code>

--------------------


### stopAlarm()

```typescript
stopAlarm() => Promise<void>
```

Stops the currently ringing alarm sound.

--------------------


### addListener('alarmTriggered', ...)

```typescript
addListener(eventName: "alarmTriggered", listenerFunc: (data: Alarm) => void) => Promise<PluginListenerHandle>
```

Listener fired when the alarm triggers.
(The device will send a broadcast event.)

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

Listener fired when the user taps the alarm notification.

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

Removes all event listeners.

--------------------


### Interfaces


#### calendar

Calendar-based alarm schedule.
Used for weekly, monthly, or daily time schedules.

| Prop          | Type                                        |
| ------------- | ------------------------------------------- |
| **`weekday`** | <code><a href="#weekday">Weekday</a></code> |
| **`hour`**    | <code>number</code>                         |
| **`minute`**  | <code>number</code>                         |


#### cancelAlarm

Used to cancel a specific alarm.

| Prop          | Type                |
| ------------- | ------------------- |
| **`alarmId`** | <code>number</code> |


#### checkResult

Permission check result.

| Prop                | Type                 |
| ------------------- | -------------------- |
| **`hasPermission`** | <code>boolean</code> |


#### alarmResult

Returned list of stored alarms.

| Prop         | Type                 |
| ------------ | -------------------- |
| **`alarms`** | <code>Alarm[]</code> |


#### AlarmSoundResult

Result returned from alarm sound picker.

| Prop      | Type                |
| --------- | ------------------- |
| **`uri`** | <code>string</code> |


#### PluginListenerHandle

| Prop         | Type                                      |
| ------------ | ----------------------------------------- |
| **`remove`** | <code>() =&gt; Promise&lt;void&gt;</code> |


### Type Aliases


#### Alarm

<a href="#alarm">Alarm</a> object used for scheduling an alarm.

<code>{ id?: number; // auto-generated when setAlarm() is called timestamp?: number; // Unix timestamp in ms (for one-time alarms) <a href="#calendar">calendar</a>?: <a href="#calendar">calendar</a>; // Weekly / monthly / daily <a href="#calendar">calendar</a> schedule repeatInterval?: number; // ms interval for repeating alarms title: string; // Notification title msg: string; // Notification message soundName: string; // URI of the alarm sound icon?: string; // Notification icon name (Android res) dismissText?: string; // Button text for dismiss action missedText?: string; // Text shown for missed alarms data?: any; // Additional custom data included in events }</code>


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
