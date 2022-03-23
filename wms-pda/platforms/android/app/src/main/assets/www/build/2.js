webpackJsonp([2,68],{

/***/ 1037:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return ReplenishmenPage; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(43);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__services_App_service__ = __webpack_require__(157);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__services_NativeService__ = __webpack_require__(64);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__services_BarCodeService__ = __webpack_require__(359);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};





var ReplenishmenPage = /** @class */ (function () {
    function ReplenishmenPage(navCtrl, navParams, modalController, appService, nativeService, barcodeService, events, ngZone) {
        this.navCtrl = navCtrl;
        this.navParams = navParams;
        this.modalController = modalController;
        this.appService = appService;
        this.nativeService = nativeService;
        this.barcodeService = barcodeService;
        this.events = events;
        this.ngZone = ngZone;
        this.IsShow = true;
        this.result = {}; //补货数据集
        this.selectModel = {}; //选中任务
        this.locList = [];
    }
    ReplenishmenPage.prototype.ionViewWillEnter = function () {
    };
    ReplenishmenPage.prototype.ionViewWillLeave = function () {
    };
    ReplenishmenPage.prototype.ionViewDidLoad = function () {
    };
    __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["ViewChild"])('labelTypecom'),
        __metadata("design:type", Object)
    ], ReplenishmenPage.prototype, "labelTypecom", void 0);
    ReplenishmenPage = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Component"])({
            selector: 'page-replenishmen',template:/*ion-inline-start:"D:\developer_tools\workspace\wms_3.0_base_pda\src\pages\replenishmen\replenishmen.html"*/'<ion-header>\n\n\n\n  <ion-navbar color="wmscolor">\n\n    <ion-title>补货任务</ion-title>\n\n    <ion-buttons end (click)="finishDetailed()">\n\n      <button ion-button icon-only>\n\n        <ion-icon name="menu"></ion-icon>\n\n      </button>\n\n    </ion-buttons>\n\n  </ion-navbar>\n\n  <ion-grid style="color: white;">\n\n    <ion-row>\n\n      <ion-col>\n\n        <div class="nav-tile">\n\n          <crumbs></crumbs>\n\n        </div>\n\n      </ion-col>\n\n      <ion-col text-right col-4>\n\n        任务进度:0/30\n\n      </ion-col>\n\n    </ion-row>\n\n  </ion-grid>\n\n</ion-header>\n\n<ion-content>\n\n  <ion-grid>\n\n    <ion-row class="body-item-box-1">\n\n      <ion-col>\n\n        <div class="header-item-box">\n\n          <div class="left">\n\n            <div class="row1">\n\n              <div class="left">\n\n                <span>双十一大促 哇哈哈 限量版包装</span>\n\n                <span>SKU0000000000000001</span>\n\n              </div>\n\n              <div class="right">\n\n                <span>物品规格:1-10</span>\n\n              </div>\n\n            </div>\n\n            <div class="row2">\n\n              <div class="left">\n\n                <span>批属性01</span>\n\n                <span>批属性02</span>\n\n              </div>\n\n              <div class="right">\n\n                <span>补货数量：20/100  台</span>\n\n              </div>\n\n            </div>\n\n          </div>\n\n          <div class="right">\n\n            <ion-icon name="arrow-forward"></ion-icon>\n\n          </div>\n\n        </div>\n\n      </ion-col>\n\n    </ion-row>\n\n    <ion-row class="body-item-box-2">\n\n      <ion-col>\n\n        <ion-item>\n\n          <ion-label>原库位：</ion-label>\n\n          <ion-input  maxlength=\'30\' type="text" [(ngModel)]="selectModel.sourceLocCode"></ion-input>\n\n          <ion-avatar item-end>\n\n            <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n          </ion-avatar>\n\n        </ion-item>\n\n          <ion-item>\n\n            <ion-label>箱&nbsp;&nbsp;&nbsp;&nbsp;码：</ion-label>\n\n            <ion-input (keyup)="scanModelChange($event)" type="text" [(ngModel)]="scanView"></ion-input>\n\n            <ion-avatar item-end>\n\n              <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n            </ion-avatar>\n\n          </ion-item>\n\n          <page-check-in-box-label-type [asnBillNo]=\'asnBillNo\' [ruleCode]=\'ruleCode\' (outer)="runParent($event)" #labelTypecom>\n\n          </page-check-in-box-label-type>\n\n      </ion-col>\n\n    </ion-row>\n\n  </ion-grid>\n\n</ion-content>\n\n<ion-footer>\n\n  <div class="btn-box">\n\n    <button ion-button class="btn-l"> 返回[Esc]</button>\n\n    <button ion-button class="btn-d"> {{IsShow?\'下一步\':\'确定\'}}[Ent]</button>\n\n  </div>\n\n</ion-footer>'/*ion-inline-end:"D:\developer_tools\workspace\wms_3.0_base_pda\src\pages\replenishmen\replenishmen.html"*/,
        }),
        __metadata("design:paramtypes", [__WEBPACK_IMPORTED_MODULE_1_ionic_angular__["l" /* NavController */],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["m" /* NavParams */],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["j" /* ModalController */],
            __WEBPACK_IMPORTED_MODULE_2__services_App_service__["a" /* AppService */],
            __WEBPACK_IMPORTED_MODULE_3__services_NativeService__["a" /* NativeService */],
            __WEBPACK_IMPORTED_MODULE_4__services_BarCodeService__["a" /* BarcodeService */],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["c" /* Events */],
            __WEBPACK_IMPORTED_MODULE_0__angular_core__["NgZone"]])
    ], ReplenishmenPage);
    return ReplenishmenPage;
}());

//# sourceMappingURL=replenishmen.js.map

/***/ }),

/***/ 701:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "CheckInBoxLabelTypePageModule", function() { return CheckInBoxLabelTypePageModule; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(43);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__check_in_box_label_type__ = __webpack_require__(792);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};



var CheckInBoxLabelTypePageModule = /** @class */ (function () {
    function CheckInBoxLabelTypePageModule() {
    }
    CheckInBoxLabelTypePageModule = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["NgModule"])({
            declarations: [
                __WEBPACK_IMPORTED_MODULE_2__check_in_box_label_type__["a" /* CheckInBoxLabelTypePage */],
            ],
            imports: [
                __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["g" /* IonicPageModule */].forChild(__WEBPACK_IMPORTED_MODULE_2__check_in_box_label_type__["a" /* CheckInBoxLabelTypePage */]),
            ],
            exports: [
                __WEBPACK_IMPORTED_MODULE_2__check_in_box_label_type__["a" /* CheckInBoxLabelTypePage */]
            ]
        })
    ], CheckInBoxLabelTypePageModule);
    return CheckInBoxLabelTypePageModule;
}());

//# sourceMappingURL=check-in-box-label-type.module.js.map

/***/ }),

/***/ 737:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "ReplenishmenPageModule", function() { return ReplenishmenPageModule; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(43);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__replenishmen__ = __webpack_require__(1037);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__shared_check_in_box_label_type_check_in_box_label_type_module__ = __webpack_require__(701);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4_primeng_dropdown__ = __webpack_require__(822);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4_primeng_dropdown___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_4_primeng_dropdown__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5_primeng_calendar__ = __webpack_require__(810);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5_primeng_calendar___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_5_primeng_calendar__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_6__components_crumbs_crumbs_module__ = __webpack_require__(781);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};







var ReplenishmenPageModule = /** @class */ (function () {
    function ReplenishmenPageModule() {
    }
    ReplenishmenPageModule = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["NgModule"])({
            declarations: [
                __WEBPACK_IMPORTED_MODULE_2__replenishmen__["a" /* ReplenishmenPage */],
            ],
            imports: [
                __WEBPACK_IMPORTED_MODULE_3__shared_check_in_box_label_type_check_in_box_label_type_module__["CheckInBoxLabelTypePageModule"],
                __WEBPACK_IMPORTED_MODULE_4_primeng_dropdown__["DropdownModule"],
                __WEBPACK_IMPORTED_MODULE_5_primeng_calendar__["CalendarModule"],
                __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["g" /* IonicPageModule */].forChild(__WEBPACK_IMPORTED_MODULE_2__replenishmen__["a" /* ReplenishmenPage */]),
                __WEBPACK_IMPORTED_MODULE_6__components_crumbs_crumbs_module__["a" /* CrumbsModule */]
            ],
            schemas: [__WEBPACK_IMPORTED_MODULE_0__angular_core__["NO_ERRORS_SCHEMA"]]
        })
    ], ReplenishmenPageModule);
    return ReplenishmenPageModule;
}());

//# sourceMappingURL=replenishmen.module.js.map

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

/***/ }),

/***/ 792:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return CheckInBoxLabelTypePage; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(43);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__utils_appConstant__ = __webpack_require__(65);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__services_Utils__ = __webpack_require__(89);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__services_NativeService__ = __webpack_require__(64);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5__services_App_service__ = __webpack_require__(157);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_6__models_SkuLotModel__ = __webpack_require__(158);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_7__models_DataBase_Model__ = __webpack_require__(361);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_8__services_BarCodeService__ = __webpack_require__(359);
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
 * Generated class for the CheckInBoxLabelTypePage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */
var CheckInBoxLabelTypePage = /** @class */ (function () {
    function CheckInBoxLabelTypePage(navCtrl, navParams, nativeService, appService, barcodeService) {
        this.navCtrl = navCtrl;
        this.navParams = navParams;
        this.nativeService = nativeService;
        this.appService = appService;
        this.barcodeService = barcodeService;
        this.skuLotModel = new __WEBPACK_IMPORTED_MODULE_6__models_SkuLotModel__["a" /* SkuLotModel */](); //批属性对象
        this.ComponentFlag = true; //组件显示
        this.EidtFlag = true; //是否可编辑
        this.qtyDisabled = true; //数量是否可编辑
        this.locDisabled = false; //库位是否可编辑
        this.locCode = 'STAGE'; //库位编码
        this.locCodeFlag = true; //库位显隐
        this.scanModel = new __WEBPACK_IMPORTED_MODULE_7__models_DataBase_Model__["a" /* ScanModel */](); //箱码解析实体
        this.isCopyLabel = false;
        this.outer = new __WEBPACK_IMPORTED_MODULE_0__angular_core__["EventEmitter"]();
        this.labelType = __WEBPACK_IMPORTED_MODULE_2__utils_appConstant__["c" /* AppConstant */].labelType;
        console.log('constructor CheckInBoxLabelTypePage');
    }
    CheckInBoxLabelTypePage.prototype.ionViewDidLoad = function () {
        console.log('子组件---ionViewDidLoad---CheckInBoxLabelTypePage');
    };
    CheckInBoxLabelTypePage.prototype.ionViewWillLeave = function () {
        console.log('子组件---ionViewWillLeave---CheckInBoxLabelTypePage');
    };
    /**
     * 初始化数据
     * @param scan
     */
    CheckInBoxLabelTypePage.prototype.changeRun = function (scan) {
        //解析条码规则
        if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isNotEmpty(scan)) {
            this.ComponentFlag = true;
            this.scanModel = scan;
            this.outer.emit('');
        }
    };
    /**
     * 数据初始化
     */
    CheckInBoxLabelTypePage.prototype.clearModel = function () {
        this.skuCode = '';
        this.scanQty = 0;
        this.wsuName = '';
        this.instockDate = null;
        this.produceDate = null;
        this.typeCode = '';
        this.lotNumber = '';
        this.version = '';
        this.remarks = '';
        this.type = '';
        this.labelCode = '';
        this.locCode = 'STAGE';
        this.skuLotModel = new __WEBPACK_IMPORTED_MODULE_6__models_SkuLotModel__["a" /* SkuLotModel */]();
        this.ComponentFlag = false;
    };
    __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Output"])(),
        __metadata("design:type", Object)
    ], CheckInBoxLabelTypePage.prototype, "outer", void 0);
    __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Input"])(),
        __metadata("design:type", Object)
    ], CheckInBoxLabelTypePage.prototype, "ruleCode", void 0);
    __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Input"])(),
        __metadata("design:type", String)
    ], CheckInBoxLabelTypePage.prototype, "asnBillNo", void 0);
    CheckInBoxLabelTypePage = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Component"])({
            selector: 'page-check-in-box-label-type',template:/*ion-inline-start:"D:\developer_tools\workspace\wms_3.0_base_pda\src\shared\check-in-box-label-type\check-in-box-label-type.html"*/'<div *ngIf="ComponentFlag">\n\n  <ion-item *ngIf="locCodeFlag">\n\n    <ion-label>库位编码：</ion-label>\n\n    <ion-input [disabled]=\'locDisabled\' maxlength=\'30\' type="text" [(ngModel)]="locCode">\n\n    </ion-input>\n\n  </ion-item>\n\n  <div class="list_item">\n\n    <ion-item *ngIf="skuCodeFlag">\n\n      <ion-label>物品编码：</ion-label>\n\n      <ion-input [disabled]=\'EidtFlag\' maxlength=\'30\' type="text" [(ngModel)]="scanModel.skucode"></ion-input>\n\n    </ion-item>\n\n  </div>\n\n  <ion-item *ngIf="skuNameFlag">\n\n    <ion-label>物品名称：</ion-label>\n\n    <ion-input [disabled]=\'EidtFlag\' maxlength=\'30\' type="text" [(ngModel)]="scanModel.skuname">\n\n    </ion-input>\n\n  </ion-item>\n\n  <ion-item>\n\n    <ion-label>数量：</ion-label>\n\n    <ion-input [disabled]=\'qtyDisabled\' maxlength=\'30\' type="number" [(ngModel)]="scanModel.qty">\n\n    </ion-input>\n\n    <ion-select [disabled]=\'EidtFlag\' [(ngModel)]="scanModel.um" cancelText="取消">\n\n      <ion-option [value]="scanModel.um">{{scanModel.um}}</ion-option>\n\n    </ion-select>\n\n  </ion-item>\n\n\n\n  <ion-item *ngIf="!isCopyLabel&&scanModel.skuLotModel.skuLotName1!=null &&scanModel.skuLotModel.skulot1!=\'\' ">\n\n    <ion-label>{{scanModel.skuLotModel.skuLotName1}}：</ion-label>\n\n    <ion-input [disabled]=\'EidtFlag\' maxlength=\'30\' type="text" [(ngModel)]="scanModel.skuLotModel.skulot1">\n\n    </ion-input>\n\n  </ion-item>\n\n\n\n  <ion-item *ngIf="!isCopyLabel&&scanModel.skuLotModel.skuLotName2!=null &&scanModel.skuLotModel.skulot2!=\'\'">\n\n    <ion-label>{{scanModel.skuLotModel.skuLotName2}}：</ion-label>\n\n    <ion-input [disabled]=\'EidtFlag\' maxlength=\'30\' type="text" [(ngModel)]="scanModel.skuLotModel.skulot2">\n\n    </ion-input>\n\n  </ion-item>\n\n\n\n  <ion-item *ngIf="!isCopyLabel&&scanModel.skuLotModel.skuLotName3!=null &&scanModel.skuLotModel.skulot3!=\'\'">\n\n    <ion-label>{{scanModel.skuLotModel.skuLotName3}}：</ion-label>\n\n    <ion-input [disabled]=\'EidtFlag\' maxlength=\'30\' type="text" [(ngModel)]="scanModel.skuLotModel.skulot3">\n\n    </ion-input>\n\n  </ion-item>\n\n\n\n  <ion-item *ngIf="!isCopyLabel&&scanModel.skuLotModel.skuLotName4!=null &&scanModel.skuLotModel.skulot4!=\'\'">\n\n    <ion-label>{{scanModel.skuLotModel.skuLotName4}}：</ion-label>\n\n    <ion-input [disabled]=\'EidtFlag\' maxlength=\'30\' type="text" [(ngModel)]="scanModel.skuLotModel.skulot4">\n\n    </ion-input>\n\n  </ion-item>\n\n\n\n  <ion-item *ngIf="!isCopyLabel&&scanModel.skuLotModel.skuLotName5!=null&&scanModel.skuLotModel.skuLotName5!=\'\' &&scanModel.skuLotModel.skulot5!=\'\'">\n\n    <ion-label>{{scanModel.skuLotModel.skuLotName5}}：</ion-label>\n\n    <ion-input [disabled]=\'EidtFlag\' maxlength=\'30\' type="text" [(ngModel)]="scanModel.skuLotModel.skulot5">\n\n    </ion-input>\n\n  </ion-item>\n\n\n\n  <ion-item *ngIf="!isCopyLabel&&scanModel.skuLotModel.skuLotName6!=null &&scanModel.skuLotModel.skulot6!=\'\' ">\n\n    <ion-label>{{scanModel.skuLotModel.skuLotName6}}：</ion-label>\n\n    <ion-input [disabled]=\'EidtFlag\' maxlength=\'30\' type="text" [(ngModel)]="scanModel.skuLotModel.skulot6">\n\n    </ion-input>\n\n  </ion-item>\n\n\n\n  <ion-item *ngIf="!isCopyLabel&&scanModel.skuLotModel.skuLotName7!=null &&scanModel.skuLotModel.skulot7!=\'\'">\n\n    <ion-label>{{scanModel.skuLotModel.skuLotName7}}：</ion-label>\n\n    <ion-input [disabled]=\'EidtFlag\' maxlength=\'30\' type="text" [(ngModel)]="scanModel.skuLotModel.skulot7">\n\n    </ion-input>\n\n  </ion-item>\n\n\n\n  \n\n  <ion-item *ngIf="!isCopyLabel&&scanModel.skuLotModel.skuLotName8!=null &&scanModel.skuLotModel.skulot8!=\'\'">\n\n    <ion-label>{{scanModel.skuLotModel.skuLotNam8}}：</ion-label>\n\n    <ion-input [disabled]=\'EidtFlag\' maxlength=\'30\' type="text" [(ngModel)]="scanModel.skuLotModel.skulot8">\n\n    </ion-input>\n\n  </ion-item>\n\n\n\n  \n\n  <ion-item *ngIf="!isCopyLabel&&scanModel.skuLotModel.skuLotName9!=null &&scanModel.skuLotModel.skulot9!=\'\'">\n\n    <ion-label>{{scanModel.skuLotModel.skuLotName9}}：</ion-label>\n\n    <ion-input [disabled]=\'EidtFlag\' maxlength=\'30\' type="text" [(ngModel)]="scanModel.skuLotModel.skulot9">\n\n    </ion-input>\n\n  </ion-item>\n\n\n\n  \n\n  <ion-item *ngIf="!isCopyLabel&&scanModel.skuLotModel.skuLotName10!=null &&scanModel.skuLotModel.skulot10!=\'\'">\n\n    <ion-label>{{scanModel.skuLotModel.skuLotName10}}：</ion-label>\n\n    <ion-input [disabled]=\'EidtFlag\' maxlength=\'30\' type="text" [(ngModel)]="scanModel.skuLotModel.skulot10">\n\n    </ion-input>\n\n  </ion-item>\n\n\n\n\n\n\n\n  <ion-item *ngIf="isCopyLabel&&scanModel.skuLotModel.skuLotName1!=null &&scanModel.skuLotModel.skuLotName1!=\'\'">\n\n    <ion-label>{{scanModel.skuLotModel.skuLotName1}}：</ion-label>\n\n    <ion-input [disabled]=\'EidtFlag\' maxlength=\'30\' type="text" [(ngModel)]="scanModel.skuLotModel.skulot1">\n\n    </ion-input>\n\n  </ion-item>\n\n\n\n  <ion-item *ngIf="isCopyLabel&&scanModel.skuLotModel.skuLotName2!=null &&scanModel.skuLotModel.skuLotName2!=\'\'">\n\n    <ion-label>{{scanModel.skuLotModel.skuLotName2}}：</ion-label>\n\n    <ion-input [disabled]=\'EidtFlag\' maxlength=\'30\' type="text" [(ngModel)]="scanModel.skuLotModel.skulot2">\n\n    </ion-input>\n\n  </ion-item>\n\n\n\n  <ion-item *ngIf="isCopyLabel&&scanModel.skuLotModel.skuLotName3!=null &&scanModel.skuLotModel.skuLotName3!=\'\'">\n\n    <ion-label>{{scanModel.skuLotModel.skuLotName3}}：</ion-label>\n\n    <ion-input [disabled]=\'EidtFlag\' maxlength=\'30\' type="text" [(ngModel)]="scanModel.skuLotModel.skulot3">\n\n    </ion-input>\n\n  </ion-item>\n\n\n\n  <ion-item *ngIf="isCopyLabel&&scanModel.skuLotModel.skuLotName4!=null &&scanModel.skuLotModel.skuLotName4!=\'\'">\n\n    <ion-label>{{scanModel.skuLotModel.skuLotName4}}：</ion-label>\n\n    <ion-input [disabled]=\'EidtFlag\' maxlength=\'30\' type="text" [(ngModel)]="scanModel.skuLotModel.skulot4">\n\n    </ion-input>\n\n  </ion-item>\n\n\n\n  <ion-item *ngIf="isCopyLabel&&scanModel.skuLotModel.skuLotName5!=null &&scanModel.skuLotModel.skuLotName5!=\'\'">\n\n    <ion-label>{{scanModel.skuLotModel.skuLotName5}}：</ion-label>\n\n    <ion-input [disabled]=\'EidtFlag\' maxlength=\'30\' type="text" [(ngModel)]="scanModel.skuLotModel.skulot5">\n\n    </ion-input>\n\n  </ion-item>\n\n\n\n  <ion-item *ngIf="isCopyLabel&&scanModel.skuLotModel.skuLotName6!=null &&scanModel.skuLotModel.skuLotName6!=\'\' ">\n\n    <ion-label>{{scanModel.skuLotModel.skuLotName6}}：</ion-label>\n\n    <ion-input [disabled]=\'EidtFlag\' maxlength=\'30\' type="text" [(ngModel)]="scanModel.skuLotModel.skulot6">\n\n    </ion-input>\n\n  </ion-item>\n\n\n\n  <ion-item *ngIf="isCopyLabel&&scanModel.skuLotModel.skuLotName7!=null &&scanModel.skuLotModel.skuLotName7!=\'\'">\n\n    <ion-label>{{scanModel.skuLotModel.skuLotName7}}：</ion-label>\n\n    <ion-input [disabled]=\'EidtFlag\' maxlength=\'30\' type="text" [(ngModel)]="scanModel.skuLotModel.skulot7">\n\n    </ion-input>\n\n  </ion-item>\n\n\n\n  \n\n  <ion-item *ngIf="isCopyLabel&&scanModel.skuLotModel.skuLotName8!=null &&scanModel.skuLotModel.skuLotName8!=\'\'">\n\n    <ion-label>{{scanModel.skuLotModel.skuLotNam8}}：</ion-label>\n\n    <ion-input [disabled]=\'EidtFlag\' maxlength=\'30\' type="text" [(ngModel)]="scanModel.skuLotModel.skulot8">\n\n    </ion-input>\n\n  </ion-item>\n\n\n\n  \n\n  <ion-item *ngIf="isCopyLabel&&scanModel.skuLotModel.skuLotName9!=null &&scanModel.skuLotModel.skuLotName9!=\'\'">\n\n    <ion-label>{{scanModel.skuLotModel.skuLotName9}}：</ion-label>\n\n    <ion-input [disabled]=\'EidtFlag\' maxlength=\'30\' type="text" [(ngModel)]="scanModel.skuLotModel.skulot9">\n\n    </ion-input>\n\n  </ion-item>\n\n\n\n  \n\n  <ion-item *ngIf="isCopyLabel&&scanModel.skuLotModel.skuLotName10!=null &&scanModel.skuLotModel.skuLotName10!=\'\'">\n\n    <ion-label>{{scanModel.skuLotModel.skuLotName10}}：</ion-label>\n\n    <ion-input [disabled]=\'EidtFlag\' maxlength=\'30\' type="text" [(ngModel)]="scanModel.skuLotModel.skulot10">\n\n    </ion-input>\n\n  </ion-item>\n\n</div>'/*ion-inline-end:"D:\developer_tools\workspace\wms_3.0_base_pda\src\shared\check-in-box-label-type\check-in-box-label-type.html"*/,
        }),
        __metadata("design:paramtypes", [__WEBPACK_IMPORTED_MODULE_1_ionic_angular__["l" /* NavController */],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["m" /* NavParams */],
            __WEBPACK_IMPORTED_MODULE_4__services_NativeService__["a" /* NativeService */],
            __WEBPACK_IMPORTED_MODULE_5__services_App_service__["a" /* AppService */],
            __WEBPACK_IMPORTED_MODULE_8__services_BarCodeService__["a" /* BarcodeService */]])
    ], CheckInBoxLabelTypePage);
    return CheckInBoxLabelTypePage;
}());

