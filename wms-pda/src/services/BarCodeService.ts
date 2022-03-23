import { Observable } from 'rxjs';
import { Injectable } from "@angular/core";
import { BarcodeRuleModel } from "../models/SystemFramework.Model";
import { AppService } from "./App.service";
import { NativeService } from "./NativeService";
import { Utils } from "./Utils";
import { SelectSkuModel } from "../models/Instroe.model";
import { ParamModel } from "../models/ParamModel";
import { ScanModel } from "../models/DataBase.Model";
import { AlertController } from "ionic-angular";
import { AppConstant, Api, ContenType, Method, BaseCodeConstant } from '../utils/appConstant';
import { Storage } from '@ionic/storage';
import { SkuLotModel } from '../models/SkuLotModel'

@Injectable()
export class BarcodeService {
    barcodeRules: BarcodeRuleModel[];

    skuList: SelectSkuModel[];

    //构造函数 依赖注入
    constructor(public appService: AppService,
        public nativeService: NativeService,
        public alertCtrl: AlertController,
        public storage: Storage

    ) {
        this.BarcodeRules();
    }

    BarcodeRules() {
        this.appService.httpRequest(Api.api + Api.BarcodeRuleList, Method.Post, '', ContenType.Json, result => {
            if (Utils.isNotEmpty(result.data)) {
                this.barcodeRules = result.data;
            }
        });
    }
    /// 返回条码类型
    ToBarcodeType(text) {
        switch (text) {
            case 10://"货位条码":
                return BarcodeType.HuoWei;
            case 20://"容器编码":
                return BarcodeType.TuoPan;
            case 30://"物品编码":
                return BarcodeType.SKU;
            case 40://"序列号":
                return BarcodeType.SerialNumber;
            case 50://"批次号":
                return BarcodeType.BatchNo;
            default:
                return BarcodeType.BoxCode;
        }
    }


