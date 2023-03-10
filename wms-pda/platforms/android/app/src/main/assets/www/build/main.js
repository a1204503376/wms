webpackJsonp([79],{

/***/ 157:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return AppService; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__angular_common_http__ = __webpack_require__(318);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2_ionic_angular__ = __webpack_require__(43);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__utils_common__ = __webpack_require__(91);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__ionic_storage__ = __webpack_require__(66);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5__Utils__ = __webpack_require__(89);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_6__NativeService__ = __webpack_require__(64);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_7__utils_appConstant__ = __webpack_require__(65);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_8__services_AppGlobal__ = __webpack_require__(360);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : new P(function (resolve) { resolve(result.value); }).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
var __generator = (this && this.__generator) || function (thisArg, body) {
    var _ = { label: 0, sent: function() { if (t[0] & 1) throw t[1]; return t[1]; }, trys: [], ops: [] }, f, y, t, g;
    return g = { next: verb(0), "throw": verb(1), "return": verb(2) }, typeof Symbol === "function" && (g[Symbol.iterator] = function() { return this; }), g;
    function verb(n) { return function (v) { return step([n, v]); }; }
    function step(op) {
        if (f) throw new TypeError("Generator is already executing.");
        while (_) try {
            if (f = 1, y && (t = y[op[0] & 2 ? "return" : op[0] ? "throw" : "next"]) && !(t = t.call(y, op[1])).done) return t;
            if (y = 0, t) op = [0, t.value];
            switch (op[0]) {
                case 0: case 1: t = op; break;
                case 4: _.label++; return { value: op[1], done: false };
                case 5: _.label++; y = op[1]; op = [0]; continue;
                case 7: op = _.ops.pop(); _.trys.pop(); continue;
                default:
                    if (!(t = _.trys, t = t.length > 0 && t[t.length - 1]) && (op[0] === 6 || op[0] === 2)) { _ = 0; continue; }
                    if (op[0] === 3 && (!t || (op[1] > t[0] && op[1] < t[3]))) { _.label = op[1]; break; }
                    if (op[0] === 6 && _.label < t[1]) { _.label = t[1]; t = op; break; }
                    if (t && _.label < t[2]) { _.label = t[2]; _.ops.push(op); break; }
                    if (t[2]) _.ops.pop();
                    _.trys.pop(); continue;
            }
            op = body.call(thisArg, _);
        } catch (e) { op = [6, e]; y = 0; } finally { f = t = 0; }
        if (op[0] & 5) throw op[1]; return { value: op[0] ? op[1] : void 0, done: true };
    }
};









var AppService = /** @class */ (function () {
    function AppService(http, alertCtrl, toastCtrl, common, app, storage, nativeService) {
        this.http = http;
        this.alertCtrl = alertCtrl;
        this.toastCtrl = toastCtrl;
        this.common = common;
        this.app = app;
        this.storage = storage;
        this.nativeService = nativeService;
    }
    AppService.prototype.setApiIp = function (ip) {
        this.common.setData('apiIp', ip);
    };
    AppService.prototype.setTenantId = function (id) {
        this.common.setData('tenantId', id);
    };
    AppService.prototype.httpRequest = function (url, method, body, ContentType, callback) {
        return __awaiter(this, void 0, void 0, function () {
            var _this = this;
            var currentTime, Seconds;
            return __generator(this, function (_a) {
                if (__WEBPACK_IMPORTED_MODULE_5__Utils__["a" /* Utils */].isEmpty(this.sysDate)) {
                    this.sysDate = new Date();
                    this.httpRequestExecute(url, method, body, ContentType, callback);
                }
                else {
                    currentTime = new Date();
                    Seconds = (currentTime.getTime() - this.sysDate.getTime()) / 1000;
                    if (Seconds >= 600) {
                        //??????token
                        this.storage.get('userInfo').then(function (val) {
                            if (__WEBPACK_IMPORTED_MODULE_5__Utils__["a" /* Utils */].isNotEmpty(val)) {
                                var bodyRef = "grant_type=refresh_token" + "&refresh_token=" + val.refresh_token + "&tenantId=" + '000000';
                                _this.refreshToken(__WEBPACK_IMPORTED_MODULE_7__utils_appConstant__["b" /* Api */].api + __WEBPACK_IMPORTED_MODULE_7__utils_appConstant__["b" /* Api */].token, __WEBPACK_IMPORTED_MODULE_7__utils_appConstant__["f" /* Method */].Post, bodyRef, __WEBPACK_IMPORTED_MODULE_7__utils_appConstant__["e" /* ContenType */].Form, function (result) {
                                    _this.storage.set('access_token', result.access_token).then(function (val) {
                                        _this.storage.set('userInfo', result).then(function (valUser) {
                                            _this.sysDate = new Date();
                                            _this.httpRequestExecute(url, method, body, ContentType, callback);
                                        });
                                    });
                                });
                            }
                        });
                    }
                    else {
                        this.httpRequestExecute(url, method, body, ContentType, callback);
                    }
                }
                return [2 /*return*/];
            });
        });
    };
    AppService.prototype.httpRequestExecute = function (url, method, body, ContentType, callback) {
        var _this = this;
        var requestHeader;
        this.storage.get('access_token').then(function (val) {
            if (!__WEBPACK_IMPORTED_MODULE_5__Utils__["a" /* Utils */].isEmpty(val)) {
                requestHeader = new __WEBPACK_IMPORTED_MODULE_1__angular_common_http__["c" /* HttpHeaders */]({
                    'Method': method,
                    'Content-Type': ContentType,
                    'Authorization': 'Basic c3dvcmQ6c3dvcmRfc2VjcmV0',
                    'isNeedReLogin': 'true',
                    'Blade-Auth': 'bearer ' + val,
                    'isReady': '',
                    'observe': 'response'
                });
            }
            else {
                requestHeader = new __WEBPACK_IMPORTED_MODULE_1__angular_common_http__["c" /* HttpHeaders */]({
                    'Method': method,
                    'Content-Type': ContentType,
                    'Authorization': 'Basic c3dvcmQ6c3dvcmRfc2VjcmV0',
                    'isNeedReLogin': 'true',
                    'isReady': '',
                });
            }
            _this.storage.get('apiIp').then(function (val) {
                var apiIp = "http://" + val;
                //??????get??????url
                if (method != __WEBPACK_IMPORTED_MODULE_7__utils_appConstant__["f" /* Method */].Post) {
                    if (__WEBPACK_IMPORTED_MODULE_5__Utils__["a" /* Utils */].isNotEmpty(body)) {
                        var parmsArr = Object.keys(body);
                        if (parmsArr.length == 1) {
                            // url += '?' + parmsArr[0] + '=' + Utils.urlFormat(body[parmsArr[0]]);
                            url += '?' + parmsArr[0] + '=' + body[parmsArr[0]];
                        }
                        else {
                            for (var index = 0; index < parmsArr.length; index++) {
                                if (index == 0) {
                                    url += '?' + parmsArr[0] + '=' + body[parmsArr[0]];
                                }
                                else {
                                    url += '&' + parmsArr[index] + '=' + body[parmsArr[index]];
                                }
                            }
                        }
                    }
                }
                _this.http.request(method, apiIp + url, {
                    observe: 'response',
                    headers: requestHeader,
                    responseType: "json",
                    body: body,
                }).subscribe(function (response) {
                    _this.nativeService.hideLoading();
                    if (__WEBPACK_IMPORTED_MODULE_5__Utils__["a" /* Utils */].isNotEmpty(response.body['error_code'])) {
                        _this.toast(response.body['error_description']);
                        _this.nativeService.hideLoading();
                        // this.nativeService.hide();
                        return;
                    }
                    if (__WEBPACK_IMPORTED_MODULE_5__Utils__["a" /* Utils */].isNotEmpty(response.body['code']) && response.body['code'] == '401') {
                        _this.toast(response.body['msg']);
                        _this.nativeService.hideLoading();
                        return;
                    }
                    if (__WEBPACK_IMPORTED_MODULE_5__Utils__["a" /* Utils */].isNotEmpty(response.body['code']) && response.body['code'] == '500') {
                        _this.toast(response.body['msg']);
                        _this.nativeService.hideLoading();
                        return;
                    }
                    callback(response.body == null ? null : response.body);
                }, function (error) {
                    _this.handleError(error);
                });
            });
        });
    };
    AppService.prototype.refreshToken = function (url, method, body, ContentType, callback) {
        return __awaiter(this, void 0, void 0, function () {
            var _this = this;
            var requestHeader;
            return __generator(this, function (_a) {
                requestHeader = new __WEBPACK_IMPORTED_MODULE_1__angular_common_http__["c" /* HttpHeaders */]({
                    'Method': method,
                    'Content-Type': ContentType,
                    'Authorization': 'Basic c3dvcmQ6c3dvcmRfc2VjcmV0',
                    'isNeedReLogin': 'true',
                    'isReady': ''
                });
                this.storage.get('apiIp').then(function (val) {
                    var apiIp = "http://" + val;
                    //??????get??????url
                    if (method != __WEBPACK_IMPORTED_MODULE_7__utils_appConstant__["f" /* Method */].Post) {
                        if (__WEBPACK_IMPORTED_MODULE_5__Utils__["a" /* Utils */].isNotEmpty(body)) {
                            var parmsArr = Object.keys(body);
                            if (parmsArr.length == 1) {
                                url += '?' + parmsArr[0] + '=' + __WEBPACK_IMPORTED_MODULE_5__Utils__["a" /* Utils */].urlFormat(body[parmsArr[0]]);
                            }
                            else {
                                for (var index = 0; index < parmsArr.length; index++) {
                                    if (index == 0) {
                                        url += '?' + parmsArr[0] + '=' + __WEBPACK_IMPORTED_MODULE_5__Utils__["a" /* Utils */].urlFormat(body[parmsArr[0]]);
                                    }
                                    else {
                                        url += '&' + parmsArr[index] + '=' + __WEBPACK_IMPORTED_MODULE_5__Utils__["a" /* Utils */].urlFormat(body[parmsArr[index]]);
                                    }
                                }
                            }
                        }
                    }
                    _this.http.request(method, apiIp + url, {
                        observe: 'response',
                        headers: requestHeader,
                        responseType: "json",
                        body: body,
                    }).subscribe(function (response) {
                        _this.nativeService.hideLoading();
                        if (__WEBPACK_IMPORTED_MODULE_5__Utils__["a" /* Utils */].isNotEmpty(response.body['error_code'])) {
                            _this.toast(response.body['error_description']);
                            _this.nativeService.hideLoading();
                            return;
                        }
                        if (__WEBPACK_IMPORTED_MODULE_5__Utils__["a" /* Utils */].isNotEmpty(response.body['code']) && response.body['code'] == '401') {
                            _this.toast(response.body['msg']);
                            _this.nativeService.hideLoading();
                            return;
                        }
                        if (__WEBPACK_IMPORTED_MODULE_5__Utils__["a" /* Utils */].isNotEmpty(response.body['code']) && response.body['code'] == '500') {
                            _this.toast(response.body['msg']);
                            _this.nativeService.hideLoading();
                            return;
                        }
                        callback(response.body == null ? null : response.body);
                    }, function (error) {
                        _this.handleError(error);
                    });
                });
                return [2 /*return*/];
            });
        });
    };
    AppService.prototype.handleError = function (error) {
        return __awaiter(this, void 0, void 0, function () {
            var msg, userInfo;
            return __generator(this, function (_a) {
                switch (_a.label) {
                    case 0:
                        __WEBPACK_IMPORTED_MODULE_8__services_AppGlobal__["a" /* AppGlobal */].scanFlag = true;
                        msg = '';
                        this.nativeService.hideLoading();
                        if (!(error.status == 401)) return [3 /*break*/, 2];
                        return [4 /*yield*/, this.storage.get('userInfo')];
                    case 1:
                        userInfo = _a.sent();
                        this.common.setData('userInfo', userInfo);
                        this.toast(error.msg);
                        this.app.getRootNav().push('LoginPage');
                        this.app.getRootNav().setRoot('LoginPage');
                        return [2 /*return*/];
                    case 2:
                        if (error.status == 404) {
                            msg = '?????????????????????(code???404)';
                            console.error(msg + '??????????????????????????????');
                        }
                        if (error.status == 500) {
                            msg = error.error.msg;
                        }
                        if (msg != '') {
                            this.toast(msg);
                        }
                        return [2 /*return*/];
                }
            });
        });
    };
    AppService.prototype.alert = function (message, callback) {
        if (callback) {
            var alert_1 = this.alertCtrl.create({
                title: '??????',
                message: message,
                buttons: [{
                        text: "??????",
                        handler: function (data) {
                            callback();
                        }
                    }]
            });
            alert_1.present();
        }
        else {
            var alert_2 = this.alertCtrl.create({
                title: '??????',
                message: message,
                buttons: ["??????"]
            });
            alert_2.present();
        }
    };
    AppService.prototype.toast = function (message, callback) {
        var toast = this.toastCtrl.create({
            message: message,
            duration: 2000,
            position: 'middle',
            dismissOnPageChange: true,
        });
        toast.present();
        if (callback) {
            callback();
        }
    };
    AppService = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Injectable"])(),
        __metadata("design:paramtypes", [__WEBPACK_IMPORTED_MODULE_1__angular_common_http__["a" /* HttpClient */],
            __WEBPACK_IMPORTED_MODULE_2_ionic_angular__["a" /* AlertController */],
            __WEBPACK_IMPORTED_MODULE_2_ionic_angular__["q" /* ToastController */],
            __WEBPACK_IMPORTED_MODULE_3__utils_common__["a" /* CommonService */],
            __WEBPACK_IMPORTED_MODULE_2_ionic_angular__["b" /* App */],
            __WEBPACK_IMPORTED_MODULE_4__ionic_storage__["b" /* Storage */],
            __WEBPACK_IMPORTED_MODULE_6__NativeService__["a" /* NativeService */]])
    ], AppService);
    return AppService;
}());

//# sourceMappingURL=App.service.js.map

/***/ }),

