import { IDevice, NewDevice } from './device.model';

export const sampleWithRequiredData: IDevice = {
  id: 14898,
};

export const sampleWithPartialData: IDevice = {
  id: 7478,
};

export const sampleWithFullData: IDevice = {
  id: 30994,
  name: 'modernity',
  powerRating: 11854.69,
};

export const sampleWithNewData: NewDevice = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