    /// 解析条码
    GetBarcodeType(text) {
        if (Utils.isEmpty(text)) {
            return BarcodeType.Error;
        }
        if (this.barcodeRules) {
            for (let i = 0; i < this.barcodeRules.length; i++) {
                let barcodeDesc = this.barcodeRules[i].barcodeType;
                let rule = this.barcodeRules[i].barcodeRule;
                let regexp = new RegExp(Utils.clearSpace(rule));
                if (Utils.isNotEmpty(rule)) {
                    if (regexp.test(text)) {
                        return this.ToBarcodeType(barcodeDesc);
                    }
                }
            }
        }

        // 无法解析到条码时,都属于物品
        return BarcodeType.SKU;
    }
    /**
     * 解析箱码
     * @param barcode 箱码规则
     */
    ScanSkuBarcode(barcode): Observable<ScanModel> {
        if (Utils.isEmpty(barcode)) {
            return Observable.of(null);
        }
        barcode = Utils.ToCDB(barcode.trim());
        return Observable.create(observer => {
            this.storage.get('sysParam').then(data => {
                //系统参数规则
                let paramModel = new ParamModel();
                //箱码绑定规则
                let scanModel = new ScanModel();
                //匹配箱码验证规则
                if (Utils.isNotEmpty(data.filter(x => x.paramKey == 'split'))) {
                    paramModel = data.filter(x => x.paramKey == 'split')[0];
                    paramModel.paramValue = JSON.parse(paramModel.paramValue);
                    let splModel = barcode.split(paramModel.paramValue.sp1);
                    scanModel.skuLotModel = new SkuLotModel();
                    for (let index = 0; index < splModel.length; index++) {
                        //根据系统参数分割数据
                        const element = splModel[index];
                        if (Utils.isNotEmpty(element)) {
                            let sp2Model = element.split(paramModel.paramValue.sp2)
                            let sp2Key = sp2Model[0].toLowerCase();
                            let sp2Value = sp2Model[1];
                            if (!sp2Value) {
                                scanModel = barcode;
                            } else {
                                if (Utils.Contains("skucode", sp2Key)
                                    || Utils.Contains("skuname", sp2Key)
                                    || Utils.Contains("qty", sp2Key)
                                    || Utils.Contains("um", sp2Key)) {
                                    //验证必填值
                                    if (Utils.isEmpty(sp2Value)) {
                                        this.nativeService.showToast(`箱码中的${sp2Key}不能为空`);
                                        observer.next(null);
                                        return;
                                    }
                                    scanModel[sp2Key] = sp2Value.trim();
                                } else if (Utils.Contains(sp2Key.toLowerCase(), "skulot")) {
                                    scanModel.skuLotModel[sp2Key] = sp2Value.trim();
                                } else {
                                    this.nativeService.showToast('扫描信息不符合规范,请确认条码规则！');
                                    observer.next(null);
                                    return;
                                }
                            }
                        }
                    }
                }
                observer.next(scanModel);
            });
        });
    }
    /**
     * 批属性转换提交Lot
     */
    convertLotModel(val: ScanModel) {
        if (Utils.isEmpty(val)) {
            return;
        }
        let param = {
            skuLot1: val.skuLotModel.skulot1,
            skuLot2: val.skuLotModel.skulot2,
            skuLot3: val.skuLotModel.skulot3,
            skuLot4: val.skuLotModel.skulot4,
            skuLot5: val.skuLotModel.skulot5,
            skuLot6: val.skuLotModel.skulot6,
            skuLot7: val.skuLotModel.skulot7,
            skuLot8: val.skuLotModel.skulot8,
            skuLot9: val.skuLotModel.skulot9,
            skuLot10: val.skuLotModel.skulot10,
            skuLot11: val.skuLotModel.skulot11,
            skuLot12: val.skuLotModel.skulot12,
            skuLot13: val.skuLotModel.skulot13,
            skuLot14: val.skuLotModel.skulot14,
            skuLot15: val.skuLotModel.skulot15,
            skuLot16: val.skuLotModel.skulot16,
            skuLot17: val.skuLotModel.skulot17,
            skuLot18: val.skuLotModel.skulot18,
            skuLot19: val.skuLotModel.skulot19,
            skuLot20: val.skuLotModel.skulot20,
            skuLot21: val.skuLotModel.skulot21,
            skuLot22: val.skuLotModel.skulot22,
            skuLot23: val.skuLotModel.skulot23,
            skuLot24: val.skuLotModel.skulot24,
            skuLot25: val.skuLotModel.skulot25,
            skuLot26: val.skuLotModel.skulot26,
            skuLot27: val.skuLotModel.skulot27,
            skuLot28: val.skuLotModel.skulot28,
            skuLot29: val.skuLotModel.skulot29,
            skuLot30: val.skuLotModel.skulot30,
        }
        return param;
    }
    /**
     * 批属性转换提交Lots
     */
    convertLotsModel(val: ScanModel) {
        if (Utils.isEmpty(val)) {
            return;
        }
        let param = {
            ['skuLots.skuLot1']: val.skuLotModel.skulot1,
            ['skuLots.skuLot2']: val.skuLotModel.skulot2,
            ['skuLots.skuLot3']: val.skuLotModel.skulot3,
            ['skuLots.skuLot4']: val.skuLotModel.skulot4,
            ['skuLots.skuLot5']: val.skuLotModel.skulot5,
            ['skuLots.skuLot6']: val.skuLotModel.skulot6,
            ['skuLots.skuLot7']: val.skuLotModel.skulot7,
            ['skuLots.skuLot8']: val.skuLotModel.skulot8,
            ['skuLots.skuLot9']: val.skuLotModel.skulot9,
            ['skuLots.skuLot10']: val.skuLotModel.skulot10,
            ['skuLots.skuLot11']: val.skuLotModel.skulot11,
            ['skuLots.skuLot12']: val.skuLotModel.skulot12,
            ['skuLots.skuLot13']: val.skuLotModel.skulot13,
            ['skuLots.skuLot14']: val.skuLotModel.skulot14,
            ['skuLots.skuLot15']: val.skuLotModel.skulot15,
            ['skuLots.skuLot16']: val.skuLotModel.skulot16,
            ['skuLots.skuLot17']: val.skuLotModel.skulot17,
            ['skuLots.skuLot18']: val.skuLotModel.skulot18,
            ['skuLots.skuLot19']: val.skuLotModel.skulot19,
            ['skuLots.skuLot20']: val.skuLotModel.skulot20,
            ['skuLots.skuLot21']: val.skuLotModel.skulot21,
            ['skuLots.skuLot22']: val.skuLotModel.skulot22,
            ['skuLots.skuLot23']: val.skuLotModel.skulot23,
            ['skuLots.skuLot24']: val.skuLotModel.skulot24,
            ['skuLots.skuLot25']: val.skuLotModel.skulot25,
            ['skuLots.skuLot26']: val.skuLotModel.skulot26,
            ['skuLots.skuLot27']: val.skuLotModel.skulot27,
            ['skuLots.skuLot28']: val.skuLotModel.skulot28,
            ['skuLots.skuLot29']: val.skuLotModel.skulot29,
            ['skuLots.skuLot30']: val.skuLotModel.skulot30,
        }
        return param;
    }
    /**
     *
     * 批属性获取转换
     */
    convertGetLotModel(targetModel: any, model: SkuLotModel): SkuLotModel {
        if (Utils.isEmpty(targetModel) && Utils.isEmpty(model)) {
            return;
        }
        model.skuLotName1 = targetModel.skuLotLabel1;
        model.skuLotName2 = targetModel.skuLotLabel2;
        model.skuLotName3 = targetModel.skuLotLabel3;
        model.skuLotName4 = targetModel.skuLotLabel4;
        model.skuLotName5 = targetModel.skuLotLabel5;
        model.skuLotName6 = targetModel.skuLotLabel6;
        model.skuLotName7 = targetModel.skuLotLabel7;
        model.skuLotName8 = targetModel.skuLotLabel8;
        model.skuLotName9 = targetModel.skuLotLabel9;
        model.skuLotName10 = targetModel.skuLotLabel10;
        model.skuLotName11 = targetModel.skuLotLabel11;
        model.skuLotName12 = targetModel.skuLotLabel12;
        model.skuLotName13 = targetModel.skuLotLabel13;
        model.skuLotName14 = targetModel.skuLotLabel14;
        model.skuLotName15 = targetModel.skuLotLabel15;
        model.skuLotName16 = targetModel.skuLotLabel16;
        model.skuLotName17 = targetModel.skuLotLabel17;
        model.skuLotName18 = targetModel.skuLotLabel18;
        model.skuLotName19 = targetModel.skuLotLabel19;
        model.skuLotName20 = targetModel.skuLotLabel20;
        model.skuLotName21 = targetModel.skuLotLabel21;
        model.skuLotName22 = targetModel.skuLotLabel22;
        model.skuLotName23 = targetModel.skuLotLabel23;
        model.skuLotName24 = targetModel.skuLotLabel24;
        model.skuLotName25 = targetModel.skuLotLabel25;
        model.skuLotName26 = targetModel.skuLotLabel26;
        model.skuLotName27 = targetModel.skuLotLabel27;
        model.skuLotName28 = targetModel.skuLotLabel28;
        model.skuLotName29 = targetModel.skuLotLabel29;
        model.skuLotName30 = targetModel.skuLotLabel30;
    }
    /**
     * 商品列表超过1个，每一个商品一一添加
     */
    openModel(): Observable<any> {
        return Observable.create(observer => {
            let alert = this.alertCtrl.create();
            alert.setTitle('选择添加的商品');
            this.skuList.forEach(item => {
                alert.addInput({
                    type: 'radio',
                    label: item.SkuCode + ' ' + item.SkuName,
                    value: item.SkuId.toString()
                });
            });
            alert.addButton({
                text: '取消',
                handler: () => {
                    observer.next(null);
                }
            });
            alert.addButton({
                text: '确定',
                handler: data => {
                    observer.next(this.skuList.find((value) => {
                        return value.SkuId.toString() === data;
                    }));
                }
            });
            alert.present();
            alert.onDidDismiss(() => {
            })
        });

    }
    // 序列号扫描
    ScanSerialNumber(serialNumber): Observable<SelectSkuModel> {
        return Observable.create(observer => {
            if (Utils.isEmpty(serialNumber)) {
                observer.of(null);
            }
            // this.appService.httpRequest(IFD.BASEDATA_MATERIAL + "/", IFD.BASEDATA_MATERIAL_B02016, serialNumber, result => {
            //     this.nativeService.hideLoading();
            //     observer.next(result);
            // });
        });
    }

}

// 托盘，货位，SKU，序列号, 员工账号，错误的条码
export let BarcodeType = {
    TuoPan: 0,
    HuoWei: 1,
    SKU: 2,
    SerialNumber: 3,
    YuanGongZhangHao: 4,
    BatchNo: 6,
    Error: 5,
    BoxCode: 7
}
