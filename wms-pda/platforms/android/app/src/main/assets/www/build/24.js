webpackJsonp([24],{

/***/ 1049:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return TakeawayindetailedPage; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(43);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__services_Utils__ = __webpack_require__(89);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};



/**
 * Generated class for the TakeawayindetailedPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */
var TakeawayindetailedPage = /** @class */ (function () {
    function TakeawayindetailedPage(navCtrl, navParams) {
        this.navCtrl = navCtrl;
        this.navParams = navParams;
        this.trailerTaskList = [1, 2, 3, 4, 5, 6, 7, 8, 9];
        this.groupRecords = [];
        this.skuLotNum = Array(30).fill(0).map(function (x, i) { return i; });
        this.pickPlans = this.navParams.get('pickPlans');
        this.finish = this.navParams.get('finish');
        this.count = this.navParams.get('count');
        this.wellenNo = this.navParams.get('wellenNo');
        this.orderNo = this.navParams.get('orderNo');
        this.sobillNo = this.navParams.get('sobillNo');
        if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isNotEmpty(this.pickPlans)) {
            var _loop_1 = function (item) {
                if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(this_1.groupRecords)) {
                    this_1.groupRecords.push(item);
                }
                else {
                    var arr = this_1.groupRecords.find(function (v) {
                        return v.skuCode == item.skuCode;
                    });
                    if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(arr)) {
                        this_1.groupRecords.push(item);
                        return "break";
                    }
                }
            };
            var this_1 = this;
            for (var _i = 0, _a = this.pickPlans; _i < _a.length; _i++) {
                var item = _a[_i];
                var state_1 = _loop_1(item);
                if (state_1 === "break")
                    break;
            }
        }
    }
    TakeawayindetailedPage.prototype.range = function (item) {
        var arr = [];
        for (var i = 1; i <= 30; i++) {
            if (item["skuLot" + i] && item["skuLot" + i] != '') {
                arr.push(item["skuLot" + i]);
            }
        }
        return arr;
    };
    TakeawayindetailedPage.prototype.ionViewDidLoad = function () {
        console.log('ionViewDidLoad TakeawayindetailedPage');
    };
    TakeawayindetailedPage.prototype.exit = function () {
        this.navCtrl.pop();
    };
    TakeawayindetailedPage = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Component"])({
            selector: 'page-takeawayindetailed',template:/*ion-inline-start:"D:\developer_tools\workspace\wms_3.0_base_pda\src\pages\takeawayhome\takeawaypiece\takeawayindetailed\takeawayindetailed.html"*/'<ion-header >\n\n  <ion-navbar color="wmscolor">\n\n    <ion-title (click)="expression()">\n\n      拣货记录\n\n      <!-- <div class="title_f_name">(青啤五厂)</div> -->\n\n    </ion-title>\n\n  </ion-navbar>\n\n  <div class="title-bt"> \n\n    <div class="order">\n\n     <div class="order-number">{{orderNo||sobillNo}}</div>\n\n     <div class="order-info">单据收货进度 {{finish}}/{{count}}</div>\n\n    </div>\n\n    <div class="nav-tile">\n\n      <!-- <ul>\n\n        <li>首页</li>\n\n        <li><ion-icon class="iconfont icon-arr-right"></ion-icon></li>\n\n        <li>拣货</li>\n\n        <li><ion-icon class="iconfont icon-arr-right"></ion-icon></li>\n\n        <li>按件拣货</li>\n\n        <li><ion-icon class="iconfont icon-arr-right"></ion-icon></li>\n\n        <li>执行拣货</li>\n\n        <li><ion-icon class="iconfont icon-arr-right"></ion-icon></li>\n\n        <li>拣货记录</li>\n\n        \n\n    </ul> -->\n\n    <crumbs></crumbs>\n\n </div>\n\n  </div>\n\n</ion-header>\n\n<ion-content  class="no-scroll">\n\n  <div class="total-title" >\n\n    <div *ngFor="let item of groupRecords">\n\n      <div *ngIf="item.totalPlanQty>0" class="box">\n\n        <span>【{{item.skuName}}】</span>\n\n        <span>{{item.totalScanQty}}/{{item.totalPlanQty}}</span>\n\n      </div>\n\n    </div>\n\n</div>\n\n  <div  *ngFor="let item of pickPlans" style="margin-top: 10px;">\n\n    <div class="nodes-list-item">\n\n      <span>{{item.skuName}}</span>\n\n      <div  class="item-bottom">\n\n        <div class="item-bottom-left">\n\n          <div>\n\n            <span>落放位置:</span>\n\n            <span>PACK</span>\n\n          </div>\n\n          <!-- <div>\n\n            <span>容&emsp;&emsp;器:</span>\n\n            <span>{{item.sourceLpnCode}}</span>\n\n          </div> -->\n\n          <div>\n\n            <span>库&emsp;&emsp;位:</span>\n\n            <span>{{item.sourceLocCode}}</span>\n\n          </div>\n\n          <div *ngFor="let skuLot of range(item.skuLot)">\n\n            <span>{{skuLot}}</span>\n\n          </div>\n\n        </div>\n\n        <div class="item-bottom-right">\n\n          <div>\n\n            <span>已经拣货:</span>\n\n            <span>{{item.realCountQty}}</span><span>/{{item.planCountQty}}</span>\n\n          </div>\n\n          <div>\n\n            <span>拣货人: </span>\n\n            <span>{{item.userName}}</span>\n\n          </div>\n\n        </div>\n\n      </div>\n\n    </div>\n\n  </div>\n\n <!-- <div  *ngFor="let item of pickPlans" class="title-ms">\n\n   <div class="title-ms-info">\n\n    <div class="title-font-s1">\n\n      <label >{{item.skuName}}</label>\n\n     </div>\n\n     <div class="title-font-small">\n\n      <div>\n\n        <label>落放位置:</label>\n\n        <label style="color: black;">PACK</label>\n\n       </div>\n\n       <div>\n\n        <label>容&emsp;&emsp;器:</label>\n\n        <label style="color: black;">{{item.sourceLpnCode}}</label>\n\n       </div>\n\n       <div class="title-font-s4">\n\n        <label >{{item.skuLot.skuLot1}}</label>\n\n       </div>\n\n       <div class="title-font-s4">\n\n        <label >{{item.skuLot.skuLot2}}</label>\n\n       </div>\n\n       <div class="received"> \n\n         已经拣货: <span style="color: #F59A23;">{{item.realCountQty}}</span>/{{item.planCountQty}}\n\n       </div>\n\n       <div class="consignee"> \n\n        拣货人: <span style="color: #F59A23;">{{item.userName}}</span>\n\n      </div>\n\n     \n\n     </div>\n\n   </div>\n\n </div> -->\n\n</ion-content>\n\n<ion-footer>\n\n  <div class="btn-box">\n\n    <button ion-button block class="btn-l" (click)="exit()">\n\n      返回[Esc]\n\n    </button>\n\n  </div>\n\n\n\n</ion-footer>\n\n'/*ion-inline-end:"D:\developer_tools\workspace\wms_3.0_base_pda\src\pages\takeawayhome\takeawaypiece\takeawayindetailed\takeawayindetailed.html"*/,
        }),
        __metadata("design:paramtypes", [__WEBPACK_IMPORTED_MODULE_1_ionic_angular__["l" /* NavController */], __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["m" /* NavParams */]])
    ], TakeawayindetailedPage);
    return TakeawayindetailedPage;
}());

