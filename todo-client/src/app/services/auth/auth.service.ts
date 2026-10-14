import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpContext } from '@angular/common/http';
import { Observable } from 'rxjs';
import { LoginRequest } from '@/@types/login.types';
import { RegisterRequest } from '@/@types/register.types';
import { environment } from '@/environments/environment';
import { SKIP_CREDENTIALS } from './auth.context';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private httpClient = inject(HttpClient);

  login(data: LoginRequest): Observable<{ message: string }> {
    return this.httpClient.post<{ message: string }>(`${environment.apiUrl}/auth/login`, data);
  }

  logout(): Observable<{ message: string }> {
    return this.httpClient.post<{ message: string }>(`${environment.apiUrl}/auth/logout`, null);
  }

  register(data: RegisterRequest): Observable<{ message: string }> {
    return this.httpClient.post<{ message: string }>(`${environment.apiUrl}/auth/register`, data, {
      context: new HttpContext().set(SKIP_CREDENTIALS, true),
    });
  }

  isLoggedIn(): Observable<{ authenticated: boolean }> {
    return this.httpClient.get<{ authenticated: boolean }>(`${environment.apiUrl}/auth/status`);
  }
}
