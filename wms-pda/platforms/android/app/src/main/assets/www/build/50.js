webpackJsonp([50],{

/***/ 1022:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return CyclecountrantaskPage; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(43);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__services_App_service__ = __webpack_require__(157);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__services_Utils__ = __webpack_require__(89);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__services_NativeService__ = __webpack_require__(64);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__ = __webpack_require__(65);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_6__ionic_storage__ = __webpack_require__(66);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_7__services_BarCodeService__ = __webpack_require__(359);
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
 * Generated class for the CyclecountrantaskPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */
var CyclecountrantaskPage = /** @class */ (function () {
    function CyclecountrantaskPage(navCtrl, navParams, appService, nativeService, storage, ngZone, events, barcodeService) {
        this.navCtrl = navCtrl;
        this.navParams = navParams;
        this.appService = appService;
        this.nativeService = nativeService;
        this.storage = storage;
        this.ngZone = ngZone;
        this.events = events;
        this.barcodeService = barcodeService;
        this.subHeaderModel = '??????';
        this.refreshType = '';
        this.isModel = true;
        this.subHeader = [
            { taskType: '', taskName: '??????' },
            // { taskType: BaseCodeConstant.UnAllot, taskName: '?????????' },
            { taskType: __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["d" /* BaseCodeConstant */].UnComplate, taskName: '?????????' },
            { taskType: __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["d" /* BaseCodeConstant */].Complated, taskName: '?????????' },
        ];
        this.subHeaderModel = this.subHeader[0];
        if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isNotEmpty(this.navParams.get('result'))) {
            var result = this.navParams.get('result');
            //console.log(result, 'aaa');
            this.taskResult = result;
            this.taskResultAll = result;
        }
    }
    // ionViewWillEnter() {
    //   //????????????????????????????????????
    //   this.events.subscribe('barcode:scan', (barcode, time) => {
    //     let bt = this.barcodeService.GetBarcodeType(barcode);
    //     if (bt == BarcodeType.HuoWei) {
    //       this.ngZone.run(() => {
    //       });
    //     }
    //   });
    // }
    // ionViewWillLeave() {
    //   AppGlobal.removeSubscribe(this);
    // }
    CyclecountrantaskPage.prototype.tapEvent = function (item) {
        this.taskModel = item;
    };
    /**
     *
     * @param ??????????????????
     */
    CyclecountrantaskPage.prototype.locCode_KeyDown = function (event) {
        var _this = this;
        if (event && event.keyCode == 13) {
            if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isEmpty(this.locCode)) {
                this.taskResult = this.taskResultAll;
            }
            this.taskResult = this.taskResultAll.filter(function (x) { return x.locCode == _this.locCode; });
        }
    };
    CyclecountrantaskPage.prototype.headSelected = function (item) {
        this.subHeaderModel = item;
        this.refreshType = item.taskType;
        //????????????
        if (item.taskName == '??????') {
            this.taskResult = this.taskResultAll;
        }
        else {
            this.taskResult = this.taskResultAll.filter(function (x) { return x.locStatusDesc == item.taskName; });
        }
    };
    CyclecountrantaskPage.prototype.ionViewDidLoad = function () {
        // this.nativeService.showLoading();
        // this.cyclecountrantask('');
        // console.log('ionViewDidLoad CyclecountrantaskPage');
    };
    CyclecountrantaskPage.prototype.onClick = function (item) {
        this.navCtrl.push('RandomCheckTaskPage', { result: item });
    };
    CyclecountrantaskPage.prototype.detailed = function () {
        this.navCtrl.push('CyclecountRecordPage', { countBillNo: this.taskResult[0].countBillNo });
    };
    /**
    * ??????
    */
    CyclecountrantaskPage.prototype.exit = function () {
        this.navCtrl.pop();
    };
    /**
     * ??????
     */
    CyclecountrantaskPage.prototype.btnOk = function () {
        var _this = this;
        var locList = this.taskResultAll.filter(function (x) { return x.isModel == true; });
        var locstr = [];
        if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isNotEmpty(locList)) {
            locList.forEach(function (element) {
                locstr.push(element.countDetailId);
            });
        }
        if (locstr.length <= 0) {
            this.nativeService.showToast('???????????????????????????');
            return;
        }
        var params = {
            ids: locstr
        };
        var body = "ids=" + locstr;
        this.nativeService.showLoading();
        this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].StockCountPDA + __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].completeTask, __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["f" /* Method */].Post, body, __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["e" /* ContenType */].Form, function (result) {
            _this.nativeService.hideLoading();
            if (result.data.countBillState == 30) {
                _this.nativeService.showToast('????????????');
                _this.navCtrl.pop();
            }
            else {
                _this.taskResult = result.data.countDetailVOList;
                ;
                _this.taskResultAll = result.data.countDetailVOList;
                ;
                _this.nativeService.showToast('????????????');
            }
        });
    };
    CyclecountrantaskPage = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Component"])({
            selector: 'page-cyclecountrantask',template:/*ion-inline-start:"D:\developer_tools\workspace\wms_3.0_base_pda\src\pages\cyclecounthome\cyclecountrantask\cyclecountrantask.html"*/'<!--\n\n  Generated template for the TaskviewPage page.\n\n\n\n  See http://ionicframework.com/docs/components/#navigation for more info on\n\n  Ionic pages and navigation.\n\n-->\n\n<ion-header>\n\n  <ion-navbar color="wmscolor">\n\n    <ion-title>????????????\n\n      <!-- <div class="title_f_name">(????????????)</div> -->\n\n    </ion-title>\n\n    <ion-buttons end (click)="detailed()">\n\n      <button ion-button icon-only>\n\n        <ion-icon name="menu"></ion-icon>\n\n      </button>\n\n    </ion-buttons>\n\n  </ion-navbar>\n\n\n\n  <ion-searchbar Description=\'primary\' placeholder="??????" (keyup)="locCode_KeyDown($event)" [(ngModel)]="locCode">\n\n\n\n  </ion-searchbar>\n\n  <div class="nav-tile">\n\n    <!-- <ul>\n\n      <li>??????</li>\n\n      <li>\n\n        <ion-icon class="iconfont  iconicon_arr_right"></ion-icon>\n\n      </li>\n\n      <li>??????</li>\n\n      <li>\n\n        <ion-icon class="iconfont  iconicon_arr_right"></ion-icon>\n\n      </li>\n\n      <li>????????????</li>\n\n      <li>\n\n        <ion-icon class="iconfont  iconicon_arr_right"></ion-icon>\n\n      </li>\n\n      <li>????????????</li>\n\n    </ul> -->\n\n    <crumbs></crumbs>\n\n  </div>\n\n  <div class="sub_header1">\n\n    <div *ngFor="let item of subHeader" (click)="headSelected(item)"\n\n      [ngClass]="{selected:subHeaderModel.taskName === item.taskName}">\n\n      {{item.taskName}}\n\n    </div>\n\n  </div>\n\n</ion-header>\n\n<ion-content>\n\n\n\n  <div *ngFor="let taskItem of taskResult; let i = index">\n\n   \n\n    <div  class="title-ms">\n\n      <ion-checkbox (tap)="tapEvent(taskItem)" [(ngModel)]="taskItem.isModel"></ion-checkbox>\n\n      <div class="title-ms-info" (tap)="onClick(taskItem)">\n\n        <!-- <button ion-button color="dark">{{i+1}}</button> -->\n\n        <div class="title-font-s1">\n\n          <div>{{taskItem.locStatusDesc}}</div>\n\n          <span>{{taskItem.whName}}</span>\n\n        </div>\n\n        <div class="title-font-s2">\n\n          <div>{{taskItem.locCode}}</div>\n\n          <span>???????????????<b style="color: orange;">{{taskItem.skuType}}</b></span>\n\n        </div>\n\n        <div class="ionic_right">\n\n          <ion-icon class="iconfont icon-xiangyou"></ion-icon>\n\n        </div>\n\n      </div>\n\n    </div>\n\n  </div>\n\n\n\n</ion-content>\n\n<ion-footer>\n\n  <div class="btn-box">\n\n    <button no-dbl-click ion-button block class="btn-l" (tap)="exit()">\n\n      ??????[Esc]\n\n    </button>\n\n    <button no-dbl-click ion-button block class="btn-d" (tap)="btnOk()">\n\n      ??????[Ent]\n\n    </button>\n\n  </div>\n\n\n\n</ion-footer>'/*ion-inline-end:"D:\developer_tools\workspace\wms_3.0_base_pda\src\pages\cyclecounthome\cyclecountrantask\cyclecountrantask.html"*/,
        }),
        __metadata("design:paramtypes", [__WEBPACK_IMPORTED_MODULE_1_ionic_angular__["l" /* NavController */],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["m" /* NavParams */],
            __WEBPACK_IMPORTED_MODULE_2__services_App_service__["a" /* AppService */],
            __WEBPACK_IMPORTED_MODULE_4__services_NativeService__["a" /* NativeService */],
            __WEBPACK_IMPORTED_MODULE_6__ionic_storage__["b" /* Storage */],
            __WEBPACK_IMPORTED_MODULE_0__angular_core__["NgZone"],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["c" /* Events */],
            __WEBPACK_IMPORTED_MODULE_7__services_BarCodeService__["a" /* BarcodeService */]])
    ], CyclecountrantaskPage);
    return CyclecountrantaskPage;
}());

