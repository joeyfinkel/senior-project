import * as functions from 'firebase-functions';
// import { ScheduleNotificationBody } from './types';
// import { PubSub } from '@google-cloud/pubsub';
import { ScheduleNotificationBody } from './types';
import { initializeApp, messaging } from 'firebase-admin';

initializeApp();

const stringToTime = (startTime: string, endTime: string) => {
  const start = startTime.split(':');
  const end = endTime.split(':');

  return {
    start: { hours: parseInt(start[0]), minutes: parseInt(start[1]) },
    end: { hours: parseInt(end[0]), minutes: parseInt(end[0]) },
  };
};

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

export const scheduleNotification = functions.https.onRequest(
  async (req, res: functions.Response) => {
    const { endTime, startTime, token } = req.body as ScheduleNotificationBody;
    const time = stringToTime(startTime, endTime);
    const randomTime = getRandomTime(time);

    messaging()
      .send({
        notification: {
          title: 'Notification',
          body: `At ${randomTime}`,
        },
        token,
      })
      .then((response) => {
        console.log('Successfully sent message:', response);
        res.status(200).send('Notification sent');
      })
      .catch((error) => {
        console.error('Error sending message:', error);
        res.status(500).send('Error sending notification');
      });

    // // Schedule a Cloud Function to send the notification at the specified time
    // functions.pubsub.schedule('40 19 * * * *').onRun(() => {
    //   console.log('This will be run every day at 7:40 pm!');
    //   res.send('This will be run every day at 7:40 pm!');
    // });

    // functions.pubsub.topic('scheduled-notification');
    // scheduledFunction.onRun((context) => {
    //   context.params;
    // });

    // scheduledFunction.timeZone(timezone);

    // res
    //   .status(200)
    //   .send(
    //     `Notification scheduled successfully on ${activeDays.join(
    //       ', '
    //     )} between ${time.start} and ${time.end}`
    //   );
    // res.send({
    //   message: 'Hello from Firebase!',
    //   timezone,
    //   activeDays,
    //   randomTime,
    // });
  }
);

// export const scheduleMessage = functions.https.onRequest((req, res) => {
//   const { message, time } = req.body;
//   const pubsub = new PubSub();
//   const topicName = 'my-topic';
//   const topic = pubsub.topic(topicName);

//   const dataBuffer = Buffer.from(JSON.stringify({ message }));
//   const options = {
//     publishTime: time.toISOString(),
//   };

//   topic.publish(dataBuffer, options, (err, messageIds) => {
//     if (err) {
//       console.error(err);
//       res.status(500).send('Error scheduling message');
//     } else {
//       console.log(`Message ${message} published at ${time.toISOString()}`);
//       res.status(200).send(`Message scheduled for ${time.toISOString()}`);
//     }
//   });
// });
