webpackJsonp([53],{

/***/ 1019:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return StockFrozenPage; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(43);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__services_Utils__ = __webpack_require__(89);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__services_App_service__ = __webpack_require__(157);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__utils_appConstant__ = __webpack_require__(65);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5__services_NativeService__ = __webpack_require__(64);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_6__services_BarCodeService__ = __webpack_require__(359);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_7__services_AppGlobal__ = __webpack_require__(360);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_8__ionic_storage__ = __webpack_require__(66);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};









var StockFrozenPage = /** @class */ (function () {
    function StockFrozenPage(navCtrl, navParams, popoverCtrl, storage, modalController, appService, nativeService, barcodeService, events, ngZone) {
        var _this = this;
        this.navCtrl = navCtrl;
        this.navParams = navParams;
        this.popoverCtrl = popoverCtrl;
        this.storage = storage;
        this.modalController = modalController;
        this.appService = appService;
        this.nativeService = nativeService;
        this.barcodeService = barcodeService;
        this.events = events;
        this.ngZone = ngZone;
        this.current = 1; //当前页 
        this.size = 10; //每页条数
        this.stockVOList = []; //库存查询列表
        this.locCode = ""; //库位编码
        this.skuLotNum = Array(30).fill(0).map(function (x, i) { return i; });
        this.storage.get('sysParam').then(function (data) {
            _this.paramValue = data.filter(function (x) { return x.paramKey == 'system::lpnEnable'; })[0]["paramValue"];
        });
    }
    StockFrozenPage.prototype.ionViewWillEnter = function () {
        var _this = this;
        this.events.subscribe('barcode:scan', function (barcode, time) {
            var bt = _this.barcodeService.GetBarcodeType(barcode);
            if (bt != __WEBPACK_IMPORTED_MODULE_6__services_BarCodeService__["b" /* BarcodeType */].HuoWei) {
                _this.nativeService.showToast("请扫描正确的库位编码！");
                return;
            }
            _this.ngZone.run(function () {
                _this.locCode = barcode;
                _this.resetPage();
                _this.queryStockRequest();
            });
        });
    };
    StockFrozenPage.prototype.ionViewWillLeave = function () {
        __WEBPACK_IMPORTED_MODULE_7__services_AppGlobal__["a" /* AppGlobal */].removeSubscribe(this);
    };
    StockFrozenPage.prototype.ionViewDidLoad = function () {
    };
    StockFrozenPage.prototype.presentPopover = function (myEvent) {
        var _this = this;
        var popover = this.popoverCtrl.create('PopoverPage', { show_item_3: false });
        popover.onDidDismiss(function (data) {
            if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isNotEmpty(data) && __WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isNotEmpty(_this.stockVOList)) {
                console.log(_this.stockVOList);
                if (data == 'allCheck') {
                    //全选
                    for (var index = 0; index < _this.stockVOList.length; index++) {
                        _this.stockVOList[index].CheckStock = true;
                    }
                }
                else if (data == 'clearCheck') {
                    //取消全选
                    for (var index = 0; index < _this.stockVOList.length; index++) {
                        _this.stockVOList[index].CheckStock = false;
                    }
                }
            }
        });
        popover.present({
            ev: myEvent
        });
    };
    StockFrozenPage.prototype.queryStockRequest = function () {
        var _this = this;
        var params = {
            'locCode': this.locCode
        };
        this.nativeService.showLoading();
        this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_4__utils_appConstant__["b" /* Api */].api + __WEBPACK_IMPORTED_MODULE_4__utils_appConstant__["b" /* Api */].StockQuery
            + "?current=" + this.current
            + "&size=" + this.size
            + "&type=0", __WEBPACK_IMPORTED_MODULE_4__utils_appConstant__["f" /* Method */].Post, params, __WEBPACK_IMPORTED_MODULE_4__utils_appConstant__["e" /* ContenType */].Json, function (result) {
            _this.nativeService.hideLoading();
            if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(result.data.records)) {
                if (_this.infiniteScroll) {
                    _this.infiniteScroll.enable(false);
                    _this.infiniteScroll.complete();
                }
                else {
                    _this.nativeService.showToast('暂无库存信息！');
                }
                return;
            }
            _this.stockVOList = _this.stockVOList.concat(result.data.records);
            _this.current++;
            if (_this.infiniteScroll) {
                _this.infiniteScroll.complete();
            }
        });
    };
    StockFrozenPage.prototype.keyEnter = function (event) {
        if (event && event.keyCode == 13) {
            this.resetPage();
            this.queryStockRequest();
        }
    };
    StockFrozenPage.prototype.resetPage = function () {
        this.stockVOList = [];
        if (this.infiniteScroll) {
            this.infiniteScroll.enable(true);
        }
        this.size = 10;
        this.current = 1;
    };
    StockFrozenPage.prototype.loadData = function (infiniteScroll) {
        this.infiniteScroll = infiniteScroll;
        this.queryStockRequest();
    };
    StockFrozenPage.prototype.range = function (item) {
        var arr = [];
        for (var i = 1; i <= 30; i++) {
            if (item["skuLot" + i] && item["skuLot" + i] != '') {
                arr.push(item["skuLot" + i]);
            }
        }
        return arr;
    };
    StockFrozenPage.prototype.singleClick = function (item) {
        if (item.stockStatus == 0) {
            //冻结
        }
        else {
            //解冻
        }
    };
    StockFrozenPage.prototype.multClick = function (type) {
        if (type == 1) {
            //冻结
        }
        else {
            //解冻
        }
    };
    StockFrozenPage = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Component"])({
            selector: 'page-stock-frozen',template:/*ion-inline-start:"D:\developer_tools\workspace\wms_3.0_base_pda\src\pages\cougny\stock-frozen\stock-frozen.html"*/'<ion-header>\n\n\n\n  <ion-navbar color="wmscolor">\n\n    <ion-title>库存冻结</ion-title>\n\n    <!-- <ion-buttons end (click)="finishDetailed()">\n\n      <button ion-button icon-only>\n\n        <ion-icon name="menu"></ion-icon>\n\n      </button>\n\n    </ion-buttons> -->\n\n  </ion-navbar>\n\n  <ion-grid style="color: white;">\n\n    <ion-row>\n\n      <ion-col>\n\n        <ion-searchbar [(ngModel)]="searchQuery" (keyup)="keyEnter($event)" placeholder="请扫描库位|物料">\n\n          <ion-icon class="iconfont icon-saomiao"></ion-icon>\n\n        </ion-searchbar>\n\n      </ion-col>\n\n    </ion-row>\n\n    <ion-row>\n\n      <ion-col>\n\n        <div class="nav-tile">\n\n          <crumbs></crumbs>\n\n        </div>\n\n      </ion-col>\n\n      <ion-col text-right col-1 class="col-more">\n\n          <div (click)="presentPopover($event)"> \n\n            <ion-icon ios="md-more" md="md-more"></ion-icon>\n\n        </div>\n\n      </ion-col>\n\n    </ion-row>\n\n  </ion-grid>\n\n</ion-header>\n\n<ion-content>\n\n  <ion-list>\n\n    <ion-item *ngFor="let stockVO of stockVOList">\n\n      <div class="item-box">\n\n          <div class="left">\n\n            <ion-item>\n\n              <ion-checkbox  [(ngModel)]="stockVO.CheckStock" ></ion-checkbox>\n\n            </ion-item>\n\n          </div>\n\n          <div class="right">\n\n              <div class="left">\n\n                <span>{{stockVO.sku.skuName}}</span>\n\n                <span>规格：{{stockVO.skuPackageVO.spec}}</span>\n\n                <span>容器：{{stockVO.lpnCode}}</span>\n\n                <span *ngIf="paramValue==0">容器：{{stockVO.lpnCode}}</span>\n\n                <div *ngFor="let skuLot of range(stockVO)">\n\n                  <span>{{skuLot}}</span>\n\n                </div>\n\n              </div>\n\n              <div  class="right">\n\n                <span>{{stockVO.warehouse.whName}}</span>\n\n                <span>总  量:{{stockVO.stockQty}}</span>\n\n                <span>可用量：{{stockVO.availableQty}}</span>\n\n                <span>计量单位：{{stockVO.wsuName}}</span>\n\n                <button ion-button (click)="singleClick(stockVO)"\n\n                  [ngClass]="{\'frozen-off\':stockVO.stockStatus==0,\'frozen-on\':stockVO.stockStatus==1}" >\n\n                  {{stockVO.stockStatus==0?\'冻结\':\'解冻\'}}\n\n                </button>\n\n              </div>\n\n          </div>\n\n      </div>\n\n    </ion-item>\n\n  </ion-list>\n\n</ion-content>\n\n<ion-footer>\n\n  <div class="btn-box">\n\n    <button ion-button class="btn-l" (click)="multClick(1)">冻结</button>\n\n    <button ion-button class="btn-d" (click)="multClick(0)">解冻</button>\n\n  </div>\n\n</ion-footer>'/*ion-inline-end:"D:\developer_tools\workspace\wms_3.0_base_pda\src\pages\cougny\stock-frozen\stock-frozen.html"*/,
        }),
        __metadata("design:paramtypes", [__WEBPACK_IMPORTED_MODULE_1_ionic_angular__["l" /* NavController */],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["m" /* NavParams */],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["p" /* PopoverController */],
            __WEBPACK_IMPORTED_MODULE_8__ionic_storage__["b" /* Storage */],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["j" /* ModalController */],
            __WEBPACK_IMPORTED_MODULE_3__services_App_service__["a" /* AppService */],
            __WEBPACK_IMPORTED_MODULE_5__services_NativeService__["a" /* NativeService */],
            __WEBPACK_IMPORTED_MODULE_6__services_BarCodeService__["a" /* BarcodeService */],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["c" /* Events */],
            __WEBPACK_IMPORTED_MODULE_0__angular_core__["NgZone"]])
    ], StockFrozenPage);
    return StockFrozenPage;
}());

//# sourceMappingURL=stock-frozen.js.map

/***/ }),

/***/ 720:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "StockFrozenPageModule", function() { return StockFrozenPageModule; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(43);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__stock_frozen__ = __webpack_require__(1019);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__components_crumbs_crumbs_module__ = __webpack_require__(781);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};




var StockFrozenPageModule = /** @class */ (function () {
    function StockFrozenPageModule() {
    }
    StockFrozenPageModule = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["NgModule"])({
            declarations: [
                __WEBPACK_IMPORTED_MODULE_2__stock_frozen__["a" /* StockFrozenPage */],
            ],
            imports: [
                __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["g" /* IonicPageModule */].forChild(__WEBPACK_IMPORTED_MODULE_2__stock_frozen__["a" /* StockFrozenPage */]),
                __WEBPACK_IMPORTED_MODULE_3__components_crumbs_crumbs_module__["a" /* CrumbsModule */]
            ],
            schemas: [__WEBPACK_IMPORTED_MODULE_0__angular_core__["NO_ERRORS_SCHEMA"]]
        })
    ], StockFrozenPageModule);
    return StockFrozenPageModule;
}());

//# sourceMappingURL=stock-frozen.module.js.map

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
//# sourceMappingURL=53.js.map