webpackJsonp([22],{

/***/ 1052:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return TakeawaypieceboxPage; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(43);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__services_App_service__ = __webpack_require__(157);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__services_Utils__ = __webpack_require__(89);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__services_NativeService__ = __webpack_require__(64);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__ = __webpack_require__(65);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_6__services_BarCodeService__ = __webpack_require__(359);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_7__services_AppGlobal__ = __webpack_require__(360);
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
 * Generated class for the TakeawaypieceboxPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */
var TakeawaypieceboxPage = /** @class */ (function () {
    function TakeawaypieceboxPage(navCtrl, navParams, appService, nativeService, events, barCodeService, ngZone) {
        this.navCtrl = navCtrl;
        this.navParams = navParams;
        this.appService = appService;
        this.nativeService = nativeService;
        this.events = events;
        this.barCodeService = barCodeService;
        this.ngZone = ngZone;
        this.current = 1; //当前页
        this.size = 10; //数量
        this.records = [];
    }
    TakeawaypieceboxPage.prototype.ionViewDidEnter = function () {
        this.asnBillNo = '';
    };
    TakeawaypieceboxPage.prototype.ionViewWillEnter = function () {
        var _this = this;
        //事件注册（扫描结果接收）
        this.events.subscribe('barcode:scan', function (barcode, time) {
            if (__WEBPACK_IMPORTED_MODULE_7__services_AppGlobal__["a" /* AppGlobal */].scanFlag) {
                __WEBPACK_IMPORTED_MODULE_7__services_AppGlobal__["a" /* AppGlobal */].scanFlag = false;
                _this.analysisDate(barcode);
            }
        });
        if (this.navParams.get('flag') || false) {
            this.resetPage();
        }
    };
    TakeawaypieceboxPage.prototype.onClick = function (record) {
        this.getHeadInfo(record.billNo);
    };
    TakeawaypieceboxPage.prototype.getContainerList = function () {
        var _this = this;
        if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isEmpty(this.orderNo)) {
            this.nativeService.showToast("单据编号不能为空！");
            return;
        }
        this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].api + __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].takewayList, __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["f" /* Method */].Post, 'current=' + this.current + '&' +
            'size=' + this.size + '&' +
            'orderNo=' + this.orderNo, __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["e" /* ContenType */].Form, function (result) {
            if (result.data.records.length == 1) {
                _this.getHeadInfo(result.data.records[0].billNo);
            }
            else {
                _this.records = _this.records.concat(result.data.records);
                if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isEmpty(_this.records)) {
                    if (_this.infiniteScroll) {
                        _this.infiniteScroll.enable(false);
                        _this.infiniteScroll.complete();
                    }
                    else {
                        _this.nativeService.showToast("该单据没有查到拣货任务");
                    }
                    return;
                }
            }
            _this.current++;
            if (_this.infiniteScroll) {
                _this.infiniteScroll.complete(); /*请求完成数据更新页面*/
            }
        });
    };
    TakeawaypieceboxPage.prototype.loadData = function (infiniteScroll) {
        this.infiniteScroll = infiniteScroll;
        this.getContainerList();
    };
    TakeawaypieceboxPage.prototype.toggleFun = function () {
        console.log(this.asnBillNo);
    };
    TakeawaypieceboxPage.prototype.ionViewWillLeave = function () {
        __WEBPACK_IMPORTED_MODULE_7__services_AppGlobal__["a" /* AppGlobal */].removeSubscribe(this);
    };
    /**
     * 数据解析
     */
    TakeawaypieceboxPage.prototype.analysisDate = function (barcode) {
        var _this = this;
        // let bt = this.barCodeService.GetBarcodeType(barcode);
        this.ngZone.run(function () {
            _this.orderNo = barcode;
            _this.resetPage();
            _this.getContainerList();
        });
    };
    TakeawaypieceboxPage.prototype.resetPage = function () {
        this.records = [];
        if (this.infiniteScroll) {
            this.infiniteScroll.enable(true);
        }
        this.size = 10;
        this.current = 1;
    };
    /**
    * 入库单回车事件
    */
    TakeawaypieceboxPage.prototype.keyEnter = function (event) {
        if (event && event.keyCode == 13) {
            this.resetPage();
            this.getContainerList();
        }
    };
    TakeawaypieceboxPage.prototype.getHeadInfo = function (wellenNo) {
        var _this = this;
        var params = {
            wellenNo: wellenNo
        };
        this.nativeService.showLoading();
        this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].Outstock + __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].getPickInfoByBox, __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["f" /* Method */].Get, params, __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["e" /* ContenType */].Form, function (result) {
            _this.nativeService.hideLoading();
            __WEBPACK_IMPORTED_MODULE_7__services_AppGlobal__["a" /* AppGlobal */].scanFlag = true;
            _this.PickInfoResult = result.data;
            _this.pickPlansList = _this.PickInfoResult.pickPlans;
            if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isEmpty(_this.pickPlansList)) {
                _this.nativeService.showToast('当前任务下没有物品');
                return;
            }
            //默认取第一条
            _this.pickPlansModel = _this.pickPlansList[0];
            if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isNotEmpty(_this.pickPlansModel)) {
                _this.navCtrl.push('TakeawayinboxPage', { pickPlansModel: _this.pickPlansModel, PickInfoResult: _this.PickInfoResult, pickPlansList: _this.pickPlansList, wellenNo: wellenNo });
            }
        });
    };
    TakeawaypieceboxPage = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Component"])({
            selector: 'page-takeawaypiecebox',template:/*ion-inline-start:"D:\developer_tools\workspace\wms_3.0_base_pda\src\pages\takeawayhome\takeawaypiecebox\takeawaypiecebox.html"*/'<!--\n\n  Generated template for the TaskviewPage page.\n\n\n\n  See http://ionicframework.com/docs/components/#navigation for more info on\n\n  Ionic pages and navigation.\n\n-->\n\n<ion-header>\n\n  <ion-navbar color="wmscolor">\n\n    <ion-title>按箱拣货\n\n      <!-- <div class="title_f_name">(青啤五厂)</div> -->\n\n    </ion-title>\n\n    <!-- <ion-buttons end (click)="detailed()">\n\n      <button ion-button icon-only>\n\n        <ion-icon name="menu"></ion-icon>\n\n      </button>\n\n    </ion-buttons> -->\n\n  </ion-navbar>\n\n  <ion-searchbar [(ngModel)]="orderNo" debounce="500" (keydown)="keyEnter($event)" Description=\'primary\'\n\n    placeholder="订单编号(WMS单号|T100单号)">\n\n  </ion-searchbar>\n\n  <!-- <ion-input  [(ngModel)]="asnBillNo"  (keyup)="keyEnter($event)" type="text"> -->\n\n  <!-- </ion-input> -->\n\n  <!-- <div class="nav-tile">\n\n    <ul>\n\n      <li>首页</li>\n\n      <li>\n\n        <ion-icon class="iconfont  iconicon_arr_right"></ion-icon>\n\n      </li>\n\n      <li>拣货</li>\n\n      <li>\n\n        <ion-icon class="iconfont  iconicon_arr_right"></ion-icon>\n\n      </li>\n\n      <li>按箱拣货</li>\n\n    </ul>\n\n  </div> -->\n\n  <div class="nav-tile"> \n\n  <crumbs></crumbs>\n\n  </div>\n\n</ion-header>\n\n<ion-content>\n\n  <div class="nodes-list-box">\n\n    <div class="nodes-list-item-box" *ngFor="let record of records" (click)="onClick(record)" >\n\n      <div class="left">\n\n        <span>{{record.billNo}}</span>\n\n        <span>{{record.whName}}</span>\n\n      </div>\n\n      <div class="right">\n\n      </div>\n\n    </div>\n\n  </div>\n\n  \n\n  <ion-infinite-scroll threshold="100px" (ionInfinite)="loadData($event)">\n\n    <ion-infinite-scroll-content\n\n      loadingSpinner="bubbles"\n\n      loadingText="加载更多...">\n\n    </ion-infinite-scroll-content>\n\n  </ion-infinite-scroll>\n\n</ion-content>'/*ion-inline-end:"D:\developer_tools\workspace\wms_3.0_base_pda\src\pages\takeawayhome\takeawaypiecebox\takeawaypiecebox.html"*/,
        }),
        __metadata("design:paramtypes", [__WEBPACK_IMPORTED_MODULE_1_ionic_angular__["l" /* NavController */],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["m" /* NavParams */],
            __WEBPACK_IMPORTED_MODULE_2__services_App_service__["a" /* AppService */],
            __WEBPACK_IMPORTED_MODULE_4__services_NativeService__["a" /* NativeService */],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["c" /* Events */],
            __WEBPACK_IMPORTED_MODULE_6__services_BarCodeService__["a" /* BarcodeService */],
            __WEBPACK_IMPORTED_MODULE_0__angular_core__["NgZone"]])
    ], TakeawaypieceboxPage);
    return TakeawaypieceboxPage;
}());

