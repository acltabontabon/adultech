import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ServiceProviderDetailComponent } from './service-provider-detail.component';

describe('ServiceProvider Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ServiceProviderDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: ServiceProviderDetailComponent,
              resolve: { serviceProvider: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ServiceProviderDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load serviceProvider on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ServiceProviderDetailComponent);

      // THEN
      expect(instance.serviceProvider).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
