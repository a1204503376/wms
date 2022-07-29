<template>
	<view>
		<u-navbar leftIconColor="#fff" @leftClick="esc()" :fixed="false" :autoBack="false"
			:bgColor="navigationBarBackgroundColor" title="拆箱提交" titleStyle="color:#ffffff;font-size:21px"
			style="color:#ffffff;font-size:21px">
		</u-navbar>
		<u--form>
			<u-form-item label="目标LOC" :required="true" class="left-text-one-line" labelWidth="100">
				<u--input v-model="params.locCode"></u--input>
			</u-form-item>
			<u-form-item label="整箱上架" :required="true" class="left-text-one-line" labelWidth="100">
				<picker style="width: 100%;height: 100%;" v-model="dataSource" :range="isAllLpnPutawayList"
					range-key="name" value="index" @change="bindPickerChange">
					<view class="uni-input-input" style="width: 100%;">
						<u--input style="margin-top: 0rpx; z-index: 99999;" v-model="dataSource"></u--input>
					</view>
				</picker>
			</u-form-item>
		</u--form>
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
	import receive from '@/api/inStock/receiveByPcs.js'
	import uniSelect from '@/components/uni-select.vue'
	import barcodeFunc from '@/common/barcodeFunc.js'
	import tool from '@/utils/tool.js'
	export default {
		components: {
			uniSelect
		},
		data() {
			return {
				navigationBarBackgroundColor: setting.customNavigationBarBackgroundColor,
				params: {
					locCode: ''
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
		onLoad() {
		this.dataSource = this.isAllLpnPutawayList[0].name;
		this.params.isAllLpnPutaway = this.isAllLpnPutawayList[0].isAllLpnPutaway;
		console.log(this.params.isAllLpnPutaway)
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
			bindPickerChange: function(e) {
				this.index = e.detail.value
				this.dataSource = this.isAllLpnPutawayList[e.detail.value].name;
				this.params.isAllLpnPutaway = this.isAllLpnPutawayList[e.detail.value].isAllLpnPutaway;
			    console.log(this.params.isAllLpnPutaway)
			},
			submit() {
				var _this = this;
				_this.params.isSn = true;
				uni.$u.throttle(function() {
					if (tool.isNotEmpty(_this.params.locCode)) {
						console.log('拆箱成功')
						uni.$u.func.routeNavigateTo('/pages/stock/stockManage/standardMove/standardMoveSerialNumber', _this.params);
						
						return;
					}
					console.log('拆箱失败')
				}, 1000)

			},
			esc() {
				uni.$u.func.navigateBackTo(1);
			}
		}
	}
</script>

<style>

</style>
