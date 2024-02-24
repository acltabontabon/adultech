import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IServiceProvider } from '../service-provider.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../service-provider.test-samples';

import { ServiceProviderService, RestServiceProvider } from './service-provider.service';

const requireRestSample: RestServiceProvider = {
  ...sampleWithRequiredData,
  lastUpdate: sampleWithRequiredData.lastUpdate?.toJSON(),
};

describe('ServiceProvider Service', () => {
  let service: ServiceProviderService;
  let httpMock: HttpTestingController;
  let expectedResult: IServiceProvider | IServiceProvider[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ServiceProviderService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a ServiceProvider', () => {
      const serviceProvider = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(serviceProvider).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ServiceProvider', () => {
      const serviceProvider = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(serviceProvider).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ServiceProvider', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ServiceProvider', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ServiceProvider', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addServiceProviderToCollectionIfMissing', () => {
      it('should add a ServiceProvider to an empty array', () => {
        const serviceProvider: IServiceProvider = sampleWithRequiredData;
        expectedResult = service.addServiceProviderToCollectionIfMissing([], serviceProvider);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(serviceProvider);
      });

      it('should not add a ServiceProvider to an array that contains it', () => {
        const serviceProvider: IServiceProvider = sampleWithRequiredData;
        const serviceProviderCollection: IServiceProvider[] = [
          {
            ...serviceProvider,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addServiceProviderToCollectionIfMissing(serviceProviderCollection, serviceProvider);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ServiceProvider to an array that doesn't contain it", () => {
        const serviceProvider: IServiceProvider = sampleWithRequiredData;
        const serviceProviderCollection: IServiceProvider[] = [sampleWithPartialData];
        expectedResult = service.addServiceProviderToCollectionIfMissing(serviceProviderCollection, serviceProvider);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(serviceProvider);
      });

      it('should add only unique ServiceProvider to an array', () => {
        const serviceProviderArray: IServiceProvider[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const serviceProviderCollection: IServiceProvider[] = [sampleWithRequiredData];
        expectedResult = service.addServiceProviderToCollectionIfMissing(serviceProviderCollection, ...serviceProviderArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const serviceProvider: IServiceProvider = sampleWithRequiredData;
        const serviceProvider2: IServiceProvider = sampleWithPartialData;
        expectedResult = service.addServiceProviderToCollectionIfMissing([], serviceProvider, serviceProvider2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(serviceProvider);
        expect(expectedResult).toContain(serviceProvider2);
      });

      it('should accept null and undefined values', () => {
        const serviceProvider: IServiceProvider = sampleWithRequiredData;
        expectedResult = service.addServiceProviderToCollectionIfMissing([], null, serviceProvider, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(serviceProvider);
      });

      it('should return initial array if no ServiceProvider is added', () => {
        const serviceProviderCollection: IServiceProvider[] = [sampleWithRequiredData];
        expectedResult = service.addServiceProviderToCollectionIfMissing(serviceProviderCollection, undefined, null);
        expect(expectedResult).toEqual(serviceProviderCollection);
      });
    });

    describe('compareServiceProvider', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareServiceProvider(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareServiceProvider(entity1, entity2);
        const compareResult2 = service.compareServiceProvider(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareServiceProvider(entity1, entity2);
        const compareResult2 = service.compareServiceProvider(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareServiceProvider(entity1, entity2);
        const compareResult2 = service.compareServiceProvider(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
