<template>
	<view class="u-page">
		<u-row customStyle="margin-bottom: 10px;margin-top:30px">
			<u-col span="4">
				<view  class="demo-layout img">
					<u--image shape="circle" width="80px" height="80px" src="/static/images/login.png"></u--image>
				</view>
			</u-col>
			<u-col span="4">
				<view class="demo-layout user-name">
					{{ userName }}</view>
			</u-col>
			<u-col span="2">
				<view class="demo-layout bg-purple-font">
					{{signStatus === 1 ? "已签到" : "未签到"}}</view>
			</u-col>

		</u-row>
		<u-row>
			<u-col span="12">
		 	<view class="demo-layout login-time">登录时间:{{ loginTime }}</view>
			</u-col>

		</u-row>

		<view style="margin-left: 10%;margin-right: 10%;">
			<br /><br />
			<u--form>
				<u-form-item borderBottom labelWidth="60" @click="toPwdSetting">
					<u-icon name="edit-pen" size="28"></u-icon>
					<text style="font-size: 100%;">修改密码</text>
				</u-form-item>
			</u--form>
			<u--form>
				<u-form-item borderBottom labelWidth="60" @click="sign">
					<u-icon name="calendar-fill" size="28"></u-icon>
					<text style="font-size: 100%;">签到</text>
				</u-form-item>
			</u--form>
		</view>
	</view>
</template>
<script>
	export default {
		methods: {
			toPwdSetting() {
				uni.$u.func.route('/pages/userSetting/pwdSetting');
			},
			sign() {
				uni.$u.func.route('/pages/userSetting/signSetting');
			}
		}
	}
</script>

<style lang="scss">
	@import 'userSetting.scss';
</style>
