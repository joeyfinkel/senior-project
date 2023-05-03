type AM = 'AM' | 'am';
type PM = 'PM' | 'pm';

export type Time = `${string}:${string} ${AM}` | `${string}:${string} ${PM}`;
export type UpdateNotificationBody = {
  username: string;
  token: string;
  userId: string;
  startTime: Time;
  endTime: Time;
};
export type ScheduleNotificationBody = UpdateNotificationBody & {
  token: string;
  title: string;
  body: string;
  activeDays: string;
};
