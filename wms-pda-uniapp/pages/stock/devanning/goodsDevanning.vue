<template>
	<view>
		<u-navbar leftIconColor="#fff" @leftClick="esc" :fixed="false" :autoBack="false"
			:bgColor="navigationBarBackgroundColor" title="按物品拆箱" titleStyle="color:#ffffff;font-size:21px"
			style="color:#ffffff;font-size:21px">
		</u-navbar>
		<!-- 注意，如果需要兼容微信小程序，最好通过setRules方法设置rules规则 -->
		<u--form labelPosition="left" :model="paramsView">
			<u-form-item label="物品" borderBottom class="left-text-one-line font-in-page" labelWidth="100">
				<u--input v-model.trim="paramsView.skuCode"></u--input>
			</u-form-item>
			<u-form-item label="批次" borderBottom class="left-text-one-line font-in-page" labelWidth="100">
				<u--input v-model.trim="paramsView.skuLot1"></u--input>
			</u-form-item>
			<u-form-item label="余额" borderBottom class="left-text-one-line font-in-page" labelWidth="100">
				<u--input v-model.trim="paramsView.stockBalance"></u--input>
			</u-form-item>
			<u-form-item label="拆数量" borderBottom class="left-text-one-line font-in-page" labelWidth="100">
				<u--input v-model.trim="paramsView.splitQty"></u--input>
			</u-form-item>
		</u--form>
		<keyboard-listener @keydown="emitKeyDown"></keyboard-listener>
		<view class="footer">
			<view class="btn-cancle" @click="esc()">
				返回
			</view>
			<view class="btn-cancle" @click="next()">
				下一项
			</view>
		</view>
	</view>
</template>

<script>
	import setting from '@/common/setting'
	import keyboardListener from '@/components/keyboard-listener/keyboard-listener'
	import barcodeFunc from '@/utils/barcodeFunc.js'
	import tool from '@/utils/tool.js'
	import putawayByBox from '@/api/inStock/putawayByBox.js'
	export default {
		components: {
			keyboardListener
		},
		data() {
			return {
				navigationBarBackgroundColor: setting.customNavigationBarBackgroundColor,
				params: {
					boxCode: '',
					whId: uni.getStorageSync('warehouse').whId
				},
				paramsView: {},
				putawayData: {}
			}
		},
		onLoad: function(option) {
			var parse = JSON.parse(option.param)
			this.params = parse;
			if (tool.isNotEmpty(this.params.page)) {
				this.paramsView = this.params.stockList[this.params.page]
			} else {
				this.params.page = 0;
				this.paramsView = this.params.stockList[this.params.page]
			}
			console.log(this.paramsView)
		},
		onUnload() {
			uni.$u.func.unRegisterScanner();
		},
		onShow() {
			this.paramsView.splitQty=0;
			uni.$u.func.registerScanner(this.scannerCallback);
			var that = this;
			that.emitKeyDown = function(e) {
				if (e.key == 'Enter') {
					that.analysisCode(that.params.boxCode);
					//查询方法
					that.getPutawayData();
				}
			};
		},
		methods: {
			next() {
				this.params.page++;
				if (this.params.stockList.length > this.params.page) {
					this.params.stockList[this.params.page].splitQty=this.paramsView.splitQty;
					uni.$u.func.routeNavigateTo('/pages/stock/devanning/goodsDevanning', this.params);
				} else {
					this.params.isSn = false;
					uni.$u.func.routeNavigateTo('/pages/stock/devanning/devanningSubmit', this
						.params);
				}
			},
			getPutawayData() {
				if (tool.isEmpty(this.params.boxCode)) {
					uni.$u.func.showToast({
						title: '查询失败，请输入箱码后重试'
					});
				}
				this.clearEmitKeyDown();
			},
			analysisCode(code) {
				var barcode = barcodeFunc.parseBarcode(code);
				var barcodeType = barcodeFunc.BarcodeType;
				switch (barcode.type) {
					case barcodeType.UnKnow:
						this.params.boxCode = barcode.content;
						break;
					case barcodeType.Lpn:
						this.params.boxCode = barcode.content;
						break;
					default:
						this.$u.func.showToast({
							title: '条码识别失败,不支持的条码类型'
						});
						break;
				}
			},
			esc() {
				this.clearEmitKeyDown();
				uni.navigateBack({
					delta: 1
				});
			},
			scannerCallback(no) {
				this.analysisCode(no);
				//查询方法
				this.getPutawayData();
			},
			clearEmitKeyDown() {
				this.emitKeyDown = function() {};
			},
			emitKeyDown(e) {
				if (e.key == 'Enter') {
					this.analysisCode(this.params.boxCode);
					//查询方法
					this.getPutawayData();
				}
			}
		}
	}
</script>

<style>

</style>
