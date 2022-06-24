<template>
	<view>
		<template>
			<u-search placeholder="请输入收货单编码/上游编码" v-model="params.no" :show-action="false" @custom="search"
				@search="search" class="font-in-page">
			</u-search>
		</template>
		<u-list style="height: 990rpx;" pagingEnabled="true" @scrolltolower="scrolltolower">
			<u-divider text=""></u-divider>
			<u-list-item v-for="(item, index) in receiveList" :key="item.receiveNo">
				<view @click="clickItem(item)">
					<u-row customStyle="margin-bottom: 10px">
						<u-col span="6" class="left-text-one-line font-in-page">
							<u--text class="demo-layout bg-purple-light" v-text="item.receiveNo"></u--text>
						</u-col>
						<u-col span="6" >
							<u--text class="demo-layout bg-purple  font-in-page" v-text="item.billTypeName"></u--text>
						</u-col>
					</u-row>
					<u-row customStyle="margin-bottom: 10px">
						<u-col span="12" class="left-text-one-line">
							<u--text class="demo-layout bg-purple font-in-page" v-text="item.supplierName"></u--text>
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
		</view>
	</view>
</template>

<script>
	import receive from '@/api/receiveByPcs.js'
	import keyboardListener from '@/components/keyboard-listener/keyboard-listener'
	import debounce from '@/utils/debounce/debounce.js'
	import BarCodeService from '@/common/BarCodeService.js'
	export default {
		components: {
			keyboardListener
		},
		data() {
			return {
				params: {
					no: '',
				},
				receiveList: [],
				page: {
					total: 0,
					size: 7,
					current: 1,
					ascs: "", //正序字段集合
					descs: "", //倒序字段集合
				},
			}
		},
		onUnload() {
			uni.$u.func.unRegisterScanner();
		},
		onShow() {
			uni.$u.func.registerScanner(this.scannerCallback);
			// UnKnow: 0, // 未知类型
			// Loc: 10, // 库位
			// Lpn: 20, // 容器
			// Sku: 30, // 物品
			// Serial: 40, // 序列号
			// LotNumber: 50, // 批次号
			var Barcode = BarCodeService.parseBarcode('111:10086');
			
			console.log(BarCodeService.BarcodeType);
		},
		methods: {
			esc() {
				this.$u.func.navigateBack();
			},
			search:debounce(function () {
				this.page.current=1;
				receive.getReceiveList(this.params, this.page).then(data => {
					this.receiveList = data.data.records;
				})
			},500),
			clickItem(item) {
				uni.$u.func.route('/pages/inStock/receiveByPcs/receiptDetailEnquiry', item);
			},
			scannerCallback(no) {
				console.log(BarCodeService.parseBarcode('loc:10086'));
				this.params.no = no;
				this.search();
			},
			emitKeyDown(e) {
				if (e.key == 'Enter') {
					this.search();
				}
			},
			scrolltolower() {
				this.page.current++;
				receive.getReceiveList(this.params, this.page).then(data => {
					data.data.records.forEach((item, index) => { //js遍历数组
						this.receiveList.push(item) //push() 方法可向数组的末尾添加一个或多个元素，并返回新的长度。
					});


				})
			}
		}
	}
</script>

<style>

</style>
