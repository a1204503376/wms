cordova.define("cordova-plugin-barcodeplugin.BarCodePlugin", function(require, exports, module) {
var exec = require('cordova/exec');

var barCodePlugin = {
    setReceiveScanCallback:function(callback){
        exec(callback, function (err) {
        }, "BarCodePlugin", "setReceiveScanCallback", []);
    }
}
		
module.exports = barCodePlugin;

});
