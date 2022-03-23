webpackJsonp([76],{

/***/ 1012:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return ClockInHomePage; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(43);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__services_App_service__ = __webpack_require__(157);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__services_Utils__ = __webpack_require__(89);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__services_NativeService__ = __webpack_require__(64);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__ = __webpack_require__(65);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_6__ionic_storage__ = __webpack_require__(66);
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
 * Generated class for the ClockInHomePage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */
var ClockInHomePage = /** @class */ (function () {
    function ClockInHomePage(navCtrl, navParams, appService, nativeService, storage) {
        this.navCtrl = navCtrl;
        this.navParams = navParams;
        this.appService = appService;
        this.nativeService = nativeService;
        this.storage = storage;
        this.Modelvalue = ''; //签到值
        this.clockList = []; //签到记录
        this.clockItem = null; //人员记录
    }
    ClockInHomePage.prototype.setDom = function () {
        if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isEmpty(this.clockItem)) {
            this.Modelvalue = '签到';
            this.setDomInfo();
            return;
        }
        if (this.clockItem.loginStatus == 0) {
            this.Modelvalue = '签到';
        }
        else {
            this.Modelvalue = '签退';
        }
        this.setDomInfo();
    };
    ClockInHomePage.prototype.setDomInfo = function () {
        var oStu = document.getElementById('element2');
        oStu.setAttribute('data-text', this.Modelvalue); //替换
    };
    ClockInHomePage.prototype.ionViewDidLoad = function () {
        var _this = this;
        this.time = setInterval(function () {
            var date = new Date();
            var year = date.getFullYear(); //获取当前年份 
            var mon = date.getMonth() + 1; //获取当前月份 
            var da = date.getDate(); //获取当前日 
            var day = date.getDay(); //获取当前星期几 
            var h = date.getHours(); //获取小时 
            var m = date.getMinutes(); //获取分钟 
            var s = date.getSeconds(); //获取秒 
            var d = document.getElementById('date');
            d.innerHTML = year + '年' + mon + '月' + da + '日' + '&emsp;' + h + '时' + m + '分' + s + '秒';
        }, 1000);
        //获取当前人员签到
        this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].api + __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].UserOnlineList, __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["f" /* Method */].Get, '', '', function (result) {
            _this.clockItem = result.data[0];
            _this.setDom();
        });
        this.clockGetApi();
    };
    /**
     * 签到
     */
    ClockInHomePage.prototype.onClick = function () {
        this.clockInApi();
    };
    /**
     * 获取签到列表
     */
    ClockInHomePage.prototype.clockInApi = function () {
        var _this = this;
        this.storage.get('access_token').then(function (val) {
            if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isNotEmpty(val)) {
                var parmams = {
                    token: val,
                    loginStatus: '',
                    suoId: ''
                };
                if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isEmpty(_this.clockItem)) {
                    parmams['loginStatus'] = '1';
                }
                else {
                    parmams['suoId'] = _this.clockItem.suoId;
                    parmams['loginStatus'] = _this.clockItem.loginStatus;
                    if (_this.clockItem.loginStatus == '0') {
                        parmams['loginStatus'] = '1';
                    }
                    else {
                        parmams['loginStatus'] = '0';
                    }
                }
                _this.nativeService.showLoading();
                _this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].api + __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].UserOnlinesubmit, __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["f" /* Method */].Post, parmams, __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["e" /* ContenType */].Json, function (result) {
                    _this.nativeService.hideLoading();
                    //签到日志
                    _this.clockList = result.data.userRegister;
                    //用户签到状态
                    _this.clockItem = result.data.userOnline;
                    _this.setDom();
                });
            }
        });
    };
    /**
     * 当天日志列表
     */
    ClockInHomePage.prototype.clockGetApi = function () {
        var _this = this;
        this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].api + __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].UserRegisterList, __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["f" /* Method */].Get, '', '', function (result) {
            _this.clockList = result.data;
        });
    };
    ClockInHomePage.prototype.ionViewWillLeave = function () {
        clearInterval(this.time);
    };
    ClockInHomePage.prototype.detailed = function () {
        this.navCtrl.push('ClockInPage');
    };
    ClockInHomePage = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Component"])({
            selector: 'page-clock-in-home',template:/*ion-inline-start:"D:\developer_tools\workspace\wms_3.0_base_pda\src\pages\clock-in-home\clock-in-home.html"*/'<!--\n\n  Generated template for the ClockInHomePage page.\n\n\n\n  See http://ionicframework.com/docs/components/#navigation for more info on\n\n  Ionic pages and navigation.\n\n-->\n\n<ion-header>\n\n  <ion-navbar color="wmscolor">\n\n    <ion-title>签到</ion-title>\n\n    <ion-buttons end (click)="detailed()">\n\n      <button ion-button icon-only>\n\n        <ion-icon class="iconfont iconrili"></ion-icon>\n\n      </button>\n\n    </ion-buttons>\n\n  </ion-navbar>\n\n  <div class="title-bt">\n\n  </div>\n\n</ion-header>\n\n\n\n<ion-content>\n\n  <div id="date">2020年0月0日&emsp;00时00分00秒</div>\n\n  <a (tap)="onClick()" id="element2" data-text="" class="element2"></a>\n\n  <!-- <div class="place"> -->\n\n  <!-- <span><ion-icon class="iconfont iconrili"></ion-icon></span> -->\n\n  <!-- 欣天新</div> -->\n\n  <p>签到状态</p>\n\n  <div class="clock-list">\n\n    <ion-list>\n\n      <ion-scroll id="scroll" scrollY="true" style="height:20rem">\n\n        <ion-item *ngFor="let item of clockList">\n\n          <ion-thumbnail item-start>\n\n            <ion-icon *ngIf="item.loginStatus==0;else elseionic" style=" color:#0a8b8b;"\n\n              class="iconfont iconyonghufangkeshu"></ion-icon>\n\n            <ng-template #elseionic>\n\n              <ion-icon style=" color:#e0e5e6;" class="iconfont iconyonghufangkeshu"></ion-icon>\n\n            </ng-template>\n\n          </ion-thumbnail>\n\n          <h2 *ngIf="item.loginStatus==0; else elseBlock">{{item.userName}}&emsp;签退</h2>\n\n          <ng-template #elseBlock>\n\n            <h2>{{item.userName}}&emsp;签到</h2>\n\n          </ng-template>\n\n          <p>{{item.lastLoginTime}}</p>\n\n        </ion-item>\n\n      </ion-scroll>\n\n    </ion-list>\n\n  </div>\n\n</ion-content>'/*ion-inline-end:"D:\developer_tools\workspace\wms_3.0_base_pda\src\pages\clock-in-home\clock-in-home.html"*/,
        }),
        __metadata("design:paramtypes", [__WEBPACK_IMPORTED_MODULE_1_ionic_angular__["l" /* NavController */],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["m" /* NavParams */],
            __WEBPACK_IMPORTED_MODULE_2__services_App_service__["a" /* AppService */],
            __WEBPACK_IMPORTED_MODULE_4__services_NativeService__["a" /* NativeService */],
            __WEBPACK_IMPORTED_MODULE_6__ionic_storage__["b" /* Storage */]])
    ], ClockInHomePage);
    return ClockInHomePage;
}());

//# sourceMappingURL=clock-in-home.js.map

/***/ }),

/***/ 713:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "ClockInHomePageModule", function() { return ClockInHomePageModule; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(43);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__clock_in_home__ = __webpack_require__(1012);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};



var ClockInHomePageModule = /** @class */ (function () {
    function ClockInHomePageModule() {
    }
    ClockInHomePageModule = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["NgModule"])({
            declarations: [
                __WEBPACK_IMPORTED_MODULE_2__clock_in_home__["a" /* ClockInHomePage */],
            ],
            imports: [
                __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["g" /* IonicPageModule */].forChild(__WEBPACK_IMPORTED_MODULE_2__clock_in_home__["a" /* ClockInHomePage */]),
            ],
        })
    ], ClockInHomePageModule);
    return ClockInHomePageModule;
}());

//# sourceMappingURL=clock-in-home.module.js.map

/***/ })

});
//# sourceMappingURL=76.js.map