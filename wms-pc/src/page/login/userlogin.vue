<template>
    <el-form class="login-form"
             status-icon
             :rules="loginRules"
             ref="loginForm"
             :model="loginForm"
             label-width="0">
        <el-form-item v-if="tenantMode" prop="tenantId">
            <el-input size="small"
                      @keyup.enter.native="handleLogin"
                      v-model="loginForm.tenantId"
                      auto-complete="off"
                      :placeholder="$t('login.tenantId')">
                <i slot="prefix" class="icon-quanxian"/>
            </el-input>
        </el-form-item>
        <el-form-item prop="username">
            <el-input size="small"
                      @keyup.enter.native="handleLogin"
                      v-model="loginForm.username"
                      auto-complete="off"
                      :placeholder="$t('login.username')">
                <i slot="prefix" class="icon-yonghu"/>
            </el-input>
        </el-form-item>
        <el-form-item prop="password">
            <el-input size="small"
                      @keyup.enter.native="handleLogin"
                      :type="passwordType"
                      v-model="loginForm.password"
                      auto-complete="off"
                      :placeholder="$t('login.password')">
                <i class="el-icon-view el-input__icon" slot="suffix" @click="showPassword"/>
                <i slot="prefix" class="icon-mima"/>
            </el-input>
        </el-form-item>
        <el-form-item v-if="this.website.captchaMode" prop="code">
            <el-row :span="24">
                <el-col :span="16">
                    <el-input size="small"
                              @keyup.enter.native="handleLogin"
                              v-model="loginForm.code"
                              auto-complete="off"
                              :placeholder="$t('login.code')">
                        <i slot="prefix" class="icon-yanzhengma"/>
                    </el-input>
                </el-col>
                <el-col :span="8">
                    <div class="login-code">
                        <img :src="loginForm.image" class="login-code-img" @click="refreshCode"
                        />
                    </div>
                </el-col>
            </el-row>
        </el-form-item>
        <el-form-item>
            <el-button type="primary"
                       size="small"
                       @click.native.prevent="handleLogin"
                       class="login-submit">{{ $t('login.submit') }}
            </el-button>
        </el-form-item>
    </el-form>
</template>

<script>
import {mapGetters} from "vuex";
import {info} from "@/api/core/tenant";
import {getCaptcha} from "@/api/user";
import {getTopUrl} from "@/util/util";

export default {
    name: "userlogin",
    data() {
        return {
            tenantMode: this.website.tenantMode,
            loginForm: {
                //??????ID
                tenantId: "000000",
                //?????????
                username: "",
                //??????
                password: "",
                //????????????
                type: "account",
                //???????????????
                code: "",
                //??????????????????
                key: "",
                //?????????????????????
                image: "data:image/gif;base64,R0lGODlhAQABAIAAAAAAAP///yH5BAEAAAAALAAAAAABAAEAAAIBRAA7",
            },
            loginRules: {
                tenantId: [
                    {required: false, message: "???????????????ID", trigger: "blur"}
                ],
                username: [
                    {required: true, message: "??????????????????", trigger: "blur"}
                ],
                password: [
                    {required: true, message: "???????????????", trigger: "blur"},
                    {min: 1, message: "?????????????????????6???", trigger: "blur"}
                ]
            },
            passwordType: "password"
        };
    },
    created() {
        this.getTenant();
        this.refreshCode();
    },
    mounted() {
    },
    computed: {
        ...mapGetters(["tagWel"])
    },
    props: [],
    methods: {
        refreshCode() {
            getCaptcha().then(res => {
                const data = res.data;
                this.loginForm.key = data.key;
                this.loginForm.image = data.image;
            })
        },
        showPassword() {
            this.passwordType === ""
                ? (this.passwordType = "password")
                : (this.passwordType = "");
        },
        handleLogin() {
            this.$refs.loginForm.validate(valid => {
                if (valid) {
                    const loading = this.$loading({
                        lock: true,
                        text: '?????????,??????????????????',
                        spinner: "el-icon-loading"
                    });
                    this.$store.dispatch("LoginByUsername", this.loginForm).then(() => {
                        this.$router.push({path: this.tagWel.value});
                        loading.close();
                    }).catch(() => {
                        loading.close();
                        this.refreshCode();
                    });
                }
            });
        },
        getTenant() {
            let domain = getTopUrl();
            // ?????????????????????????????????
            //domain = "https://bladex.vip";
            info(domain).then(res => {
                const data = res.data;
                if (data.success && data.data.tenantId) {
                    this.tenantMode = false;
                    this.loginForm.tenantId = data.data.tenantId;
                    this.$parent.$refs.login.style.backgroundImage = `url(${data.data.backgroundUrl})`;
                }
            })
        }
    }
};
</script>

<style>
</style>