//# sourceMappingURL=check-in-box-label-type.js.map

/***/ }),

/***/ 798:
/***/ (function(module, exports, __webpack_require__) {

"use strict";

var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
Object.defineProperty(exports, "__esModule", { value: true });
var core_1 = __webpack_require__(1);
var DomHandler = /** @class */ (function () {
    function DomHandler() {
        this.calculatedScrollbarWidth = null;
    }
    DomHandler.prototype.addClass = function (element, className) {
        if (element.classList)
            element.classList.add(className);
        else
            element.className += ' ' + className;
    };
    DomHandler.prototype.addMultipleClasses = function (element, className) {
        if (element.classList) {
            var styles = className.split(' ');
            for (var i = 0; i < styles.length; i++) {
                element.classList.add(styles[i]);
            }
        }
        else {
            var styles = className.split(' ');
            for (var i = 0; i < styles.length; i++) {
                element.className += ' ' + styles[i];
            }
        }
    };
    DomHandler.prototype.removeClass = function (element, className) {
        if (element.classList)
            element.classList.remove(className);
        else
            element.className = element.className.replace(new RegExp('(^|\\b)' + className.split(' ').join('|') + '(\\b|$)', 'gi'), ' ');
    };
    DomHandler.prototype.hasClass = function (element, className) {
        if (element.classList)
            return element.classList.contains(className);
        else
            return new RegExp('(^| )' + className + '( |$)', 'gi').test(element.className);
    };
    DomHandler.prototype.siblings = function (element) {
        return Array.prototype.filter.call(element.parentNode.children, function (child) {
            return child !== element;
        });
    };
    DomHandler.prototype.find = function (element, selector) {
        return Array.from(element.querySelectorAll(selector));
    };
    DomHandler.prototype.findSingle = function (element, selector) {
        return element.querySelector(selector);
    };
    DomHandler.prototype.index = function (element) {
        var children = element.parentNode.childNodes;
        var num = 0;
        for (var i = 0; i < children.length; i++) {
            if (children[i] == element)
                return num;
            if (children[i].nodeType == 1)
                num++;
        }
        return -1;
    };
    DomHandler.prototype.indexWithinGroup = function (element, attributeName) {
        var children = element.parentNode.childNodes;
        var num = 0;
        for (var i = 0; i < children.length; i++) {
            if (children[i] == element)
                return num;
            if (children[i].attributes && children[i].attributes[attributeName] && children[i].nodeType == 1)
                num++;
        }
        return -1;
    };
    DomHandler.prototype.relativePosition = function (element, target) {
        var elementDimensions = element.offsetParent ? { width: element.offsetWidth, height: element.offsetHeight } : this.getHiddenElementDimensions(element);
        var targetHeight = target.offsetHeight;
        var targetWidth = target.offsetWidth;
        var targetOffset = target.getBoundingClientRect();
        var windowScrollTop = this.getWindowScrollTop();
        var viewport = this.getViewport();
        var top, left;
        if ((targetOffset.top + targetHeight + elementDimensions.height) > viewport.height) {
            top = -1 * (elementDimensions.height);
            if (targetOffset.top + top < 0) {
                top = 0;
            }
        }
        else {
            top = targetHeight;
        }
        if ((targetOffset.left + elementDimensions.width) > viewport.width)
            left = targetWidth - elementDimensions.width;
        else
            left = 0;
        element.style.top = top + 'px';
        element.style.left = left + 'px';
    };
    DomHandler.prototype.absolutePosition = function (element, target) {
        var elementDimensions = element.offsetParent ? { width: element.offsetWidth, height: element.offsetHeight } : this.getHiddenElementDimensions(element);
        var elementOuterHeight = elementDimensions.height;
        var elementOuterWidth = elementDimensions.width;
        var targetOuterHeight = target.offsetHeight;
        var targetOuterWidth = target.offsetWidth;
        var targetOffset = target.getBoundingClientRect();
        var windowScrollTop = this.getWindowScrollTop();
        var windowScrollLeft = this.getWindowScrollLeft();
        var viewport = this.getViewport();
        var top, left;
        if (targetOffset.top + targetOuterHeight + elementOuterHeight > viewport.height) {
            top = targetOffset.top + windowScrollTop - elementOuterHeight;
            if (top < 0) {
                top = 0 + windowScrollTop;
            }
        }
        else {
            top = targetOuterHeight + targetOffset.top + windowScrollTop;
        }
        if (targetOffset.left + targetOuterWidth + elementOuterWidth > viewport.width)
            left = targetOffset.left + windowScrollLeft + targetOuterWidth - elementOuterWidth;
        else
            left = targetOffset.left + windowScrollLeft;
        element.style.top = top + 'px';
        element.style.left = left + 'px';
    };
    DomHandler.prototype.getHiddenElementOuterHeight = function (element) {
        element.style.visibility = 'hidden';
        element.style.display = 'block';
        var elementHeight = element.offsetHeight;
        element.style.display = 'none';
        element.style.visibility = 'visible';
        return elementHeight;
    };
    DomHandler.prototype.getHiddenElementOuterWidth = function (element) {
        element.style.visibility = 'hidden';
        element.style.display = 'block';
        var elementWidth = element.offsetWidth;
        element.style.display = 'none';
        element.style.visibility = 'visible';
        return elementWidth;
    };
    DomHandler.prototype.getHiddenElementDimensions = function (element) {
        var dimensions = {};
        element.style.visibility = 'hidden';
        element.style.display = 'block';
        dimensions.width = element.offsetWidth;
        dimensions.height = element.offsetHeight;
        element.style.display = 'none';
        element.style.visibility = 'visible';
        return dimensions;
    };
    DomHandler.prototype.scrollInView = function (container, item) {
        var borderTopValue = getComputedStyle(container).getPropertyValue('borderTopWidth');
        var borderTop = borderTopValue ? parseFloat(borderTopValue) : 0;
        var paddingTopValue = getComputedStyle(container).getPropertyValue('paddingTop');
        var paddingTop = paddingTopValue ? parseFloat(paddingTopValue) : 0;
        var containerRect = container.getBoundingClientRect();
        var itemRect = item.getBoundingClientRect();
        var offset = (itemRect.top + document.body.scrollTop) - (containerRect.top + document.body.scrollTop) - borderTop - paddingTop;
        var scroll = container.scrollTop;
        var elementHeight = container.clientHeight;
        var itemHeight = this.getOuterHeight(item);
        if (offset < 0) {
            container.scrollTop = scroll + offset;
        }
        else if ((offset + itemHeight) > elementHeight) {
            container.scrollTop = scroll + offset - elementHeight + itemHeight;
        }
    };
    DomHandler.prototype.fadeIn = function (element, duration) {
        element.style.opacity = 0;
        var last = +new Date();
        var opacity = 0;
        var tick = function () {
            opacity = +element.style.opacity.replace(",", ".") + (new Date().getTime() - last) / duration;
            element.style.opacity = opacity;
            last = +new Date();
            if (+opacity < 1) {
                (window.requestAnimationFrame && requestAnimationFrame(tick)) || setTimeout(tick, 16);
            }
        };
        tick();
    };
    DomHandler.prototype.fadeOut = function (element, ms) {
        var opacity = 1, interval = 50, duration = ms, gap = interval / duration;
        var fading = setInterval(function () {
            opacity = opacity - gap;
            if (opacity <= 0) {
                opacity = 0;
                clearInterval(fading);
            }
            element.style.opacity = opacity;
        }, interval);
    };
    DomHandler.prototype.getWindowScrollTop = function () {
        var doc = document.documentElement;
        return (window.pageYOffset || doc.scrollTop) - (doc.clientTop || 0);
    };
    DomHandler.prototype.getWindowScrollLeft = function () {
        var doc = document.documentElement;
        return (window.pageXOffset || doc.scrollLeft) - (doc.clientLeft || 0);
    };
    DomHandler.prototype.matches = function (element, selector) {
        var p = Element.prototype;
        var f = p['matches'] || p.webkitMatchesSelector || p['mozMatchesSelector'] || p.msMatchesSelector || function (s) {
            return [].indexOf.call(document.querySelectorAll(s), this) !== -1;
        };
        return f.call(element, selector);
    };
    DomHandler.prototype.getOuterWidth = function (el, margin) {
        var width = el.offsetWidth;
        if (margin) {
            var style = getComputedStyle(el);
            width += parseFloat(style.marginLeft) + parseFloat(style.marginRight);
        }
        return width;
    };
    DomHandler.prototype.getHorizontalPadding = function (el) {
        var style = getComputedStyle(el);
        return parseFloat(style.paddingLeft) + parseFloat(style.paddingRight);
    };
    DomHandler.prototype.getHorizontalMargin = function (el) {
        var style = getComputedStyle(el);
        return parseFloat(style.marginLeft) + parseFloat(style.marginRight);
    };
    DomHandler.prototype.innerWidth = function (el) {
        var width = el.offsetWidth;
        var style = getComputedStyle(el);
        width += parseFloat(style.paddingLeft) + parseFloat(style.paddingRight);
        return width;
    };
    DomHandler.prototype.width = function (el) {
        var width = el.offsetWidth;
        var style = getComputedStyle(el);
        width -= parseFloat(style.paddingLeft) + parseFloat(style.paddingRight);
        return width;
    };
    DomHandler.prototype.getInnerHeight = function (el) {
        var height = el.offsetHeight;
        var style = getComputedStyle(el);
        height += parseFloat(style.paddingTop) + parseFloat(style.paddingBottom);
        return height;
    };
    DomHandler.prototype.getOuterHeight = function (el, margin) {
        var height = el.offsetHeight;
        if (margin) {
            var style = getComputedStyle(el);
            height += parseFloat(style.marginTop) + parseFloat(style.marginBottom);
        }
        return height;
    };
    DomHandler.prototype.getHeight = function (el) {
        var height = el.offsetHeight;
        var style = getComputedStyle(el);
        height -= parseFloat(style.paddingTop) + parseFloat(style.paddingBottom) + parseFloat(style.borderTopWidth) + parseFloat(style.borderBottomWidth);
        return height;
    };
    DomHandler.prototype.getWidth = function (el) {
        var width = el.offsetWidth;
        var style = getComputedStyle(el);
        width -= parseFloat(style.paddingLeft) + parseFloat(style.paddingRight) + parseFloat(style.borderLeftWidth) + parseFloat(style.borderRightWidth);
        return width;
    };
    DomHandler.prototype.getViewport = function () {
        var win = window, d = document, e = d.documentElement, g = d.getElementsByTagName('body')[0], w = win.innerWidth || e.clientWidth || g.clientWidth, h = win.innerHeight || e.clientHeight || g.clientHeight;
        return { width: w, height: h };
    };
    DomHandler.prototype.getOffset = function (el) {
        var rect = el.getBoundingClientRect();
        return {
            top: rect.top + document.body.scrollTop,
            left: rect.left + document.body.scrollLeft
        };
    };
    DomHandler.prototype.replaceElementWith = function (element, replacementElement) {
        var parentNode = element.parentNode;
        if (!parentNode)
            throw "Can't replace element";
        return parentNode.replaceChild(replacementElement, element);
    };
    DomHandler.prototype.getUserAgent = function () {
        return navigator.userAgent;
    };
    DomHandler.prototype.isIE = function () {
        var ua = window.navigator.userAgent;
        var msie = ua.indexOf('MSIE ');
        if (msie > 0) {
            // IE 10 or older => return version number
            return true;
        }
        var trident = ua.indexOf('Trident/');
        if (trident > 0) {
            // IE 11 => return version number
            var rv = ua.indexOf('rv:');
            return true;
        }
        var edge = ua.indexOf('Edge/');
        if (edge > 0) {
            // Edge (IE 12+) => return version number
            return true;
        }
        // other browser
        return false;
    };
    DomHandler.prototype.appendChild = function (element, target) {
        if (this.isElement(target))
            target.appendChild(element);
        else if (target.el && target.el.nativeElement)
            target.el.nativeElement.appendChild(element);
        else
            throw 'Cannot append ' + target + ' to ' + element;
    };
    DomHandler.prototype.removeChild = function (element, target) {
        if (this.isElement(target))
            target.removeChild(element);
        else if (target.el && target.el.nativeElement)
            target.el.nativeElement.removeChild(element);
        else
            throw 'Cannot remove ' + element + ' from ' + target;
    };
    DomHandler.prototype.isElement = function (obj) {
        return (typeof HTMLElement === "object" ? obj instanceof HTMLElement :
            obj && typeof obj === "object" && obj !== null && obj.nodeType === 1 && typeof obj.nodeName === "string");
    };
    DomHandler.prototype.calculateScrollbarWidth = function () {
        if (this.calculatedScrollbarWidth !== null)
            return this.calculatedScrollbarWidth;
        var scrollDiv = document.createElement("div");
        scrollDiv.className = "ui-scrollbar-measure";
        document.body.appendChild(scrollDiv);
        var scrollbarWidth = scrollDiv.offsetWidth - scrollDiv.clientWidth;
        document.body.removeChild(scrollDiv);
        this.calculatedScrollbarWidth = scrollbarWidth;
        return scrollbarWidth;
    };
    DomHandler.prototype.invokeElementMethod = function (element, methodName, args) {
        element[methodName].apply(element, args);
    };
    DomHandler.prototype.clearSelection = function () {
        if (window.getSelection) {
            if (window.getSelection().empty) {
                window.getSelection().empty();
            }
            else if (window.getSelection().removeAllRanges && window.getSelection().rangeCount > 0 && window.getSelection().getRangeAt(0).getClientRects().length > 0) {
                window.getSelection().removeAllRanges();
            }
        }
        else if (document['selection'] && document['selection'].empty) {
            try {
                document['selection'].empty();
            }
            catch (error) {
                //ignore IE bug
            }
        }
    };
    DomHandler.prototype.getBrowser = function () {
        if (!this.browser) {
            var matched = this.resolveUserAgent();
            this.browser = {};
            if (matched.browser) {
                this.browser[matched.browser] = true;
                this.browser['version'] = matched.version;
            }
            if (this.browser['chrome']) {
                this.browser['webkit'] = true;
            }
            else if (this.browser['webkit']) {
                this.browser['safari'] = true;
            }
        }
        return this.browser;
    };
    DomHandler.prototype.resolveUserAgent = function () {
        var ua = navigator.userAgent.toLowerCase();
        var match = /(chrome)[ \/]([\w.]+)/.exec(ua) ||
            /(webkit)[ \/]([\w.]+)/.exec(ua) ||
            /(opera)(?:.*version|)[ \/]([\w.]+)/.exec(ua) ||
            /(msie) ([\w.]+)/.exec(ua) ||
            ua.indexOf("compatible") < 0 && /(mozilla)(?:.*? rv:([\w.]+)|)/.exec(ua) ||
            [];
        return {
            browser: match[1] || "",
            version: match[2] || "0"
        };
    };
    DomHandler.prototype.isInteger = function (value) {
        if (Number.isInteger) {
            return Number.isInteger(value);
        }
        else {
            return typeof value === "number" && isFinite(value) && Math.floor(value) === value;
        }
    };
    DomHandler.zindex = 1000;
    DomHandler = __decorate([
        core_1.Injectable()
    ], DomHandler);
    return DomHandler;
}());
exports.DomHandler = DomHandler;
//# sourceMappingURL=domhandler.js.map

/***/ }),

