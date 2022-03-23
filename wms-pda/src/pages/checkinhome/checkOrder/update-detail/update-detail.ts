import { Component, ViewChild, NgZone ,ChangeDetectorRef} from '@angular/core';
import { IonicPage, NavController, NavParams, ModalController, Events, Navbar } from 'ionic-angular';
import { Utils } from '../../../../services/Utils';
import { AppService } from "../../../../services/App.service";
import { AppConstant, Api, ContenType, Method, BaseCodeConstant } from '../../../../utils/appConstant';
import { NativeService } from "../../../../services/NativeService"
import { BarcodeService } from '../../../../services/BarCodeService';

@IonicPage()
@Component({
  selector: 'page-update-detail',
  templateUrl: 'update-detail.html',
})
export class UpdateDetailPage {
  public result: any = {};
  public skulots: any = [];
  public callback: any;
  public skuPackageDetails: any;//包装管理集合
  public skuPackageDetailsItem: any = {};//包装管理选中
  public planQtyBak:any;
  // public maxConvertQty:any;
  @ViewChild(Navbar) nav: Navbar;
  constructor(public navCtrl: NavController,
    private cdRef: ChangeDetectorRef,
    public navParams: NavParams,
    public modalController: ModalController,
    public appService: AppService,
    public nativeService: NativeService,
    public barcodeService: BarcodeService,
    public events: Events,
    public ngZone: NgZone
  ) {
      this.callback = this.navParams.get('callback');
      if (Utils.isNotEmpty(this.navParams.get('result'))) {
        let result = this.navParams.get('result');
        Object.assign(this.result, result);
        this.planQtyBak = this.result.planQty;
      }
      this.result.skuLotList.forEach(element => {
        let skulotVaule = this.result[`skuLot${element.skuLotIndex}`];
        if (Utils.isNotEmpty(skulotVaule)) {
          let skulot = {};
          skulot[`skuLotName`] = element.skuLotLabel;
          skulot[`skuLotValue`] = skulotVaule;
          this.skulots.push(skulot);
        }
      });
  }

  ionViewWillEnter() {
  }
  ionViewWillLeave() {
  }
  change(value) {
    this.cdRef.detectChanges();
    this.result.planQty = value >= this.planQtyBak ? this.planQtyBak : value <= 0 ? 1 : value;
  }
  ionViewDidLoad() {
    let that = this;
    this.nav.backButtonClick = function () {
      that.callback({ isPop: true });
      this.navCtrl.pop();
    }
  }
  backClick() {
    this.callback({ isPop: true });
    this.navCtrl.pop();
  }
  submitClick() {
    this.callback({ isPop: true, result: this.result,
    currentUmItem: this.skuPackageDetailsItem});
    this.navCtrl.pop();
  }
}
