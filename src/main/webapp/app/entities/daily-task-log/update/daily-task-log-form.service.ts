import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IDailyTaskLog, NewDailyTaskLog } from '../daily-task-log.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDailyTaskLog for edit and NewDailyTaskLogFormGroupInput for create.
 */
type DailyTaskLogFormGroupInput = IDailyTaskLog | PartialWithRequiredKeyOf<NewDailyTaskLog>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IDailyTaskLog | NewDailyTaskLog> = Omit<T, 'startDate' | 'endDate'> & {
  startDate?: string | null;
  endDate?: string | null;
};

type DailyTaskLogFormRawValue = FormValueOf<IDailyTaskLog>;

type NewDailyTaskLogFormRawValue = FormValueOf<NewDailyTaskLog>;

type DailyTaskLogFormDefaults = Pick<NewDailyTaskLog, 'id' | 'startDate' | 'endDate'>;

type DailyTaskLogFormGroupContent = {
  id: FormControl<DailyTaskLogFormRawValue['id'] | NewDailyTaskLog['id']>;
  employee: FormControl<DailyTaskLogFormRawValue['employee']>;
  customer: FormControl<DailyTaskLogFormRawValue['customer']>;
  startDate: FormControl<DailyTaskLogFormRawValue['startDate']>;
  endDate: FormControl<DailyTaskLogFormRawValue['endDate']>;
  breakTotalMinutes: FormControl<DailyTaskLogFormRawValue['breakTotalMinutes']>;
};

export type DailyTaskLogFormGroup = FormGroup<DailyTaskLogFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DailyTaskLogFormService {
  createDailyTaskLogFormGroup(dailyTaskLog: DailyTaskLogFormGroupInput = { id: null }): DailyTaskLogFormGroup {
    const dailyTaskLogRawValue = this.convertDailyTaskLogToDailyTaskLogRawValue({
      ...this.getFormDefaults(),
      ...dailyTaskLog,
    });
    return new FormGroup<DailyTaskLogFormGroupContent>({
      id: new FormControl(
        { value: dailyTaskLogRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      employee: new FormControl(dailyTaskLogRawValue.employee, {
        validators: [Validators.required, Validators.minLength(3)],
      }),
      customer: new FormControl(dailyTaskLogRawValue.customer, {
        validators: [Validators.required, Validators.minLength(3)],
      }),
      startDate: new FormControl(dailyTaskLogRawValue.startDate),
      endDate: new FormControl(dailyTaskLogRawValue.endDate),
      breakTotalMinutes: new FormControl(dailyTaskLogRawValue.breakTotalMinutes),
    });
  }

  getDailyTaskLog(form: DailyTaskLogFormGroup): IDailyTaskLog | NewDailyTaskLog {
    return this.convertDailyTaskLogRawValueToDailyTaskLog(form.getRawValue() as DailyTaskLogFormRawValue | NewDailyTaskLogFormRawValue);
  }

  resetForm(form: DailyTaskLogFormGroup, dailyTaskLog: DailyTaskLogFormGroupInput): void {
    const dailyTaskLogRawValue = this.convertDailyTaskLogToDailyTaskLogRawValue({ ...this.getFormDefaults(), ...dailyTaskLog });
    form.reset(
      {
        ...dailyTaskLogRawValue,
        id: { value: dailyTaskLogRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): DailyTaskLogFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      startDate: currentTime,
      endDate: currentTime,
    };
  }

  private convertDailyTaskLogRawValueToDailyTaskLog(
    rawDailyTaskLog: DailyTaskLogFormRawValue | NewDailyTaskLogFormRawValue
  ): IDailyTaskLog | NewDailyTaskLog {
    return {
      ...rawDailyTaskLog,
      startDate: dayjs(rawDailyTaskLog.startDate, DATE_TIME_FORMAT),
      endDate: dayjs(rawDailyTaskLog.endDate, DATE_TIME_FORMAT),
    };
  }

  private convertDailyTaskLogToDailyTaskLogRawValue(
    dailyTaskLog: IDailyTaskLog | (Partial<NewDailyTaskLog> & DailyTaskLogFormDefaults)
  ): DailyTaskLogFormRawValue | PartialWithRequiredKeyOf<NewDailyTaskLogFormRawValue> {
    return {
      ...dailyTaskLog,
      startDate: dailyTaskLog.startDate ? dailyTaskLog.startDate.format(DATE_TIME_FORMAT) : undefined,
      endDate: dailyTaskLog.endDate ? dailyTaskLog.endDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
