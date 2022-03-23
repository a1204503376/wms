<template>
    <div class="avue-logo">
        <img class="avatar" v-if="this.$store.getters.userInfo.role_name != 'developer'" :src="imageUrl"
             style="height:56px;line-height:56px;"/>
        <el-upload
            v-else
            with-credentials
            class="avatar-uploader"
            action="/api/blade-system/resource/put-file"
            :show-file-list="false"
            :data="{name:'logo'}"
            :on-success="handleAvatarSuccess"
            :before-upload="beforeAvatarUpload">
            <img v-if="imageUrl" :src="imageUrl" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
        </el-upload>
    </div>
</template>

<script>
import {mapGetters} from "vuex";
import {getFile} from "@/api/core/resource";

export default {
    name: "logo",
    data() {
        return {
            imageUrl: ''
        };
    },
    created() {
        getFile({name: 'logo'}).then(res => {
            if (res.data.data && res.data.data.length > 0) {
                this.imageUrl = "data:image/jpeg;base64," + res.data.data;
            } else {
                this.imageUrl = "wms-logo.png"
            }
        });
    },
    computed: {
        ...mapGetters(["website", "keyCollapse"])
    },
    methods: {
        handleAvatarSuccess(res, file) {
            this.imageUrl = URL.createObjectURL(file.raw);
        },
        beforeAvatarUpload(file) {
            const isJPG = file.type === 'image/jpeg' || file.type === 'image/png';
            const isLt2M = file.size / 1024 / 1024 < 2;

            if (!isJPG) {
                this.$message.error('上传logo图片只能是 JPG/PNG 格式!');
            }
            if (!isLt2M) {
                this.$message.error('上传logo图片大小不能超过 2MB!');
            }
            return isJPG && isLt2M;
        }
    }
};
</script>

<style lang="scss">

.avatar-uploader {
    //text-align: center;
}

.avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    width: 200px;
    text-align: left;
}

.avatar-uploader .el-upload:hover {
    border-color: #409EFF;
}

.avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 235px;
    height: 50px;
    line-height: 50px;
    //text-align: center;
}

.avatar {
    height: 50px;
}

.avue-logo {
    position: fixed;
    top: 0;
    left: 0;
    width: 240px;
    padding-top: 4px;
    //height: 100px;
    //line-height: 100px;
    background-color: #20222a;
    font-size: 20px;
    overflow: hidden;
    box-sizing: border-box;
    padding-left: 20px;
    box-shadow: 0 1px 2px 0 rgba(0, 0, 0, 0.15);
    color: rgba(255, 255, 255, 0.8);
    z-index: 1024;

    &_title {
        display: block;
        //text-align: center;
        font-weight: 300;
        font-size: 20px;
    }

    &_subtitle {
        display: block;
        //text-align: center;
        font-size: 18px;
        font-weight: bold;
        color: #fff;
    }
}
</style>
