import dayjs from 'dayjs/esm';

export interface IDailyTaskLog {
  id: number;
  employee?: string | null;
  customer?: string | null;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  breakTotalMinutes?: number | null;
}

export type NewDailyTaskLog = Omit<IDailyTaskLog, 'id'> & { id: null };