/***/ 158:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return SkuLotModel; });
var SkuLotModel = /** @class */ (function () {
    function SkuLotModel() {
        this.skulot1 = '';
        this.skulot2 = '';
        this.skulot3 = '';
        this.skulot4 = '';
        this.skulot5 = '';
        this.skulot6 = '';
        this.skulot7 = '';
        this.skulot8 = '';
        this.skulot9 = '';
        this.skulot10 = '';
        this.skulot11 = '';
        this.skulot12 = '';
        this.skulot13 = '';
        this.skulot14 = '';
        this.skulot15 = '';
        this.skulot16 = '';
        this.skulot17 = '';
        this.skulot18 = '';
        this.skulot19 = '';
        this.skulot20 = '';
        this.skulot21 = '';
        this.skulot22 = '';
        this.skulot23 = '';
        this.skulot24 = '';
        this.skulot25 = '';
        this.skulot26 = '';
        this.skulot27 = '';
        this.skulot28 = '';
        this.skulot29 = '';
        this.skulot30 = '';
        this.skuLotName1 = '';
        this.skuLotName2 = '';
        this.skuLotName3 = '';
        this.skuLotName4 = '';
        this.skuLotName5 = '';
        this.skuLotName6 = '';
        this.skuLotName7 = '';
        this.skuLotName8 = '';
        this.skuLotName9 = '';
        this.skuLotName10 = '';
        this.skuLotName11 = '';
        this.skuLotName12 = '';
        this.skuLotName13 = '';
        this.skuLotName14 = '';
        this.skuLotName15 = '';
        this.skuLotName16 = '';
        this.skuLotName17 = '';
        this.skuLotName18 = '';
        this.skuLotName19 = '';
        this.skuLotName20 = '';
        this.skuLotName21 = '';
        this.skuLotName22 = '';
        this.skuLotName23 = '';
        this.skuLotName24 = '';
        this.skuLotName25 = '';
        this.skuLotName26 = '';
        this.skuLotName27 = '';
        this.skuLotName28 = '';
        this.skuLotName29 = '';
        this.skuLotName30 = '';
    }
    Object.defineProperty(SkuLotModel.prototype, "setLotModel", {
        set: function (model) {
            for (var index = 0; index < model.length; index++) {
                var indexExt = index + 1;
                this.setLotValue(indexExt, model[index]);
            }
        },
        enumerable: true,
        configurable: true
    });
    SkuLotModel.prototype.setLotValue = function (index, value) {
        switch (index) {
            case 1:
                this.skulot1 = value;
                break;
            case 2:
                this.skulot2 = value;
                break;
            case 3:
                this.skulot3 = value;
                break;
            case 4:
                this.skulot4 = value;
                break;
            case 5:
                this.skulot5 = value;
                break;
            case 6:
                this.skulot6 = value;
                break;
            case 7:
                this.skulot7 = value;
                break;
            case 8:
                this.skulot8 = value;
                break;
            case 9:
                this.skulot9 = value;
                break;
            case 10:
                this.skulot10 = value;
                break;
            case 11:
                this.skulot11 = value;
                break;
            case 12:
                this.skulot12 = value;
                break;
            case 13:
                this.skulot13 = value;
                break;
            case 14:
                this.skulot14 = value;
                break;
            case 15:
                this.skulot15 = value;
                break;
            case 16:
                this.skulot16 = value;
                break;
            case 17:
                this.skulot17 = value;
                break;
            case 18:
                this.skulot18 = value;
                break;
            case 19:
                this.skulot19 = value;
                break;
            case 20:
                this.skulot20 = value;
                break;
            case 21:
                this.skulot21 = value;
                break;
            case 22:
                this.skulot22 = value;
                break;
            case 23:
                this.skulot23 = value;
                break;
            case 24:
                this.skulot24 = value;
                break;
            case 25:
                this.skulot25 = value;
                break;
            case 26:
                this.skulot26 = value;
                break;
            case 27:
                this.skulot27 = value;
                break;
            case 28:
                this.skulot28 = value;
                break;
            case 29:
                this.skulot29 = value;
                break;
            case 30:
                this.skulot30 = value;
                break;
        }
    };
    return SkuLotModel;
}());

//# sourceMappingURL=SkuLotModel.js.map

/***/ }),

/***/ 176:
/***/ (function(module, exports) {

function webpackEmptyAsyncContext(req) {
	// Here Promise.resolve().then() is used instead of new Promise() to prevent
	// uncatched exception popping up in devtools
	return Promise.resolve().then(function() {
		throw new Error("Cannot find module '" + req + "'.");
	});
}
webpackEmptyAsyncContext.keys = function() { return []; };
webpackEmptyAsyncContext.resolve = webpackEmptyAsyncContext;
module.exports = webpackEmptyAsyncContext;
webpackEmptyAsyncContext.id = 176;

/***/ }),

/***/ 220:
/***/ (function(module, exports, __webpack_require__) {

var map = {
	"../components/mergetrayselect-modal/mergetrayselect-modal.module": [
		702,
		78
	],
	"../components/skuBill-modal/skuBill-modal.module": [
		703,
		77
	],
	"../components/skulist-modal/skulist-modal.module": [
		704,
		66
	],
	"../components/stockselect-modal/stockselect-modal.module": [
		705,
		18
	],
	"../pages/checkinhome/checkOrder/check-order.module": [
		710,
		64
	],
	"../pages/checkinhome/checkOrder/search-order/search-order.module": [
		711,
		63
	],
	"../pages/checkinhome/checkOrder/update-detail/update-detail.module": [
		712,
		13
	],
	"../pages/checkinhome/checkinbox/checkin-box-info/checkin-box-info.module": [
		758,
		12
	],
	"../pages/checkinhome/checkinbox/checkinbox.module": [
		706,
		62
	],
	"../pages/checkinhome/checkinhome.module": [
		707,
		61
	],
	"../pages/checkinhome/checkinpiece/check-in/check-in.module": [
		774,
		17
	],
	"../pages/checkinhome/checkinpiece/checkin-detailed/checkin-detailed.module": [
		708,
		60
	],
	"../pages/checkinhome/checkinpiece/checkinpiece.module": [
		709,
		59
	],
	"../pages/checkinhome/checkintray/checkintray.module": [
		759,
		16
	],
	"../pages/clock-in-home/clock-in-home.module": [
		713,
		76
	],
	"../pages/clock-in-home/clock-in/clock-in.module": [
		760,
		0
	],
	"../pages/collage-task/collage-task-box/collage-task-box.module": [
		714,
		75
	],
	"../pages/collage-task/collage-task-list/collage-task-list.module": [
		715,
		58
	],
	"../pages/collage-task/collage-task.module": [
		761,
		57
	],
	"../pages/cougny/copy-label/copy-label.module": [
		716,
		11
	],
	"../pages/cougny/cougny.module": [
		717,
		56
	],
	"../pages/cougny/merge-tray/merge-tray-box/merge-tray-box.module": [
		718,
		74
	],
	"../pages/cougny/merge-tray/merge-tray-list/merge-tray-list.module": [
		719,
		55
	],
	"../pages/cougny/merge-tray/merge-tray.module": [
		775,
		54
	],
	"../pages/cougny/stock-frozen/stock-frozen.module": [
		720,
		53
	],
	"../pages/cyclecounthome/cyclecounthome.module": [
		721,
		52
	],
	"../pages/cyclecounthome/cyclecountrandom/cyclecountrandom.module": [
		722,
		73
	],
	"../pages/cyclecounthome/cyclecountrandominfo/cyclecountrandominfo.module": [
		776,
		15
	],
	"../pages/cyclecounthome/cyclecountrantask/cyclecount-bill/cyclecount-bill.module": [
		724,
		51
	],
	"../pages/cyclecounthome/cyclecountrantask/cyclecountrantask.module": [
		723,
		50
	],
	"../pages/cyclecounthome/cyclecountrantask/cyclecoutrantaskinfo/cyclecoutrantaskinfo.module": [
		777,
		14
	],
	"../pages/cyclecounthome/cyclecountrecord/cyclecountrecord.module": [
		725,
		49
	],
	"../pages/cyclecounthome/randomcheck/randomcheck.module": [
		762,
		48
	],
	"../pages/cyclecounthome/randomchecktask/randomchecktask.module": [
		763,
		47
	],
	"../pages/home/home.module": [
		726,
		72
	],
	"../pages/login/login.module": [
		764,
		5
	],
	"../pages/movehome/movehome.module": [
		727,
		46
	],
	"../pages/movehome/movesbox/movesbox-info/movesbox-info.module": [
		728,
		45
	],
	"../pages/movehome/movesbox/movesbox.module": [
		765,
		44
	],
	"../pages/movehome/movesku/movesku.module": [
		778,
		43
	],
	"../pages/movehome/movestock/movestock.module": [
		729,
		42
	],
	"../pages/movehome/movestocks/movestocks.module": [
		730,
		41
	],
	"../pages/package-task/package-task-list/package-task-info/package-task-info.module": [
		731,
		40
	],
	"../pages/package-task/package-task-list/package-task-list.module": [
		766,
		10
	],
	"../pages/package-task/package-task.module": [
		732,
		9
	],
	"../pages/printing/printing.module": [
		733,
		65
	],
	"../pages/putawayhome/putaway/putaway.module": [
		734,
		39
	],
	"../pages/putawayhome/putawaybox/putawaybox.module": [
		767,
		4
	],
	"../pages/putawayhome/putawayhome.module": [
		735,
		38
	],
	"../pages/replenishmen/replenishmen-record/replenishmen-record.module": [
		736,
		3
	],
	"../pages/replenishmen/replenishmen.module": [
		737,
		2
	],
	"../pages/replenishmenhome/replenishmen-detile/replenishmen-detile.module": [
		738,
		37
	],
	"../pages/replenishmenhome/replenishmenhome.module": [
		768,
		1
	],
	"../pages/setting/setting.module": [
		739,
		36
	],
	"../pages/setting/settingpwd/settingpwd.module": [
		740,
		35
	],
	"../pages/setting/settingtheme/settingtheme.module": [
		741,
		34
	],
	"../pages/shipmenthome/shipment-car-order/shipment-car-order.module": [
		742,
		71
	],
	"../pages/shipmenthome/shipment-lpn/shipment-lpn.module": [
		769,
		33
	],
	"../pages/shipmenthome/shipment-order/shipment-order.module": [
		743,
		70
	],
	"../pages/shipmenthome/shipmenthome.module": [
		744,
		32
	],
	"../pages/stockhome/stockdetail/stockdetail.module": [
		745,
		31
	],
	"../pages/stockhome/stockhome.module": [
		746,
		30
	],
	"../pages/stockhome/stockquery/stock-prit/stock-prit.module": [
		747,
		29
	],
	"../pages/stockhome/stockquery/stockquery.module": [
		770,
		28
	],
	"../pages/takeawayhome/takeawayhome.module": [
		748,
		27
	],
	"../pages/takeawayhome/takeawaypiece/takeaway/takeaway.module": [
		751,
		26
	],
	"../pages/takeawayhome/takeawaypiece/takeawayin/takeawayin.module": [
		771,
		25
	],
	"../pages/takeawayhome/takeawaypiece/takeawayindetailed/takeawayindetailed.module": [
		749,
		24
	],
	"../pages/takeawayhome/takeawaypiece/takeawaypiece.module": [
		750,
		23
	],
	"../pages/takeawayhome/takeawaypiecebox/takeawayinbox/takeawayinbox.module": [
		779,
		8
	],
	"../pages/takeawayhome/takeawaypiecebox/takeawaypiecebox.module": [
		752,
		22
	],
	"../pages/takeawayhome/taskway-mult/bind-order/bind-lpn/bind-lpn.module": [
		753,
		7
	],
	"../pages/takeawayhome/taskway-mult/bind-order/bind-order.module": [
		772,
		21
	],
	"../pages/takeawayhome/taskway-mult/bind-printter/bind-printter.module": [
		754,
		20
	],
	"../pages/takeawayhome/taskway-mult/taskway-mult.module": [
		755,
		6
	],
	"../pages/takeawayhome/taskwaylpn/taskwaylpn.module": [
		756,
		69
	],
	"../pages/taskview/taskview.module": [
		773,
		19
	],
	"../shared/check-in-box-label-type/check-in-box-label-type.module": [
		701,
		68
	],
	"../shared/popover/popover.module": [
		757,
		67
	]
};
function webpackAsyncContext(req) {
	var ids = map[req];
	if(!ids)
		return Promise.reject(new Error("Cannot find module '" + req + "'."));
	return __webpack_require__.e(ids[1]).then(function() {
		return __webpack_require__(ids[0]);
	});
};
webpackAsyncContext.keys = function webpackAsyncContextKeys() {
	return Object.keys(map);
};
webpackAsyncContext.id = 220;
module.exports = webpackAsyncContext;

/***/ }),

/***/ 359:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return BarcodeService; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "b", function() { return BarcodeType; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0_rxjs__ = __webpack_require__(161);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0_rxjs___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_0_rxjs__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__App_service__ = __webpack_require__(157);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__NativeService__ = __webpack_require__(64);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__Utils__ = __webpack_require__(89);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5__models_ParamModel__ = __webpack_require__(680);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_6__models_DataBase_Model__ = __webpack_require__(361);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_7_ionic_angular__ = __webpack_require__(43);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_8__utils_appConstant__ = __webpack_require__(65);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_9__ionic_storage__ = __webpack_require__(66);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_10__models_SkuLotModel__ = __webpack_require__(158);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};











