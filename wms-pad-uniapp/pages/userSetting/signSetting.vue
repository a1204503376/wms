<template>
	<view class="parentClass">
		<br />
		<br />
		<br />
		<br />
		<br />
		<view>
			<view class="bigRound">
				<view class="smallRound" @click="onSign">
					<span>{{loginStatus === "1" ? "签到" : "签退"}}</span>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	import api from '@/api/user.js'
	import tool from "@/utils/tool.js"
	export default {
		data() {
			return {
				type: "",
				loginStatus: ""
			}
		},
		created() {
			this.getUserOnlineList();
			this.getUserRegisterList();
		},
		methods: {
			getUserOnlineList() {
				let user = {
					userId: this.$store.state.userId
				}
				api.userOnlineList(user).then((res) => {})
			},
			getUserRegisterList() {
				api.userRegisterList().then((res) => {
					if (tool.isEmpty(res.data)) {
						this.loginStatus = "1";
					} else {
						this.loginStatus = res.data[res.data.length-1].loginStatus;
					}
				})
			},
			// 1签到  0签退
			onSign() {
				api.sign(this.loginStatus, this.$store.state.accessToken, this.$store.state.userId).then((res) => {
					this.loginStatus === "1" ? this.loginStatus="0" : this.loginStatus="1";
				})
			},
		},
	}
</script>

<style lang="scss">
	@import 'signSetting.scss';
</style>
