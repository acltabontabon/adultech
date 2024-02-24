import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IServiceProvider, NewServiceProvider } from '../service-provider.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IServiceProvider for edit and NewServiceProviderFormGroupInput for create.
 */
type ServiceProviderFormGroupInput = IServiceProvider | PartialWithRequiredKeyOf<NewServiceProvider>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IServiceProvider | NewServiceProvider> = Omit<T, 'lastUpdate'> & {
  lastUpdate?: string | null;
};

type ServiceProviderFormRawValue = FormValueOf<IServiceProvider>;

type NewServiceProviderFormRawValue = FormValueOf<NewServiceProvider>;

type ServiceProviderFormDefaults = Pick<NewServiceProvider, 'id' | 'lastUpdate'>;

type ServiceProviderFormGroupContent = {
  id: FormControl<ServiceProviderFormRawValue['id'] | NewServiceProvider['id']>;
  name: FormControl<ServiceProviderFormRawValue['name']>;
  unitMeasurement: FormControl<ServiceProviderFormRawValue['unitMeasurement']>;
  notes: FormControl<ServiceProviderFormRawValue['notes']>;
  rate: FormControl<ServiceProviderFormRawValue['rate']>;
  type: FormControl<ServiceProviderFormRawValue['type']>;
  lastUpdate: FormControl<ServiceProviderFormRawValue['lastUpdate']>;
};

export type ServiceProviderFormGroup = FormGroup<ServiceProviderFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ServiceProviderFormService {
  createServiceProviderFormGroup(serviceProvider: ServiceProviderFormGroupInput = { id: null }): ServiceProviderFormGroup {
    const serviceProviderRawValue = this.convertServiceProviderToServiceProviderRawValue({
      ...this.getFormDefaults(),
      ...serviceProvider,
    });
    return new FormGroup<ServiceProviderFormGroupContent>({
      id: new FormControl(
        { value: serviceProviderRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(serviceProviderRawValue.name),
      unitMeasurement: new FormControl(serviceProviderRawValue.unitMeasurement),
      notes: new FormControl(serviceProviderRawValue.notes),
      rate: new FormControl(serviceProviderRawValue.rate),
      type: new FormControl(serviceProviderRawValue.type),
      lastUpdate: new FormControl(serviceProviderRawValue.lastUpdate),
    });
  }

  getServiceProvider(form: ServiceProviderFormGroup): IServiceProvider | NewServiceProvider {
    return this.convertServiceProviderRawValueToServiceProvider(
      form.getRawValue() as ServiceProviderFormRawValue | NewServiceProviderFormRawValue,
    );
  }

  resetForm(form: ServiceProviderFormGroup, serviceProvider: ServiceProviderFormGroupInput): void {
    const serviceProviderRawValue = this.convertServiceProviderToServiceProviderRawValue({ ...this.getFormDefaults(), ...serviceProvider });
    form.reset(
      {
        ...serviceProviderRawValue,
        id: { value: serviceProviderRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ServiceProviderFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      lastUpdate: currentTime,
    };
  }

  private convertServiceProviderRawValueToServiceProvider(
    rawServiceProvider: ServiceProviderFormRawValue | NewServiceProviderFormRawValue,
  ): IServiceProvider | NewServiceProvider {
    return {
      ...rawServiceProvider,
      lastUpdate: dayjs(rawServiceProvider.lastUpdate, DATE_TIME_FORMAT),
    };
  }

  private convertServiceProviderToServiceProviderRawValue(
    serviceProvider: IServiceProvider | (Partial<NewServiceProvider> & ServiceProviderFormDefaults),
  ): ServiceProviderFormRawValue | PartialWithRequiredKeyOf<NewServiceProviderFormRawValue> {
    return {
      ...serviceProvider,
      lastUpdate: serviceProvider.lastUpdate ? serviceProvider.lastUpdate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
