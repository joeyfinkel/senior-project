import * as functions from 'firebase-functions';
import { ScheduleNotificationBody, Time } from './types';
import { messaging, credential } from 'firebase-admin';
import { initializeApp } from 'firebase-admin/app';
// import * as express from 'express';
import { client_email, private_key, project_id } from '../serviceAccount.json';

initializeApp({
  credential: credential.cert({
    clientEmail: client_email,
    privateKey: private_key,
    projectId: project_id,
  }),
  databaseURL: 'https://writenow-cc43f-default-rtdb.firebaseio.com/',
});

// const app = express();

function stringToTime(startTime: Time, endTime: Time) {
  const start = startTime.split(':');
  const end = endTime.split(':');

  return {
    start: { hours: parseInt(start[0]), minutes: parseInt(start[1]) },
    end: { hours: parseInt(end[0]), minutes: parseInt(end[0]) },
  };
}

function getRandomTime(time: ReturnType<typeof stringToTime>) {
  const { end, start } = time;
  const startTime = new Date();
  const endTime = new Date();
  const randomTime = new Date();

  startTime.setHours(start.hours, start.minutes, 0, 0);
  endTime.setHours(end.minutes, end.minutes, 0, 0);

  const startTimestamp = startTime.getTime();
  const endTimestamp = endTime.getTime();

  const randomTimestamp =
    startTimestamp + Math.random() * (endTimestamp - startTimestamp);

  randomTime.setTime(randomTimestamp);

  console.log({
    utc: randomTime.toUTCString(),
    local: randomTime.toLocaleString(),
    iso: randomTime.toISOString(),
  });

  return randomTime;
}

// app.post<'/register-device', {}, {}, { userId: string; token: string }>(
//   '/register-device',
//   async (req, res) => {
//     const { userId, token } = req.body;

//     await database().ref(`/users/${userId}/deviceTokens`).push({ token });

//     res.status(200).send('Device registered');
//   }
// );

// export const scheduleMessage = functions.database
//   .ref('/notifications/{userId}/{notificationId}')
//   .onCreate(async (snapshot, context) => {
//     const { notificationId } = context.params;
//     const { endTime, startTime, token, body, title } =
//       snapshot.val() as ScheduleNotificationBody;
//     const time = stringToTime(startTime, endTime);
//     const randomTime = getRandomTime(time); // Use this for scheduling

//     console.log({ randomTime });

//     messaging()
//       .send({ notification: { title, body }, data: { notificationId }, token })
//       .then((response) => {
//         console.log('Successfully sent message:', response);
//       })
//       .catch((error) => {
//         console.error('Error sending message:', error);
//       });
//   });

export const scheduleMessage = functions.https.onRequest(
  async (req, res: functions.Response) => {
    const { endTime, startTime, token, body, title } =
      req.body as ScheduleNotificationBody;
    const time = stringToTime(startTime, endTime);
    const randomTime = getRandomTime(time); // Use this for scheduling

    console.log({ randomTime });

    messaging()
      .send({ notification: { title, body }, token })
      .then((response) => {
        console.log('Successfully sent message:', response);
        res.status(200).send('Notification sent');
      })
      .catch((error) => {
        console.error('Error sending message:', error);
        res.status(500).send('Error sending notification');
      });
  }
);

// export const api = functions.https.onRequest(app);
