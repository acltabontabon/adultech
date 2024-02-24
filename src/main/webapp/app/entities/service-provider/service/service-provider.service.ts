import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IServiceProvider, NewServiceProvider } from '../service-provider.model';

export type PartialUpdateServiceProvider = Partial<IServiceProvider> & Pick<IServiceProvider, 'id'>;

type RestOf<T extends IServiceProvider | NewServiceProvider> = Omit<T, 'lastUpdate'> & {
  lastUpdate?: string | null;
};

export type RestServiceProvider = RestOf<IServiceProvider>;

export type NewRestServiceProvider = RestOf<NewServiceProvider>;

export type PartialUpdateRestServiceProvider = RestOf<PartialUpdateServiceProvider>;

export type EntityResponseType = HttpResponse<IServiceProvider>;
export type EntityArrayResponseType = HttpResponse<IServiceProvider[]>;

@Injectable({ providedIn: 'root' })
export class ServiceProviderService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/service-providers');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(serviceProvider: NewServiceProvider): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(serviceProvider);
    return this.http
      .post<RestServiceProvider>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(serviceProvider: IServiceProvider): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(serviceProvider);
    return this.http
      .put<RestServiceProvider>(`${this.resourceUrl}/${this.getServiceProviderIdentifier(serviceProvider)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(serviceProvider: PartialUpdateServiceProvider): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(serviceProvider);
    return this.http
      .patch<RestServiceProvider>(`${this.resourceUrl}/${this.getServiceProviderIdentifier(serviceProvider)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestServiceProvider>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestServiceProvider[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getServiceProviderIdentifier(serviceProvider: Pick<IServiceProvider, 'id'>): number {
    return serviceProvider.id;
  }

  compareServiceProvider(o1: Pick<IServiceProvider, 'id'> | null, o2: Pick<IServiceProvider, 'id'> | null): boolean {
    return o1 && o2 ? this.getServiceProviderIdentifier(o1) === this.getServiceProviderIdentifier(o2) : o1 === o2;
  }

  addServiceProviderToCollectionIfMissing<Type extends Pick<IServiceProvider, 'id'>>(
    serviceProviderCollection: Type[],
    ...serviceProvidersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const serviceProviders: Type[] = serviceProvidersToCheck.filter(isPresent);
    if (serviceProviders.length > 0) {
      const serviceProviderCollectionIdentifiers = serviceProviderCollection.map(
        serviceProviderItem => this.getServiceProviderIdentifier(serviceProviderItem)!,
      );
      const serviceProvidersToAdd = serviceProviders.filter(serviceProviderItem => {
        const serviceProviderIdentifier = this.getServiceProviderIdentifier(serviceProviderItem);
        if (serviceProviderCollectionIdentifiers.includes(serviceProviderIdentifier)) {
          return false;
        }
        serviceProviderCollectionIdentifiers.push(serviceProviderIdentifier);
        return true;
      });
      return [...serviceProvidersToAdd, ...serviceProviderCollection];
    }
    return serviceProviderCollection;
  }

  protected convertDateFromClient<T extends IServiceProvider | NewServiceProvider | PartialUpdateServiceProvider>(
    serviceProvider: T,
  ): RestOf<T> {
    return {
      ...serviceProvider,
      lastUpdate: serviceProvider.lastUpdate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restServiceProvider: RestServiceProvider): IServiceProvider {
    return {
      ...restServiceProvider,
      lastUpdate: restServiceProvider.lastUpdate ? dayjs(restServiceProvider.lastUpdate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestServiceProvider>): HttpResponse<IServiceProvider> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestServiceProvider[]>): HttpResponse<IServiceProvider[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