//# sourceMappingURL=takeawayindetailed.js.map

/***/ }),

/***/ 749:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "TakeawayindetailedPageModule", function() { return TakeawayindetailedPageModule; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(43);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__takeawayindetailed__ = __webpack_require__(1049);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__components_crumbs_crumbs_module__ = __webpack_require__(781);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};




var TakeawayindetailedPageModule = /** @class */ (function () {
    function TakeawayindetailedPageModule() {
    }
    TakeawayindetailedPageModule = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["NgModule"])({
            declarations: [
                __WEBPACK_IMPORTED_MODULE_2__takeawayindetailed__["a" /* TakeawayindetailedPage */],
            ],
            imports: [
                __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["g" /* IonicPageModule */].forChild(__WEBPACK_IMPORTED_MODULE_2__takeawayindetailed__["a" /* TakeawayindetailedPage */]),
                __WEBPACK_IMPORTED_MODULE_3__components_crumbs_crumbs_module__["a" /* CrumbsModule */]
            ],
            schemas: [__WEBPACK_IMPORTED_MODULE_0__angular_core__["NO_ERRORS_SCHEMA"]]
        })
    ], TakeawayindetailedPageModule);
    return TakeawayindetailedPageModule;
}());

//# sourceMappingURL=takeawayindetailed.module.js.map

