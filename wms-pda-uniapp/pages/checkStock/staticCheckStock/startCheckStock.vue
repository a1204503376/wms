<template>
	<view>
		<u-navbar leftIconColor="#fff" @leftClick="esc" :fixed="false" :autoBack="false"
			:bgColor="navigationBarBackgroundColor" :title="title" titleStyle="color:#ffffff;font-size:21px"
			style="color:#ffffff;font-size:21px">
		</u-navbar>
		<h4 align="center" style='background-color:#33cbcc;height: 70rpx;' class="font-in-page">开始盘点</h4>
		<u-divider text="" style="margin-top:0rpx;"></u-divider>
		<u-divider text="暂无数据" v-if="noData"></u-divider>
		<u-list style="height: 950rpx;" @scrolltolower="scrolltolower">
			<view v-for="(item, index) in receiveList" :key="index">
				<u-row customStyle="margin-bottom: 10px">
					<u-col span="3" class="left-text-one-line font-in-page">
						<u--text class="demo-layout bg-purple-light" v-text="'库位'"></u--text>
					</u-col>
					<u-col span="5">
						<u--text class="demo-layout bg-purple  font-in-page" v-text="item.locCode"></u--text>
					</u-col>
					<u-col span="2">
						<view>
							<u-button type="success" :plain="true" text="盘点" @click="clickItem(item)"></u-button>
						</view>
					</u-col>
				</u-row>
				<u-row customStyle="margin-bottom: 10px">
					<u-col span="3" class="left-text-one-line font-in-page">
						<u--text class="demo-layout bg-purple-light" v-text="'仓管'"></u--text>
					</u-col>
					<u-col span="7">
						<u--text class="demo-layout bg-purple  font-in-page" v-text="item.userName"></u--text>
					</u-col>
				</u-row>
				<view v-for="(res, indes) in item.pdaBoxQtyResponseList" :key="indes">
					<u-row customStyle="margin-bottom: 10px">
						<u-col span="3" class="left-text-one-line font-in-page">
							<u--text class="demo-layout bg-purple-light" v-text="'箱号'" v-if="indes==0"></u--text>
						</u-col>
						<u-col span="9">
							<u--text class="demo-layout bg-purple  font-in-page"
								v-text="res.boxCode+' ('+res.totalQty+')'"></u--text>
						</u-col>
					</u-row>
				</view>
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
					countBillId: ''
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
				title: '',
			}
		},
		onLoad: function(option) {
			var parse = JSON.parse(option.param);
			this.title = parse.countBillNo;
			this.params.countBillId = parse.countBillId;
			this.getReceiveList();
		},
		onUnload() {
			uni.$u.func.unRegisterScanner();
		},
		onShow() {
			uni.$u.func.registerScanner(this.scannerCallback);
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
						this.params.no = barcode.content;
						break;
					case barcodeType.Loc:
						this.params.no = barcode.content;
						this.params.type = "loc_code";
						break;
					case barcodeType.Lpn:
						this.params.no = barcode.content;
						this.params.type = "lpn_code";
						break;
					case barcodeType.Sku:
						this.params.no = barcode.content;
						this.params.type = "sku_code";
						break;
					case barcodeType.LotNumber:
						this.params.no = barcode.content;
						this.params.type = "sku_lot1";
						break;
					case barcodeType.Box:
						this.params.no = barcode.content;
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
				uni.$u.func.navigateBackTo(1);
			},
			getReceiveList() {
				this.noData = false;
				this.receiveList = [];
				this.loadmore = true;
				this.status = 'loading';
				this.page.current = 1;
				this.params.whId = uni.getStorageSync('warehouse').whId;
				staticCheckStock.getPdaStockCountDetailResponseList(this.params, this.page).then(data => {
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
				if (item.isPickLocation) {
					uni.$u.func.routeNavigateTo('/pages/checkStock/staticCheckStock/artificialLocation', item);
				} else {
					uni.$u.func.routeNavigateTo('/pages/checkStock/staticCheckStock/autoLocation', item);
				}

			},
			scannerCallback(no) {
				this.analysisCode(no);
				this.search();
			},
			scrolltolower() {
				this.loading = false;
				this.divider = false;
				this.page.current++;
				this.params.whId = uni.getStorageSync('warehouse').whId;
				staticCheckStock.getPdaStockCountDetailResponseList(this.params, this.page).then(data => {
					if (data.data.length > 0) {
						this.status = 'loading';
						data.data.forEach((item, index) => { //js遍历数组
							this.receiveList.push(item) //push() 方法可向数组的末尾添加一个或多个元素，并返回新的长度。
						});
					} else {
						this.status = 'nomore';
					}

				})
			}
		}
	}
</script>

<style>

</style>
