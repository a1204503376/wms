<template>
	<view>
		<u-navbar leftIconColor="#fff" @leftClick="esc()" :fixed="false" :autoBack="false"
			:bgColor="navigationBarBackgroundColor" title="按箱上架" titleStyle="color:#ffffff;font-size:21px"
			style="color:#ffffff;font-size:21px">
		</u-navbar>
		<u--form>
			<u-form-item label="箱码" class="left-text-one-line" labelWidth="100">
				<u--input v-model.trim="params.boxCode" border="0" disabled></u--input>
			</u-form-item>
			<u-form-item label="LOC" :required="true" class="left-text-one-line" labelWidth="100">
				<u--input v-model.trim="params.locCode"></u--input>
			</u-form-item>
			<u-form-item label="总数" :required="true" class="left-text-one-line" labelWidth="100">
				<u--input v-model.trim="params.qty" border="0" disabled></u--input>
			</u-form-item>
			<u-form-item label="整托上架" :required="true" class="left-text-one-line" labelWidth="100">
				<picker style="width: 100%;height: 100%;" v-model="dataSource" :range="isAllLpnPutawayList"
					range-key="name" value="index" @change="bindPickerChange">
					<view class="uni-input-input" style="width: 100%;">
						<u--input style="margin-top: 0rpx; z-index: 99999;" v-model.trim="dataSource" disabled>
						</u--input>
					</view>
				</picker>
			</u-form-item>
		</u--form>
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
	import putawayByBoxs from '@/api/inStock/putawayByBox.js'
	import barcodeFunc from '@/utils/barcodeFunc.js'
	import tool from '@/utils/tool.js'
	import keyboardListener from '@/components/keyboard-listener/keyboard-listener'
	export default {
		components: {
			keyboardListener
		},
		data() {
			return {
				navigationBarBackgroundColor: setting.customNavigationBarBackgroundColor,
				params: {
					boxCode: '',
					locCode: '',
					qty: '',
					isAllLpnPutaway: undefined
				},
				dataSource: "",
				isAllLpnPutawayList: [{
						id: 1,
						name: "否",
						isAllLpnPutaway: false
					},
					{
						id: 2,
						name: "是",
						isAllLpnPutaway: true
					},
				]
			}
		},
		onLoad: function(option) {
			var parse = JSON.parse(option.param)
			this.params.boxCode = parse.boxCode;
			this.params.stockId = parse.stockId;
			this.params.qty = parse.qty;
			this.dataSource = this.isAllLpnPutawayList[1].name;
			this.params.isAllLpnPutaway = this.isAllLpnPutawayList[1].isAllLpnPutaway;
		},
		onUnload() {
			uni.$u.func.unRegisterScanner();
		},
		onShow() {
			uni.$u.func.registerScanner(this.scannerCallback);
			var that = this;
			that.emitKeyDown = function(e) {
				if (e.key == 'Enter') {
					that.analysisCode(that.params.locCode);
					//查询方法
				}
			};
		},
		methods: {
			bindPickerChange: function(e) {
				this.index = e.detail.value
				this.dataSource = this.isAllLpnPutawayList[e.detail.value].name;
				this.params.isAllLpnPutaway = this.isAllLpnPutawayList[e.detail.value].isAllLpnPutaway;
			},
			analysisCode(code) {
				var barcode = barcodeFunc.parseBarcode(code);
				var barcodeType = barcodeFunc.BarcodeType;
				switch (barcode.type) {
					case barcodeType.UnKnow:
						this.params.locCode = barcode.content;
						break;
					case barcodeType.Loc:
						this.params.locCode = barcode.content;
						break;
					default:
						this.$u.func.showToast({
							title: '条码识别失败,不支持的条码类型'
						});
						break;
				}
			},
			submit() {
				var _this = this;
				uni.$u.throttle(function() {
					if (tool.isNotEmpty(_this.params.locCode)) {
						_this.submitPutawayByBox();
					} else {
						_this.$u.func.showToast({
							title: '请正确输入数据'
						});
					}
				}, 10000)

			},
			submitPutawayByBox() {
				this.params.whId = uni.getStorageSync('warehouse').whId
				putawayByBoxs.submitPutawayByBox(this.params).then(data => {
					this.$u.func.showToast({
						title: '上架成功'
					});
					this.esc();
				})
			},
			esc() {
				this.clearEmitKeyDown();
				uni.navigateBack({
					delta: 1
				});
			},
			scannerCallback(no) {
				this.analysisCode(no);
			},
			clearEmitKeyDown() {
				this.emitKeyDown = function() {};
			},
			emitKeyDown(e) {
				if (e.key == 'Enter') {
					this.analysisCode(this.params.locCode);
					//查询方法
				}
			}
		}
	}
</script>

<style>

</style>