var BarcodeService = /** @class */ (function () {
    //???????????? ????????????
    function BarcodeService(appService, nativeService, alertCtrl, storage) {
        this.appService = appService;
        this.nativeService = nativeService;
        this.alertCtrl = alertCtrl;
        this.storage = storage;
        this.BarcodeRules();
    }
    BarcodeService.prototype.BarcodeRules = function () {
        var _this = this;
        this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_8__utils_appConstant__["b" /* Api */].api + __WEBPACK_IMPORTED_MODULE_8__utils_appConstant__["b" /* Api */].BarcodeRuleList, __WEBPACK_IMPORTED_MODULE_8__utils_appConstant__["f" /* Method */].Post, '', __WEBPACK_IMPORTED_MODULE_8__utils_appConstant__["e" /* ContenType */].Json, function (result) {
            if (__WEBPACK_IMPORTED_MODULE_4__Utils__["a" /* Utils */].isNotEmpty(result.data)) {
                _this.barcodeRules = result.data;
            }
        });
    };
    /// ??????????????????
    BarcodeService.prototype.ToBarcodeType = function (text) {
        switch (text) {
            case 10://"????????????":
                return BarcodeType.HuoWei;
            case 20://"????????????":
                return BarcodeType.TuoPan;
            case 30://"????????????":
                return BarcodeType.SKU;
            case 40://"?????????":
                return BarcodeType.SerialNumber;
            case 50://"?????????":
                return BarcodeType.BatchNo;
            default:
                return BarcodeType.BoxCode;
        }
    };
    /// ????????????
    BarcodeService.prototype.GetBarcodeType = function (text) {
        if (__WEBPACK_IMPORTED_MODULE_4__Utils__["a" /* Utils */].isEmpty(text)) {
            return BarcodeType.Error;
        }
        if (this.barcodeRules) {
            for (var i = 0; i < this.barcodeRules.length; i++) {
                var barcodeDesc = this.barcodeRules[i].barcodeType;
                var rule = this.barcodeRules[i].barcodeRule;
                var regexp = new RegExp(__WEBPACK_IMPORTED_MODULE_4__Utils__["a" /* Utils */].clearSpace(rule));
                if (__WEBPACK_IMPORTED_MODULE_4__Utils__["a" /* Utils */].isNotEmpty(rule)) {
                    if (regexp.test(text)) {
                        return this.ToBarcodeType(barcodeDesc);
                    }
                }
            }
        }
        // ????????????????????????,???????????????
        return BarcodeType.BoxCode;
    };
    /**
     * ????????????
     * @param barcode ????????????
     */
    BarcodeService.prototype.ScanSkuBarcode = function (barcode) {
        var _this = this;
        if (__WEBPACK_IMPORTED_MODULE_4__Utils__["a" /* Utils */].isEmpty(barcode)) {
            return __WEBPACK_IMPORTED_MODULE_0_rxjs__["Observable"].of(null);
        }
        barcode = __WEBPACK_IMPORTED_MODULE_4__Utils__["a" /* Utils */].ToCDB(barcode.trim());
        return __WEBPACK_IMPORTED_MODULE_0_rxjs__["Observable"].create(function (observer) {
            _this.storage.get('sysParam').then(function (data) {
                //??????????????????
                var paramModel = new __WEBPACK_IMPORTED_MODULE_5__models_ParamModel__["a" /* ParamModel */]();
                //??????????????????
                var scanModel = new __WEBPACK_IMPORTED_MODULE_6__models_DataBase_Model__["a" /* ScanModel */]();
                //????????????????????????
                if (__WEBPACK_IMPORTED_MODULE_4__Utils__["a" /* Utils */].isNotEmpty(data.filter(function (x) { return x.paramKey == 'split'; }))) {
                    paramModel = data.filter(function (x) { return x.paramKey == 'split'; })[0];
                    paramModel.paramValue = JSON.parse(paramModel.paramValue);
                    var splModel = barcode.split(paramModel.paramValue.sp1);
                    scanModel.skuLotModel = new __WEBPACK_IMPORTED_MODULE_10__models_SkuLotModel__["a" /* SkuLotModel */]();
                    for (var index = 0; index < splModel.length; index++) {
                        //??????????????????????????????
                        var element = splModel[index];
                        if (__WEBPACK_IMPORTED_MODULE_4__Utils__["a" /* Utils */].isNotEmpty(element)) {
                            var sp2Model = element.split(paramModel.paramValue.sp2);
                            var sp2Key = sp2Model[0].toLowerCase();
                            var sp2Value = sp2Model[1];
                            if (__WEBPACK_IMPORTED_MODULE_4__Utils__["a" /* Utils */].Contains("skucode", sp2Key)
                                || __WEBPACK_IMPORTED_MODULE_4__Utils__["a" /* Utils */].Contains("skuname", sp2Key)
                                || __WEBPACK_IMPORTED_MODULE_4__Utils__["a" /* Utils */].Contains("qty", sp2Key)
                                || __WEBPACK_IMPORTED_MODULE_4__Utils__["a" /* Utils */].Contains("um", sp2Key)) {
                                //???????????????
                                if (__WEBPACK_IMPORTED_MODULE_4__Utils__["a" /* Utils */].isEmpty(sp2Value)) {
                                    _this.nativeService.showToast("\u7BB1\u7801\u4E2D\u7684" + sp2Key + "\u4E0D\u80FD\u4E3A\u7A7A");
                                    observer.next(null);
                                    return;
                                }
                                scanModel[sp2Key] = sp2Value.trim();
                            }
                            else if (__WEBPACK_IMPORTED_MODULE_4__Utils__["a" /* Utils */].Contains(sp2Key.toLowerCase(), "skulot")) {
                                scanModel.skuLotModel[sp2Key] = sp2Value.trim();
                            }
                            else {
                                _this.nativeService.showToast('???????????????????????????,????????????????????????');
                                observer.next(null);
                                return;
                            }
                        }
                    }
                }
                observer.next(scanModel);
            });
        });
    };
    /**
     * ?????????????????????Lot
     */
    BarcodeService.prototype.convertLotModel = function (val) {
        if (__WEBPACK_IMPORTED_MODULE_4__Utils__["a" /* Utils */].isEmpty(val)) {
            return;
        }
        var param = {
            skuLot1: val.skuLotModel.skulot1,
            skuLot2: val.skuLotModel.skulot2,
            skuLot3: val.skuLotModel.skulot3,
            skuLot4: val.skuLotModel.skulot4,
            skuLot5: val.skuLotModel.skulot5,
            skuLot6: val.skuLotModel.skulot6,
            skuLot7: val.skuLotModel.skulot7,
            skuLot8: val.skuLotModel.skulot8,
            skuLot9: val.skuLotModel.skulot9,
            skuLot10: val.skuLotModel.skulot10,
            skuLot11: val.skuLotModel.skulot11,
            skuLot12: val.skuLotModel.skulot12,
            skuLot13: val.skuLotModel.skulot13,
            skuLot14: val.skuLotModel.skulot14,
            skuLot15: val.skuLotModel.skulot15,
            skuLot16: val.skuLotModel.skulot16,
            skuLot17: val.skuLotModel.skulot17,
            skuLot18: val.skuLotModel.skulot18,
            skuLot19: val.skuLotModel.skulot19,
            skuLot20: val.skuLotModel.skulot20,
            skuLot21: val.skuLotModel.skulot21,
            skuLot22: val.skuLotModel.skulot22,
            skuLot23: val.skuLotModel.skulot23,
            skuLot24: val.skuLotModel.skulot24,
            skuLot25: val.skuLotModel.skulot25,
            skuLot26: val.skuLotModel.skulot26,
            skuLot27: val.skuLotModel.skulot27,
            skuLot28: val.skuLotModel.skulot28,
            skuLot29: val.skuLotModel.skulot29,
            skuLot30: val.skuLotModel.skulot30,
        };
        return param;
    };
    /**
     * ?????????????????????Lots
     */
    BarcodeService.prototype.convertLotsModel = function (val) {
        if (__WEBPACK_IMPORTED_MODULE_4__Utils__["a" /* Utils */].isEmpty(val)) {
            return;
        }
        var param = (_a = {},
            _a['skuLots.skuLot1'] = val.skuLotModel.skulot1,
            _a['skuLots.skuLot2'] = val.skuLotModel.skulot2,
            _a['skuLots.skuLot3'] = val.skuLotModel.skulot3,
            _a['skuLots.skuLot4'] = val.skuLotModel.skulot4,
            _a['skuLots.skuLot5'] = val.skuLotModel.skulot5,
            _a['skuLots.skuLot6'] = val.skuLotModel.skulot6,
            _a['skuLots.skuLot7'] = val.skuLotModel.skulot7,
            _a['skuLots.skuLot8'] = val.skuLotModel.skulot8,
            _a['skuLots.skuLot9'] = val.skuLotModel.skulot9,
            _a['skuLots.skuLot10'] = val.skuLotModel.skulot10,
            _a['skuLots.skuLot11'] = val.skuLotModel.skulot11,
            _a['skuLots.skuLot12'] = val.skuLotModel.skulot12,
            _a['skuLots.skuLot13'] = val.skuLotModel.skulot13,
            _a['skuLots.skuLot14'] = val.skuLotModel.skulot14,
            _a['skuLots.skuLot15'] = val.skuLotModel.skulot15,
            _a['skuLots.skuLot16'] = val.skuLotModel.skulot16,
            _a['skuLots.skuLot17'] = val.skuLotModel.skulot17,
            _a['skuLots.skuLot18'] = val.skuLotModel.skulot18,
            _a['skuLots.skuLot19'] = val.skuLotModel.skulot19,
            _a['skuLots.skuLot20'] = val.skuLotModel.skulot20,
            _a['skuLots.skuLot21'] = val.skuLotModel.skulot21,
            _a['skuLots.skuLot22'] = val.skuLotModel.skulot22,
            _a['skuLots.skuLot23'] = val.skuLotModel.skulot23,
            _a['skuLots.skuLot24'] = val.skuLotModel.skulot24,
            _a['skuLots.skuLot25'] = val.skuLotModel.skulot25,
            _a['skuLots.skuLot26'] = val.skuLotModel.skulot26,
            _a['skuLots.skuLot27'] = val.skuLotModel.skulot27,
            _a['skuLots.skuLot28'] = val.skuLotModel.skulot28,
            _a['skuLots.skuLot29'] = val.skuLotModel.skulot29,
            _a['skuLots.skuLot30'] = val.skuLotModel.skulot30,
            _a);
        return param;
        var _a;
    };
    /**
     *
     * ?????????????????????
     */
    BarcodeService.prototype.convertGetLotModel = function (targetModel, model) {
        if (__WEBPACK_IMPORTED_MODULE_4__Utils__["a" /* Utils */].isEmpty(targetModel) && __WEBPACK_IMPORTED_MODULE_4__Utils__["a" /* Utils */].isEmpty(model)) {
            return;
        }
        model.skuLotName1 = targetModel.skuLotLabel1;
        model.skuLotName2 = targetModel.skuLotLabel2;
        model.skuLotName3 = targetModel.skuLotLabel3;
        model.skuLotName4 = targetModel.skuLotLabel4;
        model.skuLotName5 = targetModel.skuLotLabel5;
        model.skuLotName6 = targetModel.skuLotLabel6;
        model.skuLotName7 = targetModel.skuLotLabel7;
        model.skuLotName8 = targetModel.skuLotLabel8;
        model.skuLotName9 = targetModel.skuLotLabel9;
        model.skuLotName10 = targetModel.skuLotLabel10;
        model.skuLotName11 = targetModel.skuLotLabel11;
        model.skuLotName12 = targetModel.skuLotLabel12;
        model.skuLotName13 = targetModel.skuLotLabel13;
        model.skuLotName14 = targetModel.skuLotLabel14;
        model.skuLotName15 = targetModel.skuLotLabel15;
        model.skuLotName16 = targetModel.skuLotLabel16;
        model.skuLotName17 = targetModel.skuLotLabel17;
        model.skuLotName18 = targetModel.skuLotLabel18;
        model.skuLotName19 = targetModel.skuLotLabel19;
        model.skuLotName20 = targetModel.skuLotLabel20;
        model.skuLotName21 = targetModel.skuLotLabel21;
        model.skuLotName22 = targetModel.skuLotLabel22;
        model.skuLotName23 = targetModel.skuLotLabel23;
        model.skuLotName24 = targetModel.skuLotLabel24;
        model.skuLotName25 = targetModel.skuLotLabel25;
        model.skuLotName26 = targetModel.skuLotLabel26;
        model.skuLotName27 = targetModel.skuLotLabel27;
        model.skuLotName28 = targetModel.skuLotLabel28;
        model.skuLotName29 = targetModel.skuLotLabel29;
        model.skuLotName30 = targetModel.skuLotLabel30;
    };
    /**
     * ??????????????????1?????????????????????????????????
     */
    BarcodeService.prototype.openModel = function () {
        var _this = this;
        return __WEBPACK_IMPORTED_MODULE_0_rxjs__["Observable"].create(function (observer) {
            var alert = _this.alertCtrl.create();
            alert.setTitle('?????????????????????');
            _this.skuList.forEach(function (item) {
                alert.addInput({
                    type: 'radio',
                    label: item.SkuCode + ' ' + item.SkuName,
                    value: item.SkuId.toString()
                });
            });
            alert.addButton({
                text: '??????',
                handler: function () {
                    observer.next(null);
                }
            });
            alert.addButton({
                text: '??????',
                handler: function (data) {
                    observer.next(_this.skuList.find(function (value) {
                        return value.SkuId.toString() === data;
                    }));
                }
            });
            alert.present();
            alert.onDidDismiss(function () {
            });
        });
    };
    // ???????????????
    BarcodeService.prototype.ScanSerialNumber = function (serialNumber) {
        return __WEBPACK_IMPORTED_MODULE_0_rxjs__["Observable"].create(function (observer) {
            if (__WEBPACK_IMPORTED_MODULE_4__Utils__["a" /* Utils */].isEmpty(serialNumber)) {
                observer.of(null);
            }
            // this.appService.httpRequest(IFD.BASEDATA_MATERIAL + "/", IFD.BASEDATA_MATERIAL_B02016, serialNumber, result => {
            //     this.nativeService.hideLoading();
            //     observer.next(result);
            // });
        });
    };
    BarcodeService = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_1__angular_core__["Injectable"])(),
        __metadata("design:paramtypes", [__WEBPACK_IMPORTED_MODULE_2__App_service__["a" /* AppService */],
            __WEBPACK_IMPORTED_MODULE_3__NativeService__["a" /* NativeService */],
            __WEBPACK_IMPORTED_MODULE_7_ionic_angular__["a" /* AlertController */],
            __WEBPACK_IMPORTED_MODULE_9__ionic_storage__["b" /* Storage */]])
    ], BarcodeService);
    return BarcodeService;
}());

// ??????????????????SKU????????????, ??????????????????????????????
var BarcodeType = {
    TuoPan: 0,
    HuoWei: 1,
    SKU: 2,
    SerialNumber: 3,
    YuanGongZhangHao: 4,
    BatchNo: 6,
    Error: 5,
    BoxCode: 7
};
//# sourceMappingURL=BarCodeService.js.map

/***/ }),

/***/ 360:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return AppGlobal; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_rxjs_add_operator_toPromise__ = __webpack_require__(305);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_rxjs_add_operator_toPromise___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_1_rxjs_add_operator_toPromise__);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};


var AppGlobal = /** @class */ (function () {
    function AppGlobal() {
    }
    AppGlobal.removeSubscribe = function (obj) {
        obj.events.unsubscribe('barcode:scan', null);
        this.scanFlag = true;
    };
    //??????key?????????
    AppGlobal.cache = {};
    //?????????????????????????????????????????????
    AppGlobal.domain = "http://localhost:8100/api/";
    //??????????????????
    // static domain = "http://114.215.129.212:5308/api/"
    //????????????
    AppGlobal.API = {
        //????????????
        postLoginin: 'UserLogin/',
    };
    AppGlobal.scanFlag = true;
    AppGlobal = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Injectable"])()
    ], AppGlobal);
    return AppGlobal;
}());

//# sourceMappingURL=AppGlobal.js.map

/***/ }),

/***/ 361:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* unused harmony export AsnHandleBaseInfoModel */
/* unused harmony export BaseDataModel */
/* unused harmony export ContainerInfoModel */
/* unused harmony export ContainerStateInfoModel */
/* unused harmony export ReturnAttributeModel */
/* unused harmony export SkuInfoModel */
/* unused harmony export SkuQureyModel */
/* unused harmony export SundriesInfoModel */
/* unused harmony export UmSkuModel */
/* unused harmony export WmsLabModel */
/* unused harmony export WMSLogin */
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return ScanModel; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__models_SkuLotModel__ = __webpack_require__(158);

var AsnHandleBaseInfoModel = /** @class */ (function () {
    function AsnHandleBaseInfoModel() {
    }
    return AsnHandleBaseInfoModel;
}());

var BaseDataModel = /** @class */ (function () {
    function BaseDataModel() {
    }
    return BaseDataModel;
}());

var ContainerInfoModel = /** @class */ (function () {
    function ContainerInfoModel() {
    }
    return ContainerInfoModel;
}());

var ContainerStateInfoModel = /** @class */ (function () {
    function ContainerStateInfoModel() {
    }
    return ContainerStateInfoModel;
}());

var ReturnAttributeModel = /** @class */ (function () {
    function ReturnAttributeModel() {
    }
    return ReturnAttributeModel;
}());

var SkuInfoModel = /** @class */ (function () {
    function SkuInfoModel() {
    }
    return SkuInfoModel;
}());

var SkuQureyModel = /** @class */ (function () {
    function SkuQureyModel() {
    }
    return SkuQureyModel;
}());

var SundriesInfoModel = /** @class */ (function () {
    function SundriesInfoModel() {
    }
    return SundriesInfoModel;
}());

var UmSkuModel = /** @class */ (function () {
    function UmSkuModel() {
    }
    return UmSkuModel;
}());

var WmsLabModel = /** @class */ (function () {
    function WmsLabModel() {
    }
    return WmsLabModel;
}());

var WMSLogin = /** @class */ (function () {
    function WMSLogin() {
    }
    return WMSLogin;
}());

var ScanModel = /** @class */ (function () {
    function ScanModel() {
        this.skuLotModel = new __WEBPACK_IMPORTED_MODULE_0__models_SkuLotModel__["a" /* SkuLotModel */]();
    }
    return ScanModel;
}());

//# sourceMappingURL=DataBase.Model.js.map

/***/ }),

/***/ 362:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return ComponentsModule; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__serial_number_serial_number__ = __webpack_require__(681);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};


var ComponentsModule = /** @class */ (function () {
    function ComponentsModule() {
    }
    ComponentsModule = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["NgModule"])({
            declarations: [__WEBPACK_IMPORTED_MODULE_1__serial_number_serial_number__["a" /* SerialNumberComponent */],
            ],
            imports: [],
            exports: [__WEBPACK_IMPORTED_MODULE_1__serial_number_serial_number__["a" /* SerialNumberComponent */],
            ],
        })
    ], ComponentsModule);
    return ComponentsModule;
}());

