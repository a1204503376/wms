<template>
	<view @keyup.esc="esc">
		<u-navbar leftIconColor="#fff" @leftClick="esc()" :fixed="false" :autoBack="false"
			:bgColor="navigationBarBackgroundColor" title="接驳区拣货" titleStyle="color:#ffffff;font-size:21px"
			style="color:#ffffff;font-size:21px">
		</u-navbar>
		<u--form labelPosition="left">
			<u-form-item label="当前选择" :required="true" class="left-text-one-line" labelWidth="100">
				<u--input v-model.trim="param.locCodeView" disabled></u--input>
			</u-form-item>
		</u--form>

		<view>
			<template v-for="(item, index) in locList" style="float: left;">
				<u-button v-if="!item.stockExist" style="float: left;height: 60px;font-size: 40rpx; width: 25%;"
					@click="change(item)">
					{{item.locCodeView}}
				</u-button>
				<u-button v-if="item.stockExist"
					style="float: left;height: 60px;font-size: 40rpx; width: 25%;background-color: aquamarine;"
					@click="change(item)">
					{{item.locCodeView}}
				</u-button>
			</template>
		</view>

		<view class="footer">
			<view class="btn-cancle" @click="esc()">
				返回
			</view>
			<view class="btn-submit" @click="submit()">
				确定
			</view>
		</view>
	</view>
</template>

<script>
	import barCodeService from '@/utils/barcodeFunc.js'
	import setting from '@/common/setting'
	import pick from '@/api/picking/connectionAreaPicking.js'
	import tool from '@/utils/tool.js'
	export default {
		components: {},
		data() {
			return {
				navigationBarBackgroundColor: setting.customNavigationBarBackgroundColor,
				param: {
					locId: undefined,
					locCodeView: '',
					locCode: ''
				},
				lpnItem: '',
				locCode: '',
				locList: []
			}
		},
		onLoad: function(option) {
            this.refresh();
			uni.$u.func.registerScanner(this.scannerCallback);
		},
		onUnload() {
			uni.$u.func.unRegisterScanner();
		},
		onBackPress(event) {
			// #ifdef APP-PLUS
			if (event.from === 'backbutton') {
				this.esc();
				return true;
			}
			// #endif
		},

		onShow() {

		},
		methods: {
			refresh(){
				let params = {
					whId: uni.getStorageSync('warehouse').whId
				};
				pick.getConnectionAreaLocation(params).then(data => {
					this.locList = data.data;
				})
			},
			esc() {
				uni.navigateBack({
					delta: 1
				});
			},
			change(item) {
				this.param.locId = item.locId;
				this.param.locCodeView = item.locCodeView;
				this.param.locCode = item.locCode;
			},

			submit() {
				var _this = this;
				uni.$u.throttle(function() {
					if (tool.isNotEmpty(_this.param.locCode) && tool.isNotEmpty(_this.param.locId) && tool
						.isNotEmpty(_this.param.locCodeView)) {
						pick.connectionAreaPicking(_this.param).then(data => {
							if (data.data) {

								var isExist = _this.locList.findIndex(item => item.locCode == _this.param.locCode);
								if(isExist >= 0){
									_this.locList[isExist].stockExist = false
								}
								_this.$u.func.showToast({
									title: '接驳区拣货成功'
								})
								
							} else {
								uni.$u.func.routeNavigateTo(
									'/pages/picking/agvPickto/agvPicktoMove', _this
									.param);
							}
						})
					} else {
						_this.$u.func.showToast({
							title: '请选择你要拣货的库位'
						})
					}
				}, 10000)
			},
			scannerCallback(no) {
				this.analysisCode(no);
			},
			analysisCode(code) {
				var barcode = barCodeService.parseBarcode(code);
				var barcodeType = barCodeService.BarcodeType;
				switch (barcode.type) {
					case barcodeType.UnKnow:
						for (let i = 0; i < this.locList.length; i++) {
							if (this.locList[i].locCode == barcode.content) {
								this.param.locId = this.locList[i].locId
								this.param.locCode = this.locList[i].locCode
								this.param.locCodeView = this.locList[i].locCodeView
							}
						}
						break;
					case barcodeType.Loc:
						for (let i = 0; i < this.locList.length; i++) {
							if (this.locList[i].locCode == barcode.content) {
								this.param.locId = this.locList[i].locId
								this.param.locCode = this.locList[i].locCode
								this.param.locCodeView = this.locList[i].locCodeView
							}
						}
						break;
					default:
						this.$u.func.showToast({
							title: '条码识别失败,不支持的条码类型'
						});
						break;
				}
			},
		}
	}
</script>

<style>

</style>
