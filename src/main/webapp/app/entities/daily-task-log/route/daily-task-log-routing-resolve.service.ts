import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDailyTaskLog } from '../daily-task-log.model';
import { DailyTaskLogService } from '../service/daily-task-log.service';

@Injectable({ providedIn: 'root' })
export class DailyTaskLogRoutingResolveService implements Resolve<IDailyTaskLog | null> {
  constructor(protected service: DailyTaskLogService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDailyTaskLog | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((dailyTaskLog: HttpResponse<IDailyTaskLog>) => {
          if (dailyTaskLog.body) {
            return of(dailyTaskLog.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