/***/ 800:
/***/ (function(module, exports, __webpack_require__) {

"use strict";

var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
Object.defineProperty(exports, "__esModule", { value: true });
var core_1 = __webpack_require__(1);
var common_1 = __webpack_require__(45);
var core_2 = __webpack_require__(1);
var Header = /** @class */ (function () {
    function Header() {
    }
    Header = __decorate([
        core_2.Component({
            selector: 'p-header',
            template: '<ng-content></ng-content>'
        })
    ], Header);
    return Header;
}());
exports.Header = Header;
var Footer = /** @class */ (function () {
    function Footer() {
    }
    Footer = __decorate([
        core_2.Component({
            selector: 'p-footer',
            template: '<ng-content></ng-content>'
        })
    ], Footer);
    return Footer;
}());
exports.Footer = Footer;
var PrimeTemplate = /** @class */ (function () {
    function PrimeTemplate(template) {
        this.template = template;
    }
    PrimeTemplate.prototype.getType = function () {
        return this.name;
    };
    __decorate([
        core_1.Input(),
        __metadata("design:type", String)
    ], PrimeTemplate.prototype, "type", void 0);
    __decorate([
        core_1.Input('pTemplate'),
        __metadata("design:type", String)
    ], PrimeTemplate.prototype, "name", void 0);
    PrimeTemplate = __decorate([
        core_1.Directive({
            selector: '[pTemplate]',
            host: {}
        }),
        __metadata("design:paramtypes", [core_1.TemplateRef])
    ], PrimeTemplate);
    return PrimeTemplate;
}());
exports.PrimeTemplate = PrimeTemplate;
/* Deprecated */
var Column = /** @class */ (function () {
    function Column() {
        this.filterType = 'text';
        this.exportable = true;
        this.resizable = true;
        this.sortFunction = new core_1.EventEmitter();
    }
    Column.prototype.ngAfterContentInit = function () {
        var _this = this;
        this.templates.forEach(function (item) {
            switch (item.getType()) {
                case 'header':
                    _this.headerTemplate = item.template;
                    break;
                case 'body':
                    _this.bodyTemplate = item.template;
                    break;
                case 'footer':
                    _this.footerTemplate = item.template;
                    break;
                case 'filter':
                    _this.filterTemplate = item.template;
                    break;
                case 'editor':
                    _this.editorTemplate = item.template;
                    break;
                default:
                    _this.bodyTemplate = item.template;
                    break;
            }
        });
    };
    __decorate([
        core_1.Input(),
        __metadata("design:type", String)
    ], Column.prototype, "field", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", String)
    ], Column.prototype, "colId", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", String)
    ], Column.prototype, "sortField", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", String)
    ], Column.prototype, "filterField", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", String)
    ], Column.prototype, "header", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", String)
    ], Column.prototype, "footer", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", Object)
    ], Column.prototype, "sortable", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", Boolean)
    ], Column.prototype, "editable", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", Boolean)
    ], Column.prototype, "filter", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", String)
    ], Column.prototype, "filterMatchMode", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", String)
    ], Column.prototype, "filterType", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", Boolean)
    ], Column.prototype, "excludeGlobalFilter", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", Number)
    ], Column.prototype, "rowspan", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", Number)
    ], Column.prototype, "colspan", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", String)
    ], Column.prototype, "scope", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", Object)
    ], Column.prototype, "style", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", String)
    ], Column.prototype, "styleClass", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", Boolean)
    ], Column.prototype, "exportable", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", Object)
    ], Column.prototype, "headerStyle", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", String)
    ], Column.prototype, "headerStyleClass", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", Object)
    ], Column.prototype, "bodyStyle", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", String)
    ], Column.prototype, "bodyStyleClass", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", Object)
    ], Column.prototype, "footerStyle", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", String)
    ], Column.prototype, "footerStyleClass", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", Boolean)
    ], Column.prototype, "hidden", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", Boolean)
    ], Column.prototype, "expander", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", String)
    ], Column.prototype, "selectionMode", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", String)
    ], Column.prototype, "filterPlaceholder", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", Number)
    ], Column.prototype, "filterMaxlength", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", Boolean)
    ], Column.prototype, "frozen", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", Boolean)
    ], Column.prototype, "resizable", void 0);
    __decorate([
        core_1.Output(),
        __metadata("design:type", core_1.EventEmitter)
    ], Column.prototype, "sortFunction", void 0);
    __decorate([
        core_1.ContentChildren(PrimeTemplate),
        __metadata("design:type", core_1.QueryList)
    ], Column.prototype, "templates", void 0);
    __decorate([
        core_1.ContentChild(core_1.TemplateRef),
        __metadata("design:type", core_1.TemplateRef)
    ], Column.prototype, "template", void 0);
    Column = __decorate([
        core_2.Component({
            selector: 'p-column',
            template: ''
        })
    ], Column);
    return Column;
}());
exports.Column = Column;
/* Deprecated */
var Row = /** @class */ (function () {
    function Row() {
    }
    __decorate([
        core_1.ContentChildren(Column),
        __metadata("design:type", core_1.QueryList)
    ], Row.prototype, "columns", void 0);
    Row = __decorate([
        core_2.Component({
            selector: 'p-row',
            template: ""
        })
    ], Row);
    return Row;
}());
exports.Row = Row;
/* Deprecated */
var HeaderColumnGroup = /** @class */ (function () {
    function HeaderColumnGroup() {
    }
    __decorate([
        core_1.Input(),
        __metadata("design:type", Boolean)
    ], HeaderColumnGroup.prototype, "frozen", void 0);
    __decorate([
        core_1.ContentChildren(Row),
        __metadata("design:type", core_1.QueryList)
    ], HeaderColumnGroup.prototype, "rows", void 0);
    HeaderColumnGroup = __decorate([
        core_2.Component({
            selector: 'p-headerColumnGroup',
            template: ""
        })
    ], HeaderColumnGroup);
    return HeaderColumnGroup;
}());
exports.HeaderColumnGroup = HeaderColumnGroup;
/* Deprecated */
var FooterColumnGroup = /** @class */ (function () {
    function FooterColumnGroup() {
    }
    __decorate([
        core_1.Input(),
        __metadata("design:type", Boolean)
    ], FooterColumnGroup.prototype, "frozen", void 0);
    __decorate([
        core_1.ContentChildren(Row),
        __metadata("design:type", core_1.QueryList)
    ], FooterColumnGroup.prototype, "rows", void 0);
    FooterColumnGroup = __decorate([
        core_2.Component({
            selector: 'p-footerColumnGroup',
            template: ""
        })
    ], FooterColumnGroup);
    return FooterColumnGroup;
}());
exports.FooterColumnGroup = FooterColumnGroup;
var SharedModule = /** @class */ (function () {
    function SharedModule() {
    }
    SharedModule = __decorate([
        core_1.NgModule({
            imports: [common_1.CommonModule],
            exports: [Header, Footer, Column, PrimeTemplate, Row, HeaderColumnGroup, FooterColumnGroup],
            declarations: [Header, Footer, Column, PrimeTemplate, Row, HeaderColumnGroup, FooterColumnGroup]
        })
    ], SharedModule);
    return SharedModule;
}());
exports.SharedModule = SharedModule;
//# sourceMappingURL=shared.js.map

/***/ }),

/***/ 810:
/***/ (function(module, exports, __webpack_require__) {

"use strict";
/* Shorthand */

function __export(m) {
    for (var p in m) if (!exports.hasOwnProperty(p)) exports[p] = m[p];
}
Object.defineProperty(exports, "__esModule", { value: true });
__export(__webpack_require__(811));

/***/ }),

