import { DashboardSummary } from '@/@types/dashboard.model';
import { environment } from '@/environments/environment';
import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class DashboardService {
  private readonly apiUrl = environment.apiUrl;
  httpClient = inject(HttpClient);

  getSummary(): Observable<DashboardSummary> {
    return this.httpClient.get<DashboardSummary>(`${this.apiUrl}/dashboard/summary`);
  }
}
