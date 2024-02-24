import dayjs from 'dayjs/esm';

import { IServiceProvider, NewServiceProvider } from './service-provider.model';

export const sampleWithRequiredData: IServiceProvider = {
  id: 11329,
};

export const sampleWithPartialData: IServiceProvider = {
  id: 29833,
  unitMeasurement: 'imperfect meh',
  notes: 'smuggle',
  lastUpdate: dayjs('2024-02-24T02:18'),
};

export const sampleWithFullData: IServiceProvider = {
  id: 19216,
  name: 'concerning',
  unitMeasurement: 'throughout once',
  notes: 'dispute journalism jaggedly',
  rate: 19026.48,
  type: 'WATER',
  lastUpdate: dayjs('2024-02-23T19:20'),
};

export const sampleWithNewData: NewServiceProvider = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
