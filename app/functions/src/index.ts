import * as functions from 'firebase-functions';
import { ScheduleNotificationBody, Time } from './types';
import { messaging, credential, firestore } from 'firebase-admin';
import { initializeApp } from 'firebase-admin/app';
// import * as express from 'express';
import { client_email, private_key, project_id } from './serviceAccount.json';

initializeApp({
  credential: credential.cert({
    clientEmail: client_email,
    privateKey: private_key,
    projectId: project_id,
  }),
  databaseURL: 'https://writenow-cc43f-default-rtdb.firebaseio.com/',
});

const db = firestore();

function getTime(time: Time) {
  const now = new Date();
  const [_time, period] = time.split(' ');
  let [hours, minutes] = _time.split(':');

  if (hours === '12') {
    hours = '00';
  }

  if (period.toLowerCase() === 'pm') {
    hours = (parseInt(hours, 10) + 12).toString();
  }

  return new Date(
    now.getFullYear(),
    now.getMonth(),
    now.getDate(),
    +hours,
    +minutes
  );
}

function getRandomTime(startTime: Time, endTime: Time) {
  const start = getTime(startTime);
  const end = getTime(endTime);
  const randomTime = new Date();
  const startTimestamp = start.getTime();
  const endTimestamp = end.getTime();
  const randomTimestamp =
    startTimestamp + Math.random() * (endTimestamp - startTimestamp);

  randomTime.setTime(randomTimestamp);

  return randomTime;
}

export const updateNotificationInformation = functions.https.onRequest(
  async (req, res) => {
    const { userId } = req.body as ScheduleNotificationBody;
  }
);

export const scheduleMessage = functions.https.onRequest(async (req, res) => {
  const { endTime, startTime, token, body, title, userId } =
    req.body as ScheduleNotificationBody;
  const randomTime = getRandomTime(startTime, endTime); // Use this for scheduling
  const collectionRef = db
    .collection('notificationInformation')
    .doc(userId.toString())
    .collection('notifications');

  console.log({ randomTime });

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