/***/ 811:
/***/ (function(module, exports, __webpack_require__) {

"use strict";

var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
Object.defineProperty(exports, "__esModule", { value: true });
var core_1 = __webpack_require__(1);
var animations_1 = __webpack_require__(159);
var common_1 = __webpack_require__(45);
var button_1 = __webpack_require__(812);
var domhandler_1 = __webpack_require__(798);
var shared_1 = __webpack_require__(800);
var forms_1 = __webpack_require__(22);
exports.CALENDAR_VALUE_ACCESSOR = {
    provide: forms_1.NG_VALUE_ACCESSOR,
    useExisting: core_1.forwardRef(function () { return Calendar; }),
    multi: true
};
var Calendar = /** @class */ (function () {
    function Calendar(el, domHandler, renderer, cd) {
        this.el = el;
        this.domHandler = domHandler;
        this.renderer = renderer;
        this.cd = cd;
        this.dateFormat = 'mm/dd/yy';
        this.inline = false;
        this.showOtherMonths = true;
        this.icon = 'pi pi-calendar';
        this.shortYearCutoff = '+10';
        this.hourFormat = '24';
        this.stepHour = 1;
        this.stepMinute = 1;
        this.stepSecond = 1;
        this.showSeconds = false;
        this.showOnFocus = true;
        this.dataType = 'date';
        this.selectionMode = 'single';
        this.todayButtonStyleClass = 'ui-button-secondary';
        this.clearButtonStyleClass = 'ui-button-secondary';
        this.autoZIndex = true;
        this.baseZIndex = 0;
        this.keepInvalid = false;
        this.hideOnDateTimeSelect = false;
        this.numberOfMonths = 1;
        this.view = 'date';
        this.showTransitionOptions = '225ms ease-out';
        this.hideTransitionOptions = '195ms ease-in';
        this.onFocus = new core_1.EventEmitter();
        this.onBlur = new core_1.EventEmitter();
        this.onClose = new core_1.EventEmitter();
        this.onSelect = new core_1.EventEmitter();
        this.onInput = new core_1.EventEmitter();
        this.onTodayClick = new core_1.EventEmitter();
        this.onClearClick = new core_1.EventEmitter();
        this.onMonthChange = new core_1.EventEmitter();
        this.onYearChange = new core_1.EventEmitter();
        this._locale = {
            firstDayOfWeek: 0,
            dayNames: ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"],
            dayNamesShort: ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"],
            dayNamesMin: ["Su", "Mo", "Tu", "We", "Th", "Fr", "Sa"],
            monthNames: ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"],
            monthNamesShort: ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"],
            today: 'Today',
            clear: 'Clear'
        };
        this.onModelChange = function () { };
        this.onModelTouched = function () { };
        this.inputFieldValue = null;
    }
    Object.defineProperty(Calendar.prototype, "utc", {
        get: function () {
            return this._utc;
        },
        set: function (_utc) {
            this._utc = _utc;
            console.log("Setting utc has no effect as built-in UTC support is dropped.");
        },
        enumerable: true,
        configurable: true
    });
    Object.defineProperty(Calendar.prototype, "minDate", {
        get: function () {
            return this._minDate;
        },
        set: function (date) {
            this._minDate = date;
            if (this.currentMonth != undefined && this.currentMonth != null && this.currentYear) {
                this.createMonths(this.currentMonth, this.currentYear);
            }
        },
        enumerable: true,
        configurable: true
    });
    Object.defineProperty(Calendar.prototype, "maxDate", {
        get: function () {
            return this._maxDate;
        },
        set: function (date) {
            this._maxDate = date;
            if (this.currentMonth != undefined && this.currentMonth != null && this.currentYear) {
                this.createMonths(this.currentMonth, this.currentYear);
            }
        },
        enumerable: true,
        configurable: true
    });
    Object.defineProperty(Calendar.prototype, "disabledDates", {
        get: function () {
            return this._disabledDates;
        },
        set: function (disabledDates) {
            this._disabledDates = disabledDates;
            if (this.currentMonth != undefined && this.currentMonth != null && this.currentYear) {
                this.createMonths(this.currentMonth, this.currentYear);
            }
        },
        enumerable: true,
        configurable: true
    });
    Object.defineProperty(Calendar.prototype, "disabledDays", {
        get: function () {
            return this._disabledDays;
        },
        set: function (disabledDays) {
            this._disabledDays = disabledDays;
            if (this.currentMonth != undefined && this.currentMonth != null && this.currentYear) {
                this.createMonths(this.currentMonth, this.currentYear);
            }
        },
        enumerable: true,
        configurable: true
    });
    Object.defineProperty(Calendar.prototype, "showTime", {
        get: function () {
            return this._showTime;
        },
        set: function (showTime) {
            this._showTime = showTime;
            if (this.currentHour === undefined) {
                this.initTime(this.value || new Date());
            }
            this.updateInputfield();
        },
        enumerable: true,
        configurable: true
    });
    Object.defineProperty(Calendar.prototype, "locale", {
        get: function () {
            return this._locale;
        },
        set: function (newLocale) {
            this._locale = newLocale;
            if (this.view === 'date') {
                this.createWeekDays();
                this.createMonths(this.currentMonth, this.currentYear);
            }
            else if (this.view === 'month') {
                this.createMonthPickerValues();
            }
        },
        enumerable: true,
        configurable: true
    });
    Calendar.prototype.ngOnInit = function () {
        var date = this.defaultDate || new Date();
        this.currentMonth = date.getMonth();
        this.currentYear = date.getFullYear();
        if (this.yearNavigator && this.yearRange) {
            var years = this.yearRange.split(':');
            var yearStart = parseInt(years[0]);
            var yearEnd = parseInt(years[1]);
            this.populateYearOptions(yearStart, yearEnd);
        }
        if (this.view === 'date') {
            this.createWeekDays();
            this.initTime(date);
            this.createMonths(this.currentMonth, this.currentYear);
            this.ticksTo1970 = (((1970 - 1) * 365 + Math.floor(1970 / 4) - Math.floor(1970 / 100) + Math.floor(1970 / 400)) * 24 * 60 * 60 * 10000000);
        }
        else if (this.view === 'month') {
            this.createMonthPickerValues();
        }
    };
    Calendar.prototype.ngAfterContentInit = function () {
        var _this = this;
        this.templates.forEach(function (item) {
            switch (item.getType()) {
                case 'date':
                    _this.dateTemplate = item.template;
                    break;
                default:
                    _this.dateTemplate = item.template;
                    break;
            }
        });
    };
    Calendar.prototype.populateYearOptions = function (start, end) {
        this.yearOptions = [];
        for (var i = start; i <= end; i++) {
            this.yearOptions.push(i);
        }
    };
    Calendar.prototype.createWeekDays = function () {
        this.weekDays = [];
        var dayIndex = this.locale.firstDayOfWeek;
        for (var i = 0; i < 7; i++) {
            this.weekDays.push(this.locale.dayNamesMin[dayIndex]);
            dayIndex = (dayIndex == 6) ? 0 : ++dayIndex;
        }
    };
    Calendar.prototype.createMonthPickerValues = function () {
        this.monthPickerValues = [];
        for (var i = 0; i <= 11; i++) {
            this.monthPickerValues.push(this.locale.monthNamesShort[i]);
        }
    };
    Calendar.prototype.createMonths = function (month, year) {
        this.months = this.months = [];
        for (var i = 0; i < this.numberOfMonths; i++) {
            var m = month + i;
            var y = year;
            if (m > 11) {
                m = m % 11 - 1;
                y = year + 1;
            }
            this.months.push(this.createMonth(m, y));
        }
    };
    Calendar.prototype.createMonth = function (month, year) {
        var dates = [];
        var firstDay = this.getFirstDayOfMonthIndex(month, year);
        var daysLength = this.getDaysCountInMonth(month, year);
        var prevMonthDaysLength = this.getDaysCountInPrevMonth(month, year);
        var sundayIndex = this.getSundayIndex();
        var dayNo = 1;
        var today = new Date();
        for (var i = 0; i < 6; i++) {
            var week = [];
            if (i == 0) {
                for (var j = (prevMonthDaysLength - firstDay + 1); j <= prevMonthDaysLength; j++) {
                    var prev = this.getPreviousMonthAndYear(month, year);
                    week.push({ day: j, month: prev.month, year: prev.year, otherMonth: true,
                        today: this.isToday(today, j, prev.month, prev.year), selectable: this.isSelectable(j, prev.month, prev.year, true) });
                }
                var remainingDaysLength = 7 - week.length;
                for (var j = 0; j < remainingDaysLength; j++) {
                    week.push({ day: dayNo, month: month, year: year, today: this.isToday(today, dayNo, month, year),
                        selectable: this.isSelectable(dayNo, month, year, false) });
                    dayNo++;
                }
            }
            else {
                for (var j = 0; j < 7; j++) {
                    if (dayNo > daysLength) {
                        var next = this.getNextMonthAndYear(month, year);
                        week.push({ day: dayNo - daysLength, month: next.month, year: next.year, otherMonth: true,
                            today: this.isToday(today, dayNo - daysLength, next.month, next.year),
                            selectable: this.isSelectable((dayNo - daysLength), next.month, next.year, true) });
                    }
                    else {
                        week.push({ day: dayNo, month: month, year: year, today: this.isToday(today, dayNo, month, year),
                            selectable: this.isSelectable(dayNo, month, year, false) });
                    }
                    dayNo++;
                }
            }
            dates.push(week);
        }
        return {
            month: month,
            year: year,
            dates: dates
        };
    };
    Calendar.prototype.initTime = function (date) {
        this.pm = date.getHours() > 11;
        if (this.showTime) {
            this.currentMinute = date.getMinutes();
            this.currentSecond = date.getSeconds();
            if (this.hourFormat == '12')
                this.currentHour = date.getHours() == 0 ? 12 : date.getHours() % 12;
            else
                this.currentHour = date.getHours();
        }
        else if (this.timeOnly) {
            this.currentMinute = 0;
            this.currentHour = 0;
            this.currentSecond = 0;
        }
    };
    Calendar.prototype.navBackward = function (event) {
        if (this.disabled) {
            event.preventDefault();
            return;
        }
        if (this.view === 'month') {
            this.decrementYear();
        }
        else {
            if (this.currentMonth === 0) {
                this.currentMonth = 11;
                this.decrementYear();
            }
            else {
                this.currentMonth--;
            }
            this.onMonthChange.emit({ month: this.currentMonth + 1, year: this.currentYear });
            this.createMonths(this.currentMonth, this.currentYear);
        }
        event.preventDefault();
    };
    Calendar.prototype.navForward = function (event) {
        if (this.disabled) {
            event.preventDefault();
            return;
        }
        if (this.view === 'month') {
            this.incrementYear();
        }
        else {
            if (this.currentMonth === 11) {
                this.currentMonth = 0;
                this.incrementYear();
            }
            else {
                this.currentMonth++;
            }
            this.onMonthChange.emit({ month: this.currentMonth + 1, year: this.currentYear });
            this.createMonths(this.currentMonth, this.currentYear);
        }
        event.preventDefault();
    };
    Calendar.prototype.decrementYear = function () {
        this.currentYear--;
        if (this.yearNavigator && this.currentYear < this.yearOptions[0]) {
            var difference = this.yearOptions[this.yearOptions.length - 1] - this.yearOptions[0];
            this.populateYearOptions(this.yearOptions[0] - difference, this.yearOptions[this.yearOptions.length - 1] - difference);
        }
    };
    Calendar.prototype.incrementYear = function () {
        this.currentYear++;
        if (this.yearNavigator && this.currentYear > this.yearOptions[this.yearOptions.length - 1]) {
            var difference = this.yearOptions[this.yearOptions.length - 1] - this.yearOptions[0];
            this.populateYearOptions(this.yearOptions[0] + difference, this.yearOptions[this.yearOptions.length - 1] + difference);
        }
    };
    Calendar.prototype.onDateSelect = function (event, dateMeta) {
        var _this = this;
        if (this.disabled || !dateMeta.selectable) {
            event.preventDefault();
            return;
        }
        if (this.isMultipleSelection() && this.isSelected(dateMeta)) {
            this.value = this.value.filter(function (date, i) {
                return !_this.isDateEquals(date, dateMeta);
            });
            this.updateModel(this.value);
        }
        else {
            if (this.shouldSelectDate(dateMeta)) {
                if (dateMeta.otherMonth) {
                    this.currentMonth = dateMeta.month;
                    this.currentYear = dateMeta.year;
                    this.createMonths(this.currentMonth, this.currentYear);
                    this.selectDate(dateMeta);
                }
                else {
                    this.selectDate(dateMeta);
                }
            }
        }
        if (this.isSingleSelection() && (!this.showTime || this.hideOnDateTimeSelect)) {
            setTimeout(function () {
                event.preventDefault();
                _this.overlayVisible = false;
                if (_this.mask) {
                    _this.disableModality();
                }
                _this.cd.markForCheck();
            }, 150);
        }
        this.updateInputfield();
        event.preventDefault();
    };
    Calendar.prototype.shouldSelectDate = function (dateMeta) {
        if (this.isMultipleSelection())
            return !this.maxDateCount || !this.value || this.maxDateCount > this.value.length;
        else
            return true;
    };
    Calendar.prototype.onMonthSelect = function (event, index) {
        this.onDateSelect(event, { year: this.currentYear, month: index, day: 1, selectable: true });
    };
    Calendar.prototype.updateInputfield = function () {
        var formattedValue = '';
        if (this.value) {
            if (this.isSingleSelection()) {
                formattedValue = this.formatDateTime(this.value);
            }
            else if (this.isMultipleSelection()) {
                for (var i = 0; i < this.value.length; i++) {
                    var dateAsString = this.formatDateTime(this.value[i]);
                    formattedValue += dateAsString;
                    if (i !== (this.value.length - 1)) {
                        formattedValue += ', ';
                    }
                }
            }
            else if (this.isRangeSelection()) {
                if (this.value && this.value.length) {
                    var startDate = this.value[0];
                    var endDate = this.value[1];
                    formattedValue = this.formatDateTime(startDate);
                    if (endDate) {
                        formattedValue += ' - ' + this.formatDateTime(endDate);
                    }
                }
            }
        }
        this.inputFieldValue = formattedValue;
        this.updateFilledState();
        if (this.inputfieldViewChild && this.inputfieldViewChild.nativeElement) {
            this.inputfieldViewChild.nativeElement.value = this.inputFieldValue;
        }
    };
    Calendar.prototype.formatDateTime = function (date) {
        var formattedValue = null;
        if (date) {
            if (this.timeOnly) {
                formattedValue = this.formatTime(date);
            }
            else {
                formattedValue = this.formatDate(date, this.dateFormat);
                if (this.showTime) {
                    formattedValue += ' ' + this.formatTime(date);
                }
            }
        }
        return formattedValue;
    };
    Calendar.prototype.selectDate = function (dateMeta) {
        var date = new Date(dateMeta.year, dateMeta.month, dateMeta.day);
        if (this.showTime) {
            if (this.hourFormat === '12' && this.pm && this.currentHour != 12)
                date.setHours(this.currentHour + 12);
            else
                date.setHours(this.currentHour);
            date.setMinutes(this.currentMinute);
            date.setSeconds(this.currentSecond);
        }
        if (this.minDate && this.minDate > date) {
            date = this.minDate;
            this.currentHour = date.getHours();
            this.currentMinute = date.getMinutes();
            this.currentSecond = date.getSeconds();
        }
        if (this.maxDate && this.maxDate < date) {
            date = this.maxDate;
            this.currentHour = date.getHours();
            this.currentMinute = date.getMinutes();
            this.currentSecond = date.getSeconds();
        }
        if (this.isSingleSelection()) {
            this.updateModel(date);
        }
        else if (this.isMultipleSelection()) {
            this.updateModel(this.value ? this.value.concat([date]) : [date]);
        }
        else if (this.isRangeSelection()) {
            if (this.value && this.value.length) {
                var startDate = this.value[0];
                var endDate = this.value[1];
                if (!endDate && date.getTime() >= startDate.getTime()) {
                    endDate = date;
                }
                else {
                    startDate = date;
                    endDate = null;
                }
                this.updateModel([startDate, endDate]);
            }
            else {
                this.updateModel([date, null]);
            }
        }
        this.onSelect.emit(date);
    };
    Calendar.prototype.updateModel = function (value) {
        var _this = this;
        this.value = value;
        if (this.dataType == 'date') {
            this.onModelChange(this.value);
        }
        else if (this.dataType == 'string') {
            if (this.isSingleSelection()) {
                this.onModelChange(this.formatDateTime(this.value));
            }
            else {
                var stringArrValue = null;
                if (this.value) {
                    stringArrValue = this.value.map(function (date) { return _this.formatDateTime(date); });
                }
                this.onModelChange(stringArrValue);
            }
        }
    };
    Calendar.prototype.getFirstDayOfMonthIndex = function (month, year) {
        var day = new Date();
        day.setDate(1);
        day.setMonth(month);
        day.setFullYear(year);
        var dayIndex = day.getDay() + this.getSundayIndex();
        return dayIndex >= 7 ? dayIndex - 7 : dayIndex;
    };
    Calendar.prototype.getDaysCountInMonth = function (month, year) {
        return 32 - this.daylightSavingAdjust(new Date(year, month, 32)).getDate();
    };
    Calendar.prototype.getDaysCountInPrevMonth = function (month, year) {
        var prev = this.getPreviousMonthAndYear(month, year);
        return this.getDaysCountInMonth(prev.month, prev.year);
    };
    Calendar.prototype.getPreviousMonthAndYear = function (month, year) {
        var m, y;
        if (month === 0) {
            m = 11;
            y = year - 1;
        }
        else {
            m = month - 1;
            y = year;
        }
        return { 'month': m, 'year': y };
    };
    Calendar.prototype.getNextMonthAndYear = function (month, year) {
        var m, y;
        if (month === 11) {
            m = 0;
            y = year + 1;
        }
        else {
            m = month + 1;
            y = year;
        }
        return { 'month': m, 'year': y };
    };
    Calendar.prototype.getSundayIndex = function () {
        return this.locale.firstDayOfWeek > 0 ? 7 - this.locale.firstDayOfWeek : 0;
    };
    Calendar.prototype.isSelected = function (dateMeta) {
        if (this.value) {
            if (this.isSingleSelection()) {
                return this.isDateEquals(this.value, dateMeta);
            }
            else if (this.isMultipleSelection()) {
                var selected = false;
                for (var _i = 0, _a = this.value; _i < _a.length; _i++) {
                    var date = _a[_i];
                    selected = this.isDateEquals(date, dateMeta);
                    if (selected) {
                        break;
                    }
                }
                return selected;
            }
            else if (this.isRangeSelection()) {
                if (this.value[1])
                    return this.isDateEquals(this.value[0], dateMeta) || this.isDateEquals(this.value[1], dateMeta) || this.isDateBetween(this.value[0], this.value[1], dateMeta);
                else
                    return this.isDateEquals(this.value[0], dateMeta);
            }
        }
        else {
            return false;
        }
    };
    Calendar.prototype.isMonthSelected = function (month) {
        return this.value ? (this.value.getMonth() === month && this.value.getFullYear() === this.currentYear) : false;
    };
    Calendar.prototype.isDateEquals = function (value, dateMeta) {
        if (value)
            return value.getDate() === dateMeta.day && value.getMonth() === dateMeta.month && value.getFullYear() === dateMeta.year;
        else
            return false;
    };
    Calendar.prototype.isDateBetween = function (start, end, dateMeta) {
        var between = false;
        if (start && end) {
            var date = new Date(dateMeta.year, dateMeta.month, dateMeta.day);
            return start.getTime() <= date.getTime() && end.getTime() >= date.getTime();
        }
        return between;
    };
    Calendar.prototype.isSingleSelection = function () {
        return this.selectionMode === 'single';
    };
    Calendar.prototype.isRangeSelection = function () {
        return this.selectionMode === 'range';
    };
    Calendar.prototype.isMultipleSelection = function () {
        return this.selectionMode === 'multiple';
    };
    Calendar.prototype.isToday = function (today, day, month, year) {
        return today.getDate() === day && today.getMonth() === month && today.getFullYear() === year;
    };
    Calendar.prototype.isSelectable = function (day, month, year, otherMonth) {
        var validMin = true;
        var validMax = true;
        var validDate = true;
        var validDay = true;
        if (otherMonth && !this.selectOtherMonths) {
            return false;
        }
        if (this.minDate) {
            if (this.minDate.getFullYear() > year) {
                validMin = false;
            }
            else if (this.minDate.getFullYear() === year) {
                if (this.minDate.getMonth() > month) {
                    validMin = false;
                }
                else if (this.minDate.getMonth() === month) {
                    if (this.minDate.getDate() > day) {
                        validMin = false;
                    }
                }
            }
        }
        if (this.maxDate) {
            if (this.maxDate.getFullYear() < year) {
                validMax = false;
            }
            else if (this.maxDate.getFullYear() === year) {
                if (this.maxDate.getMonth() < month) {
                    validMax = false;
                }
                else if (this.maxDate.getMonth() === month) {
                    if (this.maxDate.getDate() < day) {
                        validMax = false;
                    }
                }
            }
        }
        if (this.disabledDates) {
            validDate = !this.isDateDisabled(day, month, year);
        }
        if (this.disabledDays) {
            validDay = !this.isDayDisabled(day, month, year);
        }
        return validMin && validMax && validDate && validDay;
    };
    Calendar.prototype.isDateDisabled = function (day, month, year) {
        if (this.disabledDates) {
            for (var _i = 0, _a = this.disabledDates; _i < _a.length; _i++) {
                var disabledDate = _a[_i];
                if (disabledDate.getFullYear() === year && disabledDate.getMonth() === month && disabledDate.getDate() === day) {
                    return true;
                }
            }
        }
        return false;
    };
    Calendar.prototype.isDayDisabled = function (day, month, year) {
        if (this.disabledDays) {
            var weekday = new Date(year, month, day);
            var weekdayNumber = weekday.getDay();
            return this.disabledDays.indexOf(weekdayNumber) !== -1;
        }
        return false;
    };
    Calendar.prototype.onInputFocus = function (event) {
        this.focus = true;
        if (this.showOnFocus) {
            this.showOverlay();
        }
        this.onFocus.emit(event);
    };
    Calendar.prototype.onInputClick = function (event) {
        this.datepickerClick = true;
        if (this.overlay && this.autoZIndex) {
            this.overlay.style.zIndex = String(this.baseZIndex + (++domhandler_1.DomHandler.zindex));
        }
    };
    Calendar.prototype.onInputBlur = function (event) {
        this.focus = false;
        this.onBlur.emit(event);
        if (!this.keepInvalid) {
            this.updateInputfield();
        }
        this.onModelTouched();
    };
    Calendar.prototype.onButtonClick = function (event, inputfield) {
        if (!this.overlayVisible) {
            inputfield.focus();
            this.showOverlay();
        }
        else {
            this.overlayVisible = false;
        }
        this.datepickerClick = true;
    };
    Calendar.prototype.onInputKeydown = function (event) {
        this.isKeydown = true;
        if (event.keyCode === 9) {
            this.overlayVisible = false;
        }
    };
    Calendar.prototype.onMonthDropdownChange = function (m) {
        this.currentMonth = parseInt(m);
        this.onMonthChange.emit({ month: this.currentMonth + 1, year: this.currentYear });
        this.createMonths(this.currentMonth, this.currentYear);
    };
    Calendar.prototype.onYearDropdownChange = function (y) {
        this.currentYear = parseInt(y);
        this.onYearChange.emit({ month: this.currentMonth + 1, year: this.currentYear });
        this.createMonths(this.currentMonth, this.currentYear);
    };
    Calendar.prototype.incrementHour = function (event) {
        var prevHour = this.currentHour;
        var newHour = this.currentHour + this.stepHour;
        if (this.validateHour(newHour)) {
            if (this.hourFormat == '24')
                this.currentHour = (newHour >= 24) ? (newHour - 24) : newHour;
            else if (this.hourFormat == '12') {
                // Before the AM/PM break, now after
                if (prevHour < 12 && newHour > 11) {
                    this.pm = !this.pm;
                }
                this.currentHour = (newHour >= 13) ? (newHour - 12) : newHour;
            }
            this.updateTime();
        }
        event.preventDefault();
    };
    Calendar.prototype.decrementHour = function (event) {
        var newHour = this.currentHour - this.stepHour;
        if (this.validateHour(newHour)) {
            if (this.hourFormat == '24')
                this.currentHour = (newHour < 0) ? (24 + newHour) : newHour;
            else if (this.hourFormat == '12') {
                // If we were at noon/midnight, then switch
                if (this.currentHour === 12) {
                    this.pm = !this.pm;
                }
                this.currentHour = (newHour <= 0) ? (12 + newHour) : newHour;
            }
            this.updateTime();
        }
        event.preventDefault();
    };
    Calendar.prototype.validateHour = function (hour) {
        var valid = true;
        var value = this.value;
        if (this.isRangeSelection()) {
            value = this.value[1] || this.value[0];
        }
        if (this.isMultipleSelection()) {
            value = this.value[this.value.length - 1];
        }
        var valueDateString = value ? value.toDateString() : null;
        if (this.minDate && valueDateString && this.minDate.toDateString() === valueDateString) {
            if (this.minDate.getHours() > hour) {
                valid = false;
            }
        }
        if (this.maxDate && valueDateString && this.maxDate.toDateString() === valueDateString) {
            if (this.maxDate.getHours() < hour) {
                valid = false;
            }
        }
        return valid;
    };
    Calendar.prototype.incrementMinute = function (event) {
        var newMinute = this.currentMinute + this.stepMinute;
        if (this.validateMinute(newMinute)) {
            this.currentMinute = (newMinute > 59) ? newMinute - 60 : newMinute;
            this.updateTime();
        }
        event.preventDefault();
    };
    Calendar.prototype.decrementMinute = function (event) {
        var newMinute = this.currentMinute - this.stepMinute;
        if (this.validateMinute(newMinute)) {
            this.currentMinute = (newMinute < 0) ? 60 + newMinute : newMinute;
            this.updateTime();
        }
        event.preventDefault();
    };
    Calendar.prototype.validateMinute = function (minute) {
        var valid = true;
        var value = this.value;
        if (this.isRangeSelection()) {
            value = this.value[1] || this.value[0];
        }
        if (this.isMultipleSelection()) {
            value = this.value[this.value.length - 1];
        }
        var valueDateString = value ? value.toDateString() : null;
        if (this.minDate && valueDateString && this.minDate.toDateString() === valueDateString) {
            if (value.getHours() == this.minDate.getHours()) {
                if (this.minDate.getMinutes() > minute) {
                    valid = false;
                }
            }
        }
        if (this.maxDate && valueDateString && this.maxDate.toDateString() === valueDateString) {
            if (value.getHours() == this.maxDate.getHours()) {
                if (this.maxDate.getMinutes() < minute) {
                    valid = false;
                }
            }
        }
        return valid;
    };
    Calendar.prototype.incrementSecond = function (event) {
        var newSecond = this.currentSecond + this.stepSecond;
        if (this.validateSecond(newSecond)) {
            this.currentSecond = (newSecond > 59) ? newSecond - 60 : newSecond;
            this.updateTime();
        }
        event.preventDefault();
    };
    Calendar.prototype.decrementSecond = function (event) {
        var newSecond = this.currentSecond - this.stepSecond;
        if (this.validateSecond(newSecond)) {
            this.currentSecond = (newSecond < 0) ? 60 + newSecond : newSecond;
            this.updateTime();
        }
        event.preventDefault();
    };
    Calendar.prototype.validateSecond = function (second) {
        var valid = true;
        var value = this.value;
        if (this.isRangeSelection()) {
            value = this.value[1] || this.value[0];
        }
        if (this.isMultipleSelection()) {
            value = this.value[this.value.length - 1];
        }
        var valueDateString = value ? value.toDateString() : null;
        if (this.minDate && valueDateString && this.minDate.toDateString() === valueDateString) {
            if (this.minDate.getSeconds() > second) {
                valid = false;
            }
        }
        if (this.maxDate && valueDateString && this.maxDate.toDateString() === valueDateString) {
            if (this.maxDate.getSeconds() < second) {
                valid = false;
            }
        }
        return valid;
    };
    Calendar.prototype.updateTime = function () {
        var value = this.value;
        if (this.isRangeSelection()) {
            value = this.value[1] || this.value[0];
        }
        if (this.isMultipleSelection()) {
            value = this.value[this.value.length - 1];
        }
        value = value ? new Date(value.getTime()) : new Date();
        if (this.hourFormat == '12') {
            if (this.currentHour === 12)
                value.setHours(this.pm ? 12 : 0);
            else
                value.setHours(this.pm ? this.currentHour + 12 : this.currentHour);
        }
        else {
            value.setHours(this.currentHour);
        }
        value.setMinutes(this.currentMinute);
        value.setSeconds(this.currentSecond);
        if (this.isRangeSelection()) {
            if (this.value[1])
                value = [this.value[0], value];
            else
                value = [value, null];
        }
        if (this.isMultipleSelection()) {
            value = this.value.slice(0, -1).concat([value]);
        }
        this.updateModel(value);
        this.onSelect.emit(value);
        this.updateInputfield();
    };
    Calendar.prototype.toggleAMPM = function (event) {
        this.pm = !this.pm;
        this.updateTime();
        event.preventDefault();
    };
    Calendar.prototype.onUserInput = function (event) {
        // IE 11 Workaround for input placeholder : https://github.com/primefaces/primeng/issues/2026
        if (!this.isKeydown) {
            return;
        }
        this.isKeydown = false;
        var val = event.target.value;
        try {
            var value = this.parseValueFromString(val);
            if (this.isSelectable(value.getDate(), value.getMonth(), value.getFullYear(), false)) {
                this.updateModel(value);
                this.updateUI();
            }
        }
        catch (err) {
            //invalid date
            this.updateModel(null);
        }
        this.filled = val != null && val.length;
        this.onInput.emit(event);
    };
    Calendar.prototype.parseValueFromString = function (text) {
        if (!text || text.trim().length === 0) {
            return null;
        }
        var value;
        if (this.isSingleSelection()) {
            value = this.parseDateTime(text);
        }
        else if (this.isMultipleSelection()) {
            var tokens = text.split(',');
            value = [];
            for (var _i = 0, tokens_1 = tokens; _i < tokens_1.length; _i++) {
                var token = tokens_1[_i];
                value.push(this.parseDateTime(token.trim()));
            }
        }
        else if (this.isRangeSelection()) {
            var tokens = text.split(' - ');
            value = [];
            for (var i = 0; i < tokens.length; i++) {
                value[i] = this.parseDateTime(tokens[i].trim());
            }
        }
        return value;
    };
    Calendar.prototype.parseDateTime = function (text) {
        var date;
        var parts = text.split(' ');
        if (this.timeOnly) {
            date = new Date();
            this.populateTime(date, parts[0], parts[1]);
        }
        else {
            if (this.showTime) {
                date = this.parseDate(parts[0], this.dateFormat);
                this.populateTime(date, parts[1], parts[2]);
            }
            else {
                date = this.parseDate(text, this.dateFormat);
            }
        }
        return date;
    };
    Calendar.prototype.populateTime = function (value, timeString, ampm) {
        if (this.hourFormat == '12' && !ampm) {
            throw 'Invalid Time';
        }
        this.pm = (ampm === 'PM' || ampm === 'pm');
        var time = this.parseTime(timeString);
        value.setHours(time.hour);
        value.setMinutes(time.minute);
        value.setSeconds(time.second);
    };
    Calendar.prototype.updateUI = function () {
        var val = this.value || this.defaultDate || new Date();
        if (Array.isArray(val)) {
            val = val[0];
        }
        this.currentMonth = val.getMonth();
        this.currentYear = val.getFullYear();
        this.createMonths(this.currentMonth, this.currentYear);
        if (this.showTime || this.timeOnly) {
            var hours = val.getHours();
            if (this.hourFormat == '12') {
                this.pm = hours > 11;
                if (hours >= 12) {
                    this.currentHour = (hours == 12) ? 12 : hours - 12;
                }
                else {
                    this.currentHour = (hours == 0) ? 12 : hours;
                }
            }
            else {
                this.currentHour = val.getHours();
            }
            this.currentMinute = val.getMinutes();
            this.currentSecond = val.getSeconds();
        }
    };
    Calendar.prototype.onDatePickerClick = function (event) {
        this.datepickerClick = true;
    };
    Calendar.prototype.showOverlay = function () {
        this.overlayVisible = true;
    };
    Calendar.prototype.onOverlayAnimationStart = function (event) {
        switch (event.toState) {
            case 'visible':
            case 'visibleTouchUI':
                if (!this.inline) {
                    this.overlay = event.element;
                    this.appendOverlay();
                    if (this.autoZIndex) {
                        this.overlay.style.zIndex = String(this.baseZIndex + (++domhandler_1.DomHandler.zindex));
                    }
                    this.alignOverlay();
                    this.bindDocumentClickListener();
                }
                break;
            case 'void':
                this.onOverlayHide();
                break;
        }
    };
    Calendar.prototype.appendOverlay = function () {
        if (this.appendTo) {
            if (this.appendTo === 'body')
                document.body.appendChild(this.overlay);
            else
                this.domHandler.appendChild(this.overlay, this.appendTo);
        }
    };
    Calendar.prototype.restoreOverlayAppend = function () {
        if (this.overlay && this.appendTo) {
            this.el.nativeElement.appendChild(this.overlay);
        }
    };
    Calendar.prototype.alignOverlay = function () {
        if (this.touchUI) {
            this.enableModality(this.overlay);
        }
        else {
            if (this.appendTo)
                this.domHandler.absolutePosition(this.overlay, this.inputfieldViewChild.nativeElement);
            else
                this.domHandler.relativePosition(this.overlay, this.inputfieldViewChild.nativeElement);
        }
    };
    Calendar.prototype.enableModality = function (element) {
        var _this = this;
        if (!this.mask) {
            this.mask = document.createElement('div');
            this.mask.style.zIndex = String(parseInt(element.style.zIndex) - 1);
            var maskStyleClass = 'ui-widget-overlay ui-datepicker-mask ui-datepicker-mask-scrollblocker';
            this.domHandler.addMultipleClasses(this.mask, maskStyleClass);
            this.maskClickListener = this.renderer.listen(this.mask, 'click', function (event) {
                _this.disableModality();
            });
            document.body.appendChild(this.mask);
            this.domHandler.addClass(document.body, 'ui-overflow-hidden');
        }
    };
    Calendar.prototype.disableModality = function () {
        if (this.mask) {
            document.body.removeChild(this.mask);
            var bodyChildren = document.body.children;
            var hasBlockerMasks = void 0;
            for (var i = 0; i < bodyChildren.length; i++) {
                var bodyChild = bodyChildren[i];
                if (this.domHandler.hasClass(bodyChild, 'ui-datepicker-mask-scrollblocker')) {
                    hasBlockerMasks = true;
                    break;
                }
            }
            if (!hasBlockerMasks) {
                this.domHandler.removeClass(document.body, 'ui-overflow-hidden');
            }
            this.overlayVisible = false;
            this.unbindMaskClickListener();
            this.mask = null;
        }
    };
    Calendar.prototype.unbindMaskClickListener = function () {
        if (this.maskClickListener) {
            this.maskClickListener();
            this.maskClickListener = null;
        }
    };
    Calendar.prototype.writeValue = function (value) {
        this.value = value;
        if (this.value && typeof this.value === 'string') {
            this.value = this.parseValueFromString(this.value);
        }
        this.updateInputfield();
        this.updateUI();
    };
    Calendar.prototype.registerOnChange = function (fn) {
        this.onModelChange = fn;
    };
    Calendar.prototype.registerOnTouched = function (fn) {
        this.onModelTouched = fn;
    };
    Calendar.prototype.setDisabledState = function (val) {
        this.disabled = val;
    };
    // Ported from jquery-ui datepicker formatDate
    Calendar.prototype.formatDate = function (date, format) {
        if (!date) {
            return '';
        }
        var iFormat;
        var lookAhead = function (match) {
            var matches = (iFormat + 1 < format.length && format.charAt(iFormat + 1) === match);
            if (matches) {
                iFormat++;
            }
            return matches;
        }, formatNumber = function (match, value, len) {
            var num = '' + value;
            if (lookAhead(match)) {
                while (num.length < len) {
                    num = '0' + num;
                }
            }
            return num;
        }, formatName = function (match, value, shortNames, longNames) {
            return (lookAhead(match) ? longNames[value] : shortNames[value]);
        };
        var output = '';
        var literal = false;
        if (date) {
            for (iFormat = 0; iFormat < format.length; iFormat++) {
                if (literal) {
                    if (format.charAt(iFormat) === '\'' && !lookAhead('\'')) {
                        literal = false;
                    }
                    else {
                        output += format.charAt(iFormat);
                    }
                }
                else {
                    switch (format.charAt(iFormat)) {
                        case 'd':
                            output += formatNumber('d', date.getDate(), 2);
                            break;
                        case 'D':
                            output += formatName('D', date.getDay(), this.locale.dayNamesShort, this.locale.dayNames);
                            break;
                        case 'o':
                            output += formatNumber('o', Math.round((new Date(date.getFullYear(), date.getMonth(), date.getDate()).getTime() -
                                new Date(date.getFullYear(), 0, 0).getTime()) / 86400000), 3);
                            break;
                        case 'm':
                            output += formatNumber('m', date.getMonth() + 1, 2);
                            break;
                        case 'M':
                            output += formatName('M', date.getMonth(), this.locale.monthNamesShort, this.locale.monthNames);
                            break;
                        case 'y':
                            output += lookAhead('y') ? date.getFullYear() : (date.getFullYear() % 100 < 10 ? '0' : '') + (date.getFullYear() % 100);
                            break;
                        case '@':
                            output += date.getTime();
                            break;
                        case '!':
                            output += date.getTime() * 10000 + this.ticksTo1970;
                            break;
                        case '\'':
                            if (lookAhead('\'')) {
                                output += '\'';
                            }
                            else {
                                literal = true;
                            }
                            break;
                        default:
                            output += format.charAt(iFormat);
                    }
                }
            }
        }
        return output;
    };
    Calendar.prototype.formatTime = function (date) {
        if (!date) {
            return '';
        }
        var output = '';
        var hours = date.getHours();
        var minutes = date.getMinutes();
        var seconds = date.getSeconds();
        if (this.hourFormat == '12' && hours > 11 && hours != 12) {
            hours -= 12;
        }
        if (this.hourFormat == '12') {
            output += hours === 0 ? 12 : (hours < 10) ? '0' + hours : hours;
        }
        else {
            output += (hours < 10) ? '0' + hours : hours;
        }
        output += ':';
        output += (minutes < 10) ? '0' + minutes : minutes;
        if (this.showSeconds) {
            output += ':';
            output += (seconds < 10) ? '0' + seconds : seconds;
        }
        if (this.hourFormat == '12') {
            output += date.getHours() > 11 ? ' PM' : ' AM';
        }
        return output;
    };
    Calendar.prototype.parseTime = function (value) {
        var tokens = value.split(':');
        var validTokenLength = this.showSeconds ? 3 : 2;
        if (tokens.length !== validTokenLength) {
            throw "Invalid time";
        }
        var h = parseInt(tokens[0]);
        var m = parseInt(tokens[1]);
        var s = this.showSeconds ? parseInt(tokens[2]) : null;
        if (isNaN(h) || isNaN(m) || h > 23 || m > 59 || (this.hourFormat == '12' && h > 12) || (this.showSeconds && (isNaN(s) || s > 59))) {
            throw "Invalid time";
        }
        else {
            if (this.hourFormat == '12' && h !== 12 && this.pm) {
                h += 12;
            }
            return { hour: h, minute: m, second: s };
        }
    };
    // Ported from jquery-ui datepicker parseDate
    Calendar.prototype.parseDate = function (value, format) {
        if (format == null || value == null) {
            throw "Invalid arguments";
        }
        value = (typeof value === "object" ? value.toString() : value + "");
        if (value === "") {
            return null;
        }
        var iFormat, dim, extra, iValue = 0, shortYearCutoff = (typeof this.shortYearCutoff !== "string" ? this.shortYearCutoff : new Date().getFullYear() % 100 + parseInt(this.shortYearCutoff, 10)), year = -1, month = -1, day = -1, doy = -1, literal = false, date, lookAhead = function (match) {
            var matches = (iFormat + 1 < format.length && format.charAt(iFormat + 1) === match);
            if (matches) {
                iFormat++;
            }
            return matches;
        }, getNumber = function (match) {
            var isDoubled = lookAhead(match), size = (match === "@" ? 14 : (match === "!" ? 20 :
                (match === "y" && isDoubled ? 4 : (match === "o" ? 3 : 2)))), minSize = (match === "y" ? size : 1), digits = new RegExp("^\\d{" + minSize + "," + size + "}"), num = value.substring(iValue).match(digits);
            if (!num) {
                throw "Missing number at position " + iValue;
            }
            iValue += num[0].length;
            return parseInt(num[0], 10);
        }, getName = function (match, shortNames, longNames) {
            var index = -1;
            var arr = lookAhead(match) ? longNames : shortNames;
            var names = [];
            for (var i = 0; i < arr.length; i++) {
                names.push([i, arr[i]]);
            }
            names.sort(function (a, b) {
                return -(a[1].length - b[1].length);
            });
            for (var i = 0; i < names.length; i++) {
                var name_1 = names[i][1];
                if (value.substr(iValue, name_1.length).toLowerCase() === name_1.toLowerCase()) {
                    index = names[i][0];
                    iValue += name_1.length;
                    break;
                }
            }
            if (index !== -1) {
                return index + 1;
            }
            else {
                throw "Unknown name at position " + iValue;
            }
        }, checkLiteral = function () {
            if (value.charAt(iValue) !== format.charAt(iFormat)) {
                throw "Unexpected literal at position " + iValue;
            }
            iValue++;
        };
        if (this.view === 'month') {
            day = 1;
        }
        for (iFormat = 0; iFormat < format.length; iFormat++) {
            if (literal) {
                if (format.charAt(iFormat) === "'" && !lookAhead("'")) {
                    literal = false;
                }
                else {
                    checkLiteral();
                }
            }
            else {
                switch (format.charAt(iFormat)) {
                    case "d":
                        day = getNumber("d");
                        break;
                    case "D":
                        getName("D", this.locale.dayNamesShort, this.locale.dayNames);
                        break;
                    case "o":
                        doy = getNumber("o");
                        break;
                    case "m":
                        month = getNumber("m");
                        break;
                    case "M":
                        month = getName("M", this.locale.monthNamesShort, this.locale.monthNames);
                        break;
                    case "y":
                        year = getNumber("y");
                        break;
                    case "@":
                        date = new Date(getNumber("@"));
                        year = date.getFullYear();
                        month = date.getMonth() + 1;
                        day = date.getDate();
                        break;
                    case "!":
                        date = new Date((getNumber("!") - this.ticksTo1970) / 10000);
                        year = date.getFullYear();
                        month = date.getMonth() + 1;
                        day = date.getDate();
                        break;
                    case "'":
                        if (lookAhead("'")) {
                            checkLiteral();
                        }
                        else {
                            literal = true;
                        }
                        break;
                    default:
                        checkLiteral();
                }
            }
        }
        if (iValue < value.length) {
            extra = value.substr(iValue);
            if (!/^\s+/.test(extra)) {
                throw "Extra/unparsed characters found in date: " + extra;
            }
        }
        if (year === -1) {
            year = new Date().getFullYear();
        }
        else if (year < 100) {
            year += new Date().getFullYear() - new Date().getFullYear() % 100 +
                (year <= shortYearCutoff ? 0 : -100);
        }
        if (doy > -1) {
            month = 1;
            day = doy;
            do {
                dim = this.getDaysCountInMonth(year, month - 1);
                if (day <= dim) {
                    break;
                }
                month++;
                day -= dim;
            } while (true);
        }
        date = this.daylightSavingAdjust(new Date(year, month - 1, day));
        if (date.getFullYear() !== year || date.getMonth() + 1 !== month || date.getDate() !== day) {
            throw "Invalid date"; // E.g. 31/02/00
        }
        return date;
    };
    Calendar.prototype.daylightSavingAdjust = function (date) {
        if (!date) {
            return null;
        }
        date.setHours(date.getHours() > 12 ? date.getHours() + 2 : 0);
        return date;
    };
    Calendar.prototype.updateFilledState = function () {
        this.filled = this.inputFieldValue && this.inputFieldValue != '';
    };
    Calendar.prototype.onTodayButtonClick = function (event) {
        var date = new Date();
        var dateMeta = { day: date.getDate(), month: date.getMonth(), year: date.getFullYear(), otherMonth: date.getMonth() !== this.currentMonth || date.getFullYear() !== this.currentYear, today: true, selectable: true };
        this.onDateSelect(event, dateMeta);
        this.onTodayClick.emit(event);
    };
    Calendar.prototype.onClearButtonClick = function (event) {
        this.updateModel(null);
        this.updateInputfield();
        this.overlayVisible = false;
        this.onClearClick.emit(event);
    };
    Calendar.prototype.bindDocumentClickListener = function () {
        var _this = this;
        if (!this.documentClickListener) {
            this.documentClickListener = this.renderer.listen('document', 'click', function (event) {
                if (!_this.datepickerClick && _this.overlayVisible) {
                    _this.overlayVisible = false;
                    _this.onClose.emit(event);
                }
                _this.datepickerClick = false;
                _this.cd.detectChanges();
            });
        }
    };
    Calendar.prototype.unbindDocumentClickListener = function () {
        if (this.documentClickListener) {
            this.documentClickListener();
            this.documentClickListener = null;
        }
    };
    Calendar.prototype.onOverlayHide = function () {
        this.unbindDocumentClickListener();
        this.unbindMaskClickListener();
        this.overlay = null;
    };
    Calendar.prototype.ngOnDestroy = function () {
        this.restoreOverlayAppend();
        this.onOverlayHide();
    };
    __decorate([
        core_1.Input(),
        __metadata("design:type", Date)
    ], Calendar.prototype, "defaultDate", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", Object)
    ], Calendar.prototype, "style", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", String)
    ], Calendar.prototype, "styleClass", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", Object)
    ], Calendar.prototype, "inputStyle", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", String)
    ], Calendar.prototype, "inputId", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", String)
    ], Calendar.prototype, "name", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", String)
    ], Calendar.prototype, "inputStyleClass", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", String)
    ], Calendar.prototype, "placeholder", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", Object)
    ], Calendar.prototype, "disabled", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", String)
    ], Calendar.prototype, "dateFormat", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", Boolean)
    ], Calendar.prototype, "inline", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", Boolean)
    ], Calendar.prototype, "showOtherMonths", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", Boolean)
    ], Calendar.prototype, "selectOtherMonths", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", Boolean)
    ], Calendar.prototype, "showIcon", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", String)
    ], Calendar.prototype, "icon", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", Object)
    ], Calendar.prototype, "appendTo", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", Boolean)
    ], Calendar.prototype, "readonlyInput", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", Object)
    ], Calendar.prototype, "shortYearCutoff", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", Boolean)
    ], Calendar.prototype, "monthNavigator", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", Boolean)
    ], Calendar.prototype, "yearNavigator", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", String)
    ], Calendar.prototype, "yearRange", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", String)
    ], Calendar.prototype, "hourFormat", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", Boolean)
    ], Calendar.prototype, "timeOnly", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", Number)
    ], Calendar.prototype, "stepHour", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", Number)
    ], Calendar.prototype, "stepMinute", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", Number)
    ], Calendar.prototype, "stepSecond", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", Boolean)
    ], Calendar.prototype, "showSeconds", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", Boolean)
    ], Calendar.prototype, "required", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", Boolean)
    ], Calendar.prototype, "showOnFocus", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", String)
    ], Calendar.prototype, "dataType", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", String)
    ], Calendar.prototype, "selectionMode", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", Number)
    ], Calendar.prototype, "maxDateCount", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", Boolean)
    ], Calendar.prototype, "showButtonBar", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", String)
    ], Calendar.prototype, "todayButtonStyleClass", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", String)
    ], Calendar.prototype, "clearButtonStyleClass", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", Boolean)
    ], Calendar.prototype, "autoZIndex", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", Number)
    ], Calendar.prototype, "baseZIndex", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", String)
    ], Calendar.prototype, "panelStyleClass", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", Object)
    ], Calendar.prototype, "panelStyle", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", Boolean)
    ], Calendar.prototype, "keepInvalid", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", Boolean)
    ], Calendar.prototype, "hideOnDateTimeSelect", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", Number)
    ], Calendar.prototype, "numberOfMonths", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", String)
    ], Calendar.prototype, "view", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", Boolean)
    ], Calendar.prototype, "touchUI", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", String)
    ], Calendar.prototype, "showTransitionOptions", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", String)
    ], Calendar.prototype, "hideTransitionOptions", void 0);
    __decorate([
        core_1.Output(),
        __metadata("design:type", core_1.EventEmitter)
    ], Calendar.prototype, "onFocus", void 0);
    __decorate([
        core_1.Output(),
        __metadata("design:type", core_1.EventEmitter)
    ], Calendar.prototype, "onBlur", void 0);
    __decorate([
        core_1.Output(),
        __metadata("design:type", core_1.EventEmitter)
    ], Calendar.prototype, "onClose", void 0);
    __decorate([
        core_1.Output(),
        __metadata("design:type", core_1.EventEmitter)
    ], Calendar.prototype, "onSelect", void 0);
    __decorate([
        core_1.Output(),
        __metadata("design:type", core_1.EventEmitter)
    ], Calendar.prototype, "onInput", void 0);
    __decorate([
        core_1.Output(),
        __metadata("design:type", core_1.EventEmitter)
    ], Calendar.prototype, "onTodayClick", void 0);
    __decorate([
        core_1.Output(),
        __metadata("design:type", core_1.EventEmitter)
    ], Calendar.prototype, "onClearClick", void 0);
    __decorate([
        core_1.Output(),
        __metadata("design:type", core_1.EventEmitter)
    ], Calendar.prototype, "onMonthChange", void 0);
    __decorate([
        core_1.Output(),
        __metadata("design:type", core_1.EventEmitter)
    ], Calendar.prototype, "onYearChange", void 0);
    __decorate([
        core_1.ContentChildren(shared_1.PrimeTemplate),
        __metadata("design:type", core_1.QueryList)
    ], Calendar.prototype, "templates", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", Number)
    ], Calendar.prototype, "tabindex", void 0);
    __decorate([
        core_1.ViewChild('inputfield'),
        __metadata("design:type", core_1.ElementRef)
    ], Calendar.prototype, "inputfieldViewChild", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", Boolean),
        __metadata("design:paramtypes", [Boolean])
    ], Calendar.prototype, "utc", null);
    __decorate([
        core_1.Input(),
        __metadata("design:type", Date),
        __metadata("design:paramtypes", [Date])
    ], Calendar.prototype, "minDate", null);
    __decorate([
        core_1.Input(),
        __metadata("design:type", Date),
        __metadata("design:paramtypes", [Date])
    ], Calendar.prototype, "maxDate", null);
    __decorate([
        core_1.Input(),
        __metadata("design:type", Array),
        __metadata("design:paramtypes", [Array])
    ], Calendar.prototype, "disabledDates", null);
    __decorate([
        core_1.Input(),
        __metadata("design:type", Array),
        __metadata("design:paramtypes", [Array])
    ], Calendar.prototype, "disabledDays", null);
    __decorate([
        core_1.Input(),
        __metadata("design:type", Boolean),
        __metadata("design:paramtypes", [Boolean])
    ], Calendar.prototype, "showTime", null);
    __decorate([
        core_1.Input(),
        __metadata("design:type", Object),
        __metadata("design:paramtypes", [Object])
    ], Calendar.prototype, "locale", null);
    Calendar = __decorate([
        core_1.Component({
            selector: 'p-calendar',
            template: "\n        <span [ngClass]=\"{'ui-calendar':true, 'ui-calendar-w-btn': showIcon, 'ui-calendar-timeonly': timeOnly}\" [ngStyle]=\"style\" [class]=\"styleClass\">\n            <ng-template [ngIf]=\"!inline\">\n                <input #inputfield type=\"text\" [attr.id]=\"inputId\" [attr.name]=\"name\" [attr.required]=\"required\" [value]=\"inputFieldValue\" (focus)=\"onInputFocus($event)\" (keydown)=\"onInputKeydown($event)\" (click)=\"onInputClick($event)\" (blur)=\"onInputBlur($event)\"\n                    [readonly]=\"readonlyInput\" (input)=\"onUserInput($event)\" [ngStyle]=\"inputStyle\" [class]=\"inputStyleClass\" [placeholder]=\"placeholder||''\" [disabled]=\"disabled\" [attr.tabindex]=\"tabindex\"\n                    [ngClass]=\"'ui-inputtext ui-widget ui-state-default ui-corner-all'\" autocomplete=\"off\"\n                    ><button type=\"button\" [icon]=\"icon\" pButton *ngIf=\"showIcon\" (click)=\"onButtonClick($event,inputfield)\" class=\"ui-datepicker-trigger ui-calendar-button\"\n                    [ngClass]=\"{'ui-state-disabled':disabled}\" [disabled]=\"disabled\" tabindex=\"-1\"></button>\n            </ng-template>\n            <div [class]=\"panelStyleClass\" [ngStyle]=\"panelStyle\" [ngClass]=\"{'ui-datepicker ui-widget ui-widget-content ui-helper-clearfix ui-corner-all': true, 'ui-datepicker-inline':inline,'ui-shadow':!inline,\n                'ui-state-disabled':disabled,'ui-datepicker-timeonly':timeOnly,'ui-datepicker-multiple-month': this.numberOfMonths > 1, 'ui-datepicker-monthpicker': (view === 'month'), 'ui-datepicker-touch-ui': touchUI}\"\n                (click)=\"onDatePickerClick($event)\" [@overlayAnimation]=\"touchUI ? {value: 'visibleTouchUI', params: {showTransitionParams: showTransitionOptions, hideTransitionParams: hideTransitionOptions}}: \n                                            {value: 'visible', params: {showTransitionParams: showTransitionOptions, hideTransitionParams: hideTransitionOptions}}\" \n                                            [@.disabled]=\"inline === true\" (@overlayAnimation.start)=\"onOverlayAnimationStart($event)\" *ngIf=\"inline || overlayVisible\">\n                <ng-container *ngIf=\"!timeOnly\">\n                    <div class=\"ui-datepicker-group ui-widget-content\" *ngFor=\"let month of months; let i = index;\">\n                        <div class=\"ui-datepicker-header ui-widget-header ui-helper-clearfix ui-corner-all\">\n                            <ng-content select=\"p-header\"></ng-content>\n                            <a class=\"ui-datepicker-prev ui-corner-all\" href=\"#\" (click)=\"navBackward($event)\" *ngIf=\"i === 0\">\n                                <span class=\"pi pi-chevron-left\"></span>\n                            </a>\n                            <a class=\"ui-datepicker-next ui-corner-all\" href=\"#\" (click)=\"navForward($event)\" *ngIf=\"numberOfMonths === 1 ? true : (i === numberOfMonths -1)\">\n                                <span class=\"pi pi-chevron-right\"></span>\n                            </a>\n                            <div class=\"ui-datepicker-title\">\n                                <span class=\"ui-datepicker-month\" *ngIf=\"!monthNavigator && (view !== 'month')\">{{locale.monthNames[month.month]}}</span>\n                                <select class=\"ui-datepicker-month\" *ngIf=\"monthNavigator && (view !== 'month') && numberOfMonths === 1\" (change)=\"onMonthDropdownChange($event.target.value)\">\n                                    <option [value]=\"i\" *ngFor=\"let monthName of locale.monthNames;let i = index\" [selected]=\"i === month.month\">{{monthName}}</option>\n                                </select>\n                                <select class=\"ui-datepicker-year\" *ngIf=\"yearNavigator && numberOfMonths === 1\" (change)=\"onYearDropdownChange($event.target.value)\">\n                                    <option [value]=\"year\" *ngFor=\"let year of yearOptions\" [selected]=\"year === currentYear\">{{year}}</option>\n                                </select>\n                                <span class=\"ui-datepicker-year\" *ngIf=\"!yearNavigator\">{{view === 'month' ? currentYear : month.year}}</span>\n                            </div>\n                        </div>\n                        <div class=\"ui-datepicker-calendar-container\" *ngIf=\"view ==='date'\">\n                            <table class=\"ui-datepicker-calendar\">\n                                <thead>\n                                    <tr>\n                                        <th scope=\"col\" *ngFor=\"let weekDay of weekDays;let begin = first; let end = last\">\n                                            <span>{{weekDay}}</span>\n                                        </th>\n                                    </tr>\n                                </thead>\n                                <tbody>\n                                    <tr *ngFor=\"let week of month.dates\">\n                                        <td *ngFor=\"let date of week\" [ngClass]=\"{'ui-datepicker-other-month': date.otherMonth,\n                                            'ui-datepicker-current-day':isSelected(date),'ui-datepicker-today':date.today}\">\n                                            <ng-container *ngIf=\"date.otherMonth ? showOtherMonths : true\">\n                                                <a class=\"ui-state-default\" *ngIf=\"date.selectable\" [ngClass]=\"{'ui-state-active':isSelected(date), 'ui-state-highlight':date.today}\"\n                                                    (click)=\"onDateSelect($event,date)\" draggable=\"false\">\n                                                    <ng-container *ngIf=\"!dateTemplate\">{{date.day}}</ng-container>\n                                                    <ng-container *ngTemplateOutlet=\"dateTemplate; context: {$implicit: date}\"></ng-container>\n                                                </a>\n                                                <span class=\"ui-state-default ui-state-disabled\" *ngIf=\"!date.selectable\">\n                                                    {{date.day}}\n                                                </span>\n                                            </ng-container>\n                                        </td>\n                                    </tr>\n                                </tbody>\n                            </table>\n                        </div>\n                    </div>\n                    <div class=\"ui-monthpicker\" *ngIf=\"view === 'month'\">\n                        <a href=\"#\" *ngFor=\"let m of monthPickerValues; let i = index\" (click)=\"onMonthSelect($event, i)\" class=\"ui-monthpicker-month\" [ngClass]=\"{'ui-state-active': isMonthSelected(i)}\">\n                            {{m}}\n                        </a>\n                    </div>\n                </ng-container>\n                <div class=\"ui-timepicker ui-widget-header ui-corner-all\" *ngIf=\"showTime||timeOnly\">\n                    <div class=\"ui-hour-picker\">\n                        <a href=\"#\" (click)=\"incrementHour($event)\">\n                            <span class=\"pi pi-chevron-up\"></span>\n                        </a>\n                        <span [ngStyle]=\"{'display': currentHour < 10 ? 'inline': 'none'}\">0</span><span>{{currentHour}}</span>\n                        <a href=\"#\" (click)=\"decrementHour($event)\">\n                            <span class=\"pi pi-chevron-down\"></span>\n                        </a>\n                    </div>\n                    <div class=\"ui-separator\">\n                        <a href=\"#\">\n                            <span class=\"pi pi-chevron-up\"></span>\n                        </a>\n                        <span>:</span>\n                        <a href=\"#\">\n                            <span class=\"pi pi-chevron-down\"></span>\n                        </a>\n                    </div>\n                    <div class=\"ui-minute-picker\">\n                        <a href=\"#\" (click)=\"incrementMinute($event)\">\n                            <span class=\"pi pi-chevron-up\"></span>\n                        </a>\n                        <span [ngStyle]=\"{'display': currentMinute < 10 ? 'inline': 'none'}\">0</span><span>{{currentMinute}}</span>\n                        <a href=\"#\" (click)=\"decrementMinute($event)\">\n                            <span class=\"pi pi-chevron-down\"></span>\n                        </a>\n                    </div>\n                    <div class=\"ui-separator\" *ngIf=\"showSeconds\">\n                        <a href=\"#\">\n                            <span class=\"pi pi-chevron-up\"></span>\n                        </a>\n                        <span>:</span>\n                        <a href=\"#\">\n                            <span class=\"pi pi-chevron-down\"></span>\n                        </a>\n                    </div>\n                    <div class=\"ui-second-picker\" *ngIf=\"showSeconds\">\n                        <a href=\"#\" (click)=\"incrementSecond($event)\">\n                            <span class=\"pi pi-chevron-up\"></span>\n                        </a>\n                        <span [ngStyle]=\"{'display': currentSecond < 10 ? 'inline': 'none'}\">0</span><span>{{currentSecond}}</span>\n                        <a href=\"#\" (click)=\"decrementSecond($event)\">\n                            <span class=\"pi pi-chevron-down\"></span>\n                        </a>\n                    </div>\n                    <div class=\"ui-ampm-picker\" *ngIf=\"hourFormat=='12'\">\n                        <a href=\"#\" (click)=\"toggleAMPM($event)\">\n                            <span class=\"pi pi-chevron-up\"></span>\n                        </a>\n                        <span>{{pm ? 'PM' : 'AM'}}</span>\n                        <a href=\"#\" (click)=\"toggleAMPM($event)\">\n                            <span class=\"pi pi-chevron-down\"></span>\n                        </a>\n                    </div>\n                </div>\n                <div class=\"ui-datepicker-buttonbar ui-widget-header\" *ngIf=\"showButtonBar\">\n                    <div class=\"ui-g\">\n                        <div class=\"ui-g-6\">\n                            <button type=\"button\" [label]=\"_locale.today\" (click)=\"onTodayButtonClick($event)\" pButton [ngClass]=\"[todayButtonStyleClass]\"></button>\n                        </div>\n                        <div class=\"ui-g-6\">\n                            <button type=\"button\" [label]=\"_locale.clear\" (click)=\"onClearButtonClick($event)\" pButton [ngClass]=\"[clearButtonStyleClass]\"></button>\n                        </div>\n                    </div>\n                </div>\n                <ng-content select=\"p-footer\"></ng-content>\n            </div>\n        </span>\n    ",
            animations: [
                animations_1.trigger('overlayAnimation', [
                    animations_1.state('visible', animations_1.style({
                        transform: 'translateY(0)',
                        opacity: 1
                    })),
                    animations_1.state('visibleTouchUI', animations_1.style({
                        transform: 'translate(-50%,-50%)',
                        opacity: 1
                    })),
                    animations_1.transition('void => visible', [
                        animations_1.style({ transform: 'translateY(5%)', opacity: 0 }),
                        animations_1.animate('{{showTransitionParams}}')
                    ]),
                    animations_1.transition('visible => void', [
                        animations_1.animate(('{{hideTransitionParams}}'), animations_1.style({
                            opacity: 0,
                            transform: 'translateY(5%)'
                        }))
                    ]),
                    animations_1.transition('void => visibleTouchUI', [
                        animations_1.style({ opacity: 0, transform: 'translate3d(-50%, -40%, 0) scale(0.9)' }),
                        animations_1.animate('{{showTransitionParams}}')
                    ]),
                    animations_1.transition('visibleTouchUI => void', [
                        animations_1.animate(('{{hideTransitionParams}}'), animations_1.style({
                            opacity: 0,
                            transform: 'translate3d(-50%, -40%, 0) scale(0.9)'
                        }))
                    ])
                ])
            ],
            host: {
                '[class.ui-inputwrapper-filled]': 'filled',
                '[class.ui-inputwrapper-focus]': 'focus'
            },
            providers: [domhandler_1.DomHandler, exports.CALENDAR_VALUE_ACCESSOR]
        }),
        __metadata("design:paramtypes", [core_1.ElementRef, domhandler_1.DomHandler, core_1.Renderer2, core_1.ChangeDetectorRef])
    ], Calendar);
    return Calendar;
}());
exports.Calendar = Calendar;
var CalendarModule = /** @class */ (function () {
    function CalendarModule() {
    }
    CalendarModule = __decorate([
        core_1.NgModule({
            imports: [common_1.CommonModule, button_1.ButtonModule, shared_1.SharedModule],
            exports: [Calendar, button_1.ButtonModule, shared_1.SharedModule],
            declarations: [Calendar]
        })
    ], CalendarModule);
    return CalendarModule;
}());
exports.CalendarModule = CalendarModule;
//# sourceMappingURL=calendar.js.map

