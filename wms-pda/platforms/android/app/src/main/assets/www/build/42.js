webpackJsonp([42],{

/***/ 1028:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return moveStock; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0_ionic_angular__ = __webpack_require__(43);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__services_App_service__ = __webpack_require__(157);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__services_Utils__ = __webpack_require__(89);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__services_NativeService__ = __webpack_require__(64);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__utils_appConstant__ = __webpack_require__(65);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5__angular_core__ = __webpack_require__(1);
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
 * Generated class for the moveStock page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */
var moveStock = /** @class */ (function () {
    function moveStock(navCtrl, navParams, appService, nativeService) {
        var _this = this;
        this.navCtrl = navCtrl;
        this.navParams = navParams;
        this.appService = appService;
        this.nativeService = nativeService;
        this.isShow = 'warehouse';
        this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_4__utils_appConstant__["b" /* Api */].api + __WEBPACK_IMPORTED_MODULE_4__utils_appConstant__["b" /* Api */].ApiPDA, __WEBPACK_IMPORTED_MODULE_4__utils_appConstant__["f" /* Method */].Post, '', __WEBPACK_IMPORTED_MODULE_4__utils_appConstant__["e" /* ContenType */].Form, function (result) {
            if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isNotEmpty(result.data)) {
                _this.warhouser = result.data;
            }
        });
    }
    moveStock.prototype.clearModel = function () {
        this.warhouserItem = null;
        this.whName = '';
        this.beforeLocCode = '';
        this.beforelpnCode = '';
        this.IsbeforeLocCode = '';
        this.IsbeforelpnCode = '';
        this.targetLocCode = '';
        this.IstargetLocCode = '';
    };
    moveStock.prototype.toggle = function () {
        this.whName = this.warhouserItem.whName;
    };
    moveStock.prototype.exit = function () {
        if (this.isShow == 'before') {
            this.isShow = 'warehouse';
        }
        else if (this.isShow == 'target') {
            this.isShow = 'before';
        }
        else {
            this.navCtrl.pop();
        }
    };
    /**
    * ?????????????????????
    */
    moveStock.prototype.beforeLocCode_KeyDown = function (event) {
        if (event && event.keyCode == 13) {
            this.IsbeforeLocCode = this.beforeLocCode;
            this.beforeLocCode = '';
            this.vbeforelpnCode.setFocus();
        }
    };
    moveStock.prototype.beforelpnCode_KeyDown = function (event) {
        var _this = this;
        if (event && event.keyCode == 13) {
            //??????????????????????????????
            if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(this.beforelpnCode)) {
                this.nativeService.showToast('?????????????????????');
                return;
            }
            this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_4__utils_appConstant__["b" /* Api */].api + __WEBPACK_IMPORTED_MODULE_4__utils_appConstant__["b" /* Api */].stockGetLocByLpnCode, __WEBPACK_IMPORTED_MODULE_4__utils_appConstant__["f" /* Method */].Post, 'lpnCode=' + this.beforelpnCode, __WEBPACK_IMPORTED_MODULE_4__utils_appConstant__["e" /* ContenType */].Form, function (result) {
                if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isNotEmpty(result.data)) {
                    _this.IsbeforelpnCode = _this.beforelpnCode;
                    _this.IsbeforeLocCode = result.data.locCode;
                    _this.beforelpnCode = '';
                }
                else {
                    _this.nativeService.showToast('??????????????????????????????');
                }
            });
        }
    };
    moveStock.prototype.btnOk = function () {
        var _this = this;
        var params = {
            lpnCode: this.IsbeforelpnCode,
            location: {
                locCode: this.IsbeforeLocCode
            }
        };
        if (this.isShow == 'warehouse') {
            if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(this.warhouserItem)) {
                this.nativeService.showToast('???????????????');
                return;
            }
            this.isShow = 'before';
        }
        else if (this.isShow == 'before') {
            if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(this.IsbeforelpnCode)) {
                this.nativeService.showToast('????????????????????????');
                return;
            }
            if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(this.IsbeforeLocCode)) {
                this.nativeService.showToast('????????????????????????');
                return;
            }
            this.isShow = 'target';
            // this.appService.httpRequest(Api.api + Api.StockDetail, Method.Post, params, ContenType.Json, result => {
            //   if (Utils.isNotEmpty(result.data)) {
            //   } else {
            //     this.nativeService.showToast('?????????????????????????????????');
            //   }
            // });
        }
        else {
            //??????????????????
            if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(this.targetLocCode)) {
                this.nativeService.showToast('????????????????????????');
                return;
            }
            var params_1 = {
                lpnCode: this.IsbeforelpnCode,
                locCode: this.targetLocCode,
                whId: this.warhouserItem.whId
            };
            //let body = "locCode=" + this.targetLocCode + "&lpnCode=" + this.IsbeforelpnCode;
            this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_4__utils_appConstant__["b" /* Api */].Outstock + __WEBPACK_IMPORTED_MODULE_4__utils_appConstant__["b" /* Api */].submitMOVE, __WEBPACK_IMPORTED_MODULE_4__utils_appConstant__["f" /* Method */].Post, params_1, __WEBPACK_IMPORTED_MODULE_4__utils_appConstant__["e" /* ContenType */].Json, function (result) {
                if (result.data) {
                    _this.nativeService.showToast('????????????');
                    _this.isShow = 'warehouse';
                    _this.clearModel();
                }
                else {
                    _this.nativeService.showToast('????????????');
                }
            });
        }
    };
    __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_5__angular_core__["ViewChild"])('vbeforelpnCode'),
        __metadata("design:type", Object)
    ], moveStock.prototype, "vbeforelpnCode", void 0);
    __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_5__angular_core__["ViewChild"])('vbeforeLocCode'),
        __metadata("design:type", Object)
    ], moveStock.prototype, "vbeforeLocCode", void 0);
    moveStock = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_5__angular_core__["Component"])({
            selector: 'page-movestock',template:/*ion-inline-start:"D:\developer_tools\workspace\wms_3.0_base_pda\src\pages\movehome\movestock\movestock.html"*/'<ion-header>\n\n    <ion-navbar color="wmscolor">\n\n        <ion-title>\n\n            ????????????\n\n            <!-- <div class="title_f_name">(????????????)</div> -->\n\n        </ion-title>\n\n        <!-- <ion-buttons end (click)="detailed()">\n\n            <button ion-button icon-only>\n\n                <ion-icon name="menu"></ion-icon>\n\n            </button>\n\n        </ion-buttons> -->\n\n    </ion-navbar>\n\n    <div class="title-bt">\n\n        <div class="nav-tile">\n\n            <!-- <ul>\n\n                <li>??????</li>\n\n                <li>\n\n                    <ion-icon class="iconfont  iconicon_arr_right"></ion-icon>\n\n                </li>\n\n                <li>??????</li>\n\n                <li>\n\n                    <ion-icon class="iconfont  iconicon_arr_right"></ion-icon>\n\n                </li>\n\n                <li>????????????</li>\n\n            </ul> -->\n\n            <crumbs></crumbs>\n\n            <div class="order-info">{{whName}}</div>\n\n        </div>\n\n    </div>\n\n</ion-header>\n\n<ion-content>\n\n    <div *ngIf="isShow==\'warehouse\'">\n\n        <div class="list_item">\n\n            <ion-item>\n\n                <ion-label>??? ??????</ion-label>\n\n                <ion-select (ionChange)="toggle()" [(ngModel)]="warhouserItem" interface="action-sheet" cancelText="??????">\n\n                    <div *ngFor="let Item of warhouser">\n\n                        <ion-option [value]="Item">{{Item.whName}}</ion-option>\n\n                    </div>\n\n                </ion-select>\n\n            </ion-item>\n\n        </div>\n\n    </div>\n\n    <div *ngIf="isShow==\'before\'||isShow==\'target\'">\n\n        <div class="title-ms clearfix">\n\n            <div class="title-ms-info">\n\n                <ul>\n\n                    <li class="l-width2">\n\n                        <span class="s-label">????????????</span>\n\n                        <span class="s-text">{{IsbeforeLocCode}}</span>\n\n                    </li>\n\n                    <li class="l-width2">\n\n                        <span class="s-label">????????????</span>\n\n                        <span class="s-text">{{IsbeforelpnCode}}</span>\n\n                    </li>\n\n                </ul>\n\n            </div>\n\n        </div>\n\n        <div class="list_item" *ngIf="isShow==\'before\'">\n\n            <ion-item>\n\n                <ion-label>????????????</ion-label>\n\n                <ion-input #vbeforeLocCode (keyup)="beforeLocCode_KeyDown($event)" type="text"\n\n                    [(ngModel)]="beforeLocCode"></ion-input>\n\n                <ion-avatar item-end>\n\n                    <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n                </ion-avatar>\n\n            </ion-item>\n\n            <ion-item>\n\n                <ion-label>????????????</ion-label>\n\n                <ion-input (keyup)="beforelpnCode_KeyDown($event)" type="text" #vbeforelpnCode\n\n                    [(ngModel)]="beforelpnCode"></ion-input>\n\n                <ion-avatar item-end>\n\n                    <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n                </ion-avatar>\n\n            </ion-item>\n\n        </div>\n\n        <div class="list_item" *ngIf="isShow==\'target\'">\n\n            <ion-item>\n\n                <ion-label>???????????????</ion-label>\n\n                <ion-input [(ngModel)]="targetLocCode" type="text"></ion-input>\n\n                <ion-avatar item-end>\n\n                    <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n                </ion-avatar>\n\n            </ion-item>\n\n        </div>\n\n    </div>\n\n</ion-content>\n\n<ion-footer>\n\n    <div class="btn-box">\n\n        <button ion-button block class="btn-l" (click)="exit()">\n\n            ??????[Esc]\n\n        </button>\n\n        <button ion-button block class="btn-d" (click)="btnOk()">\n\n            ??????[Ent]\n\n        </button>\n\n    </div>\n\n\n\n</ion-footer>'/*ion-inline-end:"D:\developer_tools\workspace\wms_3.0_base_pda\src\pages\movehome\movestock\movestock.html"*/,
        }),
        __metadata("design:paramtypes", [__WEBPACK_IMPORTED_MODULE_0_ionic_angular__["l" /* NavController */], __WEBPACK_IMPORTED_MODULE_0_ionic_angular__["m" /* NavParams */], __WEBPACK_IMPORTED_MODULE_1__services_App_service__["a" /* AppService */],
            __WEBPACK_IMPORTED_MODULE_3__services_NativeService__["a" /* NativeService */]])
    ], moveStock);
    return moveStock;
}());

//# sourceMappingURL=movestock.js.map

/***/ }),

/***/ 729:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "moveStockModule", function() { return moveStockModule; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(43);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__movestock__ = __webpack_require__(1028);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__components_crumbs_crumbs_module__ = __webpack_require__(781);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};




var moveStockModule = /** @class */ (function () {
    function moveStockModule() {
    }
    moveStockModule = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["NgModule"])({
            declarations: [
                __WEBPACK_IMPORTED_MODULE_2__movestock__["a" /* moveStock */],
            ],
            imports: [
                __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["g" /* IonicPageModule */].forChild(__WEBPACK_IMPORTED_MODULE_2__movestock__["a" /* moveStock */]),
                __WEBPACK_IMPORTED_MODULE_3__components_crumbs_crumbs_module__["a" /* CrumbsModule */]
            ],
            schemas: [__WEBPACK_IMPORTED_MODULE_0__angular_core__["NO_ERRORS_SCHEMA"]]
        })
    ], moveStockModule);
    return moveStockModule;
}());

//# sourceMappingURL=movestock.module.js.map

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
//# sourceMappingURL=42.js.map