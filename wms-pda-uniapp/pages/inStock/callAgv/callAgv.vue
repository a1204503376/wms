<template>
	<view @keyup.esc="esc">
		<u-navbar leftIconColor="#fff" @leftClick="esc()" :fixed="false" :autoBack="false"
			:bgColor="navigationBarBackgroundColor" title="呼叫AGV" titleStyle="color:#ffffff;font-size:21px"
			style="color:#ffffff;font-size:21px">
		</u-navbar>
		<!-- 注意，如果需要兼容微信小程序，最好通过setRules方法设置rules规则 -->
		<u--form labelPosition="left">
			<u-form-item label="接驳库位" class="left-text-one-line" labelWidth="100">
				<u--input v-model.trim="locCode" readonly></u--input>
			</u-form-item>
		</u--form>
		<h4 align="center" style='background-color:#33cbcc;height: 70rpx;' class="font-in-page" v-if="param.lpnType != 'C'">库位信息</h4>
		<h4 align="center" style='background-color:#33cbcc;height: 70rpx;' class="font-in-page" v-if="param.lpnType == 'C'">C1箱库位信息</h4>

		<view style="margin-top: 5%;">
			<u-row  v-if="param.lpnType != 'C'">
				<template v-for="(item, index) in locList">
					<u-col span="3" v-if="item.isEmpty">
						<u-button style="height: 60px;width: 95%;font-size: 40rpx;" @click="change(item)">
							{{item.locCodeView}}
						</u-button>
					</u-col>
					<u-col span="3" v-if="!item.isEmpty">
						<u-button style="height: 60px;width: 95%;font-size: 40rpx;" disabled>{{item.locCodeView}}
						</u-button>
					</u-col>
				</template>
			</u-row>
			<u-row  v-if="param.lpnType == 'C'" >
				<template v-for="(item, index) in locList">
					<u-col span="3" v-if="item.isEmpty  && (item.locCodeView == '34-01' || item.locCodeView == '33-01')">
						<u-button style="height: 60px;width: 95%;font-size: 40rpx;" @click="change(item)">
							{{item.locCodeView}}
						</u-button>
					</u-col>
					<u-col span="3" v-if="!item.isEmpty && (item.locCodeView == '34-01' || item.locCodeView == '33-01')" >
						<u-button style="height: 60px;width: 95%;font-size: 40rpx;" disabled>{{item.locCodeView}}
						</u-button>
					</u-col>
				</template>
			</u-row>
			<h4 align="center" style='background-color:#33cbcc;height: 70rpx;' class="font-in-page" v-if="param.lpnType == 'C'">C2箱库位信息</h4>
			<u-row v-if="param.lpnType != 'C'">
				<template v-for="(item, index) in locList">
					<u-col span="3" v-if="item.isEmpty && index>3">
						<u-button style="height: 60px;width: 95%;font-size: 40rpx;" @click="change(item)">
							{{item.locCodeView}}
						</u-button>
					</u-col>
					<u-col span="3" v-if="!item.isEmpty && index>3">
						<u-button style="height: 60px;width: 95%;font-size: 40rpx;" disabled>{{item.locCodeView}}
						</u-button>
					</u-col>
				</template>
			</u-row>
			<u-row v-if="param.lpnType == 'C' ">
				<template v-for="(item, index) in locList">
					<u-col span="3" v-if="item.isEmpty &&  (item.locCodeView != '34-01' && item.locCodeView != '33-01')">
						<u-button style="height: 60px;width: 95%;font-size: 40rpx;" @click="change(item)">
							{{item.locCodeView}}
						</u-button>
					</u-col>
					<u-col span="3" v-if="!item.isEmpty && (item.locCodeView != '34-01' && item.locCodeView != '33-01')">
						<u-button style="height: 60px;width: 95%;font-size: 40rpx;" disabled>
							{{item.locCodeView}}
						</u-button>
					</u-col>
				</template>
			</u-row>
			<u-divider></u-divider>

		</view>


		<h4 align="center" style='background-color:#33cbcc;height: 70rpx;' class="font-in-page">箱码信息</h4>

		<view style="margin-top: 5%;" v-for="(item, index) in lpnItem.boxList">
			<u-row customStyle="margin-left: 5%;">
				<u-col span="8">
					<view style="font-size: 40rpx;">箱码:{{item.boxCode}}</view>
				</u-col>
				<u-col span="4">
					<view style="font-size: 40rpx;">数量:{{item.qty}}</view>
				</u-col>
			</u-row>
			<u-divider></u-divider>

		</view>
		<view class="footer">
			<view class="btn-cancle" @click="esc()">
				返回
			</view>
			<view class="btn-submit" @click="clickItem()">
				确定
			</view>
		</view>
	</view>
</template>

<script>
	import putWay from '@/api/inStock/callAgv.js'
	import barCodeService from '@/utils/barcodeFunc.js'
	import setting from '@/common/setting'
	import tool from '@/utils/tool.js'
	import keyboardListener from '@/components/keyboard-listener/keyboard-listener'
	export default {
		components: {
			keyboardListener
		},
		data() {
			return {
				navigationBarBackgroundColor: setting.customNavigationBarBackgroundColor,
				param: {
					lpnType: '',
					whId: 0,
				},
				lpnItem: '',
				locCode: '',
				locList: []
			}
		},
		onLoad: function(option) {

			var parse = JSON.parse(option.param)
			this.lpnItem = parse
			this.param.lpnType = this.lpnItem.lpnType
			this.param.whId = uni.getStorageSync('warehouse').whId
			putWay.findLocByLpnType(this.param).then(res => {
				this.locList = res.data
			})
			this.lpnItem['locId'] = 0
		},
		onUnload() {
			uni.$u.func.unRegisterScanner();
		},
		onBackPress(event) {
			// #ifdef APP-PLUS
			if (event.from === 'backbutton') {
				this.esc();
				return true;
			}
			// #endif
		},

		onShow() {
			uni.$u.func.registerScanner(this.scannerCallback);
		},
		methods: {
			esc() {
				uni.navigateBack({
					delta: 1
				});
			},
			change(item) {
				this.locCode = item.locCode
				this.lpnItem.locId = item.locId
			},

			clickItem() {
				var _this = this;
				uni.$u.throttle(function() {
					_this.submit()
				}, 1000)
			},
			submit() {
				var that = this
				putWay.callAgv(this.lpnItem).then(res => {
					that.$u.func.showToast({
						title: '操作成功'
					})
					this.esc()
				})
			},
			scannerCallback(no) {
				let item = barCodeService.parseBarcode(no)
				if (item.type == barCodeService.BarcodeType.Loc) {
					let param = this.locList.find(u => u.locCode === item.content);
					if (tool.isEmpty(param)) {
						this.$u.func.showToast({
							title: '扫描错误,库位信息不符'
						})
					}
					if (tool.isNotEmpty(param)) {
						this.locCode = item.content;
					}
				} else {
					this.$u.func.showToast({
						title: '无法识别,不支持的条码类型'
					})
					return
				}

			},
		}
	}
</script>

<style>

</style>
