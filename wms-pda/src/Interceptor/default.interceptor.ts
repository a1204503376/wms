import { ProcReslutModel } from './../models/ProcReslutModel';
import { Injectable, Injector } from '@angular/core';
import {
  HttpInterceptor,
  HttpRequest,
  HttpHandler,
  HttpErrorResponse,
  HttpSentEvent,
  HttpHeaderResponse,
  HttpProgressEvent,
  HttpResponse,
  HttpUserEvent,
  HttpHeaders,
  HttpEvent,
} from '@angular/common/http';
import { Observable } from 'rxjs';
import { mergeMap, catchError } from 'rxjs/operators';
import { ErrorObservable } from 'rxjs/observable/ErrorObservable';


/**
 * 默认HTTP拦截器，其注册细节见 `app.module.ts`
 */
@Injectable()
export class DefaultInterceptor implements HttpInterceptor {
  constructor(private injector: Injector) { }


  // intercept(
  //   req: HttpRequest<any>,
  //   next: HttpHandler,
  // ): Observable<
  // | HttpSentEvent
  // | HttpHeaderResponse
  // | HttpProgressEvent
  // | HttpResponse<any>
  // | HttpUserEvent<any>
  // > {
  //   // 统一加上服务端前缀
  //   //let method = req.method;
  //   // if (!url.startsWith('https://') && !url.startsWith('http://')) {
  //   //   url = environment.SERVER_URL + url;
  //   // }
  //   // let method = 'A01001';
  //   // let header = req.headers.append('Method',method);

  //   const newReq = req.clone({
  //     // method: 'A01001',
  //     // headers: req.headers.set('method', method),
  //   });
  //   // console.log(req);
  //   // return next.handle(newReq).pipe(mergeMap((event: any) => {
  //   //   if (event instanceof HttpResponse && event.status != 200) {
  //   //     return ErrorObservable.create(event);
  //   //   }
  //   //   console.log(event);
  //   //   return Observable.create(observer => observer.next(event)); //请求成功返回响应
  //   // }));

  //   return next.handle(newReq);

  // }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

    console.log("Before sending data")
    console.log(req);
    return next.handle(req)
      .retry(3)
      .map(resp => {
        if (resp instanceof HttpResponse) {
          // let procRltStr:ProcReslutModel;
          // procRltStr = JSON.parse(resp.headers.get('SYSTEM_PROC_RLT_ACIL'));
          // if (procRltStr != null){
          //   alert(procRltStr.MsgContent);
          //   return;
          // }
          // resp.body
        }
        return resp;
      }).catch(err => {
        console.log(err);
        if (err instanceof HttpResponse) {
          console.log(err);
          console.log(err);
        }

        return Observable.of(err);
      });

  }


}
