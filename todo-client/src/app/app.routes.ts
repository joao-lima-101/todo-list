import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { TasksComponent } from './pages/tasks/tasks.component';
import { RegisterComponent } from './pages/register/register.component';
import { ListsComponent } from './pages/lists/lists.component';
import { authGuard } from './services/auth/auth.guard';
import { DashboardComponent } from './pages/dashboard/dashboard.component';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: '', component: DashboardComponent, canActivate: [authGuard] },
  { path: 'lists', component: ListsComponent, canActivate: [authGuard] },
  { path: 'list/:listId', component: TasksComponent, canActivate: [authGuard] },
  { path: '**', redirectTo: 'login' },
];
