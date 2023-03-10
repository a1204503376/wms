webpackJsonp([58],{

/***/ 1014:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return CollageTaskListPage; });
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


/**
 * Generated class for the CollageTaskListPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */
var CollageTaskListPage = /** @class */ (function () {
    function CollageTaskListPage(navCtrl, navParams) {
        this.navCtrl = navCtrl;
        this.navParams = navParams;
        this.isShow = true;
        this.resultList = this.navParams.get('result');
    }
    CollageTaskListPage.prototype.ionViewDidLoad = function () {
        console.log('ionViewDidLoad CollageTaskListPage');
    };
    /**
     * éćŠćç
     */
    CollageTaskListPage.prototype.lpnListOnClick = function (item) {
        if (this.isShow) {
            this.resultListlpnItem = item;
            console.log(this.resultListlpnItem.detailList.length);
            this.isShow = false;
        }
    };
    CollageTaskListPage.prototype.exit = function () {
        if (this.isShow) {
            this.navCtrl.pop();
        }
        else {
            this.isShow = true;
        }
    };
    CollageTaskListPage = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Component"])({
            selector: 'page-collage-task-list',template:/*ion-inline-start:"D:\developer_tools\workspace\wms_3.0_base_pda\src\pages\collage-task\collage-task-list\collage-task-list.html"*/'<ion-header>\n\n  <ion-navbar color="wmscolor">\n\n    <ion-title>\n\n      ćźćäťťĺĄ\n\n      <!-- <div class="title_f_name">(éĺ¤äşĺ)</div> -->\n\n    </ion-title>\n\n    <!-- <ion-buttons end (click)="detailed()">\n\n          <button ion-button icon-only>\n\n              <ion-icon name="menu"></ion-icon>\n\n          </button>\n\n      </ion-buttons> -->\n\n  </ion-navbar>\n\n  <div class="title-bt">\n\n    <div class="nav-tile">\n\n      <div class="order-info">äťťĺĄčżĺşŚ{{resultList.finish}}/{{resultList.total}}</div>\n\n      <!-- <ul>\n\n        <li>éŚéĄľ</li>\n\n        <li>\n\n          <ion-icon class="iconfont  iconicon_arr_right"></ion-icon>\n\n        </li>\n\n        <li>äťťĺĄćĽç</li>\n\n        <li>\n\n          <ion-icon class="iconfont  iconicon_arr_right"></ion-icon>\n\n        </li>\n\n        <li>ćźćäťťĺĄ</li>\n\n        <li>\n\n          <ion-icon class="iconfont  iconicon_arr_right"></ion-icon>\n\n        </li>\n\n        <li>ćçčŻŚć</li>\n\n      </ul> -->\n\n      <crumbs></crumbs>\n\n    </div>\n\n  </div>\n\n</ion-header>\n\n<ion-content>\n\n  <div>\n\n    <!-- çŠĺéćžĺĄ -->\n\n    <div class="title-ms clearfix">\n\n      <div class="title-ms-info clearfix">\n\n        <div>ĺćŽçźç ďź{{resultList.soBillNo}}</div>\n\n        <div>ĺćŽçąťĺďź{{resultList.billTypeName}}</div>\n\n        <div>ĺŽ˘&emsp;&emsp;ćˇďź{{resultList.clientName}}</div>\n\n        <div *ngIf="isShow">ćçć°:{{resultList.lpnList.length}}</div>\n\n        <div *ngIf="!isShow">ćťçŽąć°:{{resultListlpnItem.detailList.length}}</div>\n\n        <div>{{resultList.whName}}</div>\n\n      </div>\n\n    </div>\n\n  </div>\n\n  <div>\n\n    <!-- ĺˇ˛ćźćç-->\n\n    <div *ngIf="isShow" class="list_item_serial clearfix">\n\n      <div class="list_item_title">\n\n        <span [ngStyle]="{\'border-bottom\':\'3px solid #008080\'}" (click)="isTitle(true)">ĺˇ˛ćźćç(<b>1</b>)</span>\n\n        <!-- <span [ngStyle]="{\'border-bottom\':isTilebool === false ? \'3px solid #008080\' : \'0\' }"\n\n          (click)="isTitle(false)">ĺˇ˛ćśĺşĺĺˇ(<b>2</b>)</span> -->\n\n      </div>\n\n      <div class="list_item_ls clearfix" (click)="lpnListOnClick(item)" *ngFor="let item of resultList.lpnList">\n\n        <div>{{item.lpnCode}}</div>\n\n        <div>{{item.weight}}</div>\n\n        <div> {{item.length}} *{{item.width}} *{{item.height}}</div>\n\n        <div><b style="color: orange;">{{item.detailList.length}}</b>çŽą</div>\n\n        <div>\n\n          <ion-icon name="arrow-forward"></ion-icon>\n\n        </div>\n\n      </div>\n\n    </div>\n\n  </div>\n\n  <!-- çŽąç ĺčĄ¨ -->\n\n  <div *ngIf="!isShow">\n\n    <div class="list_item_ls_box clearfix" *ngFor="let item of resultListlpnItem.detailList">\n\n      <div>{{item.skuName}}</div>\n\n      <div>{{item.skuCode}}</div>\n\n      <div><b style="color: orange;">{{item.pickQty}}</b>{{item.wsuName}}</div>\n\n    </div>\n\n  </div>\n\n\n\n\n\n</ion-content>\n\n<ion-footer>\n\n  <div class="btn-box">\n\n    <button no-dbl-click ion-button block class="btn-l" (tap)="exit()">\n\n      čżĺ[Esc]\n\n    </button>\n\n  </div>\n\n</ion-footer>'/*ion-inline-end:"D:\developer_tools\workspace\wms_3.0_base_pda\src\pages\collage-task\collage-task-list\collage-task-list.html"*/,
        }),
        __metadata("design:paramtypes", [__WEBPACK_IMPORTED_MODULE_1_ionic_angular__["l" /* NavController */], __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["m" /* NavParams */]])
    ], CollageTaskListPage);
    return CollageTaskListPage;
}());

