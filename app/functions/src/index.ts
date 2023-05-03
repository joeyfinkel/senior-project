import { credential, firestore, messaging } from 'firebase-admin';
import { initializeApp } from 'firebase-admin/app';
import * as functions from 'firebase-functions';
import { client_email, private_key, project_id } from '../serviceAccount.json';
import { UpdateNotificationBody } from './types';
import { getRandomTime } from './utils';
import axios from 'axios';

initializeApp({
  credential: credential.cert({
    clientEmail: client_email,
    privateKey: private_key,
    projectId: project_id,
  }),
  databaseURL: 'https://writenow-cc43f-default-rtdb.firebaseio.com/',
});

const db = firestore();
// db.settings({ ignoreUndefinedProperties: true });

const getCurrentUser = async (userId: string) => {
  const collectionRef = db.collection('notificationInformation');
  const currentUserDocument = await collectionRef
    .where('userId', '==', userId)
    .get();

  return currentUserDocument;
};

export const updateNotificationInformation = functions.https.onRequest(
  async (req, res) => {
    const { endTime, startTime, userId, username, token } =
      req.body as UpdateNotificationBody;
    const collectionRef = db.collection('notificationInformation');
    const currentUserDocument = await getCurrentUser(userId);
    let docId: { docId: string } = { docId: '' };

    if (currentUserDocument.docs.length === 1) {
      currentUserDocument.forEach((doc) => {
        doc.ref.update({ startTime, endTime });
      });
    } else {
      collectionRef
        .add({ userId, startTime, endTime, username })
        .then((doc) => {
          console.log('Document written with ID: ', doc.id);
          docId.docId = doc.id;
        })
        .catch((error) => {
          res.status(500).send({ error: `Error writing document (${error})` });
        });
    }

    axios
      .get(
        `https://us-central1-writenow-cc43f.cloudfunctions.net/scheduleMessage?userId=${userId}}&token=${token}`
      )
      .then((response) => {
        res
          .status(200)
          .send({ message: 'Successfully scheduled message', ...docId });
      })
      .catch((error) => {
        res.status(500).send({ message: 'Error scheduling message' });
      });
  }
);

export const scheduleMessage = functions.https.onRequest(async (req, res) => {
  const { userId, token } = req.query;
  const collectionRef = db
    .collection('notificationInformation')
    .where('userId', '==', parseInt((userId ?? '') as string, 10));

  collectionRef.get().then((qs) => {
    if (qs.docs.length > 0) {
      const { endTime, startTime, username } =
        qs.docs[0].data() as UpdateNotificationBody;
      const randomTime = getRandomTime(startTime, endTime); // Use this for scheduling

      if (randomTime.getTime() === Date.now()) {
        const title = 'WriteNow';
        const body = `Hello ${username}, it is time to write!`;

        messaging()
          .send({
            notification: { title, body },
            token: token as string,
            android: {
              priority: 'high',
              notification: { priority: 'high', title, body },
            },
          })
          .then(() => {
            console.log(`Successfully sent message at ${randomTime}`);
            res
              .status(200)
              .send({ message: `Notification sent at ${randomTime}` });
          })
          .catch((error) => {
            console.error('Error sending message:', error);
            res.status(500).send({ message: 'Error sending notification' });
          });
      }
      res.status(200).send({ message: 'User found', data: qs.docs[0].data() });
    } else {
      res.status(500).send({ message: 'User not found' });
    }
  });
});
