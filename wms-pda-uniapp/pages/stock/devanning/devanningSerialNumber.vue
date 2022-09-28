<template>
	<view>
		<u-navbar leftIconColor="#fff" @leftClick="esc" :fixed="false" :autoBack="false"
			:bgColor="navigationBarBackgroundColor" title="按序列号拆箱" titleStyle="color:#ffffff;font-size:21px"
			style="color:#ffffff;font-size:21px">
		</u-navbar>
		<u--form>
			<u-form-item label="序列号" class="left-text-one-line" labelWidth="100">
				<u--input v-model.trim="params.serialNumber"></u--input>
			</u-form-item>
		</u--form>
		<h4 align="center" style='background-color:#33cbcc;height: 70rpx;' class="font-in-page">
			序列号列表({{serialNumberList.length}})</h4>
		<!-- ${index + 1} -->
		<u-list style="height: 650rpx;">
			<u-list-item v-for="(item, index) in serialNumberList" :key="index" :style="item.backgroundColor">
				<!-- 	<u-cell :title="item.serialNumber">
					<u-row customStyle="margin-bottom: 10px">
						<u-col span="10" class="left-text-one-line">
							<view class="demo-layout bg-purple-light font-in-page">{{item.serialNumber}}</view>
						</u-col>
						<u-col span="2">
							<view class="demo-layout bg-purple font-in-page" @click="remove(index)">删除</view>
						</u-col>
					</u-row>
				</u-cell> -->
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
	import receive from '@/api/inStock/receiveByPcs.js'
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
				serialNumberLists: [],
				receiveDetailId: '',
				receiveId: ''
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
				var _this = this;
				uni.$u.throttle(function() {
					if (_this.serialNumberList.length > 0) {
						let serialNumberList = []
						_this.serialNumberList.forEach(item => {
							serialNumberList.push(item.serialNumber)
						})
						var params = {};
						params = _this.params;
						params.serialNumberList = serialNumberList;
						params.isSn = true;
						uni.$u.func.routeNavigateTo('/pages/stock/devanning/devanningSubmit', params);
					} else {
						_this.$u.func.showToast({
							title: '请至少采集一个序列号'
						});
					}

				}, 1000)
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
				let isExists = this.params.serialNumberList.findIndex(item => item == barcode.content);
				if (isExists >= 0) {
					this.params.serialNumber = barcode.content;
					this.serialNumberList.push({
						serialNumber: barcode.content,
						backgroundColor: "background-color: #fff;"
					});
					return;
				} else {
					this.$u.func.showToast({
						title: '此托盘上不存在该序列号'
					});
				}

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
