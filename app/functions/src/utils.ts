import { Time } from './types';

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

export function getRandomTime(startTime: Time, endTime: Time) {
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
