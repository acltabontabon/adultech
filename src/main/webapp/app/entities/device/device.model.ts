export interface IDevice {
  id: number;
  name?: string | null;
  powerRating?: number | null;
}

export type NewDevice = Omit<IDevice, 'id'> & { id: null };