//# sourceMappingURL=cyclecountrantask.js.map

/***/ }),

/***/ 723:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "CyclecountrantaskPageModule", function() { return CyclecountrantaskPageModule; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(43);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__cyclecountrantask__ = __webpack_require__(1022);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__components_crumbs_crumbs_module__ = __webpack_require__(781);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};




var CyclecountrantaskPageModule = /** @class */ (function () {
    function CyclecountrantaskPageModule() {
    }
    CyclecountrantaskPageModule = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["NgModule"])({
            declarations: [
                __WEBPACK_IMPORTED_MODULE_2__cyclecountrantask__["a" /* CyclecountrantaskPage */],
            ],
            imports: [
                __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["g" /* IonicPageModule */].forChild(__WEBPACK_IMPORTED_MODULE_2__cyclecountrantask__["a" /* CyclecountrantaskPage */]),
                __WEBPACK_IMPORTED_MODULE_3__components_crumbs_crumbs_module__["a" /* CrumbsModule */]
            ],
            schemas: [__WEBPACK_IMPORTED_MODULE_0__angular_core__["NO_ERRORS_SCHEMA"]]
        })
    ], CyclecountrantaskPageModule);
    return CyclecountrantaskPageModule;
}());

//# sourceMappingURL=cyclecountrantask.module.js.map

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
            { id: "HomePage", name: "??????" },
            { id: "TaskviewPage", name: "????????????" },
            { id: "CheckinhomePage", name: "??????" },
            { id: "CheckinpiecePage", name: "????????????" },
            { id: "CheckInPage", name: "????????????" },
            { id: "CheckinboxPage", name: "????????????" },
            { id: "SearchOrderPage", name: "????????????" },
            { id: "CheckinBoxInfoPage", name: "????????????" },
            { id: "CheckintrayPage", name: "????????????" },
            { id: "CheckinDetailedPage", name: "????????????" },
            { id: "CollageTaskPage", name: "????????????" },
            { id: "CollageTaskListPage", name: "????????????" },
            { id: "CollageTaskBoxPage", name: "????????????" },
            { id: "CyclecounthomePage", name: "??????" },
            { id: "CyclecountrandominfoPage", name: "????????????" },
            { id: "CyclecountrantaskPage", name: "????????????" },
            { id: "CyclecountBillPage", name: "????????????" },
            { id: "CyclecoutrantaskinfoPage", name: "????????????" },
            { id: "MovehomePage", name: "??????" },
            { id: "MovesboxPage", name: "????????????" },
            { id: "MovesboxInfoPage", name: "????????????" },
            { id: "moveSku", name: "????????????" },
            { id: "moveStock", name: "????????????" },
            { id: "moveStocks", name: "????????????" },
            { id: "PackageTaskPage", name: "????????????" },
            { id: "PackageTaskListPage", name: "????????????" },
            { id: "PackageTaskInfoPage", name: "????????????" },
            { id: "PutawayhomePage", name: "??????" },
            { id: "PutawayPage", name: "????????????" },
            { id: "PutawayboxPage", name: "????????????" },
            { id: "ReplenishmenhomePage", name: "????????????" },
            { id: "ReplenishmenDetilePage", name: "????????????" },
            { id: "SettingPage", name: "????????????" },
            { id: "SettingPwdPage", name: "????????????" },
            { id: "SettingThemePage", name: "????????????" },
            { id: "ShipmenthomePage", name: "??????" },
            { id: "ShipmentLpnPage", name: "????????????" },
            { id: "StockhomePage", name: "??????" },
            { id: "StockdetailPage", name: "????????????" },
            { id: "StockqueryPage", name: "????????????" },
            { id: "StockPritPage", name: "????????????" },
            { id: "TakeawayhomePage", name: "??????" },
            { id: "TakeawaypiecePage", name: "????????????" },
            { id: "TakeawayPage", name: "????????????" },
            { id: "TakeawayinPage", name: "????????????" },
            { id: "TakeawayindetailedPage", name: "????????????" },
            { id: "TakeawaypieceboxPage", name: "????????????" },
            { id: "TakeawayinboxPage", name: "????????????" },
            { id: "CougnyPage", name: "??????" },
            { id: "CopyLabelPage", name: "????????????" },
            { id: "RandomCheckPage", name: "????????????" },
            { id: "RandomCheckTaskPage", name: "????????????" },
            { id: "ReplenishmenPage", name: "????????????" },
            { id: "ReplenishmenRecordPage", name: "????????????" },
            { id: "StockFrozenPage", name: "????????????" },
            { id: "CheckOrderPage", name: "????????????" },
            { id: "UpdateDetailPage", name: "????????????" },
            { id: "TaskwayMultPage", name: "????????????" },
            { id: "BindPrintterPage", name: "???????????????" },
            { id: "BindOrderPage", name: "????????????" },
            { id: "BindLpnPage", name: "????????????" },
            { id: "CyclecountRecordPage", name: "????????????" },
            { id: "MergeTrayPage", name: "??????" }
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
//# sourceMappingURL=50.js.map