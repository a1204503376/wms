webpackJsonp([59],{

/***/ 1008:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return CheckinpiecePage; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(43);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__services_App_service__ = __webpack_require__(157);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__services_Utils__ = __webpack_require__(89);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__services_NativeService__ = __webpack_require__(64);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__ = __webpack_require__(65);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_6__services_AppGlobal__ = __webpack_require__(360);
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
 * Generated class for the CheckinpiecePage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */
var CheckinpiecePage = /** @class */ (function () {
    function CheckinpiecePage(navCtrl, navParams, appService, nativeService, events, ngZone) {
        this.navCtrl = navCtrl;
        this.navParams = navParams;
        this.appService = appService;
        this.nativeService = nativeService;
        this.events = events;
        this.ngZone = ngZone;
        this.current = 1; //?????????
        this.size = 10; //??????
        this.records = [];
    }
    CheckinpiecePage.prototype.itemInfo = function () {
        this.navCtrl.push('CheckInPage');
    };
    CheckinpiecePage.prototype.ionViewWillEnter = function () {
        var _this = this;
        //????????????????????????????????????
        if (__WEBPACK_IMPORTED_MODULE_6__services_AppGlobal__["a" /* AppGlobal */].scanFlag) {
            __WEBPACK_IMPORTED_MODULE_6__services_AppGlobal__["a" /* AppGlobal */].scanFlag = false;
            this.events.subscribe('barcode:scan', function (barcode, time) {
                _this.analysisDate(barcode);
            });
        }
        if (this.navParams.get('flag') || false) {
            this.resetPage();
        }
    };
    CheckinpiecePage.prototype.resetPage = function () {
        this.records = [];
        if (this.infiniteScroll) {
            this.infiniteScroll.enable(true);
        }
        this.size = 10;
        this.current = 1;
    };
    CheckinpiecePage.prototype.ionViewWillLeave = function () {
        __WEBPACK_IMPORTED_MODULE_6__services_AppGlobal__["a" /* AppGlobal */].removeSubscribe(this);
    };
    CheckinpiecePage.prototype.ionViewDidEnter = function () {
        this.asnBillNo = '';
    };
    /**
   * ????????????
   */
    CheckinpiecePage.prototype.analysisDate = function (barcode) {
        var _this = this;
        this.ngZone.run(function () {
            _this.orderNo = barcode;
            _this.resetPage();
            _this.getContainerList();
        });
    };
    /**
   * ?????????????????????
   */
    CheckinpiecePage.prototype.keyEnter = function (event) {
        if (event && event.keyCode == 13) {
            this.resetPage();
            this.getContainerList();
        }
    };
    CheckinpiecePage.prototype.onClick = function (record) {
        this.asnHeaderList(record.asnBillNo);
    };
    CheckinpiecePage.prototype.asnHeaderList = function (asnBillNo) {
        var _this = this;
        this.nativeService.showLoading();
        if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isEmpty(asnBillNo)) {
            this.nativeService.showToast('?????????????????????');
            __WEBPACK_IMPORTED_MODULE_6__services_AppGlobal__["a" /* AppGlobal */].scanFlag = false;
            return;
        }
        var params = {
            asnBillNo: asnBillNo
        };
        this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].Instock + __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].AsnHeaderList, __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["f" /* Method */].Post, params, __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["e" /* ContenType */].Json, function (result) {
            _this.nativeService.hideLoading();
            __WEBPACK_IMPORTED_MODULE_6__services_AppGlobal__["a" /* AppGlobal */].scanFlag = false;
            if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isEmpty(result.data)) {
                _this.nativeService.showToast('?????????????????????????????????');
            }
            else {
                if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isNotEmpty(result.data[0].asnDetailMinVO)) {
                    _this.asnBillNo = '';
                    _this.navCtrl.push('CheckInPage', { result: result.data[0] });
                }
            }
        });
    };
    CheckinpiecePage.prototype.getContainerList = function () {
        var _this = this;
        if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isEmpty(this.orderNo)) {
            this.nativeService.showToast("???????????????????????????");
            return;
        }
        this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].Instock + __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].getAsnHeaderList, __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["f" /* Method */].Post, 'current=' + this.current + '&' +
            'size=' + this.size + '&' +
            'orderNo=' + this.orderNo, __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["e" /* ContenType */].Form, function (result) {
            if (result.data.records.length == 1) {
                var asnBillNo = result.data.records[0].asnBillNo;
                _this.asnHeaderList(asnBillNo);
            }
            else {
                _this.records = _this.records.concat(result.data.records);
                if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isEmpty(_this.records)) {
                    if (_this.infiniteScroll) {
                        _this.infiniteScroll.enable(false);
                        _this.infiniteScroll.complete();
                    }
                    else {
                        _this.nativeService.showToast("??????????????????????????????");
                    }
                    return;
                }
            }
            _this.current++;
            if (_this.infiniteScroll) {
                _this.infiniteScroll.complete(); /*??????????????????????????????*/
            }
        });
    };
    CheckinpiecePage.prototype.loadData = function (infiniteScroll) {
        this.infiniteScroll = infiniteScroll;
        this.getContainerList();
    };
    CheckinpiecePage = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Component"])({
            selector: 'page-checkinpiece',template:/*ion-inline-start:"D:\developer_tools\workspace\wms_3.0_base_pda\src\pages\checkinhome\checkinpiece\checkinpiece.html"*/'<!--\n\n  Generated template for the TaskviewPage page.\n\n\n\n  See http://ionicframework.com/docs/components/#navigation for more info on\n\n  Ionic pages and navigation.\n\n-->\n\n<ion-header>\n\n  <ion-navbar color="wmscolor">\n\n    <ion-title>????????????\n\n      <!-- <div class="title_f_name">(????????????)</div> -->\n\n    </ion-title>\n\n    <!-- <ion-buttons end (click)="detailed()">\n\n      <button ion-button icon-only>\n\n        <ion-icon name="menu"></ion-icon>\n\n      </button>\n\n    </ion-buttons> -->\n\n  </ion-navbar>\n\n  <ion-searchbar [(ngModel)]="orderNo" debounce="1000" (keydown)="keyEnter($event)" Description=\'primary\'\n\n    placeholder="????????????(WMS??????|T100??????)">\n\n  </ion-searchbar>\n\n  <!-- <ion-input  [(ngModel)]="asnBillNo"  (keyup)="keyEnter($event)" type="text"> -->\n\n  <!-- </ion-input> -->\n\n  <!-- <div class="nav-tile">\n\n    <ul>\n\n      <li>??????</li>\n\n      <li>\n\n        <ion-icon class="iconfont  iconicon_arr_right"></ion-icon>\n\n      </li>\n\n      <li>??????</li>\n\n      <li>\n\n        <ion-icon class="iconfont  iconicon_arr_right"></ion-icon>\n\n      </li>\n\n      <li>????????????</li>\n\n    </ul>\n\n  </div> -->\n\n  <div class="nav-tile"> \n\n  <crumbs></crumbs>\n\n  </div>\n\n</ion-header>\n\n<ion-content>\n\n\n\n  <div class="nodes-list-box">\n\n    <div class="nodes-list-item-box" *ngFor="let record of records" (click)="onClick(record)" >\n\n      <div class="left">\n\n        <span>{{record.asnBillNo}}</span>\n\n        <span *ngIf="record.orderNo&&record.orderNo.length>0">{{record.orderNo}}</span>\n\n        <span>{{record.sname}}</span>\n\n      </div>\n\n      <div class="right">\n\n        <span>{{record.whName}}</span>\n\n        <span>{{record.billTypeName}}</span>\n\n      </div>\n\n    </div>\n\n  </div>\n\n  \n\n  <ion-infinite-scroll threshold="100px" (ionInfinite)="loadData($event)">\n\n    <ion-infinite-scroll-content\n\n      loadingSpinner="bubbles"\n\n      loadingText="????????????...">\n\n    </ion-infinite-scroll-content>\n\n  </ion-infinite-scroll>\n\n  <!-- <button ion-button icon-start>\n\n    <ion-icon name="home"></ion-icon>\n\n  </button> -->\n\n  <!-- <div *ngFor="let i of [1,2,3,4,5,6,7,8,9,10]; let _i = index" class="title-ms">\n\n    <div class="title-ms-info" (click)="itemInfo()">\n\n      <button ion-button color="dark">1</button>\n\n      <div class="title-font-s1">\n\n        <div>ASN00000000001</div>\n\n        <span>?????????????????? <b style="color: #F59A23;">5</b> <b style="color: 008080;">/10</b></span>\n\n      </div>\n\n      <div class="title-font-s2">\n\n        <div>1?????????</div>\n\n        <div>????????????????????????????????????????????????</div>\n\n        <div>??? ?????????????????????????????????????????????</div>\n\n      </div>\n\n    </div>\n\n  </div> -->\n\n</ion-content>'/*ion-inline-end:"D:\developer_tools\workspace\wms_3.0_base_pda\src\pages\checkinhome\checkinpiece\checkinpiece.html"*/,
        }),
        __metadata("design:paramtypes", [__WEBPACK_IMPORTED_MODULE_1_ionic_angular__["l" /* NavController */],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["m" /* NavParams */],
            __WEBPACK_IMPORTED_MODULE_2__services_App_service__["a" /* AppService */],
            __WEBPACK_IMPORTED_MODULE_4__services_NativeService__["a" /* NativeService */],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["c" /* Events */],
            __WEBPACK_IMPORTED_MODULE_0__angular_core__["NgZone"]])
    ], CheckinpiecePage);
    return CheckinpiecePage;
}());

//# sourceMappingURL=checkinpiece.js.map

/***/ }),

/***/ 709:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "CheckinpiecePageModule", function() { return CheckinpiecePageModule; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(43);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__checkinpiece__ = __webpack_require__(1008);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__components_crumbs_crumbs_module__ = __webpack_require__(781);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};




var CheckinpiecePageModule = /** @class */ (function () {
    function CheckinpiecePageModule() {
    }
    CheckinpiecePageModule = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["NgModule"])({
            declarations: [
                __WEBPACK_IMPORTED_MODULE_2__checkinpiece__["a" /* CheckinpiecePage */],
            ],
            imports: [
                __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["g" /* IonicPageModule */].forChild(__WEBPACK_IMPORTED_MODULE_2__checkinpiece__["a" /* CheckinpiecePage */]),
                __WEBPACK_IMPORTED_MODULE_3__components_crumbs_crumbs_module__["a" /* CrumbsModule */]
            ],
            schemas: [__WEBPACK_IMPORTED_MODULE_0__angular_core__["NO_ERRORS_SCHEMA"]]
        })
    ], CheckinpiecePageModule);
    return CheckinpiecePageModule;
}());

//# sourceMappingURL=checkinpiece.module.js.map

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
//# sourceMappingURL=59.js.map
