import { DecimalPipe } from '@angular/common';
import { Component, computed, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { rxResource, toSignal } from '@angular/core/rxjs-interop';
import { catchError, of } from 'rxjs';
import { MatIconModule } from '@angular/material/icon';
import { DashboardService } from '@/app/services/api/dashboard.service';
import { HeaderComponent } from '@/app/components/header/header.component';
import { MatProgressBar } from '@angular/material/progress-bar';
import { MatTooltip } from '@angular/material/tooltip';
import { AuthService } from '@/app/services/api/auth.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [DecimalPipe, RouterLink, MatIconModule, HeaderComponent, MatProgressBar, MatTooltip],
  templateUrl: './dashboard.component.html',
})
export class DashboardComponent {
  private readonly api = inject(DashboardService);
  private readonly authApi = inject(AuthService);
  readonly error = signal<string | null>(null);

  readonly userData = this.authApi.userResource.value();
  readonly userName = this.userData?.name.split(' ')[0];

  readonly summary = toSignal(
    this.api.getSummary().pipe(
      catchError(() => {
        this.error.set('Não foi possível carregar os dados do dashboard.');
        return of(null);
      }),
    ),
    { initialValue: null },
  );
}
