<template>
	<view>
		<u-navbar leftIconColor="#fff" @leftClick="esc" :fixed="false" :autoBack="false"
			:bgColor="navigationBarBackgroundColor" title="差异处理(自动库区)" titleStyle="color:#ffffff;font-size:21px"
			style="color:#ffffff;font-size:21px">
		</u-navbar>
		<u-divider text="" style="margin-top:0rpx;"></u-divider>
		<u-divider text="暂无数据" v-if="noData"></u-divider>
			<u-row customStyle="margin-bottom: 10px">
				<u-col span="2" class="left-text-one-line font-in-page">
					<u--text class="demo-layout bg-purple-light" v-text="'箱号'"></u--text>
				</u-col>
				<u-col span="10">
					<u-input v-model="params.boxCode"></u-input>
				</u-col>
			</u-row>
			<u-row>
				<u-col span="2" class="left-text-one-line font-in-page">
					<view>
						<u--text class="demo-layout bg-purple-light" v-text="'库位'"></u--text>
					</view>
				</u-col>
				<u-col span="10">
					<u-input v-model="params.locCode"></u-input>
				</u-col>
			</u-row>
			<view v-for="(item, index) in receiveList.pdaBoxQtyResponseList" :key="index" :style="item.backgroundColor">
				<u-row customStyle="margin-bottom: 10px">
					<u-col span="1" class="left-text-one-line font-in-page">
						<u-icon name="checkbox-mark" color="green" v-if="item.isValid"></u-icon>
					</u-col>
					<u-col span="7">
						<u--text class="left-text-one-line  demo-layout bg-purple  font-in-page"
							v-text="item.boxCode+' ('+item.totalQty+')'"></u--text>
					</u-col>
					<u-col span="2">
						<u-button type="error" :plain="true" text="差异" @click="updateStatesIsDiff(item,'error')">
						</u-button>
					</u-col>
					<u-col span="2">
						<u-button type="success" :plain="true" text="无误" @click="updateStatesIsDiff(item,'success')">
						</u-button>
					</u-col>
				</u-row>
				<u-divider text=""></u-divider>
			</view>
			<!-- 	<u-loadmore :status="status" v-if="loadmore" /> -->
		<view class="footer">
			<view class="btn-cancle" @click="esc()">
				返回
			</view>
			<view class="btn-cancle" @click="esc()">
				提交
			</view>
		</view>
	</view>
</template>

<script>
	import setting from '@/common/setting'
	import stockInquiry from '@/api/stock/stockInquiry.js'
	import barcodeFunc from '@/common/barcodeFunc.js'
	import tool from '@/utils/tool.js'
	export default {
		data() {
			return {
				navigationBarBackgroundColor: setting.customNavigationBarBackgroundColor,
				params: {
					boxCode: '',
					locCode: ''
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
				title: '开始盘点',
			}
		},
		onLoad: function(option) {
			var parse = JSON.parse(option.param);
			this.receiveList = parse;
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
			updateStatesIsDiff(item,bool){
				if(bool == 'success'){
					item.isValid=true;
				}else{
					item.isValid=false;
				}
				console.log(item)
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
				uni.$u.func.navigateBackTo(1);
			},
			getReceiveList() {
				this.noData = false;
				this.receiveList = [];
				this.loadmore = true;
				this.status = 'loading';
				this.page.current = 1;
				this.analysisCode(this.params.no);
				this.params.whId = uni.getStorageSync('warehouse').whId;
				stockInquiry.findAllStockByNo(this.params, this.page).then(data => {
					if (data.data.records.length > 0) {
						this.status = 'loading';
						this.loadmore = true;
						this.noData = false;
					} else {
						this.loadmore = false;
						this.noData = true;
					}
					this.receiveList = data.data.records;
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
				stockInquiry.findAllStockByNo(this.params, this.page).then(data => {
					if (data.data.records.length > 0) {
						this.status = 'loading';
						data.data.records.forEach((item, index) => { //js遍历数组
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
