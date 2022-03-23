webpackJsonp([67],{

/***/ 1057:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return PopoverPage; });
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
 * Generated class for the PopoverPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */
var PopoverPage = /** @class */ (function () {
    function PopoverPage(navCtrl, navParams, viewCtrl) {
        this.navCtrl = navCtrl;
        this.navParams = navParams;
        this.viewCtrl = viewCtrl;
        this.isShow = true;
        this.isShow1 = true;
        this.isShow11 = true;
        this.isShow12 = true;
        if (navParams.get('show_item_3') != undefined) {
            this.isShow = navParams.get('show_item_3');
        }
        if (navParams.get('show_item_4') != undefined) {
            this.isShow1 = navParams.get('show_item_4');
        }
        if (navParams.get('show_item_5') != undefined) {
            this.isShow11 = navParams.get('show_item_5');
        }
        if (navParams.get('show_item_6') != undefined) {
            this.isShow12 = navParams.get('show_item_6');
        }
    }
    PopoverPage.prototype.ionViewDidLoad = function () {
        console.log('ionViewDidLoad PopoverPage');
    };
    PopoverPage.prototype.onPick = function (item) {
        this.viewCtrl.dismiss(item);
    };
    PopoverPage = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Component"])({
            selector: 'page-popover',template:/*ion-inline-start:"D:\developer_tools\workspace\wms_3.0_base_pda\src\shared\popover\popover.html"*/'<ion-item (click)="onPick(\'allCheck\')">\n\n  <ion-icon name="pie-chart-outline"></ion-icon>\n\n  <ion-label>全选</ion-label>\n\n</ion-item>\n\n<ion-item (click)="onPick(\'clearCheck\')">\n\n  <ion-icon name="pulse-outline"></ion-icon>\n\n  <ion-label>取消全选</ion-label>\n\n</ion-item>\n\n<ion-item (click)="onPick(\'show\')" *ngIf="isShow11">\n\n  <ion-icon name="bar-chart-outline"></ion-icon>\n\n  <ion-label>显示已扫物料</ion-label>\n\n</ion-item>\n\n<ion-item (click)="onPick(\'hide\')" *ngIf="isShow12">\n\n  <ion-icon name="bar-chart-outline"></ion-icon>\n\n  <ion-label>显示当前物料</ion-label>\n\n</ion-item>\n\n<ion-item (click)="onPick(\'del\')" *ngIf="isShow">\n\n  <ion-icon name="bar-chart-outline"></ion-icon>\n\n  <ion-label>删除选中</ion-label>\n\n</ion-item>\n\n<ion-item (click)="onPick(\'complated\')" *ngIf="isShow1">\n\n  <ion-icon name="bar-chart-outline"></ion-icon>\n\n  <ion-label>完成</ion-label>\n\n</ion-item>\n\n'/*ion-inline-end:"D:\developer_tools\workspace\wms_3.0_base_pda\src\shared\popover\popover.html"*/,
        }),
        __metadata("design:paramtypes", [__WEBPACK_IMPORTED_MODULE_1_ionic_angular__["l" /* NavController */],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["m" /* NavParams */],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["r" /* ViewController */]])
    ], PopoverPage);
    return PopoverPage;
}());

//# sourceMappingURL=popover.js.map

/***/ }),

/***/ 757:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "PopoverPageModule", function() { return PopoverPageModule; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(43);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__popover__ = __webpack_require__(1057);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};



var PopoverPageModule = /** @class */ (function () {
    function PopoverPageModule() {
    }
    PopoverPageModule = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["NgModule"])({
            declarations: [
                __WEBPACK_IMPORTED_MODULE_2__popover__["a" /* PopoverPage */],
            ],
            imports: [
                __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["g" /* IonicPageModule */].forChild(__WEBPACK_IMPORTED_MODULE_2__popover__["a" /* PopoverPage */]),
            ],
        })
    ], PopoverPageModule);
    return PopoverPageModule;
}());

//# sourceMappingURL=popover.module.js.map

/***/ })

});
//# sourceMappingURL=67.js.map