<template>
	<view>
		<u-navbar leftIconColor="#fff" @leftClick="esc()" :fixed="false" :autoBack="false"
			:bgColor="navigationBarBackgroundColor" title="拆箱提交" titleStyle="color:#ffffff;font-size:21px"
			style="color:#ffffff;font-size:21px">
		</u-navbar>
		<u--form>
			<u-form-item label="目标LOC" :required="true" class="left-text-one-line" labelWidth="100">
				<u--input v-model.trim="params.locCode"></u--input>
			</u-form-item>
			<u-form-item label="新箱码" :required="true" class="left-text-one-line" labelWidth="100">
				<picker style="width: 100%;height: 100%;" v-model="dataSource" :range="isAllLpnPutawayList"
					range-key="name" value="index" @change="bindPickerChange">
					<view class="uni-input-input" style="width: 100%;">
						<u--input style="margin-top: 0rpx; z-index: 99999;" v-model.trim="dataSource"></u--input>
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
	import devanning from '@/api/stock/devanning.js'
	export default {
		components: {
			uniSelect
		},
		data() {
			return {
				navigationBarBackgroundColor: setting.customNavigationBarBackgroundColor,
				params: {
					locCode: '',
					newBoxCode: ''
				},
				dataSource: "",
				isAllLpnPutawayList: [{
						id: 2,
						name: "是",
						isAllLpnPutaway: true
					},
					{
						id: 1,
						name: "否",
						isAllLpnPutaway: false
					},
				]
			}
		},
		onLoad: function(option) {
			var parse = JSON.parse(option.param)
			this.params = parse;
			this.dataSource = this.isAllLpnPutawayList[0].name;
			this.params.newBoxCode = this.isAllLpnPutawayList[0].isAllLpnPutaway;
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
				this.params.newBoxCode = this.isAllLpnPutawayList[e.detail.value].isAllLpnPutaway;
				console.log(this.params.newBoxCode)
			},
			submit() {
				var _this = this;
				uni.$u.throttle(function() {
					if (tool.isNotEmpty(_this.params.locCode)) {
						devanning.devanningSubmit(_this.params).then(data => {
							uni.$u.func.showToast({
								title: '拆箱成功'
							});
							_this.esc();
						})
						return;
					} else {
						uni.$u.func.showToast({
							title: '拆箱失败'
						});
					}
				}, 1000)

			},
			esc() {
				if (tool.isNotEmpty(this.params.page)) {
					uni.navigateBack({
						delta: Number(this.params.page + 1)
					});
				} else {
					uni.navigateBack({
						delta: 2
					});
				}
			}
		}
	}
</script>

<style>

</style>
