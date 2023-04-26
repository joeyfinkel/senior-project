export type ScheduleNotificationBody = {
  timezone: string;
  activeDays: string[];
  startTime: string;
  endTime: string;
  token: string;
};
