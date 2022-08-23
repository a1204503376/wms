<template>
	<view @keyup.esc="esc">
		<u-navbar leftIconColor="#fff" @leftClick="esc()" :fixed="false" :autoBack="false"
			:bgColor="navigationBarBackgroundColor" title="接驳区拣货" titleStyle="color:#ffffff;font-size:21px"
			style="color:#ffffff;font-size:21px">
		</u-navbar>
		<u--form labelPosition="left">
			<u-form-item label="当前选择" :required="true" class="left-text-one-line" labelWidth="100">
				<u--input v-model="param.locCodeView" disabled></u--input>
			</u-form-item>
		</u--form>

		<view>
			<u-row>
				<template v-for="(item, index) in locList">
					<u-col span="3" v-if="index>=0 && index<4">
						<u-button style="height: 60px;font-size: 40rpx;" @click="change(item)">
							{{item.locCodeView}}
						</u-button>
					</u-col>
				</template>
			</u-row>

			<u-row>
				<template v-for="(item, index) in locList">
					<u-col span="3" v-if="index>=4 && index<8">
						<u-button style="height: 60px;font-size: 40rpx;" @click="change(item)">
							{{item.locCodeView}}
						</u-button>
					</u-col>
				</template>
			</u-row>
			<u-row>
				<template v-for="(item, index) in locList">
					<u-col span="3" v-if="index>=8 && index<12">
						<u-button style="height: 60px;font-size: 40rpx;" @click="change(item)">
							{{item.locCodeView}}
						</u-button>
					</u-col>
				</template>
			</u-row>
		</view>

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
	import barCodeService from '@/common/barcodeFunc.js'
	import setting from '@/common/setting'
	import pick from '@/api/picking/connectionAreaPicking.js'
	import tool from '@/utils/tool.js'
	export default {
		components: {},
		data() {
			return {
				navigationBarBackgroundColor: setting.customNavigationBarBackgroundColor,
				param: {
					locId: undefined,
					locCodeView: '',
					locCode: ''
				},
				lpnItem: '',
				locCode: '',
				locList: []
			}
		},
		onLoad: function(option) {
			let pickToLocList = uni.getStorageSync('pickToLocList');
			if (tool.isEmpty(pickToLocList)) {
				let params = {
					whId: uni.getStorageSync('warehouse').whId
				};
				pick.getConnectionAreaLocation(params).then(data => {
					this.locList = data.data;
					uni.setStorageSync('pickToLocList', data.data);
				})
			} else {
				this.locList = pickToLocList
			}
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
				this.param = item;
			},

			submit() {
				var _this = this;
				uni.$u.throttle(function() {
					pick.connectionAreaPicking(_this.param).then(data => {
						if (data.data) {
							_this.$u.func.showToast({
								title: '接驳区拣货成功'
							})
						} else {
							uni.$u.func.routeNavigateTo(
								'/pages/picking/connectionAreaPicking/connectionAreaMove', _this.param);
						}
					})
				}, 1000)
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
