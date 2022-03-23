webpackJsonp([72],{

/***/ 1025:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return HomePage; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(43);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__ionic_native_status_bar__ = __webpack_require__(90);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__services_App_service__ = __webpack_require__(157);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__ionic_storage__ = __webpack_require__(66);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5__services_Utils__ = __webpack_require__(89);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_6__services_NativeService__ = __webpack_require__(64);
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
 * Generated class for the HomePage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */
var HomePage = /** @class */ (function () {
    function HomePage(navCtrl, navParams, platform, statusBar, appService, storage, nativeService, app) {
        this.navCtrl = navCtrl;
        this.navParams = navParams;
        this.platform = platform;
        this.statusBar = statusBar;
        this.appService = appService;
        this.storage = storage;
        this.nativeService = nativeService;
        this.app = app;
        this.menuRole = [];
        console.log('父组件');
    }
    HomePage.prototype.ionViewWillEnter = function () {
        var _this = this;
        console.log('父组件生命周期');
        //权限过滤器
        this.storage.get('routes').then(function (val) {
            if (__WEBPACK_IMPORTED_MODULE_5__services_Utils__["a" /* Utils */].isNotEmpty(val)) {
                _this.menuRole = val.children;
                // if (this.menuRole.length < 15) {
                //   let count = 15 - this.menuRole.length;
                //   for (let index = 0; index < count; index++) {
                //     this.menuRole.push({ source: '' });
                //   }
                // }
            }
        });
        this.statusBar.backgroundColorByHexString("#33cbcc"); // => #FFAABB
        this.storage.get('userInfo').then(function (val) {
            if (__WEBPACK_IMPORTED_MODULE_5__services_Utils__["a" /* Utils */].isNotEmpty(val)) {
                _this.nickName = val.nick_name;
                // this.password = val.password;
            }
        });
    };
    HomePage.prototype.expression = function (event) {
        // if (event.path === 'CheckinhomePage') {//收货
        //   if (Utils.isEmpty(event.children)) {
        //     this.nativeService.showToast('当前用户不存在收货子权限');
        //     return;
        //   }
        //   this.navCtrl.push(event.path, { menuRole: event.children });
        // } else if (event.path === 'TakeawayhomePage') {//拣货
        //   if (Utils.isEmpty(event.children)) {
        //     this.nativeService.showToast('当前用户不存在拣货子权限');
        //     return;
        //   }
        //   this.navCtrl.push(event.path, { menuRole: event.children });
        // } else if (event.path === 'CyclecounthomePage') {//盘点
        //   if (Utils.isEmpty(event.children)) {
        //     this.nativeService.showToast('当前用户不存在盘点子权限');
        //     return;
        //   }
        //   this.navCtrl.push(event.path, { menuRole: event.children });
        // } else if (event.path === 'MovehomePage') {//移动
        //   if (Utils.isEmpty(event.children)) {
        //     this.nativeService.showToast('当前用户不存在移动子权限');
        //     return;
        //   }
        //   this.navCtrl.push(event.path, { menuRole: event.children });
        // } else if (event.path === 'StockhomePage') {//库存
        //   if (Utils.isEmpty(event.children)) {
        //     this.nativeService.showToast('当前用户不存在移动子权限');
        //     return;
        //   }
        //   this.navCtrl.push(event.path, { menuRole: event.children });
        // } else if (event.path == 'ShipmenthomePage') {//装车
        //   if (Utils.isEmpty(event.children)) {
        //     this.nativeService.showToast('当前用户不存在装车子权限');
        //     return;
        //   }
        //   this.navCtrl.push(event.path, { menuRole: event.children });
        // } else if (event.path == 'PutawayhomePage') {//上架
        //   if (Utils.isEmpty(event.children)) {
        //     this.nativeService.showToast('当前用户不存在装车子权限');
        //     return;
        //   }
        //   this.navCtrl.push(event.path, { menuRole: event.children });
        // } eis.navCtrl.push(event.path);
        // }lse {
        //   th
        // if (Utils.isEmpty(event.children)) {
        //   this.nativeService.showToast('当前用户不存在装车子权限');
        //   return;
        // }
        if (__WEBPACK_IMPORTED_MODULE_5__services_Utils__["a" /* Utils */].isEmpty(event.children)) {
            this.navCtrl.push(event.path);
        }
        else {
            if (event.children.length == 1) {
                this.navCtrl.push(event.children[0].path);
            }
            else {
                this.navCtrl.push(event.path, { menuRole: event.children });
            }
        }
    };
    /**
     * 个人设置
     */
    HomePage.prototype.userSetting = function () {
        this.navCtrl.push('SettingPage');
    };
    /**
     * 退出APP
     */
    HomePage.prototype.detailed = function () {
        //this.navCtrl.pop();
        this.app.getRootNav().setRoot('LoginPage');
    };
    HomePage.prototype.exit = function () {
        this.platform.exitApp();
    };
    HomePage = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Component"])({
            selector: 'page-home',template:/*ion-inline-start:"D:\developer_tools\workspace\wms_3.0_base_pda\src\pages\home\home.html"*/'<!--\n\n  Generated template for the HomePage page.\n\n\n\n  See http://ionicframework.com/docs/components/#navigation for more info on\n\n  Ionic pages and navigation.\n\n-->\n\n<ion-header>\n\n  <ion-navbar color="wmscolor">\n\n    <ion-title>首页\n\n      <!-- <div class="title_f_name">(青啤五厂)</div> -->\n\n    </ion-title>\n\n    <ion-buttons class="user_btn" start (click)="userSetting()">\n\n      <button ion-button icon-only>\n\n        <ion-icon class="iconfont iconyonghufangkeshu"></ion-icon>\n\n        {{nickName}}\n\n      </button>\n\n    </ion-buttons>\n\n    <ion-buttons end (click)="detailed()">\n\n      <button ion-button icon-only>\n\n        <ion-icon class="iconfont iconHome_icon_tuichu"></ion-icon>\n\n      </button>\n\n    </ion-buttons>\n\n  </ion-navbar>\n\n  <div class="title-bt-after"></div>\n\n  <!-- <div class="title-bt">\n\n    <div class="factory">\n\n      <span>待完成任务：200</span>\n\n    </div>\n\n\n\n    <div class="centerdiv"></div>\n\n    <div class="finish_task">\n\n      <span>已完成任务：200</span>\n\n    </div>\n\n  </div> -->\n\n</ion-header>\n\n<ion-content>\n\n  <div class="login_content">\n\n    <ion-row>\n\n      <ion-col col-4 *ngFor="let item of menuRole; let _i = index">\n\n        <div *ngIf="item.source!=\'\'" class="logon_ionic" (click)="expression(item)">\n\n          <span>{{_i+1}}</span>\n\n          <ion-icon class="{{item.source}}"></ion-icon>\n\n          <span>{{item.name}}</span>\n\n        </div>\n\n      </ion-col>\n\n    </ion-row>\n\n  </div>\n\n\n\n  <!-- <serial-number></serial-number> -->\n\n</ion-content>'/*ion-inline-end:"D:\developer_tools\workspace\wms_3.0_base_pda\src\pages\home\home.html"*/,
        }),
        __metadata("design:paramtypes", [__WEBPACK_IMPORTED_MODULE_1_ionic_angular__["l" /* NavController */],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["m" /* NavParams */],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["o" /* Platform */],
            __WEBPACK_IMPORTED_MODULE_2__ionic_native_status_bar__["a" /* StatusBar */],
            __WEBPACK_IMPORTED_MODULE_3__services_App_service__["a" /* AppService */],
            __WEBPACK_IMPORTED_MODULE_4__ionic_storage__["b" /* Storage */],
            __WEBPACK_IMPORTED_MODULE_6__services_NativeService__["a" /* NativeService */],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["b" /* App */]])
    ], HomePage);
    return HomePage;
}());

//# sourceMappingURL=home.js.map

/***/ }),

/***/ 726:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "HomePageModule", function() { return HomePageModule; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(43);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__home__ = __webpack_require__(1025);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};



var HomePageModule = /** @class */ (function () {
    function HomePageModule() {
    }
    HomePageModule = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["NgModule"])({
            declarations: [
                __WEBPACK_IMPORTED_MODULE_2__home__["a" /* HomePage */]
            ],
            imports: [
                __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["g" /* IonicPageModule */].forChild(__WEBPACK_IMPORTED_MODULE_2__home__["a" /* HomePage */]),
            ],
        })
    ], HomePageModule);
    return HomePageModule;
}());

//# sourceMappingURL=home.module.js.map

/***/ })

});
//# sourceMappingURL=72.js.map