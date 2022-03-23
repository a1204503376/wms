import { Component, ViewChild, NgZone } from '@angular/core';
import { IonicPage, NavController, NavParams, ModalController, Events,MenuController  } from 'ionic-angular';
import { Utils } from '../../../services/Utils';
import { ScanModel } from "../../../models/DataBase.Model";
import { AppService } from "../../../services/App.service";
import { AppConstant, Api, ContenType, Method, BaseCodeConstant } from '../../../utils/appConstant';
import { NativeService } from "../../../services/NativeService"
import { BarcodeService, BarcodeType } from '../../../services/BarCodeService';
import { s } from '@angular/core/src/render3';
import { SelectItem } from 'primeng/api';
import { AppGlobal } from '../../../services/AppGlobal';

@IonicPage()
@Component({
  selector: 'page-taskway-mult',
  templateUrl: 'taskway-mult.html',
})
export class TaskwayMultPage {

  IsShow: boolean = true;

  taskId: string;//任务ID

  result: any = {};//补货数据集

  selectModel: any = {}; //选中任务

  scanView: string;//扫描信息

  locList: SelectItem[] = [];

  public locCode: string;//当前库位

  @ViewChild('labelTypecom') labelTypecom;
  constructor(public navCtrl: NavController,
    public navParams: NavParams,
    public modalController: ModalController,
    public appService: AppService,
    public nativeService: NativeService,
    public barcodeService: BarcodeService,
    public events: Events,
    public ngZone: NgZone,
    public menu: MenuController
  ) {
    menu.enable(true);
  }
  ionViewWillEnter() {
  }
  ionViewWillLeave() {
  }
  ionViewDidLoad() {
  }
}
