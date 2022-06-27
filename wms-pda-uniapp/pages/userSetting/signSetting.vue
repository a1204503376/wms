<template>
	<view class="parentClass">
		<view>
			<view class="bigRound">
				<view class="smallRound" @click="editUserLoginStatus" >
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
	import api from '@/api/user.js'
	import tool from "@/utils/tool.js"
	export default {	
		methods: {
			// 1签到  0签退
			editUserLoginStatus() {
				api.editUserLoginStatus(this.signStatus,this.$store.state.accessToken)
				.then((res) => {
					this.$u.vuex('signStatus', res.data.loginStatus);
					 this.$u.vuex('lastSignTime', res.data.lastLoginTime);
					
				})
			},
		},
	}
</script>

<style lang="scss">
	@import 'signSetting.scss';
</style>
