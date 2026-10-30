import { DecimalPipe } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { toSignal } from '@angular/core/rxjs-interop';
import { catchError, finalize, of } from 'rxjs';

import { MatIconModule } from '@angular/material/icon';
import { DashboardService } from '@/app/services/dashboard.service';
import { HeaderComponent } from '@/app/components/ui/header.component';
import { MatProgressBar } from '@angular/material/progress-bar';
import { MatTooltip } from '@angular/material/tooltip';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [DecimalPipe, RouterLink, MatIconModule, HeaderComponent, MatProgressBar, MatTooltip],
  templateUrl: './dashboard.component.html',
})
export class DashboardComponent {
  private readonly api = inject(DashboardService);
  readonly loading = signal(true);
  readonly error = signal<string | null>(null);

  readonly summary = toSignal(
    this.api.getSummary().pipe(
      catchError(() => {
        this.error.set('Não foi possível carregar os dados do dashboard.');
        return of(null);
      }),
      finalize(() => this.loading.set(false)),
    ),
    { initialValue: null },
  );
}
