webpackJsonp([45],{

/***/ 1027:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return MovesboxInfoPage; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(43);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__services_Utils__ = __webpack_require__(89);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__services_App_service__ = __webpack_require__(157);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__utils_appConstant__ = __webpack_require__(65);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5__services_NativeService__ = __webpack_require__(64);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_6__ionic_storage__ = __webpack_require__(66);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_7__services_BarCodeService__ = __webpack_require__(359);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_8__services_AppGlobal__ = __webpack_require__(360);
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
 * Generated class for the MovesboxInfoPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */
var MovesboxInfoPage = /** @class */ (function () {
    function MovesboxInfoPage(navCtrl, navParams, appService, nativeService, viewCtrl, storage, events, ngZone, barcodeService) {
        var _this = this;
        this.navCtrl = navCtrl;
        this.navParams = navParams;
        this.appService = appService;
        this.nativeService = nativeService;
        this.viewCtrl = viewCtrl;
        this.storage = storage;
        this.events = events;
        this.ngZone = ngZone;
        this.barcodeService = barcodeService;
        this.skuStockList = []; //移动数据列表
        this.sysparms = []; //系统参数
        if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isNotEmpty(this.navParams.get('skuStockList'))) {
            this.skuStockList = this.navParams.get('skuStockList');
        }
        if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isNotEmpty(this.navParams.get('sourceLocCode'))) {
            this.sourceLocCode = this.navParams.get('sourceLocCode');
        }
        if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isNotEmpty(this.navParams.get('sourceLpnCode'))) {
            this.sourceLpnCode = this.navParams.get('sourceLpnCode');
        }
        this.storage.get('sysParam').then(function (data) {
            _this.sysparms = data.filter(function (x) { return x.paramKey == 'system::lpnEnable'; })[0];
        });
    }
    MovesboxInfoPage.prototype.ionViewWillEnter = function () {
        var _this = this;
        //事件注册（扫描结果接收）
        this.events.subscribe('barcode:scan', function (barcode, time) {
            var bt = _this.barcodeService.GetBarcodeType(barcode);
            if (bt == __WEBPACK_IMPORTED_MODULE_7__services_BarCodeService__["b" /* BarcodeType */].HuoWei) {
                _this.ngZone.run(function () {
                    _this.targetLocCode = barcode;
                });
            }
        });
    };
    MovesboxInfoPage.prototype.ionViewWillLeave = function () {
        __WEBPACK_IMPORTED_MODULE_8__services_AppGlobal__["a" /* AppGlobal */].removeSubscribe(this);
    };
    MovesboxInfoPage.prototype.ionViewDidLoad = function () {
        console.log('ionViewDidLoad MovesboxInfoPage');
    };
    /**
     * 库位转移提交
     */
    MovesboxInfoPage.prototype.btnOk = function () {
        var _this = this;
        if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(this.targetLocCode)) {
            this.nativeService.showToast('请填写目标库位');
            return;
        }
        for (var index = 0; index < this.skuStockList.length; index++) {
            this.skuStockList[index]['targetLocCode'] = this.targetLocCode;
            this.skuStockList[index]['targetLpnCode'] = this.targetLpnCode;
        }
        this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_4__utils_appConstant__["b" /* Api */].stockInner + __WEBPACK_IMPORTED_MODULE_4__utils_appConstant__["b" /* Api */].submitStockForMoveByBox, __WEBPACK_IMPORTED_MODULE_4__utils_appConstant__["f" /* Method */].Post, this.skuStockList, __WEBPACK_IMPORTED_MODULE_4__utils_appConstant__["e" /* ContenType */].Json, function (result) {
            _this.nativeService.showToast('提交成功');
            _this.viewCtrl.dismiss(true);
        });
    };
    MovesboxInfoPage.prototype.exit = function () {
        this.viewCtrl.dismiss(false);
    };
    MovesboxInfoPage = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Component"])({
            selector: 'page-movesbox-info',template:/*ion-inline-start:"D:\developer_tools\workspace\wms_3.0_base_pda\src\pages\movehome\movesbox\movesbox-info\movesbox-info.html"*/'<!--\n\n  Generated template for the MovesboxInfoPage page.\n\n\n\n  See http://ionicframework.com/docs/components/#navigation for more info on\n\n  Ionic pages and navigation.\n\n-->\n\n<ion-header>\n\n  <ion-navbar color="wmscolor">\n\n    <ion-title>\n\n      按箱移动\n\n      <div class="title_f_name">(欣天新)</div>\n\n    </ion-title>\n\n  </ion-navbar>\n\n  <div class="title-bt">\n\n    <div class="nav-tile">\n\n      <!-- <ul>\n\n        <li>首页</li>\n\n        <li>\n\n          <ion-icon class="iconfont  iconicon_arr_right"></ion-icon>\n\n        </li>\n\n        <li>移动</li>\n\n        <li>\n\n          <ion-icon class="iconfont  iconicon_arr_right"></ion-icon>\n\n        </li>\n\n        <li>物品移动</li>\n\n      </ul> -->\n\n      <!-- <crumbs></crumbs> -->\n\n    </div>\n\n  </div>\n\n</ion-header>\n\n<ion-content>\n\n    <ion-item *ngIf="sysparms.paramValue==0">\n\n      <ion-label>目标容器：</ion-label>\n\n    <ion-input type="text" [(ngModel)]="targetLpnCode"></ion-input>\n\n    <ion-avatar item-end>\n\n      <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n    </ion-avatar>\n\n  </ion-item>\n\n  <ion-item>\n\n    <ion-label>目标库位：</ion-label>\n\n    <ion-input type="text" [(ngModel)]="targetLocCode"></ion-input>\n\n    <ion-avatar item-end>\n\n      <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n    </ion-avatar>\n\n  </ion-item>\n\n\n\n  <ion-item>\n\n    <ion-label>原始库位：</ion-label>\n\n    <ion-input readonly type="text" [(ngModel)]="sourceLocCode"></ion-input>\n\n    <ion-avatar item-end>\n\n      <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n    </ion-avatar>\n\n  </ion-item>\n\n\n\n  <ion-item *ngIf="sysparms.paramValue==0">\n\n    <ion-label>原始容器：</ion-label>\n\n    <ion-input readonly="readonly"  type="text" [(ngModel)]="sourceLpnCode"></ion-input>\n\n    <ion-avatar item-end>\n\n      <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n    </ion-avatar>\n\n  </ion-item>\n\n</ion-content>\n\n<ion-footer>\n\n  <div class="btn-box">\n\n    <button ion-button block class="btn-l" (click)="exit()">\n\n      返回[Esc]\n\n    </button>\n\n    <button ion-button block class="btn-d" (click)="btnOk()">\n\n      确认[Ent]\n\n    </button>\n\n  </div>\n\n</ion-footer>'/*ion-inline-end:"D:\developer_tools\workspace\wms_3.0_base_pda\src\pages\movehome\movesbox\movesbox-info\movesbox-info.html"*/,
        }),
        __metadata("design:paramtypes", [__WEBPACK_IMPORTED_MODULE_1_ionic_angular__["l" /* NavController */],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["m" /* NavParams */],
            __WEBPACK_IMPORTED_MODULE_3__services_App_service__["a" /* AppService */],
            __WEBPACK_IMPORTED_MODULE_5__services_NativeService__["a" /* NativeService */],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["r" /* ViewController */],
            __WEBPACK_IMPORTED_MODULE_6__ionic_storage__["b" /* Storage */],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["c" /* Events */],
            __WEBPACK_IMPORTED_MODULE_0__angular_core__["NgZone"],
            __WEBPACK_IMPORTED_MODULE_7__services_BarCodeService__["a" /* BarcodeService */]])
    ], MovesboxInfoPage);
    return MovesboxInfoPage;
}());