/***/ }),

/***/ 812:
/***/ (function(module, exports, __webpack_require__) {

"use strict";

var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
Object.defineProperty(exports, "__esModule", { value: true });
var core_1 = __webpack_require__(1);
var domhandler_1 = __webpack_require__(798);
var common_1 = __webpack_require__(45);
var ButtonDirective = /** @class */ (function () {
    function ButtonDirective(el, domHandler) {
        this.el = el;
        this.domHandler = domHandler;
        this.iconPos = 'left';
        this.cornerStyleClass = 'ui-corner-all';
    }
    ButtonDirective.prototype.ngAfterViewInit = function () {
        this.domHandler.addMultipleClasses(this.el.nativeElement, this.getStyleClass());
        if (this.icon) {
            var iconElement = document.createElement("span");
            iconElement.setAttribute("aria-hidden", "true");
            var iconPosClass = (this.iconPos == 'right') ? 'ui-button-icon-right' : 'ui-button-icon-left';
            iconElement.className = iconPosClass + ' ui-clickable ' + this.icon;
            this.el.nativeElement.appendChild(iconElement);
        }
        var labelElement = document.createElement("span");
        labelElement.className = 'ui-button-text ui-clickable';
        labelElement.appendChild(document.createTextNode(this.label || 'ui-btn'));
        this.el.nativeElement.appendChild(labelElement);
        this.initialized = true;
    };
    ButtonDirective.prototype.getStyleClass = function () {
        var styleClass = 'ui-button ui-widget ui-state-default ' + this.cornerStyleClass;
        if (this.icon) {
            if (this.label != null && this.label != undefined) {
                if (this.iconPos == 'left')
                    styleClass = styleClass + ' ui-button-text-icon-left';
                else
                    styleClass = styleClass + ' ui-button-text-icon-right';
            }
            else {
                styleClass = styleClass + ' ui-button-icon-only';
            }
        }
        else {
            if (this.label) {
                styleClass = styleClass + ' ui-button-text-only';
            }
            else {
                styleClass = styleClass + ' ui-button-text-empty';
            }
        }
        return styleClass;
    };
    Object.defineProperty(ButtonDirective.prototype, "label", {
        get: function () {
            return this._label;
        },
        set: function (val) {
            this._label = val;
            if (this.initialized) {
                this.domHandler.findSingle(this.el.nativeElement, '.ui-button-text').textContent = this._label;
                if (!this.icon) {
                    if (this._label) {
                        this.domHandler.removeClass(this.el.nativeElement, 'ui-button-text-empty');
                        this.domHandler.addClass(this.el.nativeElement, 'ui-button-text-only');
                    }
                    else {
                        this.domHandler.addClass(this.el.nativeElement, 'ui-button-text-empty');
                        this.domHandler.removeClass(this.el.nativeElement, 'ui-button-text-only');
                    }
                }
            }
        },
        enumerable: true,
        configurable: true
    });
    Object.defineProperty(ButtonDirective.prototype, "icon", {
        get: function () {
            return this._icon;
        },
        set: function (val) {
            this._icon = val;
            if (this.initialized) {
                var iconPosClass = (this.iconPos == 'right') ? 'ui-button-icon-right' : 'ui-button-icon-left';
                this.domHandler.findSingle(this.el.nativeElement, '.ui-clickable').className =
                    iconPosClass + ' ui-clickable ' + this.icon;
            }
        },
        enumerable: true,
        configurable: true
    });
    ButtonDirective.prototype.ngOnDestroy = function () {
        while (this.el.nativeElement.hasChildNodes()) {
            this.el.nativeElement.removeChild(this.el.nativeElement.lastChild);
        }
        this.initialized = false;
    };
    __decorate([
        core_1.Input(),
        __metadata("design:type", String)
    ], ButtonDirective.prototype, "iconPos", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", String)
    ], ButtonDirective.prototype, "cornerStyleClass", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", String),
        __metadata("design:paramtypes", [String])
    ], ButtonDirective.prototype, "label", null);
    __decorate([
        core_1.Input(),
        __metadata("design:type", String),
        __metadata("design:paramtypes", [String])
    ], ButtonDirective.prototype, "icon", null);
    ButtonDirective = __decorate([
        core_1.Directive({
            selector: '[pButton]',
            providers: [domhandler_1.DomHandler]
        }),
        __metadata("design:paramtypes", [core_1.ElementRef, domhandler_1.DomHandler])
    ], ButtonDirective);
    return ButtonDirective;
}());
exports.ButtonDirective = ButtonDirective;
var Button = /** @class */ (function () {
    function Button() {
        this.iconPos = 'left';
        this.onClick = new core_1.EventEmitter();
        this.onFocus = new core_1.EventEmitter();
        this.onBlur = new core_1.EventEmitter();
    }
    __decorate([
        core_1.Input(),
        __metadata("design:type", String)
    ], Button.prototype, "type", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", String)
    ], Button.prototype, "iconPos", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", String)
    ], Button.prototype, "icon", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", String)
    ], Button.prototype, "label", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", Boolean)
    ], Button.prototype, "disabled", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", Object)
    ], Button.prototype, "style", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", String)
    ], Button.prototype, "styleClass", void 0);
    __decorate([
        core_1.Output(),
        __metadata("design:type", core_1.EventEmitter)
    ], Button.prototype, "onClick", void 0);
    __decorate([
        core_1.Output(),
        __metadata("design:type", core_1.EventEmitter)
    ], Button.prototype, "onFocus", void 0);
    __decorate([
        core_1.Output(),
        __metadata("design:type", core_1.EventEmitter)
    ], Button.prototype, "onBlur", void 0);
    Button = __decorate([
        core_1.Component({
            selector: 'p-button',
            template: "\n        <button [attr.type]=\"type\" [class]=\"styleClass\" [style]=\"style\" [disabled]=\"disabled\"\n            [ngClass]=\"{'ui-button ui-widget ui-state-default ui-corner-all':true,\n                        'ui-button-icon-only': (icon && !label),\n                        'ui-button-text-icon-left': (icon && label && iconPos === 'left'),\n                        'ui-button-text-icon-right': (icon && label && iconPos === 'right'),\n                        'ui-button-text-only': (!icon && label),\n                        'ui-button-text-empty': (!icon && !label),\n                        'ui-state-disabled': disabled}\"\n                        (click)=\"onClick.emit($event)\" (focus)=\"onFocus.emit($event)\" (blur)=\"onBlur.emit($event)\">\n            <ng-content></ng-content>\n            <span [ngClass]=\"{'ui-clickable': true,\n                        'ui-button-icon-left': (iconPos === 'left'), \n                        'ui-button-icon-right': (iconPos === 'right')}\"\n                        [class]=\"icon\" *ngIf=\"icon\"></span>\n            <span class=\"ui-button-text ui-clickable\">{{label||'ui-btn'}}</span>\n        </button>\n    "
        })
    ], Button);
    return Button;
}());
exports.Button = Button;
var ButtonModule = /** @class */ (function () {
    function ButtonModule() {
    }
    ButtonModule = __decorate([
        core_1.NgModule({
            imports: [common_1.CommonModule],
            exports: [ButtonDirective, Button],
            declarations: [ButtonDirective, Button]
        })
    ], ButtonModule);
    return ButtonModule;
}());
exports.ButtonModule = ButtonModule;
//# sourceMappingURL=button.js.map

