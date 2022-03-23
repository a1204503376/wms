import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams, Events } from 'ionic-angular';
import { AppService } from "../../services/App.service";
import { Utils } from '../../services/Utils';
import { NativeService } from "../../services/NativeService";
import { Api, ContenType, Method, BaseCodeConstant, AppConstant } from '../../utils/appConstant';
import { Storage } from '@ionic/storage';
import { AppGlobal } from '../../services/AppGlobal';
import { of } from 'rxjs/observable/of';
import { BarcodeService, BarcodeType } from '../../services/BarCodeService';
/**
 * Generated class for the TaskviewPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-taskview',
  templateUrl: 'taskview.html',
})
export class TaskviewPage {
  public subHeader: any;

  public subHeaderModel: string = '全部';

  public taskResult: any;//任务列表

  public billNo: string;//单据编号

  public refreshType: string = '';

  public taskInventory: any;//任务：收货执行方式
  public taskPutaway: any;//任务：上架执行方式
  public taskTakeaway: any;//任务：拣货执行方式
  public tempList:any = [];
  constructor(public navCtrl: NavController,
    public navParams: NavParams,
    public appService: AppService,
    public nativeService: NativeService,
    public storage: Storage,
    private events: Events
  ) {
    this.subHeader = [
      { taskType: '', taskName: '全部' },
      { taskType: BaseCodeConstant.task_type_102, taskName: '收货' },
      { taskType: BaseCodeConstant.task_type_103, taskName: '发货' },
      { taskType: BaseCodeConstant.task_type_104, taskName: '盘点' },
      // { taskType: BaseCodeConstant.task_type_106, taskName: '打包' },
      // { taskType: BaseCodeConstant.task_type_107, taskName: '拼托' },
      { taskType: BaseCodeConstant.task_type_108, taskName: '补货' },
    ];
    this.subHeaderModel = this.subHeader[0];
    this.storage.get('sysParam').then(val => {
      if (Utils.isNotEmpty(val)) {
        this.taskInventory = val.filter(x => x.paramKey == 'task:inventory')[0];
        this.taskPutaway = val.filter(x => x.paramKey == 'task:putaway')[0];
        this.taskTakeaway = val.filter(x => x.paramKey == 'task:takeaway')[0];
      }
    });
  }

  ionViewWillEnter() {
    this.subHeaderModel = this.subHeader[0];
    this.nativeService.showLoading();
    this.taskPDAList('');
    //事件注册（扫描结果接收）
    this.events.subscribe('barcode:scan', (barcode, time) => {
      this.taskPDAList({ billNo: barcode });
    });
  }
  ionViewWillLeave() {
    AppGlobal.removeSubscribe(this);
  }
  headSelected(item) {
    this.subHeaderModel = item;
    this.nativeService.showLoading();
    this.refreshType = item.taskType;
    debugger
    this.taskPDAList({ taskTypeCd: item.taskType });
  }
  onClick(item) {
    this.nativeService.showLoading();
    if (item.taskTypeCd == BaseCodeConstant.task_type_102) {
      if (Utils.isEmpty(item.billNo)) {
        this.nativeService.showToast('单据编号为空');
        return;
      }
      this.AsnHeaderList({ asnBillNo: item.billNo,taskId:item.taskId });
    } else if (item.taskTypeCd == BaseCodeConstant.task_type_103) {
      if (Utils.isEmpty(item.billNo)) {
        this.nativeService.showToast('单据编号为空');
        return;
      }
      this.OutHeaderList({ wellenNo: item.billNo,taskId:item.taskId  });
    } else if (item.taskTypeCd == BaseCodeConstant.task_type_104) {
      if (Utils.isEmpty(item.billNo)) {
        this.nativeService.showToast('单据编号为空');
        return;
      }
      this.cyclecountrantask({ countBillNo: item.billNo });  // 调接口的盘点详情
    }
    else if (item.taskTypeCd == BaseCodeConstant.task_type_106) {
      if (Utils.isEmpty(item.billNo)) {
        this.nativeService.showToast('单据编号为空');
        return;
      }
      this.upPickList({ taskId: item.taskId });  // 调接口的盘点详情
    }
    else if (item.taskTypeCd == BaseCodeConstant.task_type_107) {
      if (Utils.isEmpty(item.billNo)) {
        this.nativeService.showToast('单据编号为空');
        return;
      }
      this.getOutstockLpnInfo({ taskId: item.taskId });  // 调接口的盘点详情
    }
    else if (item.taskTypeCd == BaseCodeConstant.task_type_108) {
      if (Utils.isEmpty(item.billNo)) {
        this.nativeService.showToast('单据编号为空');
        return;
      }
      this.navCtrl.push('ReplenishmenPage', {taskId: item.taskId });
    }
  }
  /**
   * 获取收货单任务明细
   */
  AsnHeaderList(params) {
    //按件收货
    if (this.taskInventory.paramValue == BaseCodeConstant.INVENTORY_1) {
      this.appService.httpRequest(Api.Instock + Api.AsnHeaderList, Method.Post, params, ContenType.Json, result => {
        this.nativeService.hideLoading();
        if (Utils.isEmpty(result.data)) {
          this.nativeService.showToast('该单据不存在或已收货完成');
          return;

        } else {
          if (Utils.isNotEmpty(result.data[0].asnDetailMinVO)) {
            this.navCtrl.push('CheckInPage', { result: result.data[0],taskId: params.taskId});
          }
        }
      });
    } else {
      //按箱收货
      this.appService.httpRequest(Api.Instock + Api.getAsnHeaderInfoForBox, Method.Get, params, ContenType.Form, result => {
        this.nativeService.hideLoading();
        if (Utils.isEmpty(result.data)) {
          this.nativeService.showToast('该单据已经完成或不存在');
        } else {
          if (Utils.isNotEmpty(result.data)) {
            this.navCtrl.push('CheckinBoxInfoPage', { result: result.data,taskId: params.taskId });
          }
        }
      });
    }

  }
  /**
   * 获取盘点明细
   * @param params
   */
  cyclecountrantask(params) {
    this.appService.httpRequest(Api.StockCountPDA + Api.cyclecountrantask, Method.Get, params, ContenType.Content, result => {
      // console.log(result.data.countDetailVOList,'result盘点')
      this.nativeService.hideLoading();
      if (Utils.isEmpty(result.data)) {
        this.nativeService.showToast('该单据不存在或已收货完成');
        return;
      } else {
        //进入盘点
        this.navCtrl.push('CyclecountrantaskPage', { result: result.data.countDetailVOList });
      }
    });
  }
  /**
   * 打包任务
   */
  upPickList(params) {
    this.appService.httpRequest(Api.stockInner + Api.upPickList, Method.Get, {
      taskId: params.taskId
    },
      ContenType.Content, result => {
        this.nativeService.hideLoading();
        if (Utils.isNotEmpty(result.data)) {
          this.navCtrl.push('PackageTaskPage', { result: result.data, taskId: params.taskId });
        } else {
          this.navCtrl.push('PackageTaskListPage', { taskId: params.taskId });
        }
      });
  }
  /**
   * 拼托任务
   * @param params
   */
  getOutstockLpnInfo(params) {
    this.appService.httpRequest(Api.Outstock + Api.getOutstockLpnInfo, Method.Get, { taskId: params.taskId },
      ContenType.Content, result => {
        this.nativeService.hideLoading();
        if (Utils.isEmpty(result.data)) {
          this.nativeService.showToast('该单据不存在或已收货完成');
          return;
        } else {
          if (Utils.isNotEmpty(result.data)) {
            //按件收货
            this.navCtrl.push('CollageTaskPage', { result: result.data, taskId: params.taskId });
          }
        }
      });
  }
  /**
  * 出库任务
  */
  OutHeaderList(params) {
    this.appService.httpRequest(Api.Outstock + Api.getPickInfo, Method.Get, params,
      ContenType.Form, result => {
        this.nativeService.hideLoading();
        let PickInfoResult = result.data;
        let pickPlansList = PickInfoResult.pickPlans;
        if (Utils.isEmpty(pickPlansList)) {
          this.nativeService.showToast('当前任务下没有物品');
          return;
        }
        //默认取第一条
        let pickPlansModel = pickPlansList[0];
        if (Utils.isNotEmpty(pickPlansModel)) {
          if (this.taskTakeaway.paramValue == BaseCodeConstant.TAKEAWAY_1) {
            this.navCtrl.push('TakeawayinPage', { pickPlansModel: pickPlansModel, PickInfoResult: PickInfoResult, pickPlansList: pickPlansList, wellenNo: params.wellenNo,taskId: params.taskId});
          } else if(this.taskTakeaway.paramValue == BaseCodeConstant.TAKEAWAY_2) {
            this.navCtrl.push('TakeawayinboxPage', { pickPlansModel: pickPlansModel, PickInfoResult: PickInfoResult, pickPlansList: pickPlansList, wellenNo: params.wellenNo,taskId: params.taskId});
          }else{
            this.navCtrl.push('TakeawayinTrayPage', { pickPlansModel: pickPlansModel, PickInfoResult: PickInfoResult, pickPlansList: pickPlansList, wellenNo: params.wellenNo,taskId: params.taskId});
          }
        }
      });
  }

  /**
  * 物品回车事件
  */
  billNo_KeyDown() {
    this.taskResult = this.tempList;
    if(Utils.isEmpty(this.taskResult))return;
    this.taskResult = this.taskResult.filter(t=>t.taskRemark.includes(this.billNo));
  }
  /**
   * 任务集合接口
   */
  taskPDAList(params) {
    this.appService.httpRequest(Api.taskPDA + Api.taskPDAList, Method.Get, params, '', result => {
      this.nativeService.hideLoading();
      this.taskResult = result.data;
      this.tempList=[];
      Object.assign(this.tempList,this.taskResult);
      if(Utils.isNotEmpty(this.billNo)){
        this.billNo_KeyDown();
      }
    });
  }
  /**
   * 手动刷新
   */
  onRefresh() {
    this.nativeService.showLoading();
    this.taskPDAList({ taskTypeCd: this.refreshType });
  }
}
