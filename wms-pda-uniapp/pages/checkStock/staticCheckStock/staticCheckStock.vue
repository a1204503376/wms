<template>
	<view>
		<u-navbar leftIconColor="#fff" @leftClick="esc" :fixed="false" :autoBack="false"
			:bgColor="navigationBarBackgroundColor" title="静态盘点" titleStyle="color:#ffffff;font-size:21px"
			style="color:#ffffff;font-size:21px">
		</u-navbar>
		<template>
			<u-search placeholder="盘点单号" v-model.trim="params.countBillNo" :show-action="false" @custom="search"
				@search="search" class="font-in-page" style="margin: 12rpx">
			</u-search>
		</template>
		<u-divider text="" style="margin-top:0rpx;"></u-divider>
		<u-divider text="暂无数据" v-if="noData"></u-divider>
		<u-list style="height: 950rpx;">
			<view v-for="(item, index) in receiveList" :key="index" @click="clickItem(item)">
				<u-row customStyle="margin-bottom: 10px">
					<u-col span="3" class="left-text-one-line font-in-page">
						<u--text class="demo-layout bg-purple-light" v-text="'盘点单号:'"></u--text>
					</u-col>
					<u-col span="9">
						<u--text class="demo-layout bg-purple  font-in-page" v-text="item.countBillNo"></u--text>
					</u-col>
				</u-row>
				<u-row customStyle="margin-bottom: 10px">
					<u-col span="3" class="left-text-one-line font-in-page">
						<u--text class="demo-layout bg-purple-light" v-text="'建单人:'"></u--text>
					</u-col>
					<u-col span="9">
						<u--text class="demo-layout bg-purple  font-in-page" v-text="item.creator"></u--text>
					</u-col>
				</u-row>
				<u-row customStyle="margin-bottom: 10px">
					<u-col span="3" class="left-text-one-line font-in-page">
						<u--text class="demo-layout bg-purple-light" v-text="'创建时间:'"></u--text>
					</u-col>
					<u-col span="9">
						<u--text class="demo-layout bg-purple  font-in-page" v-text="item.createTime"></u--text>
					</u-col>
				</u-row>
				<u-divider text=""></u-divider>
			</view>
			<u-loadmore :status="status" v-if="loadmore" />
		</u-list>
		<view class="footer">
			<view class="btn-cancle" @click="esc()">
				返回
			</view>
		</view>
	</view>
</template>

<script>
	import setting from '@/common/setting'
	import staticCheckStock from '@/api/checkStock/staticCheckStock.js'
	import barcodeFunc from '@/common/barcodeFunc.js'
	import tool from '@/utils/tool.js'
	export default {
		data() {
			return {
				navigationBarBackgroundColor: setting.customNavigationBarBackgroundColor,
				params: {
					countBillNo: '',
					type: ''
				},
				receiveList: [],
				page: {
					total: 0,
					size: 7,
					current: 1,
					ascs: "", //正序字段集合
					descs: "", //倒序字段集合
				},
				status: 'loadmore',
				loadmore: false,
				noData: false,
			}
		},
		onUnload() {
			uni.$u.func.unRegisterScanner();
		},
		onShow() {
			uni.$u.func.registerScanner(this.scannerCallback);
			if (tool.isNotEmpty(this.params.countBillNo)) {
				this.getReceiveList();
			}
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
				this.params.type = '';
				var barcode = barcodeFunc.parseBarcode(code);
				var barcodeType = barcodeFunc.BarcodeType;
				switch (barcode.type) {
					case barcodeType.UnKnow:
						this.params.countBillNo = barcode.content;
						break;
					case barcodeType.Loc:
						this.params.countBillNo = barcode.content;
						this.params.type = "loc_code";
						break;
					case barcodeType.Lpn:
						this.params.countBillNo = barcode.content;
						this.params.type = "lpn_code";
						break;
					case barcodeType.Sku:
						this.params.countBillNo = barcode.content;
						this.params.type = "sku_code";
						break;
					case barcodeType.LotNumber:
						this.params.countBillNo = barcode.content;
						this.params.type = "sku_lot1";
						break;
					case barcodeType.Box:
						this.params.countBillNo = barcode.content;
						this.params.type = "box_code";
						break;
					default:
						this.$u.func.showToast({
							title: '条码识别失败,不支持的条码类型'
						});
						break;
				}
			},
			esc() {
				uni.navigateBack({
					delta: 1
				});
			},
			getReceiveList() {
				this.noData = false;
				this.receiveList = [];
				this.loadmore = true;
				this.status = 'loading';
				this.page.current = 1;
				this.analysisCode(this.params.countBillNo);
				this.params.whId = uni.getStorageSync('warehouse').whId;
				staticCheckStock.getPdaStockCountResponseList(this.params, this.page).then(data => {
					if (data.data.length > 0) {
						this.status = 'loading';
						this.loadmore = true;
						this.noData = false;
					} else {
						this.loadmore = false;
						this.noData = true;
					}
					this.receiveList = data.data;
					if (this.receiveList.length < 7) {
						this.loadmore = false;
					}
				})
			},
			search() {
				uni.$u.throttle(this.getReceiveList(), 1000)
			},
			clickItem(item) {
				uni.$u.func.routeNavigateTo('/pages/checkStock/staticCheckStock/startCheckStock', item);
			},
			scannerCallback(countBillNo) {
				this.analysisCode(countBillNo);
				this.search();
			},
			// scrolltolower() {
			// 	this.loading = false;
			// 	this.divider = false;
			// 	this.page.current++;
			// 	this.params.whId = uni.getStorageSync('warehouse').whId;
			// 	staticCheckStock.getPdaStockCountResponseList(this.params, this.page).then(data => {
			// 		if (data.data.length > 0) {
			// 			this.status = 'loading';
			// 			data.data.forEach((item, index) => { //js遍历数组
			// 				this.receiveList.push(item) //push() 方法可向数组的末尾添加一个或多个元素，并返回新的长度。
			// 			});
			// 		} else {
			// 			this.status = 'nomore';
			// 		}

			// 	})
			// }
		}
	}
</script>

<style>

</style>
