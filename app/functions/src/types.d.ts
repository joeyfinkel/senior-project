type AM = 'AM' | 'am';
type PM = 'PM' | 'pm';

export type Time = `${string}:${string} ${AM}` | `${string}:${string} ${PM}`;
export type ScheduleNotificationBody = {
  token: string;
  title: string;
  body: string;
  activeDays: string;
  userId: number;
  startTime: Time;
  endTime: Time;
};
