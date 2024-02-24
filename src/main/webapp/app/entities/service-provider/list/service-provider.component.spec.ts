import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ServiceProviderService } from '../service/service-provider.service';

import { ServiceProviderComponent } from './service-provider.component';

describe('ServiceProvider Management Component', () => {
  let comp: ServiceProviderComponent;
  let fixture: ComponentFixture<ServiceProviderComponent>;
  let service: ServiceProviderService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'service-provider', component: ServiceProviderComponent }]),
        HttpClientTestingModule,
        ServiceProviderComponent,
      ],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'id,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'id,desc',
              }),
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(ServiceProviderComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ServiceProviderComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ServiceProviderService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        }),
      ),
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.serviceProviders?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to serviceProviderService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getServiceProviderIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getServiceProviderIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