//# sourceMappingURL=takeawaypiecebox.js.map

/***/ }),

/***/ 752:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "TakeawaypieceboxPageModule", function() { return TakeawaypieceboxPageModule; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(43);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__takeawaypiecebox__ = __webpack_require__(1052);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__components_crumbs_crumbs_module__ = __webpack_require__(781);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};




var TakeawaypieceboxPageModule = /** @class */ (function () {
    function TakeawaypieceboxPageModule() {
    }
    TakeawaypieceboxPageModule = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["NgModule"])({
            declarations: [
                __WEBPACK_IMPORTED_MODULE_2__takeawaypiecebox__["a" /* TakeawaypieceboxPage */],
            ],
            imports: [
                __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["g" /* IonicPageModule */].forChild(__WEBPACK_IMPORTED_MODULE_2__takeawaypiecebox__["a" /* TakeawaypieceboxPage */]),
                __WEBPACK_IMPORTED_MODULE_3__components_crumbs_crumbs_module__["a" /* CrumbsModule */]
            ],
            schemas: [__WEBPACK_IMPORTED_MODULE_0__angular_core__["NO_ERRORS_SCHEMA"]]
        })
    ], TakeawaypieceboxPageModule);
    return TakeawaypieceboxPageModule;
}());

//# sourceMappingURL=takeawaypiecebox.module.js.map

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
//# sourceMappingURL=22.js.map