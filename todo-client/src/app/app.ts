import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { LoadingComponent } from './components/loading/loading.component';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, LoadingComponent],
  template: `
    <app-loading />

    <router-outlet />
  `,
})
export class App {
  protected readonly title = signal('todo-client');
}
