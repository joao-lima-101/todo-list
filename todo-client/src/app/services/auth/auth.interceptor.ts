import { HttpInterceptorFn } from '@angular/common/http';
import { SKIP_CREDENTIALS } from './auth.context';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  if (req.context.get(SKIP_CREDENTIALS)) {
    return next(req);
  }

  const modReq = req.clone({
    withCredentials: true,
  });

  return next(modReq);
};
