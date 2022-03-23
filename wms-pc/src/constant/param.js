/**
 * 系统参数
 * @type {{system: {lpnEnable: string}}}
 */
export const param = {
    // 系统参数
    system: {
        // 是否启用容器
        lpnEnable: "system::lpnEnable",
        // PDA部署的URL
        pdaUrl: "system.pda.url",
        // 批属性开放数量
        lotCount:'account:lotCount',
        //分配策略切换模式
        allocMode:'strategy.alloc.mode',
    },
    label: {
        fields: "label.fields"
    }
}
