<template>
	<view class="parentClass">
		<u-navbar leftIconColor="#fff" @leftClick="esc" :fixed="false" :autoBack="false"
			:bgColor="navigationBarBackgroundColor" title="签到" titleStyle="color:#ffffff;font-size:21px"
			style="color:#ffffff;font-size:21px">
		</u-navbar>
		<view>
			<view class="bigRound">
				<view class="smallRound" @click="editUserLoginStatus">
					<span>{{signStatus === 1 ? "签退" : "签到"}}</span>
				</view>

			</view>
			<view class="sing-status">
				{{signStatus === 1 ? "最后签到时间:" : "最后签退时间:"}}{{lastSignTime}}
			</view>
		</view>
	</view>
</template>

<script>
	import setting from '@/common/setting'
	import api from '@/api/user.js'
	import tool from "@/utils/tool.js"
	export default {
		data() {
			return {
				navigationBarBackgroundColor: setting.customNavigationBarBackgroundColor,
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
		onLoad: function(option) {
			var parse = JSON.parse(option.param)
			this.path = parse.path;
		},
		methods: {
			// 1签到  0签退
			editUserLoginStatus() {
				api.editUserLoginStatus(this.signStatus, this.$store.state.accessToken)
					.then((res) => {
						this.$u.vuex('signStatus', res.data.loginStatus);
						this.$u.vuex('lastSignTime', res.data.lastLoginTime);
					})
			},
			esc() {
				uni.navigateBack({
					delta: 1
				});
			},
		},
	}
</script>

<style lang="scss">
	@import 'signSetting.scss';
</style>
