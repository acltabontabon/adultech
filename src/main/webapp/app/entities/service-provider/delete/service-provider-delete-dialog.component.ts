import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IServiceProvider } from '../service-provider.model';
import { ServiceProviderService } from '../service/service-provider.service';

@Component({
  standalone: true,
  templateUrl: './service-provider-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ServiceProviderDeleteDialogComponent {
  serviceProvider?: IServiceProvider;

  constructor(
    protected serviceProviderService: ServiceProviderService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.serviceProviderService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