//# sourceMappingURL=components.module.js.map

/***/ }),

/***/ 365:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return DirectivesModule; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__debounce_click_no_dbl_click__ = __webpack_require__(682);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};


var DirectivesModule = /** @class */ (function () {
    function DirectivesModule() {
    }
    DirectivesModule = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["NgModule"])({
            declarations: [__WEBPACK_IMPORTED_MODULE_1__debounce_click_no_dbl_click__["a" /* NoDblClickDirective */]],
            imports: [],
            exports: [__WEBPACK_IMPORTED_MODULE_1__debounce_click_no_dbl_click__["a" /* NoDblClickDirective */]]
        })
    ], DirectivesModule);
    return DirectivesModule;
}());

//# sourceMappingURL=directives.module.js.map

/***/ }),

/***/ 366:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_platform_browser_dynamic__ = __webpack_require__(367);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__app_module__ = __webpack_require__(371);


Object(__WEBPACK_IMPORTED_MODULE_0__angular_platform_browser_dynamic__["a" /* platformBrowserDynamic */])().bootstrapModule(__WEBPACK_IMPORTED_MODULE_1__app_module__["a" /* AppModule */]);
//# sourceMappingURL=main.js.map

/***/ }),

/***/ 371:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return AppModule; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_platform_browser__ = __webpack_require__(35);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__angular_platform_browser_animations__ = __webpack_require__(372);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3_ionic_angular__ = __webpack_require__(43);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__ionic_native_splash_screen__ = __webpack_require__(121);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5__ionic_native_status_bar__ = __webpack_require__(90);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_6__angular_common_http__ = __webpack_require__(318);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_7__ionic_native_toast__ = __webpack_require__(317);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_8__app_component__ = __webpack_require__(700);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_9__angular_http__ = __webpack_require__(364);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_10__ionic_storage__ = __webpack_require__(66);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_11__services_App_service__ = __webpack_require__(157);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_12__services_NativeService__ = __webpack_require__(64);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_13__utils_common__ = __webpack_require__(91);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_14__services_BarCodeService__ = __webpack_require__(359);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_15__angular_forms__ = __webpack_require__(22);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_16__ionic_native_android_permissions__ = __webpack_require__(358);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_17__ionic_native_file__ = __webpack_require__(163);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_18__ionic_native_file_transfer__ = __webpack_require__(162);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_19__ionic_native_app_version__ = __webpack_require__(164);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_20__ionic_native_file_opener__ = __webpack_require__(165);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_21__ionic_native_printer__ = __webpack_require__(363);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_22__components_components_module__ = __webpack_require__(362);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_23__ionic_native_keyboard__ = __webpack_require__(160);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_24__directives_directives_module__ = __webpack_require__(365);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};

























//????????????
var serviceList = [
    __WEBPACK_IMPORTED_MODULE_11__services_App_service__["a" /* AppService */],
    __WEBPACK_IMPORTED_MODULE_12__services_NativeService__["a" /* NativeService */],
    __WEBPACK_IMPORTED_MODULE_13__utils_common__["a" /* CommonService */],
    __WEBPACK_IMPORTED_MODULE_14__services_BarCodeService__["a" /* BarcodeService */],
];
var AppModule = /** @class */ (function () {
    function AppModule() {
    }
    AppModule = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_2__angular_core__["NgModule"])({
            declarations: [
                __WEBPACK_IMPORTED_MODULE_8__app_component__["a" /* MyApp */],
            ],
            imports: [
                __WEBPACK_IMPORTED_MODULE_24__directives_directives_module__["a" /* DirectivesModule */],
                __WEBPACK_IMPORTED_MODULE_15__angular_forms__["FormsModule"],
                __WEBPACK_IMPORTED_MODULE_15__angular_forms__["ReactiveFormsModule"],
                __WEBPACK_IMPORTED_MODULE_0__angular_platform_browser__["a" /* BrowserModule */],
                __WEBPACK_IMPORTED_MODULE_1__angular_platform_browser_animations__["a" /* BrowserAnimationsModule */],
                __WEBPACK_IMPORTED_MODULE_1__angular_platform_browser_animations__["b" /* NoopAnimationsModule */],
                __WEBPACK_IMPORTED_MODULE_3_ionic_angular__["f" /* IonicModule */].forRoot(__WEBPACK_IMPORTED_MODULE_8__app_component__["a" /* MyApp */], { conMode: 'md', mode: 'ios', backButtonText: '' }, {
                    links: [
                        { loadChildren: '../components/mergetrayselect-modal/mergetrayselect-modal.module#MergeTraySelectModule', name: 'MergeTraySelectModal', segment: 'mergetrayselect-modal', priority: 'low', defaultHistory: [] },
                        { loadChildren: '../components/skuBill-modal/skuBill-modal.module#AsnBillnoModalModule', name: 'SkuBillModal', segment: 'skuBill-modal', priority: 'low', defaultHistory: [] },
                        { loadChildren: '../components/skulist-modal/skulist-modal.module#AsnBillnoModalModule', name: 'SkuListModal', segment: 'skulist-modal', priority: 'low', defaultHistory: [] },
                        { loadChildren: '../components/stockselect-modal/stockselect-modal.module#StockSelectModalModule', name: 'StockSelectModal', segment: 'stockselect-modal', priority: 'low', defaultHistory: [] },
                        { loadChildren: '../pages/checkinhome/checkinbox/checkinbox.module#CheckinboxPageModule', name: 'CheckinboxPage', segment: 'checkinbox', priority: 'low', defaultHistory: [] },
                        { loadChildren: '../pages/checkinhome/checkinhome.module#CheckinhomePageModule', name: 'CheckinhomePage', segment: 'checkinhome', priority: 'low', defaultHistory: [] },
                        { loadChildren: '../pages/checkinhome/checkinpiece/checkin-detailed/checkin-detailed.module#CheckinDetailedPageModule', name: 'CheckinDetailedPage', segment: 'checkin-detailed', priority: 'low', defaultHistory: [] },
                        { loadChildren: '../pages/checkinhome/checkinpiece/checkinpiece.module#CheckinpiecePageModule', name: 'CheckinpiecePage', segment: 'checkinpiece', priority: 'low', defaultHistory: [] },
                        { loadChildren: '../pages/checkinhome/checkOrder/check-order.module#CheckOrderPageModule', name: 'CheckOrderPage', segment: 'check-order', priority: 'low', defaultHistory: [] },
                        { loadChildren: '../pages/checkinhome/checkOrder/search-order/search-order.module#SearchOrderPageModule', name: 'SearchOrderPage', segment: 'search-order', priority: 'low', defaultHistory: [] },
                        { loadChildren: '../pages/checkinhome/checkOrder/update-detail/update-detail.module#UpdateDetailPageModule', name: 'UpdateDetailPage', segment: 'update-detail', priority: 'low', defaultHistory: [] },
                        { loadChildren: '../pages/clock-in-home/clock-in-home.module#ClockInHomePageModule', name: 'ClockInHomePage', segment: 'clock-in-home', priority: 'low', defaultHistory: [] },
                        { loadChildren: '../pages/collage-task/collage-task-box/collage-task-box.module#CollageTaskBoxPageModule', name: 'CollageTaskBoxPage', segment: 'collage-task-box', priority: 'low', defaultHistory: [] },
                        { loadChildren: '../pages/collage-task/collage-task-list/collage-task-list.module#CollageTaskListPageModule', name: 'CollageTaskListPage', segment: 'collage-task-list', priority: 'low', defaultHistory: [] },
                        { loadChildren: '../pages/cougny/copy-label/copy-label.module#CopyLabelPageModule', name: 'CopyLabelPage', segment: 'copy-label', priority: 'low', defaultHistory: [] },
                        { loadChildren: '../pages/cougny/cougny.module#CougnyPageModule', name: 'CougnyPage', segment: 'cougny', priority: 'low', defaultHistory: [] },
                        { loadChildren: '../pages/cougny/merge-tray/merge-tray-box/merge-tray-box.module#MergeTrayBoxPageModule', name: 'MergeTrayBoxPage', segment: 'merge-tray-box', priority: 'low', defaultHistory: [] },
                        { loadChildren: '../pages/cougny/merge-tray/merge-tray-list/merge-tray-list.module#MergeTrayListPageModule', name: 'MergeTrayListPage', segment: 'merge-tray-list', priority: 'low', defaultHistory: [] },
                        { loadChildren: '../pages/cougny/stock-frozen/stock-frozen.module#StockFrozenPageModule', name: 'StockFrozenPage', segment: 'stock-frozen', priority: 'low', defaultHistory: [] },
                        { loadChildren: '../pages/cyclecounthome/cyclecounthome.module#CyclecounthomePageModule', name: 'CyclecounthomePage', segment: 'cyclecounthome', priority: 'low', defaultHistory: [] },
                        { loadChildren: '../pages/cyclecounthome/cyclecountrandom/cyclecountrandom.module#CyclecountrandomPageModule', name: 'CyclecountrandomPage', segment: 'cyclecountrandom', priority: 'low', defaultHistory: [] },
                        { loadChildren: '../pages/cyclecounthome/cyclecountrantask/cyclecountrantask.module#CyclecountrantaskPageModule', name: 'CyclecountrantaskPage', segment: 'cyclecountrantask', priority: 'low', defaultHistory: [] },
                        { loadChildren: '../pages/cyclecounthome/cyclecountrantask/cyclecount-bill/cyclecount-bill.module#CyclecountBillPageModule', name: 'CyclecountBillPage', segment: 'cyclecount-bill', priority: 'low', defaultHistory: [] },
                        { loadChildren: '../pages/cyclecounthome/cyclecountrecord/cyclecountrecord.module#CyclecountPageModule', name: 'CyclecountRecordPage', segment: 'cyclecountrecord', priority: 'low', defaultHistory: [] },
                        { loadChildren: '../pages/home/home.module#HomePageModule', name: 'HomePage', segment: 'home', priority: 'low', defaultHistory: [] },
                        { loadChildren: '../pages/movehome/movehome.module#MovehomePageModule', name: 'MovehomePage', segment: 'movehome', priority: 'low', defaultHistory: [] },
                        { loadChildren: '../pages/movehome/movesbox/movesbox-info/movesbox-info.module#MovesboxInfoPageModule', name: 'MovesboxInfoPage', segment: 'movesbox-info', priority: 'low', defaultHistory: [] },
                        { loadChildren: '../pages/movehome/movestock/movestock.module#moveStockModule', name: 'moveStock', segment: 'movestock', priority: 'low', defaultHistory: [] },
                        { loadChildren: '../pages/movehome/movestocks/movestocks.module#moveStocksModule', name: 'moveStocks', segment: 'movestocks', priority: 'low', defaultHistory: [] },
                        { loadChildren: '../pages/package-task/package-task-list/package-task-info/package-task-info.module#PackageTaskInfoPageModule', name: 'PackageTaskInfoPage', segment: 'package-task-info', priority: 'low', defaultHistory: [] },
                        { loadChildren: '../pages/package-task/package-task.module#PackageTaskPageModule', name: 'PackageTaskPage', segment: 'package-task', priority: 'low', defaultHistory: [] },
                        { loadChildren: '../pages/printing/printing.module#PrintingPageModule', name: 'PrintingPage', segment: 'printing', priority: 'low', defaultHistory: [] },
                        { loadChildren: '../pages/putawayhome/putaway/putaway.module#PutawayPageModule', name: 'PutawayPage', segment: 'putaway', priority: 'low', defaultHistory: [] },
                        { loadChildren: '../pages/putawayhome/putawayhome.module#PutawayhomePageModule', name: 'PutawayhomePage', segment: 'putawayhome', priority: 'low', defaultHistory: [] },
                        { loadChildren: '../pages/replenishmen/replenishmen-record/replenishmen-record.module#ReplenishmenRecordPageModule', name: 'ReplenishmenRecordPage', segment: 'replenishmen-record', priority: 'low', defaultHistory: [] },
                        { loadChildren: '../pages/replenishmen/replenishmen.module#ReplenishmenPageModule', name: 'ReplenishmenPage', segment: 'replenishmen', priority: 'low', defaultHistory: [] },
                        { loadChildren: '../pages/replenishmenhome/replenishmen-detile/replenishmen-detile.module#ReplenishmenDetilePageModule', name: 'ReplenishmenDetilePage', segment: 'replenishmen-detile', priority: 'low', defaultHistory: [] },
                        { loadChildren: '../pages/setting/setting.module#SettingPageModule', name: 'SettingPage', segment: 'setting', priority: 'low', defaultHistory: [] },
                        { loadChildren: '../pages/setting/settingpwd/settingpwd.module#SettingPwdPageModule', name: 'SettingPwdPage', segment: 'settingpwd', priority: 'low', defaultHistory: [] },
                        { loadChildren: '../pages/setting/settingtheme/settingtheme.module#SettingThemePageModule', name: 'SettingThemePage', segment: 'settingtheme', priority: 'low', defaultHistory: [] },
                        { loadChildren: '../pages/shipmenthome/shipment-car-order/shipment-car-order.module#ShipmentCarOrderPageModule', name: 'ShipmentCarOrderPage', segment: 'shipment-car-order', priority: 'low', defaultHistory: [] },
                        { loadChildren: '../pages/shipmenthome/shipment-order/shipment-order.module#ShipmentOrderPageModule', name: 'ShipmentOrderPage', segment: 'shipment-order', priority: 'low', defaultHistory: [] },
                        { loadChildren: '../pages/shipmenthome/shipmenthome.module#ShipmenthomePageModule', name: 'ShipmenthomePage', segment: 'shipmenthome', priority: 'low', defaultHistory: [] },
                        { loadChildren: '../pages/stockhome/stockdetail/stockdetail.module#StockdetailPageModule', name: 'StockdetailPage', segment: 'stockdetail', priority: 'low', defaultHistory: [] },
                        { loadChildren: '../pages/stockhome/stockhome.module#StockhomePageModule', name: 'StockhomePage', segment: 'stockhome', priority: 'low', defaultHistory: [] },
                        { loadChildren: '../pages/stockhome/stockquery/stock-prit/stock-prit.module#StockPritPageModule', name: 'StockPritPage', segment: 'stock-prit', priority: 'low', defaultHistory: [] },
                        { loadChildren: '../pages/takeawayhome/takeawayhome.module#TakeawayhomePageModule', name: 'TakeawayhomePage', segment: 'takeawayhome', priority: 'low', defaultHistory: [] },
                        { loadChildren: '../pages/takeawayhome/takeawaypiece/takeawayindetailed/takeawayindetailed.module#TakeawayindetailedPageModule', name: 'TakeawayindetailedPage', segment: 'takeawayindetailed', priority: 'low', defaultHistory: [] },
                        { loadChildren: '../pages/takeawayhome/takeawaypiece/takeawaypiece.module#TakeawaypiecePageModule', name: 'TakeawaypiecePage', segment: 'takeawaypiece', priority: 'low', defaultHistory: [] },
                        { loadChildren: '../pages/takeawayhome/takeawaypiece/takeaway/takeaway.module#TakeawayPageModule', name: 'TakeawayPage', segment: 'takeaway', priority: 'low', defaultHistory: [] },
                        { loadChildren: '../pages/takeawayhome/takeawaypiecebox/takeawaypiecebox.module#TakeawaypieceboxPageModule', name: 'TakeawaypieceboxPage', segment: 'takeawaypiecebox', priority: 'low', defaultHistory: [] },
                        { loadChildren: '../pages/takeawayhome/taskway-mult/bind-order/bind-lpn/bind-lpn.module#BindLpnPageModule', name: 'BindLpnPage', segment: 'bind-lpn', priority: 'low', defaultHistory: [] },
                        { loadChildren: '../pages/takeawayhome/taskway-mult/bind-printter/bind-printter.module#BindPrintterPageModule', name: 'BindPrintterPage', segment: 'bind-printter', priority: 'low', defaultHistory: [] },
                        { loadChildren: '../pages/takeawayhome/taskway-mult/taskway-mult.module#TaskwayMultPageModule', name: 'TaskwayMultPage', segment: 'taskway-mult', priority: 'low', defaultHistory: [] },
                        { loadChildren: '../pages/takeawayhome/taskwaylpn/taskwaylpn.module#TaskwaylpnPageModule', name: 'TaskwaylpnPage', segment: 'taskwaylpn', priority: 'low', defaultHistory: [] },
                        { loadChildren: '../shared/check-in-box-label-type/check-in-box-label-type.module#CheckInBoxLabelTypePageModule', name: 'CheckInBoxLabelTypePage', segment: 'check-in-box-label-type', priority: 'low', defaultHistory: [] },
                        { loadChildren: '../shared/popover/popover.module#PopoverPageModule', name: 'PopoverPage', segment: 'popover', priority: 'low', defaultHistory: [] },
                        { loadChildren: '../pages/checkinhome/checkinbox/checkin-box-info/checkin-box-info.module#CheckinBoxInfoPageModule', name: 'CheckinBoxInfoPage', segment: 'checkin-box-info', priority: 'low', defaultHistory: [] },
                        { loadChildren: '../pages/checkinhome/checkintray/checkintray.module#CheckintrayPageModule', name: 'CheckintrayPage', segment: 'checkintray', priority: 'low', defaultHistory: [] },
                        { loadChildren: '../pages/clock-in-home/clock-in/clock-in.module#ClockInPageModule', name: 'ClockInPage', segment: 'clock-in', priority: 'low', defaultHistory: [] },
                        { loadChildren: '../pages/collage-task/collage-task.module#CollageTaskPageModule', name: 'CollageTaskPage', segment: 'collage-task', priority: 'low', defaultHistory: [] },
                        { loadChildren: '../pages/cyclecounthome/randomcheck/randomcheck.module#RandomCheckPageModule', name: 'RandomCheckPage', segment: 'randomcheck', priority: 'low', defaultHistory: [] },
                        { loadChildren: '../pages/cyclecounthome/randomchecktask/randomchecktask.module#RandomCheckTaskPageModule', name: 'RandomCheckTaskPage', segment: 'randomchecktask', priority: 'low', defaultHistory: [] },
                        { loadChildren: '../pages/login/login.module#LoginPageModule', name: 'LoginPage', segment: 'login', priority: 'low', defaultHistory: [] },
                        { loadChildren: '../pages/movehome/movesbox/movesbox.module#MovesboxPageModule', name: 'MovesboxPage', segment: 'movesbox', priority: 'low', defaultHistory: [] },
                        { loadChildren: '../pages/package-task/package-task-list/package-task-list.module#PackageTaskListPageModule', name: 'PackageTaskListPage', segment: 'package-task-list', priority: 'low', defaultHistory: [] },
                        { loadChildren: '../pages/putawayhome/putawaybox/putawaybox.module#PutawayboxPageModule', name: 'PutawayboxPage', segment: 'putawaybox', priority: 'low', defaultHistory: [] },
                        { loadChildren: '../pages/replenishmenhome/replenishmenhome.module#ReplenishmenhomePageModule', name: 'ReplenishmenhomePage', segment: 'replenishmenhome', priority: 'low', defaultHistory: [] },
                        { loadChildren: '../pages/shipmenthome/shipment-lpn/shipment-lpn.module#ShipmentLpnPageModule', name: 'ShipmentLpnPage', segment: 'shipment-lpn', priority: 'low', defaultHistory: [] },
                        { loadChildren: '../pages/stockhome/stockquery/stockquery.module#StockqueryPageModule', name: 'StockqueryPage', segment: 'stockquery', priority: 'low', defaultHistory: [] },
                        { loadChildren: '../pages/takeawayhome/takeawaypiece/takeawayin/takeawayin.module#TakeawayinPageModule', name: 'TakeawayinPage', segment: 'takeawayin', priority: 'low', defaultHistory: [] },
                        { loadChildren: '../pages/takeawayhome/taskway-mult/bind-order/bind-order.module#BindOrderPageModule', name: 'BindOrderPage', segment: 'bind-order', priority: 'low', defaultHistory: [] },
                        { loadChildren: '../pages/taskview/taskview.module#TaskviewPageModule', name: 'TaskviewPage', segment: 'taskview', priority: 'low', defaultHistory: [] },
                        { loadChildren: '../pages/checkinhome/checkinpiece/check-in/check-in.module#CheckInPageModule', name: 'CheckInPage', segment: 'check-in', priority: 'low', defaultHistory: [] },
                        { loadChildren: '../pages/cougny/merge-tray/merge-tray.module#MergeTrayPageModule', name: 'MergeTrayPage', segment: 'merge-tray', priority: 'low', defaultHistory: [] },
                        { loadChildren: '../pages/cyclecounthome/cyclecountrandominfo/cyclecountrandominfo.module#CyclecountrandominfoPageModule', name: 'CyclecountrandominfoPage', segment: 'cyclecountrandominfo', priority: 'low', defaultHistory: [] },
                        { loadChildren: '../pages/cyclecounthome/cyclecountrantask/cyclecoutrantaskinfo/cyclecoutrantaskinfo.module#CyclecoutrantaskinfoPageModule', name: 'CyclecoutrantaskinfoPage', segment: 'cyclecoutrantaskinfo', priority: 'low', defaultHistory: [] },
                        { loadChildren: '../pages/movehome/movesku/movesku.module#moveSkuModule', name: 'moveSku', segment: 'movesku', priority: 'low', defaultHistory: [] },
                        { loadChildren: '../pages/takeawayhome/takeawaypiecebox/takeawayinbox/takeawayinbox.module#TakeawayinboxPageModule', name: 'TakeawayinboxPage', segment: 'takeawayinbox', priority: 'low', defaultHistory: [] }
                    ]
                }),
                __WEBPACK_IMPORTED_MODULE_22__components_components_module__["a" /* ComponentsModule */],
                __WEBPACK_IMPORTED_MODULE_6__angular_common_http__["b" /* HttpClientModule */],
                __WEBPACK_IMPORTED_MODULE_9__angular_http__["b" /* HttpModule */],
                __WEBPACK_IMPORTED_MODULE_9__angular_http__["d" /* JsonpModule */],
                __WEBPACK_IMPORTED_MODULE_10__ionic_storage__["a" /* IonicStorageModule */].forRoot()
            ],
            bootstrap: [__WEBPACK_IMPORTED_MODULE_3_ionic_angular__["d" /* IonicApp */]],
            entryComponents: [
                __WEBPACK_IMPORTED_MODULE_8__app_component__["a" /* MyApp */]
            ],
            providers: [
                __WEBPACK_IMPORTED_MODULE_23__ionic_native_keyboard__["a" /* Keyboard */],
                __WEBPACK_IMPORTED_MODULE_16__ionic_native_android_permissions__["a" /* AndroidPermissions */],
                __WEBPACK_IMPORTED_MODULE_17__ionic_native_file__["a" /* File */],
                __WEBPACK_IMPORTED_MODULE_18__ionic_native_file_transfer__["a" /* FileTransfer */],
                __WEBPACK_IMPORTED_MODULE_19__ionic_native_app_version__["a" /* AppVersion */],
                __WEBPACK_IMPORTED_MODULE_20__ionic_native_file_opener__["a" /* FileOpener */],
                serviceList,
                __WEBPACK_IMPORTED_MODULE_5__ionic_native_status_bar__["a" /* StatusBar */],
                __WEBPACK_IMPORTED_MODULE_4__ionic_native_splash_screen__["a" /* SplashScreen */],
                __WEBPACK_IMPORTED_MODULE_7__ionic_native_toast__["a" /* Toast */],
                __WEBPACK_IMPORTED_MODULE_21__ionic_native_printer__["a" /* Printer */],
                { provide: __WEBPACK_IMPORTED_MODULE_2__angular_core__["ErrorHandler"], useClass: __WEBPACK_IMPORTED_MODULE_3_ionic_angular__["e" /* IonicErrorHandler */] }
            ]
        })
    ], AppModule);
    return AppModule;
}());

