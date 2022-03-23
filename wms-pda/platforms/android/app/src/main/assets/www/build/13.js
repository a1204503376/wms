webpackJsonp([13,68],{

/***/ 1011:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return UpdateDetailPage; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(43);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__services_Utils__ = __webpack_require__(89);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__services_App_service__ = __webpack_require__(157);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__services_NativeService__ = __webpack_require__(64);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5__services_BarCodeService__ = __webpack_require__(359);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};






var UpdateDetailPage = /** @class */ (function () {
    function UpdateDetailPage(navCtrl, navParams, modalController, appService, nativeService, barcodeService, events, ngZone) {
        var _this = this;
        this.navCtrl = navCtrl;
        this.navParams = navParams;
        this.modalController = modalController;
        this.appService = appService;
        this.nativeService = nativeService;
        this.barcodeService = barcodeService;
        this.events = events;
        this.ngZone = ngZone;
        this.result = {};
        this.skulots = [];
        this.callback = this.navParams.get('callback');
        if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isNotEmpty(this.navParams.get('result'))) {
            var result = this.navParams.get('result');
            Object.assign(this.result, result);
            this.result.skuLotList.forEach(function (element) {
                var skulotVaule = _this.result["skuLot" + element.skuLotIndex];
                if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isNotEmpty(skulotVaule)) {
                    var skulot = {};
                    skulot["skuLotName"] = element.skuLotLabel;
                    skulot["skuLotValue"] = skulotVaule;
                    _this.skulots.push(skulot);
                }
            });
        }
    }
    UpdateDetailPage.prototype.ionViewWillEnter = function () {
    };
    UpdateDetailPage.prototype.ionViewWillLeave = function () {
    };
    UpdateDetailPage.prototype.ionViewDidLoad = function () {
        var that = this;
        this.nav.backButtonClick = function () {
            that.callback({ isPop: true });
            this.navCtrl.pop();
        };
    };
    UpdateDetailPage.prototype.backClick = function () {
        this.callback({ isPop: true });
        this.navCtrl.pop();
    };
    UpdateDetailPage.prototype.submitClick = function () {
        this.callback({ isPop: true, result: this.result });
        this.navCtrl.pop();
    };
    __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["ViewChild"])(__WEBPACK_IMPORTED_MODULE_1_ionic_angular__["n" /* Navbar */]),
        __metadata("design:type", __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["n" /* Navbar */])
    ], UpdateDetailPage.prototype, "nav", void 0);
    UpdateDetailPage = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Component"])({
            selector: 'page-update-detail',template:/*ion-inline-start:"D:\developer_tools\workspace\wms_3.0_base_pda\src\pages\checkinhome\checkOrder\update-detail\update-detail.html"*/'<ion-header>\n\n\n\n  <ion-navbar color="wmscolor" #nav>\n\n    <ion-title>修改明细</ion-title>\n\n    <!-- <ion-buttons end (click)="finishDetailed()">\n\n      <button ion-button icon-only>\n\n        <ion-icon name="menu"></ion-icon>\n\n      </button>\n\n    </ion-buttons> -->\n\n  </ion-navbar>\n\n  <ion-grid style="color: white;">\n\n    <ion-row>\n\n      <ion-col>\n\n        <div style="display: flex;flex-direction: row;justify-content: space-between;">\n\n          <span>{{result.asnBillNo}}</span>\n\n          <span>收货行数：{{result.asnLineNo}}</span>\n\n        </div>\n\n      </ion-col>\n\n    </ion-row>\n\n    <ion-row>\n\n      <ion-col>\n\n        <div class="nav-tile">\n\n          <crumbs></crumbs>\n\n        </div>\n\n      </ion-col>\n\n    </ion-row>\n\n  </ion-grid>\n\n</ion-header>\n\n<ion-content>\n\n  <ion-item>\n\n    <div class="sub-header-box">\n\n      <div class="row1">\n\n        <div class="left">\n\n          <span>{{result.skuName}}</span>\n\n          <span>{{result.skuCode}}</span>\n\n        </div>\n\n        <div class="right">\n\n          <span>收货规格：{{result.skuSpec}}</span>\n\n          <span>收货数量：{{result.planQtyName}}</span>\n\n        </div>\n\n      </div>\n\n      <div class="row2">\n\n        <span>状态：{{result.detailStatusName}}</span>\n\n      </div>\n\n    </div>\n\n  </ion-item>\n\n  <ion-scroll class="list-box" direction="y">\n\n    <ion-list>\n\n      <ion-item>\n\n        <ion-label>库位编码：</ion-label>\n\n        <ion-input maxlength=\'30\' type="text" [(ngModel)]="result.locCode">\n\n        </ion-input>\n\n      </ion-item>\n\n      <ion-item>\n\n        <ion-label>收货数量：</ion-label>\n\n        <ion-input maxlength=\'30\' type="number" [(ngModel)]="result.planQty">\n\n        </ion-input>\n\n        <ion-select [(ngModel)]="result.umCode" cancelText="取消">\n\n          <ion-option [value]="result.umCode">{{result.umName}}</ion-option>\n\n        </ion-select>\n\n      </ion-item>\n\n      <ion-item *ngFor="let skulot of skulots">\n\n        <ion-label>{{skulot.skuLotName}}：</ion-label>\n\n        <ion-input maxlength=\'30\' type="text" [(ngModel)]="skulot.skuLotValue">\n\n        </ion-input>\n\n      </ion-item>\n\n    </ion-list>\n\n  </ion-scroll>\n\n</ion-content>\n\n<ion-footer>\n\n  <div class="btn-box">\n\n    <button ion-button class="btn-l" (click)="backClick()"> 返回[Esc]</button>\n\n    <button ion-button class="btn-d" (click)="submitClick()"> 提交[Ent]</button>\n\n  </div>\n\n</ion-footer>'/*ion-inline-end:"D:\developer_tools\workspace\wms_3.0_base_pda\src\pages\checkinhome\checkOrder\update-detail\update-detail.html"*/,
        }),
        __metadata("design:paramtypes", [__WEBPACK_IMPORTED_MODULE_1_ionic_angular__["l" /* NavController */],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["m" /* NavParams */],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["j" /* ModalController */],
            __WEBPACK_IMPORTED_MODULE_3__services_App_service__["a" /* AppService */],
            __WEBPACK_IMPORTED_MODULE_4__services_NativeService__["a" /* NativeService */],
            __WEBPACK_IMPORTED_MODULE_5__services_BarCodeService__["a" /* BarcodeService */],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["c" /* Events */],
            __WEBPACK_IMPORTED_MODULE_0__angular_core__["NgZone"]])
    ], UpdateDetailPage);
    return UpdateDetailPage;
}());

