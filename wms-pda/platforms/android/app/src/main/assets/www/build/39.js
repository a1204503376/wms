webpackJsonp([39],{

/***/ 1034:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return PutawayPage; });
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
 * Generated class for the PutawayPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */
var PutawayPage = /** @class */ (function () {
    function PutawayPage(navCtrl, navParams, appService, nativeService, barcodeService, events, ngZone) {
        this.navCtrl = navCtrl;
        this.navParams = navParams;
        this.appService = appService;
        this.nativeService = nativeService;
        this.barcodeService = barcodeService;
        this.events = events;
        this.ngZone = ngZone;
        this.lpnCode = ''; //????????????
        this.zoneCode = ''; //????????????
        this.zoneName = ''; //????????????
        this.typeCount = ''; //??????????????????
        this.locCode = ''; //????????????
        this.islocCode = ''; //????????????
        this.locStatusName = ''; //????????????
        this.whName = ''; //????????????
        this.whId = ''; //??????ID
    }
    PutawayPage.prototype.ionViewDidLoad = function () {
    };
    PutawayPage.prototype.ionViewWillEnter = function () {
        var _this = this;
        //????????????????????????????????????
        this.events.subscribe('barcode:scan', function (barcode, time) {
            var bt = _this.barcodeService.GetBarcodeType(barcode);
            if (bt == __WEBPACK_IMPORTED_MODULE_6__services_BarCodeService__["b" /* BarcodeType */].TuoPan) {
                _this.ngZone.run(function () {
                    _this.searchCode = barcode;
                    _this.queryStockByLpnCode();
                });
            }
            if (bt == __WEBPACK_IMPORTED_MODULE_6__services_BarCodeService__["b" /* BarcodeType */].HuoWei) {
                _this.ngZone.run(function () {
                    _this.islocCode = barcode;
                });
            }
        });
    };
    PutawayPage.prototype.ionViewWillLeave = function () {
        __WEBPACK_IMPORTED_MODULE_7__services_AppGlobal__["a" /* AppGlobal */].removeSubscribe(this);
    };
    /**
     * ?????????????????????
     */
    PutawayPage.prototype.keyEnter = function (event) {
        if (event && event.keyCode == 13) {
            this.queryStockByLpnCode();
        }
    };
    /**
     * ??????lpn??????????????????
     */
    PutawayPage.prototype.queryStockByLpnCode = function () {
        var _this = this;
        if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isEmpty(this.searchCode)) {
            this.nativeService.showToast('?????????????????????');
            return;
        }
        this.lpnCode = this.searchCode;
        this.searchCode = '';
        var body = "lpnCode=" + this.lpnCode;
        this.nativeService.showLoading();
        this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].Instock + __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].queryStockByLpnCode, __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["f" /* Method */].Post, body, __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["e" /* ContenType */].Form, function (result) {
            _this.nativeService.hideLoading();
            if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isNotEmpty(result.data)) {
                var Jsonresult = result.data;
                _this.zoneName = Jsonresult.zoneName;
                _this.zoneCode = Jsonresult.zoneCode;
                _this.typeCount = Jsonresult.typeCount;
                _this.locStatusName = Jsonresult.locStatusName;
                _this.whName = Jsonresult.whName;
                _this.locCode = Jsonresult.locCode;
                _this.whId = Jsonresult.whId;
                _this.islocCode = Jsonresult.targetLocCode;
            }
        });
    };
    PutawayPage.prototype.btnOk = function () {
        var _this = this;
        if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isEmpty(this.lpnCode)) {
            this.nativeService.showToast('??????????????????,?????????');
            return;
        }
        if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isEmpty(this.islocCode)) {
            this.nativeService.showToast('????????????????????????');
            return;
        }
        var body = "zoneCode=" + this.zoneCode + "&locCode=" + this.islocCode + "&lpnCode=" + this.lpnCode + "&whId=" + this.whId;
        this.nativeService.showLoading();
        this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].Instock + __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].submitPutaway, __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["f" /* Method */].Post, body, __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["e" /* ContenType */].Form, function (result) {
            _this.nativeService.hideLoading();
            _this.clearModel();
            _this.nativeService.showToast("????????????");
        });
    };
    PutawayPage.prototype.clearModel = function () {
        this.searchCode = '';
        this.lpnCode = '';
        this.zoneCode = '';
        this.typeCount = '';
        this.locCode = '';
        this.islocCode = '';
        this.locStatusName = '';
        this.whName = ''; //????????????
        this.zoneName = '';
    };
    PutawayPage.prototype.exit = function () {
        this.navCtrl.pop();
    };
    /**
      * ?????????????????????
    */
    PutawayPage.prototype.islocCode_KeyDown = function (event) {
        if (event && event.keyCode == 13) {
            // this.locCode=this.islocCode;
            // this.islocCode='';
            // this.btnOk();
        }
    };
    PutawayPage = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Component"])({
            selector: 'page-putaway',template:/*ion-inline-start:"D:\developer_tools\workspace\wms_3.0_base_pda\src\pages\putawayhome\putaway\putaway.html"*/'<ion-header>\n\n  <ion-navbar color="wmscolor">\n\n    <ion-title>\n\n      ????????????\n\n      <!-- <div class="title_f_name">(????????????)</div> -->\n\n    </ion-title>\n\n  </ion-navbar>\n\n  <ion-searchbar [(ngModel)]="searchCode" (keyup)="keyEnter($event)" Description=\'primary\' placeholder="??????/LPN">\n\n    <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n  </ion-searchbar>\n\n  <div class="nav-tile">\n\n    <!-- <ul>\n\n      <li>??????</li>\n\n      <li>\n\n        <ion-icon class="iconfont iconicon_arr_right"></ion-icon>\n\n      </li>\n\n      <li>??????</li>\n\n    </ul> -->\n\n    <crumbs></crumbs>\n\n  </div>\n\n</ion-header>\n\n<ion-content class="no-scroll">\n\n  <div class="title-ms clearfix">\n\n    <div class="title-ms-info">\n\n      <div class="title-font-small">\n\n        <div class="received_lpn">\n\n          {{lpnCode}}\n\n        </div>\n\n        <div class="title-font-s2">\n\n          <label>????????????:</label>\n\n          <label>{{locCode}}</label>\n\n        </div>\n\n        <div class="title-font-s3">\n\n          <label>????????????:</label>\n\n          <label>{{zoneName}}</label>\n\n        </div>\n\n        <div>\n\n          <!-- <label>???&emsp;&emsp;???:</label> -->\n\n          <!-- ????????? -->\n\n          <label style="color: black;">{{whName}}</label>\n\n        </div>\n\n\n\n        <div class="received_num">\n\n          ??????: {{typeCount}}\n\n        </div>\n\n        <!-- 1????????? -->\n\n        <div class="ware_house">\n\n          <!-- {{whName}} -->\n\n        </div>\n\n      </div>\n\n    </div>\n\n  </div>\n\n\n\n  <div class="list_item">\n\n    <ion-item>\n\n      <ion-label>??? ??????</ion-label>\n\n      <ion-input (keyup)="islocCode_KeyDown($event)" [(ngModel)]="islocCode" type="text"></ion-input>\n\n      <ion-avatar item-end>\n\n        <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n      </ion-avatar>\n\n    </ion-item>\n\n  </div>\n\n</ion-content>\n\n<ion-footer>\n\n  <div class="btn-box">\n\n    <button no-dbl-click ion-button block class="btn-l" (tap)="exit()">\n\n      ??????[Esc]\n\n    </button>\n\n    <button no-dbl-click ion-button block class="btn-d" (tap)="btnOk()">\n\n      ??????[Ent]\n\n    </button>\n\n  </div>\n\n\n\n</ion-footer>'/*ion-inline-end:"D:\developer_tools\workspace\wms_3.0_base_pda\src\pages\putawayhome\putaway\putaway.html"*/,
        }),
        __metadata("design:paramtypes", [__WEBPACK_IMPORTED_MODULE_1_ionic_angular__["l" /* NavController */],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["m" /* NavParams */],
            __WEBPACK_IMPORTED_MODULE_2__services_App_service__["a" /* AppService */],
            __WEBPACK_IMPORTED_MODULE_4__services_NativeService__["a" /* NativeService */],
            __WEBPACK_IMPORTED_MODULE_6__services_BarCodeService__["a" /* BarcodeService */],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["c" /* Events */],
            __WEBPACK_IMPORTED_MODULE_0__angular_core__["NgZone"]])
    ], PutawayPage);
    return PutawayPage;
}());

//# sourceMappingURL=putaway.js.map

/***/ }),

/***/ 734:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "PutawayPageModule", function() { return PutawayPageModule; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(43);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__putaway__ = __webpack_require__(1034);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__components_crumbs_crumbs_module__ = __webpack_require__(781);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};




var PutawayPageModule = /** @class */ (function () {
    function PutawayPageModule() {
    }
    PutawayPageModule = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["NgModule"])({
            declarations: [
                __WEBPACK_IMPORTED_MODULE_2__putaway__["a" /* PutawayPage */],
            ],
            imports: [
                __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["g" /* IonicPageModule */].forChild(__WEBPACK_IMPORTED_MODULE_2__putaway__["a" /* PutawayPage */]),
                __WEBPACK_IMPORTED_MODULE_3__components_crumbs_crumbs_module__["a" /* CrumbsModule */]
            ],
            schemas: [__WEBPACK_IMPORTED_MODULE_0__angular_core__["NO_ERRORS_SCHEMA"]]
        })
    ], PutawayPageModule);
    return PutawayPageModule;
}());

//# sourceMappingURL=putaway.module.js.map

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
//# sourceMappingURL=39.js.map