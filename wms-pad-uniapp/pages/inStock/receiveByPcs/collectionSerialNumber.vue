<template>
	<view>
		<u--form>
			<u-form-item label="物品" class="left-text-one-line" labelWidth="100">
				<u--input v-model="params.skuCode" border="0" disabled></u--input>
			</u-form-item>
			<u-form-item label="名称" class="left-text-one-line" labelWidth="100">
				<u--input v-model="params.skuName" border="0" disabled></u--input>
			</u-form-item>
			<u-form-item label="序列号" class="left-text-one-line" labelWidth="100">
				<u--input v-model="params.serialNumber"></u--input>
			</u-form-item>
		</u--form>
		<h4 align="center" style='background-color:#33cbcc;height: 70rpx;' class="font-in-page">
			序列号列表({{serialNumberList.length}}/{{serialNumberList.length}})</h4>
		<u-divider text=""></u-divider>
		<!-- ${index + 1} -->
		<u-list style="height: 650rpx;">
			<u-list-item v-for="(item, index) in serialNumberList" :key="index">
				<view @click="clickItem(item)">
					<u-row customStyle="margin-bottom: 10px">
						<u-col span="10" class="left-text-one-line">
							<view class="demo-layout bg-purple-light font-in-page">{{item}}</view>
						</u-col>
						<u-col span="2">
							<view class="demo-layout bg-purple font-in-page" @click="remove(index)">删除</view>
						</u-col>
					</u-row>
					<u-divider text=""></u-divider>
				</view>
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
	import keyboardListener from '@/components/keyboard-listener/keyboard-listener'
	import barcodeFunc from '@/common/barcodeFunc.js'
	export default {
		components: {
			keyboardListener
		},
		data() {
			return {
				params: {},
				serialNumberList: []
			}
		},
		onLoad: function(option) {
			var parse = JSON.parse(option.param)
			this.receiveDetailId = parse.receiveDetailId;
			this.params = parse;
		},
		onUnload() {
			uni.$u.func.unRegisterScanner();
		},
		onShow() {
			uni.$u.func.registerScanner(this.scannerCallback);
		},
		methods: {
			remove(index) {
				this.serialNumberList.splice(index, 1)
			},
			esc() {
				this.$u.func.navigateBack();
			},
			submit() {
				var _this=this;
				uni.$u.throttle(function() {
				_this.params.serialNumberList=_this.serialNumberList;
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
				let isExist = this.serialNumberList.findIndex(item => item == barcode.content);
				if (isExist >= 0) {
					this.$u.func.showToast({
						title: '序列号已存在'
					});
					return;
				}
				this.params.serialNumber = barcode.content;
				this.serialNumberList.push(barcode.content);
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

<style>

</style>