/***/ }),

/***/ 822:
/***/ (function(module, exports, __webpack_require__) {

"use strict";
/* Shorthand */

function __export(m) {
    for (var p in m) if (!exports.hasOwnProperty(p)) exports[p] = m[p];
}
Object.defineProperty(exports, "__esModule", { value: true });
__export(__webpack_require__(823));

/***/ }),

/***/ 823:
/***/ (function(module, exports, __webpack_require__) {

"use strict";

var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
Object.defineProperty(exports, "__esModule", { value: true });
var core_1 = __webpack_require__(1);
var animations_1 = __webpack_require__(159);
var common_1 = __webpack_require__(45);
var shared_1 = __webpack_require__(800);
var domhandler_1 = __webpack_require__(798);
var objectutils_1 = __webpack_require__(824);
var forms_1 = __webpack_require__(22);
exports.DROPDOWN_VALUE_ACCESSOR = {
    provide: forms_1.NG_VALUE_ACCESSOR,
    useExisting: core_1.forwardRef(function () { return Dropdown; }),
    multi: true
};
var Dropdown = /** @class */ (function () {
    function Dropdown(el, domHandler, renderer, cd, objectUtils, zone) {
        this.el = el;
        this.domHandler = domHandler;
        this.renderer = renderer;
        this.cd = cd;
        this.objectUtils = objectUtils;
        this.zone = zone;
        this.scrollHeight = '200px';
        this.autoWidth = true;
        this.filterBy = 'label';
        this.resetFilterOnHide = false;
        this.dropdownIcon = 'pi pi-caret-down';
        this.autoDisplayFirst = true;
        this.emptyFilterMessage = 'No results found';
        this.autoZIndex = true;
        this.baseZIndex = 0;
        this.showTransitionOptions = '225ms ease-out';
        this.hideTransitionOptions = '195ms ease-in';
        this.onChange = new core_1.EventEmitter();
        this.onFocus = new core_1.EventEmitter();
        this.onBlur = new core_1.EventEmitter();
        this.onClick = new core_1.EventEmitter();
        this.onShow = new core_1.EventEmitter();
        this.onHide = new core_1.EventEmitter();
        this.onModelChange = function () { };
        this.onModelTouched = function () { };
    }
    Dropdown.prototype.ngAfterContentInit = function () {
        var _this = this;
        this.templates.forEach(function (item) {
            switch (item.getType()) {
                case 'item':
                    _this.itemTemplate = item.template;
                    break;
                case 'selectedItem':
                    _this.selectedItemTemplate = item.template;
                    break;
                case 'group':
                    _this.groupTemplate = item.template;
                    break;
                default:
                    _this.itemTemplate = item.template;
                    break;
            }
        });
    };
    Dropdown.prototype.ngOnInit = function () {
        this.optionsToDisplay = this.options;
        this.updateSelectedOption(null);
    };
    Object.defineProperty(Dropdown.prototype, "options", {
        get: function () {
            return this._options;
        },
        set: function (val) {
            var opts = this.optionLabel ? this.objectUtils.generateSelectItems(val, this.optionLabel) : val;
            this._options = opts;
            this.optionsToDisplay = this._options;
            this.updateSelectedOption(this.value);
            this.optionsChanged = true;
            if (this.filterValue && this.filterValue.length) {
                this.activateFilter();
            }
        },
        enumerable: true,
        configurable: true
    });
    Dropdown.prototype.ngAfterViewInit = function () {
        if (this.editable) {
            this.updateEditableLabel();
        }
        this.updateDimensions();
    };
    Object.defineProperty(Dropdown.prototype, "label", {
        get: function () {
            return (this.selectedOption ? this.selectedOption.label : null);
        },
        enumerable: true,
        configurable: true
    });
    Dropdown.prototype.updateEditableLabel = function () {
        if (this.editableInputViewChild && this.editableInputViewChild.nativeElement) {
            this.editableInputViewChild.nativeElement.value = (this.selectedOption ? this.selectedOption.label : this.value || '');
        }
    };
    Dropdown.prototype.onItemClick = function (event, option) {
        var _this = this;
        this.itemClick = true;
        if (!option.disabled) {
            this.selectItem(event, option);
            this.focusViewChild.nativeElement.focus();
            this.filled = true;
        }
        setTimeout(function () {
            _this.hide();
        }, 150);
    };
    Dropdown.prototype.selectItem = function (event, option) {
        if (this.selectedOption != option) {
            this.selectedOption = option;
            this.value = option.value;
            this.onModelChange(this.value);
            this.updateEditableLabel();
            this.onChange.emit({
                originalEvent: event,
                value: this.value
            });
        }
    };
    Dropdown.prototype.ngAfterViewChecked = function () {
        var _this = this;
        if (this.autoWidth && !this.dimensionsUpdated) {
            this.updateDimensions();
        }
        if (this.optionsChanged && this.overlayVisible) {
            this.optionsChanged = false;
            this.zone.runOutsideAngular(function () {
                setTimeout(function () {
                    _this.updateDimensions();
                    _this.alignOverlay();
                }, 1);
            });
        }
        if (this.selectedOptionUpdated && this.itemsWrapper) {
            this.updateDimensions();
            var selectedItem = this.domHandler.findSingle(this.overlay, 'li.ui-state-highlight');
            if (selectedItem) {
                this.domHandler.scrollInView(this.itemsWrapper, this.domHandler.findSingle(this.overlay, 'li.ui-state-highlight'));
            }
            this.selectedOptionUpdated = false;
        }
    };
    Dropdown.prototype.writeValue = function (value) {
        if (this.filter) {
            this.resetFilter();
        }
        this.value = value;
        this.updateSelectedOption(value);
        this.updateEditableLabel();
        this.updateFilledState();
        this.cd.markForCheck();
    };
    Dropdown.prototype.resetFilter = function () {
        if (this.filterViewChild && this.filterViewChild.nativeElement) {
            this.filterValue = null;
            this.filterViewChild.nativeElement.value = '';
        }
        this.optionsToDisplay = this.options;
    };
    Dropdown.prototype.updateSelectedOption = function (val) {
        this.selectedOption = this.findOption(val, this.optionsToDisplay);
        if (this.autoDisplayFirst && !this.placeholder && !this.selectedOption && this.optionsToDisplay && this.optionsToDisplay.length && !this.editable) {
            this.selectedOption = this.optionsToDisplay[0];
        }
        this.selectedOptionUpdated = true;
    };
    Dropdown.prototype.registerOnChange = function (fn) {
        this.onModelChange = fn;
    };
    Dropdown.prototype.registerOnTouched = function (fn) {
        this.onModelTouched = fn;
    };
    Dropdown.prototype.setDisabledState = function (val) {
        this.disabled = val;
    };
    Dropdown.prototype.updateDimensions = function () {
        if (this.autoWidth && this.el.nativeElement && this.el.nativeElement.children[0] && this.el.nativeElement.offsetParent) {
            var select = this.domHandler.findSingle(this.el.nativeElement, 'select');
            if (select && !this.style || (this.style && (!this.style['width'] && !this.style['min-width']))) {
                this.el.nativeElement.children[0].style.width = select.offsetWidth + 30 + 'px';
            }
            this.dimensionsUpdated = true;
        }
    };
    Dropdown.prototype.onMouseclick = function (event) {
        var _this = this;
        if (this.disabled || this.readonly) {
            return;
        }
        this.onClick.emit(event);
        this.selfClick = true;
        this.clearClick = this.domHandler.hasClass(event.target, 'ui-dropdown-clear-icon');
        if (!this.itemClick && !this.clearClick) {
            this.focusViewChild.nativeElement.focus();
            if (this.overlayVisible) {
                this.hide();
            }
            else {
                this.show();
                setTimeout(function () {
                    if (_this.filterViewChild != undefined) {
                        _this.filterViewChild.nativeElement.focus();
                    }
                }, 200);
            }
        }
    };
    Dropdown.prototype.onEditableInputClick = function (event) {
        this.itemClick = true;
        this.bindDocumentClickListener();
    };
    Dropdown.prototype.onEditableInputFocus = function (event) {
        this.focused = true;
        this.hide();
        this.onFocus.emit(event);
    };
    Dropdown.prototype.onEditableInputChange = function (event) {
        this.value = event.target.value;
        this.updateSelectedOption(this.value);
        this.onModelChange(this.value);
        this.onChange.emit({
            originalEvent: event,
            value: this.value
        });
    };
    Dropdown.prototype.show = function () {
        this.overlayVisible = true;
    };
    Dropdown.prototype.onOverlayAnimationStart = function (event) {
        switch (event.toState) {
            case 'visible':
                this.overlay = event.element;
                this.itemsWrapper = this.domHandler.findSingle(this.overlay, '.ui-dropdown-items-wrapper');
                this.appendOverlay();
                if (this.autoZIndex) {
                    this.overlay.style.zIndex = String(this.baseZIndex + (++domhandler_1.DomHandler.zindex));
                }
                this.alignOverlay();
                this.bindDocumentClickListener();
                if (this.options && this.options.length) {
                    var selectedListItem = this.domHandler.findSingle(this.itemsWrapper, '.ui-dropdown-item.ui-state-highlight');
                    if (selectedListItem) {
                        this.domHandler.scrollInView(this.itemsWrapper, selectedListItem);
                    }
                }
                this.onShow.emit(event);
                break;
            case 'void':
                this.onHide.emit(event);
                this.onOverlayHide();
                break;
        }
    };
    Dropdown.prototype.appendOverlay = function () {
        if (this.appendTo) {
            if (this.appendTo === 'body')
                document.body.appendChild(this.overlay);
            else
                this.domHandler.appendChild(this.overlay, this.appendTo);
            this.overlay.style.minWidth = this.domHandler.getWidth(this.containerViewChild.nativeElement) + 'px';
        }
    };
    Dropdown.prototype.restoreOverlayAppend = function () {
        if (this.overlay && this.appendTo) {
            this.el.nativeElement.appendChild(this.overlay);
        }
    };
    Dropdown.prototype.hide = function () {
        this.overlayVisible = false;
        if (this.filter && this.resetFilterOnHide) {
            this.resetFilter();
        }
        this.cd.markForCheck();
    };
    Dropdown.prototype.alignOverlay = function () {
        if (this.overlay) {
            if (this.appendTo)
                this.domHandler.absolutePosition(this.overlay, this.containerViewChild.nativeElement);
            else
                this.domHandler.relativePosition(this.overlay, this.containerViewChild.nativeElement);
        }
    };
    Dropdown.prototype.onInputFocus = function (event) {
        this.focused = true;
        this.onFocus.emit(event);
    };
    Dropdown.prototype.onInputBlur = function (event) {
        this.focused = false;
        this.onModelTouched();
        this.onBlur.emit(event);
    };
    Dropdown.prototype.findPrevEnabledOption = function (index) {
        var prevEnabledOption;
        if (this.optionsToDisplay && this.optionsToDisplay.length) {
            for (var i = (index - 1); 0 <= i; i--) {
                var option = this.optionsToDisplay[i];
                if (option.disabled) {
                    continue;
                }
                else {
                    prevEnabledOption = option;
                    break;
                }
            }
            if (!prevEnabledOption) {
                for (var i = this.optionsToDisplay.length - 1; i >= index; i--) {
                    var option = this.optionsToDisplay[i];
                    if (option.disabled) {
                        continue;
                    }
                    else {
                        prevEnabledOption = option;
                        break;
                    }
                }
            }
        }
        return prevEnabledOption;
    };
    Dropdown.prototype.findNextEnabledOption = function (index) {
        var nextEnabledOption;
        if (this.optionsToDisplay && this.optionsToDisplay.length) {
            for (var i = (index + 1); index < (this.optionsToDisplay.length - 1); i++) {
                var option = this.optionsToDisplay[i];
                if (option.disabled) {
                    continue;
                }
                else {
                    nextEnabledOption = option;
                    break;
                }
            }
            if (!nextEnabledOption) {
                for (var i = 0; i < index; i++) {
                    var option = this.optionsToDisplay[i];
                    if (option.disabled) {
                        continue;
                    }
                    else {
                        nextEnabledOption = option;
                        break;
                    }
                }
            }
        }
        return nextEnabledOption;
    };
    Dropdown.prototype.onKeydown = function (event, search) {
        if (this.readonly || !this.optionsToDisplay || this.optionsToDisplay.length === null) {
            return;
        }
        switch (event.which) {
            //down
            case 40:
                if (!this.overlayVisible && event.altKey) {
                    this.show();
                }
                else {
                    if (this.group) {
                        var selectedItemIndex = this.selectedOption ? this.findOptionGroupIndex(this.selectedOption.value, this.optionsToDisplay) : -1;
                        if (selectedItemIndex !== -1) {
                            var nextItemIndex = selectedItemIndex.itemIndex + 1;
                            if (nextItemIndex < (this.optionsToDisplay[selectedItemIndex.groupIndex].items.length)) {
                                this.selectItem(event, this.optionsToDisplay[selectedItemIndex.groupIndex].items[nextItemIndex]);
                                this.selectedOptionUpdated = true;
                            }
                            else if (this.optionsToDisplay[selectedItemIndex.groupIndex + 1]) {
                                this.selectItem(event, this.optionsToDisplay[selectedItemIndex.groupIndex + 1].items[0]);
                                this.selectedOptionUpdated = true;
                            }
                        }
                        else {
                            this.selectItem(event, this.optionsToDisplay[0].items[0]);
                        }
                    }
                    else {
                        var selectedItemIndex = this.selectedOption ? this.findOptionIndex(this.selectedOption.value, this.optionsToDisplay) : -1;
                        var nextEnabledOption = this.findNextEnabledOption(selectedItemIndex);
                        if (nextEnabledOption) {
                            this.selectItem(event, nextEnabledOption);
                            this.selectedOptionUpdated = true;
                        }
                    }
                }
                event.preventDefault();
                break;
            //up
            case 38:
                if (this.group) {
                    var selectedItemIndex = this.selectedOption ? this.findOptionGroupIndex(this.selectedOption.value, this.optionsToDisplay) : -1;
                    if (selectedItemIndex !== -1) {
                        var prevItemIndex = selectedItemIndex.itemIndex - 1;
                        if (prevItemIndex >= 0) {
                            this.selectItem(event, this.optionsToDisplay[selectedItemIndex.groupIndex].items[prevItemIndex]);
                            this.selectedOptionUpdated = true;
                        }
                        else if (prevItemIndex < 0) {
                            var prevGroup = this.optionsToDisplay[selectedItemIndex.groupIndex - 1];
                            if (prevGroup) {
                                this.selectItem(event, prevGroup.items[prevGroup.items.length - 1]);
                                this.selectedOptionUpdated = true;
                            }
                        }
                    }
                }
                else {
                    var selectedItemIndex = this.selectedOption ? this.findOptionIndex(this.selectedOption.value, this.optionsToDisplay) : -1;
                    var prevEnabledOption = this.findPrevEnabledOption(selectedItemIndex);
                    if (prevEnabledOption) {
                        this.selectItem(event, prevEnabledOption);
                        this.selectedOptionUpdated = true;
                    }
                }
                event.preventDefault();
                break;
            //space
            case 32:
            case 32:
                if (!this.overlayVisible) {
                    this.show();
                    event.preventDefault();
                }
                break;
            //enter
            case 13:
                if (!this.filter || (this.optionsToDisplay && this.optionsToDisplay.length > 0)) {
                    this.hide();
                }
                event.preventDefault();
                break;
            //escape and tab
            case 27:
            case 9:
                this.hide();
                break;
            //search item based on keyboard input
            default:
                if (search) {
                    this.search(event);
                }
                break;
        }
    };
    Dropdown.prototype.search = function (event) {
        var _this = this;
        if (this.searchTimeout) {
            clearTimeout(this.searchTimeout);
        }
        var char = String.fromCharCode(event.keyCode);
        this.previousSearchChar = this.currentSearchChar;
        this.currentSearchChar = char;
        if (this.previousSearchChar === this.currentSearchChar)
            this.searchValue = this.currentSearchChar;
        else
            this.searchValue = this.searchValue ? this.searchValue + char : char;
        var newOption;
        if (this.group) {
            var searchIndex = this.selectedOption ? this.findOptionGroupIndex(this.selectedOption.value, this.optionsToDisplay) : { groupIndex: 0, itemIndex: 0 };
            newOption = this.searchOptionWithinGroup(searchIndex);
        }
        else {
            var searchIndex = this.selectedOption ? this.findOptionIndex(this.selectedOption.value, this.optionsToDisplay) : -1;
            newOption = this.searchOption(++searchIndex);
        }
        if (newOption) {
            this.selectItem(event, newOption);
            this.selectedOptionUpdated = true;
        }
        this.searchTimeout = setTimeout(function () {
            _this.searchValue = null;
        }, 250);
    };
    Dropdown.prototype.searchOption = function (index) {
        var option;
        if (this.searchValue) {
            option = this.searchOptionInRange(index, this.optionsToDisplay.length);
            if (!option) {
                option = this.searchOptionInRange(0, index);
            }
        }
        return option;
    };
    Dropdown.prototype.searchOptionInRange = function (start, end) {
        for (var i = start; i < end; i++) {
            var opt = this.optionsToDisplay[i];
            if (opt.label.toLowerCase().startsWith(this.searchValue.toLowerCase())) {
                return opt;
            }
        }
        return null;
    };
    Dropdown.prototype.searchOptionWithinGroup = function (index) {
        var option;
        if (this.searchValue) {
            for (var i = index.groupIndex; i < this.optionsToDisplay.length; i++) {
                for (var j = (index.groupIndex === i) ? (index.itemIndex + 1) : 0; j < this.optionsToDisplay[i].items.length; j++) {
                    var opt = this.optionsToDisplay[i].items[j];
                    if (opt.label.toLowerCase().startsWith(this.searchValue.toLowerCase())) {
                        return opt;
                    }
                }
            }
            if (!option) {
                for (var i = 0; i <= index.groupIndex; i++) {
                    for (var j = 0; j < ((index.groupIndex === i) ? index.itemIndex : this.optionsToDisplay[i].items.length); j++) {
                        var opt = this.optionsToDisplay[i].items[j];
                        if (opt.label.toLowerCase().startsWith(this.searchValue.toLowerCase())) {
                            return opt;
                        }
                    }
                }
            }
        }
        return null;
    };
    Dropdown.prototype.findOptionIndex = function (val, opts) {
        var index = -1;
        if (opts) {
            for (var i = 0; i < opts.length; i++) {
                if ((val == null && opts[i].value == null) || this.objectUtils.equals(val, opts[i].value, this.dataKey)) {
                    index = i;
                    break;
                }
            }
        }
        return index;
    };
    Dropdown.prototype.findOptionGroupIndex = function (val, opts) {
        var groupIndex, itemIndex;
        if (opts) {
            for (var i = 0; i < opts.length; i++) {
                groupIndex = i;
                itemIndex = this.findOptionIndex(val, opts[i].items);
                if (itemIndex !== -1) {
                    break;
                }
            }
        }
        if (itemIndex !== -1) {
            return { groupIndex: groupIndex, itemIndex: itemIndex };
        }
        else {
            return -1;
        }
    };
    Dropdown.prototype.findOption = function (val, opts, inGroup) {
        if (this.group && !inGroup) {
            var opt = void 0;
            if (opts && opts.length) {
                for (var _i = 0, opts_1 = opts; _i < opts_1.length; _i++) {
                    var optgroup = opts_1[_i];
                    opt = this.findOption(val, optgroup.items, true);
                    if (opt) {
                        break;
                    }
                }
            }
            return opt;
        }
        else {
            var index = this.findOptionIndex(val, opts);
            return (index != -1) ? opts[index] : null;
        }
    };
    Dropdown.prototype.onFilter = function (event) {
        var inputValue = event.target.value.toLowerCase();
        if (inputValue && inputValue.length) {
            this.filterValue = inputValue;
            this.activateFilter();
        }
        else {
            this.filterValue = null;
            this.optionsToDisplay = this.options;
        }
        this.optionsChanged = true;
    };
    Dropdown.prototype.activateFilter = function () {
        var searchFields = this.filterBy.split(',');
        if (this.options && this.options.length) {
            if (this.group) {
                var filteredGroups = [];
                for (var _i = 0, _a = this.options; _i < _a.length; _i++) {
                    var optgroup = _a[_i];
                    var filteredSubOptions = this.objectUtils.filter(optgroup.items, searchFields, this.filterValue);
                    if (filteredSubOptions && filteredSubOptions.length) {
                        filteredGroups.push({
                            label: optgroup.label,
                            value: optgroup.value,
                            items: filteredSubOptions
                        });
                    }
                }
                this.optionsToDisplay = filteredGroups;
            }
            else {
                this.optionsToDisplay = this.objectUtils.filter(this.options, searchFields, this.filterValue);
            }
            this.optionsChanged = true;
        }
    };
    Dropdown.prototype.applyFocus = function () {
        if (this.editable)
            this.domHandler.findSingle(this.el.nativeElement, '.ui-dropdown-label.ui-inputtext').focus();
        else
            this.domHandler.findSingle(this.el.nativeElement, 'input[readonly]').focus();
    };
    Dropdown.prototype.focus = function () {
        this.applyFocus();
    };
    Dropdown.prototype.bindDocumentClickListener = function () {
        var _this = this;
        if (!this.documentClickListener) {
            this.documentClickListener = this.renderer.listen('document', 'click', function () {
                if (!_this.selfClick && !_this.itemClick) {
                    _this.hide();
                    _this.unbindDocumentClickListener();
                }
                _this.clearClickState();
                _this.cd.markForCheck();
            });
        }
    };
    Dropdown.prototype.clearClickState = function () {
        this.selfClick = false;
        this.itemClick = false;
    };
    Dropdown.prototype.unbindDocumentClickListener = function () {
        if (this.documentClickListener) {
            this.documentClickListener();
            this.documentClickListener = null;
        }
    };
    Dropdown.prototype.updateFilledState = function () {
        this.filled = (this.selectedOption != null);
    };
    Dropdown.prototype.clear = function (event) {
        this.clearClick = true;
        this.value = null;
        this.onModelChange(this.value);
        this.onChange.emit({
            originalEvent: event,
            value: this.value
        });
        this.updateSelectedOption(this.value);
        this.updateEditableLabel();
        this.updateFilledState();
    };
    Dropdown.prototype.onOverlayHide = function () {
        this.unbindDocumentClickListener();
        this.overlay = null;
        this.itemsWrapper = null;
    };
    Dropdown.prototype.ngOnDestroy = function () {
        this.restoreOverlayAppend();
        this.onOverlayHide();
    };
    __decorate([
        core_1.Input(),
        __metadata("design:type", String)
    ], Dropdown.prototype, "scrollHeight", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", Boolean)
    ], Dropdown.prototype, "filter", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", String)
    ], Dropdown.prototype, "name", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", Object)
    ], Dropdown.prototype, "style", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", Object)
    ], Dropdown.prototype, "panelStyle", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", String)
    ], Dropdown.prototype, "styleClass", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", String)
    ], Dropdown.prototype, "panelStyleClass", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", Boolean)
    ], Dropdown.prototype, "disabled", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", Boolean)
    ], Dropdown.prototype, "readonly", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", Boolean)
    ], Dropdown.prototype, "autoWidth", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", Boolean)
    ], Dropdown.prototype, "required", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", Boolean)
    ], Dropdown.prototype, "editable", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", Object)
    ], Dropdown.prototype, "appendTo", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", Number)
    ], Dropdown.prototype, "tabindex", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", String)
    ], Dropdown.prototype, "placeholder", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", String)
    ], Dropdown.prototype, "filterPlaceholder", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", String)
    ], Dropdown.prototype, "inputId", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", String)
    ], Dropdown.prototype, "selectId", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", String)
    ], Dropdown.prototype, "dataKey", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", String)
    ], Dropdown.prototype, "filterBy", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", Boolean)
    ], Dropdown.prototype, "autofocus", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", Boolean)
    ], Dropdown.prototype, "resetFilterOnHide", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", String)
    ], Dropdown.prototype, "dropdownIcon", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", String)
    ], Dropdown.prototype, "optionLabel", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", Boolean)
    ], Dropdown.prototype, "autoDisplayFirst", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", Boolean)
    ], Dropdown.prototype, "group", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", Boolean)
    ], Dropdown.prototype, "showClear", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", String)
    ], Dropdown.prototype, "emptyFilterMessage", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", Boolean)
    ], Dropdown.prototype, "autoZIndex", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", Number)
    ], Dropdown.prototype, "baseZIndex", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", String)
    ], Dropdown.prototype, "showTransitionOptions", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", String)
    ], Dropdown.prototype, "hideTransitionOptions", void 0);
    __decorate([
        core_1.Output(),
        __metadata("design:type", core_1.EventEmitter)
    ], Dropdown.prototype, "onChange", void 0);
    __decorate([
        core_1.Output(),
        __metadata("design:type", core_1.EventEmitter)
    ], Dropdown.prototype, "onFocus", void 0);
    __decorate([
        core_1.Output(),
        __metadata("design:type", core_1.EventEmitter)
    ], Dropdown.prototype, "onBlur", void 0);
    __decorate([
        core_1.Output(),
        __metadata("design:type", core_1.EventEmitter)
    ], Dropdown.prototype, "onClick", void 0);
    __decorate([
        core_1.Output(),
        __metadata("design:type", core_1.EventEmitter)
    ], Dropdown.prototype, "onShow", void 0);
    __decorate([
        core_1.Output(),
        __metadata("design:type", core_1.EventEmitter)
    ], Dropdown.prototype, "onHide", void 0);
    __decorate([
        core_1.ViewChild('container'),
        __metadata("design:type", core_1.ElementRef)
    ], Dropdown.prototype, "containerViewChild", void 0);
    __decorate([
        core_1.ViewChild('filter'),
        __metadata("design:type", core_1.ElementRef)
    ], Dropdown.prototype, "filterViewChild", void 0);
    __decorate([
        core_1.ViewChild('in'),
        __metadata("design:type", core_1.ElementRef)
    ], Dropdown.prototype, "focusViewChild", void 0);
    __decorate([
        core_1.ViewChild('editableInput'),
        __metadata("design:type", core_1.ElementRef)
    ], Dropdown.prototype, "editableInputViewChild", void 0);
    __decorate([
        core_1.ContentChildren(shared_1.PrimeTemplate),
        __metadata("design:type", core_1.QueryList)
    ], Dropdown.prototype, "templates", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", Array),
        __metadata("design:paramtypes", [Array])
    ], Dropdown.prototype, "options", null);
    Dropdown = __decorate([
        core_1.Component({
            selector: 'p-dropdown',
            template: "\n         <div #container [ngClass]=\"{'ui-dropdown ui-widget ui-state-default ui-corner-all ui-helper-clearfix':true,\n            'ui-state-disabled':disabled, 'ui-dropdown-open':overlayVisible, 'ui-state-focus':focused, 'ui-dropdown-clearable': showClear && !disabled}\"\n            (click)=\"onMouseclick($event)\" [ngStyle]=\"style\" [class]=\"styleClass\">\n            <div class=\"ui-helper-hidden-accessible\" *ngIf=\"autoWidth\">\n                <select [attr.id]=\"selectId\" [required]=\"required\" [attr.name]=\"name\" [attr.aria-label]=\"selectedOption ? selectedOption.label : ' '\" tabindex=\"-1\" aria-hidden=\"true\">\n                    <option *ngIf=\"placeholder\">{{placeholder}}</option>\n                    <ng-container *ngIf=\"group\">\n                        <optgroup *ngFor=\"let option of options\" [attr.label]=\"option.label\">\n                            <option *ngFor=\"let option of option.items\" [value]=\"option.value\" [selected]=\"selectedOption == option\">{{option.label}}</option>\n                        <optgroup>\n                    </ng-container>\n                    <ng-container *ngIf=\"!group\">\n                        <option *ngFor=\"let option of options\" [value]=\"option.value\" [selected]=\"selectedOption == option\">{{option.label}}</option>\n                    </ng-container>\n                </select>\n            </div>\n            <div class=\"ui-helper-hidden-accessible\">\n                <input #in [attr.id]=\"inputId\" type=\"text\" [attr.aria-label]=\"selectedOption ? selectedOption.label : ' '\" readonly (focus)=\"onInputFocus($event)\" role=\"listbox\"\n                    (blur)=\"onInputBlur($event)\" (keydown)=\"onKeydown($event, true)\" [disabled]=\"disabled\" [attr.tabindex]=\"tabindex\" [attr.autofocus]=\"autofocus\">\n            </div>\n            <label [ngClass]=\"{'ui-dropdown-label ui-inputtext ui-corner-all':true,'ui-dropdown-label-empty':(label == null || label.length === 0)}\" *ngIf=\"!editable && (label != null)\">\n                <ng-container *ngIf=\"!selectedItemTemplate\">{{label||'empty'}}</ng-container>\n                <ng-container *ngTemplateOutlet=\"selectedItemTemplate; context: {$implicit: selectedOption}\"></ng-container>\n            </label>\n            <label [ngClass]=\"{'ui-dropdown-label ui-inputtext ui-corner-all ui-placeholder':true,'ui-dropdown-label-empty': (placeholder == null || placeholder.length === 0)}\" *ngIf=\"!editable && (label == null)\">{{placeholder||'empty'}}</label>\n            <input #editableInput type=\"text\" [attr.aria-label]=\"selectedOption ? selectedOption.label : ' '\" class=\"ui-dropdown-label ui-inputtext ui-corner-all\" *ngIf=\"editable\" [disabled]=\"disabled\" [attr.placeholder]=\"placeholder\"\n                        (click)=\"onEditableInputClick($event)\" (input)=\"onEditableInputChange($event)\" (focus)=\"onEditableInputFocus($event)\" (blur)=\"onInputBlur($event)\">\n            <i class=\"ui-dropdown-clear-icon pi pi-times\" (click)=\"clear($event)\" *ngIf=\"value != null && showClear && !disabled\"></i>\n            <div class=\"ui-dropdown-trigger ui-state-default ui-corner-right\">\n                <span class=\"ui-dropdown-trigger-icon ui-clickable\" [ngClass]=\"dropdownIcon\"></span>\n            </div>\n            <div *ngIf=\"overlayVisible\" [ngClass]=\"'ui-dropdown-panel  ui-widget ui-widget-content ui-corner-all ui-shadow'\" [@overlayAnimation]=\"{value: 'visible', params: {showTransitionParams: showTransitionOptions, hideTransitionParams: hideTransitionOptions}}\" (@overlayAnimation.start)=\"onOverlayAnimationStart($event)\" [ngStyle]=\"panelStyle\" [class]=\"panelStyleClass\">\n                <div *ngIf=\"filter\" class=\"ui-dropdown-filter-container\" (input)=\"onFilter($event)\" (click)=\"$event.stopPropagation()\">\n                    <input #filter type=\"text\" autocomplete=\"off\" [value]=\"filterValue||''\" class=\"ui-dropdown-filter ui-inputtext ui-widget ui-state-default ui-corner-all\" [attr.placeholder]=\"filterPlaceholder\"\n                    (keydown.enter)=\"$event.preventDefault()\" (keydown)=\"onKeydown($event, false)\">\n                    <span class=\"ui-dropdown-filter-icon pi pi-search\"></span>\n                </div>\n                <div class=\"ui-dropdown-items-wrapper\" [style.max-height]=\"scrollHeight||'auto'\">\n                    <ul class=\"ui-dropdown-items ui-dropdown-list ui-widget-content ui-widget ui-corner-all ui-helper-reset\">\n                        <ng-container *ngIf=\"group\">\n                            <ng-template ngFor let-optgroup [ngForOf]=\"optionsToDisplay\">\n                                <li class=\"ui-dropdown-item-group\">\n                                    <span *ngIf=\"!groupTemplate\">{{optgroup.label||'empty'}}</span>\n                                    <ng-container *ngTemplateOutlet=\"groupTemplate; context: {$implicit: optgroup}\"></ng-container>\n                                </li>\n                                <ng-container *ngTemplateOutlet=\"itemslist; context: {$implicit: optgroup.items, selectedOption: selectedOption}\"></ng-container>\n                            </ng-template>\n                        </ng-container>\n                        <ng-container *ngIf=\"!group\">\n                            <ng-container *ngTemplateOutlet=\"itemslist; context: {$implicit: optionsToDisplay, selectedOption: selectedOption}\"></ng-container>\n                        </ng-container>\n                        <ng-template #itemslist let-options let-selectedOption=\"selectedOption\">\n                            <li *ngFor=\"let option of options;let i=index\"  (click)=\"onItemClick($event, option)\"\n                                    [ngClass]=\"{'ui-dropdown-item ui-corner-all':true,\n                                                'ui-state-highlight':(selectedOption == option),\n                                                'ui-state-disabled':(option.disabled),\n                                                'ui-dropdown-item-empty':!option.label||option.label.length === 0}\">\n                                <span *ngIf=\"!itemTemplate\">{{option.label||'empty'}}</span>\n                                <ng-container *ngTemplateOutlet=\"itemTemplate; context: {$implicit: option}\"></ng-container>\n                            </li>\n                        </ng-template>\n                        <li *ngIf=\"filter && optionsToDisplay && optionsToDisplay.length === 0\">{{emptyFilterMessage}}</li>\n                    </ul>\n                </div>\n            </div>\n        </div>\n    ",
            animations: [
                animations_1.trigger('overlayAnimation', [
                    animations_1.state('void', animations_1.style({
                        transform: 'translateY(5%)',
                        opacity: 0
                    })),
                    animations_1.state('visible', animations_1.style({
                        transform: 'translateY(0)',
                        opacity: 1
                    })),
                    animations_1.transition('void => visible', animations_1.animate('{{showTransitionParams}}')),
                    animations_1.transition('visible => void', animations_1.animate('{{hideTransitionParams}}'))
                ])
            ],
            host: {
                '[class.ui-inputwrapper-filled]': 'filled',
                '[class.ui-inputwrapper-focus]': 'focused'
            },
            providers: [domhandler_1.DomHandler, objectutils_1.ObjectUtils, exports.DROPDOWN_VALUE_ACCESSOR]
        }),
        __metadata("design:paramtypes", [core_1.ElementRef, domhandler_1.DomHandler, core_1.Renderer2, core_1.ChangeDetectorRef,
            objectutils_1.ObjectUtils, core_1.NgZone])
    ], Dropdown);
    return Dropdown;
}());
exports.Dropdown = Dropdown;
var DropdownModule = /** @class */ (function () {
    function DropdownModule() {
    }
    DropdownModule = __decorate([
        core_1.NgModule({
            imports: [common_1.CommonModule, shared_1.SharedModule],
            exports: [Dropdown, shared_1.SharedModule],
            declarations: [Dropdown]
        })
    ], DropdownModule);
    return DropdownModule;
}());
exports.DropdownModule = DropdownModule;
//# sourceMappingURL=dropdown.js.map

