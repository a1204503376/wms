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
    * 原库位回车事件
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
            //查询当前容器库位编码
            if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(this.beforelpnCode)) {
                this.nativeService.showToast('原容器不能为空');
                return;
            }
            this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_4__utils_appConstant__["b" /* Api */].api + __WEBPACK_IMPORTED_MODULE_4__utils_appConstant__["b" /* Api */].stockGetLocByLpnCode, __WEBPACK_IMPORTED_MODULE_4__utils_appConstant__["f" /* Method */].Post, 'lpnCode=' + this.beforelpnCode, __WEBPACK_IMPORTED_MODULE_4__utils_appConstant__["e" /* ContenType */].Form, function (result) {
                if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isNotEmpty(result.data)) {
                    _this.IsbeforelpnCode = _this.beforelpnCode;
                    _this.IsbeforeLocCode = result.data.locCode;
                    _this.beforelpnCode = '';
                }
                else {
                    _this.nativeService.showToast('当前库房不存在该容器');
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
                this.nativeService.showToast('请选择库房');
                return;
            }
            this.isShow = 'before';
        }
        else if (this.isShow == 'before') {
            if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(this.IsbeforelpnCode)) {
                this.nativeService.showToast('容器编码不能为空');
                return;
            }
            if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(this.IsbeforeLocCode)) {
                this.nativeService.showToast('库位编码不能为空');
                return;
            }
            this.isShow = 'target';
            // this.appService.httpRequest(Api.api + Api.StockDetail, Method.Post, params, ContenType.Json, result => {
            //   if (Utils.isNotEmpty(result.data)) {
            //   } else {
            //     this.nativeService.showToast('当前库房不存容器和库位');
            //   }
            // });
        }
        else {
            //托盘移动提交
            if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(this.targetLocCode)) {
                this.nativeService.showToast('目标库位不能为空');
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
                    _this.nativeService.showToast('转移成功');
                    _this.isShow = 'warehouse';
                    _this.clearModel();
                }
                else {
                    _this.nativeService.showToast('移库失败');
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
            selector: 'page-movestock',template:/*ion-inline-start:"D:\developer_tools\workspace\wms_3.0_base_pda\src\pages\movehome\movestock\movestock.html"*/'<ion-header>\n\n    <ion-navbar color="wmscolor">\n\n        <ion-title>\n\n            托盘移动\n\n            <!-- <div class="title_f_name">(青啤五厂)</div> -->\n\n        </ion-title>\n\n        <!-- <ion-buttons end (click)="detailed()">\n\n            <button ion-button icon-only>\n\n                <ion-icon name="menu"></ion-icon>\n\n            </button>\n\n        </ion-buttons> -->\n\n    </ion-navbar>\n\n    <div class="title-bt">\n\n        <div class="nav-tile">\n\n            <!-- <ul>\n\n                <li>首页</li>\n\n                <li>\n\n                    <ion-icon class="iconfont  iconicon_arr_right"></ion-icon>\n\n                </li>\n\n                <li>移动</li>\n\n                <li>\n\n                    <ion-icon class="iconfont  iconicon_arr_right"></ion-icon>\n\n                </li>\n\n                <li>托盘移动</li>\n\n            </ul> -->\n\n            <crumbs></crumbs>\n\n            <div class="order-info">{{whName}}</div>\n\n        </div>\n\n    </div>\n\n</ion-header>\n\n<ion-content>\n\n    <div *ngIf="isShow==\'warehouse\'">\n\n        <div class="list_item">\n\n            <ion-item>\n\n                <ion-label>库 房：</ion-label>\n\n                <ion-select (ionChange)="toggle()" [(ngModel)]="warhouserItem" interface="action-sheet" cancelText="取消">\n\n                    <div *ngFor="let Item of warhouser">\n\n                        <ion-option [value]="Item">{{Item.whName}}</ion-option>\n\n                    </div>\n\n                </ion-select>\n\n            </ion-item>\n\n        </div>\n\n    </div>\n\n    <div *ngIf="isShow==\'before\'||isShow==\'target\'">\n\n        <div class="title-ms clearfix">\n\n            <div class="title-ms-info">\n\n                <ul>\n\n                    <li class="l-width2">\n\n                        <span class="s-label">原库位：</span>\n\n                        <span class="s-text">{{IsbeforeLocCode}}</span>\n\n                    </li>\n\n                    <li class="l-width2">\n\n                        <span class="s-label">原容器：</span>\n\n                        <span class="s-text">{{IsbeforelpnCode}}</span>\n\n                    </li>\n\n                </ul>\n\n            </div>\n\n        </div>\n\n        <div class="list_item" *ngIf="isShow==\'before\'">\n\n            <ion-item>\n\n                <ion-label>原库位：</ion-label>\n\n                <ion-input #vbeforeLocCode (keyup)="beforeLocCode_KeyDown($event)" type="text"\n\n                    [(ngModel)]="beforeLocCode"></ion-input>\n\n                <ion-avatar item-end>\n\n                    <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n                </ion-avatar>\n\n            </ion-item>\n\n            <ion-item>\n\n                <ion-label>原容器：</ion-label>\n\n                <ion-input (keyup)="beforelpnCode_KeyDown($event)" type="text" #vbeforelpnCode\n\n                    [(ngModel)]="beforelpnCode"></ion-input>\n\n                <ion-avatar item-end>\n\n                    <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n                </ion-avatar>\n\n            </ion-item>\n\n        </div>\n\n        <div class="list_item" *ngIf="isShow==\'target\'">\n\n            <ion-item>\n\n                <ion-label>目标库位：</ion-label>\n\n                <ion-input [(ngModel)]="targetLocCode" type="text"></ion-input>\n\n                <ion-avatar item-end>\n\n                    <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n                </ion-avatar>\n\n            </ion-item>\n\n        </div>\n\n    </div>\n\n</ion-content>\n\n<ion-footer>\n\n    <div class="btn-box">\n\n        <button ion-button block class="btn-l" (click)="exit()">\n\n            返回[Esc]\n\n        </button>\n\n        <button ion-button block class="btn-d" (click)="btnOk()">\n\n            提交[Ent]\n\n        </button>\n\n    </div>\n\n\n\n</ion-footer>'/*ion-inline-end:"D:\developer_tools\workspace\wms_3.0_base_pda\src\pages\movehome\movestock\movestock.html"*/,
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
//# sourceMappingURL=42.js.map