//# sourceMappingURL=collage-task-list.js.map

/***/ }),

/***/ 715:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "CollageTaskListPageModule", function() { return CollageTaskListPageModule; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(43);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__collage_task_list__ = __webpack_require__(1014);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__components_crumbs_crumbs_module__ = __webpack_require__(781);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};




var CollageTaskListPageModule = /** @class */ (function () {
    function CollageTaskListPageModule() {
    }
    CollageTaskListPageModule = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["NgModule"])({
            declarations: [
                __WEBPACK_IMPORTED_MODULE_2__collage_task_list__["a" /* CollageTaskListPage */],
            ],
            imports: [
                __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["g" /* IonicPageModule */].forChild(__WEBPACK_IMPORTED_MODULE_2__collage_task_list__["a" /* CollageTaskListPage */]),
                __WEBPACK_IMPORTED_MODULE_3__components_crumbs_crumbs_module__["a" /* CrumbsModule */]
            ],
            schemas: [__WEBPACK_IMPORTED_MODULE_0__angular_core__["NO_ERRORS_SCHEMA"]]
        })
    ], CollageTaskListPageModule);
    return CollageTaskListPageModule;
}());

//# sourceMappingURL=collage-task-list.module.js.map

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
            { id: "HomePage", name: "éŚéĄľ" },
            { id: "TaskviewPage", name: "äťťĺĄćĽç" },
            { id: "CheckinhomePage", name: "ćśč´§" },
            { id: "CheckinpiecePage", name: "ćäťśćśč´§" },
            { id: "CheckInPage", name: "ć§čĄćśč´§" },
            { id: "CheckinboxPage", name: "ćçŽąćśč´§" },
            { id: "SearchOrderPage", name: "ćĺćśč´§" },
            { id: "CheckinBoxInfoPage", name: "ć§čĄćśč´§" },
            { id: "CheckintrayPage", name: "ćććśč´§" },
            { id: "CheckinDetailedPage", name: "ćśč´§čŽ°ĺ˝" },
            { id: "CollageTaskPage", name: "ćźćäťťĺĄ" },
            { id: "CollageTaskListPage", name: "ćçčŻŚć" },
            { id: "CollageTaskBoxPage", name: "ćçčŻŚć" },
            { id: "CyclecounthomePage", name: "ççš" },
            { id: "CyclecountrandominfoPage", name: "éćşççš" },
            { id: "CyclecountrantaskPage", name: "ççšäťťĺĄ" },
            { id: "CyclecountBillPage", name: "éćççš" },
            { id: "CyclecoutrantaskinfoPage", name: "ć§čĄççš" },
            { id: "MovehomePage", name: "ç§ťĺ¨" },
            { id: "MovesboxPage", name: "ćçŽąç§ťĺ¨" },
            { id: "MovesboxInfoPage", name: "ćçŽąç§ťĺ¨" },
            { id: "moveSku", name: "çŠĺç§ťĺ¨" },
            { id: "moveStock", name: "ćçç§ťĺ¨" },
            { id: "moveStocks", name: "ĺ¤ćç§ťĺ¨" },
            { id: "PackageTaskPage", name: "ćĺäťťĺĄ" },
            { id: "PackageTaskListPage", name: "ćĺäťťĺĄ" },
            { id: "PackageTaskInfoPage", name: "äťťĺĄćçť" },
            { id: "PutawayhomePage", name: "ä¸ćś" },
            { id: "PutawayPage", name: "ććä¸ćś" },
            { id: "PutawayboxPage", name: "ćçŽąä¸ćś" },
            { id: "ReplenishmenhomePage", name: "čĄĽč´§äťťĺĄ" },
            { id: "ReplenishmenDetilePage", name: "čĄĽč´§äťťĺĄ" },
            { id: "SettingPage", name: "ä¸ŞäşşčŽžç˝Ž" },
            { id: "SettingPwdPage", name: "äżŽćšĺŻç " },
            { id: "SettingThemePage", name: "ä¸ťé˘é˘č˛" },
            { id: "ShipmenthomePage", name: "ĺčż" },
            { id: "ShipmentLpnPage", name: "ćçĺčż" },
            { id: "StockhomePage", name: "ĺşĺ­" },
            { id: "StockdetailPage", name: "ĺşĺ­ćĽčŻ˘" },
            { id: "StockqueryPage", name: "ĺşĺ­ćĽčŻ˘" },
            { id: "StockPritPage", name: "ĺşĺ­ćĺ°" },
            { id: "TakeawayhomePage", name: "ćŁč´§" },
            { id: "TakeawaypiecePage", name: "ćäťśćŁč´§" },
            { id: "TakeawayPage", name: "ć§čĄćŁč´§" },
            { id: "TakeawayinPage", name: "ć§čĄćŁč´§" },
            { id: "TakeawayindetailedPage", name: "ćŁč´§čŽ°ĺ˝" },
            { id: "TakeawaypieceboxPage", name: "ćçŽąćŁč´§" },
            { id: "TakeawayinboxPage", name: "ć§čĄćŁč´§" },
            { id: "CougnyPage", name: "ĺşĺ" },
            { id: "CopyLabelPage", name: "ĺ¤ĺść ç­ž" },
            { id: "RandomCheckPage", name: "éćşççš" },
            { id: "RandomCheckTaskPage", name: "ć§čĄççš" },
            { id: "ReplenishmenPage", name: "čĄĽč´§äťťĺĄ" },
            { id: "ReplenishmenRecordPage", name: "čĄĽč´§äťťĺĄ" },
            { id: "StockFrozenPage", name: "ĺşĺ­ĺťçť" },
            { id: "CheckOrderPage", name: "ć§čĄćśč´§" },
            { id: "UpdateDetailPage", name: "äżŽćšćçť" },
            { id: "TaskwayMultPage", name: "ćšéćŁč´§" },
            { id: "BindPrintterPage", name: "çťĺŽćĺ°ćş" },
            { id: "BindOrderPage", name: "ĺćŽçťĺŽ" },
            { id: "BindLpnPage", name: "çťĺŽĺŽšĺ¨" },
            { id: "CyclecountRecordPage", name: "ççščŽ°ĺ˝" },
            { id: "MergeTrayPage", name: "ćźć" }
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
//# sourceMappingURL=58.js.map