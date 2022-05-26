<template>
	<view>
		<!-- <u-navbar  @leftClick="navigateBack" : :fixed="false" :autoBack="false" title="修改密码" >
		    </u-navbar> -->
	<view style="margin-top: 5%;margin-left:5%;margin-right: 5%;">
		<u--form labelPosition="left" :model="form" :rules="rules" ref="uForm">
			<u-form-item prop="userInfo.oldPassword" borderBottom ref="item1">
				<u--input v-model="form.userInfo.oldPassword" :focus="oldPwdFocus" border="none" placeholder="请输入原始密码" @confirm="oldPwd"></u--input>
			</u-form-item>
			<u-form-item prop="userInfo.newPassword" borderBottom ref="item1">
				<u--input v-model="form.userInfo.newPassword" :focus="newPwdFocus" border="none" placeholder="请输入新密码" @confirm="newPwd" ></u--input>
			</u-form-item>
			<u-form-item prop="userInfo.newPassword1" borderBottom ref="item1">
				<u--input v-model="form.userInfo.newPassword1" border="none" :focus="newPwd1Focus" placeholder="请输入确认密码" @confirm="submit"></u--input>
			</u-form-item>

		</u--form>
	</view>
		<view >
		
			<u-button class="bt-1" type="primary" @click="navigateBack" text="返回[Esc]"></u-button>
			<u-button class="bt-2" type="primary"  @click="submit" text="确认[Ent]"></u-button>
		</view>

	</view>
</template>
<script>
	import api from '@/api/user.js'
	import md5 from '@/utils/md5.js'
	export default {
		data() {
			return {
				oldPwdFocus:false,
				newPwdFocus:false,
				newPwd1Focus:false,
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
			oldPwd(){
			uni.hideKeyboard();	//隐藏软键盘				
			this.newPwdFocus = true
			},
			newPwd(){
				uni.hideKeyboard();	//隐藏软键盘
				this.newPwdFocus = false
				this.newPwd1Focus = true
			},
				navigateBack(){
							uni.navigateBack({
								delta:1,//返回层数，2则上上页
							})
						},
			submit() {
                 this.newPwd1Focus = false;
				this.$refs.uForm.validate().then(res => {
					api.updatePassword(
						this.form.userInfo.id,
						md5(this.form.userInfo.oldPassword),
						md5(this.form.userInfo.newPassword),
						md5(this.form.userInfo.newPassword1)).then(data => {
							
					this.$u.func.showToast({title: '修改密码成功，请重新登录',duration:3000})
						 this.$u.func.logout()

						
					})
				})
			}
		},
	};
</script>
<style>
	.bt-1 {
		width: 50%;
		position: fixed;
		bottom: 0;
		background-color: #33FFFF;

	}

	.bt-2 {
		width: 50%;
		position: fixed;
		bottom: 0;
		margin-left: 49%;
		background-color: #00BBFF;

	}
</style>
