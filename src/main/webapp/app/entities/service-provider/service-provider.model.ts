import dayjs from 'dayjs/esm';
import { ServiceType } from 'app/entities/enumerations/service-type.model';

export interface IServiceProvider {
  id: number;
  name?: string | null;
  unitMeasurement?: string | null;
  notes?: string | null;
  rate?: number | null;
  type?: keyof typeof ServiceType | null;
  lastUpdate?: dayjs.Dayjs | null;
}

export type NewServiceProvider = Omit<IServiceProvider, 'id'> & { id: null };
