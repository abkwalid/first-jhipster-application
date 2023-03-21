import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DailyTaskLogFormService } from './daily-task-log-form.service';
import { DailyTaskLogService } from '../service/daily-task-log.service';
import { IDailyTaskLog } from '../daily-task-log.model';

import { DailyTaskLogUpdateComponent } from './daily-task-log-update.component';

describe('DailyTaskLog Management Update Component', () => {
  let comp: DailyTaskLogUpdateComponent;
  let fixture: ComponentFixture<DailyTaskLogUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let dailyTaskLogFormService: DailyTaskLogFormService;
  let dailyTaskLogService: DailyTaskLogService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [DailyTaskLogUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(DailyTaskLogUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DailyTaskLogUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    dailyTaskLogFormService = TestBed.inject(DailyTaskLogFormService);
    dailyTaskLogService = TestBed.inject(DailyTaskLogService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const dailyTaskLog: IDailyTaskLog = { id: 456 };

      activatedRoute.data = of({ dailyTaskLog });
      comp.ngOnInit();

      expect(comp.dailyTaskLog).toEqual(dailyTaskLog);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDailyTaskLog>>();
      const dailyTaskLog = { id: 123 };
      jest.spyOn(dailyTaskLogFormService, 'getDailyTaskLog').mockReturnValue(dailyTaskLog);
      jest.spyOn(dailyTaskLogService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dailyTaskLog });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: dailyTaskLog }));
      saveSubject.complete();

      // THEN
      expect(dailyTaskLogFormService.getDailyTaskLog).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(dailyTaskLogService.update).toHaveBeenCalledWith(expect.objectContaining(dailyTaskLog));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDailyTaskLog>>();
      const dailyTaskLog = { id: 123 };
      jest.spyOn(dailyTaskLogFormService, 'getDailyTaskLog').mockReturnValue({ id: null });
      jest.spyOn(dailyTaskLogService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dailyTaskLog: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: dailyTaskLog }));
      saveSubject.complete();

      // THEN
      expect(dailyTaskLogFormService.getDailyTaskLog).toHaveBeenCalled();
      expect(dailyTaskLogService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDailyTaskLog>>();
      const dailyTaskLog = { id: 123 };
      jest.spyOn(dailyTaskLogService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dailyTaskLog });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(dailyTaskLogService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
