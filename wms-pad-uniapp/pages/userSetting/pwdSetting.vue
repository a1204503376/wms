<template>
	<view @keyup.esc="esc">
		<view style="margin-top: 5%;margin-left:5%;margin-right: 5%;">
			<u--form labelPosition="left" :model="form" :rules="rules" ref="uForm">
				<u-form-item prop="userInfo.oldPassword" borderBottom ref="item1">
					<u--input v-model="form.userInfo.oldPassword" :focus="oldPwdFocus" border="none"
						placeholder="请输入原始密码" @confirm="oldPwd"></u--input>
				</u-form-item>
				<u-form-item prop="userInfo.newPassword" borderBottom ref="item1">
					<u--input v-model="form.userInfo.newPassword" :focus="newPwdFocus" border="none"
						placeholder="请输入新密码" @confirm="newPwd"></u--input>
				</u-form-item>
				<u-form-item prop="userInfo.newPassword1" borderBottom ref="item1">
					<u--input v-model="form.userInfo.newPassword1" border="none" :focus="newPwd1Focus"
						placeholder="请输入确认密码" @confirm="submit"></u--input>
				</u-form-item>
			</u--form>
		</view>

		<view class="footer">
			<view class="btn-cancle" @click="esc()">
				返回
			</view>
			<view class="btn-submit" @click="submit()" :throttleTime="1000">
				确定
			</view>
		</view>
	</view>
</template>
<script>
	import api from '@/api/user.js'
	import md5 from '@/utils/md5.js'
	export default {
		data() {
			return {
				oldPwdFocus: false,
				newPwdFocus: false,
				newPwd1Focus: false,
				form: {
					userInfo: {
						id: uni.getStorageSync('userInfo').user_id,
						oldPassword: '',
						newPassword: '',
						newPassword1: '',
					},
				},

				rules: {
					'userInfo.oldPassword': {
						type: 'string',
						required: true,
						message: '请输入原始密码',
						trigger: ['blur']
					},
					'userInfo.newPassword': {
						type: 'string',
						required: true,
						message: '请输入新密码',
						trigger: ['blur']
					},
					'userInfo.newPassword1': {
						type: 'string',
						required: true,
						message: '请输入确认密码',
						trigger: ['blur']
					},
				},

			};
		},
		methods: {
			esc() {
				this.navigateBack()
			},
			oldPwd() {
				uni.hideKeyboard(); //隐藏软键盘				
				this.newPwdFocus = true
			},
			newPwd() {
				uni.hideKeyboard(); //隐藏软键盘
				this.newPwdFocus = false
				this.newPwd1Focus = true
			},
			navigateBack() {
				this.$u.func.navigateBack();
			},
			submit() {
				uni.$u.throttle(function() {
					this.newPwd1Focus = false;
					this.$refs.uForm.validate().then(res => {
						api.updatePassword(
							this.form.userInfo.id,
							md5(this.form.userInfo.oldPassword),
							md5(this.form.userInfo.newPassword),
							md5(this.form.userInfo.newPassword1)).then(data => {
							this.navigateBack();
							})
				})
				}, 1000)
			}
		},
	};
</script>
<style lang="scss">
</style>
