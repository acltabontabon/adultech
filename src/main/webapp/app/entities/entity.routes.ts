import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'service-provider',
    data: { pageTitle: 'ServiceProviders' },
    loadChildren: () => import('./service-provider/service-provider.routes'),
  },
  {
    path: 'device',
    data: { pageTitle: 'Devices' },
    loadChildren: () => import('./device/device.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
