Learning about notifications in Android


It seems like there's two steps you have to do:
1. Create a repeating Alarm that fires off the AlarmReceiver at the specified time/interval
1. In the alarm receiver, fire off a local notification that will display to the user

The alarm can carry a request code, so I think I'll use that to send the reminder id. That way
if you cancel/delete/update a reminder, it you can cancel and reinstate only that alarm.

I'll store the reminders in a db or something:
`id, start time, interval, days of the week`



ok, so....how does this work. the alarm can be:
daily, weekly, monthly



