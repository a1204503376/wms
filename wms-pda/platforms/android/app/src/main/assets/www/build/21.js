webpackJsonp([21],{

/***/ 1367:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return BindOrderPage; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(43);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__services_Utils__ = __webpack_require__(89);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__services_NativeService__ = __webpack_require__(64);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__services_BarCodeService__ = __webpack_require__(359);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5__models_DataBase_Model__ = __webpack_require__(361);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_6__services_App_service__ = __webpack_require__(157);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_7__utils_appConstant__ = __webpack_require__(65);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_8__ionic_native_keyboard__ = __webpack_require__(160);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_9__ionic_storage__ = __webpack_require__(66);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_10__services_AppGlobal__ = __webpack_require__(360);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};











var BindOrderPage = /** @class */ (function () {
    function BindOrderPage(navCtrl, navParams, popoverCtrl, nativeService, barcodeService, appService, modalCtrl, alertCtrl, keyboard, storage, events, ngZone) {
        this.navCtrl = navCtrl;
        this.navParams = navParams;
        this.popoverCtrl = popoverCtrl;
        this.nativeService = nativeService;
        this.barcodeService = barcodeService;
        this.appService = appService;
        this.modalCtrl = modalCtrl;
        this.alertCtrl = alertCtrl;
        this.keyboard = keyboard;
        this.storage = storage;
        this.events = events;
        this.ngZone = ngZone;
        this.isShow = true;
        this.scanModel = new __WEBPACK_IMPORTED_MODULE_5__models_DataBase_Model__["a" /* ScanModel */](); //扫描实体
        this.scanModelRecords = []; //已扫描物品列表
        this.currentTotal = 0;
        this.isShowCurrentTotal = true;
    }
    BindOrderPage.prototype.ionViewWillEnter = function () {
        var _this = this;
        //事件注册（扫描结果接收）
        this.events.subscribe('barcode:scan', function (barcode, time) {
            var bt = _this.barcodeService.GetBarcodeType(barcode);
            if (bt == __WEBPACK_IMPORTED_MODULE_4__services_BarCodeService__["b" /* BarcodeType */].HuoWei) {
                _this.ngZone.run(function () {
                    _this.sourceLocCode = barcode;
                });
            }
            if (bt == __WEBPACK_IMPORTED_MODULE_4__services_BarCodeService__["b" /* BarcodeType */].BoxCode) {
                _this.ngZone.run(function () {
                    //条码解析
                    _this.barcodeService.ScanSkuBarcode(barcode).subscribe(function (val) {
                        if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isNotEmpty(val)) {
                            _this.addToList(val);
                        }
                    });
                });
            }
        });
    };
    BindOrderPage.prototype.ionViewWillLeave = function () {
        __WEBPACK_IMPORTED_MODULE_10__services_AppGlobal__["a" /* AppGlobal */].removeSubscribe(this);
    };
    BindOrderPage.prototype.presentPopover = function (myEvent) {
        var _this = this;
        var popover = this.popoverCtrl.create('PopoverPage', { show_item_3: true, show_item_4: false, show_item_5: true });
        popover.onDidDismiss(function (data) {
            if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isNotEmpty(data) && __WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isNotEmpty(_this.scanModelRecords)) {
                console.log(_this.scanModelRecords);
                if (data == 'allCheck') {
                    //全选
                    for (var index = 0; index < _this.scanModelRecords.length; index++) {
                        _this.scanModelRecords[index].isCheck = true;
                    }
                }
                else if (data == 'clearCheck') {
                    //取消全选
                    for (var index = 0; index < _this.scanModelRecords.length; index++) {
                        _this.scanModelRecords[index].isCheck = false;
                    }
                }
            }
        });
        popover.present({
            ev: myEvent
        });
    };
    BindOrderPage.prototype.skuStockIsCheckDel = function () {
        this.scanModelRecords = this.scanModelRecords.filter(function (x) { return x.isCheck === false; });
        this.execTotal();
    };
    BindOrderPage.prototype.removeItem = function (item) {
        this.scanModelRecords = this.scanModelRecords.filter(function (x) { return x != item; });
        this.execTotal();
    };
    BindOrderPage.prototype.changeQty = function () {
        if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(this.scanModel.qty)) {
            this.scanModel.qty = "0";
        }
        this.execTotal();
    };
    BindOrderPage.prototype.clearRecordsSelected = function () {
        for (var _i = 0, _a = this.scanModelRecords; _i < _a.length; _i++) {
            var item1 = _a[_i];
            item1['selected'] = false;
            if (item1.skucode != this.scanModel.skucode) {
                item1['show'] = false;
            }
            else {
                item1['show'] = true;
            }
        }
    };
    BindOrderPage.prototype.itemClick = function (item) {
        this.clearRecordsSelected();
        item['selected'] = true;
        this.scanModel = item;
    };
    BindOrderPage.prototype.exit = function () {
        this.navCtrl.pop();
    };
    BindOrderPage.prototype.ionViewDidLoad = function () {
        this.scrollStypeTrue();
    };
    /**
     * 折叠状态
     */
    BindOrderPage.prototype.onIsShowClick = function () {
        this.isShow = !this.isShow;
        if (this.isShow) {
            this.scrollStypeTrue();
        }
        else {
            this.scrollStypeFalse();
        }
    };
    /**
     * 扫码回车事件
     * @param event
     */
    BindOrderPage.prototype.scanModelChange = function (event) {
        var _this = this;
        if (event && event.keyCode == 13) {
            if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isNotEmpty(this.scanView)) {
                this.barcodeService.ScanSkuBarcode(this.scanView).subscribe(function (val) {
                    if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isNotEmpty(val)) {
                        _this.addToList(val);
                    }
                });
            }
        }
    };
    BindOrderPage.prototype.addToList = function (val) {
        this.scanModel = val;
        var skulots = [];
        for (var prop in val.skuLotModel) {
            if (prop.search('skulot') != -1 && val.skuLotModel[prop] != '') {
                skulots.push(val.skuLotModel[prop]);
            }
        }
        this.scanModel['skulots'] = skulots;
        this.scanModel['isCheck'] = false;
        this.scanModel['selected'] = true;
        this.scanModel['show'] = true;
        if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(this.scanModelRecords)) {
            this.scanModelRecords.push(this.scanModel);
        }
        else {
            this.clearRecordsSelected();
            for (var _i = 0, _a = this.scanModelRecords; _i < _a.length; _i++) {
                var item = _a[_i];
                if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isObjectValueEqual1(this.scanModel, item, ['skuname', 'skulots', 'selected', 'isCheck', 'qty', 'setLotModel', 'setLotValue'])) {
                    item.qty = Number.parseInt(item.qty) + Number.parseInt(this.scanModel.qty);
                    item['selected'] = true;
                    this.scanModel = item;
                }
                else {
                    if (!__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].contains(this.scanModelRecords, this.scanModel, ['skuname', 'skulots', 'selected', 'isCheck', 'qty', 'setLotModel', 'setLotValue'])) {
                        this.scanModelRecords.push(this.scanModel);
                        break;
                    }
                }
            }
        }
        this.execTotal();
    };
    BindOrderPage.prototype.execTotal = function () {
        this.currentTotal = 0;
        for (var _i = 0, _a = this.scanModelRecords; _i < _a.length; _i++) {
            var item1 = _a[_i];
            if (item1.skucode == this.scanModel.skucode) {
                this.currentTotal += Number.parseInt(item1.qty);
            }
        }
    };
    BindOrderPage.prototype.scrollStypeTrue = function () {
        // setTimeout(() => {
        //   var header= (document.getElementsByClassName("header")[0] as HTMLElement).offsetHeight;
        //   var head_lable= (document.getElementsByClassName("head_lable")[0] as HTMLElement).offsetHeight;
        //   var list_item_serial= (document.getElementsByClassName("list_item_serial")[0] as HTMLElement).offsetHeight;
        //   var footer= (document.getElementsByClassName("footer")[0] as HTMLElement).offsetHeight;
        //   this.scrollHeight=Number(document.body.clientHeight)-header-head_lable-list_item_serial-footer-10;
        // }, 600);
        this.scrollHeight = Number(document.body.clientHeight) - 323;
    };
    BindOrderPage.prototype.scrollStypeFalse = function () {
        // setTimeout(() => {
        //   var header= (document.getElementsByClassName("header")[0] as HTMLElement).offsetHeight;
        //   var list_item_serial= (document.getElementsByClassName("list_item_serial")[0] as HTMLElement).offsetHeight;
        //   var footer= (document.getElementsByClassName("footer")[0] as HTMLElement).offsetHeight;
        //   this.scrollHeight=Number(document.body.clientHeight)-header-list_item_serial-footer;
        // }, 600);
        this.scrollHeight = Number(document.body.clientHeight) - 173;
    };
    BindOrderPage.prototype.updateItem = function (item) {
    };
    BindOrderPage.prototype.btnOk = function () {
        if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(this.sourceLocCode)) {
            this.nativeService.showToast("库位不能为空！");
            return;
        }
        if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(this.scanModelRecords)) {
            this.nativeService.showToast("请扫描物品");
            return;
        }
        this.submitRandomCheck();
    };
    BindOrderPage.prototype.submitRandomCheck = function () {
        var _this = this;
        var items = [];
        for (var _i = 0, _a = this.scanModelRecords; _i < _a.length; _i++) {
            var item = _a[_i];
            var itemTemp = {
                qty: item.qty,
                skuCode: item.skucode,
                skuName: item.skuname
            };
            for (var prop in item.skuLotModel) {
                if (prop.search('skulot') != -1 && __WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isNotEmpty(item.skuLotModel[prop])) {
                    var index = prop.substring(prop.indexOf('t') + 1, prop.length);
                    itemTemp["skuLot" + index] = item.skuLotModel[prop];
                }
            }
            items.push(itemTemp);
        }
        var params = {
            locCode: this.sourceLocCode,
            items: items
        };
        console.log(params);
        this.nativeService.showLoading();
        this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_7__utils_appConstant__["b" /* Api */].StockCountPDA + __WEBPACK_IMPORTED_MODULE_7__utils_appConstant__["b" /* Api */].randomCheckSubmit, __WEBPACK_IMPORTED_MODULE_7__utils_appConstant__["f" /* Method */].Post, params, __WEBPACK_IMPORTED_MODULE_7__utils_appConstant__["e" /* ContenType */].Json, function (result) {
            _this.nativeService.hideLoading();
            if (result.code == 200) {
                _this.nativeService.showToast(result.msg);
                _this.clearModel();
            }
            else {
                _this.nativeService.showToast(result.msg);
            }
        });
    };
    BindOrderPage.prototype.clearModel = function () {
        this.scanView = '';
        this.sourceLocCode = '';
        this.scanModel = new __WEBPACK_IMPORTED_MODULE_5__models_DataBase_Model__["a" /* ScanModel */]();
        this.scanModel.qty = '';
        this.scanModel.skucode = '';
        this.scanModel.skuname = '';
        this.scanModelRecords = [];
    };
    BindOrderPage.prototype.scrollToTop = function () {
        setTimeout(function () {
            window.scrollTo(0, document.body.scrollTop + 1);
            document.body.scrollTop >= 1 && window.scrollTo(0, document.body.scrollTop - 1);
        }, 10);
    };
    BindOrderPage = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Component"])({
            selector: 'page-bind-order',template:/*ion-inline-start:"D:\developer_tools\workspace\wms_3.0_base_pda\src\pages\takeawayhome\taskway-mult\bind-order\bind-order.html"*/'<ion-header id="head" #deliveryhead>\n\n  <ion-navbar color="wmscolor">\n\n    <ion-title>\n\n      单据绑定\n\n    </ion-title>\n\n  </ion-navbar>\n\n  <div class="title-bt">\n\n    <div class="nav-tile">\n\n      <crumbs></crumbs>\n\n    </div>\n\n  </div>\n\n</ion-header>\n\n<ion-content overflow-scroll="true">\n\n  <div class="header-item-box" *ngIf="isShow">\n\n    <div class="item">\n\n      <div class="row">\n\n        <span>波次号：9999999</span>\n\n        <span>三号打印机</span>\n\n      </div>\n\n      <div class="row">\n\n        <span>订单数：5</span>\n\n        <span>分配量：27</span>\n\n      </div>\n\n      <div class="row">\n\n        <span>订单行：2</span>\n\n        <span>拣货量：2</span>\n\n      </div>\n\n    </div>\n\n  </div>\n\n  <div class="list_item_serial">\n\n    <div class="list_item_title">\n\n      <span>\n\n        订单数(<b>{{scanModelRecords.length}}</b>)\n\n      </span>\n\n      <span *ngIf="isShow; else elseBlock" (click)="onIsShowClick()" style="padding-right: 30px;">\n\n        <ion-icon name="md-arrow-dropup"></ion-icon>\n\n      </span>\n\n      <ng-template #elseBlock>\n\n        <span (click)="onIsShowClick()" style="padding-right: 30px;">\n\n          <ion-icon name="md-arrow-dropdown"></ion-icon>\n\n        </span>\n\n      </ng-template>\n\n      <span>\n\n        <button ion-button icon-only (click)="presentPopover($event)">\n\n          <ion-icon name="md-more"></ion-icon>\n\n        </button>\n\n      </span>\n\n    </div>\n\n  </div>\n\n\n\n  <ion-list class="list_item_div">\n\n    <ion-scroll scrollY="true" [ngStyle]="{\'height\':scrollHeight+\'px\'}">\n\n      <ion-item-sliding *ngFor="let item of [1,2,3];let i = index">\n\n        <ion-item button  >\n\n          <div class="item-box">\n\n            <div class="left">\n\n              <ion-item>\n\n                <ion-checkbox ></ion-checkbox>\n\n              </ion-item>\n\n            </div>\n\n            <div class="right">\n\n              <div class="row">\n\n                <span>SO00000000000001</span>\n\n                <div>\n\n                  <span class="tag">未</span>\n\n                  <span>未完成</span>\n\n                </div>\n\n              </div>\n\n              <div class="row">\n\n                <span>订单行数：1</span>\n\n                <span>分配量：20</span>\n\n              </div>\n\n              <div class="row">\n\n                <span>完成行数：0</span>\n\n                <span>拣货量：0</span>\n\n              </div>\n\n            </div>\n\n          </div>\n\n        </ion-item>\n\n        <ion-item-options side="right">\n\n          <button (tap)="printItem(item)" ion-button style="background-color: #00BFBF;">\n\n            打印\n\n          </button>\n\n        </ion-item-options>\n\n      </ion-item-sliding>\n\n    </ion-scroll>\n\n  </ion-list>\n\n\n\n</ion-content>\n\n<ion-footer>\n\n  <div class="btn-box">\n\n    <button no-dbl-click ion-button block class="btn-l" (tap)="exit()">\n\n      返回[Esc]\n\n    </button>\n\n  </div>\n\n</ion-footer>'/*ion-inline-end:"D:\developer_tools\workspace\wms_3.0_base_pda\src\pages\takeawayhome\taskway-mult\bind-order\bind-order.html"*/,
        }),
        __metadata("design:paramtypes", [__WEBPACK_IMPORTED_MODULE_1_ionic_angular__["l" /* NavController */],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["m" /* NavParams */],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["p" /* PopoverController */],
            __WEBPACK_IMPORTED_MODULE_3__services_NativeService__["a" /* NativeService */],
            __WEBPACK_IMPORTED_MODULE_4__services_BarCodeService__["a" /* BarcodeService */],
            __WEBPACK_IMPORTED_MODULE_6__services_App_service__["a" /* AppService */],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["j" /* ModalController */],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["a" /* AlertController */],
            __WEBPACK_IMPORTED_MODULE_8__ionic_native_keyboard__["a" /* Keyboard */],
            __WEBPACK_IMPORTED_MODULE_9__ionic_storage__["b" /* Storage */],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["c" /* Events */],
            __WEBPACK_IMPORTED_MODULE_0__angular_core__["NgZone"]])
    ], BindOrderPage);
    return BindOrderPage;
}());

//# sourceMappingURL=bind-order.js.map

/***/ }),

/***/ 772:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "BindOrderPageModule", function() { return BindOrderPageModule; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(43);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__bind_order__ = __webpack_require__(1367);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__components_crumbs_crumbs_module__ = __webpack_require__(781);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};




var BindOrderPageModule = /** @class */ (function () {
    function BindOrderPageModule() {
    }
    BindOrderPageModule = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["NgModule"])({
            declarations: [
                __WEBPACK_IMPORTED_MODULE_2__bind_order__["a" /* BindOrderPage */],
            ],
            imports: [
                __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["g" /* IonicPageModule */].forChild(__WEBPACK_IMPORTED_MODULE_2__bind_order__["a" /* BindOrderPage */]),
                __WEBPACK_IMPORTED_MODULE_3__components_crumbs_crumbs_module__["a" /* CrumbsModule */]
            ],
            schemas: [__WEBPACK_IMPORTED_MODULE_0__angular_core__["NO_ERRORS_SCHEMA"]]
        })
    ], BindOrderPageModule);
    return BindOrderPageModule;
}());

//# sourceMappingURL=bind-order.module.js.map

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
//# sourceMappingURL=21.js.map