//# sourceMappingURL=app.module.js.map

/***/ }),

/***/ 64:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return NativeService; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(43);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__ionic_native_status_bar__ = __webpack_require__(90);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__ionic_native_splash_screen__ = __webpack_require__(121);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__utils_appConstant__ = __webpack_require__(65);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5__utils_common__ = __webpack_require__(91);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_6__ionic_native_toast__ = __webpack_require__(317);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_7__ionic_native_file_transfer__ = __webpack_require__(162);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_8__ionic_native_file__ = __webpack_require__(163);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_9__ionic_native_app_version__ = __webpack_require__(164);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_10__ionic_native_file_opener__ = __webpack_require__(165);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};











var NativeService = /** @class */ (function () {
    function NativeService(platform, toastCtrl, statusBar, splashScreen, toast, loadingCtrl, common, transfer, file, appVersion, fileOpener, alertControl) {
        this.platform = platform;
        this.toastCtrl = toastCtrl;
        this.statusBar = statusBar;
        this.splashScreen = splashScreen;
        this.toast = toast;
        this.loadingCtrl = loadingCtrl;
        this.common = common;
        this.transfer = transfer;
        this.file = file;
        this.appVersion = appVersion;
        this.fileOpener = fileOpener;
        this.alertControl = alertControl;
        this.loadingIsOpen = false;
    }
    /**
     * ?????????????????????
     */
    NativeService.prototype.statusBarStyleDefault = function () {
        this.statusBar.styleDefault();
    };
    /**
     * ??????????????????
     */
    NativeService.prototype.splashScreenHide = function () {
        this.splashScreen.hide();
    };
    /**
     * ??????????????????
     */
    NativeService.prototype.isMobile = function () {
        return this.platform.is('mobile') && !this.platform.is('mobileweb');
    };
    /**
     * ??????android????????????
     */
    NativeService.prototype.isAndroid = function () {
        return this.isMobile() && this.platform.is('android');
    };
    /**
     * ??????ios????????????
     */
    NativeService.prototype.isIos = function () {
        return this.isMobile() && (this.platform.is('ios') || this.platform.is('ipad') || this.platform.is('iphone'));
    };
    NativeService.prototype.alert = function (title, subTitle) {
        if (subTitle === void 0) { subTitle = ""; }
        this.alertControl.create({
            title: title,
            subTitle: subTitle,
            buttons: [{ text: '??????' }]
        }).present();
    };
    /**
     * ???????????????????????????????????????
     * @param message ????????????
     * @param duration ????????????
     */
    NativeService.prototype.showToast = function (message, duration) {
        if (message === void 0) { message = '????????????'; }
        if (duration === void 0) { duration = 3000; }
        this.hideLoading();
        if (this.isMobile()) {
            this.toast.show(message, String(duration), 'center').subscribe();
        }
        else {
            this.toastCtrl.create({
                message: message,
                duration: duration,
                position: 'middle',
                showCloseButton: false,
                cssClass: 'showToast'
            }).present();
        }
    };
    ;
    /**
     * ???????????????????????????loading
     * @param content ???????????????
     */
    NativeService.prototype.showLoading = function (content) {
        var _this = this;
        if (content === void 0) { content = ''; }
        if (!this.common.showLoading) {
            return;
        }
        if (!this.loadingIsOpen) {
            this.loadingIsOpen = true;
            this.loading = this.loadingCtrl.create({
                content: content
            });
            this.loading.present();
            setTimeout(function () {
                _this.loadingIsOpen && _this.loading.dismiss();
                _this.loadingIsOpen = false;
            }, __WEBPACK_IMPORTED_MODULE_4__utils_appConstant__["a" /* APPCONSTANT */].REQUEST_TIMEOUT);
        }
    };
    ;
    /**
     * ??????loading
     */
    NativeService.prototype.hideLoading = function () {
        if (!this.common.showLoading) {
            this.common.showLoading = true;
        }
        this.loadingIsOpen && this.loading.dismiss();
        this.loadingIsOpen = false;
    };
    ;
    NativeService.prototype.show = function (content) {
        if (content === void 0) { content = ''; }
        this.loading = this.loadingCtrl.create({
            content: content
        });
        this.loading.present();
    };
    // ??????loading
    NativeService.prototype.hide = function () {
        if (this.loading) {
            this.loading.dismiss();
        }
    };
    /**n b
     */
    NativeService.prototype.detectionUpgrade = function (APK_DOWNLOAD) {
        var _this = this;
        //????????????????????????app???????????????,???????????????app?????????(this.getVersionNumber())??????
        //?????????????????????????????????,??????????????????return
        this.alertControl.create({
            title: '??????',
            subTitle: '???????????????,?????????????????????',
            buttons: [{ text: '??????' },
                {
                    text: '??????',
                    handler: function () {
                        _this.downloadApp(APK_DOWNLOAD);
                    }
                }
            ]
        }).present();
    };
    /**
   * ????????????app
   */
    NativeService.prototype.downloadApp = function (APK_DOWNLOAD) {
        var _this = this;
        var url = encodeURI(APK_DOWNLOAD);
        var alert;
        if (this.isAndroid()) {
            alert = this.alertControl.create({
                title: '???????????????0%',
                enableBackdropDismiss: false,
                buttons: ['????????????']
            });
            alert.present();
            var fileTransfer = this.transfer.create();
            var apk_1 = this.file.externalRootDirectory + 'WMSPDA.apk'; //apk???????????????
            fileTransfer.download(url + 'WMSPDA.apk', apk_1).then(function (entry) {
                _this.fileOpener.open(apk_1, 'application/vnd.android.package-archive');
            }, function (error) {
                console.log(error);
            });
            fileTransfer.onProgress(function (event) {
                var num = Math.floor(event.loaded / event.total * 100);
                if (num === 100) {
                    alert.dismiss();
                }
                else {
                    var title = document.getElementsByClassName('alert-title')[0];
                    title && (title.innerHTML = '???????????????' + num + '%');
                }
            });
        }
        if (this.isIos()) {
            //this.openUrlByBrowser(APP_DOWNLOAD);
        }
    };
    /**
   * ??????app?????????,???0.01
   * @description  ??????/config.xml???version??????
   * @returns {Promise<string>}
   */
    NativeService.prototype.getVersionNumber = function () {
        var _this = this;
        return new Promise(function (resolve) {
            _this.appVersion.getVersionNumber().then(function (value) {
                resolve(value);
            }).catch(function (err) {
                console.log('getVersionNumber:' + err);
            });
        });
    };
    NativeService = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Injectable"])(),
        __metadata("design:paramtypes", [__WEBPACK_IMPORTED_MODULE_1_ionic_angular__["o" /* Platform */],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["q" /* ToastController */],
            __WEBPACK_IMPORTED_MODULE_2__ionic_native_status_bar__["a" /* StatusBar */],
            __WEBPACK_IMPORTED_MODULE_3__ionic_native_splash_screen__["a" /* SplashScreen */],
            __WEBPACK_IMPORTED_MODULE_6__ionic_native_toast__["a" /* Toast */],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["h" /* LoadingController */],
            __WEBPACK_IMPORTED_MODULE_5__utils_common__["a" /* CommonService */],
            __WEBPACK_IMPORTED_MODULE_7__ionic_native_file_transfer__["a" /* FileTransfer */],
            __WEBPACK_IMPORTED_MODULE_8__ionic_native_file__["a" /* File */],
            __WEBPACK_IMPORTED_MODULE_9__ionic_native_app_version__["a" /* AppVersion */],
            __WEBPACK_IMPORTED_MODULE_10__ionic_native_file_opener__["a" /* FileOpener */],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["a" /* AlertController */]])
    ], NativeService);
    return NativeService;
}());

