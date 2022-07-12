<template>
	<view class="u-page">
		<u-navbar leftIconColor="#fff" @leftClick="esc" :fixed="false" :autoBack="false"
			:bgColor="navigationBarBackgroundColor" title="个人中心" titleStyle="color:#ffffff;font-size:21px"
			style="color:#ffffff;font-size:21px">
		</u-navbar>
		<u-row customStyle="margin-bottom: 10px;margin-top:30px">
			<u-col span="4">
				<view class="demo-layout img">
					<u--image shape="circle" width="80px" height="80px" src="/static/images/login.png"></u--image>
				</view>
			</u-col>
			<u-col span="4">
				<view class="demo-layout user-name">
					{{ userName }}
				</view>
			</u-col>
			<u-col span="2">
				<view class="demo-layout bg-purple-font">
					{{signStatus === 1 ? "已签到" : "未签到"}}
				</view>
			</u-col>


		</u-row>
		<u-row>
			<u-col span="12">
				<view class="demo-layout login-time">登录时间:{{ loginTime }}</view>
			</u-col>

		</u-row>

		<view class="pwd">
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
			<u--form>
				<u-form-item borderBottom labelWidth="60" @click="setWarehouse">
					<u-icon name="home-fill" size="28"></u-icon>
					<text style="font-size: 100%;">切换库房</text>
				</u-form-item>
			</u--form>
		</view>

		<view class="footer">
			<view class="btn-submit" @click="logout">退出登录</view>
		</view>
	</view>
</template>
<script>
	import setting from '@/common/setting'
	export default {
		data() {
			return {
				navigationBarBackgroundColor: setting.customNavigationBarBackgroundColor,
				show: false,
			};
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
			logout() {
				this.$u.func.logout();
			},
			toPwdSetting() {
				uni.$u.func.routeNavigateTo('/pages/userSetting/pwdSetting');
			},
			sign() {
				uni.$u.func.routeNavigateTo('/pages/userSetting/signSetting', {
					path: '/pages/userSetting/userSetting'
				});
			},
			setWarehouse() {
				uni.$u.func.routeNavigateTo('/pages/userSetting/warehouseSetting');
			}

		}
	}
</script>

<style lang="scss">
	@import 'userSetting.scss';
</style>
