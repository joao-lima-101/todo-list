import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { LoginRequest } from '@/@types/login.types';
import { RegisterRequest } from '@/@types/register.types';
import { environment } from '@/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private http = inject(HttpClient);

  login(data: LoginRequest): Observable<{ message: string }> {
    return this.http.post<{ message: string }>(`${environment.apiUrl}/auth/login`, data, {
      withCredentials: true,
    });
  }

  logout(): Observable<{ message: string }> {
    return this.http.post<{ message: string }>(`${environment.apiUrl}/auth/logout`, null, {
      withCredentials: true,
    });
  }

  register(data: RegisterRequest): Observable<{ message: string }> {
    return this.http.post<{ message: string }>(`${environment.apiUrl}/auth/register`, data);
  }

  isLoggedIn(): Observable<{ authenticated: boolean }> {
    return this.http.get<{ authenticated: boolean }>(`${environment.apiUrl}/auth/status`, {
      withCredentials: true,
    });
  }
}