//# sourceMappingURL=NativeService.js.map

/***/ }),

/***/ 65:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return APPCONSTANT; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "e", function() { return ContenType; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "f", function() { return Method; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "b", function() { return Api; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "d", function() { return BaseCodeConstant; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "c", function() { return AppConstant; });
/**
 * ????????????????????????
 * @type
 */
var APPCONSTANT = {
    //??????????????????
    postLoginin: 'UserLogin/',
    //??????????????????,???????????????
    REQUEST_TIMEOUT: 600000,
    ExtranetServerIP: 'lims.yili.com:90',
    ServerIP: '192.168.1.221:8088',
    ServerIP1: 'localhost:8088',
    ServerIP3: '10.168.0.238:8088',
    TENANTID: '000000',
    APPID: 'PDA001',
    version: '1.0.0.8(beta7)',
    ch: {
        /** ??????????????????0???????????? */
        firstDayOfWeek: 0,
        /** ???????????????????????? */
        dayNames: ["?????????", "?????????", "?????????", "?????????", "?????????", "?????????", "?????????"],
        /** ???????????????????????????????????????????????? */
        dayNamesShort: ["??????", "??????", "??????", "??????", "??????", "??????", "??????"],
        /** ???????????????????????? */
        dayNamesMin: ["???", "???", "???", "???", "???", "???", "???"],
        /** ???????????????????????? */
        monthNames: ["1???", "2???", "3???", "4???", "5???", "6???", "7???", "8???", "9???", "10???", "11???", "12???"],
        /** ????????????????????? */
        monthNamesShort: ["1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"]
    },
    //???????????????????????????
    yearrange: "2000:2050",
};
var ContenType = {
    Form: 'application/x-www-form-urlencoded;charset=UTF-8',
    Json: 'application/json;charset=UTF-8',
    Content: ''
};
var Method = {
    Get: 'GET',
    Post: 'POST'
};
var Api = {
    ///"????????????"
    Login: "/blade-auth/oauth/token",
    ///"????????????"
    // ????????????
    Instock: "/api/InStockPDA",
    //?????????????????????????????????
    AsnHeaderList: "/AsnHeaderList",
    //????????????????????????????????????????????????
    AsnHeaderGetAsnHeaderDetail: "/getAsnHeaderDetail",
    //????????????
    submitAsnHeader: "/AsnHeaderSubmitAsnHeader",
    //??????????????????????????????
    getAsnHeaderList: "/getAsnHeaderList",
    getAsnHeaderAndDetails: "/getAsnHeaderAndDetails",
    takewayList: "/ApiPDA/takewayList",
    //????????????????????????
    snlistInfo: "/SnListInfo",
    //???????????????????????????????????? 
    queryStockByLpnCode: "/AsnHeaderQueryStockByLpnCode",
    //????????????
    submitPutaway: "/AsnHeaderSubmitPutaway",
    //????????????
    getStockInfoPutawayForBox: "/getStockInfoPutawayForBox",
    //??????????????????
    submitPutawayForBox: "/submitPutawayForBox",
    //??????LPN??????????????????
    GetAsnLpnDetail: "/getAsnLpnDetail",
    //??????????????????
    StockAddByLPN: "/stockAddByLPN",
    //?????????????????????
    isHasSerial: "/isHasSerial",
    //?????????????????????????????????
    getAsnHeaderInfoForBox: '/getAsnHeaderInfoForBox',
    //?????????????????????????????????
    getAsnHeaderDetailForBox: '/getAsnHeaderDetailForBox',
    //??????????????????????????????????????????????????????
    getLabelInfoWithStock: '/getLabelInfoWithStock',
    //??????????????????
    submitAsnHeaderForBox: '/submitAsnHeaderForBox',
    //??????????????????
    ContainerLogList: '/ContainerLogList',
    //????????????
    getBoxCode: '/getBoxCode',
    ///"????????????"
    // ??????
    api: "/api",
    // ????????????
    ApiPDA: "/ApiPDA/Warehouseselect",
    // ??????
    token: "/ApiPDA/token",
    //????????????
    routes: "/ApiPDA/routes",
    //????????????
    UpdatePassword: "/ApiPDA/UpdatePassword",
    // ????????????????????????
    UpdateVerDetail: "/ApiPDA/UpdateVerDetail",
    //??????????????????
    StockDetail: "/ApiPDA/StockDetail",
    //??????????????????
    BarcodeRuleList: "/ApiPDA/BarcodeRuleList",
    //????????????(?????????)
    skuDetail: "/ApiPDA/skuDetail",
    //????????????
    StockQuery: "/ApiPDA/StockQuery",
    //????????????(?????????)
    SkuList: "/ApiPDA/skuList",
    //????????????????????????????????????
    querySku: "/ApiPDA/querySku",
    //??????????????????????????????(??????)
    stockGetLocByLpnCode: "/ApiPDA/stockGetLocByLpnCode",
    //????????????????????????
    UserOnlineList: '/ApiPDA/userOnlineList',
    //??????????????????????????????
    UserRegisterList: '/ApiPDA/userRegisterList',
    //????????????????????????????????????
    UserOnlinesubmit: '/ApiPDA/userOnlinesubmit',
    //??????????????????????????????
    userRegisterListMoth: '/ApiPDA/userRegisterListMoth',
    //??????????????????
    printTemplateList: '/ApiPDA/printTemplateList',
    //????????????
    ParamList: "/ApiPDA/ParamList",
    //????????????ID??????????????????????????????????????????
    skuLotList: "/skuLotList",
    ///"????????????"
    // 
    Outstock: "/api/OutStock",
    // ????????????
    submitMOVE: "/submitMOVE",
    //????????????????????????
    getPickInfo: "/getPickInfo",
    //??????????????????????????????
    getPickInfoByBox: "/getPickInfoByBox",
    //??????????????????
    submitPickInfo: '/submitPickInfo',
    //????????????????????????
    submitPickInfoByBox: '/submitPickInfoByBox',
    getTotalScanQtyBySku: '/getTotalScanQtyBySku',
    //??????????????????
    getSopickLog: '/getSopickLog',
    //?????????????????????
    OutIsHasSerial: '/isHasSerial',
    //????????????????????????
    getOutstockLpnInfo: '/getOutstockLpnInfo',
    //????????????????????????
    submitOutStockLpnInfo: '/submitOutStockLpnInfo',
    //?????????????????????
    getLpnCode: '/getLpnCode',
    ///"????????????"
    // 
    StockCountPDA: "/api/StockCountPDA",
    //??????????????????
    cyclecountrantask: "//detailPda",
    //??????????????????
    countRecords: '/countRecords',
    //??????????????????
    randomCountSubmit: "/randomCountSubmit",
    //???????????????????????????
    randomCheckSubmit: "/randomCheckSubmit",
    //???????????????????????????
    randomCheckTaskSubmit: "/randomCheckTaskSubmit",
    //????????????????????????
    stockListPda: "/stockListPda",
    //??????????????????
    billCountSubmit: "/billCountSubmit",
    //???????????????????????????
    CountPDAisHasSerial: "/isHasSerial",
    //?????????????????????
    completeTask: '/completeTaskPda',
    ///"????????????"
    // 
    LoadingPDA: "/api/LoadingPDA",
    //??????????????????
    deleteTruckDetail: '/deleteTruckDetail',
    //????????????????????????????????????
    getTruckHeader: '/getTruckHeader',
    //??????LPNCode????????????????????????
    getSkuStock: '/getSkuStock',
    //??????LPNCode????????????????????????
    saveSoTruckDetail: '/saveSoTruckDetail',
    //??????LPNCode????????????????????????
    getSkuStockDetailList: '/getSkuStockDetailList',
    //??????????????????
    // confirmSo: '/confirmSo',
    //????????????
    SaveconfirmSo: '/SaveconfirmSo',
    //???????????????????????????
    soRegisterDetail: '/soRegisterDetail',
    //????????????
    soTruckHeaderSubmit: '/soTruckHeaderSubmit',
    //????????????
    Lodingdetail: '/Lodingdetail',
    ///"????????????"
    // 
    StockMove: "/api/StockMove",
    //????????????????????????????????????
    getLocCode: "/getLocCode",
    //??????????????????
    getSkuInfo: "/getSkuInfo",
    //????????????????????????????????????
    submitSku: '/submitSku',
    //????????????????????????
    submitLotNumberSku: '/submitLotNumberSku',
    //????????????????????????
    submitMoveStockSku: '/submitMoveStockSku',
    //?????????????????????
    StockIsHasSerial: '/isHasSerial',
    ///"????????????"
    // 
    taskPDA: "/api/taskPDA",
    //??????????????????
    taskPDAList: '/list',
    //????????????
    taskPDAClose: '/close',
    ///????????????
    //
    stockInner: '/api/stockInner',
    //???????????????
    upPickList: '/upPickList',
    //??????????????????
    stockInnerSubmitPickInfo: '/submitPickInfo',
    //???????????????
    upPack: '/upPack',
    //??????????????????
    submitPackInfo: '/submitPackInfo',
    //??????????????????
    packStockList: '/packStockList',
    //????????????-?????????????????????
    getMoveStock: '/getMoveStock',
    //????????????-??????????????????
    verifyStockForMoveByBox: '/verifyStockForMoveByBox',
    //????????????-????????????????????????
    submitStockForMoveByBox: '/submitStockForMoveByBox',
    //??????-????????????????????????
    getReplenishmenInfo: '/getReplenishmenInfo',
    //??????-??????????????????
    getSkuListByCode: '/getSkuListByCode',
    //??????-??????????????????
    submitReplenishmen: '/submitReplenishmen',
    //??????-?????????????????????????????????
    getUnfinishReplenishmenList: '/getUnfinishReplenishmenList',
    ///????????????
    //
    bladeSystem: '/blade-system',
    dictList: '/dict/dictionary'
};
var BaseCodeConstant = {
    //????????????(?????????)
    UnAllot: "10",
    //????????????(?????????)
    UnComplate: "11",
    //????????????(?????????)
    Complated: "12",
    // ??????????????????
    task_type_101: "101",
    // ????????????
    task_type_102: "102",
    // ????????????
    task_type_103: "103",
    //????????????
    task_type_104: "104",
    //????????????
    task_type_106: "106",
    //????????????
    task_type_107: "107",
    //????????????
    task_type_108: "108",
    // ????????? ???????????????(?????????)
    SN_STATUS_1: "1",
    // ????????? ???????????????(?????????)
    SN_STATUS_2: "2",
    // ???????????????????????????
    CTL_STATE_FREE: "40",
    // ???????????????????????????
    CTL_STATE_OCCUPY: "41",
    // ????????????(??????)
    IS_ACTIVE_N: "N",
    // ????????????(??????)
    IS_ACTIVE_Y: "Y",
    // ????????????
    SPACE_STRING: " ",
    // ?????????
    NULL_STRING: "",
    // ???????????????????????????
    //  public const char SPLIT_STRING: '|',
    // ?????? - ?????????
    IS_ACTIVE_ING: "1",
    // ?????? -?????????
    IS_ACTIVE_CLOSED: "0",
    /// ????????????
    // ??????????????????
    OUTSTORE_TYPE: "110",
    // ????????????????????????????????????
    OUTSTORE_TYPE_10: "10",
    // ????????????????????????????????????
    OUTSTORE_TYPE_20: "20",
    // ????????????????????????????????????
    OUTSTORE_TYPE_30: "30",
    // ????????????????????????????????????
    OUTSTORE_TYPE_40: "40",
    // ????????????????????????????????????
    OUTSTORE_TYPE_50: "50",
    /// "????????????"
    // ????????????????????????
    IN_STORE_TYPE: "103",
    // ??????????????????????????????
    IN_STORE_TYPE_PURCHASE: "5",
    //?????????????????????????????????
    IN_STORE_Inventory_PURCHASE: "11",
    //???????????????????????????????????????
    IN_STORE_QUALITY__PURCHASE: "12",
    // ??????????????????????????????
    IN_STORE_TYPE_ROUTINE: "10",
    // ??????????????????????????????
    IN_STORE_TYPE_NOT_BILL: "20",
    // ??????????????????????????????
    IN_STORE_TYPE_ALLOT: "30",
    // ??????????????????????????????
    IN_STORE_TYPE_BORROW: "40",
    // ??????????????????????????????
    IN_STORE_TYPE_REPAIR: "50",
    // ??????????????????????????????
    IN_STORE_TYPE_RETURN: "60",
    /// "????????????"
    // ??????????????????
    DATE_TIME: "yyyy/MM/dd",
    // ??????????????????
    DATE_TIME_TWO: "yyyyMMdd",
    // ??????????????????
    DATE_TIME_OTHER: "yyyy-MM-dd",
    ///"????????????"
    /// "????????????"
    // ??????????????????????????????
    SCRAP_TYPE_10: "10",
    // ??????????????????????????????
    SCRAP_TYPE_20: "20",
    ///"????????????"
    // ??????????????????1
    DEFAULT_SKU_LEVEL: 1,
    // ??????????????????1
    DEFAULT_CONVERT_QTY: 1,
    /// "??????????????????"
    // ??????????????????-?????????
    COUNT_LOCATION_COMPLETE: "L2",
    // ?????????????????????
    //  NOT_COUNT_STATE: { "10", "30" },
    /// ???????????????
    // ???????????????
    SO_BILL_STATE: "105",
    // ????????????
    SO_BILL_STATE_10: "10",
    // ?????????
    SO_BILL_STATE_20: "20",
    // ????????????
    SO_BILL_STATE_30: "30",
    // ?????????
    SO_BILL_STATE_40: "40",
    // ?????????(2??????)
    SO_BILL_STATE_91: "91",
    // ??????
    SO_BILL_STATE_92: "92",
    // ?????????
    SO_BILL_STATE_93: "93",
    // ?????????????????????
    //  SO_NOT_COMPLETE_STATE: { "10", "20", "30" },
    /// "??????????????????"
    // ?????????????????????
    IN_STORE_STATE: "104",
    // ?????????????????????-????????????
    IN_STORE_STATE_CREAT: "10",
    // ?????????????????????-?????????
    IN_STORE_STATE_HANDLE: "20",
    // ?????????????????????-??????
    IN_STORE_STATE_COMPLETE: "40",
    // ?????????????????????-??????
    IN_STORE_STATE_RETURN: "90",
    // ?????????????????????
    //  IN_STORE_NOT_COMPLETE_STATE: { "10", "20", "30", "90" },
    // ??????????????????
    //  IN_STORE_REGISTER_STATE: { "10", "30" },
    /// "??????????????????"
    // ?????????????????????
    COUNT_STATE: "111",
    // ?????????????????????-????????????
    COUNT_STATE_CREAT: "10",
    // ?????????????????????-????????????
    COUNT_STATE_HANDLE: "30",
    // ?????????????????????-????????????
    COUNT_STATE_COMPLETE: "40",
    // ?????????????????????-?????????
    COUNT_STATE_ABNADONED: "50",
    // ?????????????????????-?????????
    COUNT_STATE_EXECUTE: "90",
    // ?????????????????????
    //  NOT_COMPLETE_STATE: { "10", "20", "30", "40", "60", "70" },
    /// "??????????????????"
    // ?????????????????????
    SCRAP_STATE: "130",
    // ?????????????????????(????????????)
    SCRAP_STATE_10: "10",
    // ?????????????????????(?????????)
    SCRAP_STATE_20: "20",
    // ?????????????????????(????????????)
    SCRAP_STATE_30: "30",
    // ?????????????????????(?????????)
    SCRAP_STATE_40: "40",
    // ?????????????????????
    //  SCRAP_STATE_NOT_COMPLETE_STATE: { "10", "20", "30" },
    /// "??????????????????"
    // ?????????????????????
    TRANS_STATE: "117",
    // ?????????????????????-????????????
    TRANS_STATE_CREAT: "10",
    // ?????????????????????-????????????
    TRANS_STATE_HANDLE: "20",
    // ?????????????????????-?????????
    TRANS_STATE_COMPLETE: "30",
    // ?????????????????????
    //  TRANS_STATE_NOT_COMPLETE_STATE: { "10", "20" },
    // ???????????????
    MANAGE_MODE_2: "2",
    // ????????????
    MAX_SKU_LEVEL: "1",
    //??????????????????
    INVENTORY_0: "0",
    //??????????????????
    INVENTORY_1: "1",
    //??????????????????
    INVENTORY_2: "2",
    //??????????????????
    PUtAWAY_0: "0",
    //??????????????????
    PUtAWAY_1: "1",
    //??????????????????
    PUtAWAY_2: "2",
};
//???????????????
var AppConstant = {
    skuLot: 'skuLot',
    MaxLength: 999,
    Date: 'Date',
    Text: 'Text',
    Batch: 1,
    BatchList: 3,
    serialNumber: 2,
    serialNumberListNum: 4,
    Warehouse: 0,
    buttonAffirm: '??????',
    buttonEnt: '??????',
    /*??????????????????*/
    ruleCode_00: 0,
    ruleCode_01: 1,
    ruleCode_02: 2,
    /*??????????????????*/
    labelType: {
        sample: 'sample',
        process: 'process',
        standard: 'process',
        simple: 'simple',
        common: 'process',
        production: 'process',
        other: 'process',
    },
};
//# sourceMappingURL=appConstant.js.map

/***/ }),

/***/ 680:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return ParamModel; });
var ParamModel = /** @class */ (function () {
    function ParamModel() {
    }
    return ParamModel;
}());

