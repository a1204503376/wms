import { Injectable } from '@angular/core';
import { Storage } from '@ionic/storage';
import { Observable } from 'rxjs';
import { Utils } from '../services/Utils';
import { APPCONSTANT } from './appConstant';


@Injectable()
export class CommonService {
    istologin: boolean;
    private _showLoading: boolean = true;
    IsExtranet: boolean;

    constructor(private storage: Storage) {
    }

    setData(keyName: string, data: any) {
        this.storage.set(keyName, data);
    }

    async getData(keyName: string) {
        await this.storage.get(keyName);
    }

    setlogin(islogin: boolean) {
        this.istologin = islogin
    }

    getlogin() {
        return this.istologin;
    }


    getIsExtranet() {
        return this.IsExtranet;
    }

    setIsExtranet(isExtranet: boolean) {
        this.IsExtranet = isExtranet;
    }

    getServerIP(): Observable<string> {
        return Observable.create(observer => {
            this.storage.ready().then(() => {
                this.storage.get('ServerIP').then((val) => {
                    if (Utils.isEmpty(val)) {
                        observer.next(APPCONSTANT.ServerIP);
                    } else {
                        observer.next(val);
                    }
                });
            });
        });
    }

    getExtranetServerIP(): Observable<string> {
        return Observable.create(observer => {
            this.storage.ready().then(() => {
                this.storage.get('ExtranetServerIP').then((val) => {
                    if (Utils.isEmpty(val)) {
                        observer.next(APPCONSTANT.ExtranetServerIP);
                    } else {
                        observer.next(val);
                    }
                });
            });
        });
    }

    get showLoading(): boolean {
        return this._showLoading;
    }

    set showLoading(value: boolean) {
        this._showLoading = value;
    }

}
