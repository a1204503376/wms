webpackJsonp([19],{

/***/ 1368:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return TaskviewPage; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(43);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__services_App_service__ = __webpack_require__(157);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__services_Utils__ = __webpack_require__(89);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__services_NativeService__ = __webpack_require__(64);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__ = __webpack_require__(65);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_6__ionic_storage__ = __webpack_require__(66);
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
 * Generated class for the TaskviewPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */
var TaskviewPage = /** @class */ (function () {
    function TaskviewPage(navCtrl, navParams, appService, nativeService, storage, events) {
        var _this = this;
        this.navCtrl = navCtrl;
        this.navParams = navParams;
        this.appService = appService;
        this.nativeService = nativeService;
        this.storage = storage;
        this.events = events;
        this.subHeaderModel = '??????';
        this.refreshType = '';
        this.tempList = [];
        this.subHeader = [
            { taskType: '', taskName: '??????' },
            { taskType: __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["d" /* BaseCodeConstant */].task_type_102, taskName: '??????' },
            { taskType: __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["d" /* BaseCodeConstant */].task_type_103, taskName: '??????' },
            { taskType: __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["d" /* BaseCodeConstant */].task_type_104, taskName: '??????' },
            // { taskType: BaseCodeConstant.task_type_106, taskName: '??????' }, 
            { taskType: __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["d" /* BaseCodeConstant */].task_type_107, taskName: '??????' },
            { taskType: __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["d" /* BaseCodeConstant */].task_type_108, taskName: '??????' },
        ];
        this.subHeaderModel = this.subHeader[0];
        this.storage.get('sysParam').then(function (val) {
            if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isNotEmpty(val)) {
                _this.taskInventory = val.filter(function (x) { return x.paramKey == 'task:inventory'; })[0];
                _this.taskPutaway = val.filter(function (x) { return x.paramKey == 'task:putaway'; })[0];
            }
        });
    }
    TaskviewPage.prototype.ionViewWillEnter = function () {
        var _this = this;
        this.subHeaderModel = this.subHeader[0];
        this.nativeService.showLoading();
        this.taskPDAList('');
        //????????????????????????????????????
        this.events.subscribe('barcode:scan', function (barcode, time) {
            _this.taskPDAList({ billNo: barcode });
        });
    };
    TaskviewPage.prototype.ionViewWillLeave = function () {
        __WEBPACK_IMPORTED_MODULE_7__services_AppGlobal__["a" /* AppGlobal */].removeSubscribe(this);
    };
    TaskviewPage.prototype.headSelected = function (item) {
        this.subHeaderModel = item;
        this.nativeService.showLoading();
        this.refreshType = item.taskType;
        this.taskPDAList({ taskTypeCd: item.taskType });
    };
    TaskviewPage.prototype.onClick = function (item) {
        this.nativeService.showLoading();
        if (item.taskTypeCd == __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["d" /* BaseCodeConstant */].task_type_102) {
            if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isEmpty(item.billNo)) {
                this.nativeService.showToast('??????????????????');
                return;
            }
            this.AsnHeaderList({ asnBillNo: item.billNo });
        }
        else if (item.taskTypeCd == __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["d" /* BaseCodeConstant */].task_type_103) {
            if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isEmpty(item.billNo)) {
                this.nativeService.showToast('??????????????????');
                return;
            }
            this.OutHeaderList({ wellenNo: item.billNo });
        }
        else if (item.taskTypeCd == __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["d" /* BaseCodeConstant */].task_type_104) {
            if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isEmpty(item.billNo)) {
                this.nativeService.showToast('??????????????????');
                return;
            }
            this.cyclecountrantask({ countBillNo: item.billNo }); // ????????????????????????
        }
        else if (item.taskTypeCd == __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["d" /* BaseCodeConstant */].task_type_106) {
            if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isEmpty(item.billNo)) {
                this.nativeService.showToast('??????????????????');
                return;
            }
            this.upPickList({ taskId: item.taskId }); // ????????????????????????
        }
        else if (item.taskTypeCd == __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["d" /* BaseCodeConstant */].task_type_107) {
            if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isEmpty(item.billNo)) {
                this.nativeService.showToast('??????????????????');
                return;
            }
            this.getOutstockLpnInfo({ taskId: item.taskId }); // ????????????????????????
        }
        else if (item.taskTypeCd == __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["d" /* BaseCodeConstant */].task_type_108) {
            if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isEmpty(item.billNo)) {
                this.nativeService.showToast('??????????????????');
                return;
            }
            this.getReplenishmenInfo({ taskId: item.taskId }); // ????????????????????????
        }
    };
    /**
     * ???????????????????????????
     */
    TaskviewPage.prototype.AsnHeaderList = function (params) {
        var _this = this;
        //????????????
        if (this.taskInventory.paramValue == __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["d" /* BaseCodeConstant */].INVENTORY_1) {
            this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].Instock + __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].AsnHeaderList, __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["f" /* Method */].Post, params, __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["e" /* ContenType */].Json, function (result) {
                _this.nativeService.hideLoading();
                if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isEmpty(result.data)) {
                    _this.nativeService.showToast('????????????????????????????????????');
                    return;
                }
                else {
                    if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isNotEmpty(result.data[0].asnDetailMinVO)) {
                        _this.navCtrl.push('CheckInPage', { result: result.data[0] });
                    }
                }
            });
        }
        else {
            //????????????
            this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].Instock + __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].getAsnHeaderInfoForBox, __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["f" /* Method */].Get, params, __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["e" /* ContenType */].Form, function (result) {
                _this.nativeService.hideLoading();
                if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isEmpty(result.data)) {
                    _this.nativeService.showToast('?????????????????????????????????');
                }
                else {
                    if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isNotEmpty(result.data)) {
                        _this.navCtrl.push('CheckinBoxInfoPage', { result: result.data });
                    }
                }
            });
        }
    };
    /**
     * ??????????????????
     * @param params
     */
    TaskviewPage.prototype.cyclecountrantask = function (params) {
        var _this = this;
        this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].StockCountPDA + __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].cyclecountrantask, __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["f" /* Method */].Get, params, __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["e" /* ContenType */].Content, function (result) {
            // console.log(result.data.countDetailVOList,'result??????')
            _this.nativeService.hideLoading();
            if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isEmpty(result.data)) {
                _this.nativeService.showToast('????????????????????????????????????');
                return;
            }
            else {
                //????????????
                _this.navCtrl.push('CyclecountrantaskPage', { result: result.data.countDetailVOList });
            }
        });
    };
    /**
     * ????????????
     */
    TaskviewPage.prototype.upPickList = function (params) {
        var _this = this;
        this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].stockInner + __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].upPickList, __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["f" /* Method */].Get, {
            taskId: params.taskId
        }, __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["e" /* ContenType */].Content, function (result) {
            _this.nativeService.hideLoading();
            if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isNotEmpty(result.data)) {
                _this.navCtrl.push('PackageTaskPage', { result: result.data, taskId: params.taskId });
            }
            else {
                _this.navCtrl.push('PackageTaskListPage', { taskId: params.taskId });
            }
        });
    };
    /**
     * ????????????
     * @param params
     */
    TaskviewPage.prototype.getOutstockLpnInfo = function (params) {
        var _this = this;
        this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].Outstock + __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].getOutstockLpnInfo, __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["f" /* Method */].Get, { taskId: params.taskId }, __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["e" /* ContenType */].Content, function (result) {
            _this.nativeService.hideLoading();
            if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isEmpty(result.data)) {
                _this.nativeService.showToast('????????????????????????????????????');
                return;
            }
            else {
                if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isNotEmpty(result.data)) {
                    //????????????
                    _this.navCtrl.push('CollageTaskPage', { result: result.data, taskId: params.taskId });
                }
            }
        });
    };
    /**
    * ????????????
    */
    TaskviewPage.prototype.OutHeaderList = function (params) {
        var _this = this;
        this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].Outstock + __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].getPickInfo, __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["f" /* Method */].Get, params, __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["e" /* ContenType */].Form, function (result) {
            _this.nativeService.hideLoading();
            var PickInfoResult = result.data;
            var pickPlansList = PickInfoResult.pickPlans;
            if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isEmpty(pickPlansList)) {
                _this.nativeService.showToast('???????????????????????????');
                return;
            }
            //??????????????????
            var pickPlansModel = pickPlansList[0];
            if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isNotEmpty(pickPlansModel)) {
                if (_this.taskPutaway.paramValue == __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["d" /* BaseCodeConstant */].PUtAWAY_1) {
                    _this.navCtrl.push('TakeawayinPage', { pickPlansModel: pickPlansModel, PickInfoResult: PickInfoResult, pickPlansList: pickPlansList, wellenNo: params.wellenNo });
                }
                else {
                    _this.navCtrl.push('TakeawayinboxPage', { pickPlansModel: pickPlansModel, PickInfoResult: PickInfoResult, pickPlansList: pickPlansList, wellenNo: params.wellenNo });
                }
            }
        });
    };
    /**
     * ????????????
     * @param params
     */
    TaskviewPage.prototype.getReplenishmenInfo = function (params) {
        var _this = this;
        this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].stockInner + __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].getReplenishmenInfo, __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["f" /* Method */].Get, {
            taskId: params.taskId
        }, __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["e" /* ContenType */].Content, function (result) {
            _this.nativeService.hideLoading();
            if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isNotEmpty(result.data)) {
                console.log(result.data);
                _this.navCtrl.push('ReplenishmenhomePage', { result: result.data, taskId: params.taskId });
            }
        });
    };
    /**
    * ??????????????????
    */
    TaskviewPage.prototype.billNo_KeyDown = function () {
        var _this = this;
        this.taskResult = this.tempList;
        if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isEmpty(this.taskResult))
            return;
        this.taskResult = this.taskResult.filter(function (t) { return t.taskRemark.includes(_this.billNo); });
    };
    /**
     * ??????????????????
     */
    TaskviewPage.prototype.taskPDAList = function (params) {
        var _this = this;
        this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].taskPDA + __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].taskPDAList, __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["f" /* Method */].Get, params, '', function (result) {
            _this.nativeService.hideLoading();
            _this.taskResult = result.data;
            _this.tempList = [];
            Object.assign(_this.tempList, _this.taskResult);
            if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isNotEmpty(_this.billNo)) {
                _this.billNo_KeyDown();
            }
        });
    };
    /**
     * ????????????
     */
    TaskviewPage.prototype.onRefresh = function () {
        this.nativeService.showLoading();
        this.taskPDAList({ taskTypeCd: this.refreshType });
    };
    TaskviewPage = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Component"])({
            selector: 'page-taskview',template:/*ion-inline-start:"D:\developer_tools\workspace\wms_3.0_base_pda\src\pages\taskview\taskview.html"*/'<!--\n\n  Generated template for the TaskviewPage page.\n\n\n\n  See http://ionicframework.com/docs/components/#navigation for more info on\n\n  Ionic pages and navigation.\n\n-->\n\n<ion-header>\n\n  <ion-navbar color="wmscolor">\n\n    <ion-title>????????????\n\n      <!-- <div class="title_f_name">(????????????)</div> -->\n\n    </ion-title>\n\n  </ion-navbar>\n\n\n\n  <ion-searchbar Description=\'primary\' (ionChange)="billNo_KeyDown()" [(ngModel)]="billNo" placeholder="????????????">\n\n\n\n  </ion-searchbar>\n\n  <!-- <div class="nav-tile"> \n\n    <ul>\n\n      <li>??????</li>\n\n      <li>\n\n        <ion-icon class="iconfont  iconicon_arr_right"></ion-icon>\n\n      </li>\n\n      <li>????????????</li>\n\n    </ul>\n\n  </div> -->\n\n  <div class="nav-tile">\n\n  <crumbs></crumbs>\n\n</div>\n\n  <div class="sub_header1"> \n\n    <div *ngFor="let item of subHeader" (click)="headSelected(item)"\n\n      [ngClass]="{selected:subHeaderModel.taskName === item.taskName}">\n\n      {{item.taskName}}\n\n    </div>\n\n  </div>\n\n</ion-header>\n\n<ion-content>\n\n  <div (tap)="onClick(taskItem)" *ngFor="let taskItem of taskResult; let i = index" class="title-ms">\n\n    <div class="box">\n\n      <div class="title-ms-info">\n\n          <div class="title-font-s1">\n\n            <button ion-button color="dark">{{i+1}}</button>\n\n            <div *ngIf="taskItem.taskTypeDesc!=\'\'; else elseBlock">{{taskItem.taskTypeDesc}}</div>\n\n            <ng-template #elseBlock>\n\n              <div>????????????</div>\n\n            </ng-template>\n\n            <span>???????????? {{taskItem.realQty}}/{{taskItem.planQty}}</span>\n\n          </div>\n\n          <div class="title-font-s2">\n\n            <span>{{taskItem.whName}}</span>\n\n            <!-- <span *ngIf="taskItem.taskTypeDesc!=\'\'&&taskItem.taskTypeDesc.indexOf(\'??????\')!=-1">{{taskItem.locName}}</span> -->\n\n            <div style="display: flex;flex-direction: column;text-align: right;">\n\n              <span *ngIf="taskItem.taskTypeDesc.indexOf(\'??????\')!=-1">{{taskItem.orderNo}}</span>\n\n              <span *ngIf="taskItem.taskTypeDesc.indexOf(\'??????\')!=-1">{{taskItem.sobillNo}}</span>\n\n              <span *ngIf="taskItem.taskTypeDesc.indexOf(\'??????\')==-1">{{taskItem.billNo}}</span>\n\n              <span *ngIf="taskItem.countByDesc">{{taskItem.countByDesc}}</span>\n\n            </div>\n\n          </div> \n\n        </div>\n\n        <span *ngIf="taskItem.taskTypeDesc.indexOf(\'??????\')!=-1||taskItem.taskTypeDesc.indexOf(\'??????\')!=-1">{{taskItem.cName}}</span>\n\n        <span *ngIf="taskItem.taskTypeDesc.indexOf(\'??????\')!=-1">{{taskItem.transportDate}}</span>\n\n    </div>\n\n    \n\n  </div>\n\n  <!-- <div (tap)="onClick(taskItem)" *ngFor="let taskItem of taskResult; let i = index" class="title-ms">\n\n    <div class="title-ms-info">\n\n      <div class="title-font-s1">\n\n        <button ion-button color="dark">{{i+1}}</button>\n\n        <div *ngIf="taskItem.taskTypeDesc!=\'\'; else elseBlock">{{taskItem.taskTypeDesc}}</div>\n\n        <ng-template #elseBlock>\n\n          <div>????????????</div>\n\n        </ng-template>\n\n        <span>???????????? {{taskItem.realQty}}/{{taskItem.planQty}}</span>\n\n      </div>\n\n      <div class="title-font-s2">\n\n        <div>{{taskItem.whName}}&nbsp;&nbsp;&nbsp;\n\n          <span *ngIf="taskItem.taskRemark!=\'\'&&taskItem.taskRemark.indexOf(\'??????\')!=-1">{{taskItem.locName}}</span>\n\n        </div>\n\n        \n\n        <span>{{taskItem.billNo}}</span>\n\n      </div>\n\n      <div class="ionic_right">\n\n        <ion-icon class="iconfont icon-xiangyou"></ion-icon>\n\n      </div>\n\n    </div>\n\n  </div> -->\n\n  <ion-fab right bottom>\n\n    <button (click)="onRefresh()" ion-fab>\n\n      <ion-icon name="refresh"></ion-icon>\n\n    </button>\n\n  </ion-fab>\n\n</ion-content>'/*ion-inline-end:"D:\developer_tools\workspace\wms_3.0_base_pda\src\pages\taskview\taskview.html"*/,
        }),
        __metadata("design:paramtypes", [__WEBPACK_IMPORTED_MODULE_1_ionic_angular__["l" /* NavController */],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["m" /* NavParams */],
            __WEBPACK_IMPORTED_MODULE_2__services_App_service__["a" /* AppService */],
            __WEBPACK_IMPORTED_MODULE_4__services_NativeService__["a" /* NativeService */],
            __WEBPACK_IMPORTED_MODULE_6__ionic_storage__["b" /* Storage */],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["c" /* Events */]])
    ], TaskviewPage);
    return TaskviewPage;
}());

//# sourceMappingURL=taskview.js.map

/***/ }),

/***/ 773:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "TaskviewPageModule", function() { return TaskviewPageModule; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(43);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__taskview__ = __webpack_require__(1368);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__components_crumbs_crumbs_module__ = __webpack_require__(781);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};




var TaskviewPageModule = /** @class */ (function () {
    function TaskviewPageModule() {
    }
    TaskviewPageModule = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["NgModule"])({
            declarations: [
                __WEBPACK_IMPORTED_MODULE_2__taskview__["a" /* TaskviewPage */],
            ],
            imports: [
                __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["g" /* IonicPageModule */].forChild(__WEBPACK_IMPORTED_MODULE_2__taskview__["a" /* TaskviewPage */]),
                __WEBPACK_IMPORTED_MODULE_3__components_crumbs_crumbs_module__["a" /* CrumbsModule */]
            ],
            schemas: [__WEBPACK_IMPORTED_MODULE_0__angular_core__["NO_ERRORS_SCHEMA"]]
        })
    ], TaskviewPageModule);
    return TaskviewPageModule;
}());

//# sourceMappingURL=taskview.module.js.map

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
//# sourceMappingURL=19.js.map