//# sourceMappingURL=update-detail.js.map

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

/***/ 712:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "UpdateDetailPageModule", function() { return UpdateDetailPageModule; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(43);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__update_detail__ = __webpack_require__(1011);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__shared_check_in_box_label_type_check_in_box_label_type_module__ = __webpack_require__(701);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__components_crumbs_crumbs_module__ = __webpack_require__(781);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};





var UpdateDetailPageModule = /** @class */ (function () {
    function UpdateDetailPageModule() {
    }
    UpdateDetailPageModule = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["NgModule"])({
            declarations: [
                __WEBPACK_IMPORTED_MODULE_2__update_detail__["a" /* UpdateDetailPage */],
            ],
            imports: [
                __WEBPACK_IMPORTED_MODULE_3__shared_check_in_box_label_type_check_in_box_label_type_module__["CheckInBoxLabelTypePageModule"],
                __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["g" /* IonicPageModule */].forChild(__WEBPACK_IMPORTED_MODULE_2__update_detail__["a" /* UpdateDetailPage */]),
                __WEBPACK_IMPORTED_MODULE_4__components_crumbs_crumbs_module__["a" /* CrumbsModule */]
            ],
            schemas: [__WEBPACK_IMPORTED_MODULE_0__angular_core__["NO_ERRORS_SCHEMA"]]
        })
    ], UpdateDetailPageModule);
    return UpdateDetailPageModule;
}());

//# sourceMappingURL=update-detail.module.js.map

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

/***/ })

});
//# sourceMappingURL=13.js.map