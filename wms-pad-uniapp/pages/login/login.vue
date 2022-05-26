<template>
	<view class="content">
		<u-toast ref="uToast" />
		<view class="top">
			<view class="logodiv">
				<image :src="logoImgSrc" style="width: 100px;height: 100px;" mode="widthFix"></image>
				<h2>{{name}}</h2>
				<h5>{{pdaVersion}}</h5>
			</view>

			<view>
				<u--form>
					<u-form-item>
						<template>
							<u-icon size="40" name="account-fill"></u-icon>
							<u--input placeholder="请输入账号" v-model="username" border="none" :focus="userNameFocus"
								@confirm="nextFocusToPassword"></u--input>
						</template>
					</u-form-item>
					<u-form-item>
						<template>
							<u-icon size="40" name="lock-fill"></u-icon>
							<u--input placeholder="请输入密码" type="password" v-model="password" border="none"
								:focus="passwordFocus" @confirm="submit()"></u--input>
						</template>
					</u-form-item>
				</u--form>
			</view>
			<view>
				<button class="submit_button" @click="submit">登录</button>
				<button class="quit" @click="gotoAddress">配置</button>
				<button class="quit" @click="quitApp">退出</button>
			</view>

		</view>
	</view>
</template>

<script>
	import setting from '@/common/setting'
	import md5 from '@/utils/md5.js'
	import api from '@/api/user.js'
	import tool from '@/utils/tool.js'
	import {
		options
	} from '@/http/config.js';

	export default {
		data() {
			return {
				tenantId: setting.tenantId,
				username: uni.getStorageSync('username') || 'admin',
				// TODO 测试时默认密码,正式需要删除,上面的admin
				password: '123456',
				addressDisplay: true,
				name: setting.name,
				pdaVersion: setting.version,
				userNameFocus: false,
				passwordFocus: false,
				logoImgSrc: setting.logo
			};
		},
		onLoad() {
			let subNVue = uni.getSubNVueById('honeywellScannerComponent');
			subNVue.hide();
		},
		onUnload() {
			uni.$u.func.unRegisterScanner();
		},
		onShow() {
			uni.$u.func.registerScanner(this.scannerCallback);
		},
		methods: {
			nextFocusToPassword() {
				uni.hideKeyboard(); //隐藏软键盘				
				this.passwordFocus = true;
			},
			scannerCallback(data) {
				let userInfoList = data.split('@');
				if (userInfoList.length != 2) {
					this.username = data
					return
				}
				this.username = userInfoList[0];
				this.password = userInfoList[1];
			},
			submit() {
				if (tool.isEmpty(this.username) || tool.isEmpty(this.password)) {
					this.$u.func.showToast({
						title: '登录失败,用户名或密码不能为空',
					})
					return;
				}
				uni.showLoading({
					title: '登录中'
				})
				api.token(this.tenantId, this.username, md5(this.password), this.type).then(data => {
					uni.$u.func.login(data);
				})
			},
			quitApp() {
				plus.runtime.quit();
			},
			gotoAddress() {
				uni.navigateTo({
					url: '/pages/reviseIp/reviseIp'
				});

			}
		}
	};
</script>

<style lang="scss">
	@import 'login.scss';
	@import '@/static/common.scss';
</style>
