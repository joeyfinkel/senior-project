import { firestore } from 'firebase-admin';
import * as functions from 'firebase-functions';
import { ScheduleNotificationBody } from './types';

const db = firestore();

export const updateNotificationInformation = functions.https.onRequest(
  async (req, res) => {
    const { userId, startTime, endTime } = req.body as ScheduleNotificationBody;
    const collectionRef = db.collection('notificationInformation');

    collectionRef
      .add({ userId, startTime, endTime })
      .then((docRef) => {
        console.log('Document written with ID: ', docRef.id);
        res.status(200).send({ docId: docRef.id });
      })
      .catch((error) => {
        res.status(500).send({ error: `Error writing document (${error})` });
      });
  }
);
