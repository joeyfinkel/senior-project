import { ScheduleNotificationBody } from './types';
import * as functions from 'firebase-functions';
import { getRandomTime } from './utils';
import { messaging } from 'firebase-admin';

export const scheduleMessage = functions.https.onRequest(async (req, res) => {
  const { endTime, startTime, token, body, title } =
    req.body as ScheduleNotificationBody;
  const randomTime = getRandomTime(startTime, endTime); // Use this for scheduling

  if (randomTime.getTime() === Date.now()) {
    messaging()
      .send({
        notification: { title, body },
        token,
        android: {
          priority: 'high',
          notification: { priority: 'high', title, body },
        },
      })
      .then((response) => {
        console.log(`Successfully sent message at ${randomTime}`);
        res.status(200).send(`Notification sent at ${randomTime}`);
      })
      .catch((error) => {
        console.error('Error sending message:', error);
        res.status(500).send('Error sending notification');
      });
  }
});