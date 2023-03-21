import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDailyTaskLog, NewDailyTaskLog } from '../daily-task-log.model';

export type PartialUpdateDailyTaskLog = Partial<IDailyTaskLog> & Pick<IDailyTaskLog, 'id'>;

type RestOf<T extends IDailyTaskLog | NewDailyTaskLog> = Omit<T, 'startDate' | 'endDate'> & {
  startDate?: string | null;
  endDate?: string | null;
};

export type RestDailyTaskLog = RestOf<IDailyTaskLog>;

export type NewRestDailyTaskLog = RestOf<NewDailyTaskLog>;

export type PartialUpdateRestDailyTaskLog = RestOf<PartialUpdateDailyTaskLog>;

export type EntityResponseType = HttpResponse<IDailyTaskLog>;
export type EntityArrayResponseType = HttpResponse<IDailyTaskLog[]>;

@Injectable({ providedIn: 'root' })
export class DailyTaskLogService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/daily-task-logs');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(dailyTaskLog: NewDailyTaskLog): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(dailyTaskLog);
    return this.http
      .post<RestDailyTaskLog>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(dailyTaskLog: IDailyTaskLog): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(dailyTaskLog);
    return this.http
      .put<RestDailyTaskLog>(`${this.resourceUrl}/${this.getDailyTaskLogIdentifier(dailyTaskLog)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(dailyTaskLog: PartialUpdateDailyTaskLog): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(dailyTaskLog);
    return this.http
      .patch<RestDailyTaskLog>(`${this.resourceUrl}/${this.getDailyTaskLogIdentifier(dailyTaskLog)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestDailyTaskLog>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestDailyTaskLog[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getDailyTaskLogIdentifier(dailyTaskLog: Pick<IDailyTaskLog, 'id'>): number {
    return dailyTaskLog.id;
  }

  compareDailyTaskLog(o1: Pick<IDailyTaskLog, 'id'> | null, o2: Pick<IDailyTaskLog, 'id'> | null): boolean {
    return o1 && o2 ? this.getDailyTaskLogIdentifier(o1) === this.getDailyTaskLogIdentifier(o2) : o1 === o2;
  }

  addDailyTaskLogToCollectionIfMissing<Type extends Pick<IDailyTaskLog, 'id'>>(
    dailyTaskLogCollection: Type[],
    ...dailyTaskLogsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const dailyTaskLogs: Type[] = dailyTaskLogsToCheck.filter(isPresent);
    if (dailyTaskLogs.length > 0) {
      const dailyTaskLogCollectionIdentifiers = dailyTaskLogCollection.map(
        dailyTaskLogItem => this.getDailyTaskLogIdentifier(dailyTaskLogItem)!
      );
      const dailyTaskLogsToAdd = dailyTaskLogs.filter(dailyTaskLogItem => {
        const dailyTaskLogIdentifier = this.getDailyTaskLogIdentifier(dailyTaskLogItem);
        if (dailyTaskLogCollectionIdentifiers.includes(dailyTaskLogIdentifier)) {
          return false;
        }
        dailyTaskLogCollectionIdentifiers.push(dailyTaskLogIdentifier);
        return true;
      });
      return [...dailyTaskLogsToAdd, ...dailyTaskLogCollection];
    }
    return dailyTaskLogCollection;
  }

  protected convertDateFromClient<T extends IDailyTaskLog | NewDailyTaskLog | PartialUpdateDailyTaskLog>(dailyTaskLog: T): RestOf<T> {
    return {
      ...dailyTaskLog,
      startDate: dailyTaskLog.startDate?.toJSON() ?? null,
      endDate: dailyTaskLog.endDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restDailyTaskLog: RestDailyTaskLog): IDailyTaskLog {
    return {
      ...restDailyTaskLog,
      startDate: restDailyTaskLog.startDate ? dayjs(restDailyTaskLog.startDate) : undefined,
      endDate: restDailyTaskLog.endDate ? dayjs(restDailyTaskLog.endDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestDailyTaskLog>): HttpResponse<IDailyTaskLog> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestDailyTaskLog[]>): HttpResponse<IDailyTaskLog[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
