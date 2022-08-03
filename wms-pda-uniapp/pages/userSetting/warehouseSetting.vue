<template>
	<view>
		<u-navbar leftIconColor="#fff" @leftClick="esc" :fixed="false" :autoBack="false"
			:bgColor="navigationBarBackgroundColor" title="切换库房" titleStyle="color:#ffffff;font-size:21px"
			style="color:#ffffff;font-size:21px">
		</u-navbar>
		<view class="uni-list">
			<radio-group @change="radioChange" :model="whId" style="transform: scale(0.9);">
				<label class="uni-list-cell uni-list-cell-pd" v-for="(item, index) in warehouseList" :key="item.whId">
					<view class="font-in-page">{{item.whName}}</view>
					<view>
						<radio :value="item" :checked="warehouse.whId==item.whId" />
					</view>
				</label>
			</radio-group>
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
	import setting from '@/common/setting'
	import api from '@/api/user.js'
	import md5 from '@/utils/md5.js'
	import warehouse from '@/api/warehouse.js'
	import tool from '@/utils/tool.js'
	export default {
		data() {
			return {
				navigationBarBackgroundColor: setting.customNavigationBarBackgroundColor,
				warehouseList: [],
				warehouse: uni.getStorageSync('warehouse'),
				whName: uni.getStorageSync('warehouse').whName,
				whId: uni.getStorageSync('warehouse').whId
			};
		},
		onLoad: function(option) {
			var warehouseList = uni.getStorageSync('warehouseList');
			if (tool.isNotEmpty(warehouseList)) {
				this.warehouseList = uni.getStorageSync('warehouseList');
			} else {
				var parse = JSON.parse(option.param)
				this.warehouseList = parse;
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
			esc() {
				uni.$u.func.navigateBackTo(1);
			},
			submit() {
				uni.setStorageSync('warehouse', this.warehouse);
				uni.$u.func.routeReLaunch('/pages/home/home');
			},
			radioChange(row) {
				this.warehouse = row.detail.value;
			}
		},
	};
</script>
<style lang="scss">
	@import 'warehouseSetting.scss';
</style>
