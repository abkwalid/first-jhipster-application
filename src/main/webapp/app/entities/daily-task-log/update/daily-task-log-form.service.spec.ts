import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../daily-task-log.test-samples';

import { DailyTaskLogFormService } from './daily-task-log-form.service';

describe('DailyTaskLog Form Service', () => {
  let service: DailyTaskLogFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DailyTaskLogFormService);
  });

  describe('Service methods', () => {
    describe('createDailyTaskLogFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDailyTaskLogFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            employee: expect.any(Object),
            customer: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            breakTotalMinutes: expect.any(Object),
          })
        );
      });

      it('passing IDailyTaskLog should create a new form with FormGroup', () => {
        const formGroup = service.createDailyTaskLogFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            employee: expect.any(Object),
            customer: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            breakTotalMinutes: expect.any(Object),
          })
        );
      });
    });

    describe('getDailyTaskLog', () => {
      it('should return NewDailyTaskLog for default DailyTaskLog initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createDailyTaskLogFormGroup(sampleWithNewData);

        const dailyTaskLog = service.getDailyTaskLog(formGroup) as any;

        expect(dailyTaskLog).toMatchObject(sampleWithNewData);
      });

      it('should return NewDailyTaskLog for empty DailyTaskLog initial value', () => {
        const formGroup = service.createDailyTaskLogFormGroup();

        const dailyTaskLog = service.getDailyTaskLog(formGroup) as any;

        expect(dailyTaskLog).toMatchObject({});
      });

      it('should return IDailyTaskLog', () => {
        const formGroup = service.createDailyTaskLogFormGroup(sampleWithRequiredData);

        const dailyTaskLog = service.getDailyTaskLog(formGroup) as any;

        expect(dailyTaskLog).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDailyTaskLog should not enable id FormControl', () => {
        const formGroup = service.createDailyTaskLogFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDailyTaskLog should disable id FormControl', () => {
        const formGroup = service.createDailyTaskLogFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
