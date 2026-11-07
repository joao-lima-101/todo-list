import { Injectable, computed, inject } from '@angular/core';
import { HttpClient, HttpContext, HttpErrorResponse } from '@angular/common/http';
import { catchError, Observable, of } from 'rxjs';
import { LoginRequest } from '@/@types/login.model';
import { RegisterRequest } from '@/@types/register.model';
import { environment } from '@/environments/environment';
import { SKIP_CREDENTIALS, SKIP_LOADING } from '../../interceptors/http.tokens';
import { UserResponse } from '@/@types/user.model';
import { rxResource } from '@angular/core/rxjs-interop';
import { CustomSnackBarService } from '../custom-snackbar.service';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private httpClient = inject(HttpClient);
  private readonly apiUrl = environment.apiUrl;
  private readonly snackBar = inject(CustomSnackBarService);

  login(data: LoginRequest): Observable<{ message: string }> {
    return this.httpClient.post<{ message: string }>(`${this.apiUrl}/auth/login`, data);
  }

  logout(): Observable<{ message: string }> {
    return this.httpClient.post<{ message: string }>(`${this.apiUrl}/auth/logout`, null);
  }

  register(data: RegisterRequest): Observable<{ message: string }> {
    return this.httpClient.post<{ message: string }>(`${this.apiUrl}/auth/register`, data, {
      context: new HttpContext().set(SKIP_CREDENTIALS, true),
    });
  }

  isLoggedIn(): Observable<{ authenticated: boolean }> {
    return this.httpClient.get<{ authenticated: boolean }>(`${this.apiUrl}/auth/status`, {
      context: new HttpContext().set(SKIP_LOADING, true),
    });
  }

  private me(): Observable<UserResponse> {
    return this.httpClient.get<UserResponse>(`${this.apiUrl}/auth/me`);
  }

  readonly userResource = rxResource({
    stream: () =>
      this.me().pipe(
        catchError((httpError: HttpErrorResponse) => {
          this.snackBar.error(httpError.error.message || 'Erro ao carregar dados do usuário');
          return of(null);
        }),
      ),
  });
}
