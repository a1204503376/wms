<template>
	<view>
		<u-navbar leftIconColor="#fff" @leftClick="esc" :fixed="false" :autoBack="false"
			:bgColor="navigationBarBackgroundColor" title="零散拣货" titleStyle="color:#ffffff;font-size:21px"
			style="color:#ffffff;font-size:21px">
		</u-navbar>
		<u--form>
			<u-form-item label="序列号" :required="true" class="left-text-one-line" labelWidth="100">
				<u--input v-model.trim="params.serialNumber"></u--input>
			</u-form-item>
		</u--form>
		<h4 align="center" style='background-color:#33cbcc;height: 70rpx;' class="font-in-page">
			序列号列表({{serialNumberList.length}}/{{params.qty}})</h4>
		<!-- ${index + 1} -->
		<u-list style="height: 650rpx;">
			<u-list-item v-for="(item, index) in serialNumberList" :key="index" :style="item.backgroundColor">
				<u-swipe-action>
					<u-swipe-action-item :options="buttenName" @click="remove(index)">
						<view class="swipe-action u-border-top u-border-bottom" :style="item.backgroundColor">
							<view class="swipe-action__content">
								<text class="swipe-action__content__text font-in-page">{{item.serialNumber}}</text>
							</view>
						</view>
					</u-swipe-action-item>
				</u-swipe-action>
			</u-list-item>
		</u-list>
		<keyboard-listener @keydown="emitKeyDown"></keyboard-listener>
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
	import setting from '@/common/setting'
	import keyboardListener from '@/components/keyboard-listener/keyboard-listener'
	import barcodeFunc from '@/utils/barcodeFunc.js'
	import pick from '@/api/picking/picking.js'
	import tool from '@/utils/tool.js'
	export default {
		components: {
			keyboardListener
		},
		data() {
			return {
				navigationBarBackgroundColor: setting.customNavigationBarBackgroundColor,
				buttenName: [{
					text: '删除'
				}],
				params: {},
				serialNumberList: [],
				serialNumberLists: ['1', '11', '111', '1111', '001', '11111', '111111'],
			}
		},
		onLoad: function(option) {
			var parse = JSON.parse(option.param)
			this.params = parse;
		},
		onUnload() {
			uni.$u.func.unRegisterScanner();
		},
		onShow() {
			uni.$u.func.registerScanner(this.scannerCallback);
			var that = this;
			that.emitKeyDown = function(e) {
				if (e.key == 'Enter') {
					that.analysisCode(that.params.serialNumber);
				}
			};
		},
		onBackPress(event) {
			// #ifdef APP-PLUS
			if (event.from === 'backbutton') {
				this.esc();
				return true;
			}
			// #endif
		},
		methods: {
			remove(index) {
				this.serialNumberList.splice(index, 1)
			},
			esc() {
				this.clearEmitKeyDown();
				uni.navigateBack({
					delta: 1
				});
			},
			submit() {
				console.log(this.serialNumberList.length)
				console.log(this.params.qty)
				if (this.serialNumberList.length != this.params.qty){
					this.$u.func.showToast({
						title: '拣货失败，请输入对应数量的序列号'
					});
					return;
				}
				
				var _this = this;
				uni.$u.throttle(function() {
					var serialList = [];
					_this.serialNumberList.forEach((serialNumbers, index) => {
						serialList.push(serialNumbers.serialNumber)
					})
					let params = {
						serialNumberList: serialList
					}
					var serialList = [];
					_this.serialNumberList.forEach((serialNumbers, index) => {
						serialList.push(serialNumbers.serialNumber)
					})
					_this.params.serailList = serialList;
					_this.params.whCode = uni.getStorageSync('warehouse').whCode;
					_this.params.whId = uni.getStorageSync('warehouse').whId;
					pick.bulkPick(_this.params).then(data => {
						_this.$u.func.showToast({
							title: '拣货完成'
						});
						_this.clearEmitKeyDown();
						uni.navigateBack({
							delta: 2
						});
					});
				}, 10000)
			},
			analysisCode(code) {
				var barcode = barcodeFunc.parseBarcode(code);
				var barcodeType = barcodeFunc.BarcodeType;
				switch (barcode.type) {
					case barcodeType.UnKnow:
						this.listRepeated(barcode);
						break;
					case barcodeType.Serial:
						this.listRepeated(barcode);
						break;
					default:
						this.$u.func.showToast({
							title: '条码识别失败,不支持的条码类型'
						});
						break;
				}
			},
			listRepeated(barcode) {
				let isExist = this.serialNumberList.findIndex(item => item.serialNumber == barcode.content);
				if (isExist >= 0) {
					this.$u.func.showToast({
						title: '序列号已存在'
					});
					return;
				}
				if (this.serialNumberList.length == this.params.surplusQty) {
					this.$u.func.showToast({
						title: '序列号已收集完成,请提交数据'
					});
					return;
				}
				this.params.serialNumber = barcode.content;
				this.serialNumberList.push({
					serialNumber: barcode.content,
					backgroundColor: "background-color: #fff;"
				});
			},
			clearEmitKeyDown() {
				this.emitKeyDown = function() {};
			},
			emitKeyDown(e) {
				if (e.key == 'Enter') {
					this.analysisCode(this.params.serialNumber);
				}
			},
			scannerCallback(no) {
				this.analysisCode(no);
			}
		}
	}
</script>

<style lang="scss">
</style>
