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
* [`checkExactAlarmPermission()`](#checkexactalarmpermission)
* [`checkNotificationPermission()`](#checknotificationpermission)
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

### setAlarm(...)

```typescript
setAlarm(alarm: Alarm) => Promise<Alarm>
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

| Param       | Type                                                |
| ----------- | --------------------------------------------------- |
| **`alarm`** | <code><a href="#cancelalarm">cancelAlarm</a></code> |

--------------------


### cancelAllAlarm()

```typescript
cancelAllAlarm() => Promise<void>
```

--------------------


### requestExactAlarmPermission()

```typescript
requestExactAlarmPermission() => Promise<void>
```

--------------------


### requestNotificationPermission()

```typescript
requestNotificationPermission() => Promise<void>
```

--------------------


### checkExactAlarmPermission()

```typescript
checkExactAlarmPermission() => Promise<checkResult>
```

**Returns:** <code>Promise&lt;<a href="#checkresult">checkResult</a>&gt;</code>

--------------------


### checkNotificationPermission()

```typescript
checkNotificationPermission() => Promise<checkResult>
```

**Returns:** <code>Promise&lt;<a href="#checkresult">checkResult</a>&gt;</code>

--------------------


### getAlarms()

```typescript
getAlarms() => Promise<alarmResult>
```

**Returns:** <code>Promise&lt;<a href="#alarmresult">alarmResult</a>&gt;</code>

--------------------


### pickAlarmSound()

```typescript
pickAlarmSound() => Promise<AlarmSoundResult>
```

**Returns:** <code>Promise&lt;<a href="#alarmsoundresult">AlarmSoundResult</a>&gt;</code>

--------------------


### stopAlarm()

```typescript
stopAlarm() => Promise<void>
```

--------------------


### addListener('alarmTriggered', ...)

```typescript
addListener(eventName: 'alarmTriggered', listenerFunc: (data: Alarm) => void) => Promise<PluginListenerHandle>
```

| Param              | Type                                                       |
| ------------------ | ---------------------------------------------------------- |
| **`eventName`**    | <code>'alarmTriggered'</code>                              |
| **`listenerFunc`** | <code>(data: <a href="#alarm">Alarm</a>) =&gt; void</code> |

**Returns:** <code>Promise&lt;<a href="#pluginlistenerhandle">PluginListenerHandle</a>&gt;</code>

--------------------


### addListener('alarmNotificationTapped', ...)

```typescript
addListener(eventName: 'alarmNotificationTapped', listenerFunc: (data: Alarm) => void) => Promise<PluginListenerHandle>
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

--------------------


### Interfaces


#### calendar

| Prop          | Type                                        |
| ------------- | ------------------------------------------- |
| **`weekday`** | <code><a href="#weekday">Weekday</a></code> |
| **`hour`**    | <code>number</code>                         |
| **`minute`**  | <code>number</code>                         |


#### cancelAlarm

| Prop          | Type                |
| ------------- | ------------------- |
| **`alarmId`** | <code>number</code> |


#### checkResult

| Prop                | Type                 |
| ------------------- | -------------------- |
| **`hasPermission`** | <code>boolean</code> |


#### alarmResult

| Prop         | Type                 |
| ------------ | -------------------- |
| **`alarms`** | <code>Alarm[]</code> |


#### AlarmSoundResult

| Prop      | Type                |
| --------- | ------------------- |
| **`uri`** | <code>string</code> |


#### PluginListenerHandle

| Prop         | Type                                      |
| ------------ | ----------------------------------------- |
| **`remove`** | <code>() =&gt; Promise&lt;void&gt;</code> |


### Type Aliases


#### Alarm

<code>{ id?:number, timestamp?: number, <a href="#calendar">calendar</a>?: <a href="#calendar">calendar</a>, repeatInterval?: number, title: string, msg: string, soundName: string, icon?:string, dismissText?:string, missedText?:string, data?:any }</code>


### Enums


#### Weekday

| Members         | Value          |
| --------------- | -------------- |
| **`Sunday`**    | <code>1</code> |
| **`Monday`**    |                |
| **`Tuesday`**   |                |
| **`Wednesday`** |                |
| **`Thursday`**  |                |
| **`Friday`**    |                |
| **`Saturday`**  |                |

</docgen-api>
