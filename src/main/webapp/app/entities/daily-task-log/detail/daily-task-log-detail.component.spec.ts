import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DailyTaskLogDetailComponent } from './daily-task-log-detail.component';

describe('DailyTaskLog Management Detail Component', () => {
  let comp: DailyTaskLogDetailComponent;
  let fixture: ComponentFixture<DailyTaskLogDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DailyTaskLogDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ dailyTaskLog: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(DailyTaskLogDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DailyTaskLogDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load dailyTaskLog on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.dailyTaskLog).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
