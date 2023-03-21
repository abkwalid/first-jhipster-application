import dayjs from 'dayjs/esm';

import { IDailyTaskLog, NewDailyTaskLog } from './daily-task-log.model';

export const sampleWithRequiredData: IDailyTaskLog = {
  id: 11091,
  employee: 'International transmit',
  customer: 'Cameroon panel cross-platform',
};

export const sampleWithPartialData: IDailyTaskLog = {
  id: 94641,
  employee: 'Soft',
  customer: 'input',
  endDate: dayjs('2023-03-21T12:26'),
};

export const sampleWithFullData: IDailyTaskLog = {
  id: 50179,
  employee: 'Corporate synthesizing input',
  customer: 'Tasty Awesome Corporate',
  startDate: dayjs('2023-03-21T16:19'),
  endDate: dayjs('2023-03-21T18:39'),
  breakTotalMinutes: 92728,
};

export const sampleWithNewData: NewDailyTaskLog = {
  employee: 'Refined Saint',
  customer: 'Borders',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
