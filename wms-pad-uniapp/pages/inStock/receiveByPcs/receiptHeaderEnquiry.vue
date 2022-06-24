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
						<u-col span="6">
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
		<view class="footer">
			<view class="btn-cancle" @click="esc()">
				返回
			</view>
		</view>
	</view>
</template>

<script>
	import receive from '@/api/receiveByPcs.js'
	import barcodeFunc from '@/common/barcodeFunc.js'
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
		},
		methods: {
			analysisCode(code) {
				var barcode = barcodeFunc.parseBarcode(code);
				var barcodeType = barcodeFunc.BarcodeType;
				switch (barcode.type) {
					case barcodeType.UnKnow:
						this.params.no = barcode.content;
						return barcodeType.UnKnow;
				}
			},
			esc() {
				this.$u.func.navigateBack();
			},
			getReceiveList(){
				this.page.current = 1;
				this.analysisCode(this.params.no);
				receive.getReceiveList(this.params, this.page).then(data => {
					this.receiveList = data.data.records;
				})
			},
			search(){
				uni.$u.throttle(this.getReceiveList, 1000)
			},
			clickItem(item) {
				uni.$u.func.route('/pages/inStock/receiveByPcs/receiptDetailEnquiry', item);
			},
			scannerCallback(no) {
				this.analysisCode(no);
				this.search();
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
