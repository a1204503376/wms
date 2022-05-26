<template>
	<view>
		<!-- <u-navbar  @leftClick="navigateBack" : :fixed="false" :autoBack="false" title="修改密码" >
		    </u-navbar> -->
	<view style="margin-top: 5%;margin-left:5%;margin-right: 5%;">
		<u--form labelPosition="left" :model="form" :rules="rules" ref="uForm">
			<u-form-item prop="userInfo.oldPassword" borderBottom ref="item1">
				<u--input v-model="form.userInfo.oldPassword" border="none" placeholder="请输入原始密码" @confirm="doSearch"></u--input>
			</u-form-item>
			<u-form-item prop="userInfo.newPassword" borderBottom ref="item1">
				<u--input v-model="form.userInfo.newPassword" border="none" placeholder="请输入新密码" ></u--input>
			</u-form-item>
			<u-form-item prop="userInfo.newPassword1" borderBottom ref="item1">
				<u--input v-model="form.userInfo.newPassword1" border="none" placeholder="请输入确认密码"></u--input>
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
			doSearch(){
				
             let inputs =uni.createSelectorQuery("#input");
				let b = JSON.stringify(inputs) 

				         
				                    inputs[1].focus();
				               
				                 
				     
				alert(b)
			},
				navigateBack(){
							uni.navigateBack({
								delta:1,//返回层数，2则上上页
							})
						},
			submit() {
      // uni.$u.func.showToast({title: '登录失败,用户名或密码不能为空'})
				// api.updatePassword(this.form.userInfo.id, this.form.userInfo.oldPassword,this.form.userInfo.newPassword, this.form.userInfo.newPassword1).then(data => {
				// 	this.$u.func.logout()
				// 	 uni.$u.func.route('/pages/login/login');
				// })
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
