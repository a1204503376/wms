<template>

	<view>
		<view class="user-box">
			<view class="user">
				<img src="/static/images/login.png" />
			</view>
			<view class="user-name" format="yyyy/MM/dd" style=" font-weight: bolder">用户名：{{ username }}
				<br /><br />登录时间:{{loginTime}}</view>

		</view>


		<view class="cell" style="margin-left: 10%;margin-right: 10%;">

			<div>
				<br /><br />
				<u--form>
					<u-form-item borderBottom labelWidth="60" @click="submit">
						<u-icon name="edit-pen" size="28"></u-icon>
						<text style="font-size: 100%;">修改密码</text>
					</u-form-item>
				</u--form>


			</div>
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
		onLoad() {
			uni.$u.func.registerScanner(this.scannerCallback);
		},
		onUnload() {
			uni.$u.func.unRegisterScanner();
		},
		methods: {
			scannerCallback(data) {
				this.address = data
			},
			submit() {
				uni.$u.func.route('/pages/userSetting/pwdSetting');
			}
		}
	}
</script>

<style lang="scss">
	@import 'userSetting.scss';
</style>