//# sourceMappingURL=ParamModel.js.map

/***/ }),

/***/ 681:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return SerialNumberComponent; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
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
 * Generated class for the SerialNumberComponent component.
 *
 * See https://angular.io/api/core/Component for more info on Angular
 * Components.
 */
var SerialNumberComponent = /** @class */ (function () {
    function SerialNumberComponent() {
        this.NumberListFalag = 1;
        console.log('Hello SerialNumberComponent Component');
        // this.text = 'Hello World';
    }
    SerialNumberComponent = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Component"])({
            selector: 'serial-number',template:/*ion-inline-start:"D:\developer_tools\workspace\wms_3.0_base_pda\src\components\serial-number\serial-number.html"*/'<!-- Generated template for the SerialNumberComponent component -->\n\n<div>???????????????</div>\n\n<!-- ??????????????? -->\n\n<!-- <div class="list_item_serial">\n\n  <div class="list_item_title">\n\n    <span [ngStyle]="{\'border-bottom\':isTilebool === true ? \'3px solid #008080\' : \'0\' }"\n\n      (click)="isTitle(true)">???????????????(<b></b>)</span>\n\n    <span [ngStyle]="{\'border-bottom\':isTilebool === false ? \'3px solid #008080\' : \'0\' }"\n\n      (click)="isTitle(false)">???????????????(<b></b>)</span>\n\n  </div>\n\n\n\n</div> -->\n\n<!-- ?????????????????? -->\n\n<!-- <div *ngIf="NumberListFalag==1">\n\n  <ion-list class="list_item_div">\n\n    <ion-scroll scrollY="true" style="height:35rem;">\n\n      <ion-item-sliding *ngFor="let item of serialNumber">\n\n        <ion-item>\n\n          <h2>{{item}}</h2>\n\n        </ion-item>\n\n        <ion-item-options side="right">\n\n          <button (click)="removeItem(item)" ion-button color="danger">\n\n            ??????\n\n          </button>\n\n        </ion-item-options>\n\n      </ion-item-sliding>\n\n    </ion-scroll>\n\n  </ion-list>\n\n</div> -->\n\n<!-- ??????????????? -->\n\n<!-- <div *ngIf="NumberListFalag==2 ">\n\n  <ion-list class="list_item_div">\n\n    <ion-scroll scrollY="true" style="height:35rem;">\n\n      <ion-item-sliding *ngFor="let item of Sn">\n\n        <ion-item>\n\n          <h2>{{item.snDetailCode}}</h2>\n\n        </ion-item>\n\n      </ion-item-sliding>\n\n    </ion-scroll>\n\n  </ion-list>\n\n</div> -->'/*ion-inline-end:"D:\developer_tools\workspace\wms_3.0_base_pda\src\components\serial-number\serial-number.html"*/
        }),
        __metadata("design:paramtypes", [])
    ], SerialNumberComponent);
    return SerialNumberComponent;
}());

//# sourceMappingURL=serial-number.js.map

/***/ }),

/***/ 682:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return NoDblClickDirective; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};

var NoDblClickDirective = /** @class */ (function () {
    // public defaultColor = 'pink';//??????????????????
    function NoDblClickDirective(el) {
        this.el = el;
        //???????????????????????????
        // this.el.nativeElement.style.backgroundColor = this.defaultColor;
    }
    //????????????????????????????????????????????????????????????????????????????????????
    //?????????????????????????????????(??????disabled?????????true,3????????????false)
    // @HostListener('click')
    // click() {
    //   this.el.nativeElement.disabled = true;
    //   setTimeout(() => {
    //     this.el.nativeElement.disabled = false;
    //   }, 1000)
    // }
    //???????????????????????????????????????????????????????????????
    NoDblClickDirective.prototype.onTouchStart = function () {
        var _this = this;
        this.el.nativeElement.disabled = true;
        setTimeout(function () {
            _this.el.nativeElement.disabled = false;
        }, 1000);
    };
    //????????????????????????????????????????????????????????????
    // @HostListener('touchend')
    // onTouchsEnd() {
    //   debugger;
    // }
    //??????????????????
    // @HostListener('window:keydown', ['$event'])
    // handleKeyDown(event: KeyboardEvent) {
    //   if (event.key === 'Enter') {
    //     //???????????????????????????
    //     this.el.nativeElement.disabled = true;
    //     setTimeout(() => {
    //       this.el.nativeElement.disabled = false;
    //     }, 500)
    //   }
    // }
    NoDblClickDirective.prototype.highlight = function (color) {
        this.el.nativeElement.style.backgroundColor = color;
    };
    __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Input"])('no-dbl-click'),
        __metadata("design:type", String)
    ], NoDblClickDirective.prototype, "highlightColor", void 0);
    __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["HostListener"])('touchstart'),
        __metadata("design:type", Function),
        __metadata("design:paramtypes", []),
        __metadata("design:returntype", void 0)
    ], NoDblClickDirective.prototype, "onTouchStart", null);
    NoDblClickDirective = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Directive"])({ selector: '[no-dbl-click]' }),
        __metadata("design:paramtypes", [__WEBPACK_IMPORTED_MODULE_0__angular_core__["ElementRef"]])
    ], NoDblClickDirective);
    return NoDblClickDirective;
}());

//# sourceMappingURL=no-dbl-click.js.map

/***/ }),

/***/ 700:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return MyApp; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(43);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__ionic_native_status_bar__ = __webpack_require__(90);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__ionic_native_splash_screen__ = __webpack_require__(121);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__services_NativeService__ = __webpack_require__(64);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5__ionic_native_android_permissions__ = __webpack_require__(358);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_6__ionic_native_keyboard__ = __webpack_require__(160);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};








var MyApp = /** @class */ (function () {
    function MyApp(platform, statusBar, splashScreen, nativeService, events, keyboard, ionicApp, androidPermissions) {
        var _this = this;
        this.platform = platform;
        this.nativeService = nativeService;
        this.events = events;
        this.keyboard = keyboard;
        this.ionicApp = ionicApp;
        this.androidPermissions = androidPermissions;
        this.rootPage = 'LoginPage';
        this.windows = Window;
        this.backButtonPressed = false;
        this.window = window;
        platform.ready().then(function () {
            // Okay, so the platform is ready and our plugins are available.
            // Here you can do any higher level native things you might need.
            statusBar.styleDefault();
            statusBar.backgroundColorByHexString("#ffffff"); // => #FFAABB
            // statusBar.overlaysWebView(true);
            _this.nativeService.statusBarStyleDefault();
            _this.nativeService.splashScreenHide();
            _this.nativeService.statusBarStyleDefault();
            _this.getPrmissions();
            _this.nativeService.splashScreenHide();
            // //???????????????
            _this.scanInit();
            //??????????????????
            _this.deviceResume();
            //??????????????????
            _this.closeScanHeader();
            //??????????????????
            _this.registerBackButtonAction();
            // //??????????????????
            // this.inikeycode();
        });
    }
    /**
  * pda???????????????
  */
    MyApp.prototype.scanInit = function () {
        //pda???????????????
        if (this.platform.is('android')) {
            this.deviceBind = this.onDeviceReady.bind(this);
            //???????????????
            document.addEventListener("deviceready", this.deviceBind);
        }
    };
    /**
     * ???????????????????????????
     */
    MyApp.prototype.deviceResume = function () {
        var _this = this;
        this.platform.resume.subscribe(function () {
            _this.scanInit();
        });
    };
    /**
     * pda???????????????
     */
    MyApp.prototype.onDeviceReady = function () {
        this.initHardBarcode();
    };
    /**
     * ??????????????????
     */
    MyApp.prototype.initHardBarcode = function () {
        var that = this;
        cordova.plugins.barCodePlugin.setReceiveScanCallback(function (data) {
            data = data.replace(/[\r\n]/g, "");
            that.events.publish('barcode:scan', data, Date.now());
        });
        // cordova.plugins.autoid9Plugin.initialise(() => {
        //   cordova.plugins.autoid9Plugin.setReceiveScanCallback(function (data: any) {
        //     // alert(data);
        //     data = data.replace(/[\r\n]/g, "");
        //     that.events.publish('barcode:scan', data, Date.now());
        //   });
        // });
    };
    /**
     *  ??????????????????
     */
    MyApp.prototype.closeScanHeader = function () {
        var _this = this;
        this.platform.pause.subscribe(function () {
            if (_this.platform.is('android')) {
                // cordova.plugins.autoid9Plugin.destroy(function () {
                // });
                //????????????
                document.removeEventListener("deviceready", _this.deviceBind);
                _this.deviceBind = null;
            }
        });
    };
    /**
     * ????????????????????????
     */
    MyApp.prototype.registerBackButtonAction = function () {
        var _this = this;
        this.platform.registerBackButtonAction(function () {
            if (_this.keyboard.isVisible) {
                _this.keyboard.hide();
                return;
            }
            var activePortal = _this.ionicApp._loadingPortal.getActive() ||
                _this.ionicApp._modalPortal.getActive() ||
                _this.ionicApp._toastPortal.getActive() ||
                _this.ionicApp._overlayPortal.getActive();
            if (activePortal) {
                // ready = false;
                activePortal.dismiss();
                activePortal.onDidDismiss(function () {
                });
                console.log("handled with portal");
                return;
            }
            if (_this.nav.canGoBack()) {
                _this.nav.pop();
                return;
            }
            else {
                //????????????
                _this.showExit();
            }
        }, 1);
    };
    /**
     * ????????????????????????
     */
    // inikeycode() {
    //   let that = this;
    //   this.keyboard.onKeyboardWillShow().subscribe(() => {
    //     alert('false');
    //     that.events.publish('keyBoard:scan', false, Date.now());
    //   });
    //   this.keyboard.onKeyboardWillHide().subscribe(() => {
    //     that.events.publish('keyBoard:scan', true, Date.now());
    //     alert('true');
    //   });
    // }
    //?????????????????????
    MyApp.prototype.showExit = function () {
        var _this = this;
        if (this.backButtonPressed) {
            this.platform.exitApp();
        }
        else {
            this.nativeService.showToast('????????????????????????');
            this.backButtonPressed = true;
            setTimeout(function () {
                _this.backButtonPressed = false;
            }, 2000);
        }
    };
    MyApp.prototype.getPrmissions = function () {
        var _this = this;
        this.androidPermissions.checkPermission(this.androidPermissions.PERMISSION.WRITE_EXTERNAL_STORAGE).then(function (result) {
            console.log('??????????????????:' + result.hasPermission);
        }, function (err) {
            //??????????????????
            _this.androidPermissions.requestPermission(_this.androidPermissions.PERMISSION.WRITE_EXTERNAL_STORAGE);
        });
        return new Promise(function (resolve) {
            _this.androidPermissions.requestPermissions([_this.androidPermissions.PERMISSION.WRITE_EXTERNAL_STORAGE, _this.androidPermissions.PERMISSION.GET_ACCOUNTS]).then(function (res) {
                resolve(res);
            });
        });
    };
    __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["ViewChild"])('myNav'),
        __metadata("design:type", __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["k" /* Nav */])
    ], MyApp.prototype, "nav", void 0);
    MyApp = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Component"])({template:/*ion-inline-start:"D:\developer_tools\workspace\wms_3.0_base_pda\src\app\app.html"*/'<ion-nav  #myNav [root]="rootPage"></ion-nav>\n\n\n\n\n\n'/*ion-inline-end:"D:\developer_tools\workspace\wms_3.0_base_pda\src\app\app.html"*/
        }),
        __metadata("design:paramtypes", [__WEBPACK_IMPORTED_MODULE_1_ionic_angular__["o" /* Platform */],
            __WEBPACK_IMPORTED_MODULE_2__ionic_native_status_bar__["a" /* StatusBar */],
            __WEBPACK_IMPORTED_MODULE_3__ionic_native_splash_screen__["a" /* SplashScreen */],
            __WEBPACK_IMPORTED_MODULE_4__services_NativeService__["a" /* NativeService */],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["c" /* Events */],
            __WEBPACK_IMPORTED_MODULE_6__ionic_native_keyboard__["a" /* Keyboard */],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["d" /* IonicApp */],
            __WEBPACK_IMPORTED_MODULE_5__ionic_native_android_permissions__["a" /* AndroidPermissions */]])
    ], MyApp);
    return MyApp;
}());

//# sourceMappingURL=app.component.js.map

/***/ }),

/***/ 89:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return Utils; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
/**
 * Created by yanxiaojun617@163.com on 12-27.
 */

/**
 * Utils???????????????????????????????????????
 * @description
 */
