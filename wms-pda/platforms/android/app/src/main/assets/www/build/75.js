webpackJsonp([75],{

/***/ 1013:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return CollageTaskBoxPage; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(43);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__services_NativeService__ = __webpack_require__(64);
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
 * Generated class for the CollageTaskBoxPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */
var CollageTaskBoxPage = /** @class */ (function () {
    function CollageTaskBoxPage(navCtrl, navParams, nativeService, viewCtrl) {
        this.navCtrl = navCtrl;
        this.navParams = navParams;
        this.nativeService = nativeService;
        this.viewCtrl = viewCtrl;
        this.scanModelList = []; //扫描箱码实体集合
        this.isShow = true;
        this.isChecked = false;
        this.scanModelList = this.navParams.get('scanModelList');
        this.scanModelList.forEach(function (element) {
            element["checked"] = false;
        });
        this.resultList = this.navParams.get('result');
    }
    CollageTaskBoxPage.prototype.ionViewDidLoad = function () {
        console.log('ionViewDidLoad CollageTaskBoxPage');
    };
    CollageTaskBoxPage.prototype.exit = function () {
        this.viewCtrl.dismiss(this.scanModelList);
    };
    CollageTaskBoxPage.prototype.itemClick = function (item) {
        if (item.checked) {
            item.checked = false;
        }
        else {
            item.checked = true;
        }
        var temp = [];
        this.scanModelList.forEach(function (element) {
            if (element["checked"]) {
                temp.push(true);
            }
        });
        if (temp.length == this.scanModelList.length) {
            this.isChecked = true;
        }
        else {
            this.isChecked = false;
        }
    };
    CollageTaskBoxPage.prototype.checkBoxChage = function () {
        if (this.isChecked) {
            this.scanModelList.forEach(function (element) {
                element["checked"] = true;
            });
        }
        else {
            var temp_1 = [];
            this.scanModelList.forEach(function (element) {
                if (element["checked"]) {
                    temp_1.push(true);
                }
            });
            if (temp_1.length == 0) {
                this.scanModelList.forEach(function (element) {
                    element["checked"] = false;
                });
            }
        }
    };
    CollageTaskBoxPage.prototype.headSelected = function (item) {
        this.skuListItem = item;
    };
    CollageTaskBoxPage.prototype.btnDel = function () {
        // if (Utils.isEmpty(this.skuListItem)) {
        //   this.nativeService.showToast('请选择要删除的箱码');
        //   return;
        // }
        var temp = [];
        this.scanModelList.forEach(function (element) {
            if (element["checked"]) {
                temp.push(true);
            }
        });
        if (temp.length == 0) {
            this.nativeService.showToast('请选择要删除的箱码');
            return;
        }
        this.scanModelList = this.scanModelList.filter(function (x) { return x['checked'] != true; });
    };
    CollageTaskBoxPage = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Component"])({
            selector: 'page-collage-task-box',template:/*ion-inline-start:"D:\developer_tools\workspace\wms_3.0_base_pda\src\pages\collage-task\collage-task-box\collage-task-box.html"*/'<ion-header>\n\n  <ion-navbar color="wmscolor">\n\n    <ion-title>\n\n      拼托任务\n\n      <!-- <div class="title_f_name">(青啤五厂)</div> -->\n\n    </ion-title>\n\n    <!-- <ion-buttons end (click)="detailed()">\n\n          <button ion-button icon-only>\n\n              <ion-icon name="menu"></ion-icon>\n\n          </button>\n\n      </ion-buttons> -->\n\n  </ion-navbar>\n\n  <div class="title-bt">\n\n    <div class="nav-tile">\n\n      <!-- <div class="order-info">任务进度{{resultList.finish}}/{{resultList.total}}</div> -->\n\n      <!-- <ul>\n\n        <li>首页</li>\n\n        <li>\n\n          <ion-icon class="iconfont  iconicon_arr_right"></ion-icon>\n\n        </li>\n\n        <li>任务查看</li>\n\n        <li>\n\n          <ion-icon class="iconfont  iconicon_arr_right"></ion-icon>\n\n        </li>\n\n        <li>拼托任务</li>\n\n        <li>\n\n          <ion-icon class="iconfont  iconicon_arr_right"></ion-icon>\n\n        </li>\n\n        <li>托盘详情</li>\n\n      </ul> -->\n\n      <!-- <crumbs></crumbs> -->\n\n    </div>\n\n  </div>\n\n</ion-header>\n\n<ion-content>\n\n  <div>\n\n    <!-- 物品选显卡 -->\n\n    <div class="title-ms clearfix">\n\n      <div class="title-ms-info clearfix">\n\n        <div>单据编码：{{resultList.soBillNo}}</div>\n\n        <div>单据类型：{{resultList.billTypeName}}</div>\n\n        <div>客&emsp;&emsp;户：{{resultList.clientName}}</div>\n\n        <!-- <div *ngIf="isShow">托盘数:{{resultList.lpnList.length}}</div> -->\n\n        <div *ngIf="!isShow">总箱数:{{resultListlpnItem.detailList.length}}</div>\n\n        <div>{{resultList.whName}}</div>\n\n        <!-- <div class="ionic_right">\n\n          <ion-icon name="arrow-forward"></ion-icon>\n\n        </div> -->\n\n      </div>\n\n    </div>\n\n  </div>\n\n  <!-- 箱码列表 -->\n\n  <!-- <div>\n\n    <div class="list_item_ls_box clearfix" (click)="headSelected(item)" *ngFor="let item of scanModelList"\n\n      [ngClass]="{selected:skuListItem == item}">\n\n      <div>{{item.skuname}}</div>\n\n      <div>{{item.skucode}}</div>\n\n      <div><b style="color: orange;">{{item.qty}}</b>{{item.um}}</div>\n\n    </div>\n\n  </div> -->\n\n  <div class="my-list-header" *ngIf="scanModelList.length>0">\n\n    <ion-checkbox (ionChange)="checkBoxChage()" [(ngModel)]="isChecked"></ion-checkbox>\n\n    <span>全选/全不选</span>\n\n  </div>\n\n  <div *ngFor="let item of scanModelList;">\n\n    <div class="list-item" (click)="itemClick(item)" >\n\n      <ion-checkbox [(ngModel)]="item.checked"></ion-checkbox>\n\n      <div class="list-item-left">\n\n        <span>{{item.skuname}}</span>\n\n        <span>{{item.skucode}}</span>\n\n      </div>\n\n      <span><b style="color: orange;">{{item.qty}}</b>{{item.um}}</span>\n\n    </div>\n\n  </div>\n\n</ion-content>\n\n<ion-footer>\n\n  <div class="btn-box">\n\n    <button no-dbl-click ion-button block class="btn-l" (tap)="exit()">\n\n      返回[Esc]\n\n    </button>\n\n    <button no-dbl-click ion-button block class="btn-rd" (tap)="btnDel()">\n\n      删除[Esc]\n\n    </button>\n\n  </div>\n\n</ion-footer>'/*ion-inline-end:"D:\developer_tools\workspace\wms_3.0_base_pda\src\pages\collage-task\collage-task-box\collage-task-box.html"*/,
        }),
        __metadata("design:paramtypes", [__WEBPACK_IMPORTED_MODULE_1_ionic_angular__["l" /* NavController */], __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["m" /* NavParams */],
            __WEBPACK_IMPORTED_MODULE_2__services_NativeService__["a" /* NativeService */], __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["r" /* ViewController */]])
    ], CollageTaskBoxPage);
    return CollageTaskBoxPage;
}());

//# sourceMappingURL=collage-task-box.js.map

/***/ }),

/***/ 714:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "CollageTaskBoxPageModule", function() { return CollageTaskBoxPageModule; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(43);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__collage_task_box__ = __webpack_require__(1013);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};



// import { CrumbsModule } from '../../../components/crumbs/crumbs.module';
var CollageTaskBoxPageModule = /** @class */ (function () {
    function CollageTaskBoxPageModule() {
    }
    CollageTaskBoxPageModule = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["NgModule"])({
            declarations: [
                __WEBPACK_IMPORTED_MODULE_2__collage_task_box__["a" /* CollageTaskBoxPage */],
            ],
            imports: [
                __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["g" /* IonicPageModule */].forChild(__WEBPACK_IMPORTED_MODULE_2__collage_task_box__["a" /* CollageTaskBoxPage */]),
            ],
            schemas: [__WEBPACK_IMPORTED_MODULE_0__angular_core__["NO_ERRORS_SCHEMA"]]
        })
    ], CollageTaskBoxPageModule);
    return CollageTaskBoxPageModule;
}());

//# sourceMappingURL=collage-task-box.module.js.map

/***/ })

});
//# sourceMappingURL=75.js.map