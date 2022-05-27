<template>
	<view>
		<view class="user-box">
			<view class="user">
				<img src="/static/images/login.png" />
			</view>
			<view class="user-name" format="yyyy/MM/dd" style=" font-weight: bolder">用户名：{{ username }}
				<br /><br />登录时间:{{loginTime}}
			</view>
		</view>
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
		data() {
			return {
				username: uni.getStorageSync('username'),
				loginTime: uni.getStorageSync('loginTime')
			}
		},
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
