import functions from 'firebase-functions';
import admin from 'firebase-admin';

const db = admin.firestore();

functions.pubsub
  .schedule('0 8 * * *')
  .timeZone('America/New_York')
  .onRun(async (context) => {
    // Get the user's device token from the database
    const userDoc = await admin
      .firestore()
      .collection('users')
      .doc('USER_ID')
      .get();
    const deviceToken = userDoc.data().deviceToken;

    // Send the notification
    const payload = {
      notification: {
        title: 'Good morning!',
        body: "It's time to start your day.",
        click_action: 'FLUTTER_NOTIFICATION_CLICK',
      },
    };
    await admin.messaging().sendToDevice(deviceToken, payload);

    // Log that the notification was sent
    console.log('Morning notification sent to device with token:', deviceToken);

    return null;
  });