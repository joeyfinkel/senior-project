import functions from 'firebase-functions';
import admin from 'firebase-admin';

type ActiveTime = { start: string; end: string };
type Body = {
  timezone: string;
  activeTime: ActiveTime;
  activeDays: string[];
};

const db = admin.firestore();

functions.https.onRequest(async (req, res: functions.Response) => {
  const { timezone, activeDays, activeTime } = req.body;

  // Schedule a Cloud Function to send the notification at the specified time
  const scheduledFunction = functions.pubsub.schedule(
    notificationMoment.format()
  );
  scheduledFunction.timeZone(userTimezone);
  scheduledFunction.topic('<YOUR_TOPIC_NAME>');
  scheduledFunction.data({ notificationMessage });

  console.log(
    `Scheduled notification: ${notificationMessage} at ${notificationMoment.format()}`
  );

  res.status(200).send('Notification scheduled successfully');
});
