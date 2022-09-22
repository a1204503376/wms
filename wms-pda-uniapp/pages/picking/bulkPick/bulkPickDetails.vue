<template>
	<view>
		<u-navbar leftIconColor="#fff" @leftClick="esc()" :fixed="false" :autoBack="false"
			:bgColor="navigationBarBackgroundColor" title="未拣货明细" titleStyle="color:#ffffff;font-size:21px"
			style="color:#ffffff;font-size:21px">
		</u-navbar>
		<!-- ${index + 1} -->
		<u-list style="height: 960rpx;">
			<u-list-item v-for="(item, index) in receiveDetailList" :key="index">
				<view>
					<u-row customStyle="margin-bottom: 10px">
						<u-col span="4" class="left-text-one-line">
							<view class="demo-layout bg-purple-light font-in-page" style="text-align: left;">{{'行号'}}
							</view>
						</u-col>
						<u-col span="8">
							<view class="demo-layout bg-purple font-in-page"
								style="text-align: right;margin-right: 30rpx;">
								{{item.soLineNo}}
							</view>
						</u-col>
					</u-row>
					<u-row customStyle="margin-bottom: 10px">
						<u-col span="4" class="left-text-one-line">
							<view class="demo-layout bg-purple-light font-in-page" style="text-align: left;">{{'物品编码'}}
							</view>
						</u-col>
						<u-col span="8">
							<view class="demo-layout bg-purple font-in-page"
								style="text-align: right;margin-right: 30rpx;">
								{{item.skuCode}}
							</view>
						</u-col>
					</u-row>
					<u-row customStyle="margin-bottom: 10px">
						<u-col span="4" class="left-text-one-line">
							<view class="demo-layout bg-purple-light font-in-page" style="text-align: left;">{{'实际数量'}}
							</view>
						</u-col>
						<u-col span="8">
							<view class="demo-layout bg-purple font-in-page"
								style="text-align: right;margin-right: 30rpx;">{{item.scanQty}}/{{item.baseUmName}}
							</view>
						</u-col>
					</u-row>
					<u-row customStyle="margin-bottom: 10px">
						<u-col span="4" class="left-text-one-line">
							<view class="demo-layout bg-purple-light font-in-page" style="text-align: left;">{{'计划数量'}}
							</view>
						</u-col>
						<u-col span="8">
							<view class="demo-layout bg-purple font-in-page"
								style="text-align: right;margin-right: 30rpx;">{{item.planQty}}/{{item.baseUmName}}
							</view>
						</u-col>
					</u-row>
					<u-row customStyle="margin-bottom: 10px">
					</u-row>
					<view v-for="(soPick, index) in item.soPickPlanList" :key="index">
						<u-row customStyle="margin-bottom: 10px">
							<u-col span="4" class="left-text-one-line">
								<view class="demo-layout bg-purple-light font-in-page" style="text-align: left;">
									{{'库区'}}
								</view>
							</u-col>
							<u-col span="8">
								<view class="demo-layout bg-purple font-in-page"
									style="text-align: right;margin-right: 30rpx;">
									{{soPick.zoneCode}}
								</view>
							</u-col>
						</u-row>
<!-- 						<u-row customStyle="margin-bottom: 10px">
							<u-col span="4" class="left-text-one-line">
								<view class="demo-layout bg-purple-light font-in-page" style="text-align: left;">
									{{'库位'}}
								</view>
							</u-col>
							<u-col span="8">
								<view class="demo-layout bg-purple font-in-page"
									style="text-align: right;margin-right: 30rpx;">
									{{soPick.locCode}}
								</view>
							</u-col>
						</u-row> -->
						<u-row customStyle="margin-bottom: 10px">
							<u-col span="4" class="left-text-one-line">
								<view class="demo-layout bg-purple-light font-in-page" style="text-align: left;">
									{{'箱码'}}
								</view>
							</u-col>
							<u-col span="8">
								<view class="demo-layout bg-purple font-in-page"
									style="text-align: right;margin-right: 30rpx;">
									{{soPick.boxCode}}
								</view>
							</u-col>
						</u-row>
						<u-row customStyle="margin-bottom: 10px">
							<u-col span="4" class="left-text-one-line">
								<view class="demo-layout bg-purple-light font-in-page" style="text-align: left;">
									{{'剩余量'}}
								</view>
							</u-col>
							<u-col span="8">
								<view class="demo-layout bg-purple font-in-page"
									style="text-align: right;margin-right: 30rpx;">
									{{soPick.surplusQty}}
								</view>
							</u-col>
						</u-row>
					</view>
					<u-divider text=""></u-divider>
				</view>
			</u-list-item>
		</u-list>
		<keyboard-listener @keydown="emitKeyDown"></keyboard-listener>
		<view class="footer">
			<view class="btn-cancle" @click="esc()">
				返回
			</view>
		</view>
	</view>
</template>

<script>
	import setting from '@/common/setting'
	import picking from '@/api/picking/picking.js'
	import keyboardListener from '@/components/keyboard-listener/keyboard-listener'
	import barcodeFunc from '@/common/barcodeFunc.js'
	import tool from '@/utils/tool.js'
	export default {
		components: {
			keyboardListener
		},
		data() {
			return {
				navigationBarBackgroundColor: setting.customNavigationBarBackgroundColor,
				params: {
					receiveId: '',
					skuCode: '',
				},
				receiveDetailList: []
			}
		},
		onLoad: function(option) {
			var parse = JSON.parse(option.param)
			this.params = parse;
			this.getReceiveDetailList();
		},
		onUnload() {
			uni.$u.func.unRegisterScanner();
		},
		onShow() {
			uni.$u.func.registerScanner(this.scannerCallback);
			var that = this;
			that.emitKeyDown = function(e) {
				if (e.key == 'Enter') {
					this.getReceiveDetailList();
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
			analysisCode(code) {
				var barcode = barcodeFunc.parseBarcode(code);
				var barcodeType = barcodeFunc.BarcodeType;
				switch (barcode.type) {
					case barcodeType.UnKnow:
						this.params.skuCode = barcode.content;
						break;
					case barcodeType.Sku:
						this.params.skuCode = barcode.content;
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
			getReceiveDetailList() {
				this.params.whId = uni.getStorageSync('warehouse').whId;
				picking.getPickPlanBySoBillId(this.params).then(data => {
					this.receiveDetailList = data.data.records;
					console.log(data.data.records)
					//TODO
					// if (data.data.length == 1) {
					// 	data.data[0].receiveId = this.params.receiveId;
					// 	uni.$u.func.routeNavigateTo('/pages/inStock/receiveByPcs/ReceiveByPiece', data.data[0]);
					// }
				})
			},
			// clickItem(row) {
			// 	this.params.skuName = row.skuName;
			// 	this.params.baseUmName = row.baseUmName;
			// 	this.params.planQty = row.planQty;
			// 	this.params.scanQty = row.scanQty;
			// 	this.clearEmitKeyDown();
			// 	uni.setStorageSync('soDetail', row);
			// 	this.esc();
			// },
			scannerCallback(no) {
				this.analysisCode(no);
				this.getReceiveDetailList();
			},
			clearEmitKeyDown() {
				this.emitKeyDown = function() {};
			},
			emitKeyDown(e) {
				if (e.key == 'Enter') {
					this.getReceiveDetailList();
				}
			}
		}
	}
</script>

<style>

</style>
