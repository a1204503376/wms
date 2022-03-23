import { AfterViewInit, Component, OnInit } from '@angular/core';
import { IonicPage, NavController } from 'ionic-angular';


@Component({
  selector: 'crumbs',
  templateUrl: 'crumbs.html'
})
export class CrumbsComponent {
  public dicts = [
    { id: "HomePage", name: "首页" },
    { id: "TaskviewPage", name: "任务查看" },

    { id: "CheckinhomePage", name: "收货" },
    { id: "CheckinpiecePage", name: "按件收货" },
    { id: "CheckInPage", name: "执行收货" },
    { id: "CheckinboxPage", name: "按箱收货" },
    { id: "SearchOrderPage", name: "按单收货" },
    { id: "CheckinBoxInfoPage", name: "执行收货" },
    { id: "CheckintrayPage", name: "按托收货" },
    { id: "CheckinDetailedPage", name: "收货记录" },

    { id: "CheckInTrayPage", name: "按托收货" },
    { id: "CheckInTrayInfoPage", name: "执行收货" },

    { id: "TakeawayTrayPage", name: "按托拣货" },
    { id: "TakeawayinTrayPage", name: "执行拣货" },

    { id: "CollageTaskPage", name: "拼托任务" },
    { id: "CollageTaskListPage", name: "托盘详情" },
    { id: "CollageTaskBoxPage", name: "托盘详情" },

    { id: "CyclecounthomePage", name: "盘点" },
    { id: "CyclecountrandominfoPage", name: "随机盘点" },
    { id: "CyclecountrantaskPage", name: "盘点任务" },
    { id: "CyclecountBillPage", name: "静态盘点" },
    { id: "CyclecoutrantaskinfoPage", name: "执行盘点" },

    { id: "MovehomePage", name: "移动" },
    { id: "MovesboxPage", name: "按箱移动" },
    { id: "MovesboxInfoPage", name: "按箱移动" },
    { id: "moveSku", name: "物品移动" },
    { id: "moveStock", name: "托盘移动" },
    { id: "moveStocks", name: "多拖移动" },


    { id: "PackageTaskPage", name: "打包任务" },
    { id: "PackageTaskListPage", name: "打包任务" },
    { id: "PackageTaskInfoPage", name: "任务明细" },

    { id: "PutawayhomePage", name: "上架" },
    { id: "PutawayPage", name: "按托上架" },
    { id: "PutawayboxPage", name: "按箱上架" },

    { id: "ReplenishmenhomePage", name: "补货任务" },
    { id: "ReplenishmenDetilePage", name: "补货任务" },

    { id: "SettingPage", name: "个人设置" },
    { id: "SettingPwdPage", name: "修改密码" },
    { id: "SettingThemePage", name: "主题颜色" },

    { id: "ShipmenthomePage", name: "发运" },
    { id: "ShipmentLpnPage", name: "托盘发运" },

    { id: "StockhomePage", name: "库存" },
    { id: "StockdetailPage", name: "库存查询" },
    { id: "StockqueryPage", name: "库存查询" },
    { id: "StockPritPage", name: "库存打印" },


    { id: "PutawayboxNewPage", name: "按箱上架" },
    { id: "PutawayTrayPage", name: "按托上架" },
    { id: "TakeawayhomePage", name: "拣货" },
    { id: "TakeawaySearchPage", name: "按件拣货" },
    { id: "TakeawayOrderPage", name: "按单拣货" },
    { id: "TakeawayinOrderPage", name: "执行拣货" },
    { id: "UpdateDetailOrderPage", name: "修改明细" },
    
    { id: "TakeawayPage", name: "执行拣货" },
    { id: "TakeawayinPage", name: "执行拣货" },
    { id: "TakeawayindetailedPage", name: "拣货记录" },
    { id: "TakeawaypieceboxPage", name: "按箱拣货" },
    { id: "TakeawayinboxPage", name: "执行拣货" },
    { id: "CougnyPage", name: "库内" },
    { id: "CopyLabelPage", name: "复制标签" },
    { id: "RandomCheckPage", name: "随机盘点" },
    { id: "RandomCheckTaskPage", name: "执行盘点" },
    { id: "ReplenishmenPage", name: "补货任务" },
    { id: "ReplenishmenRecordPage", name: "补货任务" },
    { id: "StockFrozenPage", name: "库存冻结" },
    { id: "CheckOrderPage", name: "执行收货" },
    { id: "UpdateDetailPage", name: "修改明细" },
    { id: "TaskwayMultPage", name: "批量拣货" },
    { id: "BindPrintterPage", name: "绑定打印机" },
    { id: "BindOrderPage", name: "单据绑定" },
    { id: "BindLpnPage", name: "绑定容器" },
    { id: "CyclecountRecordPage", name: "盘点记录" },
    { id: "MergeTrayPage", name: "拼托" }
  ]
  public alls: any = [];
  constructor(public navCtrl: NavController) {
  }
  ngOnInit() {
    if (this.navCtrl.getViews().length > 0) {
      this.navCtrl.getViews().forEach(a => {
        this.alls.push(this.distsValue(a))
        this.alls.push({ id: '_', name: '_' })
      })
    }
    if (this.alls.length > 0 && this.alls[this.alls.length - 1].id == '_') {
      this.alls.pop();
    }
    // console.log(this.alls)
  }
  toPush(item) {
    let active = this.navCtrl.getActive();
    if (active) {
      if (active.id != item.id) {
        let a = this.navCtrl.getByIndex(item.index);
        this.navCtrl.popTo(a);
      }
    }
  }
  distsValue(vc) {
    let value;
    this.dicts.forEach(a => {
      if (a.id == vc.name) {
        a["index"] = vc.index;
        value = a;
      }
    });
    return value;
  }
}