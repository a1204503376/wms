import {Component, NgZone, ViewChild} from '@angular/core';
import {IonicPage, NavController, NavParams, Events} from 'ionic-angular';
import {AppService} from "../../../services/App.service";
import {Utils} from '../../../services/Utils';
import {NativeService} from "../../../services/NativeService"
import {Api, ContenType, Method} from '../../../utils/appConstant';
import {BarcodeService, BarcodeType} from '../../../services/BarCodeService';
import {AppGlobal} from '../../../services/AppGlobal';
import {ScanModel} from "../../../models/DataBase.Model";
import {SelectItem} from "primeng/api";

/**
 * Generated class for the PutawayPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
    selector: 'page-putaway',
    templateUrl: 'putaway.html',
})

export class PutawayPage {
    public searchCode: string;
    skuCode: string;//物品编码
    @ViewChild('targetLocCodeComp') targetLocCodeComp;
    skuName: string;//物品名称
    public lpnCode: string = '';//容器编码
    public zoneCode: string = '';//库区编码
    public zoneName: string = '';//库区名称

    public typeCount: string = '';//物品种类数量
    public locCode: string = '';//当前库位
    public islocCode: string = '';//提交库位
    public locStatusName: string = '';//当前状态
    public whName: string = '';//仓库名称
    public whId: string = '';//仓库ID
    public recommendLoc: string = '';//推荐库位
    public scanModel: ScanModel;//扫描实体
    public planCount: string;
    constructor(public navCtrl: NavController,
                public navParams: NavParams,
                public appService: AppService,
                public nativeService: NativeService,
                public barcodeService: BarcodeService,
                public events: Events,
                public ngZone: NgZone
    ) {
    }

    ionViewDidLoad() {
    }

    ionViewWillEnter() {
        //事件注册（扫描结果接收）
        this.events.subscribe('barcode:scan', (barcode, time) => {
            let bt = this.barcodeService.GetBarcodeType(barcode);
            if (bt == BarcodeType.TuoPan) {
                this.ngZone.run(() => {
                    this.searchCode = barcode;
                    this.queryStockByLpnCode();
                });
            }
            if (bt == BarcodeType.HuoWei) {
                this.ngZone.run(() => {
                    this.islocCode = barcode;
                });
            }
        });
    }

    ionViewWillLeave() {
        AppGlobal.removeSubscribe(this);
    }

    /**
     * 入库单回车事件
     */
    keyEnter(event) {
        if (event && event.keyCode == 13) {
            this.queryStockByLpnCode();
        }
    }

    /**
     * 根据lpn获取上架明细
     */
    queryStockByLpnCode() {
        if (Utils.isEmpty(this.searchCode)) {
            this.nativeService.showToast('请输入容器编号');
            return;
        }
        let body = "lpnCode=" + this.searchCode;
        this.nativeService.showLoading();
        this.appService.httpRequest(Api.Instock + Api.queryStockByLpnCode, Method.Post, body, ContenType.Form, result => {
            this.nativeService.hideLoading();
            if (Utils.isNotEmpty(result.data)&&result.data.typeCount!='') {
                let Jsonresult = result.data;
                this.lpnCode = this.searchCode;
                this.zoneName = Jsonresult.zoneName;
                this.zoneCode = Jsonresult.zoneCode;
                this.typeCount = Jsonresult.typeCount;
                this.locStatusName = Jsonresult.locStatusName;
                this.whName = Jsonresult.whName;
                this.locCode = Jsonresult.locCode;
                this.whId = Jsonresult.whId;
                this.islocCode = Jsonresult.targetLocCode;
            }else{
              this.nativeService.showToast("该托盘没有要上架的物品!");
            }
        });
    }

    btnOk() {
        if (Utils.isEmpty(this.lpnCode)) {
            this.nativeService.showToast('容器不能为空,请扫描');
            return;
        }
       
        if (Utils.isEmpty(this.targetLocCodeComp.value)) {
            this.nativeService.showToast('目标库位不能为空');
            return;
        }
        let body = "zoneCode=" + this.zoneCode + "&locCode=" + this.targetLocCodeComp.value + "&lpnCode=" + this.lpnCode + "&whId=" + this.whId;
        this.nativeService.showLoading();
        this.appService.httpRequest(Api.Instock + Api.submitPutaway, Method.Post, body, ContenType.Form, result => {
            this.nativeService.hideLoading();
            this.clearModel();
            this.nativeService.showToast("提交成功");
        });
    }

    clearModel() {
        this.searchCode = ''
        this.lpnCode = '';
        this.zoneCode = '';
        this.typeCount = '';
        this.locCode = '';
        this.islocCode = '';
        this.targetLocCodeComp.value = '';
        this.locStatusName = '';
        this.whName = '';//仓库名称
        this.zoneName = '';
    }

    exit() {
        this.navCtrl.pop();
    }

    /**
     * 序列号回车事件
     */
    islocCode_KeyDown(event) {
        if (event && event.keyCode == 13) {
            // this.locCode=this.islocCode;
            // this.islocCode='';
            // this.btnOk();
        }
    }
}