var Utils = /** @class */ (function () {
    function Utils() {
    }
    Utils_1 = Utils;
    //??????????????????
    Utils.clearSpace = function (value) {
        return value.replace(/\s*/g, "");
    };
    Utils.isEmpty = function (value) {
        return value == null || value == undefined || typeof value === 'string' && value.length === 0 || value.length === 0 || JSON.stringify(value) == "{}";
    };
    Utils.isNotEmpty = function (value) {
        return !Utils_1.isEmpty(value);
    };
    /**
     * ???????????????or?????????
     * @param value
     * @returns {string|string}
     */
    Utils.formatYesOrNo = function (value) {
        return value == 1 ? '???' : (value == '0' ? '???' : null);
    };
    /**
     * ?????????????????????????????????
     * @param date ??????????????????????????????
     * @param sFormat ????????????,?????????yyyy-MM-dd                        ??????y?????????M?????????d?????????h?????????m?????????s
     * @example  dateFormat(new Date())                               "2017-02-28"
     * @example  dateFormat(new Date(),'yyyy-MM-dd')                  "2017-02-28"
     * @example  dateFormat(new Date(),'yyyy-MM-dd HH:mm:ss')         "2017-02-28 13:24:00"   ps:HH:24?????????
     * @example  dateFormat(new Date(),'yyyy-MM-dd hh:mm:ss')         "2017-02-28 01:24:00"   ps:hh:12?????????
     * @example  dateFormat(new Date(),'hh:mm')                       "09:24"
     * @example  dateFormat(new Date(),'yyyy-MM-ddTHH:mm:ss+08:00')   "2017-02-28T13:24:00+08:00"
     * @example  dateFormat(new Date('2017-02-28 13:24:00'),'yyyy-MM-ddTHH:mm:ss+08:00')   "2017-02-28T13:24:00+08:00"
     * @returns {string}
     */
    Utils.dateFormat = function (date, sFormat) {
        if (sFormat === void 0) { sFormat = 'yyyy-MM-dd'; }
        var time = {
            Year: 0,
            TYear: '0',
            Month: 0,
            TMonth: '0',
            Day: 0,
            TDay: '0',
            Hour: 0,
            THour: '0',
            hour: 0,
            Thour: '0',
            Minute: 0,
            TMinute: '0',
            Second: 0,
            TSecond: '0',
            Millisecond: 0
        };
        time.Year = date.getFullYear();
        time.TYear = String(time.Year).substr(2);
        time.Month = date.getMonth() + 1;
        time.TMonth = time.Month < 10 ? '0' + time.Month : String(time.Month);
        time.Day = date.getDate();
        time.TDay = time.Day < 10 ? '0' + time.Day : String(time.Day);
        time.Hour = date.getHours();
        time.THour = time.Hour < 10 ? '0' + time.Hour : String(time.Hour);
        time.hour = time.Hour < 13 ? time.Hour : time.Hour - 12;
        time.Thour = time.hour < 10 ? '0' + time.hour : String(time.hour);
        time.Minute = date.getMinutes();
        time.TMinute = time.Minute < 10 ? '0' + time.Minute : String(time.Minute);
        time.Second = date.getSeconds();
        time.TSecond = time.Second < 10 ? '0' + time.Second : String(time.Second);
        time.Millisecond = date.getMilliseconds();
        return sFormat.replace(/yyyy/ig, String(time.Year))
            .replace(/yyy/ig, String(time.Year))
            .replace(/yy/ig, time.TYear)
            .replace(/y/ig, time.TYear)
            .replace(/MM/g, time.TMonth)
            .replace(/M/g, String(time.Month))
            .replace(/dd/ig, time.TDay)
            .replace(/d/ig, String(time.Day))
            .replace(/HH/g, time.THour)
            .replace(/H/g, String(time.Hour))
            .replace(/hh/g, time.Thour)
            .replace(/h/g, String(time.hour))
            .replace(/mm/g, time.TMinute)
            .replace(/m/g, String(time.Minute))
            .replace(/ss/ig, time.TSecond)
            .replace(/s/ig, String(time.Second))
            .replace(/fff/ig, String(time.Millisecond));
    };
    Utils.urlFormat = function (sFormat) {
        return sFormat.replace(/\+/g, String('%2B'))
            .replace(/\s+/g, String('%20'))
            .replace(/\?/g, '%3F')
            .replace(/#/g, '%23')
            .replace(/&/g, '%26')
            .replace(/=/g, '%3D');
    };
    /**
     * ??????????????????
     * @param dateStr
     * @param separator
     */
    Utils.stringToDate = function (dateStr, separator) {
        if (!separator) {
            separator = "-";
        }
        var dateArr = dateStr.split(separator);
        var year = parseInt(dateArr[0]);
        var month;
        //???????????????04???????????????                         
        if (dateArr[1].indexOf("0") == 0) {
            month = parseInt(dateArr[1].substring(1));
        }
        else {
            month = parseInt(dateArr[1]);
        }
        var day = parseInt(dateArr[2]);
        var date = new Date(year, month - 1, day);
        return date;
    };
    /**
     * ???????????????????????????????????????2
     * @param str
     * @returns {number}
     */
    Utils.strLength = function (str) {
        var len = 0;
        for (var i = 0, length_1 = str.length; i < length_1; i++) {
            str.charCodeAt(i) > 255 ? len += 2 : len++;
        }
        return len;
    };
    /** ?????????????????????32?????????????????? */
    Utils.uuid = function () {
        var text = '';
        var possible = 'abcdef0123456789';
        for (var i = 0; i < 19; i++) {
            text += possible.charAt(Math.floor(Math.random() * possible.length));
        }
        return text + new Date().getTime();
    };
    /**
   * ????????????????????????????????????
   * @type Date
   */
    Utils.get3YearAfter = function () {
        var date = new Date();
        var year = date.getFullYear() + 3;
        var month = date.getMonth() + 1;
        var Tmonth = month < 10 ? '0' + month : String(month);
        var day = date.getDate();
        var Tday = day < 10 ? '0' + day : String(day);
        return new Date(year.toString() + "-" + Tmonth.toString() + "-" + Tday.toString());
    };
    /**
     * ?????????(YYYYMMDD)?????????
     * @type Date
     */
    Utils.getDateFromYYYYMMDD = function (str) {
        var year = str.substr(0, 4);
        var month = str.substr(4, 2);
        var day = str.substr(6, 2);
        return year + "-" + month + "-" + day;
    };
    /**
     * ?????????(YYYY*MM*DD)?????????
     * @type Date
     */
    Utils.getDateFromWhatever = function (str) {
        var year = str.substr(0, 4);
        var month = str.substr(5, 2);
        var day = str.substr(8, 2);
        var symbol = str.substr(4, 1);
        return year + "-" + month + "-" + day;
    };
    /**
    * ???????????????????????????
    * @type Date
    */
    Utils.getDateBath = function (dateType, date) {
        if (date.length == 8) {
            return this.getDateFromYYYYMMDD(date);
        }
        else if (date.length == 10) {
            return this.getDateFromWhatever(date);
        }
        else {
            return '';
        }
    };
    /**
    * ???????????????????????????
    * @type Date
    */
    Utils.postDateBath = function (dateType, date) {
        if (dateType.length == 8) {
            return date.replace(/-/g, '');
        }
        else if (dateType.length == 10) {
            return date.replace(/-/g, dateType.substr(4, 1));
        }
        else {
            return '';
        }
    };
    /**
     * ?????????????????????
     * @type Boolean
     */
    Utils.IsPositiveInt = function (str) {
        if (Utils_1.isEmpty(str)) {
            return false;
        }
        if (str.trim() === ("0")) {
            return false;
        }
        var rules = /^[1-9]\d*$/g;
        var regexp = new RegExp(rules);
        return regexp.test(str);
    };
    /**
    * ?????????????????????????????????
    * @type Boolean
    */
    Utils.IsPositiveDecimal = function (str) {
        var rules = /^\d+(\.\d+)?$/g;
        var regexp = new RegExp(rules);
        return regexp.test(str);
    };
    /**
  * ??????????????????
  * @type Boolean
  */
    Utils.Contains = function (str, value) {
        return str.indexOf(value) != -1;
    };
    /**
     * ???????????????
     * @param str
     */
    Utils.ToCDB = function (str) {
        var tmp = "";
        for (var i = 0; i < str.length; i++) {
            if (str.charCodeAt(i) > 65248 && str.charCodeAt(i) < 65375) {
                tmp += String.fromCharCode(str.charCodeAt(i) - 65248);
            }
            else {
                tmp += String.fromCharCode(str.charCodeAt(i));
            }
        }
        return tmp;
    };
    /**
     * ???????????????
     * @param txtstring
     */
    Utils.ToDBC = function (txtstring) {
        var tmp = "";
        for (var i = 0; i < txtstring.length; i++) {
            if (txtstring.charCodeAt(i) == 32) {
                tmp = tmp + String.fromCharCode(12288);
            }
            else if (txtstring.charCodeAt(i) < 127) {
                tmp = tmp + String.fromCharCode(txtstring.charCodeAt(i) + 65248);
            }
        }
        return tmp;
    };
    Utils.isObjectValueEqual = function (a, b) {
        //?????????a???b????????????
        var aProps = Object.getOwnPropertyNames(a);
        var bProps = Object.getOwnPropertyNames(b);
        //??????????????????length????????????
        if (aProps.length != bProps.length) {
            return false;
        }
        //?????????????????????????????????????????????????????? 
        for (var i = 0; i < aProps.length; i++) {
            var propName = aProps[i];
            if (a[propName] !== b[propName]) {
                return false;
            }
        }
        return true;
    };
    Utils.isObjectValueEqual1 = function (a, b, nots) {
        // ?????????????????????????????????????????????????????????????????????true
        if (a === b)
            return true;
        // ??????????????????????????????
        var aProps = Object.getOwnPropertyNames(a);
        var bProps = Object.getOwnPropertyNames(b);
        // ??????????????????????????????????????????????????????????????????false
        // if (aProps.length !== bProps.length) return false
        // ?????????????????????
        for (var prop in a) {
            if (nots.includes(prop))
                continue;
            // ??????a???????????????b????????????????????????????????????false
            if (b.hasOwnProperty(prop)) {
                // ??????a??????????????????????????????????????????????????????????????????????????????????????????????????????false
                if (typeof a[prop] === 'object') {
                    if (!Utils_1.isObjectValueEqual1(a[prop], b[prop], nots))
                        return false;
                }
                else if (a[prop] !== b[prop]) {
                    return false;
                }
            }
            else {
                return false;
            }
        }
        return true;
    };
    Utils.contains = function (a, obj, nots) {
        for (var i = 0; i < a.length; i++) {
            if (Utils_1.isObjectValueEqual1(a[i], obj, nots)) {
                return true;
            }
        }
        return false;
    };
    /**
     * ????????????sequence???1
     * @type {()=>number}
     */
    Utils.getSequence = (function () {
        var sequence = 1;
        return function () {
            return ++sequence;
        };
    })();
    Utils = Utils_1 = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Injectable"])()
    ], Utils);
    return Utils;
    var Utils_1;
}());

//# sourceMappingURL=Utils.js.map

/***/ }),

/***/ 91:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return CommonService; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__ionic_storage__ = __webpack_require__(66);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2_rxjs__ = __webpack_require__(161);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2_rxjs___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_2_rxjs__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__services_Utils__ = __webpack_require__(89);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__appConstant__ = __webpack_require__(65);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : new P(function (resolve) { resolve(result.value); }).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
var __generator = (this && this.__generator) || function (thisArg, body) {
    var _ = { label: 0, sent: function() { if (t[0] & 1) throw t[1]; return t[1]; }, trys: [], ops: [] }, f, y, t, g;
    return g = { next: verb(0), "throw": verb(1), "return": verb(2) }, typeof Symbol === "function" && (g[Symbol.iterator] = function() { return this; }), g;
    function verb(n) { return function (v) { return step([n, v]); }; }
    function step(op) {
        if (f) throw new TypeError("Generator is already executing.");
        while (_) try {
            if (f = 1, y && (t = y[op[0] & 2 ? "return" : op[0] ? "throw" : "next"]) && !(t = t.call(y, op[1])).done) return t;
            if (y = 0, t) op = [0, t.value];
            switch (op[0]) {
                case 0: case 1: t = op; break;
                case 4: _.label++; return { value: op[1], done: false };
                case 5: _.label++; y = op[1]; op = [0]; continue;
                case 7: op = _.ops.pop(); _.trys.pop(); continue;
                default:
                    if (!(t = _.trys, t = t.length > 0 && t[t.length - 1]) && (op[0] === 6 || op[0] === 2)) { _ = 0; continue; }
                    if (op[0] === 3 && (!t || (op[1] > t[0] && op[1] < t[3]))) { _.label = op[1]; break; }
                    if (op[0] === 6 && _.label < t[1]) { _.label = t[1]; t = op; break; }
                    if (t && _.label < t[2]) { _.label = t[2]; _.ops.push(op); break; }
                    if (t[2]) _.ops.pop();
                    _.trys.pop(); continue;
            }
            op = body.call(thisArg, _);
        } catch (e) { op = [6, e]; y = 0; } finally { f = t = 0; }
        if (op[0] & 5) throw op[1]; return { value: op[0] ? op[1] : void 0, done: true };
    }
};





var CommonService = /** @class */ (function () {
    function CommonService(storage) {
        this.storage = storage;
        this._showLoading = true;
    }
    CommonService.prototype.setData = function (keyName, data) {
        this.storage.set(keyName, data);
    };
    CommonService.prototype.getData = function (keyName) {
        return __awaiter(this, void 0, void 0, function () {
            return __generator(this, function (_a) {
                switch (_a.label) {
                    case 0: return [4 /*yield*/, this.storage.get(keyName)];
                    case 1:
                        _a.sent();
                        return [2 /*return*/];
                }
            });
        });
    };
    CommonService.prototype.setlogin = function (islogin) {
        this.istologin = islogin;
    };
    CommonService.prototype.getlogin = function () {
        return this.istologin;
    };
    CommonService.prototype.getIsExtranet = function () {
        return this.IsExtranet;
    };
    CommonService.prototype.setIsExtranet = function (isExtranet) {
        this.IsExtranet = isExtranet;
    };
    CommonService.prototype.getServerIP = function () {
        var _this = this;
        return __WEBPACK_IMPORTED_MODULE_2_rxjs__["Observable"].create(function (observer) {
            _this.storage.ready().then(function () {
                _this.storage.get('ServerIP').then(function (val) {
                    if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isEmpty(val)) {
                        observer.next(__WEBPACK_IMPORTED_MODULE_4__appConstant__["a" /* APPCONSTANT */].ServerIP);
                    }
                    else {
                        observer.next(val);
                    }
                });
            });
        });
    };
    CommonService.prototype.getExtranetServerIP = function () {
        var _this = this;
        return __WEBPACK_IMPORTED_MODULE_2_rxjs__["Observable"].create(function (observer) {
            _this.storage.ready().then(function () {
                _this.storage.get('ExtranetServerIP').then(function (val) {
                    if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isEmpty(val)) {
                        observer.next(__WEBPACK_IMPORTED_MODULE_4__appConstant__["a" /* APPCONSTANT */].ExtranetServerIP);
                    }
                    else {
                        observer.next(val);
                    }
                });
            });
        });
    };
    Object.defineProperty(CommonService.prototype, "showLoading", {
        get: function () {
            return this._showLoading;
        },
        set: function (value) {
            this._showLoading = value;
        },
        enumerable: true,
        configurable: true
    });
    CommonService = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Injectable"])(),
        __metadata("design:paramtypes", [__WEBPACK_IMPORTED_MODULE_1__ionic_storage__["b" /* Storage */]])
    ], CommonService);
    return CommonService;
}());

//# sourceMappingURL=common.js.map

/***/ })

},[366]);
//# sourceMappingURL=main.js.map