//# sourceMappingURL=movesbox-info.js.map

/***/ }),

/***/ 728:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "MovesboxInfoPageModule", function() { return MovesboxInfoPageModule; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(43);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__movesbox_info__ = __webpack_require__(1027);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__components_crumbs_crumbs_module__ = __webpack_require__(781);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};




var MovesboxInfoPageModule = /** @class */ (function () {
    function MovesboxInfoPageModule() {
    }
    MovesboxInfoPageModule = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["NgModule"])({
            declarations: [
                __WEBPACK_IMPORTED_MODULE_2__movesbox_info__["a" /* MovesboxInfoPage */],
            ],
            imports: [
                __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["g" /* IonicPageModule */].forChild(__WEBPACK_IMPORTED_MODULE_2__movesbox_info__["a" /* MovesboxInfoPage */]),
                __WEBPACK_IMPORTED_MODULE_3__components_crumbs_crumbs_module__["a" /* CrumbsModule */]
            ],
            schemas: [__WEBPACK_IMPORTED_MODULE_0__angular_core__["NO_ERRORS_SCHEMA"]]
        })
    ], MovesboxInfoPageModule);
    return MovesboxInfoPageModule;
}());

//# sourceMappingURL=movesbox-info.module.js.map

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
//# sourceMappingURL=45.js.map