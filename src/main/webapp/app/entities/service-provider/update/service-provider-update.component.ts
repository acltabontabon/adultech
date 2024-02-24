import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ServiceType } from 'app/entities/enumerations/service-type.model';
import { IServiceProvider } from '../service-provider.model';
import { ServiceProviderService } from '../service/service-provider.service';
import { ServiceProviderFormService, ServiceProviderFormGroup } from './service-provider-form.service';

@Component({
  standalone: true,
  selector: 'jhi-service-provider-update',
  templateUrl: './service-provider-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ServiceProviderUpdateComponent implements OnInit {
  isSaving = false;
  serviceProvider: IServiceProvider | null = null;
  serviceTypeValues = Object.keys(ServiceType);

  editForm: ServiceProviderFormGroup = this.serviceProviderFormService.createServiceProviderFormGroup();

  constructor(
    protected serviceProviderService: ServiceProviderService,
    protected serviceProviderFormService: ServiceProviderFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ serviceProvider }) => {
      this.serviceProvider = serviceProvider;
      if (serviceProvider) {
        this.updateForm(serviceProvider);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const serviceProvider = this.serviceProviderFormService.getServiceProvider(this.editForm);
    if (serviceProvider.id !== null) {
      this.subscribeToSaveResponse(this.serviceProviderService.update(serviceProvider));
    } else {
      this.subscribeToSaveResponse(this.serviceProviderService.create(serviceProvider));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IServiceProvider>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(serviceProvider: IServiceProvider): void {
    this.serviceProvider = serviceProvider;
    this.serviceProviderFormService.resetForm(this.editForm, serviceProvider);
  }
}
