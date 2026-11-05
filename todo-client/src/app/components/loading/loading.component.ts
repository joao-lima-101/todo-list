import { LoadingService } from '@/app/services/api/loading.service';
import { Component, inject } from '@angular/core';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

@Component({
  selector: 'app-loading',
  templateUrl: 'loading.component.html',
  imports: [MatProgressSpinnerModule],
})
export class LoadingComponent {
  loadingService = inject(LoadingService);
}