/***/ }),

/***/ 824:
/***/ (function(module, exports, __webpack_require__) {

"use strict";

var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
Object.defineProperty(exports, "__esModule", { value: true });
var core_1 = __webpack_require__(1);
var ObjectUtils = /** @class */ (function () {
    function ObjectUtils() {
        this.isFunction = function (obj) { return !!(obj && obj.constructor && obj.call && obj.apply); };
    }
    ObjectUtils.prototype.equals = function (obj1, obj2, field) {
        if (field)
            return (this.resolveFieldData(obj1, field) === this.resolveFieldData(obj2, field));
        else
            return this.equalsByValue(obj1, obj2);
    };
    ObjectUtils.prototype.equalsByValue = function (obj1, obj2, visited) {
        if (obj1 == null && obj2 == null) {
            return true;
        }
        if (obj1 == null || obj2 == null) {
            return false;
        }
        if (obj1 == obj2) {
            return true;
        }
        if (obj1 instanceof Date && obj2 instanceof Date) {
            return obj1.getTime() == obj2.getTime();
        }
        if (typeof obj1 == 'object' && typeof obj2 == 'object') {
            if (visited) {
                if (visited.indexOf(obj1) !== -1)
                    return false;
            }
            else {
                visited = [];
            }
            visited.push(obj1);
            for (var p in obj1) {
                if (obj1.hasOwnProperty(p) !== obj2.hasOwnProperty(p)) {
                    return false;
                }
                switch (typeof (obj1[p])) {
                    case 'object':
                        if (!this.equalsByValue(obj1[p], obj2[p], visited))
                            return false;
                        break;
                    case 'function':
                        if (typeof (obj2[p]) == 'undefined' || (p != 'compare' && obj1[p].toString() != obj2[p].toString()))
                            return false;
                        break;
                    default:
                        if (obj1[p] != obj2[p])
                            return false;
                        break;
                }
            }
            for (var p in obj2) {
                if (typeof (obj1[p]) == 'undefined')
                    return false;
            }
            delete obj1._$visited;
            return true;
        }
        return false;
    };
    ObjectUtils.prototype.resolveFieldData = function (data, field) {
        if (data && field) {
            if (this.isFunction(field)) {
                return field(data);
            }
            else if (field.indexOf('.') == -1) {
                return data[field];
            }
            else {
                var fields = field.split('.');
                var value = data;
                for (var i = 0, len = fields.length; i < len; ++i) {
                    if (value == null) {
                        return null;
                    }
                    value = value[fields[i]];
                }
                return value;
            }
        }
        else {
            return null;
        }
    };
    ObjectUtils.prototype.filter = function (value, fields, filterValue) {
        var filteredItems = [];
        if (value) {
            for (var _i = 0, value_1 = value; _i < value_1.length; _i++) {
                var item = value_1[_i];
                for (var _a = 0, fields_1 = fields; _a < fields_1.length; _a++) {
                    var field = fields_1[_a];
                    if (String(this.resolveFieldData(item, field)).toLowerCase().indexOf(filterValue.toLowerCase()) > -1) {
                        filteredItems.push(item);
                        break;
                    }
                }
            }
        }
        return filteredItems;
    };
    ObjectUtils.prototype.reorderArray = function (value, from, to) {
        var target;
        if (value && (from !== to)) {
            if (to >= value.length) {
                target = to - value.length;
                while ((target--) + 1) {
                    value.push(undefined);
                }
            }
            value.splice(to, 0, value.splice(from, 1)[0]);
        }
    };
    ObjectUtils.prototype.generateSelectItems = function (val, field) {
        var selectItems;
        if (val && val.length) {
            selectItems = [];
            for (var _i = 0, val_1 = val; _i < val_1.length; _i++) {
                var item = val_1[_i];
                selectItems.push({ label: this.resolveFieldData(item, field), value: item });
            }
        }
        return selectItems;
    };
    ObjectUtils.prototype.insertIntoOrderedArray = function (item, index, arr, sourceArr) {
        if (arr.length > 0) {
            var injected = false;
            for (var i = 0; i < arr.length; i++) {
                var currentItemIndex = this.findIndexInList(arr[i], sourceArr);
                if (currentItemIndex > index) {
                    arr.splice(i, 0, item);
                    injected = true;
                    break;
                }
            }
            if (!injected) {
                arr.push(item);
            }
        }
        else {
            arr.push(item);
        }
    };
    ObjectUtils.prototype.findIndexInList = function (item, list) {
        var index = -1;
        if (list) {
            for (var i = 0; i < list.length; i++) {
                if (list[i] == item) {
                    index = i;
                    break;
                }
            }
        }
        return index;
    };
    ObjectUtils = __decorate([
        core_1.Injectable()
    ], ObjectUtils);
    return ObjectUtils;
}());
exports.ObjectUtils = ObjectUtils;
//# sourceMappingURL=objectutils.js.map

/***/ })

});
//# sourceMappingURL=2.js.map