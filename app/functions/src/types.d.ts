export type Time = `${string}:${string} AM` | `${string}:${string} PM`;
export type ScheduleNotificationBody = {
  timezone: string;
  token: string;
  title: string;
  body: string;
  activeDays: string;
  startTime: Time;
  endTime: Time;
};