/***/ }),

/***/ 781:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return CrumbsModule; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__crumbs__ = __webpack_require__(782);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2_ionic_angular__ = __webpack_require__(43);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};



var CrumbsModule = /** @class */ (function () {
    function CrumbsModule() {
    }
    CrumbsModule = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["NgModule"])({
            declarations: [__WEBPACK_IMPORTED_MODULE_1__crumbs__["a" /* CrumbsComponent */]],
            imports: [__WEBPACK_IMPORTED_MODULE_2_ionic_angular__["g" /* IonicPageModule */].forChild(__WEBPACK_IMPORTED_MODULE_1__crumbs__["a" /* CrumbsComponent */]),
            ],
            exports: [__WEBPACK_IMPORTED_MODULE_1__crumbs__["a" /* CrumbsComponent */]]
        })
    ], CrumbsModule);
    return CrumbsModule;
}());

//# sourceMappingURL=crumbs.module.js.map

/***/ }),

/***/ 782:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return CrumbsComponent; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(43);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};


var CrumbsComponent = /** @class */ (function () {
    function CrumbsComponent(navCtrl) {
        this.navCtrl = navCtrl;
        this.dicts = [
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
            { id: "TakeawayhomePage", name: "拣货" },
            { id: "TakeawaypiecePage", name: "按件拣货" },
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
        ];
        this.alls = [];
    }
    CrumbsComponent.prototype.ngOnInit = function () {
        var _this = this;
        if (this.navCtrl.getViews().length > 0) {
            this.navCtrl.getViews().forEach(function (a) {
                _this.alls.push(_this.distsValue(a));
                _this.alls.push({ id: '_', name: '_' });
            });
        }
        if (this.alls.length > 0 && this.alls[this.alls.length - 1].id == '_') {
            this.alls.pop();
        }
        console.log(this.alls);
    };
    CrumbsComponent.prototype.toPush = function (item) {
        var active = this.navCtrl.getActive();
        if (active) {
            if (active.id != item.id) {
                var a = this.navCtrl.getByIndex(item.index);
                this.navCtrl.popTo(a);
            }
        }
    };
    CrumbsComponent.prototype.distsValue = function (vc) {
        var value;
        this.dicts.forEach(function (a) {
            if (a.id == vc.name) {
                a["index"] = vc.index;
                value = a;
            }
        });
        return value;
    };
    CrumbsComponent = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Component"])({
            selector: 'crumbs',template:/*ion-inline-start:"D:\developer_tools\workspace\wms_3.0_base_pda\src\components\crumbs\crumbs.html"*/'<ul >\n\n   <ng-container *ngFor="let item of alls">\n\n      <li *ngIf="item&&item.id!=\'_\';else elseBlock" (click)="toPush(item)"> \n\n         {{item.name}} \n\n      </li>\n\n      <ng-template #elseBlock>\n\n      <li>\n\n         <ion-icon class="iconfont iconicon_arr_right"></ion-icon>\n\n      </li>\n\n      </ng-template>\n\n   </ng-container>\n\n</ul>\n\n'/*ion-inline-end:"D:\developer_tools\workspace\wms_3.0_base_pda\src\components\crumbs\crumbs.html"*/
        }),
        __metadata("design:paramtypes", [__WEBPACK_IMPORTED_MODULE_1_ionic_angular__["l" /* NavController */]])
    ], CrumbsComponent);
    return CrumbsComponent;
}());

//# sourceMappingURL=crumbs.js.map

/***/ })

});
//# sourceMappingURL=24.js.map