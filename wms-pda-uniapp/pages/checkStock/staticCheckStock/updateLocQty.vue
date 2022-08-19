<template>
	<view>
		<u-navbar leftIconColor="#fff" @leftClick="esc" :fixed="false" :autoBack="false"
			:bgColor="navigationBarBackgroundColor" title="修改差异数量" titleStyle="color:#ffffff;font-size:21px"
			style="color:#ffffff;font-size:21px">
		</u-navbar>
		<u-divider text="" style="margin-top:0rpx;"></u-divider>
		<u-divider text="暂无数据" v-if="noData"></u-divider>
		<u-modal :show="show" :title="title" :content='content' cancelText='取消' :closeOnClickOverlay="true"
			:showCancelButton="true" @confirm="confirm()" @cancel="close()" @close="close()"></u-modal>
		<u-list style="height: 950rpx;" @scrolltolower="scrolltolower">
			<view v-for="(item, index) in receiveList" :key="index">
				<u-row customStyle="margin-bottom: 10px">
					<u-col span="4" class="left-text-one-line font-in-page">
						<u--text class="demo-layout bg-purple-light" v-text="'箱号'"></u--text>
					</u-col>
					<u-col span="8" class="font-in-page">
						<u-input v-model="item.boxCode"></u-input>
					</u-col>
				</u-row>
				<u-row customStyle="margin-bottom: 10px">
					<u-col span="4" class="left-text-one-line  font-in-page">
						<view>
							<u--text class="demo-layout bg-purple-light" v-text="'库位'"></u--text>
						</view>
					</u-col>
					<u-col span="8" class="font-in-page">
						<u-input v-model="item.locCode"></u-input>
					</u-col>
				</u-row>
				<u-row customStyle="margin-bottom: 10px">
					<u-col span="4" class="left-text-one-line font-in-page">
						<u--text class="demo-layout bg-purple-light" v-text="'物品编码'"></u--text>
					</u-col>
					<u-col span="8" class="font-in-page">
						<u-input v-model="item.skuCode" border="0" disabled></u-input>
					</u-col>
				</u-row>
				<u-row customStyle="margin-bottom: 10px">
					<u-col span="4" class="left-text-one-line font-in-page">
						<view>
							<u--text class="demo-layout bg-purple-light" v-text="'库存余额'"></u--text>
						</view>
					</u-col>
					<u-col span="8" class="font-in-page">
						<u-input v-model="item.stockBalance"></u-input>
					</u-col>
				</u-row>
				<u-divider text=""></u-divider>
			</view>
		</u-list>
		<view class="footer">
			<view class="btn-cancle" @click="esc()">
				返回
			</view>
			<view class="btn-cancle" @click="submit()">
				提交
			</view>
		</view>
	</view>
</template>

<script>
	import setting from '@/common/setting'
	import barcodeFunc from '@/common/barcodeFunc.js'
	import staticCheckStock from '@/api/checkStock/staticCheckStock.js'
	import tool from '@/utils/tool.js'
	export default {
		data() {
			return {
				navigationBarBackgroundColor: setting.customNavigationBarBackgroundColor,
				params: {
					no: '',
					type: '',
					stockBalance: 0,
					defaultList: [],
				},
				differDefaultList: [],
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
				title: '确认提交',
				show: false,
				content: '确认当前总数量为'
			}
		},
		onLoad: function(option) {
			uni.setStorageSync('isListAsDefault', '')
			var parse = JSON.parse(option.param);
			this.params.boxCode = parse.boxCode;
			this.params.locCode = parse.locCode;
			this.params.totalQty = parse.totalQty;
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
			confirm() {
				let isListAsDefault = this.isListAsDefaultList();
				if (tool.isEmpty(this.differDefaultList)) {
					this.esc();
				} else {
					let isListAsDefault = {};
					isListAsDefault.differDefaultList = this.receiveList;
					isListAsDefault.boxCode = this.params.boxCode;
					isListAsDefault.locCode = this.params.locCode;
					uni.setStorageSync('isListAsDefault', isListAsDefault)
					this.esc();
				}
			},
			close() {
				this.show = false;
			},
			submit() {
				this.params.stockBalance = 0;
				this.receiveList.forEach((item, index) => { //js遍历数组
					this.params.stockBalance = Number(item.stockBalance) + Number(this.params.stockBalance);
				});
				this.content = '确认当前总数量为' + this.params.stockBalance;
				this.show = true;
			},
			isListAsDefaultList() {
				for (let i = 0; i < this.receiveList.length; i++) {
					if (Number(this.receiveList[i].stockBalance) !== Number(this.params.defaultList[i].stockBalance) ||
						this.receiveList[i].boxCode !== this.params.defaultList[i].boxCode || this.receiveList[i]
						.locCode !== this.params.defaultList[i].locCode) {
						this.differDefaultList.push(this.receiveList[i]);
					}
				}
			},
			updateLocQty(row) {
				uni.$u.func.routeNavigateTo('/pages/checkStock/staticCheckStock/updateLocQty', row);
			},
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
				this.params.whId = uni.getStorageSync('warehouse').whId;
				staticCheckStock.findPdaSkuQtyResponseList(this.params, this.page).then(data => {
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
				staticCheckStock.findPdaSkuQtyResponseList(this.params, this.page).then(data => {
					if (data.data.length > 0) {
						this.status = 'loading';
						this.loadmore = true;
						this.noData = false;
					} else {
						this.loadmore = false;
						this.noData = true;
					}
					this.params.defaultList = data.data;

					if (this.receiveList.length < 7) {
						this.loadmore = false;
					}
				})

			},
			search() {
				uni.$u.throttle(this.getReceiveList(), 1000)
			},
			clickItem(item) {
				if (true) {
					uni.$u.func.routeNavigateTo('/pages/checkStock/staticCheckStock/autoLocation', item);
				} else {
					uni.$u.func.routeNavigateTo('/pages/checkStock/staticCheckStock/artificialLocation', item);
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
				staticCheckStock.findPdaSkuQtyResponseList(this.params, this.page).then(data => {
					if (data.data.length > 0) {
						this.status = 'loading';
						data.data.forEach((item, index) => { //js遍历数组
							this.receiveList.push(item) //push() 方法可向数组的末尾添加一个或多个元素，并返回新的长度。

						});
					} else {
						this.status = 'nomore';
					}

				})
				staticCheckStock.findPdaSkuQtyResponseList(this.params, this.page).then(data => {
					if (data.data.length > 0) {
						data.data.forEach((item, index) => { //js遍历数组
							this.params.defaultList.push(item)
						});
					}

				})

			}
		}
	}
</script>

<style>

</style>
