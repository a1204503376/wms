<template>
    <div style="width: 100px;" v-if="tabs && tabs.length > 0 && tabs[0].label">
        <ul id="tabList">
            <template v-for="(item, index) in tabs">
                <li v-if="form.getHide(item)"
                    :class="{groupItemActive:tabIndex === index}"
                    @click="changeTab(index)">{{ item.label }}<i></i>
                </li>
            </template>
        </ul>
    </div>
</template>

<script>
export default {
    name: "form-tab",
    inject: ["form"],
    provide() {
        return {
            form: this.form,
            dynamic: this
        }
    },
    props: {
        tabs: {type: Array, default: () => []}
    },
    data() {
        return {
            tabIndex: 0,
            tableOffsetTop: [],
            // 区分是点击切换还是滚动
            isHandelScroll: false,
        }
    },
    mounted() {
        // 记录tab位置
        let tabs = document.querySelectorAll('#content .tabs-item');
        if (tabs) {
            this.tabOffsetTop = [];
            tabs.forEach(item => {
                this.tabOffsetTop.push(item.offsetTop);
            });
        }
    },
    methods: {
        // 切换左侧tab
        changeTab(index) {
            this.isHandelScroll = true;
            this.tabIndex = index;
            document.querySelector('#form-content').scrollTop = this.tabOffsetTop[index];

            setTimeout(() => {
                this.isHandelScroll = false;
            }, 200)
        },
    }
}
</script>

<style>
.groupItemActive {
    color: #409EFF;
    font-weight: bold;
}
